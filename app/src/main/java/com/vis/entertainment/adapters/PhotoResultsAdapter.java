package com.vis.entertainment.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vis.entertainment.R;

import java.util.List;

public class PhotoResultsAdapter extends RecyclerView.Adapter<PhotoResultsAdapter.ViewHolder> {

    private List<Bitmap> photoList;

    private static final String TAG=PhotoResultsAdapter.class.getName();


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private ImageView photoImageView;

        public ViewHolder(View view) {
            super(view);
            photoImageView=view.findViewById(R.id.authorImg);
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
