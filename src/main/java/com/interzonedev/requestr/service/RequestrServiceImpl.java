package com.interzonedev.requestr.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

@Named("requestrService")
public class RequestrServiceImpl implements RequestrService {

	private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

	@Inject
	@Named("httpClient")
	protected DefaultHttpClient httpClient;

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

		if ((null != requestParameters) && !requestParameters.isEmpty()) {
			switch (method) {
				case POST:
				case PUT:
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					for (String parameterName : requestParameters.keySet()) {
						List<String> parameterValues = requestParameters.get(parameterName);
						for (String parameterValue : parameterValues) {
							nameValuePairs.add(new BasicNameValuePair(parameterName, parameterValue));
						}
					}
					UrlEncodedFormEntity requestBodyEntity;
					try {
						requestBodyEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
					} catch (UnsupportedEncodingException uee) {
						String errorMessage = "Error creating request body";
						log.error("doRequest: " + errorMessage, uee);
						throw new RuntimeException(errorMessage, uee);
					}
					((HttpEntityEnclosingRequestBase) httpRequestBase).setEntity(requestBodyEntity);
					break;
				default:
					for (String parameterName : requestParameters.keySet()) {
						List<String> parameterValues = requestParameters.get(parameterName);
						for (String parameterValue : parameterValues) {
							httpRequestBase.getParams().setParameter(parameterName, parameterValue);
						}
					}
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

		return new RequestrResponse(requestrRequest, statusCode, contentType, contentLength, responseHeaders,
				responseContent, locale);

	}

}
