package org.me.zhbj.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 向导界面 Adapter
 */

public class GuideVPAdapter extends PagerAdapter {
    private List<ImageView> views;

    public GuideVPAdapter(List<ImageView> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    // 是否使用了缓存view
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // 添加一个 view 到 viewpager
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
}
