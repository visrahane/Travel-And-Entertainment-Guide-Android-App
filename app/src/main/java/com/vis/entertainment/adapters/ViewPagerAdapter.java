package com.vis.entertainment.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.res.Resources;
import android.util.Log;

import com.vis.entertainment.R;
import com.vis.entertainment.activity.MainActivity;
import com.vis.entertainment.fragments.FavoriteFragment;
import com.vis.entertainment.fragments.SearchFragment;

/**
 * Created by Vis on 13-04-2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String tabs[];
    private MainActivity mainActivity;

    public ViewPagerAdapter(FragmentManager fm,MainActivity mainActivity) {
        super(fm);
        this.mainActivity=mainActivity;
        Resources res = mainActivity.getResources();
        tabs= res.getStringArray(R.array.tabsArray);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Log.d("getTabItem", "getItem: "+position);
        if (position == 0)
        {
            fragment = new SearchFragment(mainActivity);
        }
        else if (position == 1)
        {
            fragment = new FavoriteFragment();
        }
        return fragment;
    }
    @Override
    public CharSequence getPageTitle(int position) {
       return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
