package com.dts.bookies.booksAPI.entities;

import java.util.List;

public class Book {

    private String title;
    private List<String> authors;

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

}