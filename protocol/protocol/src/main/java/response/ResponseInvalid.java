package com.messenger.protocol.response;

import com.messenger.protocol.request.Request;

import org.simpleframework.xml.Root;

@Root
public class ResponseInvalid extends Response{

	public ResponseInvalid(){
		super(Request.REQ_INVALID,RES_ERROR);
	}

	public ResponseInvalid(String errorMsg){
		super(Request.REQ_INVALID,RES_ERROR,errorMsg);
	}
}
