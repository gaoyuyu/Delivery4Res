package com.gaoyy.delivery4res.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by gaoyy on 2016/8/24 0024.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter
{
    private int[] tabType;
    private List<Fragment> fragmentList;
    private Context context;

    public MainPagerAdapter(Context context, FragmentManager fm, int[] tabType, List<Fragment> fragmentList)
    {
        super(fm);
        this.context = context;
        this.tabType = tabType;
        this.fragmentList = fragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = context.getResources().getString(tabType[position]);
        return title;
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return tabType.length;
    }

}
