package com.assistant.protocol.response;

public class ResponseException extends Exception {

	public ResponseException(String str) {
		super(str);
	}

	public ResponseException(byte id) {
		super("Invalid response id: " + id);
	}

}
