package com.messenger.server.protocol.response;

public interface Responser {

	byte RES_OK = 0;
	byte RES_NOTREGISTERED = 1;
	byte RES_NOTLOGGINED = 2;
	byte RES_ERROR = 3;

	static boolean error(byte res) {
		return res!=RES_OK;
	}
}
