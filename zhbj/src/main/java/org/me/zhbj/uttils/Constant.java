package org.me.zhbj.uttils;

/**
 * 常量类
 */

public class Constant {

    // 是否已经执行完向导
    public final static String KEY_HAS_GUIDE = "key_has_guide";

    // 是否已经阅读新闻条目
    public final static String KEY_HAS_READ = "key_has_read";

    //服务器的主机
    public final static String HOST = "http://139.162.86.113:8080";

    //新闻中心页面的数据地址
    public final static String NEWSCENTER_URL = HOST + "/data";


    public static final String APPKEY = "7e2b1cf0cfe6e527";
    public static final String NEWS_URL = "http://api.jisuapi.com/news/get";
    public static final String CHANNEL_URL = "http://api.jisuapi.com/news/channel?appkey=";
    public static final String CHANNEL_HEADER = "头条";// utf8  新闻频道(头条,财经,体育,娱乐,军事,教育,科技,NBA,股票,星座,女性,健康,育儿)
    public static final String CHANNEL_NEWS = "新闻";// utf8  新闻频道(头条,财经,体育,娱乐,军事,教育,科技,NBA,股票,星座,女性,健康,育儿)
    public static final int NUM = 10;// 数量 默认10，最大40
    public static final int START = 0;
}
