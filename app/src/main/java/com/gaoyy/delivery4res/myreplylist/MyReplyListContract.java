package com.gaoyy.delivery4res.myreplylist;

import com.gaoyy.delivery4res.api.bean.MyReplyListInfo;
import com.gaoyy.delivery4res.base.BasePresenter;
import com.gaoyy.delivery4res.base.BaseView;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by gaoyy on 2017/9/13 0013.
 */

public class MyReplyListContract
{

    interface View extends BaseView<Presenter>
    {
        boolean isActive();

        /**
         * 下拉刷新
         * @param myReplyList 数据
         * @param count 一共数据量
         */
        void showMyReplyList(LinkedList<MyReplyListInfo.BodyBean.ListBean.ResultBean> myReplyList, int count);

        /**
         *上拉加载更多
         * @param myReplyList 数据
         * @param count 一共数据量
         */
        void loadMoreMyReplyList(LinkedList<MyReplyListInfo.BodyBean.ListBean.ResultBean> myReplyList,int count);

        void refreshing();

        void finishRefesh();

        void showToast(String msg);
        void showToast(int msgId);

    }

    interface Presenter extends BasePresenter
    {
        void  myReplyList(Map<String,String> params, int refreshTag);
    }
}
