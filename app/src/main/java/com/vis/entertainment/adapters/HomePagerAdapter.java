package com.vis.entertainment.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.res.Resources;
import android.util.Log;

import com.vis.entertainment.R;
import com.vis.entertainment.activity.MainActivity;
import com.vis.entertainment.fragments.BaseFragment;
import com.vis.entertainment.fragments.FavoriteFragment;
import com.vis.entertainment.fragments.SearchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vis on 13-04-2018.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    private String tabs[];
    private MainActivity mainActivity;
    private List<Fragment> fragmentList;

    public HomePagerAdapter(FragmentManager fm, MainActivity mainActivity) {
        super(fm);
        this.mainActivity=mainActivity;
        Resources res = mainActivity.getResources();
        tabs= res.getStringArray(R.array.homeTabsArray);
        fragmentList=new ArrayList<>();
        fragmentList.add(new SearchFragment(mainActivity));
        fragmentList.add(new FavoriteFragment(mainActivity));
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Log.d("getTabItem", "getItem: "+position);
        if (position == 0)
        {
            fragment = fragmentList.get(0);
        }
        else if (position == 1)
        {
            fragment = fragmentList.get(1);
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
