package com.dts.bookies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dts.bookies.activities.LoginActivity;
import com.dts.bookies.activities.SignUpActivity;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.rest.services.UserService;
import com.dts.bookies.util.Constants;
import com.dts.bookies.util.MySharedPreferences;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartingActivity extends AppCompatActivity {

    private Button main_BTN_login;
    private Button main_BTN_signUp;

    private MySharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        this.prefs = new MySharedPreferences(this);

        findViews();

//        TODO: if user is already logged in - go straight to profile/main page, else stay here.

        this.main_BTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpActivityIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signUpActivityIntent);
            }
        });

        this.main_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivityIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivityIntent);
            }
        });

    }

    private void findViews() {
        this.main_BTN_login = findViewById(R.id.main_BTN_login);
        this.main_BTN_signUp = findViewById(R.id.main_BTN_signUp);
    }

}
