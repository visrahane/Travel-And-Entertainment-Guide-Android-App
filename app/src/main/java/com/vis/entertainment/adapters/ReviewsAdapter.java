package com.vis.entertainment.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vis.entertainment.R;
import com.vis.entertainment.models.Result;
import com.vis.entertainment.models.Review;

import org.json.JSONObject;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> reviewList;

    public ReviewsAdapter(List<Review> reviewsList) {
        this.reviewList=reviewsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews, parent, false);

        ReviewsAdapter.ViewHolder viewHolder = new ReviewsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        Picasso.get().load(review.getPhotoUrl()).into(holder.authorImageView);
        holder.reviewTxt.setText(review.getText());
        holder.authorName.setText(review.getAuthorName());
        holder.reviewDate.setText(review.getDate());
        holder.reviewRating.setRating(Float.parseFloat(review.getRating()));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private ImageView authorImageView;
        private TextView authorName, reviewDate, reviewTxt;
        private RatingBar reviewRating;

        public ViewHolder(View view) {
            super(view);
            authorImageView = view.findViewById(R.id.authorImg);
            authorName = (TextView) view.findViewById(R.id.authorName);
            reviewDate = (TextView) view.findViewById(R.id.reviewDateTxt);
            reviewTxt = (TextView) view.findViewById(R.id.reviewTxt);
            reviewRating = view.findViewById(R.id.reviewRatingBar);

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
