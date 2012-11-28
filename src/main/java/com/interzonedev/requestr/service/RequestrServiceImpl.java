package com.interzonedev.requestr.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.impl.client.DefaultHttpClient;

@Named("requestrService")
public class RequestrServiceImpl implements RequestrService {

	@Inject
	@Named("httpClient")
	protected DefaultHttpClient httpClient;

	@Override
	public RequestrResponse doRequest(RequestrRequest requestrRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
