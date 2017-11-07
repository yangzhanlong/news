package org.me.zhbj.bean;

import java.util.List;

/**
 * Created by user on 2017/11/5.
 */

public class NewsChannelDatasBean {
    public String channel;
    public String num;
    public List<NewsChannelDataBean> list;

    public class NewsChannelDataBean {
        public String title;
        public String time;
        public String src;
        public String category;
        public String pic;
        public String content;
        public String url;
        public String weburl;
    }
}
