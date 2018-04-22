package com.vis.entertainment.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vis.entertainment.R;
import com.vis.entertainment.adapters.PhotoResultsAdapter;
import com.vis.entertainment.adapters.ReviewsAdapter;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.constants.OrderEnum;
import com.vis.entertainment.constants.ReviewEnum;
import com.vis.entertainment.models.PlaceDetails;
import com.vis.entertainment.models.Review;
import com.vis.entertainment.util.ApplicationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ReviewsFragment extends BaseFragment {
    public static final String NO_REVIEWS = "No Reviews";
    private List<Review> reviewsList = new ArrayList<>();
    private List<Review> googleList = new ArrayList<>();
    private List<Review> yelpList = new ArrayList<>();

    private List<Review> defaultReviewsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReviewsAdapter resultAdapter;
    private TextView emptyView;
    private Spinner orderSpinner;
    private Spinner reviewSpinner;
    private String[] orderList;
    private String[] reviewNameList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_page, container, false);
        recyclerView = view.findViewById(R.id.reviewRecycler);
        emptyView = view.findViewById(R.id.noRecordsTxt);
        orderSpinner=view.findViewById(R.id.orderDropdown);
        reviewSpinner=view.findViewById(R.id.reviewsDropdown);
        resultAdapter = new ReviewsAdapter(reviewsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(resultAdapter);
        Resources res = getResources();
        orderList= res.getStringArray(R.array.orderArray);
        reviewNameList= res.getStringArray(R.array.reviewArray);
        setListeners();
        prepareReviewList();
        return view;
    }

    private void showEmptyRecordText() {
        if (reviewsList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setText(NO_REVIEWS);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        reviewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                orderSpinner.setSelection(0);//set to default order
                switch (ReviewEnum.getEnum(reviewNameList[position])) {
                    case GOOGLE_REVIEW:
                        reviewsList.clear();reviewsList.addAll(googleList);break;
                    case YELP_REVIEW:
                        reviewsList.clear();reviewsList.addAll(yelpList);break;
                }
                showEmptyRecordText();//show no records if no reviews
                resultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                switch (OrderEnum.getEnum(orderList[position])) {
                    case HIGHEST_RATING:
                        Collections.sort(reviewsList, new Comparator<Review>() {
                            @Override
                            public int compare(Review r1, Review r2) {
                                return (int)(Float.parseFloat(r2.getRating())-Float.parseFloat(r1.getRating()));
                            }
                        }); break;
                    case LOWEST_RATING:
                        Collections.sort(reviewsList, new Comparator<Review>() {
                            @Override
                            public int compare(Review r1, Review r2) {
                                return (int)(Float.parseFloat(r1.getRating())-Float.parseFloat(r2.getRating()));
                            }
                        }); break;
                    case MOST_RECENT:
                        Collections.sort(reviewsList, new Comparator<Review>() {
                            @Override
                            public int compare(Review r1, Review r2) {
                                SimpleDateFormat sdf = new SimpleDateFormat(ApplicationConstants.DATE_PATTERN);
                                Date date1=null,date2 = null;
                                try {
                                    date1 = sdf.parse(r1.getDate());
                                    date2 = sdf.parse(r2.getDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return date2.compareTo(date1);
                            }
                        });
                        break;
                    case LEAST_RECENT:
                        Collections.sort(reviewsList, new Comparator<Review>() {
                            @Override
                            public int compare(Review r1, Review r2) {
                                SimpleDateFormat sdf = new SimpleDateFormat(ApplicationConstants.DATE_PATTERN);
                                Date date1=null,date2 = null;
                                try {
                                    date1 = sdf.parse(r1.getDate());
                                    date2 = sdf.parse(r2.getDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return date1.compareTo(date2);
                            }
                        });
                        break;
                    case DEFAULT_ORDER:
                    default:
                        reviewsList.clear();
                        if(ReviewEnum.GOOGLE_REVIEW.getName().equals(reviewSpinner.getSelectedItem().toString())){
                            reviewsList.addAll(googleList);
                        }else {
                            reviewsList.addAll(yelpList);
                        }
                        break;
                }
                resultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void prepareReviewList() {
        resultAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateInfo(JSONObject resultJson) {
        //prepare Review objects and add to the list
        if (resultJson.has("reviews")) {
            googleList.addAll(getGoogleReviews(resultJson));
        } else if(resultJson.has(ApplicationConstants.YELP_REVIEWS)) {
            yelpList.addAll(getYelpReviews(resultJson));
        }
        //defaultReviewsList.addAll(reviewsList);
        reviewsList.addAll(googleList);
    }

    private List<Review>  getYelpReviews(JSONObject resultJson) {
        JSONArray reviews = null;
        List<Review>yelpReviews=new ArrayList<>();
        try {
            reviews = resultJson.getJSONArray(ApplicationConstants.YELP_REVIEWS);
            for (int i = 0; i < reviews.length(); i++) {
                Review review = new Review();
                JSONObject reviewJson = reviews.getJSONObject(i);
                review.setAuthorName(reviewJson.getJSONObject("user").getString("name"));
                review.setAuthorUrl(reviewJson.getString("url"));
                review.setPhotoUrl(reviewJson.getJSONObject("user").getString("image_url"));
                review.setRating(reviewJson.getString("rating"));
                review.setText(reviewJson.getString("text"));
                review.setDate((reviewJson.getString("time_created")));
                yelpReviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return yelpReviews;
    }

    private List<Review> getGoogleReviews(JSONObject resultJson) {
        JSONArray reviews = null;
        List<Review>googleReviews=new ArrayList<>();
        try {
            reviews = resultJson.getJSONArray("reviews");
            for (int i = 0; i < reviews.length(); i++) {
                Review review = new Review();
                JSONObject reviewJson = reviews.getJSONObject(i);
                review.setAuthorName(reviewJson.getString("author_name"));
                review.setAuthorUrl(reviewJson.getString("author_url"));
                review.setPhotoUrl(reviewJson.getString("profile_photo_url"));
                review.setRating(reviewJson.getString("rating"));
                review.setText(reviewJson.getString("text"));
                review.setDate(ApplicationUtil.getDateFromEpoch(reviewJson.getString("time")));
                googleReviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googleReviews;
    }
}
