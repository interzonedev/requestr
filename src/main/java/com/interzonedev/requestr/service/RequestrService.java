package com.interzonedev.requestr.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

/**
 * Interface for performing HTTP requests and returning responses using the {@link RequestrRequest} and
 * {@link RequestrResponse} value objects.
 * 
 * @author mark@interzonedev.com
 */
public interface RequestrService {

	/**
	 * Performs an HTTP request using the url, method, headers and parameters in the specified {@link RequestrRequest}
	 * value object.
	 * 
	 * @param requestrRequest
	 *            The {@link RequestrRequest} value object that contains the components of the HTTP request to be made.
	 * 
	 * @return Returns a {@link RequestrResponse} value object that contains the components, including status and body,
	 *         of the HTTP response to the HTTP request performed by this method.
	 * 
	 * @throws IOException
	 *             Thrown if there is an error performing the HTTP request.
	 */
	public RequestrResponse doRequest(RequestrRequest requestrRequest) throws ClientProtocolException, IOException;

}
