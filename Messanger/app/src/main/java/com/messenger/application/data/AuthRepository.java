package com.messenger.application.data;

import com.messenger.R;
import com.messenger.application.connection.Connection;
import com.messenger.application.data.model.LoggedInUser;
import com.messenger.application.protocol.Config;
import com.messenger.application.protocol.User;
import com.messenger.application.protocol.response.Response;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class AuthRepository {

    private static volatile AuthRepository instance;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private AuthRepository() {
    }

    public static AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        if (user == null)
            return;
        user.connection.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }

    public Result<LoggedInUser> login(String phone, String password) {
        if (user != null)
            return new Result.Error(R.string.error_already_logged_in);
        Connection con = null;
        try {
            con = Connection.getInstance();
        } catch (Exception e){
            return new Result.Error(R.string.error_login);
        }
        Result<LoggedInUser> result = con.login(phone, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }

    public Result<LoggedInUser> register(User user, String password) {
        if (this.user != null)
            return new Result.Error(R.string.error_already_logged_in);
        Connection con = null;
        try {
            con = Connection.getInstance();
        } catch (Exception e){
            return new Result.Error(R.string.error_register);
        }
        Result<LoggedInUser> result = con.register(user, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}