package com.dts.bookies.rest.services;

import android.util.Log;

import com.dts.bookies.logic.boundaries.NewUserDetails;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.UserRoleBoundary;
import com.dts.bookies.rest.UserApi;
import com.dts.bookies.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserService {

    private Retrofit retrofit;
    private UserApi userApi;

    private Callback<UserBoundary> createNewUserCallback;
    private Callback<Void> updateUserCallback;
    private Callback<UserBoundary> loginUserCallback;

    public void initCreateNewUserCallback(Callback<UserBoundary> createNewUserCallback) {
        this.createNewUserCallback = createNewUserCallback;
    }

    public void initUpdateUserCallback(Callback<Void> updateUserCallback) {
        this.updateUserCallback = updateUserCallback;
    }

    public void initLoginUserCallback(Callback<UserBoundary> loginUserCallback) {
        this.loginUserCallback = loginUserCallback;
    }

    public void  initAllCallbacks(Callback<UserBoundary> createNewUserCallback,
                            Callback<Void> updateUserCallback,
                            Callback<UserBoundary> loginUserCallback) {
        this.createNewUserCallback = createNewUserCallback;
        this.updateUserCallback = updateUserCallback;
        this.loginUserCallback = loginUserCallback;
    }

    public UserService() {
        this.retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.BASE_URL)
                    .build();
        this.userApi = retrofit.create(UserApi.class);
    }

    public UserService(Retrofit retrofit) {
        setRetrofit(retrofit);
        this.userApi = retrofit.create(UserApi.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public UserApi getUserApi() {
        return userApi;
    }

    public void createNewUser(String email, String role, String username, String avatar) {
        if(this.createNewUserCallback == null) {
            Log.d("vvv", "need to initialize callback first");
            return;
        }

        NewUserDetails newUserDetails = new NewUserDetails(email, UserRoleBoundary.valueOf(role), username, avatar);

        Call<UserBoundary> call = this.userApi.createNewUser(newUserDetails);

        call.enqueue(this.createNewUserCallback);
//
//        call.enqueue(new Callback<UserBoundary>() {
//            @Override
//            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
//                if(!response.isSuccessful()) {
//                    Log.d("vvv", response.code() + ": " + response.message());
//                    return;
//                }
//
////              successful, do something with the new user.
//                UserBoundary newUser = response.body();
//
//                Log.d("vvv", newUser.toString());
//            }
//
//            @Override
//            public void onFailure(Call<UserBoundary> call, Throwable t) {
//                Log.d("vvv", "FAILED " + t.getMessage());
//            }
//        });

    }

    public void loginUser(String userSpace, String userEmail) {
        if(this.loginUserCallback == null) {
            Log.d("vvv", "need to initialize callback first");
            return;
        }

        Call<UserBoundary> call = this.userApi.loginUser(userSpace, userEmail);

        call.enqueue(this.loginUserCallback);

//        call.enqueue(new Callback<UserBoundary>() {
//            @Override
//            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
//                if(!response.isSuccessful()) {
//                    Log.d("vvv", response.code() + ": " + response.message());
//                    return;
//                }
//
////              Login user.
//                UserBoundary user = response.body();
//
//                //TODO implement login
//            }
//
//            @Override
//            public void onFailure(Call<UserBoundary> call, Throwable t) {
//
//            }
//        });

    }

    public void updateUser(String userSpace, String userEmail, UserBoundary update) {
        if(this.updateUserCallback == null) {
            Log.d("vvv", "need to initialize callback first");
            return;
        }



        Call<Void> call = this.userApi.updateUser(userSpace, userEmail, update);

        call.enqueue(this.updateUserCallback);

//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                Log.d("vvv", "update successful " + response.code());
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d("vvv", "FAILED " + t.getMessage());
//            }
//        });
    }

}
