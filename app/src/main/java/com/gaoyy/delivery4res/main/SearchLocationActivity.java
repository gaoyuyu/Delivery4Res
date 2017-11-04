package com.gaoyy.delivery4res.main;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gaoyy.delivery4res.R;
import com.gaoyy.delivery4res.adapter.SearchListAdapter;
import com.gaoyy.delivery4res.api.Constant;
import com.gaoyy.delivery4res.api.RetrofitService;
import com.gaoyy.delivery4res.api.bean.GeocodeInfo;
import com.gaoyy.delivery4res.base.BaseActivity;
import com.gaoyy.delivery4res.base.OnItemClickListener;
import com.gaoyy.delivery4res.util.CommonUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchLocationActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener
{
    private Toolbar searchToolbar;
    private ProgressWheel searchProgressWheel;
    private ImageView seachCheck;
    private EditText searchEdit;
    private RecyclerView searchRv;
    private SearchListAdapter searchListAdapter;
    private List<GeocodeInfo.ResultsBean> placeList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private Call<GeocodeInfo> call;


    @Override
    protected void initContentView()
    {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        searchToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        seachCheck = (ImageView) findViewById(R.id.search_check);
        searchProgressWheel = (ProgressWheel) findViewById(R.id.search_progresswheel);
        searchEdit = (EditText) findViewById(R.id.search_edit);
        searchRv = (RecyclerView) findViewById(R.id.search_rv);
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar(searchToolbar, "", true, -1);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        String inputText = getIntent().getStringExtra("inputingText");
        searchEdit.setText(inputText);
        //将光标移至文字末尾
        searchEdit.setSelection(inputText.length());


        searchListAdapter = new SearchListAdapter(this, placeList);
        searchRv.setAdapter(searchListAdapter);
        //设置布局
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchRv.setLayoutManager(linearLayoutManager);
        searchRv.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void setListener()
    {
        super.setListener();
        searchEdit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (!editable.toString().equals(""))
                {
                    String address = editable.toString();
                    call = RetrofitService.sGoogleMapApiService.query(address, Constant.GOOGLE_MAP_KEY, "CA");
                    CommonUtils.httpDebugLogger("谷歌地图-位置模糊查询");
                    searchProgressWheel.setVisibility(View.VISIBLE);
                    searchRv.setVisibility(View.GONE);
                    call.enqueue(new Callback<GeocodeInfo>()
                    {
                        @Override
                        public void onResponse(Call<GeocodeInfo> call, Response<GeocodeInfo> response)
                        {
                            searchProgressWheel.setVisibility(View.GONE);
                            searchRv.setVisibility(View.VISIBLE);
                            if (response.isSuccessful() && response.body() != null)
                            {
                                GeocodeInfo geocodeInfo = response.body();
                                CommonUtils.httpDebugLogger("[status]" + geocodeInfo.getStatus());
                                if (geocodeInfo.getStatus().equals("OK"))
                                {
                                    List<GeocodeInfo.ResultsBean> list = geocodeInfo.getResults();
                                    searchListAdapter.updateData(list);
                                }
                                else
                                {
                                    CommonUtils.showSnackBar(searchToolbar, geocodeInfo.getStatus());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GeocodeInfo> call, Throwable t)
                        {
                            searchProgressWheel.setVisibility(View.GONE);
                            searchRv.setVisibility(View.VISIBLE);
                            CommonUtils.httpErrorLogger(t.toString());
                            CommonUtils.showToast(SearchLocationActivity.this, getResources().getString(R.string.network_error));
                        }
                    });
                }
            }
        });

        searchListAdapter.setOnItemClickListener(this);
        seachCheck.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.search_check:
                Intent intent = new Intent();
                intent.putExtra("place", searchEdit.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position, Object itemData)
    {
        GeocodeInfo.ResultsBean place = (GeocodeInfo.ResultsBean) itemData;
        switch (view.getId())
        {
            case R.id.item_search_layout:
                Intent intent = new Intent();
                intent.putExtra("place", place.getFormatted_address());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (call != null) call.cancel();
    }
}
