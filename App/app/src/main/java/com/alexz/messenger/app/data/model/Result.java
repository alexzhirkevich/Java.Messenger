package com.alexz.messenger.app.data.model;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;


import java.util.HashSet;
import java.util.Set;

public abstract class Result<T> {

    public static class Success<T> extends Result<T> implements ISuccess<T> {

        private final T value;

        private Success() { value = null; }

        public Success(T value) { this.value = value; }

        @Override
        public T get() {
            return value;
        }
    }



    public static class Error extends Result implements IError {

        private final @StringRes int error;

        public Error(@StringRes int error) { this.error = error; }

        @Override
        public @StringRes int getError() { return error; }
    }



    public static abstract class Future<T> extends Result.Success<T> implements IFuture<T>{

        protected final Set<ResultListener<T>> listeners = new HashSet<>();

        protected boolean hasProgress = false;

        private Future() { }

        @Override
        public void addResultListener(ResultListener<T> listener) { listeners.add(listener); }

        @Override
        public void removeResultListener(ResultListener<T> listener){ listeners.remove(listener); }

        @Override
        public void clearResultListeners() { listeners.clear(); }

        @Override
        public boolean hasProgress() {
            return hasProgress;
        }
    }

    public static class MutableFuture<T> extends Result.Future<T> implements IFuture<T>, IMutable<T> {

        @Override
        public void setHasProgress(boolean hasProgress) {
            this.hasProgress = hasProgress;
        }

        @Override
        public void set(Result<T> result) {
            if (!listeners.isEmpty()) {
                if (result instanceof ISuccess) {
                    for (ResultListener<T> listener : listeners) {
                        new Handler(Looper.getMainLooper()).post(() -> listener.onSuccess((Success<T>) result));
                    }
                } else if (result instanceof IError) {
                    for (ResultListener<T> listener : listeners) {
                        new Handler(Looper.getMainLooper()).post(() -> listener.onError((Error) result));
                    }
                } else {
                    throw new IllegalArgumentException("Future can not be result of Future");
                }
            }
        }

        @Override
        public void setProgress(Double percent) {
            if (!listeners.isEmpty()) {
                for (ResultListener<T> listener : listeners) {
                    new Handler(Looper.getMainLooper()).post(() -> listener.onProgress(percent));
                }
            }
        }
    }



    public interface ResultListener<T> {

        @UiThread
        void onSuccess(@NonNull  ISuccess<T> result);

        @UiThread
        void onError(@Nullable IError error);

        @UiThread
        default void onProgress(@Nullable Double percent){}
    }



    public interface ISuccess<T>{
        T get();
    }



    public interface IError{
        @StringRes int getError();
    }



    public interface IFuture<T> {
        void addResultListener(ResultListener<T> listener);

        void removeResultListener(ResultListener<T> listener);

        void clearResultListeners();

        boolean hasProgress();
    }



    public interface IMutable<T>{
        void set(Result<T> result);

        void setProgress(Double percent);

        void setHasProgress(boolean hasProgress);
    }
}
