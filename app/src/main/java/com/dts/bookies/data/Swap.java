package com.dts.bookies.data;

import com.dts.bookies.logic.boundaries.subboundaries.ItemIdBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.LocationBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.UserIdBoundary;

public class Swap {

    private ItemIdBoundary bookId;
    private UserIdBoundary swapFrom;
    private UserIdBoundary swapTo;
    private LocationBoundary currentLocation;

    public Swap() {

    }

    public Swap(ItemIdBoundary bookId, UserIdBoundary swapFrom, UserIdBoundary swapTo, LocationBoundary currentLocation) {
        this.bookId = bookId;
        this.swapFrom = swapFrom;
        this.swapTo = swapTo;
        this.currentLocation = currentLocation;
    }

    public ItemIdBoundary getBookId() {
        return bookId;
    }

    public void setBookId(ItemIdBoundary bookId) {
        this.bookId = bookId;
    }

    public UserIdBoundary getSwapFrom() {
        return swapFrom;
    }

    public void setSwapFrom(UserIdBoundary swapFrom) {
        this.swapFrom = swapFrom;
    }

    public UserIdBoundary getSwapTo() {
        return swapTo;
    }

    public void setSwapTo(UserIdBoundary swapTo) {
        this.swapTo = swapTo;
    }

    public LocationBoundary getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LocationBoundary currentLocation) {
        this.currentLocation = currentLocation;
    }
}
