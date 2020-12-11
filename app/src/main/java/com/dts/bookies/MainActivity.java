package com.dts.bookies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dts.bookies.activities.LoginActivity;
import com.dts.bookies.logic.boundaries.NewUserDetails;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.UserRoleBoundary;
import com.dts.bookies.rest.AdminApi;
import com.dts.bookies.rest.ItemApi;
import com.dts.bookies.rest.OperationApi;
import com.dts.bookies.rest.UserApi;
import com.dts.bookies.rest.services.UserService;
import com.dts.bookies.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button main_BTN_login;
    private Button main_BTN_createNewDummyUser;
    private TextView main_TXT_dummyEmail;
    private TextView main_TXT_dummyUsername;
    private TextView main_TXT_dummyRole;
    private TextView main_TXT_dummyAvatar;


    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        findTempViews();

        this.main_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL) // 10.0.2.2 is the computer's localhost, while 127.0.0.1 is the phone emulator localhost..
                .build();

//        example for a call in the onCreate method. (3 short simple lines)
        this.userService = new UserService(retrofit);

        this.userService.initCreateNewUserCallback(createNewUserCallback);

        this.main_BTN_createNewDummyUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                userService.createNewUser("dummy@email.com", "ADMIN", "dumpster", "__!_,");
            }
        });


    }

    private void findViews() {
        this.main_BTN_login = findViewById(R.id.main_BTN_login);
    }

    private void findTempViews() {
        this.main_BTN_createNewDummyUser = findViewById(R.id.main_BTN_createNewDummyUser);
        this.main_TXT_dummyEmail = findViewById(R.id.main_TXT_dummyEmail);
        this.main_TXT_dummyUsername = findViewById(R.id.main_TXT_dummyUsername);
        this.main_TXT_dummyRole = findViewById(R.id.main_TXT_dummyRole);
        this.main_TXT_dummyAvatar = findViewById(R.id.main_TXT_dummyAvatar);
    }

//  This is the implementation for the activities that creating new users.
    private Callback<UserBoundary> createNewUserCallback = new Callback<UserBoundary>() {
        @Override
        public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
            if(!response.isSuccessful()) {
                Log.d("vvv", response.code() + ": " + response.message());
                return;
            }

//          successful, do something with the new user.
            UserBoundary newUser = response.body();
            Log.d("vvv", newUser.toString());
        }

        @Override
        public void onFailure(Call<UserBoundary> call, Throwable t) {
            Log.d("vvv", "FAILED " + t.getMessage());
        }
    };

//    THIS IS HOW IT LOOKS LIKE WITHOUT LAYERS. NEED TO IMPLEMENT THE WHOLE THIS INSTEAD JUST THE CALLBACK.
//
//    private void createNewUser(String email, String role, String username, String avatar) {
//        NewUserDetails newUserDetails = new NewUserDetails(email, UserRoleBoundary.valueOf(role), username, avatar);
//
//        Call<UserBoundary> call = this.userApi.createNewUser(newUserDetails);
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
//
//    }
//
//    private void loginUser(String userSpace, String userEmail) {
//
//        Call<UserBoundary> call = this.userApi.loginUser(userSpace, userEmail);
//
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
//
//    }
//
//    private void updateUser(String userSpace, String userEmail, UserBoundary update) {
//
//        Call<Void> call = this.userApi.updateUser(userSpace, userEmail, update);
//
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
//    }

}
