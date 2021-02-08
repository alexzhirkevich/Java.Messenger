package com.alexz.messenger.app.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.alexz.messenger.app.ChatApplication;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        ((ChatApplication)getApplication()).setRunning(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ChatApplication)getApplication()).setRunning(false);
    }
}
