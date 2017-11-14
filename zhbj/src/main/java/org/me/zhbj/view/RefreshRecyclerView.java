package org.me.zhbj.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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

    // 头的状态
    private int mHeaderState = DOWN_REFRESH_STATE;

    // 下拉刷新
    private final static int DOWN_REFRESH_STATE = 0;
    // 释放刷新
    private final static int RELEASE_REFRESH_STATE = 1;
    // 正在刷新
    private final static int REFRESHING_STATE = 2;
    private Animation animation1;
    private Animation animation2;

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
        initAnimation();
    }

    // 初始化动画
    private void initAnimation() {
        animation1 = createAnimation1();
        animation2 = createAnimation2();
    }

    private Animation createAnimation1() {
        Animation animation = new RotateAnimation(
                0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        return animation;
    }

    private Animation createAnimation2() {
        Animation animation = new RotateAnimation(
                -180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        return animation;
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

    // 重写setLayoutManager, 获取布局管理器对象
    private LinearLayoutManager lm;
    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        lm = (LinearLayoutManager) layout;
    }

    // 记录按下的位置 (只记录垂直方向)
    private int downY;
    private int diaY;

    // 分发事件
    // 没有用 onTouchEvent，因为 dispatchTouchEvent 回答的频率高一些
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();

                // 获取显示在屏幕的第一个条目
                int position = lm.findFirstVisibleItemPosition();
                // 头部的高度(随着往下拉，高度不断变大)
                diaY = moveY - downY;
                int top = -mHeaderMeasureHeight + diaY;
                if (position == 0 && diaY > 0) {
                    // 切换头的状态
                    if (mHeaderState == DOWN_REFRESH_STATE && top >= 0) {
                        // 由下拉刷新变为释放刷新
                        mHeaderState = RELEASE_REFRESH_STATE;
                        tvState.setText("释放刷新");
                        // 执行动画
                        ivArrow.startAnimation(animation1);
                    } else if (mHeaderState == RELEASE_REFRESH_STATE && top < 0) {
                        mHeaderState = DOWN_REFRESH_STATE;
                        tvState.setText("下拉刷新");
                        ivArrow.startAnimation(animation2);
                    }
                    // 执行头的显示和隐藏
                    defaultHeader.setPadding(0, top, 0, 0);
                }

                break;
            case MotionEvent.ACTION_UP: // 弹起
            case MotionEvent.ACTION_CANCEL: // 事件取消
            case MotionEvent.ACTION_OUTSIDE: // 外部点击
                int firstVisibleItemPosition = lm.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == 0 && diaY > 0) {
                    if (mHeaderState == DOWN_REFRESH_STATE) {
                        // 隐藏头
                        defaultHeader.setPadding(0, -mHeaderMeasureHeight, 0 ,0);
                    } else if (mHeaderState == RELEASE_REFRESH_STATE) {
                        // 把状态切换为正在加载
                        mHeaderState = REFRESHING_STATE;
                        // 把头缩回至本身头的位置
                        defaultHeader.setPadding(0, 0, 0, 0);
                        // 清除动画
                        ivArrow.clearAnimation();
                        // 隐藏箭头，显示进度条
                        ivArrow.setVisibility(View.INVISIBLE);
                        pb.setVisibility(View.VISIBLE);
                        // 加载最新的数据
                    }
                }



                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
