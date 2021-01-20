package com.messenger.server;

import com.messenger.protocol.User;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread implements Closeable {

	private final int PORT;
	private final ArrayList<Session> sessions;
	protected final Database database;
	private boolean isRunning;

	public Server(int port,String dbName){
		this.PORT = port;
		this.database = new Database(dbName,this);
		sessions = new ArrayList<>();
	}

	@Override
	public void run() {
		isRunning = true;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(PORT);
		}catch (Exception e){
			e.printStackTrace();
			return;
		}

		System.out.println("Server started on port: " + PORT);

		while (isRunning){
			Socket con=null;
			try {
				con = serverSocket.accept();
			}catch (IOException ignore){
				continue;
			}

			try{
				Session session = new Session(this, con);
				sessions.add(session);
				session.start();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	protected void endSession(Session session){
		if (session != null)
			sessions.remove(session);
	}

	protected boolean isUserOnline(int userId){
		for (Session s : sessions){
			if (s.getUser().getId() == userId)
				return true;
		}
		return false;
	}

	protected DataOutputStream getUserOutputStream(User user) {
		try {
			if (!database.userExists(user.getPhone()))
				return null;

			for (Session s : sessions){
				if (s.getUser().equals(user))
					return s.getOutputStream();
			}

			return null;

		}catch (Exception e){
			return null;
		}
	}

	@Override
	public void interrupt() {
		isRunning = false;
		for (Session s: sessions){
			s.close();
		}
	}

	@Override
	public void close() throws IOException {
		interrupt();
	}
}
