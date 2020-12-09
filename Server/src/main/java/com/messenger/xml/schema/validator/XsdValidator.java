//package com.messenger.xml.schema.validator;
//
//import com.messenger.protocol.Config;
//import com.messenger.xml.Xml;
//import org.xml.sax.SAXException;
//
//import javax.xml.XMLConstants;
//import javax.xml.transform.Source;
//import javax.xml.transform.stream.StreamSource;
//import javax.xml.validation.Schema;
//import javax.xml.validation.SchemaFactory;
//import javax.xml.validation.Validator;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.IOException;
//
//public abstract class XsdValidator {
//
//	private static Object sync1 = new Object();
//	private static Object sync2 = new Object();
//
//	private static void validate(Class<? extends Xml> Class, String xmlRequest) throws InvalidSchemaException {
//		synchronized (sync1) {
//			try (ByteArrayInputStream bis = new ByteArrayInputStream(xmlRequest.getBytes())) {
//				SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//				Schema schema = factory.newSchema(new File(Config.xsdDir + Class.getSimpleName() + ".xsd"));
//				Validator validator = schema.newValidator();
//				Source source = new StreamSource(bis);
//				validator.validate(source);
//			} catch (SAXException | IOException e) {
//				throw new InvalidSchemaException(Class, xmlRequest);
//			}
//		}
//	}
//}
