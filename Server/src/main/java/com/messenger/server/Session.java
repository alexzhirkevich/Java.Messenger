package com.messenger.server;

import com.messenger.server.protocol.User;
import com.messenger.server.protocol.request.Request;
import com.messenger.server.protocol.request.RequestDisconnect;
import com.messenger.server.protocol.request.RequestLogin;
import com.messenger.server.protocol.request.RequestRegister;
import com.messenger.server.protocol.response.Response;
import com.messenger.server.protocol.response.ResponseInvalid;
import com.messenger.server.protocol.response.ResponseRegister;
import com.messenger.server.protocol.response.Responser;
import com.messenger.server.xml.Xml;

import javax.xml.bind.JAXBException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class Session extends Thread {

	private final Server server;
	private final Socket socket;
	private final long id;
	private final User user;
	private final DataInputStream dataInputStream;
	private final DataOutputStream dataOutputStream;

	private boolean connected = false;
	private boolean isRunning = false;

	public Session(Server server, Socket socket, int id, User user) throws IOException {
		this.server = server;
		this.socket = socket;
		this.id = id;
		this.user = new User(user);
		this.socket.setSoTimeout(500);
		this.dataInputStream = new DataInputStream(socket.getInputStream());
		this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
	}

	public void sendResponse(Response req) throws JAXBException, IOException {
		dataOutputStream.writeUTF(Xml.toXml(req));
	}

	private Request getRequest() {
		Request req;
		try {
			req = (Request) Xml.fromXml(dataInputStream.readUTF());
			return req;
		} catch (Exception e) {
			return null;
		}
	}

	private Response processRequest(Request req) {
		if (req == null)
			return null;

		switch (req.getId()) {
			case Request.REQ_LOGIN:
				return login((RequestLogin) req);
			case Request.REQ_REGISTER:
				return register((RequestRegister) req);
			case Request.REQ_DISCONNECT:
				disconnect((RequestDisconnect) req);
		}

		return null;
	}

	@Override
	public void run() {
		isRunning = true;
		while (isRunning) {
			Request req = getRequest();
			if (req == null) {
				try {
					sendResponse(new ResponseInvalid("Invalid Request"));
				} catch (Exception ingore) {
				}
			}

			try {
				sendResponse(processRequest(req));
			} catch (Exception e) {
				try {
					sendResponse(new ResponseInvalid("404 not found"));
				} catch (Exception ignore) {
				}
			}
		}
	}

	private Response login(RequestLogin req) {
		return null;
	}

	private Response register(RequestRegister req) {
		try {
			server.database.addUser(
					req.getUser().getFirstName(),
					req.getUser().getLastName(),
					req.getUser().getPhone(), req.getPassHash());
			return new ResponseRegister(Responser.RES_OK);
		} catch (SQLException e) {
			return new ResponseRegister(Responser.RES_ERROR, "Register fail");
		}
	}

	private Response disconnect(RequestDisconnect req) {
		return null;
	}
}
