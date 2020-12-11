package com.messenger.application.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.os.AsyncTask;
import android.util.Pair;

import com.messenger.application.data.AuthRepository;
import com.messenger.application.data.Result;
import com.messenger.application.data.model.LoggedInUser;
import com.messenger.R;
import com.messenger.application.ui.register.RegisterViewModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private AuthRepository authRepository;

    class LoginTask extends AsyncTask<Pair<String,String>,Void,LoginResult>{
        @Override
        protected LoginResult doInBackground(Pair<String,String>... phoneNpass) {

            Result<LoggedInUser> result = authRepository.login(phoneNpass[0].first, phoneNpass[0].second);

            if (result instanceof Result.Success) {
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                return new LoginResult(new LoggedInUserView(data.getFirstName(), data.getLastName(), data.getPhone()));
            } else {
                return new LoginResult(((Result.Error)result).getError());
            }
        }

        @Override
        protected void onPostExecute(LoginResult res) {
            loginResult.setValue(res);
        }
    }

    LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    @SuppressWarnings("unchecked")
    public void login(String phone, String  password) {
        new LoginTask().execute(new Pair<>(phone,password));
    }

    public void registerDataChanged(String username, String password) {
        if (!RegisterViewModel.isPhoneValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_phone, null));
        } else if (!RegisterViewModel.isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }
}