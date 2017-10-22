package org.me.zhbj.fragment;


import android.view.View;

import org.me.zhbj.base.BaseFragment;

public class SettingTabFragment extends BaseFragment {

    @Override
    public void initTitle() {
        setIbMenuDisplayState(false);
        setIbPicTypeDisplayState(false);
        setTitle("设置");
    }

    @Override
    public View createContent() {
        return null;
    }
}
