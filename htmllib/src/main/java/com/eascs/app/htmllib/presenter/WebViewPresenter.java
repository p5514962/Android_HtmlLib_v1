package com.eascs.app.htmllib.presenter;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.eascs.app.htmllib.constant.Constant;
import com.eascs.app.htmllib.impl.DefaultHandler;
import com.eascs.app.htmllib.impl.ImplSendData;
import com.eascs.app.htmllib.interfaces.BridgeHandler;
import com.eascs.app.htmllib.interfaces.CallBackFunction;
import com.eascs.app.htmllib.interfaces.IFlushJsMessageQueue;
import com.eascs.app.htmllib.interfaces.ISendData;
import com.eascs.app.htmllib.interfaces.IUpdatePresenter;
import com.eascs.app.htmllib.interfaces.JavascriptCallback;
import com.eascs.app.htmllib.interfaces.JsInterface;
import com.eascs.app.htmllib.webviewclient.BaseWebViewClient;

/**
 * @author KevinHo
 * @version V1.0
 * @ClassName:
 * @Description: P
 * @email 20497342@qq.com
 * @date
 */
public class WebViewPresenter implements IUpdatePresenter, IFlushJsMessageQueue {

    private final boolean isBelowKitKat;
    private WebView webView;
    private ISendData mISendData;
    private JsInterface myInterface;

    public WebViewPresenter(WebView webView) {
        isBelowKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        mISendData = new ImplSendData(this, new DefaultHandler(), isBelowKitKat);//具体发送数据实现
        myInterface = new JsInterface();
        this.webView = webView;
        this.webView.setVerticalScrollBarEnabled(false);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.getSettings().setJavaScriptEnabled(true);//设置js可用
        this.webView.addJavascriptInterface(myInterface, Constant.kInterface);
        webView.setWebViewClient(new BaseWebViewClient(this));
    }

    /**
     * js 回来通知native调用js WebViewJavascriptBridge._fetchQueue()
     */
    @Override
    public void flushJsMessageQueue() {
        mISendData.flushJsMessageQueue(new JavascriptCallback() {
            @Override
            public void onReceiveValue(String responseDataFromJs) {//
                mISendData.processQueueMessage(responseDataFromJs);
            }
        });
    }

    /**
     * @param data        携带消息
     * @param callback    回调函数
     * @param handlerName 消息处理器方法
     */
    public void send(String data, CallBackFunction callback,
                     String handlerName) {
        mISendData.send(data, callback, handlerName);
    }

    /**
     * @param data     携带消息
     * @param callback 回调函数
     */
    public void send(String data, CallBackFunction callback) {
        mISendData.send(data, callback);
    }


    //==============默认所有核心webview调用js方法回到当前类实现==============//

    /**
     * sdk 版本大于等于4.4的webview调用js方法
     *
     * @param script 具体脚本
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void executeJavascript(String script, final JavascriptCallback javascriptCallback) {
        //这个方法必须在UI线程调用，这个函数的回调也会在UI线程执行。
        webView.evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {//只要调用都是会回调此方法
                if (null != javascriptCallback) {//当从_fetchQueue返回的时候携带js返回数据
                    if (value != null && value.startsWith("\"")
                            && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1)
                                .replaceAll("\\\\", "");
                    }
                    javascriptCallback.onReceiveValue(value);
                }
            }
        });
    }

    /**
     * @param jsScript
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void executeJavascript(String jsScript) {//调用 JS _handleMessageFromNative
        webView.evaluateJavascript(jsScript, null);
    }

    @Override
    public void executeJavascriptBelowKitKat(String script) {//调用 JS _handleMessageFromNative或者_fetchQueue
        webView.loadUrl(script);
    }

    @Override
    public void bindJavascriptCallback(String uid, JavascriptCallback javascriptCallback) {
        myInterface.addCallback(uid, javascriptCallback);
    }

    public void registerHandler(String handlerName, BridgeHandler handler) {
        mISendData.registerHandler(handlerName,handler);
    }

    //==============end==============//

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void releasePresenter() {
        if (null != webView) {
            try {
                webView.removeJavascriptInterface(Constant.kInterface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
