package com.interzonedev.requestr.web.send;

import org.hibernate.validator.constraints.NotEmpty;

public class ComponentsForm {

	@NotEmpty
	private String url;

	@NotEmpty
	private String method;

	private String headers;

	private String parameters;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

}
