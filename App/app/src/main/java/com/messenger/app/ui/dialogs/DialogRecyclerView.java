package com.messenger.app.ui.dialogs;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DialogRecyclerView extends RecyclerView {

    private DialogRecyclerAdapter adapter;

    public DialogRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public DialogRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setLayoutManager(new LinearLayoutManager(context));
        adapter = new DialogRecyclerAdapter();
        setAdapter(adapter);
    }

    @Nullable
    @Override
    public DialogRecyclerAdapter getAdapter() {
        return adapter;
    }
}
