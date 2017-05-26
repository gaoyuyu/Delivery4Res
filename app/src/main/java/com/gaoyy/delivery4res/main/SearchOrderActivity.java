package com.gaoyy.delivery4res.main;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.orderlist.OrderListActivity;
import com.gaoyy.delivery4res.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchOrderActivity extends BaseActivity
{
    private Toolbar searchOrderToolbar;
    private LinearLayout activitySearchOrder;
    private TextInputLayout searchOrderNumberTextinputlayout;
    private TextInputEditText searchOrderNumber;
    private TextInputLayout searchDriverPhoneTextinputlayout;
    private TextInputEditText searchDriverPhone;
    private TextInputLayout searchCustomerPhoneTextinputlayout;
    private TextInputEditText searchCustomerPhone;
    private Spinner searchOrderStatus;



    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_search_order);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        searchOrderToolbar = (Toolbar) findViewById(R.id.search_order_toolbar);
        activitySearchOrder = (LinearLayout) findViewById(R.id.activity_search_order);
        searchOrderNumberTextinputlayout = (TextInputLayout) findViewById(R.id.search_order_number_textinputlayout);
        searchOrderNumber = (TextInputEditText) findViewById(R.id.search_order_number);
        searchDriverPhoneTextinputlayout = (TextInputLayout) findViewById(R.id.search_driver_phone_textinputlayout);
        searchDriverPhone = (TextInputEditText) findViewById(R.id.search_driver_phone);
        searchCustomerPhoneTextinputlayout = (TextInputLayout) findViewById(R.id.search_customer_phone_textinputlayout);
        searchCustomerPhone = (TextInputEditText) findViewById(R.id.search_customer_phone);
        searchOrderStatus = (Spinner) findViewById(R.id.search_order_status);
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar(searchOrderToolbar,R.string.toolbar_title_search_order,true,-1);
    }

    @Override
    protected void configViews()
    {
        super.configViews();

        searchCustomerPhone.setText("8956231234");

        setStatusToSpinner();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.d(Constant.TAG,"SearchOrderActivity onNewIntent");

    }

    /**
     * 填充数据至Spinner
     */
    private void setStatusToSpinner()
    {
        ArrayList<String> statusList = new ArrayList<>();
        List<RestInfo.BodyBean.DictStatusBean> dictStatus = MainActivity.dictStatus;
        statusList.add("All");
        for (int i = 0; i < dictStatus.size(); i++)
        {
            statusList.add(dictStatus.get(i).getLabel());
        }
        //适配器
        ArrayAdapter driverAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusList);
        //设置样式
        driverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        searchOrderStatus.setAdapter(driverAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.order_detail_menu, menu);
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
            case R.id.action_submit:
                Intent intent = new Intent(this, OrderListActivity.class);
                if(validate())
                {
                    String orderNo = searchOrderNumber.getText().toString();
                    String driverPhone = searchDriverPhone.getText().toString();
                    String customerPhone = searchCustomerPhone.getText().toString();

                    String status = (String)searchOrderStatus.getSelectedItem();

                    Log.d(Constant.TAG,"status==>"+status);

                    if(!orderNo.equals("")) intent.putExtra("orderNo",orderNo);
                    if(!driverPhone.equals("")) intent.putExtra("driverPhone",driverPhone);
                    if(!customerPhone.equals("")) intent.putExtra("customerPhone",customerPhone);
                    intent.putExtra("status",status);
                    startActivity(intent);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validate()
    {
        if(searchOrderNumber.getText().toString().equals("")&&searchDriverPhone.getText().toString().equals("")&&searchCustomerPhone.getText().toString().equals(""))
        {
            CommonUtils.showSnackBar(searchOrderToolbar,getResources().getString(R.string.cannt_be_empty_same_time));
            return false;
        }
        else
        {
            return true;
        }
    }
}
