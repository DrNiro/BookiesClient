package com.dts.bookies.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dts.bookies.R;
import com.dts.bookies.booksAPI.entities.Book;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.OperationBoundary;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.LocationBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.User;
import com.dts.bookies.rest.services.ItemService;
import com.dts.bookies.rest.services.OperationService;
import com.dts.bookies.util.Functions;
import com.dts.bookies.util.MySharedPreferences;
import com.dts.bookies.util.PrefsKeys;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View view = null;
    private ItemBoundary[] itemBoundaryList;

    private MySharedPreferences prefs;
    private UserBoundary myUser;
    private LocationBoundary myLocation;
    private double showBooksDistance;

    private MapView mapView;
    private GoogleMap mMap;

    private OperationService operationService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        }

        findViews();

        prefs = new MySharedPreferences(view.getContext());
        myUser = Functions.getUserBoundaryFromPrefs(prefs);

        myLocation = new Gson().fromJson
                (prefs.getString(PrefsKeys.LOCATION, ""), LocationBoundary.class);

        operationService = new OperationService();
        operationService.initInvokeCallback(findNearbyBooksCallback);

        return view;
    }

    private void findViews() {
        mapView = view.findViewById(R.id.map_LAY_googleMapsHolder);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }


    public void initialMap() {
        String itemListJson = prefs.getString(PrefsKeys.ITEM_LIST, "");
        itemBoundaryList = new Gson().fromJson(itemListJson, ItemBoundary[].class);

        // iterate over item list
        for (ItemBoundary item : itemBoundaryList) {
            LatLng itemLtLg = new LatLng(item.getLocation().getLat(), item.getLocation().getLng());
            String title = item.getName() + "," + (String) item.getItemAttributes().get("owner");
            // add item location marker
            mMap.addMarker(new MarkerOptions().position(itemLtLg).title(title));
        }


    }

    public void showAllNearbyBooks(double distance, LocationBoundary myLocation) {

        OperationBoundary findNearbyBooksOperation = new OperationBoundary();
        findNearbyBooksOperation.setType("findBooksInDistance");
        findNearbyBooksOperation.setInvokedBy(new User(myUser.getUserId()));
        Map<String, Object> operationAttributes = new HashMap<>();
        operationAttributes.put("distance", distance);
        operationAttributes.put("myLocation", myLocation);
        operationAttributes.put("pageSize", 20);
        operationAttributes.put("pageOffset", 0);
        findNearbyBooksOperation.setOperationAttributes(operationAttributes);

        operationService.invokeOperation(findNearbyBooksOperation);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        if (myLocation != null) {
            LatLng myLatLan = new LatLng(myLocation.getLat(), myLocation.getLng());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLan, 7));
            showAllNearbyBooks(showBooksDistance, myLocation);
        } else {
            LatLng israel = new LatLng(31.4117257, 35.0818155);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(israel, 7));
        }


        if(itemBoundaryList != null && itemBoundaryList.length > 0) {
            initialMap();
        }

    }

    private Callback<Object> findNearbyBooksCallback = new Callback<Object>() {
        @Override
        public void onResponse(Call<Object> call, Response<Object> response) {
            if (!response.isSuccessful()) {
                Log.d("bbb", response.code() + ": " + response.message());
                return;
            }

            Log.d("bbb", "response: " + response.body().toString());
            Log.d("bbb", "type of response: " + response.body().getClass().toString());
            ArrayList<ItemBoundary> nearbyBooks = (ArrayList<ItemBoundary>) response.body();

            if(nearbyBooks != null && nearbyBooks.size() != 0) {
                for(int i = 0; i < nearbyBooks.size(); i++) {
                    ItemBoundary ib = new Gson().fromJson(new Gson().toJsonTree(nearbyBooks.get(i)).getAsJsonObject(), ItemBoundary.class);
                    Log.d("bbb","book: " + ib);
                    LatLng bookLocation = new LatLng(ib.getLocation().getLat(), ib.getLocation().getLng());
                    Book book = (Book) new Gson().fromJson(new Gson().toJsonTree(ib.getItemAttributes().get("googleBook")).getAsJsonObject(), Book.class);
                    mMap.addMarker(new MarkerOptions().position(bookLocation).title(book.getTitle()));
                }
            } else {
                Log.d("bbb", "null/empty response..");
            }
        }

        @Override
        public void onFailure(Call<Object> call, Throwable t) {

        }
    };

}