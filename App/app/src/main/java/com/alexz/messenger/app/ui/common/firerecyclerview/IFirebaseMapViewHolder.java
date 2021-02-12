package com.alexz.messenger.app.ui.common.firerecyclerview;

import com.alexz.messenger.app.data.model.imp.BaseModel;
import com.alexz.messenger.app.data.model.interfaces.IBaseModel;


public interface IFirebaseMapViewHolder<Model extends IBaseModel> {


    /**
     * @return {@link IBaseModel} - binded model
     */
    Model getModel();



    /**
     * Override this method to bind a view holder
     * 
     * Used in
     * @see FirebaseMapRecyclerAdapter#onBindViewHolder(FirebaseMapViewHolder, int) 
     * 
     * @param model extends <b>IBaseModel</b>
     * @see IBaseModel
     */
    void bind(Model model);
}
