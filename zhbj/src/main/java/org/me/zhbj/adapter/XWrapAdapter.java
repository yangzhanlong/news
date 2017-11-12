package org.me.zhbj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 2017/11/12.
 * 包装的适配器: 区分头，脚，正常的数据(getItemViewType返回0)
 */

public class XWrapAdapter extends RecyclerView.Adapter {

    // 头
    private View mHeaderView;
    // 脚
    private View mFootView;
    // 正常的适配器
    private RecyclerView.Adapter mAdapter;

    public XWrapAdapter(View mHeaderView, View mFootView, RecyclerView.Adapter adapter) {
        this.mHeaderView = mHeaderView;
        this.mFootView = mFootView;
        this.mAdapter = adapter;
    }

    // 处理条目的类型
    @Override
    public int getItemViewType(int position) {
        // 头
        if (position == 0) {
            return RecyclerView.INVALID_TYPE;
        }

        // 正常的布局
        int adjPoisition = position - 1;
        int adapterCount = mAdapter.getItemCount();
        if (adjPoisition < adapterCount) {
            return mAdapter.getItemViewType(adjPoisition);
        }

        // 脚
        return RecyclerView.INVALID_TYPE - 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 头
        if (viewType == RecyclerView.INVALID_TYPE) {
            return new HeaderViewHolder(mHeaderView);
        } else if (viewType == RecyclerView.INVALID_TYPE - 1) { // 脚
            return new FootViewHolder(mFootView);
        }

        // 正常
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 头
        if (position == 0) {
            return;
        }

        // 正常
        int adjPosition = position -1;
        int adapterCount = mAdapter.getItemCount();
        if (adjPosition < adapterCount) {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 2;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
