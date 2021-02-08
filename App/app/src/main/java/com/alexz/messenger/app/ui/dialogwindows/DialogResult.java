package com.alexz.messenger.app.ui.dialogwindows;

import android.content.Intent;

public interface DialogResult {

    void onDialogResult(int requestCode, int resultCode, Intent resultIntent);
}
