package com.dts.bookies.booksAPI.rest;

import android.os.AsyncTask;
import android.util.Log;

import com.dts.bookies.booksAPI.entities.BooksResults;
import com.dts.bookies.util.Constants;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BooksAPIService /*extends AsyncTask<Void, Void, Void>*/ {

    private Retrofit retrofit;
    private BooksAPIController booksAPIController;

    private Callback<BooksResults> getBooksCallback;

    public BooksAPIService() {
        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_BOOKS_API_URL)
                .build();
        this.booksAPIController = retrofit.create(BooksAPIController.class);
    }
/*
    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
*/
    public BooksAPIService(Retrofit retrofit) {
        setRetrofit(retrofit);
        this.booksAPIController = retrofit.create(BooksAPIController.class);
    }

    public void initGetBooksCallback(Callback<BooksResults> getBooksCallback) {
        this.getBooksCallback = getBooksCallback;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public void getBooksWithApi(String q, String key) {
        if(this.getBooksCallback == null) {
            Log.d("vvv", "need to initialize callback first");
            return;
        }

        Call<BooksResults> call = this.booksAPIController.getBooks(q, Constants.BOOK_API_MAX_SIZE, key);

        call.enqueue(this.getBooksCallback);

    }

    public void getBooksSpecificUri() {
        if(this.getBooksCallback == null) {
            Log.d("vvv", "need to initialize callback first");
            return;
        }

        Call<BooksResults> call = this.booksAPIController.getBooksSpecificQ();

        call.enqueue(this.getBooksCallback);
    }

}
