package org.me.zhbj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.me.zhbj.R;
import org.me.zhbj.activity.MainActivity;
import org.me.zhbj.bean.NewsCenterBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<NewsCenterBean.NewsCenterMenuBean> newsCenterMenuBeanList;

    // 默认选中的条目的下标
    private int selectedPosition;

    public void setNewsCenterMenuBeanList(List<NewsCenterBean.NewsCenterMenuBean> newsCenterMenuBeanList) {
        this.newsCenterMenuBeanList = newsCenterMenuBeanList;
        // 刷新显示
        notifyDataSetChanged();
    }

    public MenuAdapter(Context context, List<NewsCenterBean.NewsCenterMenuBean> newsCenterMenuBeanList) {
        this.context = context;
        this.newsCenterMenuBeanList = newsCenterMenuBeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // 不能做成成员变量，因为做成成员变量newsCenterMenuBean在setTitle的时候无法改变
        final NewsCenterBean.NewsCenterMenuBean newsCenterMenuBean = newsCenterMenuBeanList.get(position);

        // 把RecyclerView.ViewHolder 转成 ViewHolder
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvMenuTitle.setText(newsCenterMenuBean.title);

        // 选择的条目等于当前的position
        if (selectedPosition == position) {
            viewHolder.ivArrow.setImageResource(R.drawable.menu_arr_select);
            viewHolder.tvMenuTitle.setTextColor(Color.RED);

        } else {
            viewHolder.ivArrow.setImageResource(R.drawable.menu_arr_normal);
            viewHolder.tvMenuTitle.setTextColor(Color.WHITE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断被选中的条目是否不等于当前的position
                if (selectedPosition != position) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                    // 修改 fragment 标题
                    ((MainActivity)context).getCurrentFragment().setTitle(newsCenterMenuBean.title);
                }

                // 选中后关闭侧滑菜单
                ((MainActivity)context).slidingMenu.toggle();
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsCenterMenuBeanList != null ? newsCenterMenuBeanList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_arrow)
        ImageView ivArrow;
        @BindView(R.id.tv_menu_title)
        TextView tvMenuTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
