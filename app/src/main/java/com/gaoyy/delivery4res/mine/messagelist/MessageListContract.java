package com.gaoyy.delivery4res.mine.messagelist;

import com.gaoyy.delivery4res.api.bean.MessageListInfo;
import com.gaoyy.delivery4res.base.BasePresenter;
import com.gaoyy.delivery4res.base.BaseView;

import java.util.LinkedList;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by gaoyy on 2017/9/9 0009.
 */

public class MessageListContract
{
    interface View extends BaseView<Presenter>
    {
        boolean isActive();

        /**
         * 下拉刷新
         * @param messageList 数据
         * @param count 一共数据量
         */
        void showMessageList(LinkedList<MessageListInfo.BodyBean.ListBean.ResultBean> messageList, int count);

        /**
         *上拉加载更多
         * @param messageList 数据
         * @param count 一共数据量
         */
        void loadMoreMessageList(LinkedList<MessageListInfo.BodyBean.ListBean.ResultBean> messageList,int count);

        void refreshing();

        void finishRefesh();

        void showToast(String msg);
        void showToast(int msgId);

    }

    interface Presenter extends BasePresenter
    {
        void  messageList(Call<MessageListInfo> call,Map<String,String> params, int refreshTag);
    }
}
