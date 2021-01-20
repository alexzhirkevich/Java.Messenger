package com.messenger.server;

import com.messenger.protocol.*;
import com.messenger.protocol.request.*;
import com.messenger.protocol.response.*;
import com.messenger.protocol.Xml;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class Session extends Thread implements Closeable {

	private final Server server;
	private final Socket socket;
	private User user = null;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;

	private boolean isRunning = false;

	public Session(Server server, Socket socket) throws IOException {
		this.server = server;
		this.socket = socket;
		this.socket.setSoTimeout(5000);
	}

	public void sendResponse(Response res) throws Exception {
		if (res != null) {
			dataOutputStream.writeUTF(Xml.toXml(res));
			dataOutputStream.flush();
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
			case Request.REQ_CHAT:
				return chat((RequestChat)req);
			case Request.REQ_SENDMSG:
				return message((RequestSendMessage)req);
			case Request.REQ_DISCONNECT:
				disconnect((RequestDisconnect) req);
				return null;
		}

		return null;
	}

	@Override
	public void run() {
		try {
			this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
			this.dataInputStream = new DataInputStream(socket.getInputStream());
		}catch (Exception e){
			e.printStackTrace();
		}

			isRunning = true;
			while (isRunning) {
				Request req = null;
				try {
					req = (Request) Xml.fromXml(dataInputStream.readUTF());
				} catch (Exception ignore){}

				try {
					sendResponse(processRequest(req));
				} catch (Exception e) {
					try {
						sendResponse(new ResponseInvalid("404 not found"));
					} catch (Exception ignore) { }
				}
			}
	}

	private boolean send(Message msg){
		try{
			if (!server.database.userExists(msg.getFromUser().getPhone()))
				return false;
			if (!server.database.userExists(msg.getToUser().getPhone()))
				return false;
			if (msg.getText() == null)
				return false;

			DataOutputStream out = server.getUserOutputStream(msg.getToUser());
			out.writeUTF(Xml.toXml(msg));
			return true;

		}catch (Exception e){
			return false;
		}
	}

	private ResponseSendMessage message(RequestSendMessage req) {
		try{

			if (user == null || !user.equals(req.getMessage().getFromUser()))
				return new ResponseSendMessage(Responser.RES_NOTAUTHORIZED,-1,"Not authorised");
			if (!server.database.userExists(req.getMessage().getFromUser().getPhone()))
				return new ResponseSendMessage(Responser.RES_USERNOTEXISTS,-1,"FromUser doesn't exists");
			if (!server.database.userExists(req.getMessage().getToUser().getPhone()))
				return new ResponseSendMessage(Responser.RES_USERNOTEXISTS,-1,"ToUser doesn't exists");

			int msgId  = server.database.addMessage(req.getMessage());

			Message msg = req.getMessage();
			if (msg.getFromUser().getId() == -1)
				msg.setFromUserId(server.database.getUserId(msg.getFromUser().getPhone()));
			if (msg.getToUser().getId() == -1)
				msg.setToUserId(server.database.getUserId(msg.getToUser().getPhone()));

			msg.setId(msgId);

			if (send(msg))
				return new ResponseSendMessage(Responser.RES_OK,msgId);
			else
				return new ResponseSendMessage(Responser.RES_ERROR,-1,"Error");
		}catch (SQLException e){
			return new ResponseSendMessage(Responser.RES_ERROR,-1, "Error");
		}
	}

	private ResponseChat chat(RequestChat req) {
		try {
			if (user == null || req.getUserId() == null || req.getWithUserId() == null ||
					(req.getWithUserId()!= user.getId() && req.getUserId() != user.getId()))
				return new ResponseChat(Responser.RES_ERROR, null, "Error");

			if (!server.database.userExists(req.getUserId()))
				return new ResponseChat(Response.RES_USERNOTEXISTS, null, "Requested user1 doesn't exist");
			if (req.getWithUserId() == -1 || !server.database.userExists(req.getWithUserId()))
				return new ResponseChat(Response.RES_USERNOTEXISTS, null, "Requested user2 doesn't exist");

			return new ResponseChat(Response.RES_OK, server.database.getChat(req.getUserId(),req.getWithUserId()));
		}
		catch (SQLException e) {
			return new ResponseChat(Response.RES_ERROR, null, "Error");
		}
	}

	private ResponseLogin login(RequestLogin req) {
		try {
			if (!server.database.userExists(req.getPhone()))
				return new ResponseLogin(Response.RES_USERNOTEXISTS, null,"User is not registered");
			if (!server.database.passCorrect(req.getPhone(),req.getPassHash()))
				return new ResponseLogin(Response.RES_WRONGPASSWORD,null,"Incorrect password");

			int id = server.database.getUserId(req.getPhone());
			user = server.database.getUser(id);

			System.out.println("User logged in: " + user.getPhone());

			return new ResponseLogin(Response.RES_OK,user);
		} catch (SQLException e){
			return  new ResponseLogin(Response.RES_ERROR,user, "Login error");
		}
	}

	private ResponseRegister register(RequestRegister req) {
		try {
			if (server.database.userExists(req.getUser().getPhone()))
				return new ResponseRegister(Response.RES_USEREXISTS, null, "User is already exists");
			server.database.addUser(
					req.getUser().getFirstName(),
					req.getUser().getLastName(),
					req.getUser().getPhone(), BCrypt.hashpw(req.getPassword(),BCrypt.gensalt()));
			int userID = server.database.getUserId(req.getUser().getPhone());
			User user = req.getUser();
			user.setId(userID);
			this.user = new User(user);
			return new ResponseRegister(Response.RES_OK, user);
		} catch (SQLException e) {
			return new ResponseRegister(Response.RES_ERROR, null,"Register fail");
		}
	}

	private ResponseDialogMembers dialogMembers(RequestDialogMembers req){
		try{
			if (user == null || user.getId() != req.getUser().getId())
				return new ResponseDialogMembers(Response.RES_ERROR,null,"Error");

			if (server.database.userExists(req.getUser().getId())) {
				return new ResponseDialogMembers(Response.RES_USERNOTEXISTS,null,"Requester is not exists");
		}
		Integer[] ids = server.database.getAllDialogs(req.getUser().getId());
		return new ResponseDialogMembers(Response.RES_OK,ids);
		} catch (Exception e){
			return new ResponseDialogMembers(Response.RES_ERROR,null,"Failed to get chat members");
		}
	}

	private void disconnect(RequestDisconnect req) {
		close();
	}

	protected User getUser() {
		return new User(user);
	}

	public DataOutputStream getOutputStream(){
		return dataOutputStream;
	}

	@Override
	public void close() {
		try {
			isRunning = false;
			server.endSession(this);
			dataInputStream.close();
			dataOutputStream.close();
			socket.close();
		} catch (Exception e){}

		interrupt();
	}
}
