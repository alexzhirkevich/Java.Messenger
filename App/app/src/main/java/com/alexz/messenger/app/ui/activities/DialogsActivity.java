package com.alexz.messenger.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alexz.messenger.app.ui.chats.ChatsViewPagerAdapter;
import com.alexz.messenger.app.ui.common.AvatarImageView;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.alexz.messenger.app.util.KeyboardUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;
import com.messenger.app.R;

public class DialogsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String STR_DIALOGS = "dialogs";
    private static final String STR_RECYCLER_DATA = "recyclerview_data";

    private DrawerLayout drawerLayout;
    private EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        editSearch = findViewById(R.id.edit_search);
        setupNavDrawer();
        setupToolbar();
        setupTabLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (editSearch.getVisibility() == View.VISIBLE){
            editSearch.setVisibility(View.GONE);
            editSearch.setText("");
            editSearch.getLayoutParams().width = 0;
            editSearch.requestLayout();
            if (!KeyboardUtil.hasHardwareKeyboard(this)) {
                KeyboardUtil.hideKeyboard(editSearch);
            }
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
            case R.id.nav_account:
            case R.id.nav_about:
                Toast.makeText(this,"Coming soon",Toast.LENGTH_SHORT).show();
                //SettingsActivity.startActivity(this);
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
                if (editSearch != null){
                    if (editSearch.getVisibility() == View.VISIBLE) {
                        editSearch.setVisibility(View.GONE);
                        editSearch.setText("");
                        editSearch.getLayoutParams().width = 0;
                        editSearch.requestLayout();
                        if (!KeyboardUtil.hasHardwareKeyboard(this)) {
                            KeyboardUtil.hideKeyboard(editSearch);
                        }
                    } else {
                        editSearch.setVisibility(View.VISIBLE);
                        editSearch.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                        editSearch.requestLayout();
                        if (!KeyboardUtil.hasHardwareKeyboard(this)) {
                            KeyboardUtil.showKeyboard(editSearch);
                        }
                        editSearch.requestFocus();
                    }
                }
                break;
        }
        return true;
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

    private void setupTabLayout() {
        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.main_viewpager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setAdapter(
                new ChatsViewPagerAdapter(
                        getSupportFragmentManager(),
                        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                        getString(R.string.title_chats),
                        getString(R.string.title_channels)));

        tabLayout.setupWithViewPager(viewPager,true);
    }

}