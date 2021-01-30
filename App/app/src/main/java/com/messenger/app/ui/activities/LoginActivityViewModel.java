package com.messenger.app.ui.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.messenger.app.data.model.Result;
import com.messenger.app.data.repo.AuthRepository;


public class LoginActivityViewModel extends ViewModel {

    private final AuthRepository repository = AuthRepository.getInstance();
    private final MutableLiveData<Result<String>> loginResult = new MutableLiveData<>();

    public LiveData<Result<String>> getLoginResult() {
        return loginResult;
    }

    public void login(String gToken){
        new Thread(() -> loginResult.postValue(repository.googleLogin(gToken))).start();
    }
}
