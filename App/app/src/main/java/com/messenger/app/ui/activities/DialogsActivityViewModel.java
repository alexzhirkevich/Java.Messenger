package com.messenger.app.ui.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.messenger.app.ui.dialogs.DialogItem;
import com.messenger.app.util.MyGoogleUtils;

import java.util.ArrayList;
import java.util.List;

public class DialogsActivityViewModel extends ViewModel {

    private final MutableLiveData<List<DialogItem>> dialogs = new MutableLiveData<>();

    public LiveData<List<DialogItem>> getDialogs(){
        return dialogs;
    }

    public void updateDialogs(){

        List<DialogItem> dialogItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dialogItems.add(i, new DialogItem(
                    0,
                    MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                    "Name",
                    "Message",
                    "Sender:",
                    "13:37",
                    i));

        }
        dialogItems.add(0, new DialogItem(
                0,
                MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                "One",
                "Message",
                "Sender:",
                "13:37",
                0));
        dialogItems.add(0, new DialogItem(
                0,
                MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                "Two",
                "Message",
                "Sender:",
                "13:37",
                0));
        dialogItems.add(0, new DialogItem(
                0,
                MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                "Three",
                "Message",
                "Sender:",
                "13:37",
                0));
        dialogItems.add(0, new DialogItem(
                0,
                MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                "Four",
                "Message",
                "Sender:",
                "13:37",
                0));

        dialogs.setValue(dialogItems);
    }
}
