package org.me.zhbj.fragment;


import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.viewpagerindicator.TabPageIndicator;

import org.me.zhbj.R;
import org.me.zhbj.adapter.NewsCenterTabVPAdapter;
import org.me.zhbj.base.BaseFragment;
import org.me.zhbj.base.BaseLoadNetDataOperator;
import org.me.zhbj.base.NewsCenterContentTabPager;
import org.me.zhbj.bean.NewsCenterBean;
import org.me.zhbj.bean.NewsChannelBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsCenterTabFragment extends BaseFragment implements BaseLoadNetDataOperator, ViewPager.OnPageChangeListener {
    private static final String TAG = "NewsCenterFragment";
    private NewsCenterBean newsCenterBean;
    private TabPageIndicator tabPageIndicator;
    private ImageButton imageButton;
    private ViewPager viewPager;

    private List<NewsCenterContentTabPager> views;
    public NewsChannelBean newsChannelBean;

    private Map<Integer, View> viewMap = new HashMap<>();
    private List<String> channel_name;
    private List<String> channel_id;

    @Override
    public void initTitle() {
        setIbMenuDisplayState(true);
        setIbPicTypeDisplayState(false);
        setTitle("新闻");
    }

    public int get_index_of_viewPager() {
        return viewPager.getCurrentItem();
    }

    public List<String> get_channel_id() {
        return channel_id;
    }

    @Override
    public View createContent() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.newscenter_content, (ViewGroup) getView(), false);
        tabPageIndicator = (TabPageIndicator) view.findViewById(R.id.tabPagerIndicator);
        imageButton = (ImageButton) view.findViewById(R.id.ib_next);
        viewPager = (ViewPager) view.findViewById(R.id.vp_newscenter_content);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取 viewpager 当前显示页面的下标
                int item = viewPager.getCurrentItem();

                // 下标没有到最后，都切换到下一个tab页面
                if (item != channel_name.size() -1) {
                    viewPager.setCurrentItem(item + 1);
                }
            }
        });

        // 初始化 viewpager 的数据
        initViewPager();
        return view;
    }

    private void initViewPager() {
        views = new ArrayList<>();
        for (int i = 0 ; i < channel_name.size(); i++) {
            NewsCenterContentTabPager tabPager = new NewsCenterContentTabPager(getContext(), this);
            views.add(tabPager);
        }

        // 设置适配器
        viewPager.setAdapter(new NewsCenterTabVPAdapter(views, channel_name, channel_id));

        // 绑定 tabPageIndicator 和 viewPager
        tabPageIndicator.setViewPager(viewPager);

        // 让第一个子 tab 开始轮播切换
        views.get(0).startSwitch();

        // 给 ViewPager设置切换监听
        // 注意；ViewPager 和 tabPageIndicator 配合使用，监听只能使用tabPageIndicator
        tabPageIndicator.setOnPageChangeListener(this);
    }

    // 加载网络数据
    @Override
    public void loadNetData() {
        processChannelData();
    }

    public void processChannelData() {
        hasLoadData = true;
        channel_name = Arrays.asList(getContext().getResources().getStringArray(R.array.news_channel_name));
        channel_id = Arrays.asList(getContext().getResources().getStringArray(R.array.news_channel_id));

        // 创建布局
        View view = createContent();

        // 加载布局
        addView(view);

        // 把布局添加到缓存View
        viewMap.put(0, view);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    // 当前选中的新闻中心子tab
    @Override
    public void onPageSelected(int position) {
        // 当前的tab开始切换，其他的tab停止切换
        for (int i = 0; i < views.size(); i++) {
            NewsCenterContentTabPager tabPager = views.get(i);
            if (position == i) {
                tabPager.startSwitch();
            } else {
                tabPager.stopSwitch();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // 切换Menu标题, 选择对应的内容
    public void switchContent(int position) {
        /*
        switch (position) {
            case 2: // 组图
                ibPicType.setVisibility(View.VISIBLE);
                break;
            default:
                ibPicType.setVisibility(View.GONE);
        }

        // 从缓存View中获取view
        View view = viewMap.get(position);
        if (view == null) {
            // 创建内容
            container.removeAllViews();
        } else {
            addView(view);
        }
        */
    }

}
