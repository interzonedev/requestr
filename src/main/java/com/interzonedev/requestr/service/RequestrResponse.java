package com.interzonedev.requestr.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RequestrResponse {

	private final int status;

	private final String contentType;

	private final int contentLength;

	private final Map<String, List<String>> headers;

	private final String content;

	public RequestrResponse(int status, String contentType, int contentLength, Map<String, List<String>> headers,
			String content) {
		this.status = status;
		this.contentType = contentType;
		this.contentLength = contentLength;
		this.headers = Collections.unmodifiableMap(headers);
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public String getContentType() {
		return contentType;
	}

	public int getContentLength() {
		return contentLength;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public String getContent() {
		return content;
	}

}
