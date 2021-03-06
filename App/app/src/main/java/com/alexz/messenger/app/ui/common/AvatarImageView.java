package com.alexz.messenger.app.ui.common;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.messenger.app.BuildConfig;
import com.messenger.app.R;

public class AvatarImageView extends AppCompatImageView {

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
        if (circle == null) {
            circle = AppCompatResources.getDrawable(context, R.drawable.drawable_circle);
        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    @Override
    public void setImageURI(@Nullable Uri uri) {
        if (uri!=null) {
            Glide.with(this).load(uri).circleCrop().addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    if (BuildConfig.DEBUG && e != null)
                        e.printStackTrace();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    setImageDrawable(resource);
                    return true;
                }
            }).diskCacheStrategy(DiskCacheStrategy.ALL).submit();
            imageUri = uri.toString();
        }
    }

    public String getImageUri() {
        return imageUri;
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (circle == null){
            circle = AppCompatResources.getDrawable(getContext(), R.drawable.drawable_circle);
        }
        try {
            super.setImageDrawable(new LayerDrawable(new Drawable[]{drawable, circle}));
        }catch (Exception e){
            super.setImageDrawable(drawable);
        }
    }
}
