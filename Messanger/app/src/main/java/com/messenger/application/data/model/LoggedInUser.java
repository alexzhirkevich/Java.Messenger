package com.messenger.application.data.model;

import com.messenger.application.connection.Connection;
import com.messenger.application.protocol.User;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser extends User {

    private Connection connection;

    public LoggedInUser(Connection connection) {
        this.connection = connection;
    }
}