package com.dts.bookies.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dts.bookies.R;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.OperationBoundary;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.LocationBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.User;
import com.dts.bookies.rest.services.OperationService;

import com.dts.bookies.util.MySharedPreferences;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddBookActivity extends AppCompatActivity {
    private OperationService operationService;
    private UserBoundary myUser;
    private User user;
    private OperationBoundary operationBoundary;
    private Map<String, Object> itemDetails;
    private Map<String, Object> operationsAttributes;
    private String myLocation;

    private TextView add_TXT_name;
    private TextView add_TXT_author;
    private TextView add_TET_genre;
    private TextView add_TXT_summery;
    private Button add_BTN_submit;
    private MySharedPreferences prefs;
    private LocationBoundary locationBoundary;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        findViews();
        locationBoundary = new LocationBoundary(0.0, 0.0);

        prefs = new MySharedPreferences(this);

        operationService = new OperationService();
        operationService.initInvokeCallback(invokeCreatenewItemCallback);
        itemDetails = new HashMap<String, Object>();
        operationsAttributes = new HashMap<String, Object>();
        myLocation = prefs.getString("Location", "");
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
//            successful, create item.
            ItemBoundary newItem = new Gson().fromJson(new Gson()
                    .toJsonTree(response.body()).getAsJsonObject(), ItemBoundary.class);


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