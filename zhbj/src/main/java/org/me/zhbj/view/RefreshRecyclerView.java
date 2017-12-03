package org.me.zhbj.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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

    private static final String TAG = "RefreshRecyclerView";
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
        mHeaderView = (ViewGroup) View.inflate(getContext(), R.layout.header, null);
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

    // 轮播图
    private View mSwitchImageView;
    // 添加轮播图的方法
    public void addSwitchImageView(View view) {
        this.mSwitchImageView = view;
        mHeaderView.addView(view);
    }

    public void removeSwitchImageView() {
        if (mSwitchImageView != null) {
            mHeaderView.removeView(mSwitchImageView);
        }
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
    // 没有用 onTouchEvent，因为 dispatchTouchEvent 回调的频率高一些
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();

                // 解决轮播图回缩的问题
                // 获取 RecyclerView 在窗体的位置
                int[] rvLocation = new int[2];
                getLocationInWindow(rvLocation);

                // 获取轮播图在窗体的位置
                int[] location = new int[2];
                mSwitchImageView.getLocationInWindow(location);

                // 对比 RecyclerView 和轮播图的Y轴的值
                if (location[1] < rvLocation[1]) {
                    // 不处理
                    return super.dispatchTouchEvent(ev);
                }

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
                        tvState.setText("正在加载");
                        // 把头缩回至本身头的位置
                        defaultHeader.setPadding(0, 0, 0, 0);
                        // 清除动画
                        ivArrow.clearAnimation();
                        // 隐藏箭头，显示进度条
                        ivArrow.setVisibility(View.INVISIBLE);
                        pb.setVisibility(View.VISIBLE);
                        // 加载最新的数据
                        if (mOnRefreshListener != null) {
                            mOnRefreshListener.onRefresh();
                        }
                    }
                }

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean hasLoadMoreData = false;
    //滑动状态改变 (实现上拉加载)
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        // 状态类型: SCROLL_STATE_DRAGGING 手指按下 -> 手指拖拽列表移动 -> 手指停止拖拽 -> 抬起手指
        //          SCROLL_STATE_SETTLING 手指按下 -> 手指快速拖拽后抬起手指 -> 列表继续滚动 -> 停止滚动
        //          SCROLL_STATE_IDLE 空闲

        // 判断当前的 state 是否等于空闲状态
        boolean isState = state == RecyclerView.SCROLL_STATE_IDLE;
        // 获取当前最后一个可见Item的position
        int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
        // 判断 lastVisibleItemPosition 是否等于适配器的最后一个条目
        boolean isLastVisibleItem = lastVisibleItemPosition == getAdapter().getItemCount() - 2;

        //在静止的状态下   && 必须是最后显示的条目就是RecyclerView的最后一个条目 && 没有在加载更多的数据
        if (isState && isLastVisibleItem && !hasLoadMoreData && mOnLoadMoreListener != null) {
            hasLoadMoreData = true;
            // 显示脚
            mFootview.setPadding(0, 0, 0, 0);
            // 滑动到显示的脚的位置
            smoothScrollToPosition(lastVisibleItemPosition);

            mOnLoadMoreListener.onLoadMore();
        }

    }

    public void hideHeaderView(boolean loadState) {
        // 隐藏进度条，显示箭头，修改状态，修改文字内容，通过数据加载成功的状态去判断是否更改上次加载数据的实现
        pb.setVisibility(View.INVISIBLE);
        ivArrow.setVisibility(View.VISIBLE);
        mHeaderState = DOWN_REFRESH_STATE;
        tvState.setText("下拉刷新");
        defaultHeader.setPadding(0, -mHeaderMeasureHeight, 0, 0);
        if (loadState) {
            String dateStr = DateFormat.getDateFormat(getContext()).format(System.currentTimeMillis());
            String timeStr = DateFormat.getTimeFormat(getContext()).format(System.currentTimeMillis());
            tvTime.setText(dateStr + " " + timeStr);
        }
        getAdapter().notifyDataSetChanged();
    }

    public void hideFooterView() {
        hasLoadMoreData = false;
        mFootview.setPadding(0, -mFooterMeasureHeight, 0, 0);
        // 刷新数据
        getAdapter().notifyDataSetChanged();
    }

    // 加载最新数据的方法
    public interface OnRefreshListener {
        public void onRefresh();
    }

    private OnRefreshListener mOnRefreshListener;
    public void setOnRefreshListener(OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }

    //加载更多的接口
    public interface OnLoadMoreListener{
        void onLoadMore();
    }
    private OnLoadMoreListener mOnLoadMoreListener;
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener){
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }
}
