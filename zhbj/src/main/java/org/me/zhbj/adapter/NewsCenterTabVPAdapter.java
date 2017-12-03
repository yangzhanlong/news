package org.me.zhbj.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.me.zhbj.api.ApiConstants;
import org.me.zhbj.base.NewsCenterContentTabPager;
import org.me.zhbj.uttils.MyLogger;

import java.util.List;


public class NewsCenterTabVPAdapter extends PagerAdapter {
    private static final String TAG = "NewsCenterTabVPAdapter";
    private List<NewsCenterContentTabPager> views;
    private List<String> channelName;
    private List<String> channelId;

    public NewsCenterTabVPAdapter(List<NewsCenterContentTabPager> views,
                                  List<String> channelName, List<String> channelId) {
        this.views = views;
        this.channelName = channelName;
        this.channelId = channelId;
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
        MyLogger.i(TAG, position + "");
        MyLogger.i(TAG, channelId.get(position));

        String url = ApiConstants.NEWS_HOST
                + "?pageToken=" + ApiConstants.PAGE_TOKEN
                + "&catid=" + channelId.get(position)
                + "&apikey=" + ApiConstants.APIKEY;

        MyLogger.i(TAG, url);
        tabPager.loadNetData(url);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return tabPager.view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return channelName.get(position);
    }
}
