package com.cat.aop.login;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public interface ILoginSeek {
    void onNotLoggedIn(Activity activity, int requestCode);
    void onNotLoggedIn(Fragment fragment, int requestCode);
}
