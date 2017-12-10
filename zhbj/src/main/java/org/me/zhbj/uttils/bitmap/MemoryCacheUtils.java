package org.me.zhbj.uttils.bitmap;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by user on 2017/12/10.
 * 内存缓存: 通过HashMap进行数据的存储
 */

public class MemoryCacheUtils {

    static {
        //caches = new HashMap<>(); // Android 虚拟机的内存只有16M, 当图片不断增加的时候，会产生OOM异常 (内存溢出)
        // java 语言提供另一种机制：软引用、弱引用、虚引用
        // 软引用： 当虚拟机内存不足的时候，回收软引用的对象
        // 弱引用：当对象没有应用的时候，马上回收
        // 虚引用：任何情况下都可能回收
        // java 默认的数据类型是强引用类型

        caches = new HashMap<>();
    }

    private static HashMap<String, SoftReference<Bitmap>> caches;

    // 写缓存
    public static void saveBitmapCache(Bitmap bitmap, String url) {
        //caches.put(url, bitmap);
        SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bitmap);
        caches.put(url, soft);
    }

    // 读缓存
    public static Bitmap readBitmapCache(String url) {
        //Bitmap bitmap = caches.get(url);
        SoftReference<Bitmap> soft = caches.get(url);
        Bitmap bitmap = null;
        if (soft != null) {
            bitmap = soft.get();
        }
        return bitmap;
    }
}
