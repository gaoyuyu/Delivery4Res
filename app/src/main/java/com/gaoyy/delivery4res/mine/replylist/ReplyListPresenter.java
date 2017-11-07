package com.gaoyy.delivery4res.mine.replylist;

import android.util.Log;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.CommonInfo;
import com.gaoyy.delivery4res.api.bean.ReplyOrderListInfo;
import com.gaoyy.delivery4res.util.CommonUtils;

import java.util.LinkedList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gaoyy on 2017/9/12 0012.
 */

public class ReplyListPresenter implements ReplyListContract.Presenter
{
    private ReplyListContract.View mReplyListView;

    public ReplyListPresenter(ReplyListContract.View mReplyListView)
    {
        this.mReplyListView = mReplyListView;
        mReplyListView.setPresenter(this);
    }

    @Override
    public void start()
    {
        Log.i(Constant.TAG, "ReplyOrderList start");
    }

    @Override
    public void replyOrderList(Call<ReplyOrderListInfo> call, Map<String, String> params, final int refreshTag)
    {
        CommonUtils.httpDebugLogger("待回复列表");
        mReplyListView.refreshing();
        call.enqueue(new Callback<ReplyOrderListInfo>()
        {
            @Override
            public void onResponse(Call<ReplyOrderListInfo> call, Response<ReplyOrderListInfo> response)
            {
                if (!mReplyListView.isActive())
                {
                    return;
                }

                mReplyListView.finishRefesh();
                if (response.isSuccessful() && response.body() != null)
                {
                    ReplyOrderListInfo replyOrderListInfo = response.body();
                    boolean success = replyOrderListInfo.isSuccess();
                    String errorCode = replyOrderListInfo.getErrorCode();
                    CommonUtils.httpDebugLogger("[isSuccess=" + success + "][errorCode=" + errorCode + "]");
                    if (success)
                    {
                        LinkedList<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> replyOorderList = replyOrderListInfo.getBody().getList().getResult();
                        if (refreshTag == Constant.PULL_TO_REFRESH)
                        {
                            mReplyListView.showReplyOrderList(replyOorderList, replyOrderListInfo.getBody().getList().getPages());
                        }
                        else
                        {
                            mReplyListView.loadMoreReplyOrderList(replyOorderList, replyOrderListInfo.getBody().getList().getPages());
                        }

                    }
                    else
                    {
                        mReplyListView.showToast(R.string.network_error);
                    }
                }
            }

            @Override
            public void onFailure(Call<ReplyOrderListInfo> call, Throwable t)
            {
                if (!mReplyListView.isActive())
                {
                    return;
                }
                //停止刷新
                mReplyListView.finishRefesh();
                CommonUtils.httpErrorLogger("reply   " + t.toString());
            }
        });

    }

    @Override
    public void replyOrder(Call<CommonInfo> call, final int position, Map<String, String> params)
    {
        CommonUtils.httpDebugLogger("回复订单");
        CommonUtils.httpDebugLogger("回复订单参数：" + params.toString());
        mReplyListView.showLoading();
        call.enqueue(new Callback<CommonInfo>()
        {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response)
            {

                if (!mReplyListView.isActive())
                {
                    return;
                }
                mReplyListView.hideLoading();
                if (response.isSuccessful() && response.body() != null)
                {
                    CommonInfo commonInfo = response.body();
                    mReplyListView.showToast(commonInfo.getMsg());
                    if (commonInfo.isSuccess())
                    {
                        //移除item
                        mReplyListView.removeSingleItem(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t)
            {
                if (!mReplyListView.isActive())
                {
                    return;
                }
                mReplyListView.hideLoading();
                CommonUtils.httpErrorLogger(t.toString());
                if (!call.isCanceled())
                {
                    mReplyListView.showToast(R.string.network_error);
                }
            }
        });
    }
}
