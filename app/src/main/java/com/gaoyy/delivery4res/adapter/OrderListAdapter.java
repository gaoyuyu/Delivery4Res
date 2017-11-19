package com.gaoyy.delivery4res.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.OrderListInfo;
import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.main.MainActivity;

import java.util.List;

/**
 * Created by gaoyy on 2016/8/24 0024.
 */
public class OrderListAdapter extends BaseQuickAdapter<OrderListInfo.BodyBean.PageBean.ListBean, BaseViewHolder>
{

    private View goodsView;
    private AppCompatButton itemOrderCancleBtn;
    private AppCompatButton itemOrderResubmitBtn;
    private AppCompatButton itemOrderDeliveryBtn;
    private AppCompatButton itemOrderCancleAfterDeliveryBtn;
    private AppCompatButton itemOrderMakingFinishBtn;
    private CardView itemOrderCardView;

    public OrderListAdapter(@Nullable List<OrderListInfo.BodyBean.PageBean.ListBean> data)
    {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListInfo.BodyBean.PageBean.ListBean item)
    {
        int orderStatus = item.getStatus();
        List<RestInfo.BodyBean.DictStatusBean> dictStatus = MainActivity.dictStatus;
        helper.setText(R.id.item_order_address, item.getCustomerAddr())
                .setText(R.id.item_order_driver_name, item.getCourierName())
                .setText(R.id.item_order_customer_phone, item.getCustomerTel())
                .setText(R.id.item_order_no, item.getOrderNo())
                .setText(R.id.item_order_notes, item.getRemarks())
                .setText(R.id.item_order_finished_time, item.getFinishedTime())
                .setText(R.id.item_order_status_date, item.getCreateDate());

        //在判断有无goodsinfo数据之前，先移除view
        LinearLayout itemOrderFoodList = helper.getView(R.id.item_order_food_list);
        itemOrderFoodList.removeAllViews();
        if (item.getGoodsInfo() != null)
        {
            String goodsInfo = String.valueOf(item.getGoodsInfo());
            String[] goods = goodsInfo.split(";");
            for (int i = 0; i < goods.length; i++)
            {
                Log.d(Constant.TAG, "goods " + i + "  " + goods[i]);
                String[] items = goods[i].split(",");
                goodsView = mLayoutInflater.inflate(R.layout.item_food, itemOrderFoodList, false);
                TextView itemFoodName = (TextView) goodsView.findViewById(R.id.item_food_name);
                TextView itemFoodCount = (TextView) goodsView.findViewById(R.id.item_food_count);
                TextView itemFoodPrice = (TextView) goodsView.findViewById(R.id.item_food_price);
                itemFoodName.setText(items[0]);
                itemFoodCount.setText("x" + items[1]);
                itemFoodPrice.setText("$" + items[2]);
                itemOrderFoodList.addView(goodsView);
            }
        }

        itemOrderCancleBtn = helper.getView(R.id.item_order_cancle_btn);
        itemOrderResubmitBtn = helper.getView(R.id.item_order_resubmit_btn);
        itemOrderDeliveryBtn = helper.getView(R.id.item_order_delivery_btn);
        itemOrderCancleAfterDeliveryBtn = helper.getView(R.id.item_order_cancle_after_delivery_btn);
        itemOrderMakingFinishBtn = helper.getView(R.id.item_order_making_finish_btn);
        itemOrderCardView = helper.getView(R.id.item_order_cardview);

        itemOrderCancleBtn.setTag(item);
        itemOrderResubmitBtn.setTag(item);
        itemOrderDeliveryBtn.setTag(item);
        itemOrderCancleAfterDeliveryBtn.setTag(item);
        itemOrderMakingFinishBtn.setTag(item);
        itemOrderCardView.setTag(item);

        //为item中的btn设置点击监听
        helper.addOnClickListener(R.id.item_order_cancle_btn)
                .addOnClickListener(R.id.item_order_resubmit_btn)
                .addOnClickListener(R.id.item_order_delivery_btn)
                .addOnClickListener(R.id.item_order_cancle_after_delivery_btn)
                .addOnClickListener(R.id.item_order_making_finish_btn);

        setUpStatusBtn(helper, item, orderStatus, dictStatus);
    }

    /**
     * 根据订单状态设置按钮效果
     *
     * @param holder
     * @param itemData
     * @param orderStatus
     * @param dictStatus
     */
    private void setUpStatusBtn(BaseViewHolder holder, OrderListInfo.BodyBean.PageBean.ListBean itemData, int orderStatus, List<RestInfo.BodyBean.DictStatusBean> dictStatus)
    {
        TextView itemOrderStatus = holder.getView(R.id.item_order_status);
        LinearLayout itemOrderDriverNameLayout = holder.getView(R.id.item_order_driver_name_layout);
        LinearLayout itemOrderOperationLayout = holder.getView(R.id.item_order_operation_layout);
        for (int i = 0; i < dictStatus.size(); i++)
        {
            int value = Integer.valueOf(dictStatus.get(i).getValue());
            //Wait
            if ((orderStatus == value) && (value == 0))
            {
                if ((itemData.getIsplatformDt() != null) && (itemData.getIsplatformDt().equals("0")))
                {
                    //这里需要显示为状态：1-Accept
                    itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_orange_dark));
                    itemOrderStatus.setText(R.string.status_accept);

                    itemOrderDriverNameLayout.setVisibility(View.VISIBLE);

                    //显示按钮组
                    itemOrderOperationLayout.setVisibility(View.VISIBLE);
                    //显示Cancle按钮和Finish按钮
                    itemOrderCancleBtn.setVisibility(View.VISIBLE);
                    itemOrderResubmitBtn.setVisibility(View.GONE);
                    itemOrderDeliveryBtn.setVisibility(View.GONE);
                    itemOrderCancleAfterDeliveryBtn.setVisibility(View.GONE);
                    itemOrderMakingFinishBtn.setVisibility(View.VISIBLE);
                }
                else
                {
                    itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                    itemOrderStatus.setText(R.string.status_wait);
                    itemOrderDriverNameLayout.setVisibility(View.GONE);
                    //显示按钮组
                    itemOrderOperationLayout.setVisibility(View.VISIBLE);
                    //显示Cancle按钮和resubmit按钮
                    itemOrderCancleBtn.setVisibility(View.VISIBLE);
                    if (itemData.getIsTimeout() == 1)
                    {
                        itemOrderResubmitBtn.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        itemOrderResubmitBtn.setVisibility(View.GONE);
                    }
                    itemOrderDeliveryBtn.setVisibility(View.GONE);
                    itemOrderCancleAfterDeliveryBtn.setVisibility(View.GONE);
                    itemOrderMakingFinishBtn.setVisibility(View.GONE);
                }


            }
            //Accept
            else if ((orderStatus == value) && (value == 1))
            {
                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_orange_dark));
                itemOrderStatus.setText(R.string.status_accept);

                itemOrderDriverNameLayout.setVisibility(View.VISIBLE);

                //显示按钮组
                itemOrderOperationLayout.setVisibility(View.VISIBLE);
                //显示Cancle按钮和Delivery按钮
                itemOrderCancleBtn.setVisibility(View.VISIBLE);
                itemOrderResubmitBtn.setVisibility(View.GONE);
                itemOrderDeliveryBtn.setVisibility(View.VISIBLE);
                itemOrderCancleAfterDeliveryBtn.setVisibility(View.GONE);
                itemOrderMakingFinishBtn.setVisibility(View.GONE);

            }
            //Delivery
            else if ((orderStatus == value) && (value == 2))
            {
                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
                itemOrderStatus.setText(R.string.status_delivery);
                //显示按钮组
                itemOrderOperationLayout.setVisibility(View.VISIBLE);
                //显示CancleAfterDelivery按钮
                itemOrderCancleBtn.setVisibility(View.GONE);
                itemOrderResubmitBtn.setVisibility(View.GONE);
                itemOrderDeliveryBtn.setVisibility(View.GONE);
                itemOrderCancleAfterDeliveryBtn.setVisibility(View.VISIBLE);
                itemOrderMakingFinishBtn.setVisibility(View.GONE);
            }
            //Finish
            else if ((orderStatus == value) && (value == 3))
            {
                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
                itemOrderStatus.setText(R.string.status_finish);

                itemOrderDriverNameLayout.setVisibility(View.VISIBLE);

                //不显示按钮
                itemOrderOperationLayout.setVisibility(View.GONE);
            }
            //Cancel
            else if ((orderStatus == value) && (value == 4))
            {
                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_purple));
                itemOrderStatus.setText(R.string.status_cancle);

                itemOrderDriverNameLayout.setVisibility(View.GONE);

                //不显示按钮
                itemOrderOperationLayout.setVisibility(View.GONE);
            }
            //Back
            else if ((orderStatus == value) && (value == 5))
            {
                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_purple));
                itemOrderStatus.setText(R.string.status_back);

                itemOrderDriverNameLayout.setVisibility(View.GONE);

                //不显示按钮
                itemOrderOperationLayout.setVisibility(View.GONE);
            }
            //New
            else if ((orderStatus == value) && (value == 6))
            {
                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
                itemOrderStatus.setText(R.string.status_new);

                itemOrderDriverNameLayout.setVisibility(View.GONE);

                //不显示按钮
                itemOrderOperationLayout.setVisibility(View.GONE);
            }
            //Making
            else if ((orderStatus == value) && (value == 7))
            {
                if ((itemData.getIsplatformDt() != null) && (itemData.getIsplatformDt().equals("0")))
                {
                    //这里需要显示为状态：1-Accept
                    itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_orange_dark));
                    itemOrderStatus.setText(R.string.status_accept);

                    itemOrderDriverNameLayout.setVisibility(View.VISIBLE);

                    //显示按钮组
                    itemOrderOperationLayout.setVisibility(View.VISIBLE);
                    //显示Cancle按钮和Finish按钮
                    itemOrderCancleBtn.setVisibility(View.VISIBLE);
                    itemOrderResubmitBtn.setVisibility(View.GONE);
                    itemOrderDeliveryBtn.setVisibility(View.GONE);
                    itemOrderCancleAfterDeliveryBtn.setVisibility(View.GONE);
                    itemOrderMakingFinishBtn.setVisibility(View.VISIBLE);
                }
                else
                {
                    itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_light));
                    itemOrderStatus.setText(R.string.status_making);
                    //DistributionType 配送方式 Pick-up 自提，Delivery 配送
                    if ((itemData.getOrderType() == 1) && (itemData.getDistributionType().equals("Pick-Up")))
                    {
                        //显示Finish按钮
                        itemOrderCancleBtn.setVisibility(View.GONE);
                        itemOrderResubmitBtn.setVisibility(View.GONE);
                        itemOrderDeliveryBtn.setVisibility(View.GONE);
                        itemOrderCancleAfterDeliveryBtn.setVisibility(View.GONE);
                        itemOrderMakingFinishBtn.setVisibility(View.VISIBLE);
                        //显示按钮组
                        itemOrderOperationLayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        //不显示按钮
                        itemOrderOperationLayout.setVisibility(View.GONE);
                    }
                }


            }
        }
    }
}
//public class OrderListAdapter extends RecyclerBaseAdapter<OrderListInfo.BodyBean.PageBean.ListBean>
//{
//    private View goodsView;
//
//    private AppCompatButton itemOrderCancleBtn;
//    private AppCompatButton itemOrderResubmitBtn;
//    private AppCompatButton itemOrderDeliveryBtn;
//    private AppCompatButton itemOrderCancleAfterDeliveryBtn;
//    private AppCompatButton itemOrderMakingFinishBtn;
//    private CardView itemOrderCardView;
//
//    public OrderListAdapter(Context context, List<OrderListInfo.BodyBean.PageBean.ListBean> data)
//    {
//        super(context, R.layout.item_order, data);
//    }
//
//    @Override
//    protected void bindData(BaseViewHolder holder, OrderListInfo.BodyBean.PageBean.ListBean itemData, int position)
//    {
//        int orderStatus = itemData.getStatus();
//        List<RestInfo.BodyBean.DictStatusBean> dictStatus = MainActivity.dictStatus;
//        holder.setText(R.id.item_order_address, itemData.getCustomerAddr())
//                .setText(R.id.item_order_driver_name, itemData.getCourierName())
//                .setText(R.id.item_order_customer_phone, itemData.getCustomerTel())
//                .setText(R.id.item_order_no, itemData.getOrderNo())
//                .setText(R.id.item_order_notes, itemData.getRemarks())
//                .setText(R.id.item_order_finished_time, itemData.getFinishedTime())
//                .setText(R.id.item_order_status_date, itemData.getCreateDate());
//
//        //在判断有无goodsinfo数据之前，先移除view
//        LinearLayout itemOrderFoodList = holder.getView(R.id.item_order_food_list);
//        itemOrderFoodList.removeAllViews();
//        if (itemData.getGoodsInfo() != null)
//        {
//            String goodsInfo = String.valueOf(itemData.getGoodsInfo());
//            String[] goods = goodsInfo.split(";");
//            for (int i = 0; i < goods.length; i++)
//            {
//                Log.d(Constant.TAG, "goods " + i + "  " + goods[i]);
//                String[] items = goods[i].split(",");
//                goodsView = mLayoutInflater.inflate(R.layout.item_food, itemOrderFoodList, false);
//                TextView itemFoodName = (TextView) goodsView.findViewById(R.id.item_food_name);
//                TextView itemFoodCount = (TextView) goodsView.findViewById(R.id.item_food_count);
//                TextView itemFoodPrice = (TextView) goodsView.findViewById(R.id.item_food_price);
//                itemFoodName.setText(items[0]);
//                itemFoodCount.setText("x" + items[1]);
//                itemFoodPrice.setText("$" + items[2]);
//                itemOrderFoodList.addView(goodsView);
//            }
//        }
//
//        itemOrderCancleBtn = holder.getView(R.id.item_order_cancle_btn);
//        itemOrderResubmitBtn = holder.getView(R.id.item_order_resubmit_btn);
//        itemOrderDeliveryBtn = holder.getView(R.id.item_order_delivery_btn);
//        itemOrderCancleAfterDeliveryBtn = holder.getView(R.id.item_order_cancle_after_delivery_btn);
//        itemOrderMakingFinishBtn = holder.getView(R.id.item_order_making_finish_btn);
//        itemOrderCardView = holder.getView(R.id.item_order_cardview);
//        setUpStatusBtn(holder, itemData, orderStatus, dictStatus);
//        //设置btn的点击监听器
//        if (onItemClickListener != null)
//        {
//            itemOrderCancleBtn.setOnClickListener(new BasicOnClickListener(holder, itemData));
//            itemOrderResubmitBtn.setOnClickListener(new BasicOnClickListener(holder, itemData));
//            itemOrderDeliveryBtn.setOnClickListener(new BasicOnClickListener(holder, itemData));
//            itemOrderCancleAfterDeliveryBtn.setOnClickListener(new BasicOnClickListener(holder, itemData));
//            itemOrderMakingFinishBtn.setOnClickListener(new BasicOnClickListener(holder, itemData));
//            itemOrderCardView.setOnClickListener(new BasicOnClickListener(holder, itemData));
//        }
//    }
//
//    /**
//     * 根据订单状态设置按钮效果
//     *
//     * @param holder
//     * @param itemData
//     * @param orderStatus
//     * @param dictStatus
//     */
//    private void setUpStatusBtn(BaseViewHolder holder, OrderListInfo.BodyBean.PageBean.ListBean itemData, int orderStatus, List<RestInfo.BodyBean.DictStatusBean> dictStatus)
//    {
//        TextView itemOrderStatus = holder.getView(R.id.item_order_status);
//        LinearLayout itemOrderDriverNameLayout = holder.getView(R.id.item_order_driver_name_layout);
//        LinearLayout itemOrderOperationLayout = holder.getView(R.id.item_order_operation_layout);
//        for (int i = 0; i < dictStatus.size(); i++)
//        {
//            int value = Integer.valueOf(dictStatus.get(i).getValue());
//            //Wait
//            if ((orderStatus == value) && (value == 0))
//            {
//                if ((itemData.getIsplatformDt() != null) && (itemData.getIsplatformDt().equals("0")))
//                {
//                    //这里需要显示为状态：1-Accept
//                    itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_orange_dark));
//                    itemOrderStatus.setText(R.string.status_accept);
//
//                    itemOrderDriverNameLayout.setVisibility(View.VISIBLE);
//
//                    //显示按钮组
//                    itemOrderOperationLayout.setVisibility(View.VISIBLE);
//                    //显示Cancle按钮和Finish按钮
//                    itemOrderCancleBtn.setVisibility(View.VISIBLE);
//                    itemOrderResubmitBtn.setVisibility(View.GONE);
//                    itemOrderDeliveryBtn.setVisibility(View.GONE);
//                    itemOrderCancleAfterDeliveryBtn.setVisibility(View.GONE);
//                    itemOrderMakingFinishBtn.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
//                    itemOrderStatus.setText(R.string.status_wait);
//                    itemOrderDriverNameLayout.setVisibility(View.GONE);
//                    //显示按钮组
//                    itemOrderOperationLayout.setVisibility(View.VISIBLE);
//                    //显示Cancle按钮和resubmit按钮
//                    itemOrderCancleBtn.setVisibility(View.VISIBLE);
//                    if (itemData.getIsTimeout() == 1)
//                    {
//                        itemOrderResubmitBtn.setVisibility(View.VISIBLE);
//                    }
//                    else
//                    {
//                        itemOrderResubmitBtn.setVisibility(View.GONE);
//                    }
//                    itemOrderDeliveryBtn.setVisibility(View.GONE);
//                    itemOrderCancleAfterDeliveryBtn.setVisibility(View.GONE);
//                    itemOrderMakingFinishBtn.setVisibility(View.GONE);
//                }
//
//
//            }
//            //Accept
//            else if ((orderStatus == value) && (value == 1))
//            {
//                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_orange_dark));
//                itemOrderStatus.setText(R.string.status_accept);
//
//                itemOrderDriverNameLayout.setVisibility(View.VISIBLE);
//
//                //显示按钮组
//                itemOrderOperationLayout.setVisibility(View.VISIBLE);
//                //显示Cancle按钮和Delivery按钮
//                itemOrderCancleBtn.setVisibility(View.VISIBLE);
//                itemOrderResubmitBtn.setVisibility(View.GONE);
//                itemOrderDeliveryBtn.setVisibility(View.VISIBLE);
//                itemOrderCancleAfterDeliveryBtn.setVisibility(View.GONE);
//                itemOrderMakingFinishBtn.setVisibility(View.GONE);
//
//            }
//            //Delivery
//            else if ((orderStatus == value) && (value == 2))
//            {
//                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
//                itemOrderStatus.setText(R.string.status_delivery);
//                //显示按钮组
//                itemOrderOperationLayout.setVisibility(View.VISIBLE);
//                //显示CancleAfterDelivery按钮
//                itemOrderCancleBtn.setVisibility(View.GONE);
//                itemOrderResubmitBtn.setVisibility(View.GONE);
//                itemOrderDeliveryBtn.setVisibility(View.GONE);
//                itemOrderCancleAfterDeliveryBtn.setVisibility(View.VISIBLE);
//                itemOrderMakingFinishBtn.setVisibility(View.GONE);
//            }
//            //Finish
//            else if ((orderStatus == value) && (value == 3))
//            {
//                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
//                itemOrderStatus.setText(R.string.status_finish);
//
//                itemOrderDriverNameLayout.setVisibility(View.VISIBLE);
//
//                //不显示按钮
//                itemOrderOperationLayout.setVisibility(View.GONE);
//            }
//            //Cancel
//            else if ((orderStatus == value) && (value == 4))
//            {
//                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_purple));
//                itemOrderStatus.setText(R.string.status_cancle);
//
//                itemOrderDriverNameLayout.setVisibility(View.GONE);
//
//                //不显示按钮
//                itemOrderOperationLayout.setVisibility(View.GONE);
//            }
//            //Back
//            else if ((orderStatus == value) && (value == 5))
//            {
//                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_purple));
//                itemOrderStatus.setText(R.string.status_back);
//
//                itemOrderDriverNameLayout.setVisibility(View.GONE);
//
//                //不显示按钮
//                itemOrderOperationLayout.setVisibility(View.GONE);
//            }
//            //New
//            else if ((orderStatus == value) && (value == 6))
//            {
//                itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
//                itemOrderStatus.setText(R.string.status_new);
//
//                itemOrderDriverNameLayout.setVisibility(View.GONE);
//
//                //不显示按钮
//                itemOrderOperationLayout.setVisibility(View.GONE);
//            }
//            //Making
//            else if ((orderStatus == value) && (value == 7))
//            {
//                if ((itemData.getIsplatformDt() != null) && (itemData.getIsplatformDt().equals("0")))
//                {
//                    //这里需要显示为状态：1-Accept
//                    itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_orange_dark));
//                    itemOrderStatus.setText(R.string.status_accept);
//
//                    itemOrderDriverNameLayout.setVisibility(View.VISIBLE);
//
//                    //显示按钮组
//                    itemOrderOperationLayout.setVisibility(View.VISIBLE);
//                    //显示Cancle按钮和Finish按钮
//                    itemOrderCancleBtn.setVisibility(View.VISIBLE);
//                    itemOrderResubmitBtn.setVisibility(View.GONE);
//                    itemOrderDeliveryBtn.setVisibility(View.GONE);
//                    itemOrderCancleAfterDeliveryBtn.setVisibility(View.GONE);
//                    itemOrderMakingFinishBtn.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    itemOrderStatus.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_light));
//                    itemOrderStatus.setText(R.string.status_making);
//                    //DistributionType 配送方式 Pick-up 自提，Delivery 配送
//                    if ((itemData.getOrderType() == 1) && (itemData.getDistributionType().equals("Pick-Up")))
//                    {
//                        //显示Finish按钮
//                        itemOrderCancleBtn.setVisibility(View.GONE);
//                        itemOrderResubmitBtn.setVisibility(View.GONE);
//                        itemOrderDeliveryBtn.setVisibility(View.GONE);
//                        itemOrderCancleAfterDeliveryBtn.setVisibility(View.GONE);
//                        itemOrderMakingFinishBtn.setVisibility(View.VISIBLE);
//                        //显示按钮组
//                        itemOrderOperationLayout.setVisibility(View.VISIBLE);
//                    }
//                    else
//                    {
//                        //不显示按钮
//                        itemOrderOperationLayout.setVisibility(View.GONE);
//                    }
//                }
//
//
//            }
//        }
//    }
//
//    private class BasicOnClickListener implements View.OnClickListener
//    {
//        private BaseViewHolder vh;
//        private OrderListInfo.BodyBean.PageBean.ListBean order;
//
//        public BasicOnClickListener(BaseViewHolder vh, OrderListInfo.BodyBean.PageBean.ListBean order)
//        {
//            this.vh = vh;
//            this.order = order;
//        }
//
//        @Override
//        public void onClick(View v)
//        {
//            switch (v.getId())
//            {
//                case R.id.item_order_cancle_btn:
//                    onItemClickListener.onItemClick(itemOrderCancleBtn, vh.getLayoutPosition(), order);
//                    break;
//                case R.id.item_order_resubmit_btn:
//                    onItemClickListener.onItemClick(itemOrderResubmitBtn, vh.getLayoutPosition(), order);
//                    break;
//                case R.id.item_order_delivery_btn:
//                    onItemClickListener.onItemClick(itemOrderDeliveryBtn, vh.getLayoutPosition(), order);
//                    break;
//                case R.id.item_order_cancle_after_delivery_btn:
//                    onItemClickListener.onItemClick(itemOrderCancleAfterDeliveryBtn, vh.getLayoutPosition(), order);
//                    break;
//                case R.id.item_order_making_finish_btn:
//                    onItemClickListener.onItemClick(itemOrderMakingFinishBtn, vh.getLayoutPosition(), order);
//                    break;
//                case R.id.item_order_cardview:
//                    onItemClickListener.onItemClick(itemOrderCardView, vh.getLayoutPosition(), order);
//                    break;
//            }
//        }
//    }
//}