package org.me.zhbj.uttils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by user on 2017/12/3.
 */

public class CacheUtils {

    // 缓存json
    public static void saveCache(Context context, String url, String json) throws IOException {
        // 文件么
        String name = Md5Utils.encode(url);
        // 输出流
        FileOutputStream fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
        // 写数据
        fileOutputStream.write(json.getBytes());
        // 关闭流
        fileOutputStream.close();
    }

    // 读取缓存
    public static String readCache(Context context, String url) throws IOException {
        String name = Md5Utils.encode(url);
        FileInputStream fileInputStream = context.openFileInput(name);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0 ;
        while ((len = fileInputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        String json = bos.toString();
        bos.close();
        fileInputStream.close();
        return json;
    }
}
