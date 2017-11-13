package org.me.zhbj.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.me.zhbj.R;
import org.me.zhbj.adapter.XWrapAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017/11/12.
 */

public class RefreshRecyclerView extends RecyclerView {

    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.default_header)
    LinearLayout defaultHeader;
    private ViewGroup mHeaderView;
    private View mFootview;
    private int mFooterMeasureHeight;
    private int mHeaderMeasureHeight;

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
        ButterKnife.bind(this, mHeaderView);
        // 隐藏进度条
        pb.setVisibility(View.INVISIBLE);
        // 测量默认头的高度
        defaultHeader.measure(0,0);
        // 获取测量后的高度
        mHeaderMeasureHeight = defaultHeader.getMeasuredHeight();
        // 隐藏头
        defaultHeader.setPadding(0, -mHeaderMeasureHeight, 0, 0);
    }

    // 初始化脚
    private void initFootView() {
        mFootview = inflate(getContext(), R.layout.footer, null);
        // 测量
        mFootview.measure(0, 0);
        mFooterMeasureHeight = mFootview.getMeasuredHeight();
        // 隐藏脚
        mFootview.setPadding(0, -mFooterMeasureHeight, 0, 0);
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
