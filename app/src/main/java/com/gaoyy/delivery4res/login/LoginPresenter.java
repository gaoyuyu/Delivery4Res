package com.gaoyy.delivery4res.login;

import android.util.Log;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.RetrofitService;
import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.util.CommonUtils;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements LoginContract.Presenter
{
    private LoginContract.View mLoginView;

    public LoginPresenter(LoginContract.View mLoginView)
    {
        this.mLoginView = mLoginView;
        mLoginView.setPresenter(this);
    }

    @Override
    public void start()
    {
        Log.i(Constant.TAG,"Login start");
    }

    @Override
    public void login(Map<String, String> params)
    {
        Call<RestInfo> call = RetrofitService.sApiService.login(params);
        CommonUtils.httpDebugLogger("登录请求");
        CommonUtils.httpDebugLogger("登录请求参数："+params);
        mLoginView.showLoading();
        call.enqueue(new Callback<RestInfo>()
        {
            @Override
            public void onResponse(Call<RestInfo> call, Response<RestInfo> response)
            {
                if (!mLoginView.isActive())
                {
                    return;
                }
                mLoginView.hideLoading();
                if (response.isSuccessful() && response.body() != null)
                {
                    RestInfo restInfo = response.body();
                    String msg = restInfo.getMsg();
                    String errorCode = restInfo.getErrorCode();
                    CommonUtils.httpDebugLogger("[isSuccess="+restInfo.isSuccess()+"][errorCode=" + errorCode + "][msg=" + msg + "]");
                    mLoginView.showToast(msg);

                    if(errorCode.equals("-1"))
                    {
                        //保存用户信息
                        mLoginView.saveUserInfo(restInfo.getBody().getUser());

                        //保存餐厅位置和其他详细信息
                        mLoginView.saveHotelInfo(restInfo.getBody().getHotel());

                        List<RestInfo.BodyBean.RemarkDictBean> remarkDict = restInfo.getBody().getRemarkDict();
                        List<RestInfo.BodyBean.FinishedTimeBean> finishedTime = restInfo.getBody().getFinishedTime();
                        List<RestInfo.BodyBean.DictStatusBean> dictStatus = restInfo.getBody().getDictStatus();

                        /**
                         * 跳转到MainActivity,需要传参
                         *   remarkDict，finishedTime，dictStatus
                         */
                        mLoginView.redirectToMain(remarkDict,finishedTime,dictStatus);
                    }
                    else if(errorCode.equals("-2"))
                    {
                        //登录失败
                    }
                }
            }

            @Override
            public void onFailure(Call<RestInfo> call, Throwable t)
            {
                if (!mLoginView.isActive())
                {
                    return;
                }
                mLoginView.hideLoading();
                CommonUtils.httpErrorLogger(t.toString());
                mLoginView.showToast(R.string.network_error);
            }
        });


    }
}
