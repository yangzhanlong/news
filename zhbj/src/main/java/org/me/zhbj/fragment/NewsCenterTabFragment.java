package org.me.zhbj.fragment;


import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.me.zhbj.activity.MainActivity;
import org.me.zhbj.base.BaseFragment;
import org.me.zhbj.base.BaseLoadNetDataOperator;
import org.me.zhbj.bean.NewsCenterBean;
import org.me.zhbj.uttils.Constant;
import org.me.zhbj.uttils.MyLogger;

import okhttp3.Call;

public class NewsCenterTabFragment extends BaseFragment implements BaseLoadNetDataOperator{
    private static final String TAG = "NewsCenterFragment";
    private NewsCenterBean newsCenterBean;

    @Override
    public void initTitle() {
        setIbMenuDisplayState(true);
        setIbPicTypeDisplayState(false);
        setTitle("新闻中心");
    }

    @Override
    public View createContent() {
        return null;
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
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getContext(), "获取新闻中心数据失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        MyLogger.i(TAG,response);
                        //把response  == json 转换成对应的数据模型
                        processData(response);
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
}
