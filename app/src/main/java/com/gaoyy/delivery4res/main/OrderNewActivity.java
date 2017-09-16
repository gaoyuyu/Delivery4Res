package com.gaoyy.delivery4res.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.RetrofitService;
import com.gaoyy.delivery4res.api.bean.CommonInfo;
import com.gaoyy.delivery4res.api.bean.OrderListInfo;
import com.gaoyy.delivery4res.api.bean.OrderNewInfo;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.base.CustomDialogFragment;
import com.gaoyy.delivery4res.util.CommonUtils;
import com.gaoyy.delivery4res.util.DialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderNewActivity extends BaseActivity implements View.OnClickListener
{

    private RelativeLayout activityOrderNew;
    private Toolbar orderNewToolbar;
    private ProgressBar orderNewProgressbar;
    private LinearLayout orderNewMainLayout;
    private TextView orderNewTip;
    private TextView orderNewShip;
    private TextView orderNewTax;
    private TextView orderNewTaxTvq;
    private TextView orderNewIncome;
    private TextView orderNewCoupon;
    private TextView orderNewAcivity;
    private TextView orderNewSum;
    private TextView orderNewRemark;
    private TextView orderNewEaTime;
    private Spinner orderNewSpinner;
    private LinearLayout orderNewOperationLayout;
    private AppCompatButton orderNewRefuseBtn;
    private AppCompatButton orderNewAcceptBtn;


    private TextView itemCommonAddtime;
    private TextView itemCommonAddress;
    private TextView itemCommonCustomer;
    private TextView itemCommonPhone;
    private TextView itemCommonNo;
    private LinearLayout itemCommonGoodsLayout;


    private OrderListInfo.BodyBean.PageBean.ListBean order;


    private List<String> expectTimeList = new ArrayList<>();


    private OrderNewInfo orderNewInfo;

    private CustomDialogFragment loading;


    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_order_new);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        activityOrderNew = (RelativeLayout) findViewById(R.id.activity_order_new);
        orderNewToolbar = (Toolbar) findViewById(R.id.order_new_toolbar);
        orderNewProgressbar = (ProgressBar) findViewById(R.id.order_new_progressbar);
        orderNewMainLayout = (LinearLayout) findViewById(R.id.order_new_main_layout);
        orderNewTip = (TextView) findViewById(R.id.order_new_tip);
        orderNewShip = (TextView) findViewById(R.id.order_new_ship);
        orderNewTax = (TextView) findViewById(R.id.order_new_tax);
        orderNewTaxTvq = (TextView) findViewById(R.id.order_new_tax_tvq);
        orderNewIncome = (TextView) findViewById(R.id.order_new_income);
        orderNewCoupon = (TextView) findViewById(R.id.order_new_coupon);
        orderNewAcivity = (TextView) findViewById(R.id.order_new_acivity);
        orderNewSum = (TextView) findViewById(R.id.order_new_sum);
        orderNewRemark = (TextView) findViewById(R.id.order_new_remark);
        orderNewEaTime = (TextView) findViewById(R.id.order_new_ea_time);
        orderNewSpinner = (Spinner) findViewById(R.id.order_new_spinner);
        orderNewOperationLayout = (LinearLayout) findViewById(R.id.order_new_operation_layout);
        orderNewRefuseBtn = (AppCompatButton) findViewById(R.id.order_new_refuse_btn);
        orderNewAcceptBtn = (AppCompatButton) findViewById(R.id.order_new_accept_btn);

        itemCommonAddtime = (TextView) findViewById(R.id.item_common_addtime);
        itemCommonAddress = (TextView) findViewById(R.id.item_common_address);
        itemCommonCustomer = (TextView) findViewById(R.id.item_common_customer);
        itemCommonPhone = (TextView) findViewById(R.id.item_common_phone);
        itemCommonNo = (TextView) findViewById(R.id.item_common_no);
        itemCommonGoodsLayout = (LinearLayout) findViewById(R.id.item_common_goods_layout);
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar(orderNewToolbar, R.string.toolbar_title_order_new, true, -1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.order_new_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        order = (OrderListInfo.BodyBean.PageBean.ListBean) getIntent().getSerializableExtra("order");

        String loginName = CommonUtils.getLoginName(this);
        String randomCode = CommonUtils.getRandomCode(this);
        String id = order.getId();
        String orderId = String.valueOf(order.getOrderId());

        Map<String, String> params = new HashMap<>();
        params.put("loginName", loginName);
        params.put("randomCode", randomCode);
        params.put("id", id);
        params.put("order_id", orderId);
        params.put("language", "zh");

        CommonUtils.httpDebugLogger("最新订单参数-->" + params.toString());
        Call<OrderNewInfo> call = RetrofitService.sApiService.newOrderDetail(params);

        setLoadingVisible();
        call.enqueue(new Callback<OrderNewInfo>()
        {
            @Override
            public void onResponse(Call<OrderNewInfo> call, Response<OrderNewInfo> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    orderNewInfo = response.body();

                    OrderNewInfo.BodyBean.ObjBean data = orderNewInfo.getBody().getObj();

                    Log.e(Constant.TAG, "bean->" + data.toString());


                    itemCommonAddtime.setText("" + data.getAddTime());
                    itemCommonAddress.setText("" + data.getAddr().getArea_info());
                    itemCommonCustomer.setText("" + data.getAddr().getTrueName());
                    itemCommonPhone.setText("" + data.getAddr().getMobile());
                    itemCommonNo.setText("" + data.getOrder_id());

                    List<OrderNewInfo.BodyBean.ObjBean.GcsBean> goods = data.getGcs();
                    itemCommonGoodsLayout.removeAllViews();

                    for (OrderNewInfo.BodyBean.ObjBean.GcsBean item : goods)
                    {
                        View root = LayoutInflater.from(OrderNewActivity.this).inflate(R.layout.item_food, itemCommonGoodsLayout, false);
                        TextView goodName = (TextView) root.findViewById(R.id.item_food_name);
                        TextView goodCount = (TextView) root.findViewById(R.id.item_food_count);
                        TextView goodPrice = (TextView) root.findViewById(R.id.item_food_price);
                        goodName.setText("" + item.getGoods_name());
                        goodCount.setText("x" + item.getCount());
                        goodPrice.setText("$" + item.getPrice());
                        itemCommonGoodsLayout.addView(root);
                    }
                    //设置价格
                    setPrice(data);
                    //总计
                    orderNewSum.setText("" + data.getTotalPrice() + "");
                    //备注
                    orderNewRemark.setText("");
                    //期望送达时间
                    orderNewEaTime.setText("" + data.getAppointment_time());

                    expectTimeList.add("now");
                    expectTimeList.add("20min");
                    expectTimeList.add("40min");
                    expectTimeList.add("60min");
                    //适配器
                    ArrayAdapter adapter = new ArrayAdapter<String>(OrderNewActivity.this, android.R.layout.simple_spinner_item, expectTimeList);
                    //设置样式
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //加载适配器
                    orderNewSpinner.setAdapter(adapter);

                    setLoadingGone();

                    /**
                     * flage  0-没接单 1-已接单
                     * 已接单-不显示拒绝按钮，接单按钮，预计送达时间
                     */
                    if (1 == Integer.valueOf(orderNewInfo.getBody().getFlag()) )
                    {
                        orderNewOperationLayout.setVisibility(View.GONE);
                        ((LinearLayout) (orderNewSpinner.getParent())).setVisibility(View.GONE);

                    }
                    else
                    {
                        orderNewOperationLayout.setVisibility(View.VISIBLE);
                        ((LinearLayout) (orderNewSpinner.getParent())).setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(Call<OrderNewInfo> call, Throwable t)
            {
                Log.e(Constant.TAG, "error->" + t.toString());
            }
        });

    }

    /**
     * 设置价格
     *
     * @param data
     */
    private void setPrice(OrderNewInfo.BodyBean.ObjBean data)
    {
        //小费
        if (data.getTipPrice() == null || (Double) (data.getTipPrice()) == 0.0)
        {
            ((LinearLayout) (orderNewTip.getParent())).setVisibility(View.GONE);
        }
        else
        {
            orderNewTip.setText("" + data.getTipPrice());
        }

        //配送费
        if (data.getShip_price() == null || (Double) (data.getShip_price()) == 0.0)
        {
            ((LinearLayout) (orderNewShip.getParent())).setVisibility(View.GONE);
        }
        else
        {
            orderNewShip.setText("" + data.getShip_price());
        }

        //税1
        if (data.getTaxation() == null || (Double) (data.getTaxation()) == 0.0)
        {
            ((LinearLayout) (orderNewTax.getParent())).setVisibility(View.GONE);
        }
        else
        {
            orderNewTax.setText("" + data.getTaxation());
        }

        //税2
        if (data.getTaxation_tvq() == null || (Double) (data.getTaxation_tvq()) == 0.0)
        {
            ((LinearLayout) (orderNewTaxTvq.getParent())).setVisibility(View.GONE);
        }
        else
        {
            orderNewTaxTvq.setText("" + data.getTaxation_tvq());
        }

        //收益
        if (data.getUseIncomePrice() == null || (Double) (data.getUseIncomePrice()) == 0.0)
        {
            ((LinearLayout) (orderNewIncome.getParent())).setVisibility(View.GONE);
        }
        else
        {
            orderNewIncome.setText("-" + data.getUseIncomePrice());
        }

        //代金券
        if (data.getCouponPrice() == null || (Double) (data.getCouponPrice()) == 0.0)
        {
            ((LinearLayout) (orderNewCoupon.getParent())).setVisibility(View.GONE);
        }
        else
        {
            orderNewCoupon.setText("-" + data.getCouponPrice());
        }

        //商家满减
        if (data.getActivityPrice() == null || (Double) (data.getActivityPrice()) == 0.0)
        {
            ((LinearLayout) (orderNewAcivity.getParent())).setVisibility(View.GONE);
        }
        else
        {
            orderNewAcivity.setText("-" + data.getActivityPrice());
        }
    }

    private void setLoadingVisible()
    {
        orderNewProgressbar.setVisibility(View.VISIBLE);
        orderNewMainLayout.setVisibility(View.GONE);
        orderNewOperationLayout.setVisibility(View.GONE);
    }

    private void setLoadingGone()
    {
        orderNewProgressbar.setVisibility(View.GONE);
        orderNewMainLayout.setVisibility(View.VISIBLE);
        orderNewOperationLayout.setVisibility(View.VISIBLE);
    }


    @Override
    protected void setListener()
    {
        super.setListener();
        orderNewRefuseBtn.setOnClickListener(this);
        orderNewAcceptBtn.setOnClickListener(this);
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
            case R.id.action_print_order:
                Intent intent = new Intent(OrderNewActivity.this, PrintActivity.class);
                intent.putExtra("orderNewInfo", orderNewInfo);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.order_new_refuse_btn:
                AlertDialog.Builder dialog = new AlertDialog.Builder(OrderNewActivity.this);
                dialog.setTitle(R.string.reply_order).setMessage(R.string.alert_confirm_refuse);
                dialog.setPositiveButton(R.string.alert_confirm,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                orderRefuse();
                            }
                        })
                        .setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                orderRefuse();
                break;
            case R.id.order_new_accept_btn:
                orderAccept();
                break;
        }
    }

    public void showLoading()
    {
        loading = DialogUtils.showLoadingDialog(this);
    }

    public void hideLoading()
    {
        loading.dismiss();
    }


    /**
     * 接单
     */
    private void orderAccept()
    {
        Map<String, String> params = new HashMap<>();
        params.put("id", order.getId());
        params.put("order_id", String.valueOf(order.getOrderId()));
        params.put("finishedTime", orderNewSpinner.getSelectedItem() + "");
        params.put("loginName", CommonUtils.getLoginName(this));
        params.put("randomCode", CommonUtils.getRandomCode(this));
        CommonUtils.httpDebugLogger("接受订单请求参数" + params.toString());
        Call<CommonInfo> refuse = RetrofitService.sApiService.orderAccept(params);
        showLoading();
        refuse.enqueue(new Callback<CommonInfo>()
        {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response)
            {
                hideLoading();
                if (response.isSuccessful() && response.body() != null)
                {
                    CommonInfo commonInfo = response.body();
                    CommonUtils.showToast(OrderNewActivity.this, commonInfo.getMsg());
                    if(commonInfo.isSuccess())
                    {
                        Intent intent = new Intent(OrderNewActivity.this, PrintActivity.class);
                        intent.putExtra("orderNewInfo", orderNewInfo);
                        startActivity(intent);
                    }

                }
                else
                {
                    CommonUtils.showToast(OrderNewActivity.this, "接受订单请求失败");
                }

            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t)
            {
                hideLoading();
                CommonUtils.httpErrorLogger(t.toString());
            }
        });
    }

    /**
     * 拒绝接单
     */
    private void orderRefuse()
    {
        Map<String, String> params = new HashMap<>();
        params.put("id", order.getId());
        params.put("order_id", String.valueOf(order.getOrderId()));
        params.put("loginName", CommonUtils.getLoginName(this));
        params.put("randomCode", CommonUtils.getRandomCode(this));
        CommonUtils.httpDebugLogger("拒绝接单请求参数" + params.toString());
        Call<CommonInfo> refuse = RetrofitService.sApiService.orderRefuse(params);

        showLoading();
        refuse.enqueue(new Callback<CommonInfo>()
        {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response)
            {
                hideLoading();
                if (response.isSuccessful() && response.body() != null)
                {
                    CommonInfo commonInfo = response.body();
                    CommonUtils.showToast(OrderNewActivity.this, commonInfo.getMsg());
                    if (commonInfo.isSuccess())
                    {
                        orderNewOperationLayout.setVisibility(View.GONE);
                        ((LinearLayout) (orderNewSpinner.getParent())).setVisibility(View.GONE);
                    }
                }
                else
                {
                    CommonUtils.showToast(OrderNewActivity.this, "拒绝接单请求失败");
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t)
            {
                hideLoading();
                CommonUtils.httpErrorLogger(t.toString());
            }
        });
    }
}