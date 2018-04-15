package com.vis.entertainment.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.res.Resources;
import android.content.Context;
import android.util.Log;

import com.vis.entertainment.R;

/**
 * Created by Vis on 13-04-2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String tabs[];

    public ViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        Resources res = context.getResources();
        tabs= res.getStringArray(R.array.tabsArray);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Log.d("getTabItem", "getItem: "+position);
        if (position == 0)
        {
            fragment = new SearchFragment();
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
