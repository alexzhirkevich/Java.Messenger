package com.messenger;

import com.messenger.protocol.Config;
import com.messenger.server.Server;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
	static String dataBaseDir = "C:\\Users\\5\\Desktop\\NovayaPapka\\proga\\github\\Messenger\\Server\\src\\main\\resources\\database\\database.db";

	public static void main(String[] args)  {
	//XsdGenerator.generateAll();
		new Server(Config.port,dataBaseDir).run();
	}
}
