package com.vis.entertainment.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.vis.entertainment.R;
import com.vis.entertainment.adapters.PhotoResultsAdapter;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.models.PlaceDetails;

import java.util.ArrayList;
import java.util.List;

public class PhotosFragment extends BaseFragment  {
    private List<Bitmap> photoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PhotoResultsAdapter resultAdapter;
    private PlaceDetails place;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        Gson gson=new Gson();
        place = gson.fromJson(getArguments().getString(ApplicationConstants.PLACE_DATA),PlaceDetails.class);

        recyclerView = (RecyclerView) view.findViewById(R.id.searchResultsView);
        resultAdapter = new PhotoResultsAdapter(photoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(resultAdapter);

        preparePhotoList();
        return view;
    }

    private void preparePhotoList() {
        //photoList.add();
        resultAdapter.notifyDataSetChanged();
    }

    @Override
    public void prepareList(List<Bitmap> newList){
        photoList=newList;
        //resultAdapter.notifyDataSetChanged();
    }
}
