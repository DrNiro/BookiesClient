package com.dts.bookies.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dts.bookies.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    private View view = null;

//    private CallBackClickedBtn callBackClickedBtn;

//    public void setCallback(CallBackClickedBtn callback) {
//        this.callBackClickedBtn = callback;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        }

        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
        getChildFragmentManager().findFragmentById(R.id.google_map);



        //Async Map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng israel = new LatLng(31.4117257, 35.0818155);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(israel, 7));
                //when map loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //when clicked on map
                        //Initialize marker options
                        MarkerOptions markerOptions = new MarkerOptions();

                        //set positions of marker
                        markerOptions.position(latLng);

                        //set title of marker
                        markerOptions.title(latLng.latitude + " :  "  + latLng.longitude);

                        //remove all marker
                        googleMap.clear();

                        //Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng, 5
                        ));

                        //add marker on map
                        googleMap.addMarker(markerOptions);





                    }
                });
            }
        });

        findViews();

        return view;
    }

    private void findViews() {
//        profile_BTN_addBook = view.findViewById(R.id.profile_BTN_addBook);
    }

}
