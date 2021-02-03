package com.alexz.messenger.app.ui.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexz.messenger.app.data.model.Result;
import com.alexz.messenger.app.data.repo.AuthRepository;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.messenger.app.R;


public class LoginActivityViewModel extends ViewModel {

    private final AuthRepository repository = AuthRepository.getInstance();
    private final MutableLiveData<Result<FirebaseUser>> loginResult = new MutableLiveData<>();

    public LiveData<Result<FirebaseUser>> getLoginResult() {
        return loginResult;
    }

    public void login(GoogleSignInAccount account){
        repository.googleLogin(account).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loginResult.postValue(new Result.Success<>(repository.getCurrentUser()));
            }
            else {
                loginResult.postValue(new Result.Error(R.string.error_google_login));
            }
        });
    }


    @Nullable
    public FirebaseUser getCurrentUser() {
        return repository.getCurrentUser();
    }
}
