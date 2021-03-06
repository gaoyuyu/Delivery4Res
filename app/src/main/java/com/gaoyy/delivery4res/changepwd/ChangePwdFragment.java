package com.gaoyy.delivery4res.changepwd;


import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.RetrofitService;
import com.gaoyy.delivery4res.api.bean.CommonInfo;
import com.gaoyy.delivery4res.base.BaseFragment;
import com.gaoyy.delivery4res.base.CustomDialogFragment;
import com.gaoyy.delivery4res.login.LoginActivity;
import com.gaoyy.delivery4res.util.CommonUtils;
import com.gaoyy.delivery4res.util.DialogUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class ChangePwdFragment extends BaseFragment implements ChangePwdContract.View
{
    private static final String LOG_TAG = ChangePwdFragment.class.getSimpleName();

    private TextInputLayout changeOldpwdTextinputlayout;
    private TextInputEditText changeOldpwd;
    private TextInputLayout changeNewpwdTextinputlayout;
    private TextInputEditText changeNewpwd;

    private ChangePwdContract.Presenter mChangePwdPresenter;
    private CustomDialogFragment loading;

    private Call<CommonInfo> call;

    public ChangePwdFragment()
    {
        // Required empty public constructor
    }

    public static ChangePwdFragment newInstance()
    {
        ChangePwdFragment fragment = new ChangePwdFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayoutId()
    {
        return R.layout.fragment_change_pwd;
    }


    @Override
    protected void assignViews(View rootView)
    {
        super.assignViews(rootView);
        changeOldpwdTextinputlayout = (TextInputLayout) rootView.findViewById(R.id.change_oldpwd_textinputlayout);
        changeOldpwd = (TextInputEditText) rootView.findViewById(R.id.change_oldpwd);
        changeNewpwdTextinputlayout = (TextInputLayout) rootView.findViewById(R.id.change_newpwd_textinputlayout);
        changeNewpwd = (TextInputEditText) rootView.findViewById(R.id.change_newpwd);
    }

    /**
     * 校验输入
     */
    public void validate()
    {
        CommonUtils.textInputLayoutSetting(changeOldpwd, changeOldpwdTextinputlayout, getResources().getString(R.string.check_old_pwd));
        CommonUtils.textInputLayoutSetting(changeNewpwd, changeNewpwdTextinputlayout, getResources().getString(R.string.check_new_pwd));
        if (changeNewpwd.getText().toString().length() != 6)
        {
            changeNewpwdTextinputlayout.setErrorEnabled(true);
            changeNewpwdTextinputlayout.setError(getResources().getString(R.string.check_pwd_length));
        }
        else
        {
            changeNewpwdTextinputlayout.setError(null);
            changeNewpwdTextinputlayout.setErrorEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        activity.getMenuInflater().inflate(R.menu.change_pwd, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_change_pwd:
                validate();
                if (!(changeOldpwdTextinputlayout.isErrorEnabled() || changeNewpwdTextinputlayout.isErrorEnabled()))
                {
                    Map<String, String> params = new HashMap<>();
                    params.put("loginName", CommonUtils.getLoginName(activity));
                    params.put("randomCode", CommonUtils.getRandomCode(activity));
                    params.put("password", changeOldpwd.getText().toString());
                    params.put("newPassword", changeNewpwd.getText().toString());
                    call = RetrofitService.sApiService.changePwd(params);
                    mChangePwdPresenter.changePwd(call, params);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        if (mChangePwdPresenter == null) return;
        mChangePwdPresenter.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //取消请求
        if (call != null) call.cancel();
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
            loading.dismissAllowingStateLoss();
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
    public void redirectToLogin()
    {
        //修改完密码后将自动登录置为false
        CommonUtils.setUpAutoLogin(activity, false);
        Intent login = new Intent(activity, LoginActivity.class);
        startActivity(login);
    }

    @Override
    public void setPresenter(ChangePwdContract.Presenter presenter)
    {
        Log.i(Constant.TAG, LOG_TAG + "  setPresenter");
        if (presenter != null)
        {
            mChangePwdPresenter = presenter;
        }
    }
}
