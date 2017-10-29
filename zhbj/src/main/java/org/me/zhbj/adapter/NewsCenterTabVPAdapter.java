package org.me.zhbj.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.me.zhbj.bean.NewsCenterBean;

import java.util.List;


public class NewsCenterTabVPAdapter extends PagerAdapter {
    private List<View> views;
    private List<NewsCenterBean.NewsCenterNewsTabBean> tabBeanList;

    public NewsCenterTabVPAdapter(List<View> views, List<NewsCenterBean.NewsCenterNewsTabBean> newsTabBeanList) {
        this.views = views;
        this.tabBeanList = newsTabBeanList;
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
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabBeanList.get(position).title;
    }
}
