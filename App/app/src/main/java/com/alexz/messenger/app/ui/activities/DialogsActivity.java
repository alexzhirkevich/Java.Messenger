package com.alexz.messenger.app.ui.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.ui.chats.DialogRecyclerAdapter;
import com.alexz.messenger.app.ui.dialogwindows.AddChatDialog;
import com.alexz.messenger.app.ui.viewmodels.DialogsActivityViewModel;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.alexz.messenger.app.util.VibrateUtil;
import com.alexz.messenger.app.ui.chats.DialogSearch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.messenger.app.R;
import com.alexz.messenger.app.ui.common.AvatarImageView;
import com.alexz.messenger.app.ui.common.RecyclerClickListener;
import com.alexz.messenger.app.data.model.Chat;

public class DialogsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    RecyclerClickListener.OnItemClickListener{

    private static final String STR_DIALOGS = "dialogs";
    private static final String STR_RECYCLER_DATA = "recyclerview_data";

    private DialogRecyclerAdapter adapter;
    private DrawerLayout drawerLayout;
    private DialogSearch search;
    private DatabaseReference ref  = FirebaseDatabase.getInstance().getReference().child(FirebaseUtil.CHATS);
    private DialogsActivityViewModel viewModel;
    private RecyclerView dialogRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        setupRecyclerView();
        viewModel = new ViewModelProvider(this).get(DialogsActivityViewModel.class);
        viewModel.getChats().observe(this, list -> adapter.setAll(list));
        viewModel.setOnline(true);

        final ProgressBar loadingPb = findViewById(R.id.dialog_loading_pb);
        if (loadingPb!=null){
            viewModel.getEndLoadingObservable().observe(this, (Void) ->{
                loadingPb.setVisibility(View.INVISIBLE);
            });
        }

        setupNavDrawer();
        setupToolbar();
        setupFloatingButton();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        RecyclerView.LayoutManager recManager = dialogRecyclerView.getLayoutManager();
        if (recManager != null) {
            outState.putParcelable(STR_RECYCLER_DATA, recManager.onSaveInstanceState());
        }

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setupRecyclerView();
        if (dialogRecyclerView != null) {
            RecyclerView.LayoutManager recManager = dialogRecyclerView.getLayoutManager();
            if (recManager != null) {
                recManager.onRestoreInstanceState(savedInstanceState);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.startListening(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (search!=null && search.isVisible()){
            search.hide();
        }
        viewModel.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (search.getVisibility() == View.VISIBLE){
           search.hide();
        }
        else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_settings:
                SettingsActivity.startActivity(this);
                break;
            case R.id.nav_about:
                Toast.makeText(this,"About",Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                if (search != null) {
                    search.toggle();
                }
                break;
        }
        return true;
    }


    @Override
    public void onItemClick(View view, int position) {
        Chat clicked = adapter.get(position);
        ChatActivity.startActivity(this,clicked.getId());
    }

    @Override
    public void onLongItemClick(View view, int position) {
        PopupMenu pm = new PopupMenu(this,view);
        pm.setGravity(Gravity.RIGHT);
        pm.inflate(R.menu.menu_dialogs);
        pm.setOnMenuItemClickListener(e ->{
            if (e.getItemId() == R.id.message_delete){
                viewModel.removeChat(adapter.get(position));
                return true;
            }
            return false;
        });
        VibrateUtil.with(this).vibrate(20,VibrateUtil.POWER_LOW);
        pm.show();
    }

    private void setupNavDrawer() {
        final NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            final AvatarImageView drawerAvatar = navigationView.getHeaderView(0).findViewById(R.id.image_drawer_avatar);
            final TextView email = navigationView.getHeaderView(0).findViewById(R.id.text_drawer_email);
            final TextView name = navigationView.getHeaderView(0).findViewById(R.id.text_drawer_name);
            FirebaseUser account = FirebaseUtil.getCurrentFireUser();
            if (drawerAvatar != null && account.getPhotoUrl() != null) {
                drawerAvatar.setImageURI(account.getPhotoUrl());
            }
            if (email != null && account.getEmail() != null) {
                email.setText(account.getEmail());
            }
            if (name != null && account.getDisplayName() != null) {
                name.setText(account.getDisplayName());
            }
            navigationView.setNavigationItemSelectedListener(this);
        }
    }


    private void setupToolbar() {
        final Toolbar toolbar = findViewById(R.id.dialogs_toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            setSupportActionBar(toolbar);
        }
    }

    private void setupRecyclerView() {

        dialogRecyclerView = findViewById(R.id.dialog_list_view);
        if (dialogRecyclerView != null) {
            dialogRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new DialogRecyclerAdapter();
            dialogRecyclerView.setAdapter(adapter);
            dialogRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, dialogRecyclerView, this));
        }
        search = findViewById(R.id.edit_search);
        if (search != null && dialogRecyclerView != null) {
            search.link(adapter);
        }
    }
    private void setupFloatingButton() {
        final FloatingActionButton fab = findViewById(R.id.fab_dialogs);

        if (fab != null && adapter != null) {
            fab.setOnClickListener(e -> {
                new AddChatDialog(DialogsActivity.this).show();
            });
        }
        if (dialogRecyclerView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialogRecyclerView.setOnScrollChangeListener(
                    (View.OnScrollChangeListener) (view, i, i1, i2, i3) -> {

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