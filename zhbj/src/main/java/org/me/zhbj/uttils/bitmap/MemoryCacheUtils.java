package org.me.zhbj.uttils.bitmap;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by user on 2017/12/10.
 * 内存缓存
 */

public class MemoryCacheUtils {

    static {
        caches = new HashMap<>();
    }

    private static HashMap<String, Bitmap> caches;

    // 写缓存
    public static void saveBitmapCache(Bitmap bitmap, String url) {
        caches.put(url, bitmap);
    }

    // 读缓存
    public static Bitmap readBitmapCache(String url) {
        Bitmap bitmap = caches.get(url);
        return bitmap;
    }
}
