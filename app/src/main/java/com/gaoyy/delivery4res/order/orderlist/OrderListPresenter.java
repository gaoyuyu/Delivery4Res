package com.gaoyy.delivery4res.order.orderlist;

import android.util.Log;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.OrderListInfo;
import com.gaoyy.delivery4res.api.bean.OrderOperationStatusInfo;
import com.gaoyy.delivery4res.util.CommonUtils;

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
    public void orderList(Call<OrderListInfo> call, Map<String, String> params, final int refreshTag)
    {
        CommonUtils.httpDebugLogger("餐厅订单列表");
        if(refreshTag == Constant.PULL_TO_REFRESH)
        {
            //下拉刷新中
            mOrderListView.refreshing();
        }

        //这里的作用是防止下拉刷新的时候还可以上拉加载
        mOrderListView.setEnableLoadMore(false);

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
                //刷新完毕，开启上拉加载
                mOrderListView.setEnableLoadMore(true);

                if (response.isSuccessful() && response.body() != null)
                {
                    OrderListInfo orderListInfo = response.body();
                    String msg = orderListInfo.getMsg();
                    String errorCode = orderListInfo.getErrorCode();
                    CommonUtils.httpDebugLogger("[isSuccess=" + orderListInfo.isSuccess() + "][errorCode=" + errorCode + "][msg=" + msg + "]");
                    if (orderListInfo.isSuccess())
                    {
                        LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> orderList = orderListInfo.getBody().getPage().getList();
                        if (refreshTag == Constant.PULL_TO_REFRESH)
                        {
                            Log.d(Constant.TAG, "==下拉刷新=数据量=" + orderList.size());
                            if(orderList.size() == 0)
                            {
                                mOrderListView.handleStatus(true,Constant.NO_DATA);
                            }
                            else
                            {
                                mOrderListView.showOrderList(orderList, orderListInfo.getBody().getPage().getCount());
                            }
                        }
                        else if (refreshTag == Constant.UP_TO_LOAD_MORE)
                        {
                            mOrderListView.loadMoreOrderList(orderList, orderListInfo.getBody().getPage().getCount());
                        }

                    }
                    else
                    {
                        mOrderListView.showToast(msg);
                        mOrderListView.handleStatus(false,refreshTag);
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
                //刷新完毕，开启上拉加载
                mOrderListView.setEnableLoadMore(true);

                mOrderListView.handleStatus(false,refreshTag);

                CommonUtils.httpErrorLogger(t.toString());

                if (!call.isCanceled())
                {
//                    mOrderListView.showToast(R.string.network_error);
                }
            }
        });
    }

    @Override
    public void orderStatusOperate(Call<OrderOperationStatusInfo> call, final int position, final OrderListInfo.BodyBean.PageBean.ListBean order, final int operation)
    {
        String log = "";
        switch (operation)
        {
            case Constant.CANCLE:
                log = "饭店订单取消";
                break;
            case Constant.CANCLE_AFTER_DELIVERY:
                log = "饭店退单请求";
                break;
            case Constant.RESUBMIT:
                log = "饭店resubmit请求";
                break;
            case Constant.DELIVERY:
                log = "饭店及司机订单派送请求";
                break;
            case Constant.MAKING_FINISH:
                log = "制作完成请求";
                break;
        }
        mOrderListView.showLoading();
        final String finalLog = log;
        call.enqueue(new Callback<OrderOperationStatusInfo>()
        {
            @Override
            public void onResponse(Call<OrderOperationStatusInfo> call, Response<OrderOperationStatusInfo> response)
            {
                if (!mOrderListView.isActive())
                {
                    return;
                }
                mOrderListView.hideLoding();
                if (response.isSuccessful() && response.body() != null)
                {
                    OrderOperationStatusInfo oosi = response.body();
                    String msg = oosi.getMsg();
                    String errorCode = oosi.getErrorCode();
                    CommonUtils.httpDebugLogger("[isSuccess=" + oosi.isSuccess() + "][errorCode=" + errorCode + "][msg=" + msg + "]");

                    mOrderListView.showToast(oosi.getMsg());
                    if (oosi.isSuccess() && oosi.getErrorCode().equals("-1"))
                    {
                        if (operation != Constant.MAKING_FINISH)
                        {
                            int status = oosi.getBody().getStatus();
                            //设置订单状态
                            order.setStatus(status);
                        }
                        else
                        {
                            //设置订单状态
                            order.setStatus(3);
                        }
                        //更新数据
                        mOrderListView.singleItemUpdate(position, order);
                    }
                }
                else
                {
                    mOrderListView.showToast(finalLog + "失败");
                }
            }

            @Override
            public void onFailure(Call<OrderOperationStatusInfo> call, Throwable t)
            {
                if (!mOrderListView.isActive())
                {
                    return;
                }
                mOrderListView.hideLoding();
                CommonUtils.httpErrorLogger(t.toString());
                if (!call.isCanceled())
                {
                    mOrderListView.showToast(R.string.network_error);
                }
            }
        });
    }

}
