package com.messenger.app.ui.messages;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.messenger.app.ui.dialogs.DialogItem;

import java.util.List;

public class MessageRecyclerView extends RecyclerView {

    private MessageRecyclerAdapter adapter;

    public MessageRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public MessageRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setLayoutManager(new LinearLayoutManager(context));
        adapter = new MessageRecyclerAdapter();
        setAdapter(adapter);
    }

    @Nullable
    @Override
    public MessageRecyclerAdapter getAdapter() {
        return adapter;
    }
}
