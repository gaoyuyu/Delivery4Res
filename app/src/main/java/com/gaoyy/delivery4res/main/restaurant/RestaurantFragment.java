package com.gaoyy.delivery4res.main.restaurant;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.base.BaseFragment;
import com.gaoyy.delivery4res.main.OrderDetailActivity;
import com.gaoyy.delivery4res.main.SearchLocationActivity;
import com.gaoyy.delivery4res.orderlist.OrderListActivity;
import com.gaoyy.delivery4res.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public class RestaurantFragment extends BaseFragment implements View.OnClickListener
{

    private TextInputLayout restPhoneTextinputlayout;
    private TextInputEditText restPhone;
    private TextInputLayout restAddressTextinputlayout;
    private TextInputEditText restAddress;
    private TextInputLayout restAptTextinputlayout;
    private TextInputEditText restApt;
    private Spinner restSpinner;
    private AppCompatCheckBox restDoorBell;
    private TextInputLayout restRemarkTextinputlayout;
    private TextInputEditText restRemark;
    private AppCompatButton restBtn;


    private List<RestInfo.BodyBean.RemarkDictBean> remarkDict;
    private List<RestInfo.BodyBean.FinishedTimeBean> finishedTime;
    private List<RestInfo.BodyBean.DictStatusBean> dictStatus;

    private ClearOrderInfoReceiver clearOrderInfoReceiver;

    public class ClearOrderInfoReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals("android.intent.action.ClearOrderInfoReceiver"))
            {
                restPhone.setText("");
                restAddress.setText("");
                restApt.setText("");
                restSpinner.setSelection(0);
                restDoorBell.setChecked(false);
                restRemark.setText("");
            }
        }
    }


    public RestaurantFragment()
    {
        // Required empty public constructor
    }

    public static RestaurantFragment newInstance()
    {
        RestaurantFragment fragment = new RestaurantFragment();
        return fragment;
    }


    @Override
    protected int getFragmentLayoutId()
    {
        return R.layout.fragment_restaurant;
    }


    @Override
    protected void getParamsData()
    {
        super.getParamsData();
        remarkDict = (List<RestInfo.BodyBean.RemarkDictBean>) getArguments().getSerializable("remarkDict");
        finishedTime = (List<RestInfo.BodyBean.FinishedTimeBean>) getArguments().getSerializable("finishedTime");
        dictStatus = (List<RestInfo.BodyBean.DictStatusBean>) getArguments().getSerializable("dictStatus");

    }

    @Override
    protected void assignViews(View rootView)
    {
        super.assignViews(rootView);
        restPhoneTextinputlayout = (TextInputLayout) rootView.findViewById(R.id.rest_phone_textinputlayout);
        restPhone = (TextInputEditText) rootView.findViewById(R.id.rest_phone);
        restAddressTextinputlayout = (TextInputLayout) rootView.findViewById(R.id.rest_address_textinputlayout);
        restAddress = (TextInputEditText) rootView.findViewById(R.id.rest_address);
        restAptTextinputlayout = (TextInputLayout) rootView.findViewById(R.id.rest_apt_textinputlayout);
        restApt = (TextInputEditText) rootView.findViewById(R.id.rest_apt);
        restSpinner = (Spinner) rootView.findViewById(R.id.rest_spinner);
        restDoorBell = (AppCompatCheckBox) rootView.findViewById(R.id.rest_door_bell);
        restRemarkTextinputlayout = (TextInputLayout) rootView.findViewById(R.id.rest_remark_textinputlayout);
        restRemark = (TextInputEditText) rootView.findViewById(R.id.rest_remark);
        restBtn = (AppCompatButton) rootView.findViewById(R.id.rest_btn);

        clearOrderInfoReceiver=new ClearOrderInfoReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.ClearOrderInfoReceiver");
        activity.registerReceiver(clearOrderInfoReceiver, filter);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        activity.unregisterReceiver(clearOrderInfoReceiver);
    }

    @Override
    protected void configViews()
    {
        super.configViews();

        // TODO: 2017/5/14 0014 设置不自动弹出输入法
        restPhone.clearFocus();
        restAddress.clearFocus();
        restAddress.setFocusable(false);
        restApt.clearFocus();
        restRemark.clearFocus();

        //设置备注项信息
        restDoorBell.setText(remarkDict.get(0).getLabel());

        ArrayList<String> finishedTimeList = new ArrayList<>();
        finishedTimeList.add("");
        for (int i = 0; i < finishedTime.size(); i++)
        {
            finishedTimeList.add(finishedTime.get(i).getLabel());
        }

        //适配器
        ArrayAdapter driverAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, finishedTimeList);
        //设置样式
        driverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        restSpinner.setAdapter(driverAdapter);


        //测试数据
//        int x = (int) (Math.random() * 1000000000);
//        restPhone.setText(x + "0");
//        int y = (int) (Math.random() * 100);
//        restApt.setText(y + "");
//        restRemark.setText(x + "  This is Test Data,PLEASE DO NOT ACCEPT THIS ORDER");

    }

    @Override
    protected void setListener()
    {
        super.setListener();

        //TODO:设置输入监听，跳转到searchactivity去获取位置信息
        restAddress.setOnClickListener(this);

        restBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1000:
                if (resultCode == activity.RESULT_OK)
                {
                    restAddress.setText(data.getStringExtra("place"));
                    //将错误信息去除
                    restAddressTextinputlayout.setError(null);
                    restAddressTextinputlayout.setErrorEnabled(false);
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        activity.getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_order_list:
                Intent orderList = new Intent();
                orderList.setClass(activity, OrderListActivity.class);
                startActivity(orderList);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * 验证输入
     */
    private void validate()
    {
        CommonUtils.textInputLayoutSetting(restPhone, restPhoneTextinputlayout, "Can't be empty");
        if (restPhone.getText().toString().length() != 10)
        {
            restPhoneTextinputlayout.setErrorEnabled(true);
            restPhoneTextinputlayout.setError("The phone number should be a 10-digit array");
        }
        else
        {
            restPhoneTextinputlayout.setError(null);
            restPhoneTextinputlayout.setErrorEnabled(false);
        }
        if (restAddress.getText().toString().equals("Click To Seach") || restAddress.getText().toString().equals(""))
        {
            restAddressTextinputlayout.setErrorEnabled(true);
            restAddressTextinputlayout.setError("Can't be empty");
        }
        else
        {
            restAddressTextinputlayout.setError(null);
            restAddressTextinputlayout.setErrorEnabled(false);
        }

//        CommonUtils.textInputLayoutSetting(restApt, restAptTextinputlayout, "Can't be empty");
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.rest_address:
                Intent searchOrder = new Intent();
                searchOrder.putExtra("inputingText", "");
                searchOrder.setClass(activity, SearchLocationActivity.class);
                startActivityForResult(searchOrder, 1000);
                break;
            case R.id.rest_btn:
                validate();
                if (restPhoneTextinputlayout.isErrorEnabled() || restAddressTextinputlayout.isErrorEnabled() || restAptTextinputlayout.isErrorEnabled())
                    break;
                String customerTel = restPhone.getText().toString();
                String customerAddr = restAddress.getText().toString();
                String apt = restApt.getText().toString();
                String finishedTime = (String) restSpinner.getSelectedItem();
                String remark = "";
                if (restDoorBell.isChecked())
                {
                    remark = restDoorBell.getText().toString();
                }
                String remarks = restRemark.getText().toString();

                Intent orderDetail = new Intent();
                orderDetail.putExtra("customerTel", customerTel);
                orderDetail.putExtra("customerAddr", customerAddr);
                orderDetail.putExtra("apt", apt);
                orderDetail.putExtra("finishedTime", finishedTime);
                orderDetail.putExtra("remark", remark);
                orderDetail.putExtra("remarks", remarks);
                orderDetail.setClass(activity, OrderDetailActivity.class);
                startActivity(orderDetail);
                break;
        }
    }

}
