package com.sat.service;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.sat.service.test.TestNewsList;
import com.sat.service.test.TestService;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceManager {


    private static class Inner{
        private static final ServiceManager SERVICE_MANAGER=new ServiceManager();
    }


    public static ServiceManager getInstance(){
        return Inner.SERVICE_MANAGER;
    }


    private Retrofit retrofit;

    private ServiceManager(){

    }

    public void init(Context context,String baseUrl,boolean debug){

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(10_000, TimeUnit.MILLISECONDS)
                .readTimeout(10_000, TimeUnit.MILLISECONDS)
                .writeTimeout(10_000, TimeUnit.MILLISECONDS);

        if(debug){
            Stetho.initializeWithDefaults(context.getApplicationContext());
            okHttpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
        }

        retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }



    public <T> T create(final Class<T> service){
        return retrofit.create(service);
    }






    public void test(){
        create(TestService.class).getTestNewsList("http://api.tianapi.com/nba/?key=71e58b5b2f930eaf1f937407acde08fe&num=10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TestNewsList>() {
                    @Override
                    public void accept(TestNewsList testNewsList) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }



}
