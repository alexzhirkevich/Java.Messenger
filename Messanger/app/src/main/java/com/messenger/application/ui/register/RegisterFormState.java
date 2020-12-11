package com.messenger.application.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class RegisterFormState {
    @Nullable
    private Integer firstNameError;
    @Nullable
    private Integer lastNameError;
    @Nullable
    private Integer phoneError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer passConfirmError;
    private boolean isDataValid;

    RegisterFormState(@Nullable Integer firstNameError,
                      @Nullable Integer lastNameError,
                      @Nullable Integer phoneError,
                      @Nullable Integer passwordError,
                      @Nullable Integer passConfirmError) {
        this.firstNameError = firstNameError;
        this.lastNameError = lastNameError;
        this.phoneError = phoneError;
        this.passwordError = passwordError;
        this.passConfirmError = passConfirmError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.firstNameError = null;
        this.lastNameError = null;
        this.phoneError = null;
        this.passwordError = null;
        this.passConfirmError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getFirstNameError() {
        return firstNameError;
    }

    @Nullable
    public Integer getLastNameError() {
        return lastNameError;
    }

    @Nullable
    public Integer getPassConfirmError() {
        return passConfirmError;
    }

    @Nullable
    Integer getPhoneError() {
        return phoneError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}