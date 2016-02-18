package com.eascs.app.htmllib.interfaces;


import java.util.Map;

/**
 * 作者：KevinHo on 2016/2/2 0002 14:04
 * 邮箱：20497342@qq.com
 */
public interface ISendData {

    /**
     * @param data 携带数据
     * @param callback 回调函数
     * @param handlerName
     */
    boolean send(String data, CallBackFunction callback,
              String handlerName);

    /**
     * @param data 携带数据
     * @param callback 回调函数
     */
    boolean send(String data, CallBackFunction callback);


    void flushJsMessageQueue(JavascriptCallback javascriptCallback);

    void processQueueMessage(String messageQueueString);

    void registerHandler(String handlerName, BridgeHandler handler);

}
