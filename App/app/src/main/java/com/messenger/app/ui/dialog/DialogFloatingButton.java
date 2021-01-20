package com.messenger.app.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.messenger.app.R;

public class DialogFloatingButton extends FloatingActionButton {

    private boolean moving;

    private Animation hide;
    private Animation show;

    public DialogFloatingButton(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DialogFloatingButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogFloatingButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        moving = false;

        hide =AnimationUtils.loadAnimation(getContext(), R.anim.anim_fb_hide);
        hide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.INVISIBLE);
                moving = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        show= AnimationUtils.loadAnimation(getContext(),R.anim.anim_fb_show);
        show.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moving = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void hide() {
        if (!moving && getVisibility() == VISIBLE) {
            setVisibility(INVISIBLE);
            moving = true;
           startAnimation(hide);
        }
    }

    @Override
    public void show() {
        if (!moving && getVisibility() == INVISIBLE) {
            setVisibility(VISIBLE);
            moving = true;
            startAnimation(show);
        }
    }
}
