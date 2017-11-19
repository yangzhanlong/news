package org.me.zhbj.fragment;


import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viewpagerindicator.TabPageIndicator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.me.zhbj.R;
import org.me.zhbj.activity.MainActivity;
import org.me.zhbj.adapter.NewsCenterTabVPAdapter;
import org.me.zhbj.base.BaseFragment;
import org.me.zhbj.base.BaseLoadNetDataOperator;
import org.me.zhbj.base.NewsCenterContentTabPager;
import org.me.zhbj.bean.NewsCenterBean;
import org.me.zhbj.bean.NewsChannelBean;
import org.me.zhbj.uttils.Constant;
import org.me.zhbj.uttils.MyLogger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class NewsCenterTabFragment extends BaseFragment implements BaseLoadNetDataOperator, ViewPager.OnPageChangeListener {
    private static final String TAG = "NewsCenterFragment";
    private NewsCenterBean newsCenterBean;
    private TabPageIndicator tabPageIndicator;
    private ImageButton imageButton;
    private ViewPager viewPager;

    private List<NewsCenterContentTabPager> views;
    public NewsChannelBean newsChannelBean;

    @Override
    public void initTitle() {
        setIbMenuDisplayState(true);
        setIbPicTypeDisplayState(false);
        setTitle("新闻");
    }

    public int get_index_of_viewPager() {
        return viewPager.getCurrentItem();
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
                if (item != newsChannelBean.result.size() -1) {
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
        for (int i = 0 ; i < newsChannelBean.result.size(); i++) {
            NewsCenterContentTabPager tabPager = new NewsCenterContentTabPager(getContext(), this);
            views.add(tabPager);
        }

        // 设置适配器
        viewPager.setAdapter(new NewsCenterTabVPAdapter(views, newsCenterBean.data.get(0).children, newsChannelBean.result));

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
        final String channel_url = Constant.CHANNEL_URL + Constant.APPKEY;
        final String url = Constant.NEWSCENTER_URL;
        OkHttpUtils.get()
                .url(url)
                .build()
                .connTimeOut(5000)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        MyLogger.i(TAG,response);
                        //把response  == json 转换成对应的数据模型
                        processData(response);
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getContext(), "获取新闻中心数据失败", Toast.LENGTH_SHORT).show();
                    }
                });



        OkHttpUtils.get()
                .url(channel_url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getContext(), "获取新闻标题数据失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyLogger.i(TAG,response);
                        //把response  == json 转换成对应的数据模型
                        processChannelData(response);
                    }
                });


    }

    //把Json格式的字符串转换成对应的模型对象
    public void processData(String json){
        Gson gson = new Gson();
        newsCenterBean = gson.fromJson(json, NewsCenterBean.class);
        //把数据传递给MainActivity
        ((MainActivity)getActivity()).setNewsCenterMenuBeanList(newsCenterBean.data);
    }

    public void processChannelData(String json) {
        Gson gson = new Gson();
        newsChannelBean = gson.fromJson(json, NewsChannelBean.class);

        // 创建布局
        View view = createContent();

        // 加载布局
        addView(view);
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
}
