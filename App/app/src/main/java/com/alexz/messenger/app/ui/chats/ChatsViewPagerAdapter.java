package com.alexz.messenger.app.ui.chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alexz.messenger.app.ui.fragments.ChannelsFragment;
import com.alexz.messenger.app.ui.fragments.ChatsFragment;

public class ChatsViewPagerAdapter extends FragmentPagerAdapter {

    private final String chatsTitle, channelsTitle;
    private final ChatsFragment chatsFragment= new ChatsFragment();
    private final ChannelsFragment channelsFragment = new ChannelsFragment();

    public ChatsViewPagerAdapter(@NonNull FragmentManager fm, int behavior, String chatsTitle,  String channelsTitle) {
        super(fm, behavior);
        this.chatsTitle = chatsTitle;
        this.channelsTitle = channelsTitle;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return chatsTitle;
        } else{
            return channelsTitle;
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return chatsFragment;
        } else{
            return channelsFragment;
        }
    }
}
