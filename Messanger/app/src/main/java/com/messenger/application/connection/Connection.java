package com.messenger.application.connection;

import android.util.Log;
import android.widget.Toast;

import com.messenger.R;
import com.messenger.application.data.AuthRepository;
import com.messenger.application.data.Result;
import com.messenger.application.data.model.LoggedInUser;
import com.messenger.application.protocol.Config;
import com.messenger.application.protocol.User;
import com.messenger.application.protocol.request.Request;
import com.messenger.application.protocol.request.RequestDisconnect;
import com.messenger.application.protocol.request.RequestLogin;
import com.messenger.application.protocol.request.RequestRegister;
import com.messenger.application.protocol.response.Response;
import com.messenger.application.protocol.response.ResponseLogin;
import com.messenger.application.protocol.response.ResponseRegister;
import com.messenger.application.xml.Xml;

import org.apache.http.conn.scheme.SocketFactory;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.Provider;


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

    public Result<LoggedInUser> register(User user, String password){

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

    @Override
    public void close() throws IOException {
        logout();
    }
}
