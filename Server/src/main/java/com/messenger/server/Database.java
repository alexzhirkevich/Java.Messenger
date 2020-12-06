package com.messenger.server;

import com.messenger.server.protocol.Message;
import com.messenger.server.protocol.User;
import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;

public class Database implements AutoCloseable {

	private Connection connection;
	private Statement statement;

	public Database(String fileName) {

		try {
			Class.forName("org.sqlite.JDBC");
			System.out.println(JDBC.PREFIX + fileName);
			connection = DriverManager.getConnection(JDBC.PREFIX + fileName);
			statement = connection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addUser(String firstName, String lastName, String phone, String passHash) throws SQLException {
		statement.executeUpdate(String.format(
				"INSERT INTO users (first_name, last_name, phone, pass_hash) VALUES ('%s','%s','%s','%s')",
				firstName, lastName, phone, passHash)
		);
	}

	public boolean userExists(String phone) throws SQLException {
		ResultSet rs =  statement.executeQuery(String.format("SELECT * FROM users WHERE phone IS ('%s')",phone));
		return !rs.isClosed();
	}

	public boolean userExists(int id) throws SQLException {
		ResultSet rs =  statement.executeQuery(String.format("SELECT * FROM users WHERE user_id IS ('%d')", id));
		return !rs.isClosed();
	}

	public User getUser(int id) throws SQLException {
		if (!userExists(id))
			return null;
		ResultSet rs = statement.executeQuery(String.format("SELECT * FROM users WHERE id IS ('%d')",id));
		return new User(rs.getString("first_name"), rs.getString("last_name"), rs.getString("phone"));
	}

	public boolean passCorrect(String phone, String passHash) throws SQLException {
		if (!userExists(phone))
			return false;
		ResultSet rs =  statement.executeQuery(String.format("SELECT * FROM users WHERE phone IS ('%s')",phone));
		return rs.getString("pass_hash").equals(passHash);
	}

	public int getUserId(String phone) throws SQLException {
		if (!userExists(phone))
			return -1;
		ResultSet rs = statement.executeQuery(String.format("SELECT user_id FROM users WHERE phone IS ('%s')",phone));
		return rs.getInt("user_id");
	}

	public boolean addMessage(User fromUser, User toUser, String message) throws SQLException {
		if (message.length() > 5000 || !userExists(fromUser.getId()) || !userExists(toUser.getId())){
			return false;
		}

		statement.executeUpdate(String.format(
				"INSERT INFO messages (from_user_id, to_user_id, text) VALUES ('%s','%s','%s')",
				fromUser.getId(), toUser.getId(),message)
		);
		return true;
	}

	public Message[] getInMessages(User user) throws SQLException {
		if (!userExists(user.getId()))
			return null;

		ArrayList<Message> inMessages = new ArrayList<>();

		try (ResultSet rs = statement.executeQuery(String.format("SELECT * FROM messages WHERE to_user_id IS ('%s')", user.getId()))) {

			if (rs.isClosed())
				return null;

			Integer[] from_user_ids = (Integer[])rs.getArray("from_user_id").getArray();
			String[] text = (String[])rs.getArray("text").getArray();
			Date[] date =  (Date[])rs.getArray("date").getArray();
			Time[] time =  (Time[])rs.getArray("time").getArray();

			User to_user = new User(user);

			for (int i = 0 ;i< from_user_ids.length;i++){
				User from_user = from_user_ids[i]==null? null : getUser(from_user_ids[i]);
				inMessages.add(new Message(from_user, to_user, text[i], date[i], time[i]));
			}
		}
		return inMessages.toArray(new Message[0]);
	}

	public Message[] getOutMessages(User user) throws SQLException {
		if (!userExists(user.getId()))
			return null;

		ArrayList<Message> outMessages = new ArrayList<>();

		try (ResultSet rs = statement.executeQuery(String.format("SELECT * FROM messages WHERE from_user_id IS ('%s')", user.getId()))) {

			if (rs.isClosed())
				return null;

			Integer[] to_user_ids = (Integer[])rs.getArray("to_user_id").getArray();
			String[] text = (String[])rs.getArray("text").getArray();
			Date[] date =  (Date[])rs.getArray("date").getArray();
			Time[] time =  (Time[])rs.getArray("time").getArray();

			User from_user = new User(user);

			for (int i = 0 ;i< to_user_ids.length;i++){
				User to_user = to_user_ids[i]==null? null : getUser(to_user_ids[i]);
				outMessages.add(new Message(from_user, to_user, text[i], date[i], time[i]));
			}
		}
		return outMessages.toArray(new Message[0]);
	}

	@Override
	public void close() throws Exception {
		connection.close();
	}
}
