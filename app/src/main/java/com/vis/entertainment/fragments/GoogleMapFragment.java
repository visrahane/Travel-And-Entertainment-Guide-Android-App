package com.vis.entertainment.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.vis.entertainment.R;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.models.PlaceDetails;

public class GoogleMapFragment extends BaseFragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private PlaceDetails place;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps, container, false);
        Gson gson=new Gson();
        place = gson.fromJson(getArguments().getString(ApplicationConstants.PLACE_DATA),PlaceDetails.class);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        this.googleMap = googleMap1;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng location = place.getLatLng();//new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(location).title(place.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

    }
}
