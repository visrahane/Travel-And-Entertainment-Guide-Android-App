package com.vis.entertainment.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vis.entertainment.R;
import com.vis.entertainment.activity.MainActivity;
import com.vis.entertainment.activity.SearchResultActivity;
import com.vis.entertainment.constants.ApplicationConstants;

/**
 * Created by Vis on 13-04-2018.
 */

public class SearchFragment extends Fragment {

    private MainActivity mainActivity;
    private  View view;
    private EditText keywordTxt;
    private EditText distanceTxt;
    private AutoCompleteTextView locationTxt;
    private Spinner categorySpinner;
    private RadioButton currentLocationRadioBtn;
    private Button clearBtn;
    private Button searchBtn;
    private RadioGroup radioGroup;
    private RequestQueue requestQueue;
   // private ProgressBar progressBar;
    private ProgressDialog progress;

    @SuppressLint("ValidFragment")
    public SearchFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.input_form, container, false);

        init();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressBar.setVisibility(View.VISIBLE);
                progress.show();
                getResults();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId){
                    case R.id.otherLocationRadioBtn:locationTxt.setEnabled(true);break;
                    default:locationTxt.setEnabled(false);
                }
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keywordTxt.getText().clear();
                distanceTxt.getText().clear();
                locationTxt.getText().clear();
                categorySpinner.setSelection(0);
                currentLocationRadioBtn.performClick();
            }
        });
        
        return view;
    }
    private void init() {
        keywordTxt=  view.findViewById(R.id.keywordTxt);
        distanceTxt= view.findViewById(R.id.distanceTxt);
        locationTxt= view.findViewById(R.id.locationTxt);
        categorySpinner= view.findViewById(R.id.categoryDropDown);
        radioGroup=view.findViewById(R.id.fromRadioGroup);
        searchBtn = view.findViewById(R.id.searchBtn);
        requestQueue= Volley.newRequestQueue(this.getContext().getApplicationContext());
        currentLocationRadioBtn=view.findViewById(R.id.currentLocationRadioBtn);
        clearBtn=view.findViewById(R.id.clearBtn);
        //progressBar=view.findViewById(R.id.resultsProgressBar);

        //

        progress=new ProgressDialog(this.getContext(),R.style.AppCompatAlertDialogStyle);
        progress.setMessage("Fetching results");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

    }


    private void getResults() {
        //make a call to the server and fetch result
        String url=getResources().getString(R.string.searchResultUri);
        Uri builtUri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("keyword",keywordTxt.getText().toString() )
                .appendQueryParameter("category", categorySpinner.getSelectedItem().toString())
                .appendQueryParameter("distance", distanceTxt.getText().toString().isEmpty()?"10":distanceTxt.getText().toString())
                .appendQueryParameter("location", locationTxt.getText().toString().isEmpty()?"here":locationTxt.getText().toString())
                .appendQueryParameter("latitude",mainActivity.getLocation()!=null?Double.toString(mainActivity.getLocation().getLatitude()):"34.029653")
                .appendQueryParameter("longitude",mainActivity.getLocation()!=null?Double.toString(mainActivity.getLocation().getLongitude()):"-118.283130")
                .build();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, builtUri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // call new activity to show results
                        progress.dismiss();
                        Log.d("Search Response is: ", response);
                        Intent intent = new Intent(mainActivity, SearchResultActivity.class);
                        intent.putExtra(ApplicationConstants.SEARCH_DATA, response);
                        startActivity(intent);
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
}
