package org.me.zhbj.fragment;


import android.view.View;

import org.me.zhbj.base.BaseFragment;

public class GovaffairsTabFragment extends BaseFragment {

    @Override
    public void initTitle() {
        setIbMenuDisplayState(true);
        setIbPicTypeDisplayState(false);
        setTitle("政务");
    }

    @Override
    public View createContent() {
        return null;
    }
}
