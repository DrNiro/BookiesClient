package com.dts.bookies.rest;

import com.dts.bookies.logic.boundaries.OperationBoundary;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OperationApi {

    @POST("dts/operations")
    public Call<Object> invokeOperationOnItem(@Body OperationBoundary op);

}
