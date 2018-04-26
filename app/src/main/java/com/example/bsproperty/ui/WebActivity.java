package com.example.bsproperty.ui;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bsproperty.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebActivity extends BaseActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.wv_view)
    WebView wvView;

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_web;
    }

    @Override
    protected void loadData() {
        wvView.loadUrl("https://image.baidu.com/search/index?tn=baiduimage&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&fm=result&fr=&sf=1&fmq=1524756643180_R&pv=&ic=0&nc=1&z=&se=1&showtab=0&fb=0&width=&height=&face=0&istype=2&ie=utf-8&word=%E5%88%98%E5%BE%B7%E5%8D%8E");
    }


    @OnClick(R.id.tv_back)
    public void onViewClicked() {
    }
}
