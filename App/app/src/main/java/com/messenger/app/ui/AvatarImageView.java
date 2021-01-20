package com.messenger.app.ui;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.content.res.AppCompatResources;

import com.bumptech.glide.Glide;
import com.messenger.app.R;

public class AvatarImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Drawable circle;
    private String imageUri;

    public AvatarImageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AvatarImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AvatarImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context){
        circle = AppCompatResources.getDrawable(context, R.drawable.drawable_circle);
        int p = (int)getResources().getDimension(R.dimen.avatar_padding);
        setPadding(p,p,p,p);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    @Override
    public void setImageURI(@Nullable Uri uri) {
        if (uri!=null) {
            Glide.with(this).load(uri).circleCrop().into(this);
            imageUri = uri.toString();
        }
    }

    public String getImageUri() {
        return imageUri;
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(new LayerDrawable(new Drawable[]{ drawable,circle}));
    }
}