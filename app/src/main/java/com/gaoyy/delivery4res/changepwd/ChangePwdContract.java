package com.gaoyy.delivery4res.changepwd;


import com.gaoyy.delivery4res.api.bean.CommonInfo;
import com.gaoyy.delivery4res.base.BasePresenter;
import com.gaoyy.delivery4res.base.BaseView;

import java.util.Map;

import retrofit2.Call;

/**
 * Created by gaoyy on 2017/5/7 0007.
 */

public class ChangePwdContract
{
    interface View extends BaseView<Presenter>
    {
        boolean isActive();

        void showLoading();

        void hideLoading();

        void showToast(String msg);

        void showToast(int msgId);

        //修改密码成功后跳转到LoginActivity
        void redirectToLogin();

    }

    interface Presenter extends BasePresenter
    {
        void changePwd(Call<CommonInfo> call,Map<String, String> params);
    }
}
