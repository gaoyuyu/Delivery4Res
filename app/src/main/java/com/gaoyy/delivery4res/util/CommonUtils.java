package com.gaoyy.delivery4res.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.widget.Toast;

import com.gaoyy.delivery4res.api.Constant;


/**
 * Created by gaoyy on 2017/5/6 0006.
 */

public class CommonUtils
{
    public static boolean isEmpty(String str)
    {
        if (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals(""))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * showToast
     *
     * @param msg
     */
    public static void showToast(Context context, String msg)
    {
        if (null != msg && !CommonUtils.isEmpty(msg))
        {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取用户的名字
     *
     * @param context
     * @return
     */
    public static String getName(Context context)
    {
        SharedPreferences account = context.getSharedPreferences("account", Activity.MODE_PRIVATE);
        String name = account.getString("name", "");
        return name;
    }

    /**
     * 获取用户的登录账户名
     *
     * @param context
     * @return
     */
    public static String getLoginName(Context context)
    {
        SharedPreferences account = context.getSharedPreferences("account", Activity.MODE_PRIVATE);
        String loginName = account.getString("loginName", "");
        return loginName;
    }
    /**
     * 获取用户的随机码
     *
     * @param context
     * @return
     */
    public static String getRandomCode(Context context)
    {
        SharedPreferences account = context.getSharedPreferences("account", Activity.MODE_PRIVATE);
        String loginName = account.getString("randomCode", "");
        return loginName;
    }


    /**
     * 获取角色标识
     *
     * @param context
     * @return 1-司机，2-饭店，-1-无角色信息
     */
    public static int getRoleFlag(Context context)
    {
        int flag = -1;
        SharedPreferences account = context.getSharedPreferences("account", Activity.MODE_PRIVATE);
        String name = account.getString("roleNames", "");
        Log.e(Constant.TAG, "==>" + name);
        if (name.equals("司机"))
        {
            flag = 1;
        }
        else if (name.equals("饭店"))
        {
            flag = 2;
        }
        return flag;
    }


    /**
     * input layout 设置
     *
     * @param ed
     * @param layout
     * @param msg
     */
    public static void textInputLayoutSetting(TextInputEditText ed, TextInputLayout layout, String msg)
    {
        if (ed.getText().toString().equals(""))
        {
            layout.setErrorEnabled(true);
            layout.setError(msg);
        }
        else
        {
            layout.setError(null);
            layout.setErrorEnabled(false);
        }
    }

}
