package com.eascs.app.htmllib.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.eascs.app.htmllib.constant.Constant;
import com.eascs.app.htmllib.impl.DefaultHandler;
import com.eascs.app.htmllib.interfaces.CallBackFunction;
import com.eascs.app.htmllib.presenter.WebViewPresenter;
import com.eascs.app.htmllib.webviewclient.BaseWebViewClient;

/**
 * @author KevinHo
 * @version V1.0
 * @ClassName: BridgeWebView
 * @Description:
 * @email 20497342@qq.com
 * @date
 */
public class BridgeWebView extends WebView{

    private WebViewPresenter mWebViewPresenter;

    public BridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BridgeWebView(Context context) {
        super(context);
        init();
    }


    /**
     *  私有初始化方法
     */
    private void init() {
        mWebViewPresenter = new WebViewPresenter(this);
    }

    /**
     * 唯一对外提供的Presenter类
     * @return Presenter
     */
    public WebViewPresenter getWebViewPresenter(){
        return mWebViewPresenter;
    }

    /**
     * 对外释放BridgeWebView功能
     */
    public void releasePresenter(){
        if(null == mWebViewPresenter){
            return;
        }
        mWebViewPresenter.releasePresenter();
    }


}
