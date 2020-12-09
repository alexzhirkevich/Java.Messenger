package com.messenger.application.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String firstName;
    private String secondName;
    private String phone;

    LoggedInUserView(String firstName,String secondName,String phone) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.phone = phone;
    }

    String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getPhone() {
        return phone;
    }
}