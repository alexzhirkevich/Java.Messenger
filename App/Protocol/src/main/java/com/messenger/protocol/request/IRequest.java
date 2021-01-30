package com.messenger.protocol.request;

public interface IRequest {

	int REQ_INVALID = -1;
	int REQ_LOGIN = 0;
	int REQ_LOGIN_GOOGLE = 1;
	int REQ_REGISTER = 2;
	int REQ_DIALOG = 3;
	int REQ_DIALOGLIST = 4;
	int REQ_SENDMSG = 5;
	int getId();
	String getToken();

	static boolean isValid(byte req) {
		return req != REQ_INVALID;
	}
}
