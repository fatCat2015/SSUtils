package com.cat.aop.event;

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
    public void uploadEvent(String eventName, String eventJsonParams) {
        eventUpload.uploadEvent(eventName,eventJsonParams);
    }
}
