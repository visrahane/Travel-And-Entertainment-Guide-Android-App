package com.vis.entertainment.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vis.entertainment.R;

/**
 * Created by Vis on 13-04-2018.
 */

public class FavoriteFragment extends Fragment {
    private Activity mainActivity;

    public FavoriteFragment() {

    }

    @SuppressLint("ValidFragment")
    public FavoriteFragment(Activity mainActivity){
        this.mainActivity=mainActivity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite, container, false);
        return view;
    }
}
