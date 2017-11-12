package org.me.zhbj.base;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.me.zhbj.R;
import org.me.zhbj.adapter.NewsListAdapter;
import org.me.zhbj.adapter.SwitchImageVPAdapter;
import org.me.zhbj.bean.NewsCenterTabBean;
import org.me.zhbj.bean.NewsChannelContentBean;
import org.me.zhbj.bean.NewsChannelDatasBean;
import org.me.zhbj.uttils.MyLogger;
import org.me.zhbj.view.RefreshRecyclerView;
import org.me.zhbj.view.SimpleDividerItemDecoration;
import org.me.zhbj.view.SwitchImageViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * Created by user on 2017/10/29
 * 新闻中心内容tab页面
 */

public class NewsCenterContentTabPager implements ViewPager.OnPageChangeListener {
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

    // 处理轮播图自动切换 (消息机制)
    private Handler mHandler = new Handler();
    // 判断是否在切换
    private boolean hasSwitch;
    private RefreshRecyclerView rv_news;

    // 切换任务
    private class SwitchTask implements Runnable {

        @Override
        public void run() {
            int currentItem = vpSwitchImage.getCurrentItem();

            if (imaeViews != null){
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


    public NewsCenterContentTabPager(Context context) {
        this.context = context;
        view = initView();
    }

    private View initView() {
        View view = View.inflate(context, R.layout.newscenter_content_tab, null);
        rv_news = (RefreshRecyclerView) view.findViewById(R.id.rv_news);
        ButterKnife.bind(view);
        return view;
    }

    public void loadNetData(String url) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(context, "加载数据失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyLogger.i(TAG, response);
                        //把response  == json 转换成对应的数据模型
                        try {
                            processData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    // 把json字符串转换成对应的数据模型
    private void processData(String json) throws JSONException {
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
        rv_news.addSwitchImageView(view);
    }

    private void initRVNews() {
        // 设置布局管理器
        rv_news.setLayoutManager(new LinearLayoutManager(context));
        // 设置条目分割线
        rv_news.addItemDecoration(new SimpleDividerItemDecoration(context));
        // 设置数据  RevyclerView Adapter  ViewHolder
        NewsListAdapter adapter = new NewsListAdapter(context, newsChannelContentBean);
        rv_news.setAdapter(adapter);
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
        Gson gson = new Gson();
        String json = gson.toJson(newsChannelContentBean.result);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray array =  jsonObject.getJSONArray("list");

        ArrayList<String> pics = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            if (i == 3) {
                break;
            }

            String pic, title;
            pic = (String) array.getJSONObject(i).get("pic");
            title = (String) array.getJSONObject(i).get("title");

            if (!TextUtils.isEmpty(pic)) {
                pics.add(pic);
                ImageView iv = new ImageView(context);
                //iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.with(context).load(pic).into(iv);
                imaeViews.add(iv);
                titles.add(title);
            }
        }

        // 在轮播图的前后添加多一张图片 (实现无限循环)
        // 在开始添加一个页面
        {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(context).load(pics.get(pics.size() - 1)).into(iv);
            imaeViews.add(0, iv);
        }

        // 在最后添加一个页面
        {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
}
