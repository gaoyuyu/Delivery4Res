package com.gaoyy.delivery4res.api;

import com.gaoyy.delivery4res.api.bean.GeocodeInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gaoyy on 2017/5/11 0011.
 */

public interface GoogleMapApi
{

    @GET("geocode/json")
    Call<GeocodeInfo> getLatLng(@Query("address") String address, @Query("key") String key);
}
