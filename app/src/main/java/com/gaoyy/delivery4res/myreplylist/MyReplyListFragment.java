package com.gaoyy.delivery4res.myreplylist;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.adapter.MyReplyListAdapter;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.RetrofitService;
import com.gaoyy.delivery4res.api.bean.MyReplyListInfo;
import com.gaoyy.delivery4res.base.BaseFragment;
import com.gaoyy.delivery4res.util.CommonUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import retrofit2.Call;

public class MyReplyListFragment extends BaseFragment implements  MyReplyListContract.View, SwipeRefreshLayout.OnRefreshListener
{

    private static final String LOG_TAG = MyReplyListFragment.class.getSimpleName();
    private MyReplyListContract.Presenter mMyReplyListPresenter;
    private SwipeRefreshLayout commonSwipeRefreshLayout;
    private RecyclerView commonRv;

    private int lastVisibleItem;

    private int pageNo = 1;
    private int pageSize = Constant.PAGE_SIZE;
    private int pageCount;

    private LinearLayoutManager linearLayoutManager;
    private MyReplyListAdapter myReplyListAdapter;
    private LinkedList<MyReplyListInfo.BodyBean.ListBean.ResultBean> myReplyList = new LinkedList<>();

    private Call<MyReplyListInfo> call;
    public MyReplyListFragment()
    {
        // Required empty public constructor
    }

    public static MyReplyListFragment newInstance()
    {
        MyReplyListFragment fragment = new MyReplyListFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayoutId()
    {
        return R.layout.fragment_my_reply_list;
    }

    @Override
    protected void assignViews(View rootView)
    {
        super.assignViews(rootView);
        commonSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.common_swipeRefreshLayout);
        commonRv = (RecyclerView) rootView.findViewById(R.id.common_rv);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        myReplyListAdapter = new MyReplyListAdapter(activity, myReplyList);
        commonRv.setAdapter(myReplyListAdapter);
        //设置布局
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        commonRv.setLayoutManager(linearLayoutManager);
        commonRv.setItemAnimator(new DefaultItemAnimator());

        CommonUtils.setSwipeLayoutProgressBackgroundColor(activity, commonSwipeRefreshLayout);
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == myReplyListAdapter.getItemCount())
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
                        Map<String, String> params = getMyReplyListParams(pageNo, pageSize);
                        Log.d(Constant.TAG, "上拉加载更多，传递参数-->" + params.toString());
                        call = RetrofitService.sApiService.myReplyList(params);
                        mMyReplyListPresenter.myReplyList(call,params, Constant.UP_TO_LOAD_MORE);
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
        if (mMyReplyListPresenter == null) return;
        mMyReplyListPresenter.start();
        //在onResume中加载数据
        pageNo = 1;
        Map<String,String> params = getMyReplyListParams(pageNo,pageSize);
        Log.d(Constant.TAG, "我的回复列表参数：" + params.toString());
        call = RetrofitService.sApiService.myReplyList(params);
        mMyReplyListPresenter.myReplyList(call,params, Constant.PULL_TO_REFRESH);
    }

    public Map<String,String> getMyReplyListParams(int pageNo,int pageSize)
    {
        Map<String, String> params = new HashMap<>();
        params.put("loginName",CommonUtils.getLoginName(activity));
        params.put("randomCode",CommonUtils.getRandomCode(activity));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("language", CommonUtils.getSysLanguage());

        return params;
    }


    @Override
    public boolean isActive()
    {
        return isAdded();
    }

    @Override
    public void showMyReplyList(LinkedList<MyReplyListInfo.BodyBean.ListBean.ResultBean> myReplyList, int count)
    {
        myReplyListAdapter.updateData(myReplyList);
        pageCount = count;
    }

    @Override
    public void loadMoreMyReplyList(LinkedList<MyReplyListInfo.BodyBean.ListBean.ResultBean> myReplyList, int count)
    {
        myReplyListAdapter.addMoreItem(myReplyList);
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
    public void setPresenter(MyReplyListContract.Presenter presenter)
    {
        Log.i(Constant.TAG, LOG_TAG + "  setPresenter");
        if (presenter != null)
        {
            mMyReplyListPresenter = presenter;
        }
    }

    @Override
    public void onRefresh()
    {
        pageNo = 1;
        Map<String, String> params = getMyReplyListParams(pageNo, pageSize);
        Log.d(Constant.TAG, "下拉刷新，传递参数-->" + params.toString());
        call = RetrofitService.sApiService.myReplyList(params);
        mMyReplyListPresenter.myReplyList(call,params, Constant.PULL_TO_REFRESH);
    }
}
