package org.me.zhbj.uttils;

import android.content.Context;

/**
 * Created by user on 2017/12/10.
 * 像素转换工具类
 */

public class Dp2PxUtils {

    // dp 转 px
    public static int db2px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5);
    }
}
