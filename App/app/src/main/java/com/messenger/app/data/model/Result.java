package com.messenger.app.data.model;

import androidx.annotation.StringRes;

public abstract class Result<T> {

    public static class Success<T> extends Result<T>{

        private final T value;

        public Success(T value){
            this.value = value;
        }

        public T get() {
            return value;
        }
    }

    public static class Error extends Result{
        private final @StringRes int error;

        public Error(@StringRes int error){
            this.error = error;
        }

        public @StringRes int getError() {
            return error;
        }
    }
}
