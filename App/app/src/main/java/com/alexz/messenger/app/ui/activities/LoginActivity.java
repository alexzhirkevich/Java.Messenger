package com.alexz.messenger.app.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.alexz.messenger.app.data.model.Result;
import com.alexz.messenger.app.ui.viewmodels.LoginActivityViewModel;
import com.alexz.messenger.app.util.MetrixUtil;
import com.alexz.messenger.app.util.MyGoogleUtils;
import com.alexz.messenger.app.util.VibrateUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.messenger.app.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String STR_COUNTER = "counter";
    private SignInButton btnGoogleSighIn;
    private LoginActivityViewModel viewModel;
    private CardView logo;
    private int counter = 0;

    private TextView question;
    private Button btn1, btn2;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnGoogleSighIn = findViewById(R.id.btn_google_sign_in);
        btnGoogleSighIn.setOnClickListener(this::onGoogleSignIn);
        btnGoogleSighIn.setVisibility(View.INVISIBLE);
        question = findViewById(R.id.question);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);

        btn1.setVisibility(View.INVISIBLE);
        btn2.setVisibility(View.INVISIBLE);
        question.setVisibility(View.INVISIBLE);

        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        viewModel.getLoginResult().observe(this, result -> {
            if (result instanceof Result.Success){
                updateUI();
            } else {
                Toast.makeText(this,getString(((Result.Error)result).getError()), Toast.LENGTH_SHORT).show();
            }
        });

        logo = findViewById(R.id.logo);
        logo.setOnClickListener(this);

        View.OnClickListener easter = view -> {
            View view1 = LayoutInflater.from(this).inflate(R.layout.easter,null,false);
            Toast t = new Toast(this);
            t.setView(view1);
            t.setDuration(Toast.LENGTH_LONG);
            t.show();
            question.setVisibility(View.INVISIBLE);
            btn1.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);
            logo.animate().scaleX(1f).scaleY(1f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationEnd(animation);
                    logo.setVisibility(View.VISIBLE);
                }
            }).start();
            VibrateUtil.with(this).vibrate(100,VibrateUtil.POWER_LOW);
        };
        btn1.setOnClickListener(easter);
        btn2.setOnClickListener(easter);
    }

    private void updateUI(){
        new Handler(Looper.getMainLooper()).postDelayed(() ->
               startActivity(new Intent(this, DialogsActivity.class)),100);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = viewModel.getCurrentUser();
        if (user != null) {
            updateUI();
        } else{
            btnGoogleSighIn.setVisibility(View.VISIBLE);
        }
    }

    void setupAccount(GoogleSignInAccount account){
        ;
        viewModel.login(account);
    }

    public void onGoogleSignIn(View view) {
        try {
            int v = getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0 ).versionCode;
            if (v<12451000) {
                Toast.makeText(this, getString(R.string.error_gservices_ver) + v, Toast.LENGTH_LONG).show();
                return;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return;
        }

        startActivityForResult(MyGoogleUtils.getGoogleSignInClient(this).getSignInIntent(), MyGoogleUtils.REQ_SIGN_IN);
    }

    protected void onActivityResult(final int requestCode, final int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyGoogleUtils.REQ_SIGN_IN && resultCode == RESULT_OK) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                viewModel.login(account);
                btnGoogleSighIn.setVisibility(View.INVISIBLE);
            } catch (ApiException ignored) {
                Toast.makeText(this,getString(R.string.error_google_login), Toast.LENGTH_LONG).show();
            }
        } else
            Toast.makeText(this,getString(R.string.error_google_login), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STR_COUNTER,counter);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        counter = savedInstanceState.getInt(STR_COUNTER);
        btnGoogleSighIn = findViewById(R.id.btn_google_sign_in);
        logo = findViewById(R.id.logo);
        question = findViewById(R.id.question);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
    }

    @Override
    public void onClick(View view) {
        switch (counter){
            case 2:
                Toast.makeText(this,R.string.easter_1,Toast.LENGTH_LONG).show();
                counter++;
                break;
            case 10:
                Toast.makeText(this,R.string.easter_2,Toast.LENGTH_SHORT).show();
                counter++;
                break;
            case 20:
                Toast.makeText(this,R.string.easter_3,Toast.LENGTH_SHORT).show();
                counter++;
                break;
            case 30:
                Toast.makeText(this,R.string.easter_4,Toast.LENGTH_SHORT).show();
                counter++;
                break;
            case 50:
                logo.animate().translationYBy(MetrixUtil.dpToPx(this,300)).setDuration(300).start();
                Toast.makeText(this,R.string.easter_5,Toast.LENGTH_SHORT).show();
                counter++;
                break;
            case 60:
                logo.animate().translationYBy(MetrixUtil.dpToPx(this,-300)).setDuration(300).start();
                Toast.makeText(this,R.string.easter_6,Toast.LENGTH_SHORT).show();
                counter++;
                break;
            case 70:
                logo.animate().scaleX(0.1f).scaleY(0.1f).setDuration(300).start();
                Toast.makeText(this,R.string.easter_7,Toast.LENGTH_SHORT).show();
                counter++;
                break;
            case 71:
            case 79:
                logo.animate().translationXBy(150).setDuration(300).start();
                counter++;
                break;
            case 73:
            case 75:
            case 77:
                logo.animate().translationXBy(300).setDuration(300).start();
                counter++;
                break;
            case 72:
            case 74:
            case 76:
            case 78:
                logo.animate().translationXBy(-300).setDuration(300).start();
                counter++;
                break;
            case 80:
                Toast.makeText(this,R.string.easter_8,Toast.LENGTH_SHORT).show();
                counter++;
                break;
            case 90:
                logo.animate().scaleX(2f).scaleY(2f).setDuration(100).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        logo.animate().scaleX(1f).scaleY(1f).setDuration(200).setStartDelay(500).start();
                    }
                }).start();
                VibrateUtil.with(this).vibrate(300,VibrateUtil.POWER_HIGH);
                Toast.makeText(this,R.string.easter_9,Toast.LENGTH_SHORT).show();
                counter++;
                break;
            case 100:
                VibrateUtil v = VibrateUtil.with(this).vibrate(150,VibrateUtil.POWER_MEDUIM);
                logo.postDelayed((Runnable) () -> v.vibrate(560,VibrateUtil.POWER_MEDUIM),200);
                Toast.makeText(this,R.string.easter_10,Toast.LENGTH_SHORT).show();
                counter++;
                break;
            case 110:
                Toast.makeText(this,R.string.easter_11,Toast.LENGTH_SHORT).show();
                counter++;
                break;
            case 120:
                counter++;
                question.setText(R.string.easter_kitties);
                btn1.setText(R.string.yes);
                btn2.setText(R.string.easter_yes);
                question.setVisibility(View.VISIBLE);
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                VibrateUtil.with(this).vibrate(50,VibrateUtil.POWER_LOW);
                logo.animate().scaleX(0f).scaleY(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        logo.setVisibility(View.INVISIBLE);
                    }
                }).start();
                break;
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
                VibrateUtil.with(this).vibrate(counter%10*10,VibrateUtil.POWER_LOW);
                counter++;
                break;
            case 130:
                logo.animate().rotationX(360).setDuration(1000).start();
                VibrateUtil.with(this).vibrate(1000,VibrateUtil.POWER_HIGH);
                counter++;
                break;
            case 150:
                Toast.makeText(this,R.string.easter_14,Toast.LENGTH_LONG).show();
                VibrateUtil vi = VibrateUtil.with(this);
                new Thread( () ->{
                    try {
                        for (int i = 0; i < 4; i++) {
                            vi.vibrate(100, VibrateUtil.POWER_LOW);
                            Thread.sleep(150);
                        }
                        Thread.sleep(150);
                        vi.vibrate(150, VibrateUtil.POWER_LOW);
                        Thread.sleep(170);
                        vi.vibrate(1000);
                    }  catch (InterruptedException ignore) { }
                }).start();
                counter++;
                break;
            case 160:
                Toast.makeText(this,R.string.easter_15,Toast.LENGTH_LONG).show();
                logo.setOnClickListener(this::onGoogleSignIn);

            default:
                counter++;
        }
    }
}