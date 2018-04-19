package com.vis.entertainment.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.vis.entertainment.models.Result;

import java.util.List;

public class PhotoResultsAdapter extends RecyclerView.Adapter<PhotoResultsAdapter.ViewHolder> {

    private List<Bitmap> photoList;

    public static final String TAG=PhotoResultsAdapter.class.getName();

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private ImageView photoImageView;

        public ViewHolder(View view) {
            super(view);
            photoImageView=view.findViewById(R.id.photo);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Toast.makeText(itemView.getContext(), pos, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    public PhotoResultsAdapter(List<Bitmap> photoList) {
        this.photoList = photoList;
    }

    @Override
    public PhotoResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photos, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(PhotoResultsAdapter.ViewHolder holder, int position) {
        Bitmap bitmap= photoList.get(position);
        //holder.photoImageView.setImageBitmap(bitmap);
        holder.photoImageView.setImageBitmap(bitmap);
        //Picasso.get().load(result.getCategoryImageUrl()).into(holder.photoImageView);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
}
