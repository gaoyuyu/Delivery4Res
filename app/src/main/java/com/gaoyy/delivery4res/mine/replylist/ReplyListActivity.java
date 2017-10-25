package com.gaoyy.delivery4res.mine.replylist;


import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.util.ActivityUtils;

public class ReplyListActivity extends BaseActivity
{
    private Toolbar replyToolbar;

    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_reply_list);
    }


    @Override
    protected void assignViews()
    {
        super.assignViews();
        replyToolbar = (Toolbar) findViewById(R.id.reply_toolbar);
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar(replyToolbar, R.string.tab_reply_list, true, -1);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        ReplyListFragment replyListFragment = (ReplyListFragment) getSupportFragmentManager().findFragmentById(R.id.reply_list_content);
        if (replyListFragment == null)
        {
            replyListFragment = ReplyListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), replyListFragment, R.id.reply_list_content);
        }
        new ReplyListPresenter(replyListFragment);


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
