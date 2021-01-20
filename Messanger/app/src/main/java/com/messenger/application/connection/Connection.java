package com.messenger.application.connection;

import com.messenger.R;
import com.messenger.application.data.Result;
import com.messenger.application.data.model.Dialog;
import com.messenger.application.data.model.DialogMessage;
import com.messenger.application.data.model.LoggedInUser;
import com.messenger.application.data.model.MessageUser;
import com.messenger.application.protocol.Chat;
import com.messenger.application.protocol.Config;
import com.messenger.application.protocol.Message;
import com.messenger.application.protocol.User;
import com.messenger.application.protocol.request.Request;
import com.messenger.application.protocol.request.RequestChat;
import com.messenger.application.protocol.request.RequestDialogMembers;
import com.messenger.application.protocol.request.RequestDisconnect;
import com.messenger.application.protocol.request.RequestLogin;
import com.messenger.application.protocol.request.RequestRegister;
import com.messenger.application.protocol.response.Response;
import com.messenger.application.protocol.response.ResponseChat;
import com.messenger.application.protocol.response.ResponseDialogMembers;
import com.messenger.application.protocol.response.ResponseLogin;
import com.messenger.application.protocol.response.ResponseRegister;
import com.messenger.application.xml.Xml;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;


public class Connection implements Closeable {

    private static volatile Connection instance = null;

    private String host;
    private int port;
    private Socket socket=null;
    private DataInputStream dataInputStream=null;
    private DataOutputStream dataOutputStream=null;
    private User user = null;

    private Connection() throws IOException {
        this.host = Config.host;
        this.port = Config.port;
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 10000);
        socket.setSoTimeout(5000);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.flush();
        dataInputStream = new DataInputStream(socket.getInputStream());
    }

    public static Connection getInstance() throws IOException {
        if (instance == null){
            instance = new Connection();
            return instance;
        }
        return instance;
    }

    public Result<LoggedInUser> register(com.messenger.application.protocol.User user, String password){

        ResponseRegister response = null;
        try {
            response = (ResponseRegister) sendRequest(new RequestRegister(user,password));
            if (response.error()){
                switch (response.getId()){
                    case Response.RES_USEREXISTS:
                        return new Result.Error(R.string.error_user_exist);
                    default:
                        return new Result.Error(R.string.error_register);
                }

            }

            this.user = response.getUser();

            LoggedInUser loggedInUser = new LoggedInUser(user,this);
            return new Result.Success<>(loggedInUser);
        } catch (Exception e){
            return new Result.Error(R.string.error_register);
        }
    }

    public Result<LoggedInUser> login(String phone, String password) {

        ResponseLogin response = null;
        try {
            response = (ResponseLogin) sendRequest(new RequestLogin(phone,password));
            if (response.error()){
                switch (response.getId()){
                    case Response.RES_WRONGPASSWORD:
                        return new Result.Error(R.string.error_wrong_password);
                    case Response.RES_USERNOTEXISTS:
                        return new Result.Error(R.string.error_user_does_not_exist);
                    default:
                        return new Result.Error(R.string.error_login);
                }
            }
            LoggedInUser user = new LoggedInUser(response.getUser(),this);
            return new Result.Success<>(user);
        } catch (Exception e){
            return new Result.Error(R.string.error_login);
        }
    }

    public Response sendRequest(Request req) throws Exception {
        String xml = Xml.toXml(req);
        dataOutputStream.writeUTF(Xml.toXml(req));
        return (Response) Xml.fromXml(dataInputStream.readUTF());
    }

    public void logout(){
        if (instance!=null){
            try {
                sendRequest(new RequestDisconnect());
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
                instance = null;
            } catch (Exception ignore){}
        }
    }

    public Result<Dialog[]> getDialogList() throws Exception {
        if (user == null)
            return new Result.Error(R.string.error_not_authorized);
        if (instance == null)
            return new Result.Error(R.string.error_login);
        ArrayList<Dialog> dialogs = new ArrayList<>();
        ResponseDialogMembers res = (ResponseDialogMembers) sendRequest(new RequestDialogMembers(user));
        for (Integer i : res.getDialogMemberIds()) {
            try {
                ResponseChat responseChat = (ResponseChat) sendRequest(new RequestChat(user.getId(), i));
                Chat chat = responseChat.getChat();
                if (chat != null) {
                    ArrayList<MessageUser> users = new ArrayList<>();
                    Message[] messages = chat.getMessages();
                    DialogMessage[] dialogMessages = new DialogMessage[messages.length];
                    for (int k = 0; k < messages.length; k++) {
                        MessageUser user = this.user.getId().equals(messages[i].getFromUser().getId()) ? new MessageUser(messages[i].getFromUser()) : new MessageUser(messages[i].getToUser());
                        dialogMessages[i] = new DialogMessage(messages[i].getId(), user, messages[i].getText());
                    }
                    User a = chat.getUser1();
                    User b = chat.getUser2();
                    users.add(new MessageUser(a));
                    users.add(new MessageUser(b));

                    Dialog dialog = new Dialog(chat.getId(), b.getFirstName() + " " + b.getLastName(), b.getAvatar(), users, dialogMessages[dialogMessages.length - 1], 0);
                    dialogs.add(dialog);
                }
            } catch (Exception ignore) {
            }
        }
        return new Result.Success<>(dialogs.toArray());
    }

    @Override
    public void close() throws IOException {
        logout();
    }
}
