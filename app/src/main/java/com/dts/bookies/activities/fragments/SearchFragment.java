package com.dts.bookies.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dts.bookies.R;
import com.dts.bookies.adapters.recycler.ItemAdapter;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.rest.services.ItemService;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private UserBoundary myUser;
    private ItemService itemService;



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
        }

        findViews();
        prefs = new MySharedPreferences(view.getContext());

    //    myUser = new UserBoundary();
        itemService = new ItemService();
        getUserFromPrefs();
        String itemListJson = prefs.getString(PrefsKeys.ITEM_LIST, "");
        itemBoundaryList = new Gson().fromJson(itemListJson, ItemBoundary[].class);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //loads the itemlist to the recyclerView
        //adapter = new ItemAdapter(itemBoundaryList);
        //recyclerView.setAdapter(adapter);

        Log.d("vvv122", myUser.getUserId().getSpace());
        itemService.initSearchItemsByNameCallBack(searchBookItemByNameCallBack);
        Log.d("vvv123", myUser.getUserId().getSpace());
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProgressBar.setVisibility(View.VISIBLE);
                String search = searchItem.getText().toString();
           //     Log.d("vvv",search);
              //  Log.d("vvv124", myUser.getUserId().getSpace());
                itemService.searchItemsByName(myUser.getUserId().getSpace(),
                        myUser.getUserId().getEmail(),search
                        );
            }
        });

        return view;
    }

    private void findViews() {
        searchItem = view.findViewById(R.id.insert_EDT_BookName);
        searchButton = view.findViewById(R.id.searchFragment_BTN_search);
        searchProgressBar = view.findViewById(R.id.search_ProgressBar);
        recyclerView = view.findViewById(R.id.search_RecyclerView);
        searchProgressBar.setVisibility(View.GONE);
    }

    private void getUserFromPrefs() {
        String userJson = prefs.getString(PrefsKeys.USER_BOUNDARY, "");
        if (!userJson.equals("")) {
            myUser = new Gson().fromJson(userJson, UserBoundary.class);

            Log.d("vvv121", myUser.getUserId().getSpace());

        } else {
            Log.d("vvv", "user not found in preferences");
        }
    }

    private Callback<ItemBoundary[]> searchBookItemByNameCallBack = new Callback<ItemBoundary[]>() {
        @Override
        public void onResponse(Call<ItemBoundary[]> call, Response<ItemBoundary[]> response) {
            if(!response.isSuccessful()) {
                if(response.code() == 404) {
                    Log.d("vvv", "404: user not found");
                }
                Log.d("vvv", response.code() + ": " + response.message());
                return;
            }
           // Log.d("vvv156",   "" + itemBoundaryList.length);
            itemBoundaryList = response.body();
            Log.d("vvv157",   "" + itemBoundaryList.length);
            adapter = new ItemAdapter(itemBoundaryList);
            recyclerView.setAdapter(adapter);
            searchProgressBar.setVisibility(View.GONE);

        }

        @Override
        public void onFailure(Call<ItemBoundary[]> call, Throwable t) {
            Log.d("vvv", "failure login, message: " + t.getMessage());
        }
    };

}
