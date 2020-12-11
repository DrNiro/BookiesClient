package com.dts.bookies.rest.services;

import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.rest.ItemApi;
import com.dts.bookies.util.Constants;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemService {

    private Retrofit retrofit;
    private ItemApi itemApi;

    private Callback<ItemBoundary> getSpecificItemCallback;
    private Callback<ItemBoundary[]> getItemParentsCallback;
    private Callback<Void> updateItemCallback;
    private Callback<ItemBoundary> createItemCallback;
    private Callback<ItemBoundary[]> getAllItemsCallback;
    private Callback<ItemBoundary[]> getAllChildrenOfExistItemCallback;
    private Callback<Void> bindExistItemToExistChildItemCallback;

    public void initGetSpecificItemCallback(Callback<ItemBoundary> getSpecificItemCallback) {
        this.getSpecificItemCallback = getSpecificItemCallback;
    }

    public void initGetItemParentsCallback(Callback<ItemBoundary[]> getItemParentsCallback) {
        this.getItemParentsCallback = getItemParentsCallback;
    }

    public void initUpdateItemCallback(Callback<Void> updateItemCallback) {
        this.updateItemCallback = updateItemCallback;
    }

    public void initCreateItemCallback(Callback<ItemBoundary> createItemCallback) {
        this.createItemCallback = createItemCallback;
    }

    public void initGetAllItemsCallbackS(Callback<ItemBoundary[]> getAllItemsCallback) {
        this.getAllItemsCallback = getAllItemsCallback;
    }

    public void initGetAllChildrenOfExistItemCallback(Callback<ItemBoundary[]> getAllChildrenOfExistItemCallback) {
        this.getAllChildrenOfExistItemCallback = getAllChildrenOfExistItemCallback;
    }

    public void initBindExistItemToExistChildItemCallback(Callback<Void> bindExistItemToExistChildItemCallback) {
        this.bindExistItemToExistChildItemCallback = bindExistItemToExistChildItemCallback;
    }

    public void initAllCallbacks(Callback<ItemBoundary> getSpecificItemCallback,
                                  Callback<ItemBoundary[]> getItemParentsCallback,
                                  Callback<Void> updateItemCallback,
                                  Callback<ItemBoundary> createItemCallback,
                                  Callback<ItemBoundary[]> getAllItemsCallback,
                                  Callback<ItemBoundary[]> getAllChildrenOfExistItemCallback,
                                  Callback<Void> bindExistItemToExistChildItemCallback) {

    }

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
