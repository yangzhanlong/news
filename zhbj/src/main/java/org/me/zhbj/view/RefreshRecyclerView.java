package org.me.zhbj.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.me.zhbj.R;
import org.me.zhbj.adapter.XWrapAdapter;

/**
 * Created by user on 2017/11/12.
 */

public class RefreshRecyclerView extends RecyclerView {

    private ViewGroup mHeaderView;
    private View mFootview;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // 初始化
    private void init() {
        initHeaderView();
        initFootView();
    }

    // 初始化头
    private void initHeaderView() {
        mHeaderView = (ViewGroup) inflate(getContext(), R.layout.header, null);
    }

    // 初始化脚
    private void initFootView() {
        mFootview = inflate(getContext(), R.layout.footer, null);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        adapter = new XWrapAdapter(mHeaderView, mFootview, adapter);
        super.setAdapter(adapter);
    }

    // 添加轮播图的方法
    public void addSwitchImageView(View view) {
        mHeaderView.addView(view);
    }
}
