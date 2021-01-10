package com.dts.bookies.activities.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dts.bookies.R;
import com.dts.bookies.callbacks.ButtonClickedCallback;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.util.Functions;
import com.dts.bookies.util.MySharedPreferences;

public class ProfileFragment extends Fragment {

    private ImageView library_BTN_settings;
    private TextView library_TXT_avatar;
    private TextView library_TXT_username;

    private TextView library_TAB_myBooks;
    private TextView library_TAB_lookingFor;
    private TextView library_TAB_favourites;

    private RecyclerView library_RCL_recentBookSwapsRecycler;

    private View view = null;

    private MySharedPreferences prefs;
    private UserBoundary myUser;

    private ButtonClickedCallback buttonClickedCallback;

    public void setCallback(ButtonClickedCallback buttonClickedCallback) {
        this.buttonClickedCallback = buttonClickedCallback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_profile_private_version, container, false);
        }

        findViews();

        prefs = new MySharedPreferences(view.getContext());
        myUser = Functions.getUserBoundaryFromPrefs(prefs);

        initUserDetails();

        library_TAB_myBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickedCallback.buttonClicked(view);
            }
        });

        library_BTN_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickedCallback.buttonClicked(view);
            }
        });

        return view;
    }

    private void findViews() {
        library_BTN_settings = view.findViewById(R.id.library_BTN_settings);
        library_TAB_myBooks = view.findViewById(R.id.library_TAB_myBooks);
        library_TAB_lookingFor = view.findViewById(R.id.library_TAB_lookingFor);
        library_TAB_favourites = view.findViewById(R.id.library_TAB_favourites);
        library_TXT_avatar = view.findViewById(R.id.library_TXT_avatar);
        library_TXT_username = view.findViewById(R.id.library_TXT_username);
    }

    private void initUserDetails() {
        library_TXT_username.setText(myUser.getUsername());
        library_TXT_avatar.setText(myUser.getAvatar());
    }

    public void updateUserDetails() {
        myUser = Functions.getUserBoundaryFromPrefs(prefs);
        library_TXT_username.setText(myUser.getUsername());
        library_TXT_avatar.setText(myUser.getAvatar());
    }



}