package com.gaoyy.delivery4res.order.orderlist;

import com.gaoyy.delivery4res.api.bean.OrderListInfo;
import com.gaoyy.delivery4res.base.BasePresenter;
import com.gaoyy.delivery4res.base.BaseView;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by gaoyy on 2017/5/13 0013.
 */

public class OrderListContract
{
    interface View extends BaseView<Presenter>
    {
        boolean isActive();

        /**
         * 下拉刷新
         * @param orderList 数据
         * @param count 一共数据量
         */
        void showOrderList(LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> orderList,int count);

        /**
         *上拉加载更多
         * @param orderList 数据
         * @param count 一共数据量
         */
        void loadMoreOrderList(LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> orderList,int count);

        void refreshing();

        void finishRefesh();

        void showToast(String msg);
        void showToast(int msgId);

        void showLoading();
        void hideLoding();

        void singleItemUpdate(int position,OrderListInfo.BodyBean.PageBean.ListBean order);


    }

    interface Presenter extends BasePresenter
    {
        void  orderList(Map<String,String>params,int refreshTag);

        void orderStatusOperate(int position,Map<String,String> params,OrderListInfo.BodyBean.PageBean.ListBean order,int operation);
    }
}
