package org.me.zhbj.bean;

import java.util.List;

/**
 * Created by user on 2017/11/5.
 */

public class NewsChannelContentBean {
    public boolean hasNext;
    public String retcode;
    public String appCode;
    public String dataType;
    public String pageToken;
    public List<NewsChannelDataBean> data;

    public class NewsChannelDataBean {
        public String posterId;
        public String catPathKey;
        public List<String> tags;
        public String publishDate;
        public String commentCount;
        public List<String> imageUrls;
        public String id;
        public String posterScreenName;
        public String title;
        public String url;
        public String publishDateStr;
        public String content;
        public String videoUrls;
    }
}
