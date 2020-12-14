package com.dts.bookies.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dts.bookies.R;
import com.dts.bookies.StartingActivity;
import com.dts.bookies.activities.fragments.MapFragment;
import com.dts.bookies.activities.fragments.ProfileFragment;
import com.dts.bookies.callbacks.ButtonClickedCallback;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;

public class MainPageActivity extends AppCompatActivity {

//    TODO: create a memento class to remember fragments order entered (remember set - maximum one of each fragment) so user can go back.

    private Fragment currentFragment;

    private ProfileFragment profileFragment;
    private MapFragment mapFragment;
//    TODO: add book option in toolbar leads to a new activity which you create there, not another fragment.

    private ImageView main_BTN_profile;
    private ImageView main_BTN_map;

    private MySharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        prefs = new MySharedPreferences(this);

        findViews();
        initialFragments();
        stageFragments(profileFragment);

        main_BTN_profile.setOnClickListener(profileClickListener);

        main_BTN_map.setOnClickListener(mapClickListener);

    }

    private void findViews() {
        main_BTN_profile = findViewById(R.id.main_BTN_profile);
        main_BTN_map = findViewById(R.id.main_BTN_map);
    }

    private void initialFragments() {
        profileFragment = new ProfileFragment();
        profileFragment.setCallback(buttonClickedCallback);
        mapFragment = new MapFragment();
    }

    private void stageFragments(Fragment firstFocusFrag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_LAY_mainWindow, profileFragment);
        transaction.add(R.id.main_LAY_mainWindow, mapFragment);
        transaction.hide(profileFragment);
        transaction.hide(mapFragment);
        transaction.show(firstFocusFrag);
        currentFragment = firstFocusFrag;
        transaction.commit();
    }

    private void switchFragmentFocus(Fragment offFocusFrag, Fragment newFocusFrag) {
        if(offFocusFrag.equals(newFocusFrag))
            return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(offFocusFrag);
        transaction.show(newFocusFrag);
        currentFragment = newFocusFrag;
        transaction.commit();

        switchToolbarFocus(offFocusFrag, newFocusFrag);
    }

    //    TODO: implement this function when there are more fragments.....
    private void switchToolbarFocus(Fragment offFocusFragment, Fragment newFocusFragment) {
//        newFocusToolbar.setBackgroundResource(R.drawable.toolbar_img_profile);
//        setBackgroundResource(R.color.gray);
//        offFocusToolbar.setBackgroundResource(R.color.design_default_color_background);

//        if(!fragment.isHidden())
//            return;

//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        for(int i = 0; i < allProfileFragments.size(); i++) {
//            if(allProfileFragments.indexOf(fragment) == i) {
//                allToolbarViews.get(i).setImageResource(allToolbarImagesColor.get(i));
//                transaction.show(allProfileFragments.get(i));
//                Log.d("ddd", "i color: " + i);
//            } else if(!allProfileFragments.get(i).isHidden()) {
//                allToolbarViews.get(i).setImageResource(allToolbarImagesGray.get(i));
//                ownersLastPage = allProfileFragments.get(i);
//                transaction.hide(allProfileFragments.get(i));
//                if(allProfileFragments.get(i) == searchFriendsFragment) {
//                    if(searchFriendsFragment.isFinishedSearch()) {
//                        searchFriendsFragment.resetSearch();
//                    }
//                }
//                Log.d("ddd", "i gray: " + i);
//            }
//        }
//        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private View.OnClickListener profileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switchFragmentFocus(currentFragment, profileFragment);
        }
    };

    private View.OnClickListener mapClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switchFragmentFocus(currentFragment, mapFragment);
        }
    };

    private ButtonClickedCallback buttonClickedCallback = new ButtonClickedCallback() {
        @Override
        public void buttonClicked(View btn) {
            if (btn.getId() == R.id.profile_BTN_logout) {
                prefs.putInt(PrefsKeys.LOGGED_STATE, 0);
                Intent startingActivityIntent = new Intent(getApplicationContext(), StartingActivity.class);
                startActivity(startingActivityIntent);
                MainPageActivity.this.finish();
            }
        }
    };

}