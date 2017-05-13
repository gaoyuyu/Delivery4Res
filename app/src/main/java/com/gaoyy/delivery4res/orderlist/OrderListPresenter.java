package com.gaoyy.delivery4res.orderlist;

import android.util.Log;

import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.RetrofitService;
import com.gaoyy.delivery4res.api.bean.OrderListInfo;

import java.util.LinkedList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gaoyy on 2017/5/6 0006.
 */

public class OrderListPresenter implements OrderListContract.Presenter
{
    private OrderListContract.View mOrderListView;

    public OrderListPresenter(OrderListContract.View mOrderListView)
    {
        this.mOrderListView = mOrderListView;
        mOrderListView.setPresenter(this);
    }

    @Override
    public void start()
    {
        Log.i(Constant.TAG, "OrderList start");
    }

    @Override
    public void orderList(Map<String, String> params, final int refreshTag)
    {
        Call<OrderListInfo> call = RetrofitService.sApiService.orderList(params);

        mOrderListView.refreshing();

        call.enqueue(new Callback<OrderListInfo>()
        {
            @Override
            public void onResponse(Call<OrderListInfo> call, Response<OrderListInfo> response)
            {
                if (!mOrderListView.isActive())
                {
                    return;
                }

                //停止刷新
                mOrderListView.finishRefesh();

                if (response.isSuccessful() && response.body() != null)
                {
                    OrderListInfo orderListInfo = response.body();
                    if (orderListInfo.isSuccess())
                    {
                        LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> orderList = orderListInfo.getBody().getPage().getList();
                        if(refreshTag == OrderListFragment.PULL_TO_REFRESH)
                        {
                            mOrderListView.showOrderList(orderList,orderListInfo.getBody().getPage().getCount());
                        }
                        else
                        {
                            mOrderListView.loadMoreOrderList(orderList,orderListInfo.getBody().getPage().getCount());
                        }

                    }
                    else
                    {
                        // TODO: 2017/5/13 0013 这里应该判断为false的时候，各种处理情况
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderListInfo> call, Throwable t)
            {
                if (!mOrderListView.isActive())
                {
                    return;
                }
                //停止刷新
                mOrderListView.finishRefesh();
            }
        });
    }
}
