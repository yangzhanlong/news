package org.me.zhbj.uttils.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import org.me.zhbj.uttils.MyLogger;


/**
 * Created by user on 2017/12/6.
 * 图片三级缓存框架
 */

public class BitmapUtils {
    private static final String TAG ="BitmapUtils";

    static {
        netCacheUtils = new NetCacheUtils();
        localCacheUtils = new LocalCacheUtils();
    }

    private static NetCacheUtils netCacheUtils;
    private static LocalCacheUtils localCacheUtils;

    // 显示图片
    public static void display(Context context, ImageView iv, String url) {
        // 内存缓存

        // 磁盘缓存
        Bitmap bitmap = localCacheUtils.readBitmapCache(context, url);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
            MyLogger.i(TAG, "从磁盘获取图片");
            return;
        }

        // 网络缓存
        netCacheUtils.getBitmapFromNet(context, iv, url);
    }
}
