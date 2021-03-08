package com.alexz.messenger.app.ui.common.firerecyclerview;

import androidx.annotation.NonNull;

import com.alexz.messenger.app.data.model.interfaces.IBaseModel;
import com.google.firebase.database.Query;

public interface IFirebaseListRecyclerAdapter<Model extends IBaseModel,VH extends IFirebaseViewHolder<Model>>
        extends IFirebaseRecyclerAdapter<Model,VH> {

    /**
     * @return Firebase {@link Query} object for Models list
     */
    @NonNull
    Query onCreateModelsQuery();
}
