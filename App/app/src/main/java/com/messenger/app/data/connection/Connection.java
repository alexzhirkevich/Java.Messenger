package com.messenger.app.data.connection;

import okhttp3.OkHttpClient;

public class Connection {

    private String url = "http:\\192.168.100.3:3000";
    private OkHttpClient client;

    private Connection(){
        client = new OkHttpClient();
    }
}
