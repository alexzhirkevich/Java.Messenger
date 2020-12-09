package com.messenger.application.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.os.AsyncTask;
import android.util.Patterns;

import com.messenger.application.data.LoginRepository;
import com.messenger.application.data.Result;
import com.messenger.application.data.model.LoggedInUser;
import com.messenger.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String phone, String passHash) {

        new AsyncTask<Void, Void, LoginResult>() {

            @Override
            protected LoginResult doInBackground(Void... voids) {
                Result<LoggedInUser> result = loginRepository.login(phone, passHash);

                if (result instanceof Result.Success) {
                    LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                   return new LoginResult(new LoggedInUserView(data.getFirstName(), data.getLastName(), data.getPhone()));
                } else {
                   return new LoginResult(R.string.login_failed);
                }
            }

            @Override
            protected void onPostExecute(LoginResult res) {
               loginResult.setValue(res);
            }
        }.execute();
    }

    public void loginDataChanged(String username, String password) {
        if (!isPhoneValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isPhoneValid(String phone) {
        if (phone == null) {
            return false;
        }
        if (phone.contains("@")) {
            return Patterns.PHONE.matcher(phone).matches();
        } else {
            return !phone.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}