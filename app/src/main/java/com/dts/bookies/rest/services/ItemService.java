package com.dts.bookies.rest.services;

import com.dts.bookies.rest.ItemApi;
import com.dts.bookies.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemService {

    private Retrofit retrofit;
    private ItemApi itemApi;

    public ItemService() {
        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
        this.itemApi = retrofit.create(ItemApi.class);
    }

    public ItemService(Retrofit retrofit) {
        setRetrofit(retrofit);
        this.itemApi = retrofit.create(ItemApi.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public ItemApi getItemApi() {
        return itemApi;
    }

}
