package com.eascs.app.htmllib.interfaces;

/**
 * 作者：KevinHo on 2016/2/4 0004 10:18
 * 邮箱：20497342@qq.com
 */
public interface IUpdatePresenter {
    //==============4.4 版本及以上专用===================//
    void executeJavascript(String script, JavascriptCallback javascriptCallback);

    void executeJavascript(String script);
    //==============end===================//

    //==============4.4 版本以下专用===================//
    void executeJavascriptBelowKitKat(String script);

    void bindJavascriptCallback(String uid, JavascriptCallback javascriptCallback);
    //==============end===================//
}
