package com.vis.entertainment.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;


public class DetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DetailsPagerAdapter detailsPagerAdapter;
    private GeoDataClient geoDataClient;
    private final List<Bitmap> photoBitmapList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        String placeData = intent.getStringExtra(ApplicationConstants.PLACE_DATA);
        Gson gson = new Gson();
        PlaceDetails place=gson.fromJson(placeData, PlaceDetails.class);
        //PlaceDetails place= (PlaceDetails) intent.getSerializableExtra(ApplicationConstants.PLACE_DATA);

        getPhotos(place);
        setUpTabs(place);

    }
    private void getPhotos(PlaceDetails place) {

        geoDataClient= Places.getGeoDataClient(this);
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = geoDataClient.getPlacePhotos(place.getPlaceId());
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                for(PlacePhotoMetadata photoMetadata:photoMetadataBuffer){
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
    }

    private void setUpTabs(PlaceDetails place) {
        viewPager = (ViewPager) findViewById(R.id.detailsPager);
        detailsPagerAdapter = new DetailsPagerAdapter(getSupportFragmentManager(),this,place);
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
