package com.vis.entertainment.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;

import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vis.entertainment.R;
import com.vis.entertainment.fragments.SearchFragment;
import com.vis.entertainment.models.Result;

import java.util.ArrayList;
import java.util.List;

public class PlacePredictionAdapter  extends ArrayAdapter{
    private static final String TAG ="PlacePredictionAdapter" ;
    private List<Result> dataList;
    private Context mContext;
    private GeoDataClient geoDataClient;

    private PlacePredictionAdapter.PlaceAutoCompleteFilter listFilter =
            new PlacePredictionAdapter.PlaceAutoCompleteFilter();

    public PlacePredictionAdapter(Context context) {
        super(context, R.layout.autocomplete_location_dropdown,
                new ArrayList<Result>());
        mContext = context;

        //get GeoDataClient
        geoDataClient = Places.getGeoDataClient(mContext, null);


    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Result getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.autocomplete_location_dropdown,
                            parent, false);
        }

        TextView textOne = view.findViewById(R.id.placePredictionTxt);
        textOne.setText(dataList.get(position).getAddress());

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class PlaceAutoCompleteFilter extends Filter {
        private Object lock = new Object();
        private Object lockTwo = new Object();
        private boolean placeResults = false;


        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            placeResults = false;
            final List<Result> placesList = new ArrayList<>();

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = new ArrayList<Result>();
                    results.count = 0;
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                Task<AutocompletePredictionBufferResponse> task
                        = getAutoCompletePlaces(searchStrLowerCase);

                task.addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "Auto complete prediction successful");
                            AutocompletePredictionBufferResponse predictions = task.getResult();
                            Result autoPlace;
                            for (AutocompletePrediction prediction : predictions) {
                                autoPlace = new Result();
                                autoPlace.setPlaceId(prediction.getPlaceId());
                                autoPlace.setAddress(prediction.getFullText(null).toString());
                                placesList.add(autoPlace);
                            }
                            predictions.release();
                            Log.d("", "Auto complete predictions size " + placesList.size());
                        } else {
                            Log.d(TAG, "Auto complete prediction unsuccessful");
                        }
                        //inform waiting thread about api call completion
                        placeResults = true;
                        synchronized (lockTwo) {
                            lockTwo.notifyAll();
                        }
                    }
                });

                //wait for the results from asynchronous API call
                while (!placeResults) {
                    synchronized (lockTwo) {
                        try {
                            lockTwo.wait();
                        } catch (InterruptedException e) {

                        }
                    }
                }
                results.values = placesList;
                results.count = placesList.size();
                Log.d(TAG, "Autocomplete predictions size after wait" + results.count);
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<Result>) results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        private Task<AutocompletePredictionBufferResponse> getAutoCompletePlaces(String query) {
            //create autocomplete filter using data from filter Views
            AutocompleteFilter.Builder filterBuilder = new AutocompleteFilter.Builder();


            Task<AutocompletePredictionBufferResponse> results =
                    geoDataClient.getAutocompletePredictions(query, null,
                            filterBuilder.build());
            return results;
        }
    }

}
