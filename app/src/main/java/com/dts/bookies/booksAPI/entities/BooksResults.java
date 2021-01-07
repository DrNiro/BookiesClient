package com.dts.bookies.booksAPI.entities;

import java.util.List;

public class BooksResults {

    int totalItems;
    List<Result> items;

    public int getTotalItems() {
        return totalItems;
    }

    public List<Result> getItems() {
        return items;
    }

}
