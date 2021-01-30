package com.assistant.protocol.request;

public class RequestException extends Exception {

	public RequestException(String str) {
		super(str);
	}

	public RequestException(byte id) {
		super("Invalid request id: " + id);
	}

}
