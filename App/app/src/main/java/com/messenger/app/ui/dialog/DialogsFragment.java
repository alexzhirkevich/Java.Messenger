package com.messenger.app.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.messenger.app.R;
import com.messenger.app.util.MyGoogleUtils;
import com.messenger.app.util.OnSwipeTouchListener;
import com.messenger.app.util.VibrateUtil;

import java.util.ArrayList;
import java.util.List;

public class DialogsFragment extends Fragment {

    private static final String STR_DIALOGS = "dialogs";

    private DialogRecyclerView dialogRecyclerView;
    private DialogFloatingButton fab;
    private NestedScrollView dialogScrollView;
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        
        final View root = inflater.inflate(R.layout.fragment_dialogs, container, false);
        dialogRecyclerView = setupDialogList(root);
        dialogScrollView = root.findViewById(R.id.dialog_scroll_view);


        fab = root.findViewById(R.id.dialog_button);
        if (fab!=null) {
            fab.setOnClickListener(e -> dialogRecyclerView.insert(0,new DialogItem(
                    MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                    "Name",
                    "Message",
                    "Sender:",
                    "13:37",
                    3)));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialogScrollView.setOnScrollChangeListener(
                    (View.OnScrollChangeListener) (view, i, i1, i2, i3) -> {

                if (dialogRecyclerView.getChildCount() > 10) {
                    if (i1 < i2) {
                        fab.show();
                    } else {
                        fab.hide();
                    }
                }
            });
        }

        for (int i=0;i<10;i++) {
            dialogRecyclerView.insert(0, new DialogItem(
                    MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                    "Name",
                    "Message",
                    "Sender:",
                    "13:37",
                    i));
        }
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    private DialogRecyclerView setupDialogList(View root){
        if (root == null)
            return null;

        DialogRecyclerView dialogListView = root.findViewById(R.id.dialog_list_view);

        if (dialogListView != null) {
            dialogListView.addOnItemTouchListener(
                    new DialogItemTouchListener(getContext(), dialogListView,
                            new DialogItemTouchListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                    PopupMenu pm = new PopupMenu(getContext(),view);
                                    pm.setGravity(Gravity.RIGHT);
                                    pm.inflate(R.menu.menu_dialogs);
                                    pm.setOnMenuItemClickListener(e ->{
                                        if (e.getItemId() == R.id.delete_dialog){
                                            dialogListView.remove(position);
                                            return true;
                                        }
                                        return false;
                                    });
                                    VibrateUtil.with(getContext()).vibrate(20,VibrateUtil.POWER_LOW);
                                    pm.show();
                                }
                            }));

            dialogListView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

                @Override
                public void onSwipeRight() {
                    Activity activity = getActivity();
                    if (activity != null) {
                        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
                        if (drawer != null)
                            drawer.open();
                    }
                }
            });

        }
        return dialogListView;
    }
}