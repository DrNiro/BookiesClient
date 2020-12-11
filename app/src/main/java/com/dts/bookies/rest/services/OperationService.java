package com.dts.bookies.rest.services;

import com.dts.bookies.rest.OperationApi;
import com.dts.bookies.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OperationService {

    private Retrofit retrofit;
    private OperationApi userApi;

    public OperationService() {
        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
        this.userApi = retrofit.create(OperationApi.class);
    }

    public OperationService(Retrofit retrofit) {
        setRetrofit(retrofit);
        this.userApi = retrofit.create(OperationApi.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public OperationApi getUserApi() {
        return userApi;
    }

}
