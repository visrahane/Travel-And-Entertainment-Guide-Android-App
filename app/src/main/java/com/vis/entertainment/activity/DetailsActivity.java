package com.vis.entertainment.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.vis.entertainment.R;
import com.vis.entertainment.adapters.DetailsPagerAdapter;
import com.vis.entertainment.adapters.HomePagerAdapter;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.models.PlaceDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DetailsPagerAdapter detailsPagerAdapter;
    private GeoDataClient geoDataClient;
    private final List<Bitmap> photoBitmapList = new ArrayList<>();
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        String placeData = intent.getStringExtra(ApplicationConstants.PLACE_DATA);
        Gson gson = new Gson();
        PlaceDetails place = gson.fromJson(placeData, PlaceDetails.class);
        requestQueue = Volley.newRequestQueue(this);
        getSupportActionBar().setTitle((place.getName()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getPhotos(place);
        getPlaceReviews(place);
        setUpTabs(place);

    }

    private void getPlaceReviews(PlaceDetails place) {
        {
            //make a call to the server and fetch result
            String url = getResources().getString(R.string.placeDetailsUri);
            Uri builtUri = Uri.parse(url)
                    .buildUpon()
                    .appendQueryParameter("placeId", place.getPlaceId())
                    .build();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, builtUri.toString(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("", "Place Details response is: " + response);
                            JSONObject resultJson = getResultJson(response);
                            detailsPagerAdapter.propagePlaceDetails(resultJson);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //error Message
                    Log.d("", "Place Details response is: " + error);
                }
            });

// Add the request to the RequestQueue.
            requestQueue.add(stringRequest);
        }
    }

    private JSONObject getResultJson(String response) {
        JSONObject detailsJson = null;
        try {
            detailsJson = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detailsJson.optJSONObject("result");
    }

    private void getPhotos(PlaceDetails place) {
        try {
            geoDataClient = Places.getGeoDataClient(this);
            final Task<PlacePhotoMetadataResponse> photoMetadataResponse = geoDataClient.getPlacePhotos(place.getPlaceId());
            photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                    // Get the list of photos.
                    PlacePhotoMetadataResponse photos = task.getResult();
                    // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                    PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                    // Get the first photo in the list.
                    for (PlacePhotoMetadata photoMetadata : photoMetadataBuffer) {
                        // Get the attribution text.
                        //CharSequence attribution = photoMetadata.getAttributions();
                        // Get a full-size bitmap for the photo.
                        Task<PlacePhotoResponse> photoResponse = geoDataClient.getPhoto(photoMetadata);
                        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                PlacePhotoResponse photo = task.getResult();
                                photoBitmapList.add(photo.getBitmap());
                                detailsPagerAdapter.updatePhotoList(photoBitmapList);
                            }
                        });
                    }

                }
            });
        } catch (Exception ex) {
            Log.e("", "getPhotos: exception", ex);
        }
    }

    private void setUpTabs(PlaceDetails place) {
        viewPager = (ViewPager) findViewById(R.id.detailsPager);
        detailsPagerAdapter = new DetailsPagerAdapter(getSupportFragmentManager(), this, place);
        viewPager.setAdapter(detailsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.detailsTabs);
        tabLayout.setupWithViewPager(viewPager);
        final int[] ICONS = new int[]{
                R.drawable.info_outline,
                R.drawable.photos,
                R.drawable.maps,
                R.drawable.review
        };
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(ICONS[i]);
        }
    }


}
