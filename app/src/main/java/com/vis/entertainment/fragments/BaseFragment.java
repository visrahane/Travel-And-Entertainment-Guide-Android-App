package com.vis.entertainment.fragments;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import org.json.JSONObject;

import java.util.List;

public abstract class BaseFragment extends Fragment {
    public void prepareList(List<Bitmap> newList){ }
    public void updateInfo(JSONObject details){ }

}
