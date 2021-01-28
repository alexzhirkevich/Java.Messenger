package com.messenger.app.ui.dialogs;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import com.messenger.app.R;
import com.messenger.app.util.KeyboardUtil;

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
                        List<DialogItem> visibleDialogs = adapter.getAll();
                        for (int j = 0; j < visibleDialogs.size(); j++) {
                            if (!visibleDialogs.get(j).getName().toLowerCase()
                                    .contains(charSequence.toString().toLowerCase().trim())) {
                                visibleDialogs.remove(j--);
                            }
                        }
                        adapter.setVisible(visibleDialogs);
                        if (visibleDialogs.size() == 0){
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