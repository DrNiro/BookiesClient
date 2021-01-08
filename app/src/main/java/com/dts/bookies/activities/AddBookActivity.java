package com.dts.bookies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dts.bookies.R;
import com.dts.bookies.adapters.recycler.GoogleBooksAdapter;
import com.dts.bookies.booksAPI.entities.BooksResults;
import com.dts.bookies.booksAPI.entities.Result;
import com.dts.bookies.booksAPI.rest.BooksAPIService;
import com.dts.bookies.booksAPI.queries.QueryGenerator;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.OperationBoundary;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.LocationBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.User;
import com.dts.bookies.rest.services.OperationService;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookActivity extends AppCompatActivity {
    private OperationService operationService;
    private BooksAPIService booksAPIService;

    private UserBoundary myUser;
    private LocationBoundary myLocation;

    private MySharedPreferences prefs;

    private RecyclerView recyclerView;
    private GoogleBooksAdapter googleBooksAdapter;

    private ImageView addBook_BTN_filter;
    private EditText add_EDT_title;
    private EditText add_EDT_author;
    private EditText add_EDT_publisher;
    private EditText add_EDT_subject;
    private EditText add_EDT_isbn;
    private EditText add_EDT_lccn;
    private EditText add_EDT_oclc;
    private Button add_BTN_submit;
    private RelativeLayout add_LAY_advOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_with_api);

        findViews();

        prefs = new MySharedPreferences(this);
        setUserFromPrefs();

        operationService = new OperationService();
        operationService.initInvokeCallback(invokeCreatenewItemCallback);
        booksAPIService = new BooksAPIService();
        booksAPIService.initGetBooksCallback(getBooksWithApiCallback);

        addBook_BTN_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(add_LAY_advOptions.getVisibility() == View.VISIBLE) {
                    add_LAY_advOptions.setVisibility(View.GONE);
                } else {
                    add_LAY_advOptions.setVisibility(View.VISIBLE);
                }
            }
        });

        myLocation = new Gson().fromJson(prefs.getString(PrefsKeys.LOCATION, ""), LocationBoundary.class);
        add_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = add_EDT_title.getText().toString();
                if(title.trim().equals("")) {
                    add_EDT_title.setError("must enter title");
                    return;
                }

                String author = add_EDT_author.getText().toString();
                String publisher = add_EDT_publisher.getText().toString();
                String subject = add_EDT_subject.getText().toString();
                String isbn = add_EDT_isbn.getText().toString();
                String lccn = add_EDT_lccn.getText().toString();
                String oclc = add_EDT_oclc.getText().toString();

                QueryGenerator queryGenerator = new QueryGenerator();
                queryGenerator.setIntitle(title);
                queryGenerator.setInauthor(author);
                queryGenerator.setInpublisher(publisher);
                queryGenerator.setSubject(subject);
                queryGenerator.setIsbn(isbn);
                queryGenerator.setLccn(lccn);
                queryGenerator.setOclc(oclc);

                String q = queryGenerator.genQ();
                Log.d("aaa", q);
                // call api function.
                booksAPIService.getBooksWithApi(q, "AIzaSyBZF6LHJsVR6sSmhXMQUKC7ZS0xlFqtkgQ");
            }
        });

    }

    private void setUserFromPrefs() {
        String userJson = prefs.getString(PrefsKeys.USER_BOUNDARY, "");
        if (!userJson.equals("")) {
            myUser = new Gson().fromJson(userJson, UserBoundary.class);
        } else {
            Log.d("vvv", "user not found in preferences");
        }
    }

    private void findViews() {
        add_LAY_advOptions = findViewById(R.id.add_LAY_advOptions);
        addBook_BTN_filter = findViewById(R.id.addBook_BTN_filter);
        add_EDT_title = findViewById(R.id.add_EDT_title);
        add_EDT_author = findViewById(R.id.add_EDT_author);
        add_EDT_publisher = findViewById(R.id.add_EDT_publisher);
        add_EDT_subject = findViewById(R.id.add_EDT_subject);
        add_EDT_isbn = findViewById(R.id.add_EDT_isbn);
        add_EDT_lccn = findViewById(R.id.add_EDT_lccn);
        add_EDT_oclc = findViewById(R.id.add_EDT_oclc);
        add_BTN_submit = findViewById(R.id.add_BTN_submit);

//        recyclerView = findViewById(R.id.search_RecyclerView);
    }

    private Callback<BooksResults> getBooksWithApiCallback = new Callback<BooksResults>() {
        @Override
        public void onResponse(Call<BooksResults> call, Response<BooksResults> response) {
            if (!response.isSuccessful()) {
                Log.d("aaa", response.code() + ": " + response.message());
                return;
            }

            BooksResults booksResults = response.body();
            final List<Result> results = booksResults.getItems();
            Log.d("aaa", "success: " + results.size() + " books.");


            if (booksResults == null || booksResults.getTotalItems() < 1) {
                Log.d("aaa", "book not found");
//                throw new BookNotFoundException("Book not found");
            }

            if (results == null || results.size() < 1) {
                Log.d("aaa", "invalid items list");
//                throw new BookNotFoundException("Invalid items list");
            }

            Log.d("aaa", results.get(0).getBook().getTitle());
            Log.d("aaa", results.get(0).getBook().toString());
            // show results in recyclerView.
            createBookResultsRecycler(results);

        }

        @Override
        public void onFailure(Call<BooksResults> call, Throwable t) {
            Log.d("aaa", t.getMessage() + " (failed).");
        }
    };

    private Callback<Object> invokeCreatenewItemCallback = new Callback<Object>() {
        @Override
        public void onResponse(Call<Object> call, Response<Object> response) {
            if (!response.isSuccessful()) {
                Log.d("vvv", response.code() + ": " + response.message());
                return;
            }

            Log.d("aaa", "in create new item, received book.");

//            successful, create user.
            ItemBoundary newItem = new Gson().fromJson(new Gson().toJsonTree(response.body()).getAsJsonObject(), ItemBoundary.class);

            Log.d("aaa", "item: " + newItem.toString());

//            move to profile/main page.
            Intent mainPageActivityIntent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivity(mainPageActivityIntent);
            AddBookActivity.this.finish();

//            pop-up account created successfully.
            Toast.makeText(AddBookActivity.this, "Book created", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<Object> call, Throwable t) {
            Log.d("vvv", "FAILED " + t.getMessage());
        }
    };

    private void createBookResultsRecycler(List<Result> results) {
        recyclerView = findViewById(R.id.add_RCL_bookResultsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        googleBooksAdapter = new GoogleBooksAdapter(this, results);
        googleBooksAdapter.setClickListener(myBookResultsAdapterClickListener);
        recyclerView.setAdapter(googleBooksAdapter);
    }

    private GoogleBooksAdapter.ItemClickListener myBookResultsAdapterClickListener = new GoogleBooksAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Result bookResult = googleBooksAdapter.getItem(position);
            Log.d("aaa", "book: " + bookResult.getBook().getTitle());

            // TODO: ask add item popup.
            OperationBoundary operationBoundary = new OperationBoundary();
            Map<String, Object> itemAttributes = new HashMap<>();
            Map<String, Object> operationsAttributes = new HashMap<String, Object>();

            operationBoundary.setType("createNewBook");
            itemAttributes.put("googleBook", bookResult.getBook());
            operationsAttributes.put("owner", myUser.getUsername()); //TODO might need to change into UserIdBoundary in addition to username (for linking purposes).
            operationsAttributes.put("bookName", bookResult.getBook().getTitle());
            operationsAttributes.put("bookLocation", myLocation);
            operationsAttributes.put("bookAttributes", itemAttributes);

            operationBoundary.setInvokedBy(new User(myUser.getUserId()));
            operationBoundary.setOperationAttributes(operationsAttributes);

            operationService.invokeOperation(operationBoundary);
        }
    };

    public void updateBookAdapter() {
        googleBooksAdapter.notifyDataSetChanged();
    }

}