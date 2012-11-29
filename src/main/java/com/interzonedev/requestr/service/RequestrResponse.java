package com.interzonedev.requestr.service;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RequestrResponse {

	private final RequestrRequest request;

	private final int status;

	private final String contentType;

	private final long contentLength;

	private final Map<String, List<String>> headers;

	private final String content;

	private final Locale locale;

	public RequestrResponse(RequestrRequest request, int status, String contentType, long contentLength,
			Map<String, List<String>> headers, String content, Locale locale) {
		this.request = request;
		this.status = status;
		this.contentType = contentType;
		this.contentLength = contentLength;
		this.headers = Collections.unmodifiableMap(headers);
		this.content = content;
		this.locale = locale;
	}

	public RequestrRequest getRequest() {
		return request;
	}

	public int getStatus() {
		return status;
	}

	public String getContentType() {
		return contentType;
	}

	public long getContentLength() {
		return contentLength;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public String getContent() {
		return content;
	}

	public Locale getLocale() {
		return locale;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(getStatus()).append(" from ").append(getRequest());

		return sb.toString();
	}
}
