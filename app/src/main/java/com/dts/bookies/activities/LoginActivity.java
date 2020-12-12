package com.dts.bookies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountAuthenticatorActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dts.bookies.R;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.rest.services.UserService;
import com.dts.bookies.util.Constants;
import com.dts.bookies.util.Functions;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AccountAuthenticatorActivity {

    private UserService userService;

    private Button login_BTN_signIn;
    private EditText login_EDT_email;
    private EditText login_EDT_password;

    private boolean validEmail;
    private boolean validPassword;

    private UserBoundary myUser;

    private MySharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        validEmail = false;

        prefs = new MySharedPreferences(this);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();

        userService = new UserService(retrofit);
        userService.initLoginUserCallback(loginUserCallback);

        login_BTN_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_EDT_email.getText().toString().trim();
                String password = login_EDT_password.getText().toString().trim();

                if (!Functions.isValidEmail(email)) {
                    login_EDT_email.setError("invalid email");
                    return;
                } else {
                    validEmail = true;
                }
//                String credentialsJson = prefs.getString(PrefsKeys.USER_CREDENTIALS, null);
//                if(credentialsJson == null) {
//
//                }
                userService.loginUser(Constants.SPACE_NAME, email);
//                TODO: check if email exist in db and move to main app page.
                Intent mainPageActivityIntent = new Intent(getApplicationContext(), MainPageActivity.class);
                startActivity(mainPageActivityIntent);
            }
        });

    }

    private void findViews() {
        this.login_BTN_signIn = findViewById(R.id.login_BTN_login);
        this.login_EDT_email = findViewById(R.id.login_EDT_email);
        this.login_EDT_password = findViewById(R.id.login_EDT_password);
    }

    private Callback<UserBoundary> loginUserCallback = new Callback<UserBoundary>() {
        @Override
        public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
            if(!response.isSuccessful()) {
//                TODO: if code is 500 then notify user that user does not exist by this email.
                Log.d("vvv", response.code() + ": " + response.message());
                return;
            }

            myUser = response.body();

            String myUserJson = new Gson().toJson(myUser);
            prefs.putString(PrefsKeys.USER_BOUNDARY, myUserJson);
        }

        @Override
        public void onFailure(Call<UserBoundary> call, Throwable t) {

        }
    };


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