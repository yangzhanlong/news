package org.me.zhbj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.me.zhbj.R;
import org.me.zhbj.bean.NewsChannelContentBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017/11/12.
 */

public class NewsListAdapter extends RecyclerView.Adapter {
    private Context context;
    private NewsChannelContentBean newsChannelContentBean;
    private JSONArray data;

    public NewsListAdapter(Context context, NewsChannelContentBean newsChannelContentBean) {
        this.context = context;
        this.newsChannelContentBean = newsChannelContentBean;
        data = getData();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_news, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // 绑定数据
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String pic = null;
        String title = null;
        String time = null;

        try {
            pic = (String) data.getJSONObject(position).get("pic");
            title = (String) data.getJSONObject(position).get("title");
            time = (String) data.getJSONObject(position).get("time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        if (!TextUtils.isEmpty(pic)) {
            Picasso.with(context).load(pic).into(viewHolder.ivIcon);
        }
        viewHolder.tvTitle.setText(title);
        viewHolder.tvTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.length() - 1 : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private JSONArray getData () {
        Gson gson = new Gson();
        String json = gson.toJson(newsChannelContentBean.result);
        JSONArray array = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            array = jsonObject.getJSONArray("list");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return array;
    }

    public void addData(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            data.put(array.getJSONObject(i));
        }
    }
}
