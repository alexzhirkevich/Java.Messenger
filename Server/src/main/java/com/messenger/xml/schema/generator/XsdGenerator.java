//package com.messenger.xml.schema.generator;
//
//
//import com.messenger.protocol.Config;
//import com.messenger.protocol.request.*;
//import com.messenger.protocol.response.*;
//import com.messenger.xml.Xml;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//public abstract class XsdGenerator {
//
//	public static void create(String fileName, Class<? extends Xml> what)
//			throws JAXBException, IOException {
//		JAXBContext context = JAXBContext.newInstance(what);
//		context.generateSchema(new XsdOutputResolver(Config.xsdDir, fileName));
//	}
//
//	public static void generateAll() throws JAXBException, IOException {
//		Files.createDirectories(Paths.get(Config.xsdDir));
//		XsdGenerator.create(ResponseInvalid.class.getSimpleName(), ResponseInvalid.class);
//		XsdGenerator.create(RequestDisconnect.class.getSimpleName(), RequestDisconnect.class);
//
//		XsdGenerator.create(RequestRegister.class.getSimpleName(), RequestRegister.class);
//		XsdGenerator.create(ResponseRegister.class.getSimpleName(), ResponseRegister.class);
//
//		XsdGenerator.create(RequestLogin.class.getSimpleName(), RequestLogin.class);
//		XsdGenerator.create(ResponseLogin.class.getSimpleName(), ResponseLogin.class);
//
//		XsdGenerator.create(RequestChat.class.getSimpleName(), RequestChat.class);
//		XsdGenerator.create(ResponseChat.class.getSimpleName(), ResponseChat.class);
//
//		XsdGenerator.create(RequestSendMessage.class.getSimpleName(), RequestSendMessage.class);
//		XsdGenerator.create(ResponseSendMessage.class.getSimpleName(), ResponseSendMessage.class);
//	}
//
//
//}
