package com.gaoyy.delivery4res.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.OrderListInfo;
import com.gaoyy.delivery4res.api.bean.RestInfo;
import com.gaoyy.delivery4res.main.MainActivity;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by gaoyy on 2016/8/24 0024.
 */
public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> data;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }


    public OrderListAdapter(Context context, LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> data)
    {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_order, parent, false);
        OrderListViewHolder vh = new OrderListViewHolder(rootView);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        OrderListViewHolder vh = (OrderListViewHolder) holder;
        OrderListInfo.BodyBean.PageBean.ListBean order = data.get(position);
        int orderStatus = order.getStatus();
        List<RestInfo.BodyBean.DictStatusBean> dictStatus = MainActivity.dictStatus;
        vh.itemOrderAddress.setText(order.getCustomerAddr());
        vh.itemOrderDriverName.setText(order.getCourierName());
        vh.itemOrderCustomerPhone.setText(order.getCustomerTel());
        vh.itemOrderNo.setText(order.getOrderNo());
        vh.itemOrderNotes.setText(order.getRemarks());
        vh.itemOrderOthers.setText(order.getRemark());
        vh.itemOrderFinishedTime.setText(order.getFinishedTime());

        for(int i=0;i<dictStatus.size();i++)
        {
            int value = Integer.valueOf(dictStatus.get(i).getValue());
            //Wait
            if((orderStatus ==value)&&(value == 0))
            {
//                Log.d(Constant.TAG,"adapter order status is Wait");
                vh.itemOrderStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
                vh.itemOrderStatus.setText("Wait");
                vh.itemOrderStatusDate.setText(order.getCreateDate());
                //隐藏司机名字
                vh.itemOrderDriverNameLayout.setVisibility(View.GONE);

            }
            //Accept
            if((orderStatus ==value)&&(value == 1))
            {
//                Log.d(Constant.TAG,"adapter order status is Accept");
                vh.itemOrderStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                vh.itemOrderStatus.setText("Accept");
                vh.itemOrderStatusDate.setText(order.getAcceptDate());
            }
            //Delivery
            if((orderStatus ==value)&&(value == 2))
            {
//                Log.d(Constant.TAG,"adapter order status is Delivery");
                vh.itemOrderStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
                vh.itemOrderStatus.setText("Delivery");
                vh.itemOrderStatusDate.setText(order.getDeliveryDate());
            }
            //Finish
            if((orderStatus ==value)&&(value ==3))
            {
//                Log.d(Constant.TAG,"adapter order status is Finish");
                vh.itemOrderStatus.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                vh.itemOrderStatus.setText("Finish");
                vh.itemOrderStatusDate.setText(order.getFinishDate());
            }
            //Cancel
            if((orderStatus ==value)&&(value ==4))
            {
//                Log.d(Constant.TAG,"adapter order status is Cancel");
                vh.itemOrderStatus.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                vh.itemOrderStatus.setText("Cancel");
                vh.itemOrderStatusDate.setText((String)order.getCancelDate());
            }
            //Back
            if((orderStatus ==value)&&(value ==4))
            {
//                Log.d(Constant.TAG,"adapter order status is Back");
                vh.itemOrderStatus.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                vh.itemOrderStatus.setText("Back");
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }


    public static class OrderListViewHolder extends RecyclerView.ViewHolder
    {
        private TextView itemOrderStatus;
        private TextView itemOrderStatusDate;
        private TextView itemOrderAddress;
        private LinearLayout itemOrderDriverNameLayout;
        private TextView itemOrderDriverName;
        private LinearLayout itemOrderCustomerPhoneLayout;
        private TextView itemOrderCustomerPhone;
        private LinearLayout itemOrderOrdernoLayout;
        private TextView itemOrderNo;
        private LinearLayout itemOrderNotesLayout;
        private TextView itemOrderNotes;
        private LinearLayout itemOrderOthersLayout;
        private TextView itemOrderOthers;
        private LinearLayout itemOrderFinishedTimeLayout;
        private TextView itemOrderFinishedTime;
        private LinearLayout itemOrderOperationLayout;
        private AppCompatButton itemOrderCancleBtn;
        private AppCompatButton itemOrderResubmitBtn;


        public OrderListViewHolder(View itemView)
        {
            super(itemView);
            assignViews(itemView);
        }

        private void assignViews(View itemView)
        {
            itemOrderStatus = (TextView) itemView.findViewById(R.id.item_order_status);
            itemOrderStatusDate = (TextView) itemView.findViewById(R.id.item_order_status_date);
            itemOrderAddress = (TextView) itemView.findViewById(R.id.item_order_address);
            itemOrderDriverNameLayout = (LinearLayout) itemView.findViewById(R.id.item_order_driver_name_layout);
            itemOrderDriverName = (TextView) itemView.findViewById(R.id.item_order_driver_name);
            itemOrderCustomerPhoneLayout = (LinearLayout) itemView.findViewById(R.id.item_order_customer_phone_layout);
            itemOrderCustomerPhone = (TextView) itemView.findViewById(R.id.item_order_customer_phone);
            itemOrderOrdernoLayout = (LinearLayout) itemView.findViewById(R.id.item_order_orderno_layout);
            itemOrderNo = (TextView) itemView.findViewById(R.id.item_order_no);
            itemOrderNotesLayout = (LinearLayout) itemView.findViewById(R.id.item_order_notes_layout);
            itemOrderNotes = (TextView) itemView.findViewById(R.id.item_order_notes);
            itemOrderOthersLayout = (LinearLayout) itemView.findViewById(R.id.item_order_others_layout);
            itemOrderOthers = (TextView) itemView.findViewById(R.id.item_order_others);
            itemOrderFinishedTimeLayout = (LinearLayout) itemView.findViewById(R.id.item_order_finished_time_layout);
            itemOrderFinishedTime = (TextView) itemView.findViewById(R.id.item_order_finished_time);
            itemOrderOperationLayout = (LinearLayout) itemView.findViewById(R.id.item_order_operation_layout);
            itemOrderCancleBtn = (AppCompatButton) itemView.findViewById(R.id.item_order_cancle_btn);
            itemOrderResubmitBtn = (AppCompatButton) itemView.findViewById(R.id.item_order_resubmit_btn);
        }
    }


    /**
     * 第一次加载
     * @param s
     */
    public void updateData(LinkedList<OrderListInfo.BodyBean.PageBean.ListBean>  s)
    {
        this.data = s;
        notifyDataSetChanged();
    }

    /**
     * 下拉加载更多
     * @param newDatas
     */
    public void addMoreItem(LinkedList<OrderListInfo.BodyBean.PageBean.ListBean> newDatas)
    {
        Log.d(Constant.TAG,"newDatas-->"+newDatas.size());

        for(int i=0;i<newDatas.size();i++)
        {
            data.addLast(newDatas.get(i));
        }
        Log.d(Constant.TAG,"data-->"+data.size());
        notifyItemRangeInserted(getItemCount(),newDatas.size());
        notifyItemRangeChanged(getItemCount(), getItemCount()-newDatas.size());
    }

    private class BasicOnClickListener implements View.OnClickListener
    {
        private OrderListViewHolder vh;

        public BasicOnClickListener(OrderListViewHolder vh)
        {
            this.vh = vh;
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
//                case R.id.item_news_layout:
//                    onItemClickListener.onItemClick(newsViewHolder.itemNewsLayout, newsViewHolder.getLayoutPosition());
//                    break;
            }
        }
    }

}
