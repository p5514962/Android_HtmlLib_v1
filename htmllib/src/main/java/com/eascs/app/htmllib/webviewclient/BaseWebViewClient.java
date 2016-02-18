package com.eascs.app.htmllib.webviewclient;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eascs.app.htmllib.constant.Constant;
import com.eascs.app.htmllib.interfaces.IFlushJsMessageQueue;

/**
 * @author KevinHo
 * @version V1.0
 * @ClassName:
 * @Description:
 * @email 20497342@qq.com
 * @date
 */
public class BaseWebViewClient extends WebViewClient {

    private IFlushJsMessageQueue mIFlushJsMessageQueue;

    public BaseWebViewClient(IFlushJsMessageQueue mIFlushJsMessageQueue){
        this.mIFlushJsMessageQueue = mIFlushJsMessageQueue;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(Constant.kCustomProtocolScheme)) {//
            if (url.indexOf(Constant.kQueueHasMessage) > 0) {
                if(mIFlushJsMessageQueue != null){
                    mIFlushJsMessageQueue.flushJsMessageQueue();
                }
            }
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
}
