package com.dts.bookies.rest;

import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.ItemIdBoundary;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ItemApi {

    @GET("dts/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}")
    public Call<ItemBoundary> getSpecificItem(@Path("userSpace") String userSpace,
                                              @Path("userEmail") String userEmail,
                                              @Path("itemSpace") String itemSpace,
                                              @Path("itemId") String itemId);

    @GET("dts/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/parents")
    public Call<ItemBoundary[]> getItemParents(@Path("userSpace") String userSpace,
                                                   @Path("userEmail") String userEmail,
                                                   @Path("itemSpace") String itemSpace,
                                                   @Path("itemId") String itemId);

    @PUT("dts/items/{managerSpace}/{managerEmail}/{itemSpace}/{itemId")
    public Call<Void> updateItem(@Path("managerSpace") String managerSpace,
                           @Path("managerEmail") String managerEmail,
                           @Path("itemSpace") String itemSpace,
                           @Path("itemId") String itemId,
                           @Body ItemBoundary item);

    @POST("dts/items/{managerSpace}/{managerEmail}")
    public Call<ItemBoundary> createItem(@Path("managerSpace") String managerSpace,
                                         @Path("managerEmail") String managerEmail,
                                         @Body ItemBoundary item);

    @GET("dts/items/{userSpace}/{userEmail}")
    public Call<ItemBoundary[]> getAllItems(@Path("userSpace") String userSpace,
                                                @Path("userEmail") String userEmail);

    @GET("dts/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/children")
    public Call<ItemBoundary[]> getAllChildrenOfExistItem(@Path("userSpace") String userSpace,
                                                              @Path("userEmail") String userEmail,
                                                              @Path("itemSpace") String itemSpace,
                                                              @Path("itemId") String itemId);

    @PUT("dts/items/{managerSpace}/{managerEmail}/{itemSpace}/{itemId}/children")
    public Call<Void> bindExistItemToExistChildItem(@Path("managerSpace") String managerSpace,
                                              @Path("managerEmail") String managerEmail,
                                              @Path("itemSpace") String itemSpace,
                                              @Path("itemId") String itemId,
                                              @Body ItemIdBoundary item);


    @GET("/dts/items/{userSpace}/{userEmail}/search/byNamePattern/{namePattern}")
    public Call<ItemBoundary[]> searchItemsByNamePattern(@Path("userSpace") String userSpace,
                                                          @Path("userEmail") String userEmail,
                                                          @Path("namePattern") String namePattern);
}
