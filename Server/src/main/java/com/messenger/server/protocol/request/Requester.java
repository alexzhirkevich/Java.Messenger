package com.messenger.server.protocol.request;

public interface Requester {

	byte REQ_INVALID = -1;
	byte REQ_LOGIN = 0;
	byte REQ_REGISTER = 1;
	byte REQ_MESSAGE = 2;
	byte REQ_DISCONNECT = 3;

	static boolean isValid(byte req) {
		return req != REQ_INVALID;
	}
}
