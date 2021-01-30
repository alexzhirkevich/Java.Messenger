package com.messenger.protocol.request;

public class RequestGoogleLogin extends Request {

    private String gToken;

    public RequestGoogleLogin(String gToken) {
        super(REQ_LOGIN_GOOGLE, gToken);
        this.gToken = gToken;
    }

    @Override
    public String getToken() {
        return token;
    }
}
