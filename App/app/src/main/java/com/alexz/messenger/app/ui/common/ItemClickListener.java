package com.alexz.messenger.app.ui.common;

import android.view.View;

public interface ItemClickListener<T> {

    default void onItemClick(View view, T data){ }

    default boolean onLongItemClick(View view, T data){
        return false;
    };
}
