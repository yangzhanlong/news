package org.me.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.me.zhbj.base.NewsCenterContentTabPager;

/**
 * Created by user on 2017/11/8.
 */

public class SwitchImageViewPager extends ViewPager {
    private NewsCenterContentTabPager tabPager;

    public void setTabPager(NewsCenterContentTabPager tabPager) {
        this.tabPager = tabPager;
    }

    public SwitchImageViewPager(Context context) {
        super(context);
    }

    public SwitchImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 停止切换
                tabPager.stopSwitch();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                // 开始切换
                tabPager.startSwitch();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
