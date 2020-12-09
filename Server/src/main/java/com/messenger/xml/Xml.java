package com.messenger.xml;

import com.messenger.protocol.response.*;
import com.messenger.protocol.request.*;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.StringWriter;

public abstract class Xml {

	public static String toXml(Xml msg) throws Exception {

		Serializer s = new Persister();
		StringWriter sw = new StringWriter();
		s.write(msg,sw);
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + '\n' +sw.getBuffer().toString();
	}

	public static Xml fromXml(String xmlData)
			throws Exception {
		System.out.println(xmlData);
		Class<? extends Xml> dataClass;
		dataClass = getClassByName(getRootElement(xmlData));

		Serializer s = new Persister();
		if (s.validate(dataClass,xmlData))
			return s.read(dataClass,xmlData);
		return null;
	}

	private static String getRootElement(String xmlData) throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xmlData)));
		Element root = doc.getDocumentElement();
		return root.getTagName();
	}

	public static Class<? extends Xml> getClassByName(String name) {

		name = Character.toUpperCase(name.charAt(0)) + name.substring(1);

		if (name.contains(Request.class.getSimpleName())) {
			if (name.equals(RequestLogin.class.getSimpleName()))
				return RequestLogin.class;
			else if (name.equals(RequestRegister.class.getSimpleName()))
				return RequestRegister.class;
			else if (name.equals(RequestChat.class.getSimpleName()))
				return RequestChat.class;
			else if (name.equals(RequestSendMessage.class.getSimpleName()))
				return RequestSendMessage.class;

			else if (name.equals(RequestDisconnect.class.getSimpleName()))
				return RequestDisconnect.class;
		} else if (name.contains(Response.class.getSimpleName())) {
			if (name.equals(ResponseInvalid.class.getSimpleName()))
				return ResponseInvalid.class;
			else if (name.equals(ResponseLogin.class.getSimpleName()))
				return ResponseLogin.class;
			else if (name.equals(ResponseRegister.class.getSimpleName()))
				return ResponseRegister.class;
			else if (name.equals(ResponseChat.class.getSimpleName()))
				return ResponseChat.class;
			else if (name.equals(ResponseSendMessage.class.getSimpleName()))
				return ResponseSendMessage.class;
		}

		return null;
	}

}
