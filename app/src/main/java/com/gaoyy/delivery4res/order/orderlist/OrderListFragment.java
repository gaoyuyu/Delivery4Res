package com.gaoyy.delivery4res.order.orderlist;


import android.content.Intent;
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
import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.base.BaseFragment;
import com.gaoyy.delivery4res.base.CustomDialogFragment;
import com.gaoyy.delivery4res.main.MainActivity;
import com.gaoyy.delivery4res.main.NewOrderDetailActivity;
import com.gaoyy.delivery4res.main.OrderNewActivity;
import com.gaoyy.delivery4res.util.CommonUtils;
import com.gaoyy.delivery4res.util.DialogUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class OrderListFragment extends BaseFragment implements OrderListContract.View, SwipeRefreshLayout.OnRefreshListener, OrderListAdapter.OnItemClickListener
{
    private static final String LOG_TAG = OrderListFragment.class.getSimpleName();
    private OrderListContract.Presenter mOrderListPresenter;

    private SwipeRefreshLayout commonSwipeRefreshLayout;
    private RecyclerView commonRv;
    private int lastVisibleItem;

    private int pageNo = 1;
    private int pageSize = 10;
    private int pageCount;

    private LinearLayoutManager linearLayoutManager;
    private OrderListAdapter orderListAdapter;
    private LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> orderList = new LinkedList<>();

    private CustomDialogFragment loading;

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
        commonSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.common_swipeRefreshLayout);
        commonRv = (RecyclerView) rootView.findViewById(R.id.common_rv);
    }


    @Override
    protected void configViews()
    {
        super.configViews();
        orderListAdapter = new OrderListAdapter(activity, orderList);
        commonRv.setAdapter(orderListAdapter);
        //设置布局
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        commonRv.setLayoutManager(linearLayoutManager);
        commonRv.setItemAnimator(new DefaultItemAnimator());

        CommonUtils.setSwipeLayoutProgressBackgroundColor(activity, commonSwipeRefreshLayout);


        //在onResume中加载数据
        pageNo = 1;
        Map<String, String> params = getOrderListParams(pageNo, pageSize);
        Log.d(Constant.TAG, "订单列表参数：" + params.toString());
        mOrderListPresenter.orderList(params, Constant.PULL_TO_REFRESH);
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
                        Map<String, String> params = getOrderListParams(pageNo, pageSize);
                        Log.d(Constant.TAG, "上拉加载更多，传递参数-->" + params.toString());
                        mOrderListPresenter.orderList(params, Constant.UP_TO_LOAD_MORE);
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
        //设置item的点击事件
        orderListAdapter.setOnItemClickListener(this);
    }

    @NonNull
    private Map<String, String> getOrderListParams(int pageNo, int pageSize)
    {
        Map<String, String> params = new HashMap<>();
        params.put("loginName", CommonUtils.getLoginName(activity));
        params.put("randomCode", CommonUtils.getRandomCode(activity));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        String orderNo = getArguments().getString("orderNo");
        String driverPhone = getArguments().getString("driverPhone");
        String customerPhone = getArguments().getString("customerPhone");
        String status = getArguments().getString("status", "");
        Log.d(Constant.TAG, "orderNo==>" + orderNo);
        Log.d(Constant.TAG, "driverPhone===>" + driverPhone);
        Log.d(Constant.TAG, "customerPhone==>" + customerPhone);
        Log.d(Constant.TAG, "status==>" + status);
        if (orderNo != null) params.put("orderNo", orderNo);
        if (driverPhone != null) params.put("courierTel", driverPhone);
        if (customerPhone != null) params.put("customerTel", customerPhone);
        if (!status.equals(""))
        {
            if (!status.equals("All"))
            {
                params.put("status", transformStatus(status));
            }
        }

        Log.d(Constant.TAG, "transformStatus(status)==>" + transformStatus(status));
        return params;
    }

    /**
     * 订单状态转换
     *
     * @param status
     * @return
     */
    private String transformStatus(String status)
    {
        String statusCode = "";
        List<RestInfo.BodyBean.DictStatusBean> dictStatus = MainActivity.dictStatus;
        for (int i = 0; i < dictStatus.size(); i++)
        {
            if (status.equals(dictStatus.get(i).getLabel()))
            {
                statusCode = dictStatus.get(i).getValue();
                break;
            }
        }
        return statusCode;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mOrderListPresenter == null) return;
        mOrderListPresenter.start();
//        //在onResume中加载数据
//        pageNo = 1;
//        Map<String, String> params = getOrderListParams(pageNo, pageSize);
//        Log.d(Constant.TAG, "订单列表参数：" + params.toString());
//        mOrderListPresenter.orderList(params, Constant.PULL_TO_REFRESH);
    }

    @Override
    public boolean isActive()
    {
        return isAdded();
    }


    @Override
    public void showOrderList(LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> orderList, int count)
    {
        orderListAdapter.updateData(orderList);
        pageCount = count;
    }

    @Override
    public void loadMoreOrderList(LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> orderList, int count)
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
    public void showLoading()
    {
        loading = DialogUtils.showLoadingDialog(activity);
    }

    @Override
    public void hideLoding()
    {
        loading.dismiss();
    }

    @Override
    public void singleItemUpdate(int position, OrderListInfo.BodyBean.PageBean.ListBean order)
    {
        orderListAdapter.singleItemUpdate(position,order);
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
        pageNo = 1;
        Map<String, String> params = getOrderListParams(pageNo, pageSize);
        Log.d(Constant.TAG, "下拉刷新，传递参数-->" + params.toString());
        mOrderListPresenter.orderList(params, Constant.PULL_TO_REFRESH);
    }

    @Override
    public void onItemClick(View view, int position, final OrderListInfo.BodyBean.PageBean.ListBean order)
    {
        int id = view.getId();
        Map<String,String> params = new HashMap<>();
        params.put("loginName",CommonUtils.getLoginName(activity));
        params.put("randomCode",CommonUtils.getRandomCode(activity));
        params.put("id",order.getId());
        switch (id)
        {
            case R.id.item_order_cancle_btn:
                mOrderListPresenter.orderStatusOperate(position, params,order,Constant.CANCLE);
                break;
            case R.id.item_order_resubmit_btn:
                mOrderListPresenter.orderStatusOperate(position, params,order,Constant.RESUBMIT);
                break;
            case R.id.item_order_delivery_btn:
                mOrderListPresenter.orderStatusOperate(position, params,order,Constant.DELIVERY);
                break;
            case R.id.item_order_cancle_after_delivery_btn:
                mOrderListPresenter.orderStatusOperate(position, params,order,Constant.CANCLE_AFTER_DELIVERY);
                break;
            case R.id.item_order_making_finish_btn:
                params.put("language", "zh");
                params.put("order_id", ""+order.getOrderId());
                mOrderListPresenter.orderStatusOperate(position, params,order,Constant.MAKING_FINISH);
                break;
            case R.id.item_order_cardview:
                if (order.getOrderType() == 1)
                {
                    Intent intent = new Intent(activity, OrderNewActivity.class);
                    intent.putExtra("order", order);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(activity, NewOrderDetailActivity.class);
                    intent.putExtra("order", order);
                    startActivity(intent);
                }
                break;
        }
    }
}