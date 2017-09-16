package com.gaoyy.delivery4res.order.replylist;

import com.gaoyy.delivery4res.api.bean.ReplyOrderListInfo;
import com.gaoyy.delivery4res.base.BasePresenter;
import com.gaoyy.delivery4res.base.BaseView;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by gaoyy on 2017/9/12 0012.
 */

public class ReplyListContract
{
    interface View extends BaseView<Presenter>
    {
        boolean isActive();

        /**
         * 下拉刷新
         *
         * @param replyOrderList 数据
         * @param count          一共数据量
         */
        void showReplyOrderList(LinkedList<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> replyOrderList, int count);

        /**
         * 上拉加载更多
         *
         * @param replyOrderList 数据
         * @param count          一共数据量
         */
        void loadMoreReplyOrderList(LinkedList<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> replyOrderList, int count);

        void refreshing();

        void finishRefesh();

        void showToast(String msg);

        void showToast(int msgId);

        void showLoading();

        void hideLoading();

        void removeSingleItem(int position);
    }

    interface Presenter extends BasePresenter
    {
        void replyOrderList(Map<String, String> params, int refreshTag);
        void replyOrder(int position,Map<String,String> params);
    }
}
