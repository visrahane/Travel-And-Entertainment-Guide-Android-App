package com.vis.entertainment.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vis.entertainment.R;
import com.vis.entertainment.adapters.SearchResultsAdapter;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.models.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private List<Result> resultList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchResultsAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String searchData = intent.getStringExtra(ApplicationConstants.SEARCH_DATA);
        //getActionBar().setTitle(this.getResources().getString(R.string.searchResultPageTitle));
        recyclerView = (RecyclerView) findViewById(R.id.searchResultsView);

        resultAdapter = new SearchResultsAdapter(resultList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(resultAdapter);

        prepareResultList(searchData);
    }

    private void prepareResultList(String searchData) {
        JSONObject searchResultJson = null;
        try {
            searchResultJson = new JSONObject(searchData);
            JSONArray resultsArray=searchResultJson.getJSONArray("results");
            for(int i=0;i<resultsArray.length();i++){
                JSONObject resultJson=resultsArray.getJSONObject(i);
                Result result=new Result();
                result.setCategoryImageUrl(resultJson.getString("icon"));
                result.setName(resultJson.getString("name"));
                result.setAddress(resultJson.getString("vicinity"));
                result.setPlaceId(resultJson.getString("place_id"));
                resultList.add(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        resultAdapter.notifyDataSetChanged();
    }
}
