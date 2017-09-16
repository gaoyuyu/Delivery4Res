package com.gaoyy.delivery4res.api;


import com.gaoyy.delivery4res.api.bean.CommonInfo;
import com.gaoyy.delivery4res.api.bean.MessageListInfo;
import com.gaoyy.delivery4res.api.bean.MyReplyListInfo;
import com.gaoyy.delivery4res.api.bean.OrderListInfo;
import com.gaoyy.delivery4res.api.bean.OrderNewInfo;
import com.gaoyy.delivery4res.api.bean.OrderOperationStatusInfo;
import com.gaoyy.delivery4res.api.bean.ReplyOrderListInfo;
import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.api.bean.UpdateInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by gaoyy on 2017/5/6 0006.
 */

public interface Api
{

    @GET("a/sys/user/mobile/version")
    Call<UpdateInfo> getAppCurrentVersion();

    /**
     * 登录
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/mobile/login")
    Call<RestInfo> login(@FieldMap Map<String, String> params);


    /**
     * 退出
     *
     * @param loginName
     * @param randomCode
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/mobile/loginout")
    Call<CommonInfo> logout(@Field("loginName") String loginName, @Field("randomCode") String randomCode);


    /**
     * 修改密码
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/mobile/resetPwd")
    Call<CommonInfo> changePwd(@FieldMap Map<String, String> params);


    /**
     * 保存（提交）订单
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/order/order/mobile/ordersave")
    Call<CommonInfo> orderSave(@FieldMap Map<String, String> params);

    /**
     * 订单列表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/order/order/mobile/orderlist")
    Call<OrderListInfo> orderList(@FieldMap Map<String, String> params);


    /**
     * 饭店订单取消，用于cancle按钮
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/order/order/mobile/ordercancel")
    Call<OrderOperationStatusInfo> orderCancle(@FieldMap Map<String, String> params);
//    Call<OrderOperationStatusInfo> orderCancle(@Field("loginName") String loginName, @Field("randomCode") String randomCode, @Field("id") String id);

    /**
     * 餐厅resubmit，用于resubmit按钮
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/order/order/mobile/resubmit")
    Call<OrderOperationStatusInfo> orderResubmit(@FieldMap Map<String, String> params);

    /**
     * 饭店及司机订单派送，用于Delivery按钮
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/order/order/mobile/ordersend")
    Call<OrderOperationStatusInfo> orderSend(@FieldMap Map<String, String> params);

    /**
     * 饭店订单退单，用于cancle after deliver按钮
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/order/order/mobile/orderback")
    Call<OrderOperationStatusInfo> orderBack(@FieldMap Map<String, String> params);

    /**
     * 饭店订单退单，用于cancle after deliver按钮
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/pc/orderMakeComplete")
    Call<OrderOperationStatusInfo> orderMakeComplete(@FieldMap Map<String, String> params);


    /**
     * 订单详情（最新订单）
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/pc/orderDetails")
    Call<OrderNewInfo> newOrderDetail(@FieldMap Map<String, String> params);



    /**
     * 接单
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/pc/orderAccept")
    Call<CommonInfo> orderAccept(@FieldMap Map<String, String> params);

    /**
     * 拒绝接单
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/pc/orderCancel")
    Call<CommonInfo> orderRefuse(@FieldMap Map<String, String> params);


    /**
     * 站内消息列表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/pc/messageList")
    Call<MessageListInfo> messageList(@FieldMap Map<String, String> params);

    /**
     * 待回复列表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/pc/replyOrderList")
    Call<ReplyOrderListInfo> replyOrderList(@FieldMap Map<String, String> params);

    /**
     * 我的回复列表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/pc/myReplyList")
    Call<MyReplyListInfo> myReplyList(@FieldMap Map<String, String> params);


    /**
     * 回复订单
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/pc/replyOrderSave")
    Call<CommonInfo> replyOrderSave(@FieldMap Map<String, String> params);





}
