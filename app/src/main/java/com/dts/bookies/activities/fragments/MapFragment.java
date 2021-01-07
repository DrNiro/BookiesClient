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
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.LocationBoundary;
import com.dts.bookies.rest.services.ItemService;
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

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View view = null;
    private ItemBoundary[] itemBoundaryList;
    private MySharedPreferences prefs;
    private LocationBoundary myLocation;

    private MapView mapView;
    private GoogleMap mMap;

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
        myLocation = new Gson().fromJson
                (prefs.getString(PrefsKeys.LOCATION, ""), LocationBoundary.class);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        if (myLocation != null) {
            LatLng myLatLan = new LatLng(myLocation.getLat(), myLocation.getLng());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLan, 7));
        } else {
            LatLng israel = new LatLng(31.4117257, 35.0818155);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(israel, 7));
        }
        if(itemBoundaryList != null && itemBoundaryList.length > 0) {
            initialMap();
        }

    }
}