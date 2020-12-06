package com.messenger.server;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws JAXBException, IOException, SQLException {
		Database db = new Database("c:\\Users\\5\\Desktop\\NovayaPapka\\proga\\github\\Messenger\\Server\\src\\main\\resources\\database\\database.db");
		//db.addUser("Имя","Фамилия", "телефон","пароль");
		if (db.passCorrect("телефон", "пайуроль"))
			System.out.println("да");
		else
			System.out.println("нет");
	}
}
