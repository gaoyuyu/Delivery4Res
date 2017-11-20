package com.gaoyy.delivery4res.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.bean.MyReplyListInfo;
import com.gaoyy.delivery4res.base.recycler.BaseViewHolder;
import com.gaoyy.delivery4res.base.recycler.RecyclerBaseAdapter;

import java.util.List;

public class MyReplyListAdapter extends RecyclerBaseAdapter<MyReplyListInfo.BodyBean.ListBean.ResultBean>
{
    private View goodsView;
    public MyReplyListAdapter(Context context, List<MyReplyListInfo.BodyBean.ListBean.ResultBean> data)
    {
        super(context, R.layout.item_my_reply, data);
    }

    @Override
    protected void bindData(BaseViewHolder holder, MyReplyListInfo.BodyBean.ListBean.ResultBean itemData, int position)
    {
        TextView itemCommonAddress = holder.getView(R.id.item_common_address);
        ((LinearLayout) (itemCommonAddress.getParent())).setVisibility(View.GONE);

        holder.setText(R.id.item_common_addtime,itemData.getAddTime())
                .setText(R.id.item_common_customer,itemData.getBuyerName())
                .setText(R.id.item_common_phone,itemData.getBuyerMobile())
                .setText(R.id.item_common_no,itemData.getOrder_id());

        TextView addTimeTv = holder.getView(R.id.item_common_addtime);
        TextView customerTv = holder.getView(R.id.item_common_customer);
        TextView phoneTv = holder.getView(R.id.item_common_phone);
        TextView noTv = holder.getView(R.id.item_common_no);

        LinearLayout parent = (LinearLayout) addTimeTv.getParent();
        //由于默认的布局不能满足设计稿，现做以下操作
        //先移除View
        parent.removeView(addTimeTv);
        parent.removeView((LinearLayout)(customerTv.getParent()));
        parent.removeView((LinearLayout)(phoneTv.getParent()));
        parent.removeView((LinearLayout)(noTv.getParent()));
        //再添加View
        parent.addView(addTimeTv);
        parent.addView((LinearLayout)(customerTv.getParent()));
        parent.addView((LinearLayout)(phoneTv.getParent()));
        parent.addView((LinearLayout)(noTv.getParent()));


        List<MyReplyListInfo.BodyBean.ListBean.ResultBean.GcsBean> goods = itemData.getGcs();
        LinearLayout itemCommonGoodsLayout = holder.getView(R.id.item_common_goods_layout);
        itemCommonGoodsLayout.removeAllViews();

        for (MyReplyListInfo.BodyBean.ListBean.ResultBean.GcsBean item : goods)
        {
            goodsView = mLayoutInflater.inflate(R.layout.item_food, itemCommonGoodsLayout, false);
            TextView goodName = (TextView) goodsView.findViewById(R.id.item_food_name);
            TextView goodCount = (TextView) goodsView.findViewById(R.id.item_food_count);
            TextView goodPrice = (TextView) goodsView.findViewById(R.id.item_food_price);
            goodName.setText(""+item.getGoods_name());
            goodCount.setText("x" + item.getCount());
            goodPrice.setText("$" + item.getPrice());
            itemCommonGoodsLayout.addView(goodsView);
        }

        ForegroundColorSpan span = new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorAccent));

        String orderEva = itemData.getOrderEva();
        SpannableStringBuilder builderEva = new SpannableStringBuilder("用户评价："+orderEva);
        builderEva.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView  itemMyReplyCus = holder.getView(R.id.item_my_reply_cus);
        itemMyReplyCus.setText(builderEva);


        String orderReply = itemData.getOrderReply();
        SpannableStringBuilder builderReply = new SpannableStringBuilder("我的回复："+orderReply);
        builderReply.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView  itemMyReplyRes = holder.getView(R.id.item_my_reply_res);
        itemMyReplyRes.setText(builderReply);
    }
}


/*
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

    public void updateData(LinkedList<MyReplyListInfo.BodyBean.ListBean.ResultBean>  s)
    {
        this.data = s;
        notifyDataSetChanged();
    }

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
*/