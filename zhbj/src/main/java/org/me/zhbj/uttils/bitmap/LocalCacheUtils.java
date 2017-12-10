package org.me.zhbj.uttils.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.me.zhbj.uttils.Md5Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by user on 2017/12/10.
 * 磁盘缓存
 */

public class LocalCacheUtils {
    // 写缓存
    public static void saveBitmapCache(Context context, Bitmap bitmap, String url) {
        // 缓存目录
        File dir = new File(context.getCacheDir(), "zhbj_cache");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 把图片缓存在缓存目录
        File file = new File(dir, Md5Utils.encode(url));
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    }

    // 读缓存
    public static Bitmap readBitmapCache(Context context, String url) {
        File dir = new File(context.getCacheDir(), "zhbj_cache");
        // 目录是否存在
        if (!dir.exists()) {
            return null;
        }

        // 图片是否存在
        File file = new File(dir, Md5Utils.encode(url));
        if (!file.exists()) {
            return null;
        }

        Bitmap bitmap  = BitmapFactory.decodeFile(file.getAbsolutePath());

        // 把图片缓存在内存中
        MemoryCacheUtils.saveBitmapCache(bitmap, url);
        return bitmap;
    }
}
