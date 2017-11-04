package com.gaoyy.delivery4res.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.adapter.MainPagerAdapter;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.home.RestaurantFragment;
import com.gaoyy.delivery4res.mine.MineFragment;
import com.gaoyy.delivery4res.order.OrderFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MineFragment.OnFragmentInteractionListener
{
    private RelativeLayout contentMain;
    private ViewPager mainViewpager;
    private TabLayout mainTablayout;

    private int[] tabType = {R.string.tab_home, R.string.tab_order, R.string.tab_mine};
    private int[] tabSelector = {R.drawable.selector_home, R.drawable.selector_order, R.drawable.selector_account};

    public static List<RestInfo.BodyBean.RemarkDictBean> remarkDict;
    public static List<RestInfo.BodyBean.FinishedTimeBean> finishedTime;
    public static List<RestInfo.BodyBean.DictStatusBean> dictStatus;

    private MainPagerAdapter mainPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();

    private DetailToMainReceiver detailToMainReceiver;

    public class DetailToMainReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals("android.intent.action.DetailToMainReceiver"))
            {
                if(intent.getIntExtra("orderList",0) == 1)
                {
                    mainViewpager.setCurrentItem(1, true);
                    OrderFragment fragment = (OrderFragment) mainPagerAdapter.getItem(1);
                    fragment.setToOrderList();
                }

                if(intent.getIntExtra("clearInfo",0) == 2)
                {
                    mainViewpager.setCurrentItem(0, true);
                    RestaurantFragment fragment = (RestaurantFragment) mainPagerAdapter.getItem(0);
                    fragment.clear();
                }
            }
        }
    }

    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        contentMain = (RelativeLayout) findViewById(R.id.content_main);
        mainViewpager = (ViewPager) findViewById(R.id.main_viewpager);
        mainTablayout = (TabLayout) findViewById(R.id.main_tablayout);

        detailToMainReceiver=new DetailToMainReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.DetailToMainReceiver");
        registerReceiver(detailToMainReceiver, filter);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(detailToMainReceiver);
    }

    @Override
    protected void configViews()
    {
        super.configViews();

        Fragment restaurantFragment = RestaurantFragment.newInstance();

        remarkDict = (List<RestInfo.BodyBean.RemarkDictBean>) getIntent().getSerializableExtra("remarkDict");
        finishedTime = (List<RestInfo.BodyBean.FinishedTimeBean>) getIntent().getSerializableExtra("finishedTime");
        dictStatus = (List<RestInfo.BodyBean.DictStatusBean>) getIntent().getSerializableExtra("dictStatus");

        Bundle bundle = new Bundle();
        bundle.putSerializable("remarkDict", (Serializable) remarkDict);
        bundle.putSerializable("finishedTime", (Serializable) finishedTime);
        bundle.putSerializable("dictStatus", (Serializable) dictStatus);

        restaurantFragment.setArguments(bundle);

        fragmentList.add(restaurantFragment);

        Fragment orderFragment = OrderFragment.newInstance();
        fragmentList.add(orderFragment);

        Fragment fragment = MineFragment.newInstance();
        fragmentList.add(fragment);

        mainPagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager(), tabType, fragmentList);
        mainViewpager.setAdapter(mainPagerAdapter);

        mainTablayout.setTabMode(TabLayout.MODE_FIXED);
        mainTablayout.setupWithViewPager(mainViewpager);
        mainTablayout.setBackgroundColor(getResources().getColor(R.color.white));
        mainTablayout.setTabsFromPagerAdapter(mainPagerAdapter);
        for (int i = 0; i < tabType.length; i++)
        {
            TabLayout.Tab itemTab = mainTablayout.getTabAt(i);
            if (itemTab != null)
            {
                itemTab.setCustomView(R.layout.tab_item);
                View tabRoot = itemTab.getCustomView();
                ImageView tabImg = (ImageView) tabRoot.findViewById(R.id.tab_img);
                TextView tabText = (TextView) tabRoot.findViewById(R.id.tab_text);
                tabImg.setImageDrawable(getResources().getDrawable(tabSelector[i]));
                tabText.setText(tabType[i]);
            }
        }
        mainTablayout.getTabAt(0).getCustomView().setSelected(true);


    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.d(Constant.TAG, "MainActivity onNewIntent");
    }


    @Override
    public void onFragmentInteraction(int flag)
    {
        switch (flag)
        {
            case Constant.MSG_TO_ACT_ORDER_LIST:
                mainViewpager.setCurrentItem(1, true);
                OrderFragment orderFragment = (OrderFragment) mainPagerAdapter.getItem(1);
                orderFragment.setToOrderList();
                break;
            case Constant.MSG_TO_ACT_NEW_ORDER:
                mainViewpager.setCurrentItem(0, true);
                RestaurantFragment restaurantFragment = (RestaurantFragment) mainPagerAdapter.getItem(0);
                restaurantFragment.clear();
                break;

        }
    }
}
