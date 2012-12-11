package com.interzonedev.requestr.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import ch.qos.logback.classic.Logger;

/**
 * An {@link RequestrService} implementation using the Apache HTTP Components library.
 * 
 * @author mark@interzonedev.com
 */
@Named("requestrService")
public class RequestrServiceImpl implements RequestrService {

	private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

	@Inject
	@Named("httpClient")
	protected DefaultHttpClient httpClient;

	@Inject
	@Named("keystoreFilePath")
	private String keystoreFilePath;

	@Inject
	@Named("keystorePassword")
	private String keystorePassword;

	@PostConstruct
	public void init() {
		addSslScheme();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.interzonedev.requestr.service.RequestrService#doRequest(com.interzonedev.requestr.service.RequestrRequest)
	 */
	@Override
	public RequestrResponse doRequest(RequestrRequest requestrRequest) throws ClientProtocolException, IOException {

		log.debug("doRequest: Starting request - " + requestrRequest);

		// Assemble the HTTP request from the request value object.
		HttpRequestBase httpRequestBase = getHttpRequestBaseFromRequest(requestrRequest);

		log.debug("doRequest: Sending HTTP request");

		// Send the HTTP request.
		HttpResponse httpResponse = httpClient.execute(httpRequestBase);

		log.debug("doRequest: Received HTTP response");

		// Assemble the response value object from the HTTP response.
		RequestrResponse requestrResponse = getRequestrResponseFromHttpResponse(requestrRequest, httpResponse);

		log.debug("doRequest: Assembled response - " + requestrResponse);

		return requestrResponse;

	}

	/**
	 * Assemble the {@link HttpRequestBase} instance that represents the HTTP request from the {@link RequestrRequest}
	 * value object.
	 * 
	 * @param request
	 *            The {@link RequestrRequest} value object that contains the components of the {@link HttpRequestBase}
	 *            to assemble.
	 * 
	 * @return Returns an {@link HttpRequestBase} instance that represents the HTTP request from the
	 *         {@link RequestrRequest} value object.
	 */
	private HttpRequestBase getHttpRequestBaseFromRequest(RequestrRequest request) {

		RequestrMethod method = request.getMethod();

		List<NameValuePair> requestNameValuePairs = getNameValuePairsFromRequestParameters(request.getParameters());

		String url = addQueryStringToUrl(method, request.getUrl(), requestNameValuePairs);

		HttpRequestBase httpRequestBase = getRawHttpRequestBaseFromMethod(method, url);

		addRequestHeadersToHttpRequestBase(httpRequestBase, request.getHeaders());

		addRequestParametersToRequestBody(httpRequestBase, method, requestNameValuePairs);

		return httpRequestBase;

	}

	/**
	 * Turns the specified map of request parameters into a list of {@link NameValuePair}s. Request parameters with
	 * multiple values result in corresponding multiple {@link NameValuePair} instances.
	 * 
	 * @param requestParameters
	 *            A map of request parameters name/value pairs.
	 * 
	 * @return Returns a list of {@link NameValuePair}s that correspond to the request parameter name/value pairs.
	 */
	private List<NameValuePair> getNameValuePairsFromRequestParameters(Map<String, List<String>> requestParameters) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		if ((null != requestParameters) && !requestParameters.isEmpty()) {
			for (String parameterName : requestParameters.keySet()) {
				List<String> parameterValues = requestParameters.get(parameterName);
				for (String parameterValue : parameterValues) {
					nameValuePairs.add(new BasicNameValuePair(parameterName, parameterValue));
				}
			}
		}

		return nameValuePairs;
	}

	/**
	 * For all request {@link RequestrMethod}s except for {@link RequestrMethod#POST} and {@link RequestrMethod#PUT},
	 * transforms the specified request parameter name/value pairs into a query string and appends it to the specified
	 * url.
	 * 
	 * @param method
	 *            The {@link RequestrMethod} of the HTTP request.
	 * @param url
	 *            The url of the HTTP request.
	 * @param requestNameValuePairs
	 *            A list of {@link NameValuePair}s that correspond to the request parameter name/value pairs.
	 * 
	 * @return Returns the specified url with a query string appended if there are request parameter name/value pairs
	 *         and the request {@link RequestrMethod} is not {@link RequestrMethod#POST} or {@link RequestrMethod#PUT}.
	 */
	private String addQueryStringToUrl(RequestrMethod method, String url, List<NameValuePair> requestNameValuePairs) {

		String alteredUrl = url;

		switch (method) {
			case POST:
			case PUT:
				break;
			default:
				String queryString = URLEncodedUtils.format(requestNameValuePairs, "utf-8");
				if (!alteredUrl.contains("?")) {
					alteredUrl += "?";
				} else if (StringUtils.isNotBlank(queryString)) {
					alteredUrl += "&";
				}
				alteredUrl += queryString;
		}

		return alteredUrl;

	}

	/**
	 * Gets an instance of {@link HttpRequestBase} according to the specified {@link RequestrMethod} initialized with
	 * the specified url. The request headers and body are not set.
	 * 
	 * @param method
	 *            The {@link RequestrMethod} of the HTTP request.
	 * @param url
	 *            The url of the HTTP request.
	 * 
	 * @return Returns an instance of {@link HttpRequestBase} according to the specified {@link RequestrMethod}
	 *         initialized with the specified url.
	 */
	private HttpRequestBase getRawHttpRequestBaseFromMethod(RequestrMethod method, String url) {

		switch (method) {
			case GET:
				return new HttpGet(url);
			case POST:
				return new HttpPost(url);
			case PUT:
				return new HttpPut(url);
			case DELETE:
				return new HttpDelete(url);
			case OPTIONS:
				return new HttpOptions(url);
			case HEAD:
				return new HttpHead(url);
			case TRACE:
				return new HttpTrace(url);
			default:
				throw new RuntimeException("Unsupported request method: " + method);
		}

	}

	/**
	 * Sets the specfied request headers on the specified {@link HttpRequestBase}.
	 * 
	 * @param httpRequestBase
	 *            The {@link HttpRequestBase} that represents the HTTP request.
	 * @param requestHeaders
	 *            A map of request header name/value pairs.
	 */
	private void addRequestHeadersToHttpRequestBase(HttpRequestBase httpRequestBase,
			Map<String, List<String>> requestHeaders) {

		if ((null != requestHeaders) && !requestHeaders.isEmpty()) {
			for (String requestHeaderName : requestHeaders.keySet()) {
				List<String> requestHeaderValues = requestHeaders.get(requestHeaderName);
				for (String requestHeaderValue : requestHeaderValues) {
					httpRequestBase.addHeader(requestHeaderName, requestHeaderValue);
				}
			}
		}

	}

	/**
	 * For {@link RequestrMethod#POST} and {@link RequestrMethod#PUT} sets the request parameter name/value pairs in the
	 * body of the specified {@link HttpRequestBase}.
	 * 
	 * @param httpRequestBase
	 *            The {@link HttpRequestBase} that represents the HTTP request.
	 * @param method
	 *            The {@link RequestrMethod} of the HTTP request.
	 * @param requestNameValuePairs
	 *            A list of {@link NameValuePair}s that correspond to the request parameter name/value pairs.
	 */
	private void addRequestParametersToRequestBody(HttpRequestBase httpRequestBase, RequestrMethod method,
			List<NameValuePair> requestNameValuePairs) {

		if (!requestNameValuePairs.isEmpty()) {
			switch (method) {
				case POST:
				case PUT:
					UrlEncodedFormEntity requestBodyEntity;
					try {
						requestBodyEntity = new UrlEncodedFormEntity(requestNameValuePairs, "utf-8");
					} catch (UnsupportedEncodingException uee) {
						String errorMessage = "Error creating request body";
						log.error("doRequest: " + errorMessage, uee);
						throw new RuntimeException(errorMessage, uee);
					}
					((HttpEntityEnclosingRequestBase) httpRequestBase).setEntity(requestBodyEntity);
					break;
			}
		}

	}

	/**
	 * Assembles a {@link RequestrResponse} value object from the specified {@link HttpResponse}.
	 * 
	 * @param request
	 *            The {@link RequestrRequest} value object that represents the originiating HTTP request.
	 * @param httpResponse
	 *            The {@link HttpResponse} that represents the response to transform into a {@link RequestrResponse}.
	 * 
	 * @return Returns a {@link RequestrResponse} value object assembled from the components of the specified
	 *         {@link HttpResponse}.
	 * 
	 * @throws ParseException
	 *             Thrown if there was an error turning the response body into a string.
	 * @throws IOException
	 *             Thrown if there was an error turning the response body into a string.
	 */
	private RequestrResponse getRequestrResponseFromHttpResponse(RequestrRequest request, HttpResponse httpResponse)
			throws ParseException, IOException {

		HttpEntity responseEntity = httpResponse.getEntity();

		int statusCode = httpResponse.getStatusLine().getStatusCode();

		String contentType = null;
		Header contentTypeHeader = responseEntity.getContentType();
		if (null != contentTypeHeader) {
			contentType = responseEntity.getContentType().getValue();
		}

		long contentLength = responseEntity.getContentLength();

		Map<String, Cookie> cookies = getCookiesFromResponse(httpResponse);

		Map<String, List<String>> responseHeaders = getResponseHeaders(httpResponse);

		String responseContent = EntityUtils.toString(responseEntity);

		Locale locale = httpResponse.getLocale();

		return new RequestrResponse(request, statusCode, contentType, contentLength, responseHeaders, cookies,
				responseContent, locale);

	}

	/**
	 * Gets the headers from the specified {@link HttpResponse} and turns them into a map of header names to lists of
	 * header values.
	 * 
	 * @param httpResponse
	 *            The {@link HttpResponse} that represents the response from which to get the headers.
	 * 
	 * @return Returns a map of header names to lists of header values.
	 */
	private Map<String, List<String>> getResponseHeaders(HttpResponse httpResponse) {

		Map<String, List<String>> responseHeaders = new HashMap<String, List<String>>();

		Header[] responseHeaderValues = httpResponse.getAllHeaders();
		for (Header responseHeaderValue : responseHeaderValues) {
			String reponseHeaderName = responseHeaderValue.getName();
			String reponseHeaderValue = responseHeaderValue.getValue();

			List<String> headerValues = responseHeaders.get(reponseHeaderName);
			if (null == headerValues) {
				headerValues = new ArrayList<String>();
				responseHeaders.put(reponseHeaderName, headerValues);
			}
			headerValues.add(reponseHeaderValue);
		}

		return responseHeaders;

	}

	/**
	 * Gets a map of cookie names to {@link Cookie} instances by parsing the "Set-Cookie" headers in the specified
	 * {@link HttpResponse}.
	 * 
	 * @param httpResponse
	 *            The {@link HttpResponse} that represents the response from which to get the headers.
	 * 
	 * @return Returns a map of cookie names to {@link Cookie} instances by parsing the "Set-Cookie" headers in the
	 *         specified {@link HttpResponse}.
	 */
	private Map<String, Cookie> getCookiesFromResponse(HttpResponse httpResponse) {

		Map<String, Cookie> cookies = new HashMap<String, Cookie>();

		Header[] cookieHeaders = httpResponse.getHeaders("Set-Cookie");

		if ((null == cookieHeaders) || (0 == cookieHeaders.length)) {
			return cookies;
		}

		for (Header cookieHeader : cookieHeaders) {

			HeaderElement[] cookieHeaderElements = cookieHeader.getElements();
			for (HeaderElement cookieHeaderElement : cookieHeaderElements) {

				String cookieName = cookieHeaderElement.getName();
				String cookieValue = cookieHeaderElement.getValue();

				if (StringUtils.isNotBlank(cookieName)) {
					try {
						Cookie cookie = new Cookie(cookieName, cookieValue);

						// TODO - Parse through cookie parameters for path, age, domain, etc. and set them on the
						// cookie.
						@SuppressWarnings("unused")
						NameValuePair[] cookieParameters = cookieHeaderElement.getParameters();

						cookies.put(cookieName, cookie);
					} catch (Throwable t) {
						String warnMessage = "Error creating cookie with name " + cookieName;
						log.warn("getCookiesFromResponse: " + warnMessage, t);
					}
				}

			}

		}

		return cookies;
	}

	/**
	 * Adds an SSL {@link Scheme} to the registry of the connection manager of the {@link HttpClient}.
	 */
	private void addSslScheme() {

		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

			ClassPathResource keystoreResource = new ClassPathResource(keystoreFilePath);
			InputStream keystoreStream = keystoreResource.getInputStream();

			try {
				trustStore.load(keystoreStream, keystorePassword.toCharArray());
			} finally {
				try {
					keystoreStream.close();
				} catch (Exception ignore) {
				}
			}

			SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
			Scheme sch = new Scheme("https", 443, socketFactory);
			httpClient.getConnectionManager().getSchemeRegistry().register(sch);
		} catch (Throwable t) {
			String errorMessage = "Error adding SSL scheme to HTTP client";
			log.error("addSslScheme: " + errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		}

	}
}
