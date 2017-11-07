package org.me.zhbj.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.me.zhbj.base.NewsCenterContentTabPager;
import org.me.zhbj.bean.NewsCenterBean;
import org.me.zhbj.uttils.Constant;
import org.me.zhbj.uttils.MyLogger;

import java.util.List;


public class NewsCenterTabVPAdapter extends PagerAdapter {
    private static final String TAG = "NewsCenterTabVPAdapter";
    private List<NewsCenterContentTabPager> views;
    private List<NewsCenterBean.NewsCenterNewsTabBean> tabBeanList;
    private List<String> channelList;

    public NewsCenterTabVPAdapter(List<NewsCenterContentTabPager> views,
                                  List<NewsCenterBean.NewsCenterNewsTabBean> newsTabBeanList,
                                  List<String> channelList) {
        this.views = views;
        this.tabBeanList = newsTabBeanList;
        this.channelList = channelList;
    }

    @Override
    public int getCount() {
        return views != null ? views.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position).view;
        container.addView(view);
        NewsCenterContentTabPager tabPager = views.get(position);
        //MyLogger.i(TAG, channelList.get(position));

        String url = Constant.NEWS_URL + "?channel="
                + channelList.get(position)
                + "&start=" + Constant.START
                + "&num=" + Constant.NUM
                + "&appkey=" + Constant.APPKEY;

        MyLogger.i(TAG, url);
        tabPager.loadNetData(url);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return channelList.get(position);
    }
}
