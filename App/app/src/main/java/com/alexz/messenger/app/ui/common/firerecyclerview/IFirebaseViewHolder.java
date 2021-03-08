package com.alexz.messenger.app.ui.common.firerecyclerview;


public interface IFirebaseViewHolder<Model> {


    /**
     * @return binded model
     */
    Model getModel();



    /**
     * Override this method to bind a view holder
     * 
     * Used in
     * @see FirebaseMapRecyclerAdapter#onBindViewHolder(FirebaseViewHolder, int)
     *
     */
    void bind(Model model);
}
