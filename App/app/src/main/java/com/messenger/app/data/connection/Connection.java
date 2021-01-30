package com.messenger.app.data.connection;

import com.messenger.app.BuildConfig;
import com.messenger.protocol.Config;
import com.messenger.protocol.request.Request;
import com.messenger.protocol.response.Response;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection implements Closeable {

    private static volatile Connection instance;

    private final Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private Connection(){
        socket = new Socket();
        try {
            socket.setSoTimeout(5000);
            socket.connect(new InetSocketAddress(Config.host,Config.port),5000);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getInstance() {
        if (instance == null || instance.socket == null || instance.socket.isConnected()){
            instance = new Connection();
        }
        return instance;
    }

    @Override
    public void close() throws IOException {
        if (dis != null){
            dis.close();
        }
        if (dos != null){
            dos.close();
        }
        if (socket!=null){
            socket.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }

    public DataInputStream getInputStream() {
        return dis;
    }

    public DataOutputStream getOutputStream() {
        return dos;
    }

    public Response sendRequest(Request req){
        String strRes = null;
        try {
            dos.writeUTF(req.toString());
            strRes = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.decode(strRes);
    }
}
