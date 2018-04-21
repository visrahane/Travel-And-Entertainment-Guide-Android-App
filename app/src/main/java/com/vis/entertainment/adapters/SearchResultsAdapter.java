package com.vis.entertainment.adapters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.vis.entertainment.R;
import com.vis.entertainment.activity.DetailsActivity;
import com.vis.entertainment.activity.SearchResultActivity;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.models.PlaceDetails;
import com.vis.entertainment.models.Result;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private List<Result> resultList;
    private GeoDataClient geoDataClient;
    private SearchResultActivity searchResultActivity;
    private ProgressDialog progress;

    public static final String TAG=SearchResultsAdapter.class.getName();

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView name, address;
        private ImageView categoryImage,favoriteImage;

        public ViewHolder(View view) {
            super(view);
            categoryImage = view.findViewById(R.id.category_image);
            favoriteImage=view.findViewById(R.id.heart_image);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progress.show();
                    int pos = getAdapterPosition();
                    getDetails(resultList.get(pos));
                    Toast.makeText(itemView.getContext(), resultList.get(pos).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void getDetails(Result result) {
        //get places details from google place obj
        geoDataClient.getPlaceById(result.getPlaceId()).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse places = task.getResult();
                    Place place = places.get(0);
                    Log.i(TAG, "Place found: " + place.getName());
                    PlaceDetails placeDetails=prepareMyPlaceObj(place);
                    startDetailsActivity(placeDetails);
                    progress.dismiss();places.release();
                    //call new activty with 4 tabs and send place details to it
                } else {
                    Log.e(TAG, "Place not found.");
                }

            }
        });
        //start a new activity with 4 tabs
    }

    private PlaceDetails prepareMyPlaceObj(Place place) {
        PlaceDetails placeDetails=new PlaceDetails();
        if(place.getWebsiteUri()!=null)placeDetails.setWebsiteUri(place.getWebsiteUri().toString());
        if(place.getAddress()!=null)placeDetails.setAddress(place.getAddress().toString());
        if(place.getName()!=null)placeDetails.setName(place.getName().toString());
        if(place.getPhoneNumber()!=null)placeDetails.setPhoneNo(place.getPhoneNumber().toString());
        placeDetails.setPlaceId(place.getId());
        if(place.getRating()!=-1.0)placeDetails.setRating(Float.toString(place.getRating()));
        if(place.getPriceLevel()!=-1)placeDetails.setPriceLevel(Integer.toString(place.getPriceLevel()));
        placeDetails.setLatLng(place.getLatLng());
        return placeDetails;
    }

    private void startDetailsActivity(PlaceDetails place) {
        Intent intent = new Intent(searchResultActivity, DetailsActivity.class);
        Gson gS = new Gson();
        String data = gS.toJson(place);
        intent.putExtra(ApplicationConstants.PLACE_DATA,data);
        //intent.putExtra(ApplicationConstants.PLACE_DATA,placeDetails);
        searchResultActivity.startActivity(intent);
    }

    public SearchResultsAdapter(List<Result> resultList,SearchResultActivity searchResultActivity) {
        this.resultList = resultList;
        this.searchResultActivity=searchResultActivity;
        this.geoDataClient= Places.getGeoDataClient(searchResultActivity);
        progress=new ProgressDialog(searchResultActivity,R.style.AppCompatAlertDialogStyle);
        progress.setMessage("Fetching details");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
    }

    @Override
    public SearchResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_or_fav_list_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(SearchResultsAdapter.ViewHolder holder, int position) {
        Result result = resultList.get(position);
        Picasso.get().load(result.getCategoryImageUrl()).into(holder.categoryImage);
        holder.favoriteImage.setImageResource(R.drawable.heart_outline_black);
        holder.address.setText(result.getAddress());
        holder.name.setText(result.getName());
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }
}
