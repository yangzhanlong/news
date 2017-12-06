package org.me.zhbj.uttils.bitmap;

import android.widget.ImageView;

/**
 * Created by user on 2017/12/6.
 * 图片三级缓存框架
 */

public class BitmapUtils {

    static {
        netCacheUtils = new NetCacheUtils();
    }

    private static NetCacheUtils netCacheUtils;

    // 显示图片
    public static void display(ImageView iv, String url) {
        // 内存缓存

        // 磁盘缓存

        // 网络缓存
        netCacheUtils.getBitmapFromNet(iv, url);
    }
}
