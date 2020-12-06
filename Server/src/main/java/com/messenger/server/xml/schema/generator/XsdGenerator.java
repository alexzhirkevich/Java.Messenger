package com.messenger.server.xml.schema.generator;


import com.messenger.server.protocol.Config;
import com.messenger.server.protocol.request.RequestLogin;
import com.messenger.server.protocol.request.RequestMessage;
import com.messenger.server.protocol.request.RequestRegister;
import com.messenger.server.protocol.response.ResponseInvalid;
import com.messenger.server.protocol.response.ResponseLogin;
import com.messenger.server.protocol.response.ResponseMessage;
import com.messenger.server.protocol.response.ResponseRegister;
import com.messenger.server.xml.Xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public abstract class XsdGenerator {

	public static void create(String fileName, Class<? extends Xml> what)
			throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(what);
		context.generateSchema(new XsdOutputResolver(Config.xsdDir, fileName));
	}

	public static void generateAll() throws JAXBException, IOException {
		XsdGenerator.create(ResponseInvalid.class.getSimpleName(), ResponseInvalid.class);

		XsdGenerator.create(RequestRegister.class.getSimpleName(), RequestRegister.class);
		XsdGenerator.create(ResponseRegister.class.getSimpleName(), ResponseRegister.class);

		XsdGenerator.create(RequestLogin.class.getSimpleName(), RequestLogin.class);
		XsdGenerator.create(ResponseLogin.class.getSimpleName(), ResponseLogin.class);

		XsdGenerator.create(RequestMessage.class.getSimpleName(), RequestMessage.class);
		XsdGenerator.create(ResponseMessage.class.getSimpleName(), ResponseMessage.class);
	}


}
