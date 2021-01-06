package com.dts.bookies.activities.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dts.bookies.R;
import com.dts.bookies.activities.LoginActivity;
import com.dts.bookies.activities.MainPageActivity;
import com.dts.bookies.callbacks.ButtonClickedCallback;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.util.Functions;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;

public class ProfileFragment extends Fragment {

    private TextView profile_TXT_username;
    private Button profile_BTN_logout;

    private View view = null;

    private ButtonClickedCallback buttonClickedCallback;

    public void setCallback(ButtonClickedCallback buttonClickedCallback) {
        this.buttonClickedCallback = buttonClickedCallback;
    }

    private MySharedPreferences prefs;
    private UserBoundary myUser;
    private String myUserJson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_profile, container, false);
        }

        findViews();

        prefs = new MySharedPreferences(view.getContext());

        myUser = Functions.getUserBoundaryFromPrefs(prefs);

        initProfile();

        profile_BTN_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.putInt(PrefsKeys.LOGGED_STATE, 0);
                buttonClickedCallback.buttonClicked(view);
            }
        });


        return view;
    }

    private void findViews() {
        profile_TXT_username = view.findViewById(R.id.profile_TXT_username);
        profile_BTN_logout = view.findViewById(R.id.profile_BTN_logout);
    }

    private void initProfile() {
        profile_TXT_username.setText(myUser.getUsername());


    }

}