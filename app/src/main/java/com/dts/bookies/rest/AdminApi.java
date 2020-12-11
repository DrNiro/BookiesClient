package com.dts.bookies.rest;

import com.dts.bookies.logic.boundaries.OperationBoundary;
import com.dts.bookies.logic.boundaries.UserBoundary;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AdminApi {

    @GET("dts/admin/operations/{adminSpace}/{adminEmail}")
    public Call<UserBoundary[]> exportAllOperations(@Path("adminSpace") String adminSpace,
                                                        @Path("adminEmail") String adminEmail);

    @GET("dts/admin/users/{adminSpace}/{adminEmail}")
    public Call<OperationBoundary[]> exportAllUsers(@Path("adminSpace") String adminSpace,
                                                        @Path("adminEmail") String adminEmail);

    @DELETE("dts/admin/users/{adminSpace}/{adminEmail}")
    public Call<Void> deleteAllUsersInSpace(@Path("adminSpace") String adminSpace,
                                      @Path("adminEmail") String adminEmail);

    @DELETE("dts/admin/operations/{adminSpace}/{adminEmail}")
    public Call<Void> deleteAllOperationsInSpace(@Path("adminSpace") String adminSpace,
                                           @Path("adminEmail") String adminEmail);

    @DELETE("dts/admin/items/{adminSpace}/{adminEmail}")
    public Call<Void> deleteAllItemsInSpace(@Path("adminSpace") String adminSpace,
                                      @Path("adminEmail") String adminEmail);

}
