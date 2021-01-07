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

import com.dts.bookies.R;
import com.dts.bookies.booksAPI.entities.Book;
import com.dts.bookies.booksAPI.entities.BooksResults;
import com.dts.bookies.booksAPI.entities.Result;
import com.dts.bookies.booksAPI.exceptions.BookNotFoundException;
import com.dts.bookies.booksAPI.rest.BooksAPIService;
import com.dts.bookies.booksAPI.queries.QueryGenerator;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.LocationBoundary;
import com.dts.bookies.rest.services.OperationService;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookActivity extends AppCompatActivity {
    private OperationService operationService;
    private BooksAPIService booksAPIService;
    boolean booksApiRunning = true;

    private UserBoundary myUser;
    private LocationBoundary myLocation;

    private MySharedPreferences prefs;

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

//                booksAPIService.getBooksSpecificUri();
//                while(booksApiRunning) { /*do nothing*/ }
                booksApiRunning = true;

                Log.d("aaa", "after book-api call");
//                OperationBoundary operationBoundary = new OperationBoundary();
//                Map<String, Object> itemAttributes = new HashMap<String, Object>();
//                Map<String, Object> operationsAttributes = new HashMap<String, Object>();

//                operationBoundary.setType("createNewBook");
//                itemAttributes.put("title", title);
//                itemAttributes.put("author", author);
//                itemAttributes.put("publisher", publisher);
//                itemAttributes.put("subject", subject);
//                itemAttributes.put("isbn", isbn);
//                itemAttributes.put("lccn", lccn);
//                itemAttributes.put("oclc", oclc);
//                operationsAttributes.put("owner", myUser.getUsername());
//                operationsAttributes.put("bookName", title);
//                operationsAttributes.put("bookLocation", myLocation);
//                operationsAttributes.put("bookAttributes", itemAttributes);
//
//                operationBoundary.setInvokedBy(new User(myUser.getUserId()));
//                operationBoundary.setOperationAttributes(operationsAttributes);

//                operationService.invokeOperation(operationBoundary);
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
    }

    private Callback<Object> invokeCreatenewItemCallback = new Callback<Object>() {
        @Override
        public void onResponse(Call<Object> call, Response<Object> response) {
            if (!response.isSuccessful()) {
                Log.d("vvv", response.code() + ": " + response.message());
                return;
            }

            Log.d("aaa", "in create new item.");

//            TODO: make this shit work async
//            while(!googleBooksResponseReceived) {
//                Log.d("aaa", "not received, waiting...");
//                wait(1000);
//            }

            Log.d("aaa", "received book.");

//            successful, create user.
            ItemBoundary newItem = new Gson().fromJson(new Gson().toJsonTree(response.body()).getAsJsonObject(), ItemBoundary.class);

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

    private Callback<BooksResults> getBooksWithApiCallback = new Callback<BooksResults>() {
        @Override
        public void onResponse(Call<BooksResults> call, Response<BooksResults> response) {
            if (!response.isSuccessful()) {
                Log.d("aaa", response.code() + ": " + response.message());
                booksApiRunning = false;
                return;
            }

            Log.d("aaa", "success" + response.body().getItems().size());

            BooksResults booksResults = response.body();

            if (booksResults == null || booksResults.getTotalItems() < 1) {
                throw new BookNotFoundException("Book not found");
            }

            final List<Result> results = booksResults.getItems();
            if (results == null || results.size() < 1) {
                throw new BookNotFoundException("Invalid items list");
            }
            final Book book = results.get(0).getBook();

            booksApiRunning = false;
        }

        @Override
        public void onFailure(Call<BooksResults> call, Throwable t) {
            booksApiRunning = false;
            Log.d("aaa", t.getMessage() + " (failed).");
        }
    };

}