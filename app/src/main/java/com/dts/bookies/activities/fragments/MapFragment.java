package com.dts.bookies.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dts.bookies.R;

public class MapFragment extends Fragment {

    private View view = null;

//    private CallBackClickedBtn callBackClickedBtn;

//    public void setCallback(CallBackClickedBtn callback) {
//        this.callBackClickedBtn = callback;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        }

        findViews();

        return view;
    }

    private void findViews() {
//        profile_BTN_addBook = view.findViewById(R.id.profile_BTN_addBook);
    }

}
