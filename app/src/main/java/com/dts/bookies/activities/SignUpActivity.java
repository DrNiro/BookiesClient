package com.dts.bookies.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dts.bookies.R;
import com.dts.bookies.StartingActivity;
import com.dts.bookies.UserCredentials;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.rest.services.UserService;
import com.dts.bookies.util.Constants;
import com.dts.bookies.util.Functions;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;
import com.dts.bookies.util.TextValidator;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText signup_EDT_username;
    private EditText signup_EDT_avatar;
    private EditText signup_EDT_email;
    private Button   signup_BTN_createAccount;

    private boolean[] validations = {false, false, false};

    private UserService userService;
    private UserBoundary newUser;

    private MySharedPreferences prefs;

    private boolean userCreatedFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();

        prefs = new MySharedPreferences(this);

        userService = new UserService();
        userService.initCreateNewUserCallback(createNewUserCallback);

        signup_BTN_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(validations[0] && validations[1] && validations[2])) {
                    Toast.makeText(SignUpActivity.this, "invalid fields", Toast.LENGTH_LONG);
                    return;
                }

                String username = signup_EDT_username.getText().toString();
                String avatar = signup_EDT_avatar.getText().toString();
                String email = signup_EDT_email.getText().toString();

                Log.d("vvv", "username: " + username);
                Log.d("vvv", "avatar: " + avatar);
                Log.d("vvv", "email: " + email);

                userService.createNewUser(email, "PLAYER", username, avatar);

                Log.d("vvv", "-----TESTING----- checking userCreatedFlag");
//                check if user created successfully, else don't save credentials and don't move to the next page.
                if(!userCreatedFlag)
                    return;

            }
        });

//        validate input details text changed listeners
        signup_EDT_username.addTextChangedListener(new TextValidator(signup_EDT_username) {
            @Override
            public void validate(EditText editText, String text) {
                Log.d("vvv", "In TextValidator");
                if(this.isEmpty()) {
                    signup_EDT_username.setError("must have username");
                    validations[0] = false;
                } else {
                    validations[0] = true;
                }
            }
        });

        signup_EDT_avatar.addTextChangedListener(new TextValidator(signup_EDT_avatar) {
            @Override
            public void validate(EditText editText, String text) {
                if(this.isEmpty()) {
                    signup_EDT_avatar.setError("must have an avatar (character sequence)");
                    validations[1] = false;
                } else {
                    validations[1] = true;
                }
            }
        });

        signup_EDT_email.addTextChangedListener(new TextValidator(signup_EDT_email) {
            @Override
            public void validate(EditText editText, String text) {
                if(!Functions.isValidEmail(text)) {
                    signup_EDT_email.setError("invalid email");
                    validations[2] = false;
                } else {
                    validations[2] = true;
                }
            }
        });

    }

    private void findViews() {
        signup_EDT_username = findViewById(R.id.signup_EDT_username);
        signup_EDT_avatar = findViewById(R.id.signup_EDT_avatar);
        signup_EDT_email = findViewById(R.id.signup_EDT_email);
        signup_BTN_createAccount = findViewById(R.id.signup_BTN_createAccount);
    }

    private Callback<UserBoundary> createNewUserCallback = new Callback<UserBoundary>() {
        @Override
        public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
            if(!response.isSuccessful()) {
                Log.d("vvv", response.code() + ": " + response.message());
                if(response.code() == 500) {
                    signup_EDT_email.setError("email already exist ");
                    Log.d("vvv", "user already exist by this email");
                }
                // code 500 means user already exist with this email and need to notify user (maybe with toast message).
                return;
            }

//            successful, create user.
            newUser = response.body();

//            save user details in local memory as json.
            String newUserJson = new Gson().toJson(newUser);
            Functions.saveUserToPrefs(newUserJson, prefs, 1);

            Log.d("vvv", "new user: " + newUser.toString());

//            move to profile/main page.
            Intent mainPageActivityIntent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivity(mainPageActivityIntent);
            SignUpActivity.this.finish();

//            pop-up account created successfully.
            Toast.makeText(SignUpActivity.this, "Account created", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<UserBoundary> call, Throwable t) {
            Log.d("vvv", "FAILED " + t.getMessage());
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startingActivityIntent = new Intent(getApplicationContext(), StartingActivity.class);
        startActivity(startingActivityIntent);
        SignUpActivity.this.finish();
    }

}