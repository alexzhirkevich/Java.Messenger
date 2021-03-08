package com.alexz.messenger.app.ui.common.firerecyclerview;

public interface AdapterCallback<Model> {

    void onItemAdded(Model item);

    void onItemRemoved(Model item);

    default void onItemChanged(Model item){};
}
