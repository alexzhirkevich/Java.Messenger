package com.messenger.app.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.messenger.app.R;

public class FullscreenImageActivity extends AppCompatActivity {

    private static final String EXTRA_IMAGE_URI = "IMAGE_URI";
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        String uri = getIntent().getStringExtra(EXTRA_IMAGE_URI);
        imageView = findViewById(R.id.imageview_fullscreen);

        if (imageView != null && uri != null){
            Glide.with(this).load(uri).into(imageView);
        }

        if (imageView!=null) {
            imageView.setOnClickListener(view -> {
               if (getSupportActionBar().isShowing()) {
                   getSupportActionBar().hide();
                  // requestWindowFeature(Window.FEATURE_NO_TITLE);
               }
               else {
                   getSupportActionBar().show();
                   //requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
               }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public static void startActivity(Context context, String imageUri) {
        Intent intent = new Intent(context, FullscreenImageActivity.class);
        intent.putExtra(EXTRA_IMAGE_URI,imageUri);
        context.startActivity(intent);
    }
}