package com.messenger.application.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.os.AsyncTask;
import android.util.Pair;
import android.util.Patterns;

import com.messenger.application.data.AuthRepository;
import com.messenger.application.data.Result;
import com.messenger.application.data.model.LoggedInUser;
import com.messenger.R;
import com.messenger.application.protocol.User;
import com.messenger.application.ui.login.LoggedInUserView;
import com.messenger.application.ui.login.LoginResult;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private AuthRepository authRepository;

    class RegisterTask extends AsyncTask<Pair<User,String>,Void, LoginResult> {
        @Override
        protected LoginResult doInBackground(Pair<User,String>... userNpass) {

            Result<LoggedInUser> result = authRepository.register(userNpass[0].first, userNpass[0].second);

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

    RegisterViewModel(AuthRepository au) {
        this.authRepository = au;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<LoginResult> getRegisterResult() {
        return loginResult;
    }


    @SuppressWarnings("unchecked")
    public void register(User user, String password) {
       new RegisterTask().execute(new Pair<>(user,password));
    }

    public void loginDataChanged(String firstName, String lastName, String phone, String password,String confirmPassword) {
        if (!isFirstNameValid(firstName)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_first_name, null, null, null, null));
            return;
        } else if (!isLastNameValid(lastName) && !lastName.isEmpty()) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_last_name, null, null, null));
            return;
        } else if (!isPhoneValid(phone)&& !phone.isEmpty()) {
            registerFormState.setValue(new RegisterFormState(null,null,R.string.invalid_phone, null,null));
            return;
        } else if (!isPasswordValid(password)&& !password.isEmpty()) {
            registerFormState.setValue(new RegisterFormState(null, null,null,R.string.invalid_password,null));
            return;
        } else if (!confirmPassword.equals(password) && !confirmPassword.isEmpty()){
            registerFormState.setValue(new RegisterFormState(null,null,null,null, R.string.invalid_not_the_same_pass));
            return;
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    public static boolean isPhoneValid(String phone) {
        if (phone!=null && !phone.isEmpty() && phone.charAt(0) == '+') {
            return Patterns.PHONE.matcher(phone).matches();
        } else {
            return false;
        }
    }

    public static boolean isFirstNameValid(String firstName){
        return (firstName != null && !firstName.isEmpty() && firstName.matches("[a-zA-Zа-яА-Я]*"));
    }

    public static boolean isLastNameValid(String lastName){
        return ((lastName == null || lastName.isEmpty()) ||
                lastName.matches("[a-zA-Zа-яА-Я]*"));
    }

    // A placeholder password validation check
    public static boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}