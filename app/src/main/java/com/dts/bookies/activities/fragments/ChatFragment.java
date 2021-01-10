package com.dts.bookies.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dts.bookies.R;
import com.dts.bookies.callbacks.ButtonClickedCallback;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.rest.services.ItemService;
import com.dts.bookies.util.Functions;
import com.dts.bookies.util.MySharedPreferences;

public class ChatFragment extends Fragment {

    private View view = null;

    private MySharedPreferences prefs;
    private UserBoundary myUser;

    private ButtonClickedCallback buttonClickedCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_chat, container, false);
        }

        findViews();

        prefs = new MySharedPreferences(view.getContext());
        myUser = Functions.getUserBoundaryFromPrefs(prefs);

        return view;
    }

    private void findViews() {

    }

}
