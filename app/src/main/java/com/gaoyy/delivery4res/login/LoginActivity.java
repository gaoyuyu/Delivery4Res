package com.gaoyy.delivery4res.login;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.util.ActivityUtils;


public class LoginActivity extends BaseActivity
{
    private LinearLayout activityLogin;
    private Toolbar loginToolbar;
    private FrameLayout loginContent;


    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        activityLogin = (LinearLayout) findViewById(R.id.activity_login);
        loginToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        loginContent = (FrameLayout) findViewById(R.id.login_content);
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar(loginToolbar,R.string.login,false,-1);
    }

    @Override
    protected void configViews()
    {
        super.configViews();

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.login_content);
        if (loginFragment == null)
        {
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),loginFragment, R.id.login_content);
        }
        new LoginPresenter(loginFragment);
    }
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.d(Constant.TAG,"LoginActivity onNewIntent");
    }
}
