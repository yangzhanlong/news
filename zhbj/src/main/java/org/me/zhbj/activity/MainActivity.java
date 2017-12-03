package org.me.zhbj.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.me.zhbj.R;
import org.me.zhbj.adapter.MainVPFragmentAdapter;
import org.me.zhbj.adapter.MenuAdapter;
import org.me.zhbj.base.BaseFragment;
import org.me.zhbj.base.BaseLoadNetDataOperator;
import org.me.zhbj.bean.NewsCenterBean;
import org.me.zhbj.fragment.GovaffairsTabFragment;
import org.me.zhbj.fragment.HomeTabFragment;
import org.me.zhbj.fragment.NewsCenterTabFragment;
import org.me.zhbj.fragment.SettingTabFragment;
import org.me.zhbj.fragment.SmartServiceTabFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.tab_vp)
    ViewPager tabVp;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;

    List<Fragment> fragments;
    public SlidingMenu slidingMenu;
    private List<NewsCenterBean.NewsCenterMenuBean> newsCenterMenuBeanList;
    private MenuAdapter menuAdapter;
    private RecyclerView rv;

    // 侧滑菜单数据
    public void setNewsCenterMenuBeanList(List<NewsCenterBean.NewsCenterMenuBean> data) {
        this.newsCenterMenuBeanList = data;
        menuAdapter.setNewsCenterMenuBeanList(data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 初始化 ViewPager
        initViewPager();

        // 设置RadioGroup 监听事件
        rgTab.setOnCheckedChangeListener(this);

        // 初始化侧滑菜单
        initSlidingMenu();

        // 初始化 menu 的 RecyclerView
        initRecyclerView();
    }

    private void initRecyclerView() {
        // 从 slidingMenu 找到 RecyclerView
        rv = (RecyclerView) slidingMenu.findViewById(R.id.rv_menu);

        // 设置布局管理
        rv.setLayoutManager(new LinearLayoutManager(this));

        // 创建适配器给RecyclerView进行数据绑定
        menuAdapter = new MenuAdapter(this, null);
        rv.setAdapter(menuAdapter);
    }

    private void initSlidingMenu() {
        // 创建侧滑对象
        slidingMenu = new SlidingMenu(this);
        //设置菜单从左边滑出
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置侧滑菜单，默认不可以滑出
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        //设置侧滑菜单滑出后的宽度
        slidingMenu.setBehindWidth(500);
        //以内容的形式添加到Activity
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //设置侧滑菜单的布局
        slidingMenu.setMenu(R.layout.activity_main_menu);
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        fragments.add(new HomeTabFragment());
        fragments.add(new NewsCenterTabFragment());
        fragments.add(new SmartServiceTabFragment());
        fragments.add(new GovaffairsTabFragment());
        fragments.add(new SettingTabFragment());

        // ViewPager 绑定 fragments
        tabVp.setAdapter(new MainVPFragmentAdapter(getSupportFragmentManager(), fragments));

        // 让ViewPager缓存5个页面
        tabVp.setOffscreenPageLimit(5);
    }

    // 点击底部的tab切换ViewPager的页面
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int item = 0;
        switch (checkedId) {
            case R.id.rb_home:
                item = 0;
                slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                break;
            case R.id.rb_newscener:
                item = 1;
                slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.rb_smartservice:
                item = 2;
                slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.rb_govaffairs:
                item = 3;
                slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.rb_setting:
                item = 4;
                slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                break;
        }
        tabVp.setCurrentItem(item, false);

        //加载网络数据的入口
        BaseFragment baseFragment = (BaseFragment) fragments.get(item);
        if (baseFragment instanceof BaseLoadNetDataOperator && !baseFragment.hasLoadData) {
            ((BaseLoadNetDataOperator) baseFragment).loadNetData();
        }
    }

    // 获取当前tab选中的fragment
    public BaseFragment getCurrentFragment(){
        int currentItem = tabVp.getCurrentItem();
        BaseFragment baseFragment = (BaseFragment) fragments.get(currentItem);
        return baseFragment;
    }
}
