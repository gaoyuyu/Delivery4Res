package com.gaoyy.delivery4res.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.MessageListInfo;

import java.util.LinkedList;

/**
 * Created by gaoyy on 2017/9/15 0015.
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<MessageListInfo.BodyBean.ListBean.ResultBean> data;
    private Context context;


    public MessageListAdapter(Context context, LinkedList<MessageListInfo.BodyBean.ListBean.ResultBean> data)
    {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_message, parent, false);
        MessageListViewHolder vh = new MessageListViewHolder(rootView);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MessageListViewHolder vh = (MessageListViewHolder) holder;
        MessageListInfo.BodyBean.ListBean.ResultBean msg = data.get(position);

        vh.itemMessageContent.setText(msg.getContent());
        vh.itemMessageDate.setText(msg.getAddTime());

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
    public void updateData(LinkedList<MessageListInfo.BodyBean.ListBean.ResultBean> s)
    {
        this.data = s;
        notifyDataSetChanged();
    }

    /**
     * 下拉加载更多
     *
     * @param newDatas
     */
    public void addMoreItem(LinkedList<MessageListInfo.BodyBean.ListBean.ResultBean> newDatas)
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

    public static class MessageListViewHolder extends RecyclerView.ViewHolder
    {
        private TextView itemMessageContent;
        private TextView itemMessageDate;

        private void assignViews(View itemView)
        {
            itemMessageContent = (TextView) itemView.findViewById(R.id.item_message_content);
            itemMessageDate = (TextView) itemView.findViewById(R.id.item_message_date);
        }

        public MessageListViewHolder(View itemView)
        {
            super(itemView);
            assignViews(itemView);
        }
    }
}
