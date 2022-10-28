package com.example.onlinehardwareorderingapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    //http://fastlinehardware-001-site1.htempurl.com/
//    public static final String API_URL = "http://fastline-001-site1.dtempurl.com/";
    public static final String API_URL = "http://fastlinehardware-001-site1.htempurl.com/";
    private static RetrofitService retrofitService = null;
    private Retrofit.Builder retrofitBuilder = null;
    private RetrofitService(){
        retrofitBuilder = new Retrofit.Builder();
    }
    public static RetrofitService getInstance(){
        if (retrofitService == null){
            retrofitService = new RetrofitService();
        }

        return retrofitService;
    }

    public Retrofit.Builder getRetrofitBuilder() {
        return retrofitBuilder;
    }
}
