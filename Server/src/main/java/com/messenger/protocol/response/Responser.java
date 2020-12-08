package com.messenger.protocol.response;

public interface Responser {

	byte RES_OK = 0;
	byte RES_NOTREGISTERED = 1;
	byte RES_NOTAUTHORIZED = 2;
	byte RES_WRONGPASSWORD = 3;
	byte RES_USEREXISTS = 4;
	byte RES_USERNOTEXISTS = 5;
	byte RES_ERROR = 6;


	static boolean error(byte res) {
		return res!=RES_OK;
	}
}
