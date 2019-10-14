package com.cat.aop.login;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public interface ILoginCheck {
    boolean isLoggedIn(Context context);

    default void onStartLoginActivity(Activity activity){

    }

}
