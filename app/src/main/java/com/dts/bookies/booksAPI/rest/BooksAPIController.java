package com.dts.bookies.booksAPI.rest;

import com.dts.bookies.booksAPI.entities.BooksResults;
import com.dts.bookies.logic.boundaries.ItemBoundary;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BooksAPIController {

//    TODO: max results: 20.

    @GET("books/v1/volumes")
    public Call<BooksResults> getBooks(@Query("q") String q,
                                       @Query("key") String keyAPI);


    @GET("https://www.googleapis.com/books/v1/volumes?q=intitle:flowers&key=AIzaSyALjVlnSfXActTHq8_oeBVoiO2fFhvDN-A")
    public Call<BooksResults> getBooksSpecificQ();

}
