package com.gaoyy.delivery4res.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.bean.GeocodeInfo;

import java.util.List;


/**
 * Created by gaoyy on 2016/8/24 0024.
 */
public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private List<GeocodeInfo.ResultsBean> data;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener
    {
        void onItemClick(View view, int position, GeocodeInfo.ResultsBean place);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }


    public SearchListAdapter(Context context, List<GeocodeInfo.ResultsBean> data)
    {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_search, parent, false);
        SearchListViewHolder vh = new SearchListViewHolder(rootView);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        SearchListViewHolder vh = (SearchListViewHolder)holder;
        GeocodeInfo.ResultsBean place = data.get(0);
        vh.itemSearchPlace.setText(place.getFormatted_address());

        if(onItemClickListener != null)
        {
            vh.itemSearchLayout.setOnClickListener(new SearchListAdapter.BasicOnClickListener(vh,place));
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }


    public static class SearchListViewHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout itemSearchLayout;
        private TextView itemSearchPlace;

        private void assignViews(View itemView)
        {
            itemSearchLayout = (LinearLayout) itemView.findViewById(R.id.item_search_layout);
            itemSearchPlace = (TextView) itemView.findViewById(R.id.item_search_place);
        }

        public SearchListViewHolder(View itemView)
        {
            super(itemView);
            assignViews(itemView);
        }

    }

    /**
     * 第一次加载
     *
     * @param s
     */
    public void updateData(List<GeocodeInfo.ResultsBean> s)
    {
        this.data = s;
        notifyDataSetChanged();
    }


    private class BasicOnClickListener implements View.OnClickListener
    {
        private SearchListViewHolder vh;
        private GeocodeInfo.ResultsBean place;

        public BasicOnClickListener(SearchListViewHolder vh, GeocodeInfo.ResultsBean place)
        {
            this.vh = vh;
            this.place = place;
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.item_search_layout:
                    onItemClickListener.onItemClick(vh.itemSearchLayout, vh.getLayoutPosition(), place);
                    break;
            }
        }
    }

}
