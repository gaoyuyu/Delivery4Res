package com.gaoyy.delivery4res.application;

import android.app.Application;

import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.RetrofitService;
import com.tencent.bugly.crashreport.CrashReport;


/**
 * Created by gaoyy on 2016/12/28.
 */

public class MyApplication extends Application
{
    private static final String LOG_TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate()
    {
        super.onCreate();

        RetrofitService.init(this);

        /**
         * 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
         输出详细的Bugly SDK的Log；
         每一条Crash都会被立即上报；
         自定义日志将会在Logcat中输出。
         建议在测试阶段建议设置成true，发布时设置为false。
         */
        CrashReport.initCrashReport(getApplicationContext(), Constant.BUGLY_APP_ID, true);
    }



}
