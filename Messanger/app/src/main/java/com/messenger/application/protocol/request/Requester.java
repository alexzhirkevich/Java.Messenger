package com.messenger.application.protocol.request;

public interface Requester {

	byte REQ_INVALID = -1;
	byte REQ_LOGIN = 0;
	byte REQ_REGISTER = 1;
	byte REQ_CHAT = 2;
	byte REQ_SENDMSG = 3;
	byte REQ_DIALOGIDS = 4;
	byte REQ_DISCONNECT = 5;

	static boolean isValid(byte req) {
		return req != REQ_INVALID;
	}
}
