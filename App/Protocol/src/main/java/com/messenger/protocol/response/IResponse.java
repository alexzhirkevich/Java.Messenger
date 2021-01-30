package com.messenger.protocol.response;

public interface IResponse {

	int RES_OK = 0;
	int RES_NOTAUTHORIZED = 1;
	int RES_WRONGPASSWORD = 2;
	int RES_USEREXISTS = 3;
	int RES_USERNOTEXISTS = 4;
	int RES_ERROR = 5;

	int getResponseCode();
	int getReq();
	String getErrorMsg();

	static boolean error(byte res) {
		return res!=RES_OK;
	}
}