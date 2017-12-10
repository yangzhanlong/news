package org.me.zhbj.uttils.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2017/12/6.
 * 网络缓存
 */

public class NetCacheUtils {
    private Context context;

    // 从网络获取图片
    public void getBitmapFromNet(Context context, ImageView iv, String url) {
        this.context = context;
        // 让ImageView 和 url关联起来
        iv.setTag(url);

        // 异步操作
        new Task().execute(iv, url);
    }

    private class Task extends AsyncTask<Object, Void, Bitmap> {
        private ImageView iv;
        private String mUrl;

        @Override
        protected Bitmap doInBackground(Object... params) {
            iv = (ImageView) params[0];
            mUrl = (String) params[1];

            Bitmap bitmap = downloadBitmap(mUrl);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // 获取ImageView 对应的 url
            String url = (String) iv.getTag();
            if (bitmap != null && mUrl.equals(url)) {
                iv.setImageBitmap(bitmap);
                // 磁盘缓存
                LocalCacheUtils.saveBitmapCache(context, bitmap, mUrl);
                // 内存缓存
                MemoryCacheUtils.saveBitmapCache(bitmap, mUrl);
            }
        }
    }

    // 下载图片
    private Bitmap downloadBitmap(String url) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(6000);
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bitmap;
    }
}
