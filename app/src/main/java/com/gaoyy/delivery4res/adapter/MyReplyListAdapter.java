package com.gaoyy.delivery4res.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.MyReplyListInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gaoyy on 2017/9/13 0013.
 */

public class MyReplyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<MyReplyListInfo.BodyBean.ListBean.ResultBean> data;
    private Context context;

    private View goodsView;

    public MyReplyListAdapter(Context context, LinkedList<MyReplyListInfo.BodyBean.ListBean.ResultBean> data)
    {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_my_reply, parent, false);
        MyReplyListViewHolder vh = new MyReplyListViewHolder(rootView);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MyReplyListViewHolder vh = (MyReplyListViewHolder) holder;
        MyReplyListInfo.BodyBean.ListBean.ResultBean myReply = data.get(position);

        ((LinearLayout) (vh.itemCommonAddress.getParent())).setVisibility(View.GONE);
        vh.itemCommonAddtime.setText(myReply.getAddTime());
        vh.itemCommonCustomer.setText(myReply.getBuyerName());
        vh.itemCommonPhone.setText(myReply.getBuyerMobile());
        vh.itemCommonNo.setText(myReply.getOrder_id());

        List<MyReplyListInfo.BodyBean.ListBean.ResultBean.GcsBean> goods = myReply.getGcs();
        vh.itemCommonGoodsLayout.removeAllViews();

        for (MyReplyListInfo.BodyBean.ListBean.ResultBean.GcsBean item : goods)
        {
            goodsView = inflater.inflate(R.layout.item_food, vh.itemCommonGoodsLayout, false);
            TextView goodName = (TextView) goodsView.findViewById(R.id.item_food_name);
            TextView goodCount = (TextView) goodsView.findViewById(R.id.item_food_count);
            TextView goodPrice = (TextView) goodsView.findViewById(R.id.item_food_price);
            goodName.setText(""+item.getGoods_name());
            goodCount.setText("x" + item.getCount());
            goodPrice.setText("$" + item.getPrice());
            vh.itemCommonGoodsLayout.addView(goodsView);
        }
        ForegroundColorSpan span = new ForegroundColorSpan(context.getResources().getColor(R.color.colorAccent));

        String orderEva = myReply.getOrderEva();
        SpannableStringBuilder builderEva = new SpannableStringBuilder("用户评价："+orderEva);

        builderEva.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vh.itemMyReplyCus.setText(builderEva);


        String orderReply = myReply.getOrderReply();
        SpannableStringBuilder builderReply = new SpannableStringBuilder("我的回复："+orderReply);
        builderReply.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vh.itemMyReplyRes.setText(builderReply);
    }

    /**
     * 第一次加载
     *
     * @param s
     */
    public void updateData(LinkedList<MyReplyListInfo.BodyBean.ListBean.ResultBean>  s)
    {
        this.data = s;
        notifyDataSetChanged();
    }

    /**
     * 下拉加载更多
     *
     * @param newDatas
     */
    public void addMoreItem(LinkedList<MyReplyListInfo.BodyBean.ListBean.ResultBean>  newDatas)
    {
        Log.d(Constant.TAG, "newDatas-->" + newDatas.size());

        for (int i = 0; i < newDatas.size(); i++)
        {
            data.addLast(newDatas.get(i));
        }
        Log.d(Constant.TAG, "data-->" + data.size());
        notifyItemRangeInserted(getItemCount(), newDatas.size());
        notifyItemRangeChanged(getItemCount(), getItemCount() - newDatas.size());
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public static class MyReplyListViewHolder extends RecyclerView.ViewHolder
    {
        private TextView itemCommonAddtime;
        private TextView itemCommonAddress;
        private TextView itemCommonCustomer;
        private TextView itemCommonPhone;
        private TextView itemCommonNo;
        private LinearLayout itemCommonGoodsLayout;
        private TextView itemMyReplyCus;
        private TextView itemMyReplyRes;

        private void assignViews(View itemView)
        {
            itemCommonAddtime = (TextView) itemView.findViewById(R.id.item_common_addtime);
            itemCommonAddress = (TextView) itemView.findViewById(R.id.item_common_address);
            itemCommonCustomer = (TextView) itemView.findViewById(R.id.item_common_customer);
            itemCommonPhone = (TextView) itemView.findViewById(R.id.item_common_phone);
            itemCommonNo = (TextView) itemView.findViewById(R.id.item_common_no);
            itemCommonGoodsLayout = (LinearLayout) itemView.findViewById(R.id.item_common_goods_layout);
            itemMyReplyCus = (TextView) itemView.findViewById(R.id.item_my_reply_cus);
            itemMyReplyRes = (TextView) itemView.findViewById(R.id.item_my_reply_res);
        }


        public MyReplyListViewHolder(View itemView)
        {
            super(itemView);
            assignViews(itemView);
        }
    }
}