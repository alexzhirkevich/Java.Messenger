package com.alexz.messenger.app.util;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {

    public static boolean showKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInputFromWindow(
                    view.getWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
            return true;
        }
        return false;
    }

    public static boolean hideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    view.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    public static boolean hasHardwareKeyboard(Context context){
        return context.getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS;
    }
}
