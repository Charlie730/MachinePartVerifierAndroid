package com.humminbird.machinepartverifierandroid.Utilities;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SQLApi {

    public SQLApi(String connectionString){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://"+connectionString+".ngrok.io/")
        .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public void getString(){

    }

    public void getInteger(){

    }
}
