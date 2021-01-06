package com.dts.bookies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dts.bookies.R;

public class AddBookActivity extends AppCompatActivity {

//    TODO: implement add book page

    private ImageView addBook_BTN_filter;
    private RelativeLayout addBook_LAY_filterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        findViews();

        addBook_BTN_filter.setOnClickListener(filterButtonClicked);

    }

    private void findViews() {
        addBook_BTN_filter = findViewById(R.id.addBook_BTN_filter);
//        addBook_LAY_filterMenu = findViewById(R.id.addBook_LAY_filterMenu);
    }

    private void toggleFilterMenu() {
        if(addBook_LAY_filterMenu.getVisibility() == View.VISIBLE) {
            addBook_LAY_filterMenu.setVisibility(View.GONE);
        } else {
            addBook_LAY_filterMenu.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener filterButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            toggleFilterMenu();
        }
    };

}