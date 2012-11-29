package com.interzonedev.requestr.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;

public class RequestrRequest {

	@NotEmpty
	private final String url;

	@NotEmpty
	private final RequestrMethod method;

	private final Map<String, List<String>> headers;

	private final Map<String, List<String>> parameters;

	public RequestrRequest(String url, RequestrMethod method, Map<String, List<String>> headers,
			Map<String, List<String>> parameters) {
		this.url = url;
		this.method = method;
		this.headers = Collections.unmodifiableMap(headers);
		this.parameters = Collections.unmodifiableMap(parameters);
	}

	public String getUrl() {
		return url;
	}

	public RequestrMethod getMethod() {
		return method;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public Map<String, List<String>> getParameters() {
		return parameters;
	}

}
