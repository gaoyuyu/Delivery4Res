package com.gaoyy.delivery4res.order.replylist;


import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.adapter.ReplyOrderListAdapter;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.bean.ReplyOrderListInfo;
import com.gaoyy.delivery4res.base.BaseFragment;
import com.gaoyy.delivery4res.base.CustomDialogFragment;
import com.gaoyy.delivery4res.util.CommonUtils;
import com.gaoyy.delivery4res.util.DialogUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ReplyListFragment extends BaseFragment implements ReplyListContract.View, SwipeRefreshLayout.OnRefreshListener, ReplyOrderListAdapter.OnItemClickListener
{
    private static final String LOG_TAG = ReplyListFragment.class.getSimpleName();
    private ProgressWheel commonProgresswheel;
    private SwipeRefreshLayout commonSwipeRefreshLayout;
    private RecyclerView commonRv;

    private int lastVisibleItem;

    private int pageNo = 1;
    private int pageSize = 10;
    private int pageCount;

    private LinearLayoutManager linearLayoutManager;
    private ReplyOrderListAdapter replyOrderListAdapter;
    private LinkedList<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> replyOrderList = new LinkedList<>();
    private ReplyListContract.Presenter mReplyListPresenter;
    private CustomDialogFragment loading;


    public ReplyListFragment()
    {
        // Required empty public constructor
    }

    public static ReplyListFragment newInstance()
    {
        ReplyListFragment fragment = new ReplyListFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayoutId()
    {
        return R.layout.fragment_reply_list;
    }

    @Override
    protected void assignViews(View rootView)
    {
        super.assignViews(rootView);
        commonProgresswheel = (ProgressWheel) rootView.findViewById(R.id.common_progresswheel);
        commonSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.common_swipeRefreshLayout);
        commonRv = (RecyclerView) rootView.findViewById(R.id.common_rv);
    }

    @Override
    protected void configViews()
    {
        super.configViews();

        replyOrderListAdapter = new ReplyOrderListAdapter(activity, replyOrderList);
        commonRv.setAdapter(replyOrderListAdapter);
        //设置布局
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        commonRv.setLayoutManager(linearLayoutManager);
        commonRv.setItemAnimator(new DefaultItemAnimator());

        CommonUtils.setSwipeLayoutProgressBackgroundColor(activity, commonSwipeRefreshLayout);

        pageNo = 1;
        Map<String, String> params = getReplyOrderListParams(pageNo, pageSize);
        Log.d(Constant.TAG, "待回复列表参数：" + params.toString());
        mReplyListPresenter.replyOrderList(params, Constant.PULL_TO_REFRESH);
    }

    @Override
    protected void setListener()
    {
        super.setListener();

        commonSwipeRefreshLayout.setOnRefreshListener(this);
        commonRv.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == replyOrderListAdapter.getItemCount())
                {
                    //总共有多少页
                    int pageSum = 0;
                    pageNo = pageNo + 1;

                    if (pageCount % pageSize == 0)
                    {
                        pageSum = pageCount / pageSize;
                    }
                    else
                    {
                        pageSum = pageCount / pageSize + 1;
                    }
                    Log.d(Constant.TAG, "page sum-->" + pageSum);
                    Log.d(Constant.TAG, "page No-->" + pageNo);
                    if (pageNo <= pageSum)
                    {
                        Map<String, String> params = getReplyOrderListParams(pageNo, pageSize);
                        Log.d(Constant.TAG, "上拉加载更多，传递参数-->" + params.toString());
                        mReplyListPresenter.replyOrderList(params, Constant.UP_TO_LOAD_MORE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        //设置item的点击事件
        replyOrderListAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mReplyListPresenter == null) return;
        mReplyListPresenter.start();
//        //在onResume中加载数据
//        pageNo = 1;
//        Map<String, String> params = getReplyOrderListParams(pageNo, pageSize);
//        Log.d(Constant.TAG, "待回复列表参数：" + params.toString());
//        mReplyListPresenter.replyOrderList(params, Constant.PULL_TO_REFRESH);
    }

    private Map<String, String> getReplyOrderListParams(int pageNo, int pageSize)
    {
        Map<String, String> params = new HashMap<>();
        params.put("loginName", CommonUtils.getLoginName(activity));
        params.put("randomCode", CommonUtils.getRandomCode(activity));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("language", "zh");
        return params;
    }

    @Override
    public boolean isActive()
    {
        return isAdded();
    }

    @Override
    public void showReplyOrderList(LinkedList<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> replyOrderList, int count)
    {
        replyOrderListAdapter.updateData(replyOrderList);
        pageCount = count;
    }

    @Override
    public void loadMoreReplyOrderList(LinkedList<ReplyOrderListInfo.BodyBean.ListBean.ResultBean> replyOrderList, int count)
    {
        replyOrderListAdapter.addMoreItem(replyOrderList);
        pageCount = count;
    }

    @Override
    public void refreshing()
    {
        commonSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void finishRefesh()
    {
        commonSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showToast(String msg)
    {
        CommonUtils.showToast(activity, msg);
    }

    @Override
    public void showToast(int msgId)
    {
        CommonUtils.showToast(activity, msgId);
    }

    @Override
    public void setPresenter(ReplyListContract.Presenter presenter)
    {
        Log.i(Constant.TAG, LOG_TAG + "  setPresenter");
        if (presenter != null)
        {
            mReplyListPresenter = presenter;
        }
    }


    @Override
    public void showLoading()
    {
        loading = DialogUtils.showLoadingDialog(activity);
    }

    @Override
    public void hideLoading()
    {
        if (loading != null)
        {
            loading.dismiss();
        }
    }

    @Override
    public void onRefresh()
    {
        pageNo = 1;
        Map<String, String> params = getReplyOrderListParams(pageNo, pageSize);
        Log.d(Constant.TAG, "下拉刷新，传递参数-->" + params.toString());
        mReplyListPresenter.replyOrderList(params, Constant.PULL_TO_REFRESH);
    }

    @Override
    public void onItemClick(View view, final int position, final ReplyOrderListInfo.BodyBean.ListBean.ResultBean order)
    {
        View editView = LayoutInflater.from(activity).inflate(R.layout.dialog_reply,null);
        final EditText et = (EditText) editView.findViewById(R.id.dialog_reply_edit);
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(R.string.reply_order).setView(editView);
        dialog.setPositiveButton(R.string.alert_confirm,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        CommonUtils.showToast(activity,et.getText().toString());
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("loginName",CommonUtils.getLoginName(activity));
                        params.put("randomCode",CommonUtils.getRandomCode(activity));
                        params.put("content",et.getText().toString());
                        params.put("order_id",order.getId()+"");
                        params.put("language","zh");
                        mReplyListPresenter.replyOrder(position,params);

                    }
                }).show();
    }


    @Override
    public void removeSingleItem(int position)
    {

    }
}
