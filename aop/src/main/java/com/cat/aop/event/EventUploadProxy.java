package com.cat.aop.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.cat.aop.login.ILoginCheck;
import com.cat.aop.login.ILoginSeek;

public class EventUploadProxy implements IEventUpload {

    private volatile static EventUploadProxy instance;

    private IEventUpload eventUpload;

    private EventUploadProxy(){
    }

    public static EventUploadProxy getInstance(){
        if(instance==null){
            synchronized (EventUploadProxy.class){
                if(instance==null){
                    instance=new EventUploadProxy();
                }
            }
        }
        return instance;
    }


    public synchronized void initEventUpload(IEventUpload eventUpload){
        this.eventUpload=eventUpload;
    }

    @Override
    public void uploadEvent(String eventName, Object[] args) {
        eventUpload.uploadEvent(eventName,args);
    }
}
