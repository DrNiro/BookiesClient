package com.dts.bookies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dts.bookies.R;
import com.dts.bookies.StartingActivity;
import com.dts.bookies.logic.boundaries.NewUserDetails;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.User;
import com.dts.bookies.rest.services.ItemService;
import com.dts.bookies.rest.services.UserService;
import com.dts.bookies.util.Functions;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private RelativeLayout settings_BTN_logout;
    private RelativeLayout settings_BTN_editUsername;
    private RelativeLayout settings_BTN_editAvatar;

    private RelativeLayout settings_LAY_editUsernameEditor;
    private EditText settings_EDT_editUsername;
    private TextView settings_BTN_okUsername;
    private RelativeLayout settings_LAY_editAvatarEditor;
    private EditText settings_EDT_editAvatar;
    private TextView settings_BTN_okAvatar;

    private MySharedPreferences prefs;

    private UserBoundary myUser;
    private String newUsername;
    private String newAvatar;
    private boolean dirtyProfile = false;
    private boolean userNameFlag = false;
    private boolean avatarFlag = false;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViews();

        prefs = new MySharedPreferences(this);
        myUser = Functions.getUserBoundaryFromPrefs(prefs);
        userService = new UserService();
        userService.initUpdateUserCallback(updateUserCallback);

        settings_BTN_editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(settings_LAY_editUsernameEditor.getVisibility() == View.GONE) {
                    settings_LAY_editUsernameEditor.setVisibility(View.VISIBLE);
                } else {
                    settings_LAY_editUsernameEditor.setVisibility(View.GONE);
                    settings_EDT_editUsername.setText("");
                }
            }
        });

        settings_BTN_okUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUsername = settings_EDT_editUsername.getText().toString().trim();
                if(newUsername.equals("")) {
                    settings_EDT_editUsername.setError("Username can't be empty");
                } else {
                    Log.d("ttt", "new username: " + newUsername);
                    UserBoundary updateUser = new UserBoundary(new NewUserDetails(myUser.getUserId().getEmail(), myUser.getRole(), newUsername, myUser.getAvatar()));
                    userNameFlag = true;
                    userService.updateUser(myUser.getUserId().getSpace(), myUser.getUserId().getEmail(), updateUser);
                }
            }
        });

        settings_BTN_editAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(settings_LAY_editAvatarEditor.getVisibility() == View.GONE) {
                    settings_LAY_editAvatarEditor.setVisibility(View.VISIBLE);
                } else {
                    settings_LAY_editAvatarEditor.setVisibility(View.GONE);
                    settings_EDT_editAvatar.setText("");
                }
            }
        });

        settings_BTN_okAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAvatar = settings_EDT_editAvatar.getText().toString().trim();
                if(newAvatar.equals("")) {
                    settings_EDT_editAvatar.setError("Avatar can't be empty");
                } else {
                    Log.d("ttt", "new avatar: " + newAvatar);
                    UserBoundary updateUser = new UserBoundary(new NewUserDetails(myUser.getUserId().getEmail(), myUser.getRole(), myUser.getUsername(), newAvatar));
                    avatarFlag = true;
                    userService.updateUser(myUser.getUserId().getSpace(), myUser.getUserId().getEmail(), updateUser);
                }
            }
        });

        settings_BTN_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.putInt(PrefsKeys.LOGGED_STATE, 0);
                Intent startingActivityIntent = new Intent(getApplicationContext(), StartingActivity.class);
                setResult(Activity.RESULT_OK);
                startActivity(startingActivityIntent);
                SettingsActivity.this.finish();
            }
        });

    }

    private void findViews() {
        settings_BTN_logout = findViewById(R.id.settings_BTN_logout);
        settings_BTN_editUsername = findViewById(R.id.settings_BTN_editUsername);
        settings_BTN_editAvatar = findViewById(R.id.settings_BTN_editAvatar);

        settings_LAY_editUsernameEditor = findViewById(R.id.settings_LAY_editUsernameEditor);
        settings_EDT_editUsername = findViewById(R.id.settings_EDT_editUsername);
        settings_BTN_okUsername = findViewById(R.id.settings_BTN_okUsername);
        settings_LAY_editAvatarEditor = findViewById(R.id.settings_LAY_editAvatarEditor);
        settings_EDT_editAvatar = findViewById(R.id.settings_EDT_editAvatar);
        settings_BTN_okAvatar = findViewById(R.id.settings_BTN_okAvatar);
    }

    private Callback<Void> updateUserCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if (!response.isSuccessful()) {
                Log.d("aaa", response.code() + ": " + response.message());
                return;
            }

            if(response.code() >= 200) {
                if(userNameFlag) {
                    myUser.setUsername(newUsername);
                    String myUserJson = new Gson().toJson(myUser);
                    prefs.putString(PrefsKeys.USER_BOUNDARY, myUserJson);
                    settings_LAY_editUsernameEditor.setVisibility(View.GONE);
                    Toast.makeText(SettingsActivity.this, "Username updated", Toast.LENGTH_SHORT);
                    userNameFlag = false;
                } else if(avatarFlag) {
                    myUser.setAvatar(newAvatar);
                    String myUserJson = new Gson().toJson(myUser);
                    prefs.putString(PrefsKeys.USER_BOUNDARY, myUserJson);
                    settings_LAY_editAvatarEditor.setVisibility(View.GONE);
                    Toast.makeText(SettingsActivity.this, "Avatar updated", Toast.LENGTH_SHORT);
                    avatarFlag = false;
                }
                dirtyProfile = true;
            }

        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {

        }
    };

    @Override
    public void onBackPressed() {
        if(dirtyProfile) {
            Intent updateProfileIntent = new Intent();
            updateProfileIntent.putExtra("updateProfile", true);
            setResult(Activity.RESULT_OK, updateProfileIntent);
        }
        super.onBackPressed();
    }
}