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
import com.dts.bookies.UserCredentials;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.UserRoleBoundary;
import com.dts.bookies.rest.services.ItemService;
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
import retrofit2.converter.gson.GsonConverterFactory;

public class AddBookActivity extends AppCompatActivity {
    private Retrofit retrofit;

    private UserService userService;
    private UserBoundary myUser;
    private ItemBoundary bookItem;
    private ItemService itemService;
    private Map<String, Object> itemDetails;

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
        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
        userService = new UserService(retrofit); //TODO change3
        userService.initUpdateUserCallback(updateUserCallback); // TODO change4
        itemService = new ItemService(retrofit);
        itemService.initCreateItemCallback(createNewItemCallback);

        itemDetails = new HashMap<String, Object>();


        add_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = add_TXT_name.getText().toString();
                String author = add_TXT_author.getText().toString();
                String genre = add_TET_genre.getText().toString();
                String summery = add_TXT_summery.getText().toString();

                itemDetails.put("author", author);
                itemDetails.put("genre", genre);
                itemDetails.put("summery", summery);
                bookItem = new ItemBoundary();
                bookItem.setName(bookName);
                bookItem.setType("book");
                bookItem.setActive(true);
                bookItem.setItemAttributes(itemDetails);

//                TODO: replace this with item creation system that enables PLAYER role to create items.
                String managerSpace = myUser.getUserId().getSpace();
                String managerEmail = myUser.getUserId().getEmail();
                myUser.setRole(UserRoleBoundary.MANAGER); //TODO change1
                userService.updateUser(managerSpace, managerEmail, myUser); //TODO change2
                Log.d("vvv", "1. userRole before createNewBook: " + myUser.getRole().name());

                itemService.createNewBookItem(managerSpace, managerEmail, bookItem);

                myUser.setRole(UserRoleBoundary.PLAYER);  // TODO change3
                userService.updateUser(managerSpace, managerEmail, myUser); //TODO change4
                Log.d("vvv", "4. userRole AFTER createNewBook: " + myUser.getRole().name());

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

    private Callback<ItemBoundary> createNewItemCallback = new Callback<ItemBoundary>() {
        @Override
        public void onResponse(Call<ItemBoundary> call, Response<ItemBoundary> response) {
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
            bookItem = response.body();


            Log.d("vvv", "new user: " + bookItem.toString());


//            move to profile/main page.
            Intent mainPageActivityIntent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivity(mainPageActivityIntent);
            AddBookActivity.this.finish();

//            pop-up account created successfully.
            Toast.makeText(AddBookActivity.this, "Book created and added", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<ItemBoundary> call, Throwable t) {
            Log.d("vvv", "FAILED " + t.getMessage());
        }
    };

    private Callback<Void> updateUserCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if (!response.isSuccessful()) {
                Log.d("vvv", response.code() + ": " + response.message());
                if (response.code() == 400) {
                    //signup_EDT_email.setError("email already exist ");
                    Log.d("vvv", " already exist by this email");
                }
                return;
            }


        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Log.d("vvv", "FAILED " + t.getMessage());

        }
    };

}