package com.vis.entertainment.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vis.entertainment.R;
import com.vis.entertainment.adapters.PhotoResultsAdapter;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.models.PlaceDetails;

import java.util.ArrayList;
import java.util.List;

public class PhotosFragment extends BaseFragment {
    public static final String NO_PHOTOS = "No Photos";
    private List<Bitmap> photoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PhotoResultsAdapter resultAdapter;
    private PlaceDetails place;
    private TextView emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_page, container, false);
        Gson gson = new Gson();
        place = gson.fromJson(getArguments().getString(ApplicationConstants.PLACE_DATA), PlaceDetails.class);
        recyclerView =  view.findViewById(R.id.photoResultsView);
        emptyView =  view.findViewById(R.id.noPhotoTxt);
        if (photoList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setText(NO_PHOTOS);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        resultAdapter = new PhotoResultsAdapter(photoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(resultAdapter);
        preparePhotoList();
        return view;
    }

    private void preparePhotoList() {
        resultAdapter.notifyDataSetChanged();
    }

    @Override
    public void prepareList(List<Bitmap> newList) {
        photoList = newList;
        resultAdapter.notifyDataSetChanged();
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

}
