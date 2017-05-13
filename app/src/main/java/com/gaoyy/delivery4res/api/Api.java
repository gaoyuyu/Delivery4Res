package com.gaoyy.delivery4res.api;


import com.gaoyy.delivery4res.api.bean.CommonInfo;
import com.gaoyy.delivery4res.api.bean.OrderListInfo;
import com.gaoyy.delivery4res.api.bean.OrderSaveInfo;
import com.gaoyy.delivery4res.api.bean.RestInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by gaoyy on 2017/5/6 0006.
 */

public interface Api
{
    /**
     * 登录
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/mobile/login")
    Call<RestInfo> login(@FieldMap Map<String, String> params);


    /**
     * 退出
     * @param loginName
     * @param randomCode
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/mobile/loginout")
    Call<CommonInfo> logout(@Field("loginName") String loginName, @Field("randomCode") String randomCode);


    /**
     * 修改密码
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/sys/user/mobile/resetPwd")
    Call<CommonInfo> changePwd(@FieldMap Map<String, String> params);


    /**
     * 保存（提交）订单
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/order/order/mobile/ordersave")
    Call<OrderSaveInfo> orderSave(@FieldMap Map<String, String> params);

    /**
     * 订单列表
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("a/order/order/mobile/orderlist")
    Call<OrderListInfo> orderList(@FieldMap Map<String, String> params);

}
