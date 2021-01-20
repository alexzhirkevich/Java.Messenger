package com.messenger.protocol.response;

public interface Responser {

	byte RES_OK = 0;
	byte RES_NOTAUTHORIZED = 1;
	byte RES_WRONGPASSWORD = 2;
	byte RES_USEREXISTS = 3;
	byte RES_USERNOTEXISTS = 4;
	byte RES_ERROR = 5;


	static boolean error(byte res) {
		return res!=RES_OK;
	}
}
