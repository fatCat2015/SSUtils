package com.cat.aop.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class LoginCheckProxy implements ILoginCheck,ILoginSeek {

    private volatile static  LoginCheckProxy instance;

    private ILoginCheck loginCheck;
    private Class<? extends Activity> loginPageClz;

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


    /**
     *
     * @param loginPageClz  登录页面Class
     * @param loginCheck    主要用于判断是否登录
     */
    public synchronized void initLoginCheck(Class<? extends Activity> loginPageClz,ILoginCheck loginCheck){
        this.loginPageClz=loginPageClz;
        this.loginCheck=loginCheck;
    }

    @Override
    public boolean isLoggedIn(Context context) {
        return loginCheck.isLoggedIn(context);
    }

    @Override
    public void onNotLoggedIn(Activity activity, int requestCode) {
        if(requestCode==0){
            activity.startActivity(new Intent(activity,loginPageClz));
        }else{
            activity.startActivityForResult(new Intent(activity,loginPageClz),requestCode);
        }
        onStartLoginActivity(activity);
    }

    @Override
    public void onNotLoggedIn(Fragment fragment, int requestCode) {
        if(requestCode==0){
            fragment.startActivity(new Intent(fragment.getActivity(),loginPageClz));
        }else{
            fragment.startActivityForResult(new Intent(fragment.getActivity(),loginPageClz),requestCode);
        }
        onStartLoginActivity(fragment.getActivity());
    }

    @Override
    public void onStartLoginActivity(Activity activity) {
        loginCheck.onStartLoginActivity(activity);
    }
}
