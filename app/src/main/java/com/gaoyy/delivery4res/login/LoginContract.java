package com.gaoyy.delivery4res.login;


import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.base.BasePresenter;
import com.gaoyy.delivery4res.base.BaseView;

import java.util.List;
import java.util.Map;

public class LoginContract
{
    interface View extends BaseView<Presenter>
    {
        boolean isActive();

        void showLoading();
        void hideLoading();
        void showToast(String msg);

        /**
         * 保存用户信息
         * @param user
         */
        void saveUserInfo(RestInfo.BodyBean.UserBean user);

        /**
         *保存餐厅位置和其他详细信息
         * @param hotel
         */
        void saveHotelInfo(RestInfo.BodyBean.HotelBean hotel);

        /**
         * 跳转到MainActivity
         */
        void redirectToMain(List<RestInfo.BodyBean.RemarkDictBean> remarkDict, List<RestInfo.BodyBean.FinishedTimeBean> finishedTime , List<RestInfo.BodyBean.DictStatusBean> dictStatus);
    }

    interface Presenter extends BasePresenter
    {
        void login(Map<String, String> params);
    }
}
