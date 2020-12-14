package com.dts.bookies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dts.bookies.activities.LoginActivity;
import com.dts.bookies.activities.MainPageActivity;
import com.dts.bookies.activities.SignUpActivity;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;

public class StartingActivity extends AppCompatActivity {

    private Button main_BTN_login;
    private Button main_BTN_signUp;

    private MySharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        prefs = new MySharedPreferences(this);

//        prefs.putInt(PrefsKeys.LOGGED_STATE, 0); //        uncomment this line to cancel auto login, OR comment back to let system behave normally.
        if(prefs.getInt(PrefsKeys.LOGGED_STATE, 0) > 0) {   // if logged in
//            go straight into account
            Intent mainPageActivityIntent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivity(mainPageActivityIntent);
            StartingActivity.this.finish();
            return;
        }

        findViews();

        main_BTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpActivityIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signUpActivityIntent);
                StartingActivity.this.finish();
            }
        });

        main_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivityIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivityIntent);
                StartingActivity.this.finish();
            }
        });

    }

    private void findViews() {
        main_BTN_login = findViewById(R.id.starting_BTN_login);
        main_BTN_signUp = findViewById(R.id.starting_BTN_signUp);
    }

}
