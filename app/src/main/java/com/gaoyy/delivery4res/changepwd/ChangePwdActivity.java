package com.gaoyy.delivery4res.changepwd;

import android.support.v7.widget.Toolbar;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.util.ActivityUtils;


public class ChangePwdActivity extends BaseActivity
{
    private Toolbar changePwdToolbar;


    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_change_pwd);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        changePwdToolbar = (Toolbar) findViewById(R.id.change_pwd_toolbar);
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar(changePwdToolbar, "修改密码", true, -1);
    }

    @Override
    protected void configViews()
    {
        super.configViews();

        ChangePwdFragment changePwdFragment = (ChangePwdFragment) getSupportFragmentManager().findFragmentById(R.id.change_pwd_content);
        if (changePwdFragment == null)
        {
            changePwdFragment = ChangePwdFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),changePwdFragment, R.id.change_pwd_content);
        }
        new ChangePwdPresenter(changePwdFragment);
    }
}
