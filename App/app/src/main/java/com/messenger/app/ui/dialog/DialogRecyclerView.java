package com.messenger.app.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.List;

public class DialogRecyclerView extends RecyclerView {

    private DialogRecyclerAdapter adapter;
    private List<DialogItem> values;

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

    public void insert(int idx, DialogItem item){
        adapter.insert(idx,item);
    }

    public void add(DialogItem item){
        adapter.add(item);
    }

    public void addAll(Collection<DialogItem> dialog) {
        adapter.addAll(dialog);
    }

    public void set(int idx, DialogItem item){
        adapter.set(idx,item);
    }

    public void clearItems() {
        adapter.clearItems();
    }

    public void remove(int position) {
        adapter.remove(position);
    }

    public int getItemCount(){
        return adapter.getItemCount();
    }
}
