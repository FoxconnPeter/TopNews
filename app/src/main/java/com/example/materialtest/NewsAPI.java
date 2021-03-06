package com.example.materialtest;

/**
 * Created by gaohailong on 2016/11/24.
 */

public class NewsAPI {
    /**
     * 知乎api
     */
    public static final String ZHIHU_LATEST_NEWS = "http://news-at.zhihu.com/api/4/news/latest";
    public static final String ZHIHU_HISTORY_NEWS = "http://news-at.zhihu.com/api/4/news/before/";
    public static final String ZHIHU_NEWS_DETAIL = "http://news-at.zhihu.com/api/4/news/";
    /**
     * 微信精选api
     */
    public static final String WEIXIN_NEWS = "http://api.tianapi.com/wxnew/?key=a54000546301b5bf55cc3c3f50ca7957&num=15&page=";
    public static final String WEIXIN_RANDOM_NEWS = "http://api.tianapi.com/wxnew/?key=a54000546301b5bf55cc3c3f50ca7957&num=15&rand=1";
    /**
     * 豆瓣api
     */
    public static final String DOUBAN_NEWS = "https://moment.douban.com/api/stream/date/";
    public static final String DOUBAN_NEWS_DETAIL = "https://moment.douban.com/api/post/";
}
