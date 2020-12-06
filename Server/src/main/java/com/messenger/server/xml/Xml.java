package com.messenger.server.xml;

import com.messenger.server.protocol.Config;
import com.messenger.server.protocol.request.Request;
import com.messenger.server.protocol.request.RequestLogin;
import com.messenger.server.protocol.request.RequestRegister;
import com.messenger.server.protocol.response.Response;
import com.messenger.server.protocol.response.ResponseInvalid;
import com.messenger.server.protocol.response.ResponseLogin;
import com.messenger.server.protocol.response.ResponseRegister;
import com.messenger.server.xml.schema.validator.InvalidSchemaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@XmlTransient
public abstract class Xml {

	public static String toXml(Xml msg) throws JAXBException, IOException {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Config.XML_BUFFER)) {
			JAXBContext context = JAXBContext.newInstance(msg.getClass());
			Marshaller m = context.createMarshaller();
			m.marshal(msg, byteArrayOutputStream);
			byteArrayOutputStream.flush();
			return byteArrayOutputStream.toString();
		}
	}

	public static Xml fromXml(String xmlData)
			throws JAXBException, IOException, InvalidSchemaException {
		Class<? extends Xml> dataClass;
		try {
			dataClass = getClassByName(getRootElement(xmlData));
		} catch (Exception e) {
			throw new InvalidSchemaException("Invalid schema");
		}
		if (dataClass == null)
			throw new InvalidSchemaException("Invalid schema");

		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xmlData.getBytes())) {
			JAXBContext context = JAXBContext.newInstance(dataClass);
			Unmarshaller u = context.createUnmarshaller();
			return (Xml) u.unmarshal(byteArrayInputStream);
		}
	}

	private static String getRootElement(String xmlData) throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlData);
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
		} else if (name.contains(Response.class.getSimpleName())) {
			if (name.equals(ResponseInvalid.class.getSimpleName()))
				return ResponseInvalid.class;
			else if (name.equals(ResponseLogin.class.getSimpleName()))
				return ResponseLogin.class;
			else if (name.equals(ResponseRegister.class.getSimpleName()))
				return Response.class;

		}

		return null;
	}

}
