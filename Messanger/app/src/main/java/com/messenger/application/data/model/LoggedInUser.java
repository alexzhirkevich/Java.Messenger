package com.messenger.application.data.model;

import com.messenger.application.connection.Connection;
import com.messenger.application.protocol.User;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser extends User {

    public final Connection connection;

    public LoggedInUser(User user, Connection connection) {
        super(user);
        this.connection = connection;
    }
}