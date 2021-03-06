package org.me.zhbj.base;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.me.zhbj.R;
import org.me.zhbj.adapter.NewsListAdapter;
import org.me.zhbj.adapter.SwitchImageVPAdapter;
import org.me.zhbj.api.ApiConstants;
import org.me.zhbj.bean.NewsCenterTabBean;
import org.me.zhbj.bean.NewsChannelContentBean;
import org.me.zhbj.bean.NewsChannelDatasBean;
import org.me.zhbj.fragment.NewsCenterTabFragment;
import org.me.zhbj.uttils.CacheUtils;
import org.me.zhbj.uttils.MyLogger;
import org.me.zhbj.view.RefreshRecyclerView;
import org.me.zhbj.view.SimpleDividerItemDecoration;
import org.me.zhbj.view.SwitchImageViewPager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * Created by user on 2017/10/29
 * 新闻中心内容tab页面
 */

public class NewsCenterContentTabPager implements ViewPager.OnPageChangeListener, RefreshRecyclerView.OnRefreshListener, RefreshRecyclerView.OnLoadMoreListener {
    private static final String TAG = "NewsCenterContentTabPager";
    //@BindView(R.id.vp_switch_image)
    SwitchImageViewPager vpSwitchImage;
    //@BindView(R.id.tv_title)
    TextView tvTitle;
    //@BindView(R.id.ll_point_container)
    LinearLayout llPointContainer;
    private Context context;
    public View view;
    private NewsCenterTabBean newsCenterTabBean;
    private List<ImageView> imaeViews;
    private NewsChannelContentBean newsChannelContentBean;
    private NewsChannelDatasBean newsChannelDatasBean;
    private ArrayList<String> titles;

    private NewsCenterTabFragment newsCenterTabFragment;

    @BindView(R.id.rv_news)
    RefreshRecyclerView rv_news;

    // 处理轮播图自动切换 (消息机制)
    private Handler mHandler = new Handler();
    // 判断是否在切换
    private boolean hasSwitch;
    //private RefreshRecyclerView rv_news;
    private NewsListAdapter adapter;
    private ArrayList<String> mImagesUrls;

    // 切换任务
    private class SwitchTask implements Runnable {

        @Override
        public void run() {
            if (imaeViews != null){
                int currentItem = vpSwitchImage.getCurrentItem();
                // 判断是否是最后一页
                if (currentItem == imaeViews.size() - 1) {
                    currentItem = 0;
                } else {
                    currentItem++;
                }
                vpSwitchImage.setCurrentItem(currentItem);
            }

            mHandler.postDelayed(this, 3000);
        }
    }

    // 开始切换
    public void startSwitch() {
        if (!hasSwitch) {
            // 向 handler 发送一个延时的消息
            mHandler.postDelayed(new SwitchTask(), 3000);
        }
    }

    // 停止切换
    public void stopSwitch() {
        // 清空消息队列
        mHandler.removeCallbacksAndMessages(null);
        hasSwitch = false;
    }


    public NewsCenterContentTabPager(Context context, NewsCenterTabFragment newsCenterTabFragment) {
        this.context = context;
        this.newsCenterTabFragment = newsCenterTabFragment;
        view = initView();
    }

    private View initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.newscenter_content_tab, null);
        //rv_news = (RefreshRecyclerView) view.findViewById(R.id.rv_news);
        ButterKnife.bind(this, view);
        return view;
    }

    public void loadNetData(final String url) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        try {
                            String json = CacheUtils.readCache(context, url);
                            if (!TextUtils.isEmpty(json)) {
                                processData(json);
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        Toast.makeText(context, "加载数据失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //把response  == json 转换成对应的数据模型
                        MyLogger.i(TAG, response);
                        try {
                            processData(response);
                            CacheUtils.saveCache(context, url, response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    // 把json字符串转换成对应的数据模型
    private void processData(String json) throws JSONException {
        rv_news.removeAllViews();
        Gson gson = new Gson();
        newsChannelContentBean = gson.fromJson(json, NewsChannelContentBean.class);

        // 把数据绑定给对应的控件
        bindDataToView();

        // 把当前 NewsCenterContentTabPager 对象传递给 SwitchImageViewPager
        vpSwitchImage.setTabPager(this);
    }

    // 绑定数据给控件
    private void bindDataToView() throws JSONException {
        loadSwitchImageLayout();
        initSwitchImageView();
        initPoint();
        initRVNews();
    }

    // 动态加载轮播图的布局
    private void loadSwitchImageLayout() {
        View view = View.inflate(context, R.layout.switch_image_view, null);
        // 动态加载不能使用 ButterKnife
        // ButterKnife.bind(this, view);
        vpSwitchImage = (SwitchImageViewPager) view.findViewById(R.id.vp_switch_image);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        llPointContainer = (LinearLayout) view.findViewById(R.id.ll_point_container);
        rv_news.removeSwitchImageView();
        rv_news.addSwitchImageView(view);
    }

    private void initRVNews() {
        // 设置布局管理器
        rv_news.setLayoutManager(new LinearLayoutManager(context));
        // 设置条目分割线
        rv_news.addItemDecoration(new SimpleDividerItemDecoration(context));
        // 设置数据  RecyclerView Adapter  ViewHolder
        adapter = new NewsListAdapter(context, newsChannelContentBean);
        rv_news.setAdapter(adapter);
        // 设置下拉刷新的监听
        rv_news.setOnRefreshListener(this);
        rv_news.setOnLoadMoreListener(this);
    }

    // 初始化点
    private void initPoint() {
        // 清空容器里面的布局
        llPointContainer.removeAllViews();

        for (int i = 0; i < imaeViews.size() - 2; i++) {
            // 小圆点
            View view = new View(context);
            // 设置背景颜色
            view.setBackgroundResource(R.drawable.point_gray_bg);
            // 布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            // 右边距
            params.rightMargin = 20;
            // 添加布局
            llPointContainer.addView(view, params);
        }

        // 让第一个点为红色
        llPointContainer.getChildAt(0).setBackgroundResource(R.drawable.point_red_bg);
    }

    // 初始化轮播图的数据
    private void initSwitchImageView() throws JSONException {
        imaeViews = new ArrayList<>();
        titles = new ArrayList<>();

        ArrayList<String> pics = new ArrayList<>();

        for (int i = 0; i < newsChannelContentBean.data.size(); i++) {
            if (i == 3) {
                break;
            }

            String pic = null, title;
            title = newsChannelContentBean.data.get(i).title;
            mImagesUrls = (ArrayList<String>) newsChannelContentBean.data.get(i).imageUrls;
            if (mImagesUrls != null && mImagesUrls.size() != 0) {
                pic = mImagesUrls.get(0);
            }

            pics.add(pic);
            ImageView iv = new ImageView(context);
            //iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(context).load(pic).into(iv);
            imaeViews.add(iv);
            titles.add(title);
        }

        // 在轮播图的前后添加多一张图片 (实现无限循环)
        // 在开始添加一个页面
        {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(context).load(pics.get(pics.size() - 1)).into(iv);
            imaeViews.add(0, iv);
        }

        // 在最后添加一个页面
        {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(context).load(pics.get(0)).into(iv);
            imaeViews.add(imaeViews.size(), iv);
        }

        // 设置适配器
        SwitchImageVPAdapter adapter = new SwitchImageVPAdapter(imaeViews);
        vpSwitchImage.setAdapter(adapter);


        tvTitle.setText(titles.get(0));
        vpSwitchImage.addOnPageChangeListener(this);
        vpSwitchImage.setCurrentItem(1, false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    // 页面被选中
    @Override
    public void onPageSelected(int position) {
        // 真实下标
        int pageIndex = 0;

        if (position == 0) {
            pageIndex = titles.size() - 1;
            vpSwitchImage.setCurrentItem(titles.size(), false);
        } else if (position == titles.size() + 1) {
            pageIndex = 0;
            vpSwitchImage.setCurrentItem(1, false);
        } else {
            pageIndex = position - 1;
        }

        // 设置轮播图的文字显示
        tvTitle.setText(titles.get(pageIndex));

        // 设置轮播图的点的背景颜色
        int childCount = llPointContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = llPointContainer.getChildAt(i);
            // 切换的图片跟位置一样
            if (pageIndex == i) {
                child.setBackgroundResource(R.drawable.point_red_bg);
            } else {
                child.setBackgroundResource(R.drawable.point_gray_bg);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // 下拉加载数据
    @Override
    public void onRefresh() {
        int position = newsCenterTabFragment.get_index_of_viewPager();
        List<String> channelId = newsCenterTabFragment.get_channel_id();

        String url = ApiConstants.NEWS_HOST
                + "?pageToken=" + ApiConstants.PAGE_TOKEN
                + "&catid=" + channelId.get(position)
                + "&apikey=" + ApiConstants.APIKEY;

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(context, "联网获取数据失败", Toast.LENGTH_SHORT).show();
                        // 隐藏头
                        rv_news.hideHeaderView(false);
                        // 让轮播图继续切换
                        startSwitch();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            processData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // 隐藏头
                        rv_news.hideHeaderView(true);
                        // 让轮播图继续切换
                        startSwitch();
                    }
                });
    }

    private int pageToken = 0;
    // 加载更多数据
    @Override
    public void onLoadMore() {
        pageToken += 1;
        int position = newsCenterTabFragment.get_index_of_viewPager();
        List<String> channelId = newsCenterTabFragment.get_channel_id();
        String url = ApiConstants.NEWS_HOST
                + "?pageToken=" + pageToken
                + "&catid=" + channelId.get(position)
                + "&apikey=" + ApiConstants.APIKEY;

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(context, "联网获取数据失败", Toast.LENGTH_SHORT).show();
                        // 隐藏脚
                        rv_news.hideFooterView();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        newsChannelContentBean = gson.fromJson(response, NewsChannelContentBean.class);
                        adapter.addData(newsChannelContentBean.data);

                        // 隐藏脚
                        rv_news.hideFooterView();
                    }
                });
    }
}
