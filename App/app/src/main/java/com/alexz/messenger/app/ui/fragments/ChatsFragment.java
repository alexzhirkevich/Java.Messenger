package com.alexz.messenger.app.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.imp.Chat;
import com.alexz.messenger.app.ui.activities.ChatActivity;
import com.alexz.messenger.app.ui.chats.ChatRecyclerAdapter;
import com.alexz.messenger.app.ui.common.firerecyclerview.LoadingCallback;
import com.alexz.messenger.app.ui.common.ItemClickListener;
import com.alexz.messenger.app.ui.dialogwindows.AddChatDialog;
import com.alexz.messenger.app.ui.viewmodels.DialogsActivityViewModel;
import com.alexz.messenger.app.util.KeyboardUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.messenger.app.R;

public class ChatsFragment extends Fragment implements ItemClickListener<Chat> {

    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats,container,false);;
        drawerLayout = view.findViewById(R.id.drawer_layout);
        viewModel = new ViewModelProvider(this).get(DialogsActivityViewModel.class);
        setupRecyclerView(view);
        setupFloatingButton();
        setupToolbar();
        adapter.startListening();
        return view;
    }

    private ChatRecyclerAdapter adapter;
    private DrawerLayout drawerLayout;
    private EditText editSearch;
    private DialogsActivityViewModel viewModel;
    private RecyclerView dialogRecyclerView;
    private AddChatDialog addChatDialog;

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        if (fab != null) {
            fab.setOnClickListener(e -> {
                addChatDialog = new AddChatDialog(this);
                addChatDialog.show();
            });
        }
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddChatDialog.REQ_NEW_CHAT_PHOTO && addChatDialog != null){
            addChatDialog.onDialogResult(requestCode,resultCode,data);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else
                    drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_search:
                if (editSearch != null){
                    if (editSearch.getVisibility() == View.VISIBLE) {
                        editSearch.setVisibility(View.GONE);
                        editSearch.setText("");
                        editSearch.getLayoutParams().width = 0;
                        editSearch.requestLayout();
                        adapter.selectAll();
                        if (!KeyboardUtil.hasHardwareKeyboard(editSearch.getContext())) {
                            KeyboardUtil.hideKeyboard(editSearch);
                        }
                    } else {
                        editSearch.setVisibility(View.VISIBLE);
                        editSearch.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                        editSearch.requestLayout();
                        if (!KeyboardUtil.hasHardwareKeyboard(editSearch.getContext())) {
                            KeyboardUtil.showKeyboard(editSearch);
                        }
                        editSearch.requestFocus();
                    }
                }
                break;
        }
        return true;
    }


    @Override
    public void onItemClick(View view, Chat chat) {
        ChatActivity.startActivity(getActivity(),chat);
    }

    @Override
    public boolean onLongItemClick(View view, Chat chat) {
        PopupMenu pm = new PopupMenu(getActivity(),view);
        pm.setGravity(Gravity.RIGHT);
        pm.inflate(R.menu.menu_dialogs);
        pm.setOnMenuItemClickListener(e ->{
            if (e.getItemId() == R.id.message_delete){
                viewModel.removeChat(chat);
                return true;
            }
            return false;
        });
        pm.show();
        return true;
    }

    private void setupToolbar() {
        if (getActivity() != null) {
            editSearch = getActivity().findViewById(R.id.edit_search);
            if (editSearch != null) {
                editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() == 0) {
                            adapter.selectAll();
                        } else {
                            if (adapter.select(charSequence.toString()) == 0) {
                                editSearch.setError(getResources().getString(R.string.error_notfound));
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
            }
        }
    }


    private void setupRecyclerView(View view) {

        final ProgressBar loadingPb = view.findViewById(R.id.dialog_loading_pb);

        dialogRecyclerView = view.findViewById(R.id.dialog_rec_view);
        if (dialogRecyclerView != null) {
            dialogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new ChatRecyclerAdapter();
            dialogRecyclerView.setAdapter(adapter);
            adapter.setItemClickListener(this);
            adapter.setLoadingCallback(new LoadingCallback() {
                @Override
                public void onStartLoading() { loadingPb.setVisibility(View.VISIBLE); }

                @Override
                public void onEndLoading() { loadingPb.setVisibility(View.GONE); }
            });
        }
    }
    private void setupFloatingButton() {
        Activity activity = getActivity();
        if (activity != null) {
            fab = activity.findViewById(R.id.fab_dialogs);

            if (dialogRecyclerView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dialogRecyclerView.setOnScrollChangeListener(
                        (View, i, i1, i2, i3) -> {

                            if (adapter.getItemCount() > 10) {
                                if (i1 < i3) {
                                    fab.show();
                                } else {
                                    fab.hide();
                                }
                            } else
                                fab.show();
                        });
            }
        }
    }
}
