package org.me.zhbj.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.me.zhbj.R;
import org.me.zhbj.uttils.MyLogger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends AppCompatActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.ib_font)
    ImageButton ibFont;
    @BindView(R.id.ib_share)
    ImageButton ibShare;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.activity_news_detail)
    LinearLayout activityNewsDetail;
    @BindView(R.id.pb)
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        String url = getIntent().getStringExtra("url");
        MyLogger.i("URL", url);

        // Android webview 从Lollipop(5.0)开始webview默认不允许混合模式，https当中不能加载http资源，需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        setCommonSetting();
        webView.setWebChromeClient(new MyWebChromeClient());

        // 设置 WebView 客户端
        webView.setWebViewClient(new MyWebViewClient());

        // 让 WebView 加载url
        webView.loadUrl(url);
    }

    /**
     * 常用设置方法
     */
    private void setCommonSetting() {
        try {
            WebSettings ws = webView.getSettings();
            //支持js
            ws.setJavaScriptEnabled(true);
            //开启DOM storage API功能（HTML5 提供的一种标准的接口，主要将键值对存储在本地，在页面加载完毕后可以通过 JavaScript 来操作这些数据。）
            ws.setDomStorageEnabled(true);
            //设置Application Caches缓存
            ws.setAppCacheEnabled(false);
            //设置缓存方式
            ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
            //启用数据库
            ws.setDatabaseEnabled(true);
            //设置数据库缓存路径
            //启用地理定位
            ws.setGeolocationEnabled(true);
            //设置默认编码
            ws.setDefaultTextEncodingName("utf-8");
            //将图片调整到适合webview的大小
            ws.setUseWideViewPort(true);
            //支持缩放
            ws.setSupportZoom(true);
            //设置支持缩放
            ws.setBuiltInZoomControls(true);
            //支持内容重新布局
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            // 多窗口
            ws.supportMultipleWindows();
            //当webview调用requestFocus时为webview设置节点
            ws.setNeedInitialFocus(true);
            //支持通过JS打开新窗口
            ws.setJavaScriptCanOpenWindowsAutomatically(true);
            // 缩放至屏幕的大小
            ws.setLoadWithOverviewMode(true);
        } catch (Exception e) {
        }
    }

    /**
     * 鉴于WebView的一些漏洞，做出相应的防范措施
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setSafeWebView() {
        try {
            //移除控制不严的有弊端的JavaScript接口
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibility");
            webView.removeJavascriptInterface("accessibilityTraversal");
            //通过WebSettings.setSavePassword(false)关闭密码保存提醒功能
            WebSettings ws = webView.getSettings();
            ws.setSavePassword(false);
            //通过以下设置，防止越权访问，跨域等安全问题
            ws.setAllowFileAccess(false);
            ws.setAllowFileAccessFromFileURLs(false);
            ws.setAllowUniversalAccessFromFileURLs(false);
        } catch (Exception e) {
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(request.getUrl().toString());
            } else {
                view.loadUrl(request.toString());
            }
            return true;
        }

        //页面开始
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.i("页面加载开始", "onPageStarted");
        }

        //页面结束
        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i("页面加载结束", "onPageFinished");
            super.onPageFinished(view, url);
            // 隐藏进度条
            pb.setVisibility(View.GONE);
        }

        //页面出错
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.i("页面加载出错", "onReceivedError");
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Log.i("页面加载httpError", "onReceivedError");
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // 接受所有网站的证书
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

    }


    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.i("页面加载进度", "-->" + newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            callback.invoke(origin, true, false);
        }
    }

    @OnClick({R.id.ib_back, R.id.ib_font, R.id.ib_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_font:
                changeWebViewTextSize();
                break;
            case R.id.ib_share:
                break;
        }
    }

    private  String[] types = new String[] {
        "超大号字体",
        "大号字体",
        "正常字体",
        "小号字体",
        "超小号字体"
    };

    private WebSettings.TextSize[] textSize = new WebSettings.TextSize[] {
            WebSettings.TextSize.LARGER,
            WebSettings.TextSize.LARGEST,
            WebSettings.TextSize.NORMAL,
            WebSettings.TextSize.SMALLER,
            WebSettings.TextSize.SMALLEST,
    };

    int position;
    // 修改网页的字体大小
    private void changeWebViewTextSize() {
        // 单选对话框
        // 设置字体大小
        new AlertDialog.Builder(this)
                .setTitle("选择字体大小")
                .setSingleChoiceItems(types, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position = which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        webView.getSettings().setTextSize(textSize[position]);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
