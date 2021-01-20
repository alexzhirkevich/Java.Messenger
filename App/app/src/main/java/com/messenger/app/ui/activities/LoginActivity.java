package com.messenger.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.messenger.app.R;
import com.messenger.app.ui.AvatarImageView;
import com.messenger.app.util.MyGoogleUtils;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity  {

    //google
    GoogleSignInClient client;
    GoogleSignInAccount account;
    SignInButton btnGoogleSighIn;

    AvatarImageView avatar;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnGoogleSighIn = findViewById(R.id.btn_google_sign_in);
        btnGoogleSighIn.setOnClickListener(this::onGoogleSignIn);
        btnGoogleSighIn.setVisibility(View.INVISIBLE);

        avatar = findViewById(R.id.avatar);


        client = MyGoogleUtils.getGoogleSignInClient(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account !=null){
           setupAccount(account);
        } else{
            btnGoogleSighIn.setVisibility(View.VISIBLE);
        }
    }

    void setupAccount(GoogleSignInAccount account){
        MyGoogleUtils.setAccount(account);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hide();
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void onGoogleSignIn(View view) {
        startActivityForResult(client.getSignInIntent(), MyGoogleUtils.REQ_SIGN_IN);
    }

    protected void onActivityResult(final int requestCode, final int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyGoogleUtils.REQ_SIGN_IN && resultCode == RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Toast.makeText(this,"SUCCESS",Toast.LENGTH_SHORT).show();
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            setupAccount(account);
        } catch (ApiException e) {
        }
    }
}