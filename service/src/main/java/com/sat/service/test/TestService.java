package com.sat.service.test;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface TestService {

    @GET
    Observable<TestNewsList> getTestNewsList(@Url String url);
}



