package com.vis.entertainment.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vis.entertainment.R;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.models.PlaceDetails;

import org.json.JSONException;
import org.json.JSONObject;

public class InfoFragment extends BaseFragment {
    private PlaceDetails place;
    private TableLayout tableLayout;
    private TextView addressTxt;
    private TextView addressLbl;
    private TextView phoneNoTxt;
    private TextView phoneNoLbl;
    private TextView priceLevelTxt;
    private TextView priceLevelLbl;
    private TextView websiteUriTxt;
    private TextView websiteUriLbl;
    private TextView googlePageLbl;
    private TextView googlePageTxt;
    private TextView ratingLbl;
    private RatingBar ratingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info, container, false);
        init(view);
        Gson gson = new Gson();
        if (place==null) {
            place = gson.fromJson(getArguments().getString(ApplicationConstants.PLACE_DATA), PlaceDetails.class);
        }
        prepareTable();
        registerEvents();
        return view;
    }

    private void registerEvents() {
        phoneNoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNoTxt.getText().toString()));
                startActivity(intent);
            }
        });
        googlePageTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open page
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(googlePageTxt.getText().toString()));
                startActivity(browserIntent);
            }
        });
        websiteUriTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open page
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUriTxt.getText().toString()));
                startActivity(browserIntent);
            }
        });
    }

    private void init(View view) {
        addressLbl = view.findViewById(R.id.addressLbl);
        addressTxt = view.findViewById(R.id.addressTxt);
        phoneNoLbl = view.findViewById(R.id.phoneNoLbl);
        phoneNoTxt = view.findViewById(R.id.phoneNoTxt);
        priceLevelLbl = view.findViewById(R.id.priceLevelLbl);
        priceLevelTxt = view.findViewById(R.id.priceLevelTxt);
        websiteUriLbl = view.findViewById(R.id.websiteLbl);
        websiteUriTxt = view.findViewById(R.id.websiteTxt);
        googlePageLbl = view.findViewById(R.id.googlePageLbl);
        googlePageTxt = view.findViewById(R.id.googlePageTxt);
        ratingLbl=view.findViewById(R.id.ratingLbl);
        ratingBar=view.findViewById(R.id.reviewRatingBar);
    }

    private void prepareTable() {
        //Address
        if (place.getAddress() != null) {
            addressLbl.setVisibility(View.VISIBLE);
            addressTxt.setText(place.getAddress());
            addressTxt.setVisibility(View.VISIBLE);
        }
        //Phone Number
        if (place.getPhoneNo() != null) {
            phoneNoLbl.setVisibility(View.VISIBLE);
            phoneNoTxt.setText(place.getPhoneNo());
            phoneNoTxt.setVisibility(View.VISIBLE);
        }
        //Price Level

        if (place.getPriceLevel() != null) {
            priceLevelLbl.setVisibility(View.VISIBLE);
            StringBuilder priceLevel = new StringBuilder();
            for (int i = 0; i < Integer.parseInt(place.getPriceLevel()); i++) {
                priceLevel.append("$");
            }
            priceLevelTxt.setText(priceLevel.toString());
            priceLevelTxt.setVisibility(View.VISIBLE);
        }
        //Rating
        if (place.getRating() != null ) {
            ratingLbl.setVisibility(View.VISIBLE);
            ratingBar.setRating(Float.parseFloat(place.getRating()));
            ratingBar.setVisibility(View.VISIBLE);
        }
        //Website
        if (place.getWebsiteUri() != null) {
            websiteUriLbl.setVisibility(View.VISIBLE);
            websiteUriTxt.setText(place.getWebsiteUri());
            websiteUriTxt.setVisibility(View.VISIBLE);
        }
        //Hours
        /*if(place!=null){
            priceLevelLbl.setVisibility(View.VISIBLE);
            phoneNoTxt.setText(place.getPhoneNo());
            phoneNoTxt.setVisibility(View.VISIBLE);
        }*/
        //Google page uri
        if(place.getUrl()!=null){
            googlePageLbl.setVisibility(View.VISIBLE);
            googlePageTxt.setText(place.getUrl());
            googlePageTxt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateInfo(JSONObject resultJson) {
        //Google Page
        /*if(place.getWebsiteUri()!=null){
            priceLevelLbl.setVisibility(View.VISIBLE);
            phoneNoTxt.setText(place.getPhoneNo());
            phoneNoTxt.setVisibility(View.VISIBLE);
        }*/
        try {
            if(resultJson.has("url")){
                googlePageLbl.setVisibility(View.VISIBLE);
                googlePageTxt.setText(resultJson.getString("url"));
                googlePageTxt.setVisibility(View.VISIBLE);
                place.setUrl(resultJson.getString("url"));
            }
            if(resultJson.has("hours")){

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
