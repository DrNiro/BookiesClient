package com.dts.bookies.booksAPI.entities;

import android.net.Uri;

import java.util.List;

public class ImagesUri {

    private String smallThumbnail;
    private String thumbnail;

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public String toString() {
        return "ImagesUri{" +
                "smallThumbnail='" + smallThumbnail + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
