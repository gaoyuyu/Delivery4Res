package com.gaoyy.delivery4res.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.base.CustomDialogFragment;
import com.gaoyy.delivery4res.util.ActivityUtils;
import com.gaoyy.delivery4res.util.CommonUtils;
import com.gaoyy.delivery4res.util.DialogUtils;


public class LoginActivity extends BaseActivity
{
    private LinearLayout activityLogin;
    private Toolbar loginToolbar;
    private FrameLayout loginContent;

    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        activityLogin = (LinearLayout) findViewById(R.id.activity_login);
        loginToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        loginContent = (FrameLayout) findViewById(R.id.login_content);
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar(loginToolbar, R.string.login, false, -1);
    }


    @Override
    protected void configViews()
    {
        super.configViews();

        Log.e(Constant.TAG, "检查定位权限");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            // Android M Permission check
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                {
                    showRequestPermissionDialog();
                }
                else
                {
                    Log.e(Constant.TAG, "获取定位权限");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Constant.REQUEST_ACCESS_COARSE_LOCATION);
                }

            }
            else
            {
                Log.e(Constant.TAG, "已有定位权限");
            }
        }

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.login_content);
        if (loginFragment == null)
        {
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), loginFragment, R.id.login_content);
        }
        new LoginPresenter(loginFragment);
    }

    private void showRequestPermissionDialog()
    {
        CustomDialogFragment dialog = DialogUtils.showAlertDialog(this, getResources().getString(R.string.dialog_permission_title),
                getResources().getString(R.string.dialog_permission_message),
                getResources().getString(R.string.dialog_permission_deny), getResources().getString(R.string.dialog_permission_grant));
        dialog.setOnAlertDialogClickListener(new CustomDialogFragment.OnAlertDialogClickListener()
        {
            @Override
            public void onButtonClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case AlertDialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                    case AlertDialog.BUTTON_POSITIVE:
                        dialog.dismiss();
                        Log.e(Constant.TAG, "获取定位权限 in dialog");
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Constant.REQUEST_ACCESS_COARSE_LOCATION);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case Constant.REQUEST_ACCESS_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.e(Constant.TAG, "定位权限获取成功");
                }
                else
                {
                    CommonUtils.showToast(this, getResources().getString(R.string.permission_already_deny));
                }
                break;
        }
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.d(Constant.TAG, "LoginActivity onNewIntent");
    }
}
