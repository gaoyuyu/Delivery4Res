package com.gaoyy.delivery4res.orderlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.main.SearchOrderActivity;
import com.gaoyy.delivery4res.util.ActivityUtils;

public class OrderListActivity extends BaseActivity
{

    private LinearLayout activityOrderList;
    private Toolbar orderListToolbar;
    private FrameLayout orderListContent;


    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_order_list);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        activityOrderList = (LinearLayout) findViewById(R.id.activity_order_list);
        orderListToolbar = (Toolbar) findViewById(R.id.order_list_toolbar);
        orderListContent = (FrameLayout) findViewById(R.id.order_list_content);
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar(orderListToolbar, R.string.toolbar_title_order_list, true, -1);
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.d(Constant.TAG,"OrderListActivity onNewIntent");
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        String orderNo = getIntent().getStringExtra("orderNo");
        String driverPhone = getIntent().getStringExtra("driverPhone");
        String customerPhone = getIntent().getStringExtra("customerPhone");
        String status = getIntent().getStringExtra("status");

        OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentById(R.id.order_list_content);
        if (orderListFragment == null)
        {
            orderListFragment = OrderListFragment.newInstance();
            Bundle bundle = new Bundle();
            if(orderNo!=null) bundle.putString("orderNo",orderNo);
            if(driverPhone!=null) bundle.putString("driverPhone",driverPhone);
            if(customerPhone!=null) bundle.putString("customerPhone",customerPhone);
            if(status!=null) bundle.putString("status",status);
            orderListFragment.setArguments(bundle);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), orderListFragment, R.id.order_list_content);
        }
        new OrderListPresenter(orderListFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.order_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search_order:
                Intent intent = new Intent(OrderListActivity.this, SearchOrderActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
