package com.gaoyy.delivery4res.myreplylist;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.util.ActivityUtils;

/**
 * Created by gaoyy on 2017/9/13 0013.
 */

public class MyReplyListActivity extends BaseActivity
{
    private Toolbar myReplyListToolbar;

    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_my_reply_list);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        myReplyListToolbar = (Toolbar) findViewById(R.id.my_reply_list_toolbar);
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar(myReplyListToolbar, R.string.toolbar_title_my_reply, true, -1);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        MyReplyListFragment myReplyListFragment = (MyReplyListFragment) getSupportFragmentManager().findFragmentById(R.id.my_reply_list_content);
        if (myReplyListFragment == null)
        {
            myReplyListFragment = MyReplyListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),myReplyListFragment, R.id.my_reply_list_content);
        }
        new MyReplyListPresenter(myReplyListFragment);


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
        }
        return super.onOptionsItemSelected(item);
    }
}
