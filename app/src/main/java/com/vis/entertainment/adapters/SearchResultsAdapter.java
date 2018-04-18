package com.vis.entertainment.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vis.entertainment.R;
import com.vis.entertainment.models.Result;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private List<Result> resultList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView name, address;
        private ImageView categoryImage,favoriteImage;

        public ViewHolder(View view) {
            super(view);
            categoryImage = view.findViewById(R.id.category_image);
            favoriteImage=view.findViewById(R.id.heart_image);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
        }


    }
    public SearchResultsAdapter(List<Result> resultList) {
        this.resultList = resultList;
    }

    @Override
    public SearchResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_or_fav_list_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;

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
