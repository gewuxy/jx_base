package lib.ys.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import java.util.Map;

import lib.ys.R;
import lib.ys.ui.interfaces.impl.WebOption;
import lib.ys.ui.interfaces.impl.WebViewSetter;
import lib.ys.ui.interfaces.web.IWebViewHost;


abstract public class WebViewActivityEx extends ActivityEx implements IWebViewHost {

    private WebView mWebView;
    private ProgressBar mProgressBar;

    private WebViewSetter mSetter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_web_view_ex;
    }

    @CallSuper
    @Override
    public void findViews() {
        mWebView = findView(R.id.web_view_ex_wv);
        mProgressBar = findView(R.id.web_view_ex_progress_bar);
    }

    @CallSuper
    @Override
    public void setViews() {
        mSetter = new WebViewSetter(this);
        mSetter.set(mWebView, mProgressBar);

        WebSettings settings = mWebView.getSettings();
        settings.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }

        onLoadStart();
    }

    /**
     * 初始化完后第一次加载url, 强制执行
     */
    abstract protected void onLoadStart();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSetter.onResultData(requestCode, resultCode, data);
    }

    /**
     * 抓取h5链接中的title
     *
     * @param mH5Title
     */
    public void onReceivedWebTitle(String mH5Title) {
    }

    @Override
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, @NonNull Map<String, String> headers) {
        if (headers == null) {
            loadUrl(url);
        } else {
            mWebView.loadUrl(url, headers);
        }
    }

    @Override
    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    @Override
    public boolean canGoForward() {
        return mWebView.canGoForward();
    }

    @Override
    public void goBack() {
        mWebView.goBack();
    }

    @Override
    public void goForward() {
        mWebView.goForward();
    }

    protected WebView getWebView() {
        return mWebView;
    }

    @Override
    public WebOption getOption() {
        return WebOption.newBuilder().build();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {

            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }

            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();

        }
        super.onDestroy();
    }
}
