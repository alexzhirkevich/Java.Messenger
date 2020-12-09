package com.messenger.protocol;

import java.io.File;

public interface Config {

	int port = 8888;
	int XML_BUFFER = 1024 * 1024;
	String xsdDir = "C:\\Users\\5\\Desktop\\NovayaPapka\\proga\\github\\Messenger\\Server\\src\\main\\resources\\xsd" + File.separator;
	String dataBaseDir = "C:\\Users\\5\\Desktop\\NovayaPapka\\proga\\github\\Messenger\\Server\\src\\main\\resources\\database\\database.db";

}
