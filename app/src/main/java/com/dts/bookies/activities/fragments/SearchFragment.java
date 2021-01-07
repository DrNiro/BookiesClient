package com.dts.bookies.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dts.bookies.R;
import com.dts.bookies.activities.ItemAdapter;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;
import com.google.gson.Gson;

public class SearchFragment extends Fragment {
    private TextView searchItem;
    private Button searchButton;
    private ProgressBar searchProgressBar;
    private View view = null;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] demoList = {"blah blah", "2", "nir","aviad","hadar", "michal", "eyal","ani","shniya",
    "holech", "lashrotim"};
    private ItemBoundary[] itemBoundaryList;
    private MySharedPreferences prefs;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            //this inflates the search fragment to the view group
            view = inflater.inflate(R.layout.fragment_search, container, false);
            findViews();
            prefs = new MySharedPreferences(view.getContext());
            String itemListJson = prefs.getString(PrefsKeys.ITEM_LIST, "");
            itemBoundaryList = new Gson().fromJson(itemListJson, ItemBoundary[].class);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

            //loads the itemlist to the recyclerView
            adapter = new ItemAdapter(itemBoundaryList);
            recyclerView.setAdapter(adapter);


        }

        return view;
    }

    private void findViews() {
        searchItem = view.findViewById(R.id.insert_EDT_BookName);
        searchButton = view.findViewById(R.id.searchFragment_BTN_search);
        searchProgressBar = view.findViewById(R.id.search_ProgressBar);
        recyclerView = view.findViewById(R.id.search_RecyclerView);
        searchProgressBar.setVisibility(View.GONE);
    }
}
