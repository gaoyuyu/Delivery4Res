package com.gaoyy.delivery4res.orderlist;


import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.adapter.OrderListAdapter;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.OrderListInfo;
import com.gaoyy.delivery4res.base.BaseFragment;
import com.gaoyy.delivery4res.util.CommonUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class OrderListFragment extends BaseFragment implements OrderListContract.View, SwipeRefreshLayout.OnRefreshListener
{
    //下拉刷新标识
    public static final int PULL_TO_REFRESH = 400;
    //上拉加载更多标识
    public static final int UP_TO_LOAD_MORE = 500;

    private OrderListContract.Presenter mOrderListPresenter;
    private static final String LOG_TAG = OrderListFragment.class.getSimpleName();


    private ProgressWheel commonProgresswheel;
    private SwipeRefreshLayout commonSwipeRefreshLayout;
    private RecyclerView commonRv;
    private int lastVisibleItem;


    private int pageNo = 1;
    private int pageSize = 3;
    private int pageCount;

    private LinearLayoutManager linearLayoutManager;
    private OrderListAdapter orderListAdapter;
    private LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> orderList = new LinkedList<>();


    public OrderListFragment()
    {
        // Required empty public constructor
    }

    public static OrderListFragment newInstance()
    {
        OrderListFragment fragment = new OrderListFragment();
        return fragment;
    }


    @Override
    protected int getFragmentLayoutId()
    {
        return R.layout.fragment_order_list;
    }


    @Override
    protected void assignViews(View rootView)
    {
        super.assignViews(rootView);
        commonProgresswheel = (ProgressWheel) rootView.findViewById(R.id.common_progresswheel);
        commonSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.common_swipeRefreshLayout);
        commonRv = (RecyclerView) rootView.findViewById(R.id.common_rv);
    }


    @Override
    protected void configViews()
    {
        super.configViews();
        orderListAdapter = new OrderListAdapter(activity,orderList);
        commonRv.setAdapter(orderListAdapter);
        //设置布局
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        commonRv.setLayoutManager(linearLayoutManager);
        commonRv.setItemAnimator(new DefaultItemAnimator());

        CommonUtils.setSwipeLayoutProgressBackgroundColor(activity,commonSwipeRefreshLayout);

        Map<String, String> params = getOrderListParams(pageNo,pageSize);
        Log.d(Constant.TAG,params.toString());
        mOrderListPresenter.orderList(params,PULL_TO_REFRESH);

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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == orderListAdapter.getItemCount())
                {
                    //总共有多少页
                    int pageSum = 0;
                    pageNo = pageNo+1;

                    if(pageCount%pageSize == 0)
                    {
                        pageSum = pageCount/pageSize;
                    }
                    else
                    {
                        pageSum = pageCount/pageSize+1;
                    }
                    Log.d(Constant.TAG,"page sum-->"+pageSum);
                    Log.d(Constant.TAG,"page No-->"+pageNo);
                    if(pageNo<= pageSum)
                    {
                        Map<String, String> params = getOrderListParams(pageNo, pageSize);
                        Log.d(Constant.TAG, "上拉加载更多，传递参数-->" + params.toString());
                        mOrderListPresenter.orderList(params,UP_TO_LOAD_MORE);
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

    @NonNull
    private Map<String, String> getOrderListParams(int pageNo,int pageSize)
    {
        Map<String,String> params = new HashMap<>();
        params.put("loginName", CommonUtils.getLoginName(activity));
        params.put("randomCode",CommonUtils.getRandomCode(activity));
        params.put("pageNo",String.valueOf(pageNo));
        params.put("pageSize",String.valueOf(pageSize));
        return params;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mOrderListPresenter.start();
    }

    @Override
    public boolean isActive()
    {
        return isAdded();
    }


    @Override
    public void showOrderList(LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> orderList,int count)
    {
        orderListAdapter.updateData(orderList);
        pageCount = count;
    }

    @Override
    public void loadMoreOrderList(LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> orderList,int count)
    {
        orderListAdapter.addMoreItem(orderList);
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
    public void setPresenter(OrderListContract.Presenter presenter)
    {
        Log.i(Constant.TAG, LOG_TAG + "  setPresenter");
        if (presenter != null)
        {
            mOrderListPresenter = presenter;
        }
    }

    @Override
    public void onRefresh()
    {
        Map<String, String> params = getOrderListParams(1,pageSize);
        Log.d(Constant.TAG,"下拉刷新，传递参数-->"+params.toString());
        mOrderListPresenter.orderList(params,PULL_TO_REFRESH);
    }
}
