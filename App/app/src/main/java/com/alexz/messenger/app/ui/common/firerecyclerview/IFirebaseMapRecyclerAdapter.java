package com.alexz.messenger.app.ui.common.firerecyclerview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.interfaces.IBaseModel;
import com.google.firebase.database.Query;

/**
 * RecyclerView adapter for Firebase Realtime Database objects stored by key
 * @param <Model> - object;
 * @param <VH> - ViewHolder;
 *
 * @see IBaseModel
 * @see IFirebaseViewHolder
 * */
public interface IFirebaseMapRecyclerAdapter<Model extends IBaseModel,VH extends IFirebaseViewHolder<Model>>
        extends IFirebaseRecyclerAdapter<Model,VH> {

    /**
     * @return Firebase {@link Query} object for Models key set
     * @see IBaseModel#getId() - key
     */
    @NonNull
    Query onCreateKeyQuery();



    /**
     * @return Firebase {@link Query} object for Model by key
     * @see IBaseModel#getId() - key
     */
    @NonNull
    Query onCreateModelQuery(@NonNull String modelId);



    /**
     *  Called when model DataSnapshot, got by key, not exists
     * @see IBaseModel#getId() - key
     * @see com.google.firebase.database.DataSnapshot
     */
    void onModelNotFound(@NonNull String modelId);
}
