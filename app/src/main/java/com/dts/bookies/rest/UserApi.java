package com.dts.bookies.rest;

import com.dts.bookies.logic.boundaries.NewUserDetails;
import com.dts.bookies.logic.boundaries.UserBoundary;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {

    @PUT("dts/users/{userSpace}/{userEmail}")
    public Call<Void> updateUser(@Path("userSpace") String userSpace,
                           @Path("userEmail") String userEmail,
                           @Body UserBoundary user);

    @POST("dts/users")
    public Call<UserBoundary> createNewUser(@Body NewUserDetails newUserDetails);

    @GET("dts/users/login/{userSpace}/{userEmail}")
    public Call<UserBoundary> loginUser(@Path("userSpace") String userSpace,
                                        @Path("userEmail") String userEmail);

}
