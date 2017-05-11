package com.gaoyy.delivery4res.main;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.RetrofitService;
import com.gaoyy.delivery4res.api.bean.GeocodeInfo;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.base.CustomDialogFragment;
import com.gaoyy.delivery4res.util.DialogUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends BaseActivity implements OnMapReadyCallback
{
    private LinearLayout activityOrderDetail;
    private TextView orderDetailStartPoint;
    private TextView orderDetailDistination;
    private TextView orderDetailPhone;
    private TextView orderDetailNotes;
    private TextView orderDetailFinishTime;
    private TextView orderDetailOther;




    private GoogleMap mMap;

    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        activityOrderDetail = (LinearLayout) findViewById(R.id.activity_order_detail);
        orderDetailStartPoint = (TextView) findViewById(R.id.order_detail_start_point);
        orderDetailDistination = (TextView) findViewById(R.id.order_detail_distination);
        orderDetailPhone = (TextView) findViewById(R.id.order_detail_phone);
        orderDetailNotes = (TextView) findViewById(R.id.order_detail_notes);
        orderDetailFinishTime = (TextView) findViewById(R.id.order_detail_finish_time);
        orderDetailOther = (TextView) findViewById(R.id.order_detail_other);
    }

    @Override
    protected void configViews()
    {
        super.configViews();



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.order_detail_map);
        mapFragment.getMapAsync(this);

        SharedPreferences hotelInfo = getSharedPreferences("hotel", Activity.MODE_PRIVATE);
        String startDeliveryTime = hotelInfo.getString("startDeliveryTime","");
        String endDeliveryTime = hotelInfo.getString("endDeliveryTime","");
        final String hotelAddr = hotelInfo.getString("addr","");
        final String hotelLng = hotelInfo.getString("longitude","");
        final String hotelLat = hotelInfo.getString("latitude","");

        String customerTel = getIntent().getStringExtra("customerTel");
        final String customerAddr = getIntent().getStringExtra("customerAddr");
        String apt = getIntent().getStringExtra("apt");
        String finishedTime = getIntent().getStringExtra("finishedTime");
        String remark = getIntent().getStringExtra("remark");
        String remarks = getIntent().getStringExtra("remarks");


        orderDetailStartPoint.setText(hotelAddr);
        orderDetailDistination.setText("APT:"+apt+" "+customerAddr);
        orderDetailPhone.setText(customerTel);
        orderDetailNotes.setText(remark);
        orderDetailFinishTime.setText(finishedTime);
        orderDetailOther.setText(remarks);


        Call<GeocodeInfo> call = RetrofitService.sGoogleMapApiService.getLatLng(customerAddr, Constant.GOOGLE_MAP_KEY);
        final CustomDialogFragment loading = DialogUtils.showLoadingDialog(this);
        call.enqueue(new Callback<GeocodeInfo>()
        {
            @Override
            public void onResponse(Call<GeocodeInfo> call, Response<GeocodeInfo> response)
            {
                loading.dismiss();
                if (response.isSuccessful() && response.body() != null)
                {
                    GeocodeInfo geocodeInfo = response.body();
                    double customerAddrLat = geocodeInfo.getResults().get(0).getGeometry().getLocation().getLat();
                    double customerAddrLng = geocodeInfo.getResults().get(0).getGeometry().getLocation().getLng();



                    LatLng res = new LatLng(Double.parseDouble(hotelLat),Double.parseDouble(hotelLng));
                    LatLng cus = new LatLng(customerAddrLat,customerAddrLng);

                    MarkerOptions resOptions = new MarkerOptions()
                            .position(res)
                            .title(hotelAddr)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restaurant_location));
                    MarkerOptions cusOptions = new MarkerOptions()
                            .position(cus)
                            .title(customerAddr)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_customer_location));


                    mMap.addMarker(resOptions);
                    mMap.addMarker(cusOptions);

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(res));

                }
            }

            @Override
            public void onFailure(Call<GeocodeInfo> call, Throwable t)
            {
                loading.dismiss();
            }
        });







    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        configMapUiSettings();
    }
    /**
     * 配置地图
     */
    private void configMapUiSettings()
    {
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
        mUiSettings.setMapToolbarEnabled(true);
    }
}
