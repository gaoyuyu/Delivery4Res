package com.gaoyy.delivery4res.main.restaurant;


import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.gaoyy.delivery4res.main.SearchActivity;
import com.gaoyy.delivery4res.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public class RestaurantFragment extends BaseFragment
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


    private List<RestInfo.BodyBean.RemarkDictBean> remarkDict;
    private List<RestInfo.BodyBean.FinishedTimeBean> finishedTime;
    private List<RestInfo.BodyBean.DictStatusBean> dictStatus;


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
    }

    @Override
    protected void configViews()
    {
        super.configViews();

        //设置备注项信息
        restDoorBell.setText(remarkDict.get(0).getLabel());

        ArrayList<String> finishedTimeList = new ArrayList<>();
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
        int x=(int)(Math.random()*1000000000);
        restPhone.setText(x+"0");
        int y=(int)(Math.random()*100);
        restApt.setText(y+"");
        restRemark.setText(x+"  This is Test Data,PLEASE DO NOT ACCEPT THIS ORDER");
    }

    @Override
    protected void setListener()
    {
        super.setListener();

        //TODO:设置输入监听，跳转到searchactivity去获取位置信息
        restAddress.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                String inputingText = editable.toString();
                Intent intent = new Intent();
                intent.putExtra("inputingText", inputingText);
                intent.setClass(activity, SearchActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
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
                    Log.d("TAG", "收到返回值了收到了了子了了了了了了子了了了了了了了");
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
            case R.id.action_send_order:
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

                Intent intent = new Intent();
                intent.putExtra("customerTel", customerTel);
                intent.putExtra("customerAddr", customerAddr);
                intent.putExtra("apt", apt);
                intent.putExtra("finishedTime", finishedTime);
                intent.putExtra("remark", remark);
                intent.putExtra("remarks", remarks);
                intent.setClass(activity, OrderDetailActivity.class);
                startActivity(intent);


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
        CommonUtils.textInputLayoutSetting(restAddress, restAddressTextinputlayout, "Can't be empty");
        CommonUtils.textInputLayoutSetting(restApt, restAptTextinputlayout, "Can't be empty");
    }
}
