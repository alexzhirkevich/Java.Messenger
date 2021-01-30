package com.messenger.app.ui.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.messenger.app.data.model.Dialog;
import com.messenger.app.util.MyGoogleUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DialogsActivityViewModel extends ViewModel {

    private final MutableLiveData<List<Dialog>> dialogs = new MutableLiveData<>();

    public LiveData<List<Dialog>> getDialogs(){
        return dialogs;
    }

    public void updateDialogs(){

        List<Dialog> dialogs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dialogs.add(i, new Dialog(
                    0,
                    MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                    "Name",
                    "Message",
                    "Sender:",
                    new Date(),
                    i));

        }
        dialogs.add(0, new Dialog(
                0,
                MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                "One",
                "Message",
                "Sender:",
                new Date(),
                0));
        dialogs.add(0, new Dialog(
                0,
                MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                "Two",
                "Message",
                "Sender:",
                new Date(),
                0));
        dialogs.add(0, new Dialog(
                0,
                MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                "Three",
                "Message",
                "Sender:",
                new Date(),
                0));
        dialogs.add(0, new Dialog(
                0,
                MyGoogleUtils.getAccount().getPhotoUrl().toString(),
                "Four",
                "Message",
                "Sender:",
                new Date(),
                0));

        this.dialogs.setValue(dialogs);
    }
}
