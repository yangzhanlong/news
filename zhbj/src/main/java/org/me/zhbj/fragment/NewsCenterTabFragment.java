package org.me.zhbj.fragment;


import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
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
import org.me.zhbj.bean.NewsCenterBean;
import org.me.zhbj.uttils.Constant;
import org.me.zhbj.uttils.MyLogger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class NewsCenterTabFragment extends BaseFragment implements BaseLoadNetDataOperator{
    private static final String TAG = "NewsCenterFragment";
    private NewsCenterBean newsCenterBean;
    private TabPageIndicator tabPageIndicator;
    private ImageButton imageButton;
    private ViewPager viewPager;

    private List<View> views;

    @Override
    public void initTitle() {
        setIbMenuDisplayState(true);
        setIbPicTypeDisplayState(false);
        setTitle("新闻");
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
                if (item != newsCenterBean.data.get(0).children.size() -1) {
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
        for (NewsCenterBean.NewsCenterNewsTabBean tabBead : newsCenterBean.data.get(0).children) {
            TextView tv = new TextView(getContext());
            tv.setText(tabBead.title);
            tv.setTextColor(Color.RED);
            tv.setTextSize(20);
            tv.setGravity(Gravity.CENTER);
            views.add(tv);
        }

        // 设置适配器
        viewPager.setAdapter(new NewsCenterTabVPAdapter(views, newsCenterBean.data.get(0).children));

        // 绑定 tabPageIndicator 和 viewPager
        tabPageIndicator.setViewPager(viewPager);
    }

    // 加载网络数据
    @Override
    public void loadNetData() {
        final String url = Constant.NEWSCENTER_URL;
        OkHttpUtils.get()
                .url(url)
                .build()
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
    }

    //把Json格式的字符串转换成对应的模型对象
    public void processData(String json){
        Gson gson = new Gson();
        newsCenterBean = gson.fromJson(json, NewsCenterBean.class);
        //把数据传递给MainActivity
        ((MainActivity)getActivity()).setNewsCenterMenuBeanList(newsCenterBean.data);

        // 创建布局
        View view = createContent();

        // 加载布局
        addView(view);
    }
}
