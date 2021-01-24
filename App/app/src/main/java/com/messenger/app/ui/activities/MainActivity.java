package com.messenger.app.ui.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.messenger.app.R;
import com.messenger.app.ui.AvatarImageView;
import com.messenger.app.ui.RecyclerClickListener;
import com.messenger.app.ui.dialogs.DialogItem;
import com.messenger.app.ui.dialogs.DialogRecyclerAdapter;
import com.messenger.app.ui.dialogs.DialogRecyclerView;
import com.messenger.app.ui.dialogs.DialogSearch;
import com.messenger.app.util.MyGoogleUtils;
import com.messenger.app.util.VibrateUtil;

public class MainActivity extends AppCompatActivity
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

        final Toolbar toolbar = findViewById(R.id.toolbar);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final DialogRecyclerView dialogRecyclerView = findViewById(R.id.dialog_list_view);
        final FloatingActionButton fab = findViewById(R.id.fab_dialogs);
        final NestedScrollView dialogScrollView = findViewById(R.id.dialog_scroll_view);
        search = findViewById(R.id.edit_search);
        drawerLayout = findViewById(R.id.drawer_layout);

        if (navigationView != null) {
            final AvatarImageView drawerAvatar = navigationView.getHeaderView(0).findViewById(R.id.image_drawer_avatar);
            final TextView email = navigationView.getHeaderView(0).findViewById(R.id.text_drawer_email);
            final TextView name = navigationView.getHeaderView(0).findViewById(R.id.text_drawer_name);
            GoogleSignInAccount account = MyGoogleUtils.getAccount();
            if (account != null) {
                if (drawerAvatar != null && account.getPhotoUrl() != null) {
                    drawerAvatar.setImageURI(account.getPhotoUrl());
                }
                if (email!=null && account.getEmail() != null){
                    email.setText(account.getEmail());
                }
                if (name != null && account.getDisplayName() != null){
                    name.setText(account.getDisplayName());
                }
            }
        }

        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            setSupportActionBar(toolbar);
        }
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        if (dialogRecyclerView != null) {
            adapter = dialogRecyclerView.getAdapter();
            dialogRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, dialogRecyclerView, this));

            if (fab != null && adapter != null) {
                fab.setOnClickListener(e -> adapter.insert(0, new DialogItem(
                        MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                        "Name",
                        "Message",
                        "Sender:",
                        "13:37",
                        3)));
            }

            if (dialogScrollView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dialogScrollView.setOnScrollChangeListener(
                        (View.OnScrollChangeListener) (view, i, i1, i2, i3) -> {

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

            if (search != null){
                search.link(dialogRecyclerView);
            }

            for (int i = 0; i < 10; i++) {
                adapter.insert(0, new DialogItem(
                        MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                        "Name",
                        "Message",
                        "Sender:",
                        "13:37",
                        i));

            }
            adapter.insert(0, new DialogItem(
                    MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                    "One",
                    "Message",
                    "Sender:",
                    "13:37",
                    0));
            adapter.insert(0, new DialogItem(
                    MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                    "Two",
                    "Message",
                    "Sender:",
                    "13:37",
                    0));
            adapter.insert(0, new DialogItem(
                    MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                    "Three",
                    "Message",
                    "Sender:",
                    "13:37",
                    0));
            adapter.insert(0, new DialogItem(
                    MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                    "Four",
                    "Message",
                    "Sender:",
                    "13:37",
                    0));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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

    }

    @Override
    public void onLongItemClick(View view, int position) {
        PopupMenu pm = new PopupMenu(this,view);
        pm.setGravity(Gravity.RIGHT);
        pm.inflate(R.menu.menu_dialogs);
        pm.setOnMenuItemClickListener(e ->{
            if (e.getItemId() == R.id.delete_dialog){
                adapter.remove(position);
                return true;
            }
            return false;
        });
        VibrateUtil.with(this).vibrate(20,VibrateUtil.POWER_LOW);
        pm.show();
    }
}