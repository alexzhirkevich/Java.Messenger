package com.alexz.messenger.app.ui.common.firerecyclerview;

import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder<Model>
        extends RecyclerView.ViewHolder
        implements IFirebaseViewHolder<Model> {

    private Model model;

    public FirebaseViewHolder(@NonNull View itemView) {
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
