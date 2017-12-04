package com.gaoyy.delivery4res.mine.messagelist;

import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.MessageListInfo;
import com.gaoyy.delivery4res.util.CommonUtils;

import java.util.LinkedList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gaoyy on 2017/9/9 0009.
 */

public class MessageListPresenter implements MessageListContract.Presenter
{

    private MessageListContract.View mMessageListView;

    public MessageListPresenter(MessageListContract.View mMessageListView)
    {
        this.mMessageListView = mMessageListView;
        mMessageListView.setPresenter(this);
    }

    @Override
    public void start()
    {

    }

    @Override
    public void messageList(Call<MessageListInfo> call,Map<String, String> params, final int refreshTag)
    {
        CommonUtils.httpDebugLogger("站内消息列表");
        mMessageListView.refreshing();

        call.enqueue(new Callback<MessageListInfo>()
        {
            @Override
            public void onResponse(Call<MessageListInfo> call, Response<MessageListInfo> response)
            {
                if (!mMessageListView.isActive())
                {
                    return;
                }

                //停止刷新
                mMessageListView.finishRefesh();

                if (response.isSuccessful() && response.body() != null)
                {
                    MessageListInfo messageListInfo = response.body();
                    boolean isSuccess = messageListInfo.isSuccess();
                    String errorCode = messageListInfo.getErrorCode();
                    CommonUtils.httpDebugLogger("[isSuccess="+isSuccess+"][errorCode=" + errorCode + "]");
                    if (isSuccess)
                    {
                        LinkedList<MessageListInfo.BodyBean.ListBean.ResultBean> messageList = messageListInfo.getBody().getList().getResult();
                        if(refreshTag == Constant.PULL_TO_REFRESH)
                        {
                            mMessageListView.showMessageList(messageList,messageListInfo.getBody().getList().getPages());
                        }
                        else
                        {
                            mMessageListView.loadMoreMessageList(messageList,messageListInfo.getBody().getList().getPages());
                        }

                    }
                    else
                    {
//                        mMessageListView.showToast("请求失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageListInfo> call, Throwable t)
            {
                if (!mMessageListView.isActive())
                {
                    return;
                }
                //停止刷新
                mMessageListView.finishRefesh();
                CommonUtils.httpErrorLogger(t.toString());
                if (!call.isCanceled())
                {
//                    mMessageListView.showToast(R.string.network_error);
                }
            }
        });
    }
}
