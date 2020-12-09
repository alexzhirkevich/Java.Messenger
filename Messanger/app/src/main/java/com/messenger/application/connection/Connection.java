package com.messenger.application.connection;

import com.messenger.application.protocol.User;
import com.messenger.application.protocol.request.Request;
import com.messenger.application.protocol.request.RequestLogin;
import com.messenger.application.protocol.response.Response;
import com.messenger.application.protocol.response.ResponseLogin;
import com.messenger.application.protocol.response.ResponseRegister;
import com.messenger.application.protocol.response.Responser;
import com.messenger.application.xml.Xml;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class Connection extends Thread{

    private String host;
    private int port;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public Connection(String host, int port) throws IOException, InterruptedException {
        this.host = host;
        this.port = port;
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 10000);
        socket.setSoTimeout(5000);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.flush();
        dataInputStream = new DataInputStream(socket.getInputStream());
    }

    public ResponseRegister register(User user, String passHash){
        return null;
    }

    public ResponseLogin login(String phone, String passHash) throws IOException {
        try {
            return (ResponseLogin) sendRequest(new RequestLogin(phone,passHash));
        } catch (Exception e){
            return new ResponseLogin(Responser.RES_ERROR,-1,"Login error");
        }
    }

    public Response sendRequest(Request req) throws Exception {
        String xml = Xml.toXml(req);
        dataOutputStream.writeUTF(Xml.toXml(req));
       // Response  res = (Response) (dataInputStream.readObject());
        return (Response) Xml.fromXml(dataInputStream.readUTF());
    }
}
