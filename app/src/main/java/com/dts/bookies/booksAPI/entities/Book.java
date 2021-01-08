package com.dts.bookies.booksAPI.entities;

import android.net.Uri;

import java.util.List;

public class Book {

    private String title;
    private List<String> authors;
    private String publishedDate;
    private int pageCount;
    private List<String> categories;
    private ImagesUri imageLinks;
    private String language;
    private String infoLink;

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public List<String> getCategories() {
        return categories;
    }

    public ImagesUri getImageLinks() {
        return imageLinks;
    }

    public String getLanguage() {
        return language;
    }

    public String getInfoLink() {
        return infoLink;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", publishedDate=" + publishedDate +
                ", pageCount=" + pageCount +
                ", categories=" + categories +
                ", imageLinks=" + imageLinks +
                ", language='" + language + '\'' +
                ", infoLink='" + infoLink + '\'' +
                '}';
    }
}
