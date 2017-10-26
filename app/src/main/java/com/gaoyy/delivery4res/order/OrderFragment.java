package com.gaoyy.delivery4res.order;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.adapter.MainPagerAdapter;
import com.gaoyy.delivery4res.base.BaseFragment;
import com.gaoyy.delivery4res.order.orderlist.OrderListFragment;
import com.gaoyy.delivery4res.order.orderlist.OrderListPresenter;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends BaseFragment
{
    private LinearLayout contentOrder;
    private Toolbar orderToolbar;
    private ViewPager orderViewpager;
    private TabLayout orderTablayout;


    private int[] orderTabType = {R.string.tab_delivery_order_list, R.string.tab_ma_order_list};

    private MainPagerAdapter mainPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();

    public OrderFragment()
    {
        // Required empty public constructor
    }

    public static OrderFragment newInstance()
    {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    public void setToOrderList()
    {
        orderViewpager.setCurrentItem(0, true);
    }

    @Override
    protected int getFragmentLayoutId()
    {
        return R.layout.fragment_order;
    }

    @Override
    protected void assignViews(View rootView)
    {
        super.assignViews(rootView);
        contentOrder = (LinearLayout) rootView.findViewById(R.id.content_order);
        orderToolbar = (Toolbar) rootView.findViewById(R.id.order_toolbar);
        orderViewpager = (ViewPager) rootView.findViewById(R.id.order_viewpager);
        orderTablayout = (TabLayout) rootView.findViewById(R.id.order_tablayout);
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar(orderToolbar, R.string.toolbar_title_order_list, false, -1);
    }

    @Override
    protected void configViews()
    {
        super.configViews();


        for (int i = 0; i < orderTabType.length; i++)
        {
            OrderListFragment orderListFragment = OrderListFragment.newInstance();
            Bundle bundle = new Bundle();
            //isMA=   0 |派送订单   1|商城订单
            bundle.putInt("isMA", i);
            orderListFragment.setArguments(bundle);
            new OrderListPresenter(orderListFragment);
            fragmentList.add(orderListFragment);
        }


        mainPagerAdapter = new MainPagerAdapter(activity, getChildFragmentManager(), orderTabType, fragmentList);
        orderViewpager.setAdapter(mainPagerAdapter);

        orderTablayout.setTabMode(TabLayout.MODE_FIXED);
        orderTablayout.setupWithViewPager(orderViewpager);
        orderTablayout.setBackgroundColor(getResources().getColor(R.color.white));
        orderTablayout.setTabsFromPagerAdapter(mainPagerAdapter);
    }
}
