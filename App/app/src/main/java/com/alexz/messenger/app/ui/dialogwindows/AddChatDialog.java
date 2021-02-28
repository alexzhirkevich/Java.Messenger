package com.alexz.messenger.app.ui.dialogwindows;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alexz.messenger.app.data.model.imp.Chat;
import com.alexz.messenger.app.data.model.Result;
import com.alexz.messenger.app.data.repo.DialogsRepository;
import com.alexz.messenger.app.ui.activities.ChatActivity;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.google.firebase.storage.StorageReference;
import com.messenger.app.R;

public class AddChatDialog extends AlertDialog implements View.OnClickListener, DialogResult {

    public static final int REQ_NEW_CHAT_PHOTO = 1701;

    private NewChatDialog newChatDialog;
    private final Activity activity;

    public AddChatDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        LinearLayout layout = new LinearLayout(activity);
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
            newChatDialog = new NewChatDialog(activity);
            newChatDialog.show();
        } else if (view.getId() == R.id.btn_find_chat){
            new FindChatDialog(getContext()).show();
        }
    }

    @Override
    public void onDialogResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if (newChatDialog != null){
            newChatDialog.onDialogResult(requestCode,resultCode,imageReturnedIntent);
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
                DialogsRepository.findChat(id, getContext());
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
            implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener, DialogResult{

        private final EditText editName;
        private final Button btnPhoto;
        private final Button btnOk;
        private final ProgressBar photoUpload;

        private final Activity activity;

        private Uri imageUri;
        private StorageReference storageReference;
        private boolean deletePhoto = false;

        public NewChatDialog(Activity activity) {
            super(activity);
            this.activity = activity;
            LinearLayout layout = new LinearLayout(getContext());
            getLayoutInflater().inflate(R.layout.dialog_new_chat, layout);
            setView(layout);
            editName = layout.findViewById(R.id.edit_create_name);
            btnPhoto = layout.findViewById(R.id.btn_create_photo);
            btnOk =  layout.findViewById(R.id.btn_create_chat);
            photoUpload = layout.findViewById(R.id.progress_photo_upload);
            if (btnOk != null) {
                btnOk.setOnClickListener(this);
                btnOk.setEnabled(false);
            }
            if (editName!=null) {
                editName.addTextChangedListener(this);
                btnPhoto.setOnEditorActionListener(this);
            }
            if (btnPhoto!=null) {
                btnPhoto.setOnClickListener(this);
            }
        }

        @Override
        protected void onStop() {
            super.onStop();
            if (deletePhoto && storageReference!= null){
                storageReference.delete();
            }
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_create_chat) {
                String name = "";
                if (editName != null) {
                    name = editName.getText().toString().trim();
                }
                if (!name.isEmpty()) {
                    deletePhoto = false;
                    onBackPressed();
                    Chat c = new Chat(imageUri.toString(), name, true);
                    DialogsRepository.createChat(c);
                    ChatActivity.startActivity(getContext(), c);

                } else {
                    Toast.makeText(getContext(), R.string.error_empty_input, Toast.LENGTH_LONG).show();
                }
            } else if (view.getId() == R.id.btn_create_photo){
                if (!deletePhoto) {
                    if (activity != null) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        activity.startActivityForResult(photoPickerIntent, REQ_NEW_CHAT_PHOTO);
                    }
                } else {
                    storageReference.delete();
                    imageUri = null;
                    storageReference = null;
                    deletePhoto = false;
                    btnPhoto.setText(R.string.title_upload_photo);
                }
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

        @Override
        public void onDialogResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
            if (requestCode == REQ_NEW_CHAT_PHOTO && resultCode == Activity.RESULT_OK){
                btnPhoto.setEnabled(false);
                btnOk.setEnabled(false);
                photoUpload.setVisibility(View.VISIBLE);
                FirebaseUtil.uploadPhoto(imageReturnedIntent.getData())
                        .addResultListener(new Result.ResultListener<Pair<Uri, StorageReference>>() {
                            @Override
                            public void onSuccess(@NonNull Result.ISuccess<Pair<Uri, StorageReference>> result) {
                                imageUri = result.get().first;
                                storageReference = result.get().second;
                                deletePhoto = true;
                                btnPhoto.setText(R.string.title_remove_photo);
                                btnPhoto.post(() -> btnPhoto.setEnabled(true));
                                btnOk.post(() -> btnOk.setEnabled(true));
                                photoUpload.post(() -> photoUpload.setVisibility(View.GONE));
                            }

                            @Override
                            public void onError(@Nullable Result.IError error) {
                                if (error != null) {
                                    Toast.makeText(getContext(), error.getError(), Toast.LENGTH_SHORT).show();
                                }
                                btnPhoto.post(() -> btnPhoto.setEnabled(true));
                                btnOk.post(() -> btnOk.setEnabled(true));
                                photoUpload.post(() -> photoUpload.setVisibility(View.GONE));
                            }

                            @Override
                            public void onProgress(@Nullable Double percent) {
                                if (percent != null) {
                                    photoUpload.setProgress(percent.intValue());
                                }
                            }
                        });
            }
        }
    }
}
