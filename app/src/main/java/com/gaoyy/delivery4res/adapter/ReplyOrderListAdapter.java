package com.gaoyy.delivery4res.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.bean.ReplyOrderListInfo;
import com.gaoyy.delivery4res.base.BaseViewHolder;
import com.gaoyy.delivery4res.base.RecyclerBaseAdapter;

import java.util.List;

/**
 * Created by gaoyy on 2017/9/12 0012.
 */
public class ReplyOrderListAdapter extends RecyclerBaseAdapter<ReplyOrderListInfo.BodyBean.ListBean.ResultBean>
{
    private View goodsView;

    public ReplyOrderListAdapter(Context context, List<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> data)
    {
        super(context, R.layout.item_reply, data);
    }

    @Override
    protected void bindData(BaseViewHolder holder, ReplyOrderListInfo.BodyBean.ListBean.ResultBean itemData, int position)
    {
        ((LinearLayout) (holder.getView(R.id.item_common_address).getParent())).setVisibility(View.GONE);
        holder.setText(R.id.item_common_address, itemData.getAddTime())
                .setText(R.id.item_common_customer, itemData.getBuyerName())
                .setText(R.id.item_common_phone, itemData.getBuyerMobile())
                .setText(R.id.item_common_no, itemData.getOrder_id());

        List<ReplyOrderListInfo.BodyBean.ListBean.ResultBean.GcsBean> goods = itemData.getGcs();
        LinearLayout itemCommonGoodsLayout = holder.getView(R.id.item_common_goods_layout);
        itemCommonGoodsLayout.removeAllViews();

        for (ReplyOrderListInfo.BodyBean.ListBean.ResultBean.GcsBean item : goods)
        {
            goodsView = mLayoutInflater.inflate(R.layout.item_food, itemCommonGoodsLayout, false);
            TextView goodName = (TextView) goodsView.findViewById(R.id.item_food_name);
            TextView goodCount = (TextView) goodsView.findViewById(R.id.item_food_count);
            TextView goodPrice = (TextView) goodsView.findViewById(R.id.item_food_price);
            goodName.setText("" + item.getGoods_name());
            goodCount.setText("x" + item.getCount());
            goodPrice.setText("$" + item.getPrice());
            itemCommonGoodsLayout.addView(goodsView);
        }

        String orderEva = itemData.getOrderEva();
        SpannableStringBuilder builder = new SpannableStringBuilder("用户评价：" + orderEva);
        ForegroundColorSpan span = new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorAccent));
        builder.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView itemReplyContent = holder.getView(R.id.item_reply_content);
        itemReplyContent.setText(builder);
        AppCompatButton itemReplyBtn = holder.getView(R.id.item_reply_btn);

        //设置btn的点击监听器
        if (onItemClickListener != null)
        {
            itemReplyBtn.setOnClickListener(new BasicOnClickListener(holder, itemData));
        }
    }

    private class BasicOnClickListener implements View.OnClickListener
    {
        private BaseViewHolder vh;
        private ReplyOrderListInfo.BodyBean.ListBean.ResultBean reply;

        public BasicOnClickListener(BaseViewHolder vh, ReplyOrderListInfo.BodyBean.ListBean.ResultBean reply)
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
                    onItemClickListener.onItemClick(vh.getView(R.id.item_reply_btn), vh.getLayoutPosition(), reply);
                    break;
            }
        }
    }
}

/*
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

    public void updateData(LinkedList<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> s)
    {
        this.data = s;
        notifyDataSetChanged();
    }
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

    public void removeSingleItem(int position)
    {
        Log.d(Constant.TAG,"remove-->"+position);
        data.remove(position);//删除数据源
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据
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
*/
