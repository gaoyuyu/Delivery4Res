package com.gaoyy.delivery4res.adapter;

import android.content.Context;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.bean.MessageListInfo;
import com.gaoyy.delivery4res.base.BaseViewHolder;
import com.gaoyy.delivery4res.base.RecyclerBaseAdapter;

import java.util.List;

/**
 * Created by gaoyy on 2017/9/15 0015.
 */

public class MessageListAdapter extends RecyclerBaseAdapter<MessageListInfo.BodyBean.ListBean.ResultBean>
{
    public MessageListAdapter(Context context, List<MessageListInfo.BodyBean.ListBean.ResultBean> data)
    {
        super(context, R.layout.item_message, data);
    }

    @Override
    protected void bindData(BaseViewHolder holder, MessageListInfo.BodyBean.ListBean.ResultBean itemData, int position)
    {
        holder.setText(R.id.item_message_content, itemData.getContent());
        holder.setText(R.id.item_message_date, itemData.getAddTime());
    }
}