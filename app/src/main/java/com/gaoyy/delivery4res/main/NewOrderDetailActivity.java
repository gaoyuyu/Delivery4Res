package com.gaoyy.delivery4res.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.bean.OrderListInfo;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewOrderDetailActivity extends BaseActivity implements OnMapReadyCallback
{
    private Toolbar newOrderDetailToolbar;
    private TextView newOrderDetailDate;
    private TextView newOrderDetailOrderNo;
    private TextView newOrderDetailStartPoint;
    private TextView newOrderDetailDistination;
    private TextView newOrderDetailPhone;
    private TextView newOrderDetailNotes;
    private TextView newOrderDetailFinishTime;
    private TextView newOrderDetailOther;

    private GoogleMap mMap;
    private OrderListInfo.BodyBean.PageBean.ListBean order;

    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_new_order_detail);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        newOrderDetailToolbar = (Toolbar) findViewById(R.id.new_order_detail_toolbar);
        newOrderDetailDate = (TextView) findViewById(R.id.new_order_detail_date);
        newOrderDetailOrderNo = (TextView) findViewById(R.id.new_order_detail_order_no);
        newOrderDetailStartPoint = (TextView) findViewById(R.id.new_order_detail_start_point);
        newOrderDetailDistination = (TextView) findViewById(R.id.new_order_detail_distination);
        newOrderDetailPhone = (TextView) findViewById(R.id.new_order_detail_phone);
        newOrderDetailNotes = (TextView) findViewById(R.id.new_order_detail_notes);
        newOrderDetailFinishTime = (TextView) findViewById(R.id.new_order_detail_finish_time);
        newOrderDetailOther = (TextView) findViewById(R.id.new_order_detail_other);
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar(newOrderDetailToolbar, R.string.toolbar_title_order_detail, true, -1);
    }

    @Override
    protected void configViews()
    {
        super.configViews();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.new_order_detail_map);
        mapFragment.getMapAsync(this);

        order= (OrderListInfo.BodyBean.PageBean.ListBean) getIntent().getSerializableExtra("order");
        newOrderDetailDate.setText(order.getCreateDate());
        newOrderDetailOrderNo.setText(order.getOrderNo());
        newOrderDetailStartPoint.setText(order.getHotelAddr());
        newOrderDetailDistination.setText(order.getCustomerAddr());
        newOrderDetailPhone.setText(order.getCustomerTel());
        newOrderDetailNotes.setText(order.getRemark());
        newOrderDetailFinishTime.setText(order.getFinishedTime());
        newOrderDetailOther.setText(order.getRemarks().trim());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        configMapUiSettings();

        double customerLatitude = Double.parseDouble(order.getCustomerLatitude());
        double customerLongitude = Double.parseDouble(order.getCustomerLongitude());
        double hotelLatitude = Double.parseDouble(order.getHotelLatitude());
        double hotelLongitude = Double.parseDouble(order.getHotelLongitude());

        LatLng hotel = new LatLng(hotelLatitude, hotelLongitude);
        LatLng customer = new LatLng(customerLatitude, customerLongitude);

        MarkerOptions resOptions = new MarkerOptions()
                .position(hotel)
                .title(order.getHotelAddr())
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restaurant_location));
        MarkerOptions cusOptions = new MarkerOptions()
                .position(customer)
                .title(order.getCustomerAddr())
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_customer_location));

        mMap.addMarker(resOptions);
        mMap.addMarker(cusOptions);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(hotel));
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
