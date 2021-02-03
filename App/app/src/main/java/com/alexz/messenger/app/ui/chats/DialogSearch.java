package com.alexz.messenger.app.ui.chats;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.alexz.messenger.app.data.model.Chat;
import com.alexz.messenger.app.ui.chats.DialogRecyclerAdapter;
import com.alexz.messenger.app.util.KeyboardUtil;
import com.messenger.app.R;

import java.util.List;

public class DialogSearch extends AppCompatEditText {

    private DialogRecyclerAdapter adapter;

    public DialogSearch(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DialogSearch(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogSearch(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void link(DialogRecyclerAdapter adapter){
        this.adapter = adapter;
    }

    public boolean isVisible(){
        return getVisibility() == VISIBLE;
    }

    public void hide(){
        setVisibility(INVISIBLE);
        if (getLayoutParams() != null) {
            getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            requestLayout();
        }
        setText("");
        clearFocus();
        if (adapter != null) {
            adapter.restoreVisibility();
        }
    }

    public void show(){
        if (getLayoutParams() != null) {
            getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            requestLayout();
        }
        if (adapter != null) {
            adapter.setVisible(adapter.getAll());
        }
        setVisibility(VISIBLE);
        setFocus();
    }

    public void toggle(){
        if (getVisibility() == VISIBLE) {
            hide();
        }
        else {
            show();
        }
    }

    private void init(Context context){
        setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        hide();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter != null) {
                    if (charSequence.length() == 0) {
                       adapter.restoreVisibility();
                    } else {
                        List<Chat> visibleChats = adapter.getAll();
                        for (int j = 0; j < visibleChats.size(); j++) {
                            if (!visibleChats.get(j).getName().toLowerCase()
                                    .contains(charSequence.toString().toLowerCase().trim())) {
                                visibleChats.remove(j--);
                            }
                        }
                        adapter.setVisible(visibleChats);
                        if (visibleChats.size() == 0){
                            setError(getResources().getString(R.string.error_notfound));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setFocus(){
        KeyboardUtil.showKeyboard(this);
        requestFocus();
    }

    @Override
    public void clearFocus() {
        KeyboardUtil.hideKeyboard(this);
        super.clearFocus();
    }

}
