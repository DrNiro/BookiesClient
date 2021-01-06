package com.dts.bookies.rest.services;

import android.util.Log;

import com.dts.bookies.logic.boundaries.OperationBoundary;
import com.dts.bookies.rest.OperationApi;
import com.dts.bookies.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OperationService {

    private Retrofit retrofit;
    private OperationApi operationApi;

    private Callback<Object> invokeOperationOnItemCallback;

    public void initInvokeCallback(Callback<Object> invokeOperationOnItemCallback) {
        this.invokeOperationOnItemCallback = invokeOperationOnItemCallback;
    }

    public OperationService() {
        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
        this.operationApi = retrofit.create(OperationApi.class);
    }

    public OperationService(Retrofit retrofit) {
        setRetrofit(retrofit);
        this.operationApi = retrofit.create(OperationApi.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public OperationApi getOperationApi() {
        return operationApi;
    }


    public void invokeOperation(OperationBoundary operationBoundary) {
        if(this.invokeOperationOnItemCallback == null) {
            Log.d("vvv", "need to initialize callback first");
            return;
        }
        Call<Object> call = this.operationApi.invokeOperationOnItem(operationBoundary);

        call.enqueue(this.invokeOperationOnItemCallback);

    }
}
