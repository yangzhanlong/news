package org.me.zhbj.fragment;


import android.util.Log;
import android.view.View;

import org.me.zhbj.base.BaseFragment;

public class HomeTabFragment extends BaseFragment {

    @Override
    public void initTitle() {
        Log.v("HomeTabFragment", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        setIbMenuDisplayState(false);
        setIbPicTypeDisplayState(false);
        setTitle("首页");
    }

    @Override
    public View createContent() {
        return null;
    }
}
