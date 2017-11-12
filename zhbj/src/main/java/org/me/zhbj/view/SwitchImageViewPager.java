package org.me.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

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

    private int startX;
    private int startY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 停止切换
                tabPager.stopSwitch();
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();

                // 移动距离
                int diaX = moveX - startX;
                int diaY = moveY - startY;

                // 处理的是水平的滑动，并且是向右的
                if (Math.abs(diaX) > Math.abs(diaY) && moveX > startX) {
                    // 请求外层控件不要拦截事件
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                // 处理点击事件
                int upX = (int) ev.getX();
                int upY = (int) ev.getY();
                if (upX == startX && upY == startY) {
                    Toast.makeText(getContext(), "点击事件", Toast.LENGTH_SHORT).show();
                }

                // 开始切换
                tabPager.startSwitch();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
