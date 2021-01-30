package com.messenger.app.data.repo;

import com.messenger.app.R;
import com.messenger.app.data.connection.Connection;
import com.messenger.app.data.model.Result;
import com.messenger.app.util.MyGoogleUtils;
import com.messenger.protocol.request.Request;
import com.messenger.protocol.request.RequestGoogleLogin;
import com.messenger.protocol.response.IResponse;
import com.messenger.protocol.response.Response;
import com.messenger.protocol.response.ResponseLogin;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class AuthRepository {

    private static volatile AuthRepository instance;

    private AuthRepository() {
    }

    public static AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    public Result<String> googleLogin(String gToken) {

//        if (gToken == null) {
//            return new Result.Error(R.string.error_google_connecting);
//        }
//        Request req = new RequestGoogleLogin(gToken);
//        Response res = Connection.getInstance().sendRequest(req);
//        if (res instanceof ResponseLogin) {
//            if (res.getResponseCode() == IResponse.RES_OK) {
//                return new Result.Success<>(((ResponseLogin) res).getToken());
//            } else {
//                //TODO error variations 4
//            }
//        }
//        return new Result.Error(R.string.error_server);
        return new Result.Success<>("QWE");
    }
}
