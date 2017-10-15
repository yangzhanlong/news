package org.me.zhbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import org.me.zhbj.R;
import org.me.zhbj.uttils.Constant;
import org.me.zhbj.uttils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener{

    @BindView(R.id.rl)
    RelativeLayout rl;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Animation animation = createAnimation();
        rl.startAnimation(animation);

        // 监听动画
        animation.setAnimationListener(this);
    }

    // 创建动画
    private Animation createAnimation() {
        AnimationSet set = new AnimationSet(false);
        // 旋转
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        // 缩放
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        // 透明
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);

        set.addAnimation(rotateAnimation);
        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);
        return set;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    // 动画结束时调用
    @Override
    public void onAnimationEnd(Animation animation) {
        //延时2s进入向导界面GuideActivity  该方法在主线程   发送一个延时的消息
        mHandler.postDelayed(new Mytask(), 2000);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private class Mytask implements Runnable {

        @Override
        public void run() {
            // 如果已经执行过向导，直接进入主界面
            boolean has_guide = SPUtils.getBoolean(getApplicationContext(), Constant.KEY_HAS_GUIDE, false);
            if (has_guide) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }
}
