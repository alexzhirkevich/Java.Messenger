package com.messenger.app.ui;

import android.view.View;

public interface RecyclerItemClickListener<T> {

    default void onItemClick(View view, T data){ }

    default boolean onLongItemClick(View view, T data){
        return false;
    };
}
