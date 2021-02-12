package com.alexz.messenger.app.ui.common.firerecyclerview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.interfaces.IBaseModel;
import com.google.firebase.database.Query;

public interface IFirebaseMapRecyclerAdapter<Model extends IBaseModel,VH extends IFirebaseMapViewHolder<Model>> {

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
    Query onCreateModelQuery(String modelId);



    /**
     * Same as {@link RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)}
     *
     * Used in{@link FirebaseMapRecyclerAdapter#onCreateViewHolder(ViewGroup, int)}
     *
     * @return {@link FirebaseMapViewHolder}
     * @see IFirebaseMapViewHolder
     */
    @NonNull
    VH onCreateClickableViewHolder(@NonNull ViewGroup parent, int viewType);

    /**
     * Used in {@link FirebaseMapRecyclerAdapter#select(String, boolean)}
     * @see IFirebaseMapRecyclerAdapter#select(String, boolean)
     *
     * @param model implements {@link IBaseModel}
     *
     * @return Model field for selection
     */
    @NonNull
    default String onGetFieldForSelection(Model model) { return model.getId(); }



    /**
     * Shows only those ViewHolders, which key field contains {@param containsString}
     * @see IBaseModel#getId() - default key field
     * @see IFirebaseMapRecyclerAdapter#onGetFieldForSelection(IBaseModel): override to set up field for selection
     *
     * @param containsString substring to find.
     * @param ignoreCase ignore case in key field and <b>containsString</b>
     *
     * @return selected ViewHolders count
     */
    int select(String containsString, boolean ignoreCase);



    /**
     * Shows all ViewHolders
     */
    void selectAll();
}
