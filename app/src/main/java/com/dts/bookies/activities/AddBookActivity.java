package com.dts.bookies.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dts.bookies.R;
import com.dts.bookies.activities.fragments.MapFragment;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.OperationBoundary;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.LocationBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.User;
import com.dts.bookies.rest.services.ItemService;
import com.dts.bookies.rest.services.OperationService;
import com.dts.bookies.rest.services.UserService;
import com.dts.bookies.util.Constants;
import com.dts.bookies.util.Functions;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddBookActivity extends AppCompatActivity {
    private OperationService operationService;
    private UserBoundary myUser;
    private User user;
    private OperationBoundary operationBoundary;
    private Map<String, Object> itemDetails;
    private Map<String, Object> operationsAttributes;
    private LocationBoundary myLocation;

    private TextView add_TXT_name;
    private TextView add_TXT_author;
    private TextView add_TET_genre;
    private TextView add_TXT_summery;
    private Button add_BTN_submit;
    private MySharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        findViews();

        prefs = new MySharedPreferences(this);
        getUserFromPrefs();
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final MapFragment mapFragment = new MapFragment();
        Bundle b = new Bundle();
        b.putString("space", myUser.getUserId().getSpace());
        b.putString("email",myUser.getUserId().getEmail());
        mapFragment.setArguments(b);
        fragmentTransaction.add(R.id.frame_layout, mapFragment).commit();*/
        /*// set Fragmentclass Arguments
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(bundle);*/

        operationService = new OperationService();
        operationService.initInvokeCallback(invokeCreatenewItemCallback);
        itemDetails = new HashMap<String, Object>();
        operationsAttributes = new HashMap<String, Object>();
        myLocation = new Gson().fromJson
                (prefs.getString(PrefsKeys.LOCATION, ""), LocationBoundary.class);
        user = new User();
        add_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bookName = add_TXT_name.getText().toString();
                String author = add_TXT_author.getText().toString();
                String genre = add_TET_genre.getText().toString();
                String summery = add_TXT_summery.getText().toString();

                operationBoundary = new OperationBoundary();
                operationBoundary.setType("createNewBook");
                itemDetails.put("author", author);
                itemDetails.put("genre", genre);
                itemDetails.put("summery", summery);
                itemDetails.put("owner", myUser.getUsername());
                operationsAttributes.put("bookName", bookName);
                operationsAttributes.put("bookLocation", myLocation);
                operationsAttributes.put("bookAttributes", itemDetails);
                user.setUserId(myUser.getUserId());
                operationBoundary.setInvokedBy(user);
                operationBoundary.setOperationAttributes(operationsAttributes);

                operationService.invokeOperation(operationBoundary);

            }
        });


    }



    private void getUserFromPrefs() {
        String userJson = prefs.getString(PrefsKeys.USER_BOUNDARY, "");
        if (!userJson.equals("")) {
            myUser = new Gson().fromJson(userJson, UserBoundary.class);
        } else {
            Log.d("vvv", "user not found in preferences");
        }
    }


    private void findViews() {
        add_TXT_name = findViewById(R.id.add_EDT_BookName);
        add_TXT_author = findViewById(R.id.add_EDT_Author);
        add_TET_genre = findViewById(R.id.add_EDT_genre);
        add_TXT_summery = findViewById(R.id.add_EDT_adstractSummery);
        add_BTN_submit = findViewById(R.id.add_BTN_Submit);
    }

    private Callback<Object> invokeCreatenewItemCallback = new Callback<Object>() {
        @Override
        public void onResponse(Call<Object> call, Response<Object> response) {
            if (!response.isSuccessful()) {
                Log.d("vvv", response.code() + ": " + response.message());
                if (response.code() == 400) {
                    //signup_EDT_email.setError("email already exist ");
                    Log.d("vvv", " already exist by this email");
                }
                return;
            }
            Log.d("vvv", "3. in callback");
//            successful, create user.
            ItemBoundary newItem = new Gson().fromJson(new Gson().toJsonTree(response.body()).getAsJsonObject(), ItemBoundary.class);
            ;


            Log.d("vvv", "new user: " + operationBoundary.toString());


//            move to profile/main page.
            Intent mainPageActivityIntent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivity(mainPageActivityIntent);
            AddBookActivity.this.finish();

//            pop-up account created successfully.
            Toast.makeText(AddBookActivity.this, "Book created and added", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<Object> call, Throwable t) {
            Log.d("vvv", "FAILED " + t.getMessage());
        }
    };

}