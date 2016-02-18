package com.eascs.app.htmllib.interfaces;

import android.webkit.JavascriptInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KevinHo
 * @version V1.0
 * @ClassName:
 * @Description: 在native 注入的js对象(js 可通过对象名称调用类中方法)
 * @email 20497342@qq.com
 * @date
 */
public class JsInterface {

    Map<String, JavascriptCallback> map;

    public JsInterface() {
        map = new HashMap<String, JavascriptCallback>();
    }

    public void addCallback(String key, JavascriptCallback callback) {
        map.put(key, callback);
    }

    @JavascriptInterface
    public void onResultForScript(String key, String value) {
//        Log.i(kTag, "onResultForScript: " + value);
        JavascriptCallback javascriptCallback = map.remove(key);
        if (javascriptCallback != null)
            javascriptCallback.onReceiveValue(value);
    }
}
