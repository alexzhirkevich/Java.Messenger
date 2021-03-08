package com.alexz.messenger.app.ui.common.firerecyclerview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.interfaces.IBaseModel;
import com.google.firebase.database.DataSnapshot;

public interface IFirebaseRecyclerAdapter<Model extends IBaseModel,VH extends IFirebaseViewHolder<Model>> {
    /**
     * Same as {@link RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)}
     *
     * Used in{@link FirebaseMapRecyclerAdapter#onCreateViewHolder(ViewGroup, int)}
     *
     * @return {@link FirebaseViewHolder}
     * @see IFirebaseViewHolder
     */
    @NonNull
    VH onCreateClickableViewHolder(@NonNull ViewGroup parent, int viewType);



    /**
     * Decide if model must be selected by selection key
     *
     * Used in {@link FirebaseMapRecyclerAdapter#select(String)}
     * @see IFirebaseRecyclerAdapter#select(String)
     *
     * @param selectionKey key for models selection
     * @param model implements {@link IBaseModel}
     *
     * @return Model field for selection
     */
    boolean onSelect(@Nullable String selectionKey, @NonNull Model model);



    /**
     * Parse Model object from {@link DataSnapshot}
     *
     * @return parsed object
     * */
    Model parse(DataSnapshot snapshot);



    /**
     * Shows only those ViewHolders, which key field contains {@param containsString}
     *
     * Override {@link IFirebaseRecyclerAdapter#onSelect(String, IBaseModel)}
     * to be able to do it.
     *
     * @see IBaseModel#getId() - default key field
     *
     * @param selectionKey substring to find.
     *
     * @return selected count
     */
    int select(@NonNull String selectionKey);



    /**
     * Shows all ViewHolders
     */
    void selectAll();
}
