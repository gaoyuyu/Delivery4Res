package com.gaoyy.delivery4res.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.RetrofitService;
import com.gaoyy.delivery4res.api.bean.CommonInfo;
import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.base.CustomDialogFragment;
import com.gaoyy.delivery4res.changepwd.ChangePwdActivity;
import com.gaoyy.delivery4res.login.LoginActivity;
import com.gaoyy.delivery4res.main.restaurant.RestaurantFragment;
import com.gaoyy.delivery4res.orderlist.OrderListActivity;
import com.gaoyy.delivery4res.util.ActivityUtils;
import com.gaoyy.delivery4res.util.CommonUtils;
import com.gaoyy.delivery4res.util.DialogUtils;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
{


    // TODO: 2017/5/20 0020 需要加双击2次退出
    private Toolbar mainToolbar;
    private FloatingActionButton mainFab;

    private DrawerLayout mainDrawerLayout;
    private NavigationView mainNavView;


    public static List<RestInfo.BodyBean.RemarkDictBean> remarkDict;
    public static List<RestInfo.BodyBean.FinishedTimeBean> finishedTime;
    public static List<RestInfo.BodyBean.DictStatusBean> dictStatus;


    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mainFab = (FloatingActionButton) findViewById(R.id.main_fab);
        mainDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mainNavView = (NavigationView) findViewById(R.id.main_nav_view);
    }

    @Override
    protected void initToolbar()
    {
        String name = CommonUtils.getName(this);
        super.initToolbar(mainToolbar, name, false, -1);
    }

    @Override
    protected void configViews()
    {
        super.configViews();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainDrawerLayout, mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        View header = mainNavView.getHeaderView(0);
        TextView tv = (TextView) header.findViewById(R.id.header_username);
        tv.setText("Welcome," + CommonUtils.getName(this));

        mainNavView.setNavigationItemSelectedListener(this);


        RestaurantFragment restaurantFragment = RestaurantFragment.newInstance();

        remarkDict = (List<RestInfo.BodyBean.RemarkDictBean>) getIntent().getSerializableExtra("remarkDict");
        finishedTime = (List<RestInfo.BodyBean.FinishedTimeBean>) getIntent().getSerializableExtra("finishedTime");
        dictStatus = (List<RestInfo.BodyBean.DictStatusBean>) getIntent().getSerializableExtra("dictStatus");

        Bundle bundle = new Bundle();
        bundle.putSerializable("remarkDict",(Serializable)remarkDict);
        bundle.putSerializable("finishedTime",(Serializable)finishedTime);
        bundle.putSerializable("dictStatus",(Serializable)dictStatus);

        restaurantFragment.setArguments(bundle);


        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), restaurantFragment, R.id.main_content);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.d(Constant.TAG,"MainActivity onNewIntent");
    }

    @Override
    public void onBackPressed()
    {
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mainDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_order_list:
                Intent orderList = new Intent();
                orderList.setClass(MainActivity.this, OrderListActivity.class);
                startActivity(orderList);
                break;
            case R.id.nav_change_pwd:
                Intent changePwd = new Intent();
                changePwd.setClass(MainActivity.this, ChangePwdActivity.class);
                startActivity(changePwd);
                break;
            case R.id.nav_exit:
                String loginName = CommonUtils.getLoginName(this);
                String randomCode = CommonUtils.getRandomCode(this);
                logout(loginName, randomCode);
                break;
        }

        mainDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 用户退出
     *
     * @param loginName
     * @param randomCode
     */
    private void logout(String loginName, String randomCode)
    {
        Call<CommonInfo> call = RetrofitService.sApiService.logout(loginName, randomCode);
        CommonUtils.httpDebugLogger("退出请求");
        final CustomDialogFragment loading = DialogUtils.showLoadingDialog(this);
        call.enqueue(new Callback<CommonInfo>()
        {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response)
            {
                loading.dismiss();
                if (response.isSuccessful() && response.body() != null)
                {
                    CommonInfo logoutInfo = response.body();
                    String msg = logoutInfo.getMsg();
                    String errorCode = logoutInfo.getErrorCode();
                    CommonUtils.httpDebugLogger("[isSuccess="+logoutInfo.isSuccess()+"][errorCode=" + errorCode + "][msg=" + msg + "]");
                    CommonUtils.showToast(MainActivity.this, msg);
                    if (errorCode.equals("-1"))
                    {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t)
            {
                loading.dismiss();
                CommonUtils.httpErrorLogger(t.toString());
            }
        });
    }
}
