package com.dts.bookies.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dts.bookies.R;
import com.dts.bookies.adapters.recycler.GoogleBooksAdapter;
import com.dts.bookies.adapters.recycler.MyBooksAdapter;
import com.dts.bookies.booksAPI.entities.Book;
import com.dts.bookies.booksAPI.entities.Result;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.OperationBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.User;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyBooksActivity extends AppCompatActivity {

    private RecyclerView myBooks_RCL_myBooksRecycler;
    private MyBooksAdapter myBooksAdapter;

    private MySharedPreferences prefs;
    private Book[] allMyBooks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        prefs = new MySharedPreferences(this);
        String myBooksJson = prefs.getString(PrefsKeys.MY_BOOKS, "");
        allMyBooks = new Gson().fromJson(myBooksJson, Book[].class);

        Log.d("ccc", "my books on create");
        if(!myBooksJson.equals("")) {
            Log.d("ccc", myBooksJson);
            if(allMyBooks.length > 0) {
                Log.d("ccc", "in recycler if");
                createMyBooksRecycler(Arrays.stream(allMyBooks).collect(Collectors.toList()));
            }
        }

    }

    private void findViews() {

    }

    private void createMyBooksRecycler(List<Book> results) {
        myBooks_RCL_myBooksRecycler = findViewById(R.id.myBooks_RCL_myBooksRecycler);
        myBooks_RCL_myBooksRecycler.setLayoutManager(new LinearLayoutManager(this));
        myBooksAdapter = new MyBooksAdapter(this, results);
        myBooksAdapter.setClickListener(myBookAdapterClickListener);
        myBooks_RCL_myBooksRecycler.setAdapter(myBooksAdapter);
    }

    private MyBooksAdapter.ItemClickListener myBookAdapterClickListener = new MyBooksAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            // TODO: somehow let user decide if he offer this book or not.
        }
    };

    public void updateBookAdapter() {
        myBooksAdapter.notifyDataSetChanged();
    }

}