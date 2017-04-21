package com.example.materialtest;

import android.app.Activity;

import com.example.materialtest.utils.OkHttpManager;


/**
 * Created by gaohailong on 2016/11/24.
 */

public class NewsModelImpl implements NewsModel {

    @Override
    public void loadNews(Activity activity, String url, final OnLoadNewsListListener listener) {
        OkHttpManager.getInstance().getLoadData(activity,url,listener);
    }

    public interface OnLoadNewsListListener{
        void onSuccess(String result);
        void onFailure(String msg);
    }
}
