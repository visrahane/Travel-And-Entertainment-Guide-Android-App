package com.vis.entertainment.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vis.entertainment.R;
import com.vis.entertainment.adapters.SearchResultsAdapter;
import com.vis.entertainment.constants.TagsEnum;
import com.vis.entertainment.models.Result;
import com.vis.entertainment.util.ApplicationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vis on 13-04-2018.
 */

public class FavoriteFragment extends Fragment {
    private AppCompatActivity mainActivity;
    private TextView emptyView;
    private List<Result> resultList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchResultsAdapter resultAdapter;
    private SharedPreferences sharedPref;

    public FavoriteFragment() {

    }

    @SuppressLint("ValidFragment")
    public FavoriteFragment(AppCompatActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_or_fav_page, container, false);
        init(view);
        //resultList = fetchFavFromFile();
        if (resultList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setText("No favorites");
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        return view;
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.searchResultsView);
        //requestQueue = Volley.newRequestQueue(this.getContext().getApplicationContext());
        resultAdapter = new SearchResultsAdapter(resultList, mainActivity, TagsEnum.FAVORITE_LIST);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(resultAdapter);
        emptyView = view.findViewById(R.id.noRecordsTxt);
        sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        resultList.addAll(ApplicationUtil.retrieveFromSharedPref(sharedPref));
    }


}
