package com.gaoyy.delivery4res.mine.messagelist;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.util.ActivityUtils;

public class MessageListActivity extends BaseActivity
{
    private Toolbar messageListToolbar;
    private FrameLayout messageListContent;

    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_message_list);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        messageListToolbar = (Toolbar) findViewById(R.id.message_list_toolbar);
        messageListContent = (FrameLayout) findViewById(R.id.message_list_content);
    }


    @Override
    protected void initToolbar()
    {
        super.initToolbar(messageListToolbar, R.string.toolbar_title_message, true, -1);
    }

    @Override
    protected void configViews()
    {
        super.configViews();


        MessageListFragment messageListFragment = (MessageListFragment) getSupportFragmentManager().findFragmentById(R.id.message_list_content);
        if (messageListFragment == null)
        {
            messageListFragment = MessageListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), messageListFragment, R.id.message_list_content);
        }
        new MessageListPresenter(messageListFragment);
/*

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("loginName", CommonUtils.getLoginName(this))
                .add("randomCode", CommonUtils.getRandomCode(this))
                .add("pageNo", "1")
                .add("pageSize", "1")
                .add("language", "zh")
                .build();

        Request request = new Request.Builder()
                .url("http://www.menualliance.com/jeeplus25/a/sys/user/pc/messageList")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback()
        {
            @Override
            public void onFailure(okhttp3.Call call, IOException e)
            {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException
            {
                try (ResponseBody responseBody = response.body())
                {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++)
                    {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
                }
            }
        });
        */

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
