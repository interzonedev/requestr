package com.interzonedev.requestr.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface RequestrService {

	public RequestrResponse doRequest(RequestrRequest requestrRequest) throws ClientProtocolException, IOException;

}
