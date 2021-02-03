package com.alexz.messenger.app.util;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.messenger.app.R;

public class MyGoogleUtils {

    public final static String G_PLUS_SCOPE =
            "oauth2:https://www.googleapis.com/auth/plus.me";
    public final static String USERINFO_SCOPE =
            "https://www.googleapis.com/auth/userinfo.profile";
    public final static String EMAIL_SCOPE =
            "https://www.googleapis.com/auth/userinfo.email";
    public final static String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE + " " + EMAIL_SCOPE;

    public final static int REQ_SIGN_IN = 123;

    public static GoogleSignInClient getGoogleSignInClient(Activity activity){
        GoogleSignInOptions options= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(activity,options);
    }
}
