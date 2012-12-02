package com.interzonedev.requestr.web.send;

import java.util.HashMap;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.interzonedev.requestr.service.RequestrMethod;
import com.interzonedev.requestr.service.RequestrRequest;

public class ComponentsForm {

	@NotEmpty
	private String url;

	@NotEmpty
	private String method;

	private String parameterValues;

	private String headerValues;

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

	public String getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(String parameterValues) {
		this.parameterValues = parameterValues;
	}

	public String getHeaderValues() {
		return headerValues;
	}

	public void setHeaderValues(String headerValues) {
		this.headerValues = headerValues;
	}

	public RequestrRequest toRequest() {
		RequestrMethod requestMethod = RequestrMethod.valueOf(getMethod());

		return new RequestrRequest(getUrl(), requestMethod, new HashMap<String, List<String>>(),
				new HashMap<String, List<String>>());
	}

}
