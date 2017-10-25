package com.gaoyy.delivery4res.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.RetrofitService;
import com.gaoyy.delivery4res.api.bean.CommonInfo;
import com.gaoyy.delivery4res.base.BaseFragment;
import com.gaoyy.delivery4res.base.CustomDialogFragment;
import com.gaoyy.delivery4res.changepwd.ChangePwdActivity;
import com.gaoyy.delivery4res.login.LoginActivity;
import com.gaoyy.delivery4res.mine.messagelist.MessageListActivity;
import com.gaoyy.delivery4res.mine.replylist.ReplyListActivity;
import com.gaoyy.delivery4res.myreplylist.MyReplyListActivity;
import com.gaoyy.delivery4res.util.CommonUtils;
import com.gaoyy.delivery4res.util.DialogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MineFragment extends BaseFragment implements View.OnClickListener
{


    private TextView mineName;
    private RelativeLayout mineOrderList;
    private RelativeLayout mineChangePwd;
    private RelativeLayout mineNewOrder;
    private RelativeLayout mineMyReply;
    private RelativeLayout mineMessage;
    private RelativeLayout mineReply;
    private AppCompatButton mineSignOutBtn;




    //通信监听
    private OnFragmentInteractionListener onFragmentInteractionListener;


    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(int flag);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()+ " must implement OnFragmentInteractionListener<"+MineFragment.class.getSimpleName()+">");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    public MineFragment()
    {
        // Required empty public constructor
    }

    public static MineFragment newInstance()
    {
        MineFragment fragment = new MineFragment();
        return fragment;
    }



    @Override
    protected int getFragmentLayoutId()
    {
        return R.layout.fragment_mine;
    }

    @Override
    protected void assignViews(View rootView)
    {
        super.assignViews(rootView);

        mineName = (TextView) rootView.findViewById(R.id.mine_name);
        mineOrderList = (RelativeLayout) rootView.findViewById(R.id.mine_order_list);
        mineChangePwd = (RelativeLayout) rootView.findViewById(R.id.mine_change_pwd);
        mineNewOrder = (RelativeLayout) rootView.findViewById(R.id.mine_new_order);
        mineMyReply = (RelativeLayout) rootView.findViewById(R.id.mine_my_reply);
        mineMessage = (RelativeLayout) rootView.findViewById(R.id.mine_message);
        mineReply = (RelativeLayout) rootView.findViewById(R.id.mine_reply);
        mineSignOutBtn = (AppCompatButton)rootView.findViewById(R.id.mine_sign_out_btn);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        String name = CommonUtils.getName(activity);
        mineName.setText(name);
    }

    @Override
    protected void setListener()
    {
        super.setListener();

        mineOrderList.setOnClickListener(this);
        mineChangePwd.setOnClickListener(this);
        mineNewOrder.setOnClickListener(this);
        mineMyReply.setOnClickListener(this);
        mineMessage.setOnClickListener(this);
        mineReply.setOnClickListener(this);
        mineSignOutBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        Message msg = Message.obtain();
        int id = view.getId();
        switch (id)
        {
            case R.id.mine_order_list:
                msg.what = Constant.MSG_TO_ACT_ORDER_LIST;
                if (onFragmentInteractionListener != null)
                {
                    onFragmentInteractionListener.onFragmentInteraction(msg.what);
                }
                break;
            case R.id.mine_change_pwd:
                Intent changePwd = new Intent();
                changePwd.setClass(activity, ChangePwdActivity.class);
                startActivity(changePwd);
                break;
            case R.id.mine_new_order:
                msg.what = Constant.MSG_TO_ACT_NEW_ORDER;
                if (onFragmentInteractionListener != null)
                {
                    onFragmentInteractionListener.onFragmentInteraction(msg.what);
                }
                break;
            case R.id.mine_my_reply:
                Intent myReply = new Intent();
                myReply.setClass(activity, MyReplyListActivity.class);
                startActivity(myReply);
                break;
            case R.id.mine_message:
                Intent message = new Intent();
                message.setClass(activity, MessageListActivity.class);
                startActivity(message);
                break;
            case R.id.mine_reply:
                Intent reply = new Intent();
                reply.setClass(activity, ReplyListActivity.class);
                startActivity(reply);
                break;
            case R.id.mine_sign_out_btn:
                String loginName = CommonUtils.getLoginName(activity);
                String randomCode = CommonUtils.getRandomCode(activity);
                logout(loginName, randomCode);
                break;
        }


    }

    /**
     * 用户退出
     *
     * @param loginName
     * @param randomCode
     */
    private void logout(String loginName, String randomCode)
    {
        Call<CommonInfo> call = RetrofitService.sApiService.logout(loginName, randomCode);
        CommonUtils.httpDebugLogger("退出请求");
        final CustomDialogFragment loading = DialogUtils.showLoadingDialog(activity);
        call.enqueue(new Callback<CommonInfo>()
        {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response)
            {
                loading.dismiss();
                if (response.isSuccessful() && response.body() != null)
                {
                    CommonInfo logoutInfo = response.body();
                    String msg = logoutInfo.getMsg();
                    String errorCode = logoutInfo.getErrorCode();
                    CommonUtils.httpDebugLogger("[isSuccess=" + logoutInfo.isSuccess() + "][errorCode=" + errorCode + "][msg=" + msg + "]");
                    CommonUtils.showToast(activity, msg);
                    if (errorCode.equals("-1"))
                    {
                        Intent intent = new Intent();
                        intent.setClass(activity, LoginActivity.class);
                        startActivity(intent);
                        activity.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t)
            {
                loading.dismiss();
                CommonUtils.httpErrorLogger(t.toString());
                CommonUtils.showToast(activity, getResources().getString(R.string.network_error));
            }
        });
    }
}
