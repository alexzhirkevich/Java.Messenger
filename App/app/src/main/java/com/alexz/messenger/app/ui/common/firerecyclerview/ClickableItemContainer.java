package com.alexz.messenger.app.ui.common.firerecyclerview;

public interface ClickableItemContainer<T> {

    void setOnItemClickListener(RecyclerItemClickListener<T> listener);
}
