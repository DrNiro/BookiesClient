package com.dts.bookies.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dts.bookies.R;
import com.dts.bookies.StartingActivity;
import com.dts.bookies.activities.fragments.MapFragment;
import com.dts.bookies.activities.fragments.ProfileFragment;
import com.dts.bookies.activities.fragments.SearchFragment;
import com.dts.bookies.callbacks.ButtonClickedCallback;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.LocationBoundary;
import com.dts.bookies.rest.services.ItemService;
import com.dts.bookies.util.Functions;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;
import com.dts.bookies.util.memento.FragmentsMementoManager;
import com.dts.bookies.util.memento.MementoStates;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageActivity extends FragmentActivity {

    private ProfileFragment profileFragment;
    private MapFragment mapFragment;
    private SearchFragment searchFragment;
    private LocationBoundary locationBoundary;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

//    TODO: add book option in toolbar leads to a new activity which you create there, not another fragment.

    private ImageView main_BTN_profile;
    private ImageView main_BTN_map;
    private ImageView main_BTN_search;
    private ImageView main_BTN_addBook;

    private Map<String, ImageView> imageButtonsMap;
    private Map<String, Fragment> fragmentsMap;

    private FragmentsMementoManager mementoManager;
    private MySharedPreferences prefs;

    private ItemBoundary[] itemList;
    private ItemService itemService;
    private UserBoundary myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        findViews();
        initFragmentsAndMemento();
        initMaps();
        stageFragments(profileFragment);
        permissionsForLocation();

        locationBoundary = new LocationBoundary();
        prefs = new MySharedPreferences(this);
        itemService = new ItemService();
        itemService.initGetAllItemsCallback(getAllItemsCallBack);
        getUserFromPrefs();

        itemService.getAllBookItems(myUser.getUserId().getSpace(),myUser.getUserId().getEmail());

        main_BTN_profile.setOnClickListener(profileClickListener);

        main_BTN_map.setOnClickListener(mapClickListener);

        main_BTN_search.setOnClickListener(searchClickListener);

        main_BTN_addBook.setOnClickListener(addClickListener);

    }

    private void findViews() {
        main_BTN_profile = findViewById(R.id.main_BTN_profile);
        main_BTN_map = findViewById(R.id.main_BTN_map);
        main_BTN_search = findViewById(R.id.main_BTN_search);
        main_BTN_addBook = findViewById(R.id.main_BTN_addBook);
    }


    private void initFragmentsAndMemento() {
        profileFragment = new ProfileFragment();
        profileFragment.setCallback(buttonClickedCallback);
        mapFragment = new MapFragment();
        searchFragment = new SearchFragment();

        mementoManager = new FragmentsMementoManager();
    }

    private void initMaps() {
        imageButtonsMap = new HashMap<>();
        imageButtonsMap.put(profileFragment.getClass().getSimpleName(), main_BTN_profile);
        imageButtonsMap.put(mapFragment.getClass().getSimpleName(), main_BTN_map);
        imageButtonsMap.put(searchFragment.getClass().getSimpleName(), main_BTN_search);

        fragmentsMap = new HashMap<>();
        fragmentsMap.put(MementoStates.PROFILE_STATE, profileFragment);
        fragmentsMap.put(MementoStates.MAP_STATE, mapFragment);
        fragmentsMap.put(MementoStates.SEARCH_STATE, searchFragment);

    }

    private void stageFragments(Fragment firstFocusFrag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_LAY_mainWindow, profileFragment);
        transaction.add(R.id.main_LAY_mainWindow, mapFragment);
        transaction.add(R.id.main_LAY_mainWindow, searchFragment);
        transaction.hide(profileFragment);
        transaction.hide(mapFragment);
        transaction.hide(searchFragment);
        transaction.show(firstFocusFrag);
        transaction.commit();

        mementoManager.setCurrentFragment(firstFocusFrag);
        markToolbarImageByFragment(firstFocusFrag);
    }

    private void switchFragmentFocus(Fragment offFocusFrag, Fragment newFocusFrag) {
        if(offFocusFrag.equals(newFocusFrag))
            return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(offFocusFrag);
        transaction.show(newFocusFrag);
        mementoManager.setCurrentFragment(newFocusFrag);
        transaction.commit();

        switchToolbarFocus(offFocusFrag, newFocusFrag);

        Functions.printMementoList(mementoManager.getMementoList());
    }

    private void switchToolbarFocus(Fragment offFocusFrag, Fragment newFocusFrag) {
        if(offFocusFrag != null) {
            unmarkToolbarImageByFragment(offFocusFrag);
        }
        markToolbarImageByFragment(newFocusFrag);
    }

    private void markToolbarImageByFragment(Fragment fragment) {
        imageButtonsMap.get(fragment.getClass().getSimpleName()).setBackgroundResource(R.color.gray);
    }

    private void unmarkToolbarImageByFragment(Fragment fragment) {
        imageButtonsMap.get(fragment.getClass().getSimpleName()).setBackgroundResource(R.color.design_default_color_background);
    }

    private void nextFragClick(Fragment currentFragment, Fragment nextFragment) {
        mementoManager.saveToMemento(currentFragment, nextFragment);
        switchFragmentFocus(currentFragment, nextFragment);
    }

    private void previousFragClick(Fragment currentFragment) {
//        get last memento (last frag visited) and switch fragment focus
        mementoManager.setOriginatorState(mementoManager.getAndRemoveLastMemento());
        Fragment previousFrag = fragmentsMap.get(mementoManager.getOriginatorState());
        switchFragmentFocus(currentFragment, previousFrag);
    }

    @Override
    public void onBackPressed() {
//        if no fragment history - do super function (previous activity or minimize if none). else - go to previous fragment.
        if(mementoManager.getMementoList().isEmpty()) {
            super.onBackPressed();
        } else {
            previousFragClick(mementoManager.getCurrentFragment());
        }
    }

    private View.OnClickListener profileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nextFragClick(mementoManager.getCurrentFragment(), profileFragment);
        }
    };

    private View.OnClickListener mapClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nextFragClick(mementoManager.getCurrentFragment(), mapFragment);
        }
    };

    private View.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nextFragClick(mementoManager.getCurrentFragment(), searchFragment);
        }
    };
    private View.OnClickListener addClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent addActivityIntent = new Intent(getApplicationContext(), AddBookActivity.class);
            startActivity(addActivityIntent);
        }
    };

    private ButtonClickedCallback buttonClickedCallback = new ButtonClickedCallback() {
        @Override
        public void buttonClicked(View btn) {
            if (btn.getId() == R.id.profile_BTN_logout) { // logout button
                Intent startingActivityIntent = new Intent(getApplicationContext(), StartingActivity.class);
                startActivity(startingActivityIntent);
                MainPageActivity.this.finish();
            }
        }
    };
    public void permissionsForLocation() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainPageActivity.this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationServices.getFusedLocationProviderClient(MainPageActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainPageActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                           // Log.d("vvvvv", "latitude: " + latitude);
                          //  Log.d("vvvvv", "latitude: " + longitude);
                            locationBoundary.setLat(latitude);
                            locationBoundary.setLng(longitude);
                            String locationJson = new Gson().toJson(locationBoundary);
                            prefs.putString(PrefsKeys.LOCATION, locationJson);
                            Log.d("vvvvv", "locationBoundary1: " +
                                    prefs.getString("Location", ""));
                            Log.d("vvvvv", "locationBoundary2: " + locationBoundary);

                        }
                    }
                }, Looper.myLooper());
    }

    private void getUserFromPrefs() {
        String userJson = prefs.getString(PrefsKeys.USER_BOUNDARY, "");
        if (!userJson.equals("")) {
            myUser = new Gson().fromJson(userJson, UserBoundary.class);
        } else {
            Log.d("vvv", "user not found in preferences");
        }
    }
    private Callback<ItemBoundary[]> getAllItemsCallBack = new Callback<ItemBoundary[]>() {
        @Override
        public void onResponse(Call<ItemBoundary[]> call, Response<ItemBoundary[]> response) {
            if(!response.isSuccessful()) {
                if(response.code() == 404) {
                    Log.d("vvv", "404: user not found");
                }
                Log.d("vvv", response.code() + ": " + response.message());
                return;
            }

            itemList = response.body();
            String itemListJson = new Gson().toJson(itemList);
            prefs.putString(PrefsKeys.ITEM_LIST, itemListJson);
//            mapFragment.initialMap();
        }

        @Override
        public void onFailure(Call<ItemBoundary[]> call, Throwable t) {
            Log.d("vvv", "failure login, message: " + t.getMessage());
        }
    };

}


