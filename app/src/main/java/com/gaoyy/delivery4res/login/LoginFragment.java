package com.gaoyy.delivery4res.login;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.base.BaseFragment;
import com.gaoyy.delivery4res.base.CustomDialogFragment;
import com.gaoyy.delivery4res.main.MainActivity;
import com.gaoyy.delivery4res.util.CommonUtils;
import com.gaoyy.delivery4res.util.DialogUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginFragment extends BaseFragment implements LoginContract.View, View.OnClickListener
{
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    private LoginContract.Presenter mLoginPresenter;

    private TextInputLayout loginUsernameTextinputlayout;
    private TextInputEditText loginUsername;
    private TextInputLayout loginPasswordTextinputlayout;
    private TextInputEditText loginPassword;
    private AppCompatButton loginBtn;

    private CustomDialogFragment loading;


    public LoginFragment()
    {

    }

    public static LoginFragment newInstance()
    {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayoutId()
    {
        return R.layout.fragment_login;
    }

    @Override
    protected void assignViews(View rootView)
    {
        super.assignViews(rootView);
        loginUsernameTextinputlayout = (TextInputLayout) rootView.findViewById(R.id.login_username_textinputlayout);
        loginUsername = (TextInputEditText) rootView.findViewById(R.id.login_username);
        loginPasswordTextinputlayout = (TextInputLayout) rootView.findViewById(R.id.login_password_textinputlayout);
        loginPassword = (TextInputEditText) rootView.findViewById(R.id.login_password);
        loginBtn = (AppCompatButton) rootView.findViewById(R.id.login_btn);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
    }


    @Override
    protected void setListener()
    {
        super.setListener();
        loginUsername.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (editable.toString() != null || !editable.toString().equals(""))
                {
                    loginUsernameTextinputlayout.setError(null);
                    loginUsernameTextinputlayout.setErrorEnabled(false);
                }
            }
        });

        loginPassword.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (editable.toString() != null || !editable.toString().equals(""))
                {
                    loginPasswordTextinputlayout.setError(null);
                    loginPasswordTextinputlayout.setErrorEnabled(false);
                }
            }
        });
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(mLoginPresenter == null) return;
        mLoginPresenter.start();
    }

    @Override
    public boolean isActive()
    {
        return isAdded();
    }

    @Override
    public void showLoading()
    {
        loading = DialogUtils.showLoadingDialog(activity);
    }

    @Override
    public void hideLoading()
    {
        if (loading != null)
        {
            loading.dismiss();
        }
    }

    @Override
    public void showToast(String msg)
    {
        CommonUtils.showToast(activity, msg);
    }

    @Override
    public void showToast(int msgId)
    {
        CommonUtils.showToast(activity, msgId);
    }

    @Override
    public void saveUserInfo(RestInfo.BodyBean.UserBean user)
    {
        SharedPreferences account = activity.getSharedPreferences("account", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = account.edit();
        editor.putString("id", user.getId());
        editor.putString("loginName", user.getLoginName());
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("phone", user.getPhone());
        editor.putString("mobile", user.getMobile());
        editor.putString("randomCode", user.getRandomCode());
        editor.putString("roleNames", user.getRoleNames());
        editor.apply();
    }

    @Override
    public void saveHotelInfo(RestInfo.BodyBean.HotelBean hotel)
    {
        SharedPreferences hotelInfo = activity.getSharedPreferences("hotel", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = hotelInfo.edit();
        editor.putString("id", hotel.getId());
        editor.putString("name", hotel.getName());
        editor.putString("addr", hotel.getAddr());
        editor.putString("contacts", hotel.getContacts());
        editor.putString("tel",hotel.getTel());
        editor.putString("startDeliveryTime",hotel.getStartDeliveryTime());
        editor.putString("endDeliveryTime",hotel.getEndDeliveryTime());
        editor.putString("longitude",hotel.getLongitude());
        editor.putString("latitude",hotel.getLatitude());
        editor.putString("no",hotel.getNo());
        editor.apply();
    }


    @Override
    public void redirectToMain(List<RestInfo.BodyBean.RemarkDictBean> remarkDict, List<RestInfo.BodyBean.FinishedTimeBean> finishedTime, List<RestInfo.BodyBean.DictStatusBean> dictStatus)
    {
        Intent intent = new Intent();
        intent.putExtra("remarkDict",(Serializable) remarkDict);
        intent.putExtra("finishedTime",(Serializable) finishedTime);
        intent.putExtra("dictStatus",(Serializable) dictStatus);
        intent.setClass(activity, MainActivity.class);
        startActivity(intent);
        activity.finish();
    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter)
    {
        Log.i(Constant.TAG, LOG_TAG + "  setPresenter");
        if (presenter != null)
        {
            mLoginPresenter = presenter;
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.login_btn:
                validate();
                if (loginUsernameTextinputlayout.isErrorEnabled() || loginPasswordTextinputlayout.isErrorEnabled())
                    return;
                Map<String, String> params = new HashMap<>();
                params.put("loginName", loginUsername.getText().toString());
                params.put("pwd", loginPassword.getText().toString());
                //appType=1餐厅端
                params.put("appType", "1");
                mLoginPresenter.login(params);
                break;
        }
    }

    /**
     * 校验输入
     */
    private void validate()
    {
        CommonUtils.textInputLayoutSetting(loginUsername, loginUsernameTextinputlayout, "username mustn't be empty");
        CommonUtils.textInputLayoutSetting(loginPassword, loginPasswordTextinputlayout, "password mustn't be empty");
    }

}
