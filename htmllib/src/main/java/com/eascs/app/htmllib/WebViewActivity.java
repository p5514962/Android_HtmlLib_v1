package com.eascs.app.htmllib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.eascs.app.htmllib.constant.Constant;
import com.eascs.app.htmllib.interfaces.BridgeHandler;
import com.eascs.app.htmllib.interfaces.CallBackFunction;
import com.eascs.app.htmllib.presenter.WebViewPresenter;
import com.eascs.app.htmllib.view.BridgeWebView;

public class WebViewActivity extends AppCompatActivity {

    private WebViewPresenter mWebViewPresenter;
    private BridgeWebView mWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebview = (BridgeWebView) findViewById(R.id.webview);
        mWebViewPresenter = mWebview.getWebViewPresenter();
    }

    /**
     *
     * @param data 对外消息实体（可用实体转json字符串）
     * @param responseCallback
     */
    protected void send(String data,CallBackFunction responseCallback){
        mWebViewPresenter.send(data, responseCallback);
    }


    /**
     * @param data 携带消息
     * @param callback 回调函数
     * @param handlerName 消息处理器方法
     */
    public void send(String data, CallBackFunction callback,
                     String handlerName) {//具体可以传业务逻辑handlerName
        mWebViewPresenter.send(data, callback, Constant.JS_HANDLER);//Constant.JS_HANDLER为默认
    }

    /**
     * 对外实现具体activity
     * @param handerName
     * @param handler
     */
    protected void registerHandler(String handerName,BridgeHandler handler){
        mWebViewPresenter.registerHandler(handerName, handler);
        //===========这一段是主项目实现======================//
       /* mWebViewPresenter.registerHandler("testHandler", new BridgeHandler() {
            @Override
            public void handle(String data, CallBackFunction function) {

            }
        });//native 默认消息处理器*/
        //===========这一段是主项目实现======================//
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebViewPresenter.releasePresenter();
    }
}
