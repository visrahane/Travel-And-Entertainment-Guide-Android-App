package com.vis.entertainment.adapters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;
import com.vis.entertainment.R;
import com.vis.entertainment.activity.DetailsActivity;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.fragments.BaseFragment;
import com.vis.entertainment.fragments.GoogleMapFragment;
import com.vis.entertainment.fragments.InfoFragment;
import com.vis.entertainment.fragments.PhotosFragment;
import com.vis.entertainment.fragments.ReviewsFragment;
import com.vis.entertainment.models.PlaceDetails;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vis on 13-04-2018.
 */

public class DetailsPagerAdapter extends FragmentPagerAdapter {

    private String tabs[];
    private DetailsActivity mainActivity;
    private  PlaceDetails place;
    private PhotosFragment photoFragment;
    private List<Bitmap> photoList ;
    private List<BaseFragment> fragmentList;

    public DetailsPagerAdapter(FragmentManager fm, DetailsActivity mainActivity, PlaceDetails place) {
        super(fm);
        this.mainActivity=mainActivity;
        Resources res = mainActivity.getResources();
        tabs= res.getStringArray(R.array.detailsTabsArray);
        this.place = place;
        fragmentList=new ArrayList<>();
        fragmentList.add(new InfoFragment());
        fragmentList.add(new PhotosFragment());
        fragmentList.add(new GoogleMapFragment());
        fragmentList.add(new ReviewsFragment());
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        Gson gS = new Gson();
        String data = gS.toJson(place);
        Log.d("getTabItem", "getItem: "+position);
        switch (position){
            case 0:fragment=fragmentList.get(position);
                args.putString(ApplicationConstants.PLACE_DATA, data);
                fragment.setArguments(args);
                break;
            case 1:fragment=fragmentList.get(position);
                args.putString(ApplicationConstants.PLACE_DATA, data);
                fragment.setArguments(args);
            break;
            case 2:fragment=fragmentList.get(position);
                args.putString(ApplicationConstants.PLACE_DATA, data);
                fragment.setArguments(args);break;
            case 3:fragment=fragmentList.get(position);break;
        }

        return fragment;
    }
    @Override
    public CharSequence getPageTitle(int position) {
       return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    public void updatePhotoList(List<Bitmap> photoBitmapList) {
        fragmentList.get(1).prepareList(photoBitmapList);
        //this.photoList=photoBitmapList;
    }

    public void propagePlaceDetails(JSONObject resultJson) {
        for(BaseFragment baseFragment:fragmentList){
            baseFragment.updateInfo(resultJson);
        }
        //fragmentList.get(0).updateInfo(resultJson);
    }
}
