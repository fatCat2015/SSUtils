package com.cat.aop.login;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

public class LoginCheckProxy implements ILoginCheck {

    private volatile static  LoginCheckProxy instance;

    private ILoginCheck loginCheck;

    private LoginCheckProxy(){
    }

    public static LoginCheckProxy getInstance(){
        if(instance==null){
            synchronized (LoginCheckProxy.class){
                if(instance==null){
                    instance=new LoginCheckProxy();
                }
            }
        }
        return instance;
    }


    public synchronized void initLoginCheck(ILoginCheck loginCheck){
        this.loginCheck=loginCheck;
    }

    @Override
    public boolean isLoggedIn(Context context) {
        return loginCheck.isLoggedIn(context);
    }

    @Override
    public void onNotLoggedIn(Activity activity, int requestCode) {
        loginCheck.onNotLoggedIn(activity,requestCode);
    }

    @Override
    public void onNotLoggedIn(Fragment fragment, int requestCode) {
        loginCheck.onNotLoggedIn(fragment,requestCode);
    }



    @Override
    public void onLoggedIn() {
        loginCheck.onLoggedIn();
    }
}
