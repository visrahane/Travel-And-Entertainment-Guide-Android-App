package com.vis.entertainment.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vis.entertainment.R;
import com.vis.entertainment.models.Result;

import org.json.JSONObject;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<JSONObject> reviewList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews, parent, false);

        ReviewsAdapter.ViewHolder viewHolder = new ReviewsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*Result result = resultList.get(position);
        Picasso.get().load(result.getCategoryImageUrl()).into(holder.categoryImage);
        holder.favoriteImage.setImageResource(R.drawable.heart_outline_black);
        holder.address.setText(result.getAddress());
        holder.name.setText(result.getName());*/
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

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

}
