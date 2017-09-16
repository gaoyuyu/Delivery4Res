package com.gaoyy.delivery4res.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.main.OrderNewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;


/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver
{
    private static final String TAG = "JPush MyReceiver";
    private int messageType;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();

        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        String rid = JPushInterface.getRegistrationID(context.getApplicationContext());
        Log.d(TAG, "[MyReceiver] 接收Registration Id : " + rid);

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
        {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        }
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
        {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        }
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
        {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            Log.d(TAG, "[MyReceiver] 打印Bundle: " + printBundle(bundle));
            messageType = getMessageType(bundle);
            Log.d(TAG, "[MyReceiver] Message Type : " + messageType);
            if (messageType == 1)
            {
                //判断应用是否在前台，在前台直接打开推送界面
                if (BaseActivity.isForeground)
                {
                    Intent notice = new Intent();
                    notice.putExtra("notice", bundle);
                    notice.setClass(context, OrderNewActivity.class);
                    notice.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(notice);
                }
            }
//            else
//            {
//                CommonUtils.showToast(context,getAlertMessage(bundle));
//                Intent update = new Intent();
//                update.setAction("android.intent.action.UpdateListAfterCancleReceiver");
//                context.sendBroadcast(update);
//            }
        }
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
        {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            messageType = getMessageType(bundle);
            Log.d(TAG, "[MyReceiver] Message Type : " + messageType);
            if(messageType == 1)
            {
                //打开通知跳转到推送页面，无论是否在前台
                Intent notice = new Intent();
                notice.putExtra("notice", bundle);
                notice.setClass(context, OrderNewActivity.class);
                notice.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(notice);
            }
//            else
//            {
//                CommonUtils.showToast(context,getAlertMessage(bundle));
//                Intent update = new Intent();
//                update.setAction("android.intent.action.UpdateListAfterCancleReceiver");
//                context.sendBroadcast(update);
//            }
        }
        else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))
        {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        }
        else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))
        {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        }
        else
        {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle)
    {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet())
        {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID))
            {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }
            else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE))
            {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            }
            else if (key.equals(JPushInterface.EXTRA_EXTRA))
            {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty())
                {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try
                {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext())
                    {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                }
                catch (JSONException e)
                {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            }
            else
            {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle)
    {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);


        Log.d("JPushDemo", "EXTRA_MESSAGE->" + message);
        Log.d("JPushDemo", "EXTRA_EXTRA->" + extras);
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
    }

    /**
     * 获取推送的消息类型
     *
     * @param bundle
     * @return
     */
    private int getMessageType(Bundle bundle)
    {
        int type = -1;
        try
        {
            JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
            Iterator<String> it = json.keys();

            while (it.hasNext())
            {
                String key = it.next().toString();
                String value = json.optString(key);

                Log.d(Constant.TAG, "[MyReceiver]---key-->" + key);
                Log.d(Constant.TAG, "[MyReceiver]---value-->" + json.optString(key));

                if (key.equals("type"))
                {
                    type = Integer.valueOf(value);
                }
            }
        }
        catch (JSONException e)
        {
            Log.e(Constant.TAG, "Get message extra JSON error!");
        }
        return type;
    }

    /**
     * 获取alert内容
     *
     * @param bundle
     * @return
     */
    private String getAlertMessage(Bundle bundle)
    {
        String alertMessage = "";
        for (String key : bundle.keySet())
        {
            if (key.equals(JPushInterface.EXTRA_ALERT))
            {
                alertMessage = bundle.getString(key);
                Log.d(Constant.TAG, "EXTRA_ALERT--->" + alertMessage);
            }
        }
        return alertMessage;
    }

}
