package com.messenger.server.xml.schema.validator;


import com.messenger.server.xml.Xml;

public class InvalidSchemaException extends Exception {

	public InvalidSchemaException(String msg) {
		super(msg);
	}

	public InvalidSchemaException(Class<? extends Xml> msg, String request) {
		super("Schema validation failed" + "\nClass: " + msg.getSimpleName() + "\nRequest: " + request);
	}


}