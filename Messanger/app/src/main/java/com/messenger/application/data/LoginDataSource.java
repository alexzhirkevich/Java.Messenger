package com.messenger.application.data;

import com.messenger.application.connection.Connection;
import com.messenger.application.data.model.LoggedInUser;
import com.messenger.application.protocol.Config;
import com.messenger.application.protocol.User;
import com.messenger.application.protocol.response.ResponseLogin;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public static Result<LoggedInUser> login(String phone, String passHash) {

        ResponseLogin response = null;
        try {
            // TODO: handle loggedInUser authentication
            Connection connection = new Connection(Config.host,Config.port);
            response =  connection.login(phone,passHash);
            if (response.error())
                return new Result.Error(new IOException(response.getErrorMsg(), null ));
            LoggedInUser user = new LoggedInUser(connection);
            user.setId(response.getId());

            return new Result.Success<>(user);
        } catch (Exception e) {
            String msg;
            if (response!=null)
                msg = response.getErrorMsg();
            else
                msg = "Error";
            return new Result.Error(new IOException(msg, e));
        }
    }

    public static Result<LoggedInUser> register (User user, String passHash){
        try{

            return null;
        }catch (Exception e){
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}