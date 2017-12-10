package org.me.zhbj.uttils.bitmap;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by user on 2017/12/10.
 * 内存缓存: 通过HashMap进行数据的存储
 */

public class MemoryCacheUtils {

    static {
        //caches = new HashMap<>();
        // 问题一： Android 虚拟机的内存只有16M, 当图片不断增加的时候，会产生OOM异常 (内存溢出)
        // 解决方法：使用软引用
        // java 语言提供另一种机制：软引用、弱引用、虚引用
        // 软引用： 当虚拟机内存不足的时候，回收软引用的对象
        // 弱引用：当对象没有应用的时候，马上回收
        // 虚引用：任何情况下都可能回收
        // java 默认的数据类型是强引用类型

        //caches = new HashMap<>();

        // 问题二：因为从 Android 2.3(API Level9)开始， 垃圾回收器会更倾向于持有软引用或者弱引用的对象，这让软引用和弱引用变得不再可靠
        // 解决方法：使用 LruCache: least recently used 最近最少使用的算法
        // 图片A
        // 图片B
        // 图片C (优先被回收)
        // 图片B
        // 图片A
        long maxMemory = Runtime.getRuntime().maxMemory(); // 获取 Dalvik 虚拟机最大的内存大小:16M
        // 获取图片的大小
        lruCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) { // 指定内存缓存集合的大小
            // 获取图片的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };


    }

    //private static HashMap<String, Bitmap> caches;  // OOM
    //private static HashMap<String, SoftReference<Bitmap>> caches; // 更容易被回收，不可靠
    private static LruCache<String, Bitmap> lruCache; // 最少使用算法

    // 写缓存
    public static void saveBitmapCache(Bitmap bitmap, String url) {
        //caches.put(url, bitmap);
        //SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bitmap);
        //caches.put(url, soft);
        lruCache.put(url, bitmap);
    }

    // 读缓存
    public static Bitmap readBitmapCache(String url) {
        //Bitmap bitmap = caches.get(url);
        //SoftReference<Bitmap> soft = caches.get(url);
        //Bitmap bitmap = null;
        //if (soft != null) {
        //    bitmap = soft.get();
        //}
        return lruCache.get(url);
    }
}
