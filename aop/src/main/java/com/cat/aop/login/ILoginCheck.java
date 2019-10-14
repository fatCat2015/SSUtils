package com.cat.aop.login;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public interface ILoginCheck {
    boolean isLoggedIn(Context context);
    void onNotLoggedIn(Activity activity,int requestCode);
    void onNotLoggedIn(Fragment fragment,int requestCode);
    default void onLoggedIn(){

    }
}
