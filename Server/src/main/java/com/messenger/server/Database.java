package com.messenger.server;

import com.messenger.protocol.*;
import org.mindrot.jbcrypt.BCrypt;
import org.sqlite.JDBC;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeSet;

public class Database implements AutoCloseable {

	private Connection connection;
	private Statement statement;
	private Server server;

	public Database(String fileName, Server server) {

		try {
			Class.forName("org.sqlite.JDBC");
			System.out.println(JDBC.PREFIX + fileName);
			connection = DriverManager.getConnection(JDBC.PREFIX + fileName);
			statement = connection.createStatement();
			this.server = server;
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
		if (id == -1)
			return false;
		ResultSet rs =  statement.executeQuery(String.format("SELECT * FROM users WHERE user_id IS ('%d')", id));
		return !rs.isClosed();
	}

	public User getUser(int id) throws SQLException {
		if (!userExists(id))
			return null;
		try(ResultSet rs = statement.executeQuery(String.format("SELECT * FROM users WHERE user_id IS ('%d')",id))) {
			Byte[] image = (Byte[])rs.getArray("avatar_lq").getArray();
			return new User(id, rs.getString("first_name"), rs.getString("last_name"), rs.getString("phone"), server.isUserOnline(id),image);
		}
	}

	public boolean passCorrect(String phone, String pass) throws SQLException {
		if (!userExists(phone))
			return false;
		ResultSet rs =  statement.executeQuery(String.format("SELECT * FROM users WHERE phone IS ('%s')",phone));
		return BCrypt.checkpw(pass,rs.getString("pass_hash"));
	}

	public int getUserId(String phone) throws SQLException {
		if (!userExists(phone))
			return -1;
		try (ResultSet rs = statement.executeQuery(String.format("SELECT user_id FROM users WHERE phone IS ('%s')",phone))) {
			return rs.getInt("user_id");
		}
	}

	public int addMessage(Message message) throws SQLException {
		if (message.getText().length() > 1000 || !userExists(message.getFromUser().getId()) || !userExists(message.getToUser().getId())) {
			return -1;
		}

		int from_id = message.getFromUser().getId();
		int to_id = message.getFromUser().getId();

		if (from_id == -1)
			from_id = getUserId(message.getFromUser().getPhone());
		if (to_id == -1)
			to_id = getUserId(message.getToUser().getPhone());

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = simpleDateFormat.format(message.getDate());
		String time = message.getTime().toString() + message.getTime().toString();

		statement.executeUpdate(String.format(
				"INSERT INFO messages (from_user_id, to_user_id, text, date, time) VALUES ('%d','%d','%s','%s','%s')",
				from_id, to_id, message.getText(), date, time)
		);

		int msgId = -1;
		try (ResultSet rs = statement.executeQuery(String.format(
				"SELECT msg_id FROM messages WHERE from_user_id IS ('%d') AND to_user_id IS ('%s') ORDER BY msg_id DECS LIMIT 1",
				from_id, to_id))) {
			msgId = rs.getInt("msg_id");
		}

		return msgId;
	}

	public boolean deleteMessageByUser(Message msg, User user) throws SQLException {
		try (ResultSet rs = statement.executeQuery(String.format("SELECT * FROM messages WHERE msg_id IS ('%d')",msg.getId()))) {
			if (rs.isClosed())
				return false;
			int from_user_id = rs.getInt("from_user_id");
			boolean from_deleted = rs.getBoolean("from_deleted");
			int to_user_id = rs.getInt("to_user_id");
			boolean to_deleted = rs.getBoolean("to_deleted");

			if (user.getId() == from_user_id && !from_deleted) {
				if (!to_deleted)
					statement.executeUpdate(String.format("UPDATE messages SET from_deleted = 1 WHERE msg_id IS ('%d')",msg.getId()));
				else
					statement.executeUpdate(String.format("DELETE FROM messages WHERE msg_id IS ('%d')",msg.getId()));
				return true;
			}
			if (user.getId() == to_user_id && !to_deleted) {
				if (!from_deleted)
					statement.executeUpdate(String.format("UPDATE messages SET to_deleted = 1 WHERE msg_id IS ('%d')",msg.getId()));
				else
					statement.executeUpdate(String.format("DELETE FROM messages WHERE msg_id IS ('%d')",msg.getId()));
				return true;
			}
			return false;
		}
	}

	public Chat getChat(Integer userId, Integer withUserId) throws SQLException {

		User from = getUser(userId);
		User to = getUser(withUserId);
		Integer dialogId = getDialogId(userId,withUserId);
		if (dialogId == -1)
			return null;

		if (from == null || to == null)
			return null;

		ArrayList<Message> from1to2 = new ArrayList<>();
		ArrayList<Message> from2to1 = new ArrayList<>();

		try (ResultSet rs = statement.executeQuery(String.format(
				"SELECT * FROM messages WHERE from_user_id IS ('%d') AND to_user_id IS ('%d')", userId, withUserId))) {
			Boolean[] from_deleted = (Boolean[])rs.getArray("from_deleted").getArray();
			Integer[] ids = (Integer[])rs.getArray("msg_id").getArray();
			String[] text = (String[])rs.getArray("text").getArray();
			Date[] date =  (Date[])rs.getArray("date").getArray();
			Time[] time =  (Time[])rs.getArray("time").getArray();

			for(int i =0;i<text.length;i++){
				if (!from_deleted[i])
					from1to2.add(new Message(from,to,text[i],date[i],time[i],ids[i]));
			}
		}
		try (ResultSet rs = statement.executeQuery(String.format(
				"SELECT * FROM messages WHERE from_user_id IS ('%d') AND to_user_id IS ('%d')",
				withUserId, userId))) {
			Boolean[] to_deleted = (Boolean[])rs.getArray("to_deleted").getArray();
			Integer[] ids = (Integer[])rs.getArray("msg_id").getArray();
			String[] text = (String[])rs.getArray("text").getArray();
			Date[] date =  (Date[])rs.getArray("date").getArray();
			Time[] time =  (Time[])rs.getArray("time").getArray();


			for(int i =0;i<text.length;i++) {
				if (!to_deleted[i])
					from2to1.add(new Message(to,from,text[i],date[i],time[i],ids[i]));
			}
		}

		from1to2.addAll(from2to1);

		return new Chat(dialogId,getUser(userId),getUser(withUserId),from1to2.toArray(new Message[0]));
	}

	public Integer getDialogId(Integer firstUserId, Integer secondUserId) throws SQLException {
		try(ResultSet rs = statement.executeQuery(String.format(
				"SELECT dialog_id FROM dialogs WHERE first_user_id IS ('%d') AND second_user_id IS ('%d')",firstUserId,secondUserId))){
				if (!rs.isClosed()){
					return rs.getInt("dialog_id");
				}
		}
		try(ResultSet rs = statement.executeQuery(String.format(
				"SELECT dialog_id FROM dialogs WHERE first_user_id IS ('%d') AND second_user_id IS ('%d')",secondUserId,firstUserId))){
			if (!rs.isClosed()){
				return rs.getInt("dialog_id");
			}
		}
		return -1;
	}

	public Integer createDialog(Integer firstUserId, Integer secondUserId) throws SQLException {
		if (getDialogId(firstUserId,secondUserId) != -1)
			return -1;
		statement.executeUpdate(String.format(
				"INSERT INTO dialogs (first_user_id, second_user_id) VALUES ('%d','%d')",firstUserId,secondUserId));
		return getDialogId(firstUserId,secondUserId);
	}

	public Integer[] getAllDialogs(Integer userId) throws SQLException {

		Message[] in = getInMessages(userId);
		Message[] out = getOutMessages(userId);

		TreeSet<Integer> ids = new TreeSet<>();
		for (Message m:in){
			ids.add(m.getFromUser().getId());
		}
		for(Message m:out){
			ids.add(m.getToUser().getId());
		}
		return ids.toArray(new Integer[0]);
	}

	public Message[] getInMessages(Integer userId) throws SQLException {
		if (!userExists(userId))
			return null;

		ArrayList<Message> inMessages = new ArrayList<>();

		try (ResultSet rs = statement.executeQuery(String.format(
				"SELECT * FROM messages WHERE to_user_id IS ('%s')",
				userId))) {

			if (rs.isClosed())
				return null;

			Integer[] from_user_ids = (Integer[])rs.getArray("from_user_id").getArray();
			Boolean[] to_deleted = (Boolean[])rs.getArray("to_deleted").getArray();
			String[] text = (String[])rs.getArray("text").getArray();
			Date[] date =  (Date[])rs.getArray("date").getArray();
			Time[] time =  (Time[])rs.getArray("time").getArray();

			User to_user = getUser(userId);

			for (int i = 0 ;i< from_user_ids.length;i++){
				if (to_deleted[i])
					continue;
				User from_user = from_user_ids[i]==null? null : getUser(from_user_ids[i]);
				inMessages.add(new Message(from_user, to_user, text[i], date[i], time[i]));
			}
		}
		return inMessages.toArray(new Message[0]);
	}

	public Message[] getOutMessages(Integer userId) throws SQLException {
		if (!userExists(userId))
			return null;

		ArrayList<Message> outMessages = new ArrayList<>();

		try (ResultSet rs = statement.executeQuery(String.format(
				"SELECT * FROM messages WHERE from_user_id IS ('%s')",
				userId))) {

			if (rs.isClosed())
				return null;

			Integer[] to_user_ids = (Integer[])rs.getArray("to_user_id").getArray();
			Boolean[] from_deleted = (Boolean[])rs.getArray("from_deleted").getArray();
			String[] text = (String[])rs.getArray("text").getArray();
			Date[] date =  (Date[])rs.getArray("date").getArray();
			Time[] time =  (Time[])rs.getArray("time").getArray();

			User from_user = getUser(userId);

			for (int i = 0 ;i< to_user_ids.length;i++){
				if (from_deleted[i])
					continue;
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
