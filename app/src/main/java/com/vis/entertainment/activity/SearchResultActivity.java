package com.vis.entertainment.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vis.entertainment.R;
import com.vis.entertainment.adapters.SearchResultsAdapter;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.constants.TagsEnum;
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
    private Button btnPrev, btnNext;
    private TextView emptyView;
    private List<List<Result>> pages = new ArrayList<>();
    private int currentPage;
    private String nextPageUrl = "";
    private RequestQueue requestQueue;
    private ProgressDialog progress;

    @Override
    public void onResume(){
        super.onResume();
        resultAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_or_fav_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String searchData = intent.getStringExtra(ApplicationConstants.SEARCH_DATA);
        //getActionBar().setTitle(this.getResources().getString(R.string.searchResultPageTitle));
        init();
        nextPageUrl = getNextPageUrl(searchData);
        if (nextPageUrl != "") {
            //show prev and nextBtn,, prev disabled and next enabled
            btnNext.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.VISIBLE);
            btnPrev.setEnabled(false);
        }

        handleResultList(searchData);
        if (resultList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        setUpListeners();
    }

    private void setUpListeners() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to next Page, i.e, update resultList
                List<Result> nextPage = null;
                progress.show();
                if(pages.size()>currentPage+1) //is available in cache?
                {
                    nextPage = pages.get(++currentPage);
                    resultList.clear();
                    resultList.addAll(nextPage);
                    resultAdapter.notifyDataSetChanged();
                    btnPrev.setEnabled(true);
                    progress.dismiss();
                    if(currentPage+1>=pages.size())
                    {
                        btnNext.setEnabled(false);
                    }
                }
                else{
                    fetchFromServer();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to prev Page i.e, update resultList
                if(currentPage-1==0){
                    btnPrev.setEnabled(false);
                }
                List<Result> result=pages.get(--currentPage);
                resultList.clear();
                resultList.addAll(result);
                resultAdapter.notifyDataSetChanged();
                btnNext.setEnabled(true);
            }
        });
    }

    private void fetchFromServer() {
        //make a call to the server and fetch result
        String url = getResources().getString(R.string.nextResultUri);
        Uri builtUri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("nextPageToken", nextPageUrl)
                .build();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, builtUri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // call new activity to show results
                        Log.d("NextPage Response is: ", response);
                        //update resultList and save this page to pages
                        currentPage++;
                        pages.add(prepareResultList(response));
                        nextPageUrl=getNextPageUrl(response);
                        resultList.clear();
                        resultList.addAll(pages.get(currentPage));
                        resultAdapter.notifyDataSetChanged();
                        btnPrev.setEnabled(true);
                        if(nextPageUrl=="")//no next-page url
                        {
                            btnNext.setEnabled(false);
                        }

                        progress.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error Message
                progress.dismiss();
                Log.d("Search Response is: ", error.toString());
            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }


    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.searchResultsView);
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        resultAdapter = new SearchResultsAdapter(resultList, this, TagsEnum.SEARCH_LIST);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(resultAdapter);
        btnPrev = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        emptyView = findViewById(R.id.noRecordsTxt);
        progress = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progress.setMessage("Fetching next page");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
    }

    private String getNextPageUrl(String searchData) {
        JSONObject searchResultJson = null;
        String nextPageUrl = "";
        try {
            searchResultJson = new JSONObject(searchData);
            if (searchResultJson.has("next_page_token")) {
                nextPageUrl = searchResultJson.getString("next_page_token");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nextPageUrl;
    }

    private void handleResultList(String searchData) {
        resultList.addAll(prepareResultList(searchData));
        pages.add(new ArrayList<>(resultList));
        resultAdapter.notifyDataSetChanged();
    }

    private List<Result> prepareResultList(String searchData) {
        List<Result> resultList = new ArrayList<>();
        JSONObject searchResultJson = null;
        try {
            searchResultJson = new JSONObject(searchData);
            JSONArray resultsArray = searchResultJson.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultJson = resultsArray.getJSONObject(i);
                Result result = new Result();
                result.setCategoryImageUrl(resultJson.getString("icon"));
                result.setName(resultJson.getString("name"));
                result.setAddress(resultJson.getString("vicinity"));
                result.setPlaceId(resultJson.getString("place_id"));
                resultList.add(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
