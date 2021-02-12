package com.alexz.messenger.app.ui.common.firerecyclerview;

public interface ObservableLoader {

    /**
     * Callback when loading started
     */
    void setOnStartLoadingListener(Runnable runnable);



    /**
     * Callback when loading ended
     */
    void setOnEndLoadingListener(Runnable runnable);

}
