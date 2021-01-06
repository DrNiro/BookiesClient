package com.dts.bookies.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dts.bookies.MapsActivity;
import com.dts.bookies.R;
import com.dts.bookies.StartingActivity;
import com.dts.bookies.activities.fragments.MapFragment;
import com.dts.bookies.activities.fragments.ProfileFragment;
import com.dts.bookies.activities.fragments.SearchFragment;
import com.dts.bookies.callbacks.ButtonClickedCallback;
import com.dts.bookies.util.Functions;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.memento.FragmentsMementoManager;
import com.dts.bookies.util.memento.MementoStates;

import java.util.HashMap;
import java.util.Map;

public class MainPageActivity extends AppCompatActivity {

    private ProfileFragment profileFragment;
    private MapFragment mapFragment;
    private SearchFragment searchFragment;

    private ImageView main_BTN_profile;
    private ImageView main_BTN_map;
    private ImageView main_BTN_search;
    private ImageView main_BTN_addBook;

    private Map<String, ImageView> imageButtonsMap;
    private Map<String, Fragment> fragmentsMap;

    private FragmentsMementoManager mementoManager;
    private MySharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        prefs = new MySharedPreferences(this);

        findViews();
        initFragmentsAndMemento();
        initMaps();
        stageFragments(profileFragment);

        main_BTN_profile.setOnClickListener(profileClickListener);

        main_BTN_map.setOnClickListener(mapClickListener);

        main_BTN_addBook.setOnClickListener(addBookClickListener);

        main_BTN_search.setOnClickListener(searchClickListener);

    }

    private void findViews() {
        main_BTN_profile = findViewById(R.id.main_BTN_profile);
        main_BTN_map = findViewById(R.id.main_BTN_map);
        main_BTN_addBook = findViewById(R.id.main_BTN_addBook);
        main_BTN_search = findViewById(R.id.main_BTN_search);
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

}