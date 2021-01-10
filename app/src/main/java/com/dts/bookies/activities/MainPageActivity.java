package com.dts.bookies.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dts.bookies.R;
import com.dts.bookies.StartingActivity;
import com.dts.bookies.activities.fragments.ChatFragment;
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
    private ChatFragment chatFragment;
    private LocationBoundary locationBoundary;
    private LocationBoundary myLocation;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private ImageView main_BTN_profile;
    private ImageView main_BTN_map;
    private ImageView main_BTN_addBook;
    private ImageView main_BTN_search;
    private ImageView main_BTN_chat;

    private Map<String, ImageView> imageButtonsMap;
    private Map<String, Fragment> fragmentsMap;
    private Map<String, Integer> drawablesMap;

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
        myUser = Functions.getUserBoundaryFromPrefs(prefs);

        main_BTN_profile.setOnClickListener(profileClickListener);

        main_BTN_map.setOnClickListener(mapClickListener);

        main_BTN_addBook.setOnClickListener(addBookClickListener);

        main_BTN_addBook.setOnClickListener(addClickListener);

        main_BTN_search.setOnClickListener(searchClickListener);

        main_BTN_chat.setOnClickListener(chatClickListener);
    }

    private void findViews() {
        main_BTN_profile = findViewById(R.id.main_BTN_profile);
        main_BTN_map = findViewById(R.id.main_BTN_map);
        main_BTN_addBook = findViewById(R.id.main_BTN_addBook);
        main_BTN_search = findViewById(R.id.main_BTN_search);
        main_BTN_chat = findViewById(R.id.main_BTN_chat);
    }

    private void initFragmentsAndMemento() {
        profileFragment = new ProfileFragment();
        profileFragment.setCallback(buttonClickedCallback);
        mapFragment = new MapFragment();
        searchFragment = new SearchFragment();
        chatFragment = new ChatFragment();

        mementoManager = new FragmentsMementoManager();
    }

    private void initMaps() {
        imageButtonsMap = new HashMap<>();
        imageButtonsMap.put(profileFragment.getClass().getSimpleName(), main_BTN_profile);
        imageButtonsMap.put(mapFragment.getClass().getSimpleName(), main_BTN_map);
        imageButtonsMap.put(searchFragment.getClass().getSimpleName(), main_BTN_search);
        imageButtonsMap.put(chatFragment.getClass().getSimpleName(), main_BTN_chat);

        drawablesMap = new HashMap<>();
        drawablesMap.put(profileFragment.getClass().getSimpleName() + "unf", R.drawable.toolbar_img_library_unfocused);
        drawablesMap.put(profileFragment.getClass().getSimpleName() + "f", R.drawable.toolbar_img_library_focused);
        drawablesMap.put(mapFragment.getClass().getSimpleName() + "unf", R.drawable.toolbar_img_map_unfocused);
        drawablesMap.put(mapFragment.getClass().getSimpleName() + "f", R.drawable.toolbar_img_map_focused);
        drawablesMap.put(searchFragment.getClass().getSimpleName() + "unf", R.drawable.toolbar_img_search_unfocused);
        drawablesMap.put(searchFragment.getClass().getSimpleName() + "f", R.drawable.toolbar_img_search_focused);
        drawablesMap.put(chatFragment.getClass().getSimpleName() + "unf", R.drawable.toolbar_img_chat_unfocused);
        drawablesMap.put(chatFragment.getClass().getSimpleName() + "f", R.drawable.toolbar_img_chat_focused);

        fragmentsMap = new HashMap<>();
        fragmentsMap.put(MementoStates.PROFILE_STATE, profileFragment);
        fragmentsMap.put(MementoStates.MAP_STATE, mapFragment);
        fragmentsMap.put(MementoStates.SEARCH_STATE, searchFragment);
        fragmentsMap.put(MementoStates.CHAT_STATE, chatFragment);

    }

    private void stageFragments(Fragment firstFocusFrag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_LAY_mainWindow, profileFragment);
        transaction.add(R.id.main_LAY_mainWindow, mapFragment);
        transaction.add(R.id.main_LAY_mainWindow, searchFragment);
        transaction.add(R.id.main_LAY_mainWindow, chatFragment);
        transaction.hide(profileFragment);
        transaction.hide(mapFragment);
        transaction.hide(searchFragment);
        transaction.hide(chatFragment);
        transaction.show(firstFocusFrag);
        transaction.commit();

        mementoManager.setCurrentFragment(firstFocusFrag);
        changeToolbarFocusByFragment(firstFocusFrag, true);
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
            changeToolbarFocusByFragment(offFocusFrag, false);
        }
        changeToolbarFocusByFragment(newFocusFrag, true);
    }

    private void changeToolbarFocusByFragment(Fragment fragment, boolean isFocused) {
        if(isFocused) {
            imageButtonsMap.get(fragment.getClass().getSimpleName())
                    .setImageResource(drawablesMap.get(fragment.getClass().getSimpleName()+"f"));

        } else {
            imageButtonsMap.get(fragment.getClass().getSimpleName())
                    .setImageResource(drawablesMap.get(fragment.getClass().getSimpleName()+"unf"));
        }
    }

    private void nextFragClick(Fragment currentFragment, Fragment nextFragment) {
        mementoManager.saveToMemento(currentFragment, nextFragment);
        switchFragmentFocus(currentFragment, nextFragment);
    }

    private void previousFragClick(Fragment currentFragment) {
//        get last memento (last frag visited) and switch fragment focus
        mementoManager.pullLastFromMemento();
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

    private View.OnClickListener addBookClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Intent addBookActivityIntent = new Intent(getApplicationContext(), MapsActivity.class);
            Intent addBookActivityIntent = new Intent(getApplicationContext(), AddBookActivity.class);
            startActivity(addBookActivityIntent);
        }
    };

    private View.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nextFragClick(mementoManager.getCurrentFragment(), searchFragment);
        }
    };

    private View.OnClickListener chatClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nextFragClick(mementoManager.getCurrentFragment(), chatFragment);
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
            } else if(btn.getId() == R.id.library_BTN_settings) {
                Intent settingsActivityIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivityForResult(settingsActivityIntent, 1);
//                MainPageActivity.this.finish();
            } else if(btn.getId() == R.id.library_TAB_myBooks) {
                Intent myBooksActivityIntent = new Intent(getApplicationContext(), MyBooksActivity.class);
                startActivityForResult(myBooksActivityIntent, 2);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK && data != null && data.hasExtra("updateProfile")) { // if got result and has update profile - update profile and continute
                profileFragment.updateUserDetails();
            } else if(resultCode == Activity.RESULT_OK && data == null) { // else if got result but no update - logout pressed (finish).
                MainPageActivity.this.finish();
            }
        }
    }

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
                            locationBoundary.setLat(latitude);
                            locationBoundary.setLng(longitude);
                            String locationJson = new Gson().toJson(locationBoundary);
                            prefs.putString(PrefsKeys.LOCATION, locationJson);
                            Log.d("vvv", "locationBoundary1: " +
                                    prefs.getString("Location", ""));
                            Log.d("vvv", "locationBoundary2: " + locationBoundary);
                        }
                    }
                }, Looper.myLooper());
    }

    @Override
    protected void onResume() {
        super.onResume();
        String locationJson = prefs.getString(PrefsKeys.LOCATION, "");
        if(!locationJson.equals("")) {
            myLocation = new Gson().fromJson(locationJson, LocationBoundary.class);
            mapFragment.showAllNearbyBooks(20.0, myLocation);
        }
    }

}


