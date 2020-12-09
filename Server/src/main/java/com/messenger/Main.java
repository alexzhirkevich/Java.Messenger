package com.messenger;

import com.messenger.protocol.Config;
import com.messenger.server.Server;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args)  {
	//XsdGenerator.generateAll();
		new Server(Config.port,Config.dataBaseDir).run();
	}
}
