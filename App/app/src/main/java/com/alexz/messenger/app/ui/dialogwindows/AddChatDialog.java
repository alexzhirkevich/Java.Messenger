package com.alexz.messenger.app.ui.dialogwindows;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alexz.messenger.app.data.model.Chat;
import com.alexz.messenger.app.data.repo.DialogsRepository;
import com.messenger.app.R;

public class AddChatDialog extends AlertDialog implements View.OnClickListener {


    public AddChatDialog(Context context) {
        super(context);
        LinearLayout layout = new LinearLayout(context);
        getLayoutInflater().inflate(R.layout.dialog_add_chat, layout);
        setView(layout);
        final Button btnNew = layout.findViewById(R.id.btn_new_chat);
        final Button btnFind =  layout.findViewById(R.id.btn_find_chat);
        if (btnNew != null) {
            btnNew.setOnClickListener(this);
        }
        if (btnFind != null){
            btnFind.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_new_chat){
            onBackPressed();
            new NewChatDialog(getContext()).show();
        } else if (view.getId() == R.id.btn_find_chat){
            onBackPressed();
            new FindChatDialog(getContext()).show();
        }
    }

    public static class FindChatDialog extends AlertDialog
            implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {

        private final EditText editId;
        private final Button btnOk;

        public FindChatDialog(Context context) {
            super(context);
            LinearLayout layout = new LinearLayout(getContext());
            getLayoutInflater().inflate(R.layout.dialog_find_chat, layout);
            setView(layout);
            editId = layout.findViewById(R.id.edit_find_chat);
            btnOk =  layout.findViewById(R.id.btn_find_chat_ok);
            if (btnOk != null) {
                btnOk.setOnClickListener(this);
                btnOk.setEnabled(false);
            }
            if (editId != null) {
                editId.addTextChangedListener(this);
                editId.setOnEditorActionListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            if (editId!=null){
                String id = editId.getText().toString().trim();
                onBackPressed();
                DialogsRepository.getInstance().findChat(id, getContext());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (btnOk != null) {
                btnOk.setEnabled(charSequence.length() != 0);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) { }

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i  == EditorInfo.IME_ACTION_DONE && btnOk != null && btnOk.isEnabled()){
                btnOk.callOnClick();
                return true;
            }
            return false;
        }
    }

    public static class NewChatDialog extends AlertDialog
            implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {

        private final EditText editName;
        private final EditText editPhoto;
        private final Button btnOk;

        public NewChatDialog(Context context) {
            super(context);
            LinearLayout layout = new LinearLayout(getContext());
            getLayoutInflater().inflate(R.layout.dialog_new_chat, layout);
            setView(layout);
            editName = layout.findViewById(R.id.edit_create_name);
            editPhoto = layout.findViewById(R.id.edit_create_url);
            btnOk =  layout.findViewById(R.id.btn_create_chat);
            if (btnOk != null) {
                btnOk.setOnClickListener(this);
                btnOk.setEnabled(false);
            }
            if (editName!=null) {
                editName.addTextChangedListener(this);
                editPhoto.setOnEditorActionListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            String name = "";
            String url = "";
            if (editName!=null){
                name = editName.getText().toString().trim();
            }
            if (editPhoto != null){
                url = editPhoto.getText().toString().trim();
            }
            if (!name.isEmpty()){
                onBackPressed();
                Chat c = new Chat(url,name,true);
                DialogsRepository.getInstance().createChat(c);

            } else {
                Toast.makeText(getContext(),R.string.error_empty_input,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (btnOk!=null){
                btnOk.setEnabled(charSequence.length() != 0);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) { }

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE && btnOk != null && btnOk.isEnabled()){
                btnOk.callOnClick();
                return true;
            }
            return false;
        }
    }
}
