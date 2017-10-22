package org.me.zhbj.fragment;


import android.view.View;

import org.me.zhbj.base.BaseFragment;

public class HomeTabFragment extends BaseFragment {

    @Override
    public void initTitle() {
        setIbMenuDisplayState(false);
        setIbPicTypeDisplayState(false);
        setTitle("首页");
    }

    @Override
    public View createContent() {
        return null;
    }
}
