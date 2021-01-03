package com.dts.bookies.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dts.bookies.R;


public class AddBookFragment extends Fragment {
    private TextView add_TXT_name;
    private TextView add_TXT_author;
    private TextView add_TET_genre;
    private TextView add_TXT_summery;
    private Button add_BTN_submit;


    private View view = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_add_book, container, false);
        }
        addBookToServer();
        return view;
    }

    private void addBookToServer() {

    }


}
