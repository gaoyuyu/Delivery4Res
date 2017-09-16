package com.gaoyy.delivery4res.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
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
import com.gaoyy.delivery4res.api.bean.ReplyOrderListInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gaoyy on 2017/9/12 0012.
 */

public class ReplyOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> data;
    private Context context;
    private OnItemClickListener onItemClickListener;

    private View goodsView;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position, ReplyOrderListInfo.BodyBean.ListBean.ResultBean order);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    public ReplyOrderListAdapter(Context context, LinkedList<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> data)
    {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_reply, parent, false);
        ReplyListViewHolder vh = new ReplyListViewHolder(rootView);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ReplyListViewHolder vh = (ReplyListViewHolder) holder;
        ReplyOrderListInfo.BodyBean.ListBean.ResultBean reply = data.get(position);

        vh.itemCommonAddtime.setText(reply.getAddTime());
        ((LinearLayout) (vh.itemCommonAddress.getParent())).setVisibility(View.GONE);
        vh.itemCommonCustomer.setText(reply.getBuyerName());
        vh.itemCommonPhone.setText(reply.getBuyerMobile());
        vh.itemCommonNo.setText(reply.getOrder_id());

        List<ReplyOrderListInfo.BodyBean.ListBean.ResultBean.GcsBean> goods = reply.getGcs();
        vh.itemCommonGoodsLayout.removeAllViews();

        for (ReplyOrderListInfo.BodyBean.ListBean.ResultBean.GcsBean item : goods)
        {
            goodsView = inflater.inflate(R.layout.item_food, vh.itemCommonGoodsLayout, false);
            TextView goodName = (TextView) goodsView.findViewById(R.id.item_food_name);
            TextView goodCount = (TextView) goodsView.findViewById(R.id.item_food_count);
            TextView goodPrice = (TextView) goodsView.findViewById(R.id.item_food_price);
            goodName.setText("" + item.getGoods_name());
            goodCount.setText("x" + item.getCount());
            goodPrice.setText("$" + item.getPrice());
            vh.itemCommonGoodsLayout.addView(goodsView);
        }

        String orderEva = reply.getOrderEva();
        SpannableStringBuilder builder = new SpannableStringBuilder("用户评价：" + orderEva);
        ForegroundColorSpan span = new ForegroundColorSpan(context.getResources().getColor(R.color.colorAccent));
        builder.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vh.itemReplyContent.setText(builder);


        //设置btn的点击监听器
        if (onItemClickListener != null)
        {
            vh.itemReplyBtn.setOnClickListener(new BasicOnClickListener(vh, reply));
        }

    }

    private class BasicOnClickListener implements View.OnClickListener
    {
        private ReplyListViewHolder vh;
        private ReplyOrderListInfo.BodyBean.ListBean.ResultBean reply;

        public BasicOnClickListener(ReplyListViewHolder vh, ReplyOrderListInfo.BodyBean.ListBean.ResultBean reply)
        {
            this.vh = vh;
            this.reply = reply;
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.item_reply_btn:
                    onItemClickListener.onItemClick(vh.itemReplyBtn, vh.getLayoutPosition(), reply);
                    break;
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    /**
     * 第一次加载
     *
     * @param s
     */
    public void updateData(LinkedList<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> s)
    {
        this.data = s;
        notifyDataSetChanged();
    }

    /**
     * 下拉加载更多
     *
     * @param newDatas
     */
    public void addMoreItem(LinkedList<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> newDatas)
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

    /**
     * 移除单个item
     *
     * @param position
     */
    public void removeSingleItem(int position)
    {
        notifyItemRemoved(position);
    }

    public static class ReplyListViewHolder extends RecyclerView.ViewHolder
    {
        private TextView itemCommonAddtime;
        private TextView itemCommonAddress;
        private TextView itemCommonCustomer;
        private TextView itemCommonPhone;
        private TextView itemCommonNo;
        private LinearLayout itemCommonGoodsLayout;
        private CardView itemReplyCardView;
        private TextView itemReplyContent;
        private AppCompatButton itemReplyBtn;


        private void assignViews(View itemView)
        {
            itemCommonAddtime = (TextView) itemView.findViewById(R.id.item_common_addtime);
            itemCommonAddress = (TextView) itemView.findViewById(R.id.item_common_address);
            itemCommonCustomer = (TextView) itemView.findViewById(R.id.item_common_customer);
            itemCommonPhone = (TextView) itemView.findViewById(R.id.item_common_phone);
            itemCommonNo = (TextView) itemView.findViewById(R.id.item_common_no);
            itemCommonGoodsLayout = (LinearLayout) itemView.findViewById(R.id.item_common_goods_layout);
            itemReplyCardView = (CardView) itemView.findViewById(R.id.item_reply_cardview);
            itemReplyContent = (TextView) itemView.findViewById(R.id.item_reply_content);
            itemReplyBtn = (AppCompatButton) itemView.findViewById(R.id.item_reply_btn);


        }

        public ReplyListViewHolder(View itemView)
        {
            super(itemView);
            assignViews(itemView);
        }
    }

}