package com.gaoyy.delivery4res.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.application.ExitApplication;
import com.gaoyy.delivery4res.util.CommonUtils;


public abstract class BaseActivity extends AppCompatActivity
{
    private long firstTime = 0;

    private int toolbarColor = R.color.colorPrimary;
    public static boolean isForeground = false;

//    private UpdateManager updateManager = new UpdateManager(BaseActivity.this, Constant.DOWNLOAD_RES_APK_URL,Constant.RES_APK_NAME);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ExitApplication.getInstanse().addActivity(this);

        //加载布局
        initContentView();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏

        //初始化view
        assignViews();
        //初始化toolbar
        initToolbar();
        //配置views
        configViews();
        //设置监听器
        setListener();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //停止下载
//        updateManager.stopApkThread();
        isForeground = false;
    }

    protected abstract void initContentView();

    protected void assignViews()
    {

    }

    protected void initToolbar()
    {

    }

    protected void configViews()
    {

    }

    protected void setListener()
    {

    }


    /**
     * @param toolbar
     * @param titleId      string id
     * @param enabled      toolbar返回键是否可用，true-可用，false-不可用
     * @param toolbarColor toolbar背景颜色，-1为默认色
     */
    public void initToolbar(Toolbar toolbar, int titleId, boolean enabled, int toolbarColor)
    {
        if (-1 == toolbarColor)
        {
            toolbarColor = this.toolbarColor;
        }
        //设置toolbat标题
        toolbar.setTitle(titleId);
        //设置toolbar背景色
        toolbar.setBackgroundColor(getResources().getColor(toolbarColor));
        setSupportActionBar(toolbar);
        //设置toolbar返回键是否可用
        getSupportActionBar().setHomeButtonEnabled(enabled);
        getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
    }

    /**
     * @param toolbar
     * @param title        string id
     * @param enabled      toolbar返回键是否可用，true-可用，false-不可用
     * @param toolbarColor toolbar背景颜色，-1为默认色
     */
    public void initToolbar(Toolbar toolbar, String title, boolean enabled, int toolbarColor)
    {
        if (-1 == toolbarColor)
        {
            toolbarColor = this.toolbarColor;
        }
        //设置toolbat标题
        toolbar.setTitle(title);
        //设置toolbar背景色
        toolbar.setBackgroundColor(getResources().getColor(toolbarColor));
        setSupportActionBar(toolbar);
        //设置toolbar返回键是否可用
        getSupportActionBar().setHomeButtonEnabled(enabled);
        getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if ((System.currentTimeMillis() - firstTime) > 2000)
            {
                // 如果两次按键时间间隔大于2000毫秒，则不退出
                CommonUtils.showToast(this, getResources().getString(R.string.exit_after_press_again));
                firstTime = System.currentTimeMillis();// 更新mExitTime
            }
            else
            {
                CommonUtils.setJpushAlias(this, "");
                ExitApplication.getInstanse().exit();
                //设置别名为空，不接受推送，必须在killProcess之前
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
