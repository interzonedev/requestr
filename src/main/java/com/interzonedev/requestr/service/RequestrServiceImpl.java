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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
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

	@Override
	public RequestrResponse doRequest(RequestrRequest requestrRequest) throws ClientProtocolException, IOException {

		log.debug("doRequest: Starting request - " + requestrRequest);

		// Assemble the HTTP request from the request value object.
		HttpRequestBase httpRequestBase = getHttpRequestBaseFromRequestrRequest(requestrRequest);

		log.debug("doRequest: Sending HTTP request");

		// Send the HTTP request.
		HttpResponse httpResponse = httpClient.execute(httpRequestBase);

		// Assemble the response value object from the HTTP response.
		RequestrResponse requestrResponse = getRequestrResponseFromHttpResponse(requestrRequest, httpResponse);

		log.debug("doRequest: Received response - " + requestrResponse);

		return requestrResponse;

	}

	private HttpRequestBase getHttpRequestBaseFromRequestrRequest(RequestrRequest requestrRequest) {

		HttpRequestBase httpRequestBase = null;

		String url = requestrRequest.getUrl();
		RequestrMethod method = requestrRequest.getMethod();
		Map<String, List<String>> requestHeaders = requestrRequest.getHeaders();
		Map<String, List<String>> requestParameters = requestrRequest.getParameters();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if ((null != requestParameters) && !requestParameters.isEmpty()) {
			for (String parameterName : requestParameters.keySet()) {
				List<String> parameterValues = requestParameters.get(parameterName);
				for (String parameterValue : parameterValues) {
					nameValuePairs.add(new BasicNameValuePair(parameterName, parameterValue));
				}
			}
		}

		switch (method) {
			case POST:
			case PUT:
				break;
			default:
				String queryString = URLEncodedUtils.format(nameValuePairs, "utf-8");
				if (!url.contains("?")) {
					url += "?";
				} else if (StringUtils.isNotBlank(queryString)) {
					url += "&";
				}
				url += queryString;
		}

		switch (method) {
			case GET:
				httpRequestBase = new HttpGet(url);
				break;
			case POST:
				httpRequestBase = new HttpPost(url);
				break;
			case PUT:
				httpRequestBase = new HttpPut(url);
				break;
			case DELETE:
				httpRequestBase = new HttpDelete(url);
				break;
			case OPTIONS:
				httpRequestBase = new HttpOptions(url);
				break;
			case HEAD:
				httpRequestBase = new HttpHead(url);
				break;
			case TRACE:
				httpRequestBase = new HttpTrace(url);
				break;
			default:
				throw new RuntimeException("Unsupported request method: " + method);
		}

		if ((null != requestHeaders) && !requestHeaders.isEmpty()) {
			for (String requestHeaderName : requestHeaders.keySet()) {
				List<String> requestHeaderValues = requestHeaders.get(requestHeaderName);
				for (String requestHeaderValue : requestHeaderValues) {
					httpRequestBase.addHeader(requestHeaderName, requestHeaderValue);
				}
			}
		}

		if (!nameValuePairs.isEmpty()) {
			switch (method) {
				case POST:
				case PUT:
					UrlEncodedFormEntity requestBodyEntity;
					try {
						requestBodyEntity = new UrlEncodedFormEntity(nameValuePairs, "utf-8");
					} catch (UnsupportedEncodingException uee) {
						String errorMessage = "Error creating request body";
						log.error("doRequest: " + errorMessage, uee);
						throw new RuntimeException(errorMessage, uee);
					}
					((HttpEntityEnclosingRequestBase) httpRequestBase).setEntity(requestBodyEntity);
					break;
			}
		}

		return httpRequestBase;

	}

	private RequestrResponse getRequestrResponseFromHttpResponse(RequestrRequest requestrRequest,
			HttpResponse httpResponse) throws ParseException, IOException {

		HttpEntity responseEntity = httpResponse.getEntity();

		int statusCode = httpResponse.getStatusLine().getStatusCode();

		String contentType = null;
		Header contentTypeHeader = responseEntity.getContentType();
		if (null != contentTypeHeader) {
			contentType = responseEntity.getContentType().getValue();
		}

		long contentLength = responseEntity.getContentLength();

		List<Cookie> cookies = new ArrayList<Cookie>();

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

		String responseContent = EntityUtils.toString(responseEntity);

		Locale locale = httpResponse.getLocale();

		return new RequestrResponse(requestrRequest, statusCode, contentType, contentLength, responseHeaders, cookies,
				responseContent, locale);

	}
	
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
