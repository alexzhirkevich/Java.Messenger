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

import com.messenger.app.R;

import java.util.List;

public class DialogSearch extends AppCompatEditText {

    private DialogRecyclerView view = null;

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

    public void link(DialogRecyclerView view ){
        this.view = view;
    }

    public void hide(){
        setVisibility(INVISIBLE);
        if (getLayoutParams() != null) {
            getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            requestLayout();
        }
        setText("");
        clearFocus();
        if (view != null && view.getAdapter() != null) {
            view.getAdapter().restoreVisibility();
        }
    }

    public void show(){
        if (getLayoutParams() != null) {
            getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            requestLayout();
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
                if (view != null && view.getAdapter() != null) {
                    if (charSequence.length() == 0) {
                        view.getAdapter().restoreVisibility();
                    } else {
                        List<DialogItem> visibleDialogs = view.getAdapter().getAll();
                        for (int j = 0; j < visibleDialogs.size(); j++) {
                            if (!visibleDialogs.get(j).getName().toLowerCase()
                                    .contains(charSequence.toString().toLowerCase().trim())) {
                                visibleDialogs.remove(j--);
                            }
                        }
                        view.getAdapter().setVisible(visibleDialogs);
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
        if (getResources().getConfiguration().keyboard == Configuration.KEYBOARD_NOKEYS){
            InputMethodManager inputMethodManager =
                    (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null){
                inputMethodManager.toggleSoftInputFromWindow(
                        getWindowToken(),InputMethodManager.SHOW_IMPLICIT, 0);
            }
        }
        requestFocus();
    }

    @Override
    public void clearFocus() {
        if (getResources().getConfiguration().keyboard == Configuration.KEYBOARD_NOKEYS){
            InputMethodManager inputMethodManager =
                    (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null){
                inputMethodManager.hideSoftInputFromWindow(
                        getWindowToken(),0);
            }
        }
        super.clearFocus();
    }

}
