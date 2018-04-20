package com.vis.entertainment.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vis.entertainment.R;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.constants.InfoConstants;
import com.vis.entertainment.models.PlaceDetails;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info, container, false);
        init(view);
        Gson gson=new Gson();
        place = gson.fromJson(getArguments().getString(ApplicationConstants.PLACE_DATA),PlaceDetails.class);
        prepareTable(place);
        return view;
    }

    private void init(View view) {
        addressLbl=view.findViewById(R.id.addressLbl);
        addressTxt=view.findViewById(R.id.addressTxt);
        phoneNoLbl=view.findViewById(R.id.phoneNoLbl);
        phoneNoTxt=view.findViewById(R.id.phoneNoTxt);
        priceLevelLbl=view.findViewById(R.id.priceLevelLbl);
        priceLevelTxt=view.findViewById(R.id.priceLevelTxt);
        websiteUriLbl=view.findViewById(R.id.websiteLbl);
        websiteUriTxt=view.findViewById(R.id.websiteTxt);
    }

    private void prepareTable(PlaceDetails place) {
        //Address
        /*if(place.getAddress()!=null){
            TableRow row = new TableRow(this.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            //create textView
            TextView textView1=new TextView(this.getContext());
            textView1.setText(InfoConstants.ADDRESS);
            //create TextView
            TextView textView2=new TextView(this.getContext());
            textView2.setText(place.getAddress());
            row.addView(textView1);
            row.addView(textView2);
            tableLayout.addView(row,new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        }*/
        if(place.getAddress()!=null){
            addressLbl.setVisibility(View.VISIBLE);
            addressTxt.setText(place.getAddress());
            addressTxt.setVisibility(View.VISIBLE);
        }
        //Phone Number
        if(place.getPhoneNo()!=null){
            phoneNoLbl.setVisibility(View.VISIBLE);
            phoneNoTxt.setText(place.getPhoneNo());
            phoneNoTxt.setVisibility(View.VISIBLE);
        }
        //Price Level
        if(place.getPriceLevel()!=null){
            priceLevelLbl.setVisibility(View.VISIBLE);
            priceLevelTxt.setText(place.getPriceLevel());
            priceLevelTxt.setVisibility(View.VISIBLE);
        }
        //Rating
        //Google Page
        /*if(place.getWebsiteUri()!=null){
            priceLevelLbl.setVisibility(View.VISIBLE);
            phoneNoTxt.setText(place.getPhoneNo());
            phoneNoTxt.setVisibility(View.VISIBLE);
        }*/
        //Website
        if(place.getWebsiteUri()!=null){
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
    }
}
