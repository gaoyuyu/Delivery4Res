package com.gaoyy.delivery4res.myreplylist;

import android.util.Log;

import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.MyReplyListInfo;
import com.gaoyy.delivery4res.util.CommonUtils;

import java.util.LinkedList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gaoyy on 2017/9/13 0013.
 */

public class MyReplyListPresenter implements MyReplyListContract.Presenter
{
    private MyReplyListContract.View mMyReplyListView;

    public MyReplyListPresenter(MyReplyListContract.View mMyReplyListView)
    {
        this.mMyReplyListView = mMyReplyListView;
        mMyReplyListView.setPresenter(this);
    }

    @Override
    public void start()
    {
        Log.i(Constant.TAG, "OrderList start");
    }

    @Override
    public void myReplyList(Call<MyReplyListInfo> call ,Map<String, String> params, final int refreshTag)
    {
        CommonUtils.httpDebugLogger("我的回复列表");
        mMyReplyListView.refreshing();
        call.enqueue(new Callback<MyReplyListInfo>()
        {
            @Override
            public void onResponse(Call<MyReplyListInfo> call, Response<MyReplyListInfo> response)
            {
                if (!mMyReplyListView.isActive())
                {
                    return;
                }

                //停止刷新
                mMyReplyListView.finishRefesh();

                if (response.isSuccessful() && response.body() != null)
                {
                    MyReplyListInfo myReplyListInfo = response.body();
                    boolean isSuccess = myReplyListInfo.isSuccess();
                    String errorCode = myReplyListInfo.getErrorCode();
                    CommonUtils.httpDebugLogger("[isSuccess="+isSuccess+"][errorCode=" + errorCode + "]");
                    if (isSuccess)
                    {
                        LinkedList<MyReplyListInfo.BodyBean.ListBean.ResultBean> orderList = myReplyListInfo.getBody().getList().getResult();
                        if(refreshTag == Constant.PULL_TO_REFRESH)
                        {
                            mMyReplyListView.showMyReplyList(orderList,myReplyListInfo.getBody().getList().getPages());
                        }
                        else
                        {
                            mMyReplyListView.loadMoreMyReplyList(orderList,myReplyListInfo.getBody().getList().getPages());
                        }

                    }
                    else
                    {
                        mMyReplyListView.showToast("请求失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyReplyListInfo> call, Throwable t)
            {
                if (!mMyReplyListView.isActive())
                {
                    return;
                }
                //停止刷新
                mMyReplyListView.finishRefesh();
                CommonUtils.httpErrorLogger(t.toString());
                if (!call.isCanceled())
                {
//                    mMyReplyListView.showToast(R.string.network_error);
                }
            }
        });

    }


}
