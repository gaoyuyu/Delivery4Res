package com.gaoyy.delivery4res.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.gaoyy.delivery4res.base.CustomDialogFragment;

import java.lang.ref.WeakReference;

public class DialogUtils
{
    /**
     * 显示loading框
     * @param context
     * @return
     */
    public static CustomDialogFragment showLoadingDialog(Context context)
    {
        if (!(context instanceof FragmentActivity)) return null;
        CustomDialogFragment dialog = new WeakReference<CustomDialogFragment>(new CustomDialogFragment()).get();
        if (dialog == null) return null;
        dialog.setType(CustomDialogFragment.DialogType.LOADING);
        dialog.setCancelable(true);
        dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "LoadingDialog");
        return dialog;
    }
    public static CustomDialogFragment showLoadingDialog(Context context, String loadingText)
    {
        if (!(context instanceof FragmentActivity)) return null;
        CustomDialogFragment dialog = new WeakReference<CustomDialogFragment>(new CustomDialogFragment()).get();
        if (dialog == null) return null;
        dialog.setType(CustomDialogFragment.DialogType.LOADING_WITH_TEXT);
        dialog.setLoadingText(loadingText);
        dialog.setCancelable(true);
        dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "LoadingDialog");
        return dialog;
    }


    /**
     * 显示带选项按钮对话框
     * @param context
     * @return
     */
    public static CustomDialogFragment showAlertDialog(Context context, String title, String message, String negativeText, String positionText)
    {
        if (!(context instanceof FragmentActivity)) return null;
        CustomDialogFragment dialog = new WeakReference<CustomDialogFragment>(new CustomDialogFragment()).get();
        if (dialog == null) return null;
        dialog.setType(CustomDialogFragment.DialogType.ALERT);
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNegativeText(negativeText);
        dialog.setPositiveText(positionText);
        dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "AlertDialog");
        return dialog;
    }
}
