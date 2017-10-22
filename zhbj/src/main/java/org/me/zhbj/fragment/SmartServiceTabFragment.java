package org.me.zhbj.fragment;


import android.view.View;

import org.me.zhbj.base.BaseFragment;

public class SmartServiceTabFragment extends BaseFragment {

    @Override
    public void initTitle() {
        setIbMenuDisplayState(true);
        setIbPicTypeDisplayState(false);
        setTitle("智慧服务");
    }

    @Override
    public View createContent() {
        return null;
    }
}
