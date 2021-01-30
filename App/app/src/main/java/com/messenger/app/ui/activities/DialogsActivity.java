package com.messenger.app.ui.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.messenger.app.R;
import com.messenger.app.ui.common.AvatarImageView;
import com.messenger.app.ui.common.RecyclerClickListener;
import com.messenger.app.data.model.Dialog;
import com.messenger.app.ui.dialogs.DialogRecyclerAdapter;
import com.messenger.app.ui.dialogs.DialogSearch;
import com.messenger.app.util.MyGoogleUtils;
import com.messenger.app.util.VibrateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class DialogsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    RecyclerClickListener.OnItemClickListener{

    private static final String STR_DIALOGS = "dialogs";

    private DialogRecyclerAdapter adapter;
    private DrawerLayout drawerLayout;
    private DialogSearch search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        setupNavDrawer();
        setupToolbar();
        setupResyclerView();
        setupFloatingButton();

        DialogsActivityViewModel viewModel = new ViewModelProvider(this).get(DialogsActivityViewModel.class);
        viewModel.getDialogs().observe(this, list -> adapter.setAll(list));
        viewModel.updateDialogs();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        search = findViewById(R.id.edit_search);
        drawerLayout = findViewById(R.id.drawer_layout);
        RecyclerView recyclerView = findViewById(R.id.dialog_list_view);
        if (recyclerView!=null) {
            adapter = (DialogRecyclerAdapter) recyclerView.getAdapter();
            search = findViewById(R.id.edit_search);
            if (adapter != null && search != null) {
                search.link(adapter);
                adapter.addAll(savedInstanceState.getParcelableArrayList(STR_DIALOGS));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STR_DIALOGS,new ArrayList<>(adapter.getAll()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (search!=null && search.isVisible()){
            search.hide();
        }
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
                Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about:
                Toast.makeText(this,"About",Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        Dialog clicked = adapter.getVisible(position);
        ChatActivity.startActivity(this,clicked.getId(), clicked.getName(), clicked.getImageUri());
    }

    @Override
    public void onLongItemClick(View view, int position) {
        PopupMenu pm = new PopupMenu(this,view);
        pm.setGravity(Gravity.RIGHT);
        pm.inflate(R.menu.menu_dialogs);
        pm.setOnMenuItemClickListener(e ->{
            if (e.getItemId() == R.id.message_delete){
                adapter.remove(position);
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
            GoogleSignInAccount account = MyGoogleUtils.getAccount();
            if (account != null) {
                if (drawerAvatar != null && account.getPhotoUrl() != null) {
                    drawerAvatar.setImageURI(account.getPhotoUrl());
                }
                if (email != null && account.getEmail() != null) {
                    email.setText(account.getEmail());
                }
                if (name != null && account.getDisplayName() != null) {
                    name.setText(account.getDisplayName());
                }
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

    private void setupResyclerView() {
        final RecyclerView dialogRecyclerView = findViewById(R.id.dialog_list_view);
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
        final NestedScrollView dialogScrollView = findViewById(R.id.dialog_scroll_view);
        final FloatingActionButton fab = findViewById(R.id.fab_dialogs);
        /* Floating button on click action */
        if (fab != null && adapter != null) {
            fab.setOnClickListener(e -> {
                if (search != null && search.isVisible())
                    search.hide();
                adapter.insert(0, new Dialog(
                        new Random().nextInt(1000),
                        MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                        "Name",
                        "Message",
                        "Sender:",
                        new Date(),
                        3));
            });
        }
        if (dialogScrollView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialogScrollView.setOnScrollChangeListener(
                    (View.OnScrollChangeListener) (view, i, i1, i2, i3) -> {

                        if (!dialogScrollView.canScrollVertically(-1))
                            Toast.makeText(this,"Update",Toast.LENGTH_SHORT).show();

                        if (adapter.size() > 10) {
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