package com.example.materialtest;

/**
 * Created by gaohailong on 2016/11/24.
 */

public interface NewsView {
    void showLoading();
    void addNews(String result);
    void hideLoading();
    void showErrorMsg(String msg);
}
