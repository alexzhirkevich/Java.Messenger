package com.messenger.protocol.response;

public class ResponseException extends Exception {

	public ResponseException(String str) {
		super(str);
	}

	public ResponseException(int id) {
		super("Invalid response id: " + id);
	}

}
