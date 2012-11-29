package com.interzonedev.requestr.service;

public class InvalidJsonException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidJsonException() {
	}

	public InvalidJsonException(String message) {
		super(message);
	}

	public InvalidJsonException(Throwable cause) {
		super(cause);
	}

	public InvalidJsonException(String message, Throwable cause) {
		super(message, cause);
	}

}
