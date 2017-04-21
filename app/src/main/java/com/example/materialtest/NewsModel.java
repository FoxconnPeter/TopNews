package com.example.materialtest;

import android.app.Activity;

/**
 * Created by gaohailong on 2016/11/24.
 */

public interface NewsModel {
    void loadNews(Activity activity, String url, NewsModelImpl.OnLoadNewsListListener listener);
}
