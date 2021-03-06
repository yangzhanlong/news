package org.me.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

// 禁止 ViewPager 滑动
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 不拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    // 不处理事件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
