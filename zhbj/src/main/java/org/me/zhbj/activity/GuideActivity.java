package org.me.zhbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import org.me.zhbj.R;
import org.me.zhbj.adapter.GuideVPAdapter;
import org.me.zhbj.uttils.Constant;
import org.me.zhbj.uttils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.bt_start)
    Button btStart;
    @BindView(R.id.container_gray_point)
    LinearLayout containerGrayPoint;
    @BindView(R.id.red_point)
    View redPoint;

    private int[] imgs = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private List<ImageView> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        // 去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initViewPager();
        initGrayPoint();
    }

    // 动态的创建灰色的点
    private void initGrayPoint() {
        for (int resId : imgs) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.point_gray_bg);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            params.rightMargin = 50; // 设置右边距
            containerGrayPoint.addView(view, params);
        }
    }

    // 初始化ViewPager 的数据
    private void initViewPager() {
        views = new ArrayList<>();
        for (int resId : imgs) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(resId);
            views.add(iv);
        }

        vp.setAdapter(new GuideVPAdapter(views));

        // 设置界面的滑动监听
        vp.addOnPageChangeListener(this);
    }

    // 点击按钮
    @OnClick(R.id.bt_start)
    public void onViewClicked() {
        // 记录已经执行向导操作
        SPUtils.saveBoolean(this, Constant.KEY_HAS_GUIDE, true);
        // 进入主界面
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 两个灰点的距离
    private int width;
    // position 当前滑动页面的下标
    // positionOffset 页面的滑动比率
    // positionOffsetPixels 页面滑动的实际像素
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (width == 0) {
            width = containerGrayPoint.getChildAt(1).getLeft() - containerGrayPoint.getChildAt(0).getLeft();
        }

        // 修改小红点与相对布局的左边距
        LayoutParams params = (LayoutParams) redPoint.getLayoutParams();
        params.leftMargin = (int) (position * width + positionOffset * width);
        redPoint.setLayoutParams(params);
    }

    // 界面被选中时调用
    @Override
    public void onPageSelected(int position) {
        if (position == imgs.length - 1) {
            // 显示按钮
            btStart.setVisibility(View.VISIBLE);
        } else {
            // 隐藏按钮
            btStart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
