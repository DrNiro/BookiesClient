package com.dts.bookies.activities;

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

    private final int NUM_OF_EDIT_TEXTS = 5;

    private EditText signup_EDT_username;
    private EditText signup_EDT_avatar;
    private EditText signup_EDT_email;
    private EditText signup_EDT_password;
    private EditText signup_EDT_confirmPassword;
    private Button signup_BTN_createAccount;

//    private boolean[] validations = new boolean[NUM_OF_EDIT_TEXTS];
    private boolean[] validations = {false, false, false, false, false};

    private UserService userService;
    private UserBoundary newUser;
    private UserCredentials userCredentials;

    private MySharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();
        Log.d("vvv", "initialized edit texts.");

        this.prefs = new MySharedPreferences(this);

        this.userService = new UserService();
        this.userService.initCreateNewUserCallback(createNewUserCallback);

        this.signup_BTN_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(validations[0] && validations[1] && validations[2] && validations[3] && validations[4])) {
                    Toast.makeText(SignUpActivity.this, "invalid fields", Toast.LENGTH_LONG);
                    return;
                }

                String username = signup_EDT_username.getText().toString();
                String avatar = signup_EDT_avatar.getText().toString();
                String email = signup_EDT_email.getText().toString();
                String password = signup_EDT_password.getText().toString();
                String confirmPassword = signup_EDT_confirmPassword.getText().toString();

                Log.d("vvv", "username: " + username);
                Log.d("vvv", "avatar: " + avatar);
                Log.d("vvv", "email: " + email);
                Log.d("vvv", "password: " + password);

                userService.createNewUser(email, "PLAYER", username, avatar);

//                Log.d("vvv", "new user: " + newUser.toString());

                userCredentials = new UserCredentials(email, password);
                Log.d("vvv", "hash password: " + userCredentials.getHashedPassword());

//            move to profile/main page.
                Intent mainPageActivityIntent = new Intent(getApplicationContext(), MainPageActivity.class);
                startActivity(mainPageActivityIntent);
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
                    validations[4] = false;
                } else {
                    validations[4] = true;
                }
            }
        });

        signup_EDT_email.addTextChangedListener(new TextValidator(signup_EDT_email) {
            @Override
            public void validate(EditText editText, String text) {
                if(!Functions.isValidEmail(text)) {
                    signup_EDT_email.setError("invalid email");
                    validations[3] = false;
                } else {
                    validations[3] = true;
                }
            }
        });

        signup_EDT_password.addTextChangedListener(new TextValidator(signup_EDT_password) {
            @Override
            public void validate(EditText editText, String text) {
                if(text.length() < 6) {
                    signup_EDT_password.setError("at least 6 characters");
                    validations[1] = false;
                } else if(!(text.matches(Constants.VALID_PASSWORD_REGEX.pattern()))) {
                    signup_EDT_password.setError("password must contains numbers and letters");
                    validations[1] = false;
                } else {
                    validations[1] = true;
                }
            }
        });

        signup_EDT_confirmPassword.addTextChangedListener(new TextValidator(signup_EDT_confirmPassword) {
            @Override
            public void validate(EditText editText, String text) {
                if(signup_EDT_password.getError() != null) {
                    signup_EDT_confirmPassword.setError("insert valid password above");
                    validations[2] = false;
                } else if(!text.equals(signup_EDT_password.getText().toString())) {
                    signup_EDT_confirmPassword.setError("");
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
        signup_EDT_password = findViewById(R.id.signup_EDT_password);
        signup_EDT_confirmPassword = findViewById(R.id.signup_EDT_confirmPassword);
        signup_BTN_createAccount = findViewById(R.id.signup_BTN_createAccount);
    }

    private Callback<UserBoundary> createNewUserCallback = new Callback<UserBoundary>() {
        @Override
        public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
            if(!response.isSuccessful()) {
                Log.d("vvv", response.code() + ": " + response.message());
                // code 500 means user already exist with this email and need to notify user (maybe with toast message).
                return;
            }

//            successful, create user.
            newUser = response.body();

//            save user details in local memory as json.
            String newUserJson = new Gson().toJson(newUser);
            prefs.putString(PrefsKeys.USER_BOUNDARY, newUserJson);

            Log.d("vvv", "new user: " + newUser.toString());

//            save user credentials in local memory as json.
//            String userCredentialsJson = new Gson().toJson(userCredentials);
//            prefs.putString(PrefsKeys.USER_CREDENTIALS, userCredentialsJson);

//            Log.d("vvv", "credentials: " + userCredentialsJson);

//            pop-up account created successfully.
            Toast.makeText(SignUpActivity.this, "Account created", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<UserBoundary> call, Throwable t) {
            Log.d("vvv", "FAILED " + t.getMessage());
        }
    };

//    private TextValidator usernameValidator = new TextValidator(signup_EDT_username) {
//        @Override
//        public void validate(EditText editText, String text) {
//            Log.d("vvv", "In TextValidator");
//            if(this.isEmpty()) {
//                signup_EDT_username.setError("must have username");
//                validations[0] = false;
//            } else {
//                validations[0] = true;
//            }
//        }
//    };
//
//    private TextValidator passwordValidator = new TextValidator(signup_EDT_password) {
//        @Override
//        public void validate(EditText editText, String text) {
//            if(text.length() < 6) {
//                signup_EDT_password.setError("at least 6 characters");
//                validations[1] = false;
//            } else if(!(text.matches("[a-zA-Z]+") && text.matches("[0-9]+"))) {
//                signup_EDT_password.setError("password must contains numbers and letters");
//                validations[1] = false;
//            } else {
//                validations[1] = true;
//            }
//        }
//    };
//
//    private TextValidator confirmPasswordValidator = new TextValidator(signup_EDT_confirmPassword) {
//        @Override
//        public void validate(EditText editText, String text) {
//            if(signup_EDT_password.getError() != null) {
//                signup_EDT_confirmPassword.setError("insert valid password above");
//                validations[2] = false;
//            } else if(!text.equals(signup_EDT_password.getText().toString())) {
//                signup_EDT_confirmPassword.setError("");
//                validations[2] = false;
//            } else {
//                validations[2] = true;
//            }
//        }
//    };
//
//    private TextValidator emailValidator = new TextValidator(signup_EDT_email) {
//        @Override
//        public void validate(EditText editText, String text) {
//            if(!Functions.isValidEmail(text)) {
//                signup_EDT_email.setError("invalid email");
//                validations[3] = false;
//            } else {
//                validations[3] = true;
//            }
//        }
//    };
//
//    private TextValidator avatarValidator = new TextValidator(signup_EDT_avatar) {
//        @Override
//        public void validate(EditText editText, String text) {
//            if(this.isEmpty()) {
//                signup_EDT_avatar.setError("must have an avatar (character sequence)");
//                validations[4] = false;
//            } else {
//                validations[4] = true;
//            }
//        }
//    };

}