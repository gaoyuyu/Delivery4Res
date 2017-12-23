package com.gaoyy.delivery4res.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.adapter.MainPagerAdapter;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.event.OrderDetailEvent;
import com.gaoyy.delivery4res.home.RestaurantFragment;
import com.gaoyy.delivery4res.mine.MineFragment;
import com.gaoyy.delivery4res.order.OrderFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MainActivity extends BaseActivity
{
    private ViewPager mainViewpager;
    private TabLayout mainTablayout;

    private int[] tabType = {R.string.tab_home, R.string.tab_order, R.string.tab_mine};
    private int[] tabSelector = {R.drawable.selector_home, R.drawable.selector_order, R.drawable.selector_account};

    public static List<RestInfo.BodyBean.RemarkDictBean> remarkDict;
    public static List<RestInfo.BodyBean.FinishedTimeBean> finishedTime;
    public static List<RestInfo.BodyBean.DictStatusBean> dictStatus;

    private MainPagerAdapter mainPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();


    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void assignViews()
    {
        super.assignViews();
        mainViewpager = (ViewPager) findViewById(R.id.main_viewpager);
        mainTablayout = (TabLayout) findViewById(R.id.main_tablayout);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

        Fragment mineFragment = MineFragment.newInstance();
        fragmentList.add(mineFragment);

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


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onOrderDetailEvent(OrderDetailEvent event)
    {
        //tag=1 | 回到订单列表，2| 清除已填写的订单信息
        switch (event.getTag())
        {
            case Constant.BACK_TO_ORDER_LIST:
                mainViewpager.setCurrentItem(1, true);
                OrderFragment order = (OrderFragment) mainPagerAdapter.getItem(1);
                order.setToOrderList();
                break;
            case Constant.CLEAR_ORDER_INFO:
                mainViewpager.setCurrentItem(0, true);
                RestaurantFragment res = (RestaurantFragment) mainPagerAdapter.getItem(0);
                res.clear();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.d(Constant.TAG, "MainActivity onNewIntent");
    }
}
