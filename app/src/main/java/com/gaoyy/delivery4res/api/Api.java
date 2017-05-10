package com.gaoyy.delivery4res.api;


import com.gaoyy.delivery4res.api.bean.CommonInfo;
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
    @FormUrlEncoded
    @POST("a/sys/user/mobile/login")
    Call<RestInfo> login(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("a/sys/user/mobile/loginout")
    Call<CommonInfo> logout(@Field("loginName") String loginName, @Field("randomCode") String randomCode);


    @FormUrlEncoded
    @POST("a/sys/user/mobile/resetPwd")
    Call<CommonInfo> changePwd(@FieldMap Map<String, String> params);


}
