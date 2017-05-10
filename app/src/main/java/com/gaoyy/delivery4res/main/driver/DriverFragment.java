package com.gaoyy.delivery4res.main.driver;


import android.support.v4.app.Fragment;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class DriverFragment extends BaseFragment
{


    public DriverFragment()
    {
        // Required empty public constructor
    }

    public static DriverFragment newInstance()
    {
        DriverFragment fragment = new DriverFragment();
        return fragment;
    }


    @Override
    protected int getFragmentLayoutId()
    {
        return R.layout.fragment_driver;
    }
}
