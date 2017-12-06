package org.me.zhbj.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.me.zhbj.R;
import org.me.zhbj.activity.NewsDetailActivity;
import org.me.zhbj.bean.NewsChannelContentBean;
import org.me.zhbj.uttils.Constant;
import org.me.zhbj.uttils.SPUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017/11/12.
 */

public class NewsListAdapter extends RecyclerView.Adapter {
    private Context context;
    private NewsChannelContentBean newsChannelContentBean;

    public NewsListAdapter(Context context, NewsChannelContentBean newsChannelContentBean) {
        this.context = context;
        this.newsChannelContentBean = newsChannelContentBean;
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

        final NewsChannelContentBean.NewsChannelDataBean newsChannelDataBean = newsChannelContentBean.data.get(position);
        List<String> images = newsChannelDataBean.imageUrls;
        if (images != null && images.size() != 0) {
            pic = images.get(0);
        }

        final ViewHolder viewHolder = (ViewHolder) holder;
        Picasso.with(context).load(pic).into(viewHolder.ivIcon);
        viewHolder.tvTitle.setText(newsChannelDataBean.title);
        viewHolder.tvTime.setText(newsChannelDataBean.publishDateStr);

        // 判断条目是否已经阅读
        String readNews = SPUtils.getString(context, Constant.KEY_HAS_READ, "");
        if (readNews.contains(newsChannelDataBean.id)) {
            viewHolder.tvTitle.setTextColor(Color.GRAY);
        } else {
            viewHolder.tvTitle.setTextColor(Color.BLACK);
        }

        // 设置条目的点击事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("url", newsChannelDataBean.url);
                context.startActivity(intent);

                // 新闻条目的唯一标识
                String id = newsChannelDataBean.id;
                // 存储id (SP, file, DB)
                String readNews = SPUtils.getString(context, Constant.KEY_HAS_READ, "");
                if (!readNews.contains(id)) {
                    String value = readNews + "," + id;
                    SPUtils.saveString(context, Constant.KEY_HAS_READ, value);

                    // 把条目变成灰色
                    viewHolder.tvTitle.setTextColor(Color.GRAY);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsChannelContentBean.data != null ? newsChannelContentBean.data.size() : 0;
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

    public void addData(List<NewsChannelContentBean.NewsChannelDataBean> data) {
        newsChannelContentBean.data.addAll(data);
        notifyDataSetChanged();
    }
}
