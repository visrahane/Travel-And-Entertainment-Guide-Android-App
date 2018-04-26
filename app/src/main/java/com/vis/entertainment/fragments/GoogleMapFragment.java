package com.vis.entertainment.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.vis.entertainment.R;
import com.vis.entertainment.adapters.PlacePredictionAdapter;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.models.PlaceDetails;
import com.vis.entertainment.models.Result;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleMapFragment extends BaseFragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private PlaceDetails place;
    private AutoCompleteTextView locationTxt;
    private Polyline polyline;

    private static final int COLOR_BLUE_ARGB = 0xFF2196F3;
    private Spinner travelModeSpinner;
    private Marker destinationMarker, startMarker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps, container, false);
        Gson gson = new Gson();
        place = gson.fromJson(getArguments().getString(ApplicationConstants.PLACE_DATA), PlaceDetails.class);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        init(view);
        return view;
    }

    private void init(View view) {
        PlacePredictionAdapter adapter = new PlacePredictionAdapter(getContext());
        locationTxt = view.findViewById(R.id.fromLocationAutoComplete);
        locationTxt.setAdapter(adapter);
        locationTxt.setOnItemClickListener(onItemClickListener);
        travelModeSpinner = view.findViewById(R.id.travelModeSpinner);
        travelModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!locationTxt.getText().toString().equals("")) {
                    computeDirections();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        this.googleMap = googleMap1;
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng location = place.getLatLng();//new LatLng(-34, 151);
        startMarker = googleMap.addMarker(new MarkerOptions().position(location).title(place.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String address = ((Result) adapterView.getItemAtPosition(i)).getAddress();
                    locationTxt.setText(address);
                    computeDirections();
                }
            };

    private void computeDirections() {
        DirectionsResult directionsResult = getDirections();
        if (directionsResult != null) {
            addMarkersToMap(directionsResult);
            addPolyline(directionsResult);
            adjustCamera();
        } else {
            Toast.makeText(getContext(), "Error getting directions", Toast.LENGTH_LONG);
        }

    }

    private void adjustCamera() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startMarker.getPosition());
        builder.include(destinationMarker.getPosition());
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 15);
        googleMap.moveCamera(cameraUpdate);
    }

    private void addPolyline(DirectionsResult directionsResult) {
        List<LatLng> decodedPath = PolyUtil.decode(directionsResult.routes[0].overviewPolyline.getEncodedPath());
        if (polyline != null) polyline.remove();
        polyline = googleMap.addPolyline(new PolylineOptions().addAll(decodedPath));
        polyline.setColor(COLOR_BLUE_ARGB);
    }

    private void addMarkersToMap(DirectionsResult directionsResult) {
        if (destinationMarker != null) destinationMarker.remove();
        destinationMarker = googleMap.addMarker(new MarkerOptions().
                position(new LatLng(directionsResult.routes[0].legs[0].endLocation.lat, directionsResult.routes[0].legs[0].endLocation.lng))
                .title(locationTxt.getText().toString())
                .snippet(getEndLocationTitle(directionsResult)));

    }

    private String getEndLocationTitle(DirectionsResult directionsResult) {
        return "Time :" + directionsResult.routes[0].legs[0].duration.humanReadable + " Distance :" + directionsResult.routes[0].legs[0].distance.humanReadable;
    }


    private DirectionsResult getDirections() {
        DateTime now = new DateTime();
        DirectionsResult directionsResult = null;
        com.google.maps.model.LatLng latLng = new com.google.maps.model.LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
        try {
            directionsResult = DirectionsApi.newRequest(getGeoContext())
                    .mode(getTravelMode()).origin(latLng)
                    .destination(locationTxt.getText().toString()).departureTime(now)
                    .await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return directionsResult;
    }

    private TravelMode getTravelMode() {
        return TravelMode.valueOf(travelModeSpinner.getSelectedItem().toString().toUpperCase());
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(5)
                .setApiKey(getString(R.string.googleApiKey))
                .setConnectTimeout(30, TimeUnit.SECONDS)
                .setReadTimeout(30, TimeUnit.SECONDS)
                .setWriteTimeout(30, TimeUnit.SECONDS);
    }
}
