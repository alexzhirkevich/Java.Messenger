package com.alexz.messenger.app.ui.common.firerecyclerview;

import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.imp.BaseModel;
import com.alexz.messenger.app.data.model.interfaces.IBaseModel;

public class FirebaseMapViewHolder<Model extends IBaseModel>
        extends RecyclerView.ViewHolder
        implements IFirebaseMapViewHolder<Model>{

    private Model model;

    public FirebaseMapViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @CallSuper
    @Override
    public void bind(Model model){
        this.model = model;
    }

    @Override
    public final Model getModel() {
        return model;
    }
}
