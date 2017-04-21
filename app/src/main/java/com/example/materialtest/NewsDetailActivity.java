package com.example.materialtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.materialtest.bean.DouBanDetailNews;
import com.example.materialtest.bean.ZhiHuDetailNews;
import com.example.materialtest.presenter.NewsPresenter;
import com.example.materialtest.presenter.NewsPresenterImpl;
import com.example.materialtest.utils.BodyUtil;
import com.example.materialtest.utils.NetworkUtil;
import com.example.materialtest.utils.ShareUtil;
import com.google.gson.Gson;


/**
 * Created by gaohailong on 2016/12/5.
 */

public class NewsDetailActivity extends AppCompatActivity implements NewsView, SwipeRefreshLayout.OnRefreshListener {

    private NewsPresenter newsPresenter;
    private Gson gson;
    private CollapsingToolbarLayout collapsingToolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;
    private ImageView headImage;
    private FloatingActionButton fab;
    private ZhiHuDetailNews zhiHuDetailNews;
    private DouBanDetailNews douBanDetailNews;
    private int id;
    private String title;
    private String picUrl;
    private String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        if (id != 0){
            gson = new Gson();
            newsPresenter = new NewsPresenterImpl(this);
            onLoadNewsDetail(id);
        }else {
            title = intent.getStringExtra("title");
            picUrl = intent.getStringExtra("thumbnail_pic_s");
            url = intent.getStringExtra("url");

            addNews(title,picUrl,url);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断网络情况，在进行分享
                if (NetworkUtil.isNetworkConnected(NewsDetailActivity.this)){
                    if (id == 0){
                        ShareUtil.shareText(NewsDetailActivity.this,"微信精选：" + title + " " + url);
                    }else if (id > 1000000){
                        ShareUtil.shareText(NewsDetailActivity.this,"知乎日报：" + zhiHuDetailNews.getTitle() + " " + zhiHuDetailNews.getShare_url());
                    }else {
                        ShareUtil.shareImage(NewsDetailActivity.this, douBanDetailNews.getShare_pic_url());
                    }
                }else {
                    Toast.makeText(NewsDetailActivity.this,"连接WIFI再进行分享吧！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        headImage = (ImageView) findViewById(R.id.head_image);

        webView = (WebView) findViewById(R.id.web_view);
        webView.setScrollbarFadingEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        //设置缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        webView.getSettings().setDatabaseEnabled(true);

        String webViewCachePath = getFilesDir().getAbsolutePath() + "/webview_cache";
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCachePath(webViewCachePath);

    }

    private void addNews(String title, String picUrl, String url) {
        showLoading();

        Glide.with(this)
                .load(picUrl)
                .into(headImage);
        collapsingToolbar.setTitle(title);
        webView.loadUrl(url);

        hideLoading();
    }

    private void onLoadNewsDetail(int id) {
        String type;
        if (id > 1000000){
            type = "zhihu";
        }else {
            type = "douban";
        }
        newsPresenter.loadNews(this, type, id);
    }

    @Override
    public void onRefresh() {
        if (id != 0){
            onLoadNewsDetail(id);
        }else {
            addNews(title, picUrl, url);
        }
    }

    @Override
    public void addNews(String result) {
        if (id > 1000000){
            zhiHuDetailNews = gson.fromJson(result, ZhiHuDetailNews.class);
            Glide.with(this)
                    .load(zhiHuDetailNews.getImage())
                    .into(headImage);
            collapsingToolbar.setTitle(zhiHuDetailNews.getTitle());

            String body = BodyUtil.convertBody(zhiHuDetailNews.getBody());
            webView.loadDataWithBaseURL("x-data://base", body, "text/html", "utf-8", null);
        }else {
            douBanDetailNews = gson.fromJson(result, DouBanDetailNews.class);
            if (douBanDetailNews.getThumbs().size() > 0){
                Glide.with(this)
                        .load(douBanDetailNews.getThumbs().get(0).getLarge().getUrl())
                .into(headImage);
            }
            collapsingToolbar.setTitle(douBanDetailNews.getTitle());
            String content = BodyUtil.convertContent(douBanDetailNews);
            webView.loadDataWithBaseURL("x-data://base", content, "text/html", "utf-8", null);
        }
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showErrorMsg(String msg) {
        Snackbar.make(webView,msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
