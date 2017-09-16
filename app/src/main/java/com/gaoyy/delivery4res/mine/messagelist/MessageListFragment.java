package com.gaoyy.delivery4res.mine.messagelist;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.adapter.MessageListAdapter;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.MessageListInfo;
import com.gaoyy.delivery4res.base.BaseFragment;
import com.gaoyy.delivery4res.util.CommonUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MessageListFragment extends BaseFragment implements  MessageListContract.View, SwipeRefreshLayout.OnRefreshListener
{
    private MessageListContract.Presenter mMessageListPresenter;
    private SwipeRefreshLayout commonSwipeRefreshLayout;
    private RecyclerView commonRv;

    private int lastVisibleItem;

    private int pageNo = 1;
    private int pageSize = 10;
    private int pageCount;

    private LinearLayoutManager linearLayoutManager;
    private MessageListAdapter messageListAdapter;
    private LinkedList<MessageListInfo.BodyBean.ListBean.ResultBean> messageList = new LinkedList<>();



    public MessageListFragment()
    {
        // Required empty public constructor
    }

    public static MessageListFragment newInstance()
    {
        MessageListFragment fragment = new MessageListFragment();
        return fragment;
    }


    @Override
    protected int getFragmentLayoutId()
    {
        return R.layout.fragment_message_list;
    }


    @Override
    protected void assignViews(View rootView)
    {
        super.assignViews(rootView);
        commonSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.common_swipeRefreshLayout);
        commonRv = (RecyclerView) rootView.findViewById(R.id.common_rv);

        messageListAdapter = new MessageListAdapter(activity, messageList);
        commonRv.setAdapter(messageListAdapter);
        //设置布局
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        commonRv.setLayoutManager(linearLayoutManager);
        commonRv.setItemAnimator(new DefaultItemAnimator());

        CommonUtils.setSwipeLayoutProgressBackgroundColor(activity, commonSwipeRefreshLayout);


        pageNo = 1;
        Map<String,String> params = getMessageListParams(pageNo,pageSize);
        Log.d(Constant.TAG, "站内消息列表参数：" + params.toString());
        mMessageListPresenter.messageList(params, Constant.PULL_TO_REFRESH);
    }

    @Override
    protected void setListener()
    {
        super.setListener();

        commonSwipeRefreshLayout.setOnRefreshListener(this);
        commonRv.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == messageListAdapter.getItemCount())
                {
                    //总共有多少页
                    int pageSum = 0;
                    pageNo = pageNo + 1;

                    if (pageCount % pageSize == 0)
                    {
                        pageSum = pageCount / pageSize;
                    }
                    else
                    {
                        pageSum = pageCount / pageSize + 1;
                    }
                    Log.d(Constant.TAG, "page sum-->" + pageSum);
                    Log.d(Constant.TAG, "page No-->" + pageNo);
                    if (pageNo <= pageSum)
                    {
                        Map<String, String> params = getMessageListParams(pageNo, pageSize);
                        Log.d(Constant.TAG, "上拉加载更多，传递参数-->" + params.toString());
                        mMessageListPresenter.messageList(params, Constant.UP_TO_LOAD_MORE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if (mMessageListPresenter == null) return;
        mMessageListPresenter.start();
    }
    public Map<String,String> getMessageListParams(int pageNo,int pageSize)
    {
        Map<String, String> params = new HashMap<>();
        params.put("loginName",CommonUtils.getLoginName(activity));
        params.put("randomCode",CommonUtils.getRandomCode(activity));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("language", "zh");

        return params;
    }

    @Override
    public boolean isActive()
    {
        return isAdded();
    }

    @Override
    public void showMessageList(LinkedList<MessageListInfo.BodyBean.ListBean.ResultBean> messageList, int count)
    {
        messageListAdapter.updateData(messageList);
        pageCount = count;
    }

    @Override
    public void loadMoreMessageList(LinkedList<MessageListInfo.BodyBean.ListBean.ResultBean> messageList, int count)
    {
        messageListAdapter.addMoreItem(messageList);
        pageCount = count;
    }

    @Override
    public void refreshing()
    {
        commonSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void finishRefesh()
    {
        commonSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showToast(String msg)
    {
        CommonUtils.showToast(activity, msg);
    }

    @Override
    public void showToast(int msgId)
    {
        CommonUtils.showToast(activity, msgId);
    }


    @Override
    public void setPresenter(MessageListContract.Presenter presenter)
    {
        if (presenter != null)
        {
            mMessageListPresenter = presenter;
        }
    }

    @Override
    public void onRefresh()
    {
        pageNo = 1;
        Map<String, String> params = getMessageListParams(pageNo, pageSize);
        Log.d(Constant.TAG, "下拉刷新，传递参数-->" + params.toString());
        mMessageListPresenter.messageList(params, Constant.PULL_TO_REFRESH);
    }
}
