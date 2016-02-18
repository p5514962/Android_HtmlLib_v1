package com.eascs.app.htmllib.impl;

import android.text.TextUtils;
import com.eascs.app.htmllib.constant.Constant;
import com.eascs.app.htmllib.interfaces.BridgeHandler;
import com.eascs.app.htmllib.interfaces.CallBackFunction;
import com.eascs.app.htmllib.interfaces.ISendData;
import com.eascs.app.htmllib.interfaces.IUpdatePresenter;
import com.eascs.app.htmllib.interfaces.JavascriptCallback;
import com.eascs.app.htmllib.model.NativeMessage;
import com.eascs.app.htmllib.untils.BridgeUntil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author KevinHo
 * @version V1.0
 * @ClassName:
 * @Description:
 * @email 20497342@qq.com
 * @date
 */
public class ImplSendData implements ISendData {

    private IUpdatePresenter mIUpdatePresenter;
    private long uniqueId = 0;//回调唯一id
    private long uniqueJavaCbId = 0;//回调唯一id
    private List<NativeMessage> startupMessageQueue = new ArrayList<NativeMessage>();
    private Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();//回调集合
    Map<String, BridgeHandler> messageHandlers = new HashMap<String, BridgeHandler>();
    BridgeHandler defaultHandler;
    boolean isBelowKitKat = false;


    public ImplSendData(IUpdatePresenter mIUpdatePresenter, BridgeHandler defaultHandler, boolean isBelowKitKat) {
        this.mIUpdatePresenter = mIUpdatePresenter;
        this.defaultHandler = defaultHandler;
        this.isBelowKitKat = isBelowKitKat;
    }


    @Override
    public boolean send(String data, CallBackFunction callback, String handlerName) {
        return sendData(data, callback, handlerName);
    }

    @Override
    public boolean send(String data, CallBackFunction callback) {
        return sendData(data, callback, null);//无handlerName
    }

    /**
     * 外部发送消息
     *
     * @param data
     * @param handlerName
     * @param callback    外部回调
     * @return
     */
    private boolean sendData(String data, CallBackFunction callback,
                             String handlerName) {
        if (data == null) {
            return false;//发送失败
        }

        NativeMessage message = new NativeMessage();
        message.setData(data);

        if (callback != null) {//先把外部回调，存到集合中去，并封装到message里面去
            String callbackId = BridgeUntil.getIntance().getUniqueIdForCallBack(uniqueId);
            responseCallbacks.put(callbackId, callback);
            message.setCallbackId(callbackId);
        }

        if (!TextUtils.isEmpty(handlerName)) {
            message.setHandlerName(handlerName);
        }
        queueMessage(message);
        return true;
    }

    private void queueMessage(NativeMessage message) {
        if (startupMessageQueue != null) {
            startupMessageQueue.add(message);
        } else {
            String messageJSON = dispatchMessage(message);
            //调用 js _handleMessageFromNative
            String jsScript = String.format(Constant.JS_HANDLE_MESSAGE_FROM_JAVA, messageJSON);
            if (!isBelowKitKat) {//4.4版本以上
                //Android 4.4系统引入，因此只能在Android4.4系统中才能使用，提供在当前页面显示上下文中异步执行javascript代码
                //这个方法必须在UI线程调用，这个函数的回调也会在UI线程执行。
                mIUpdatePresenter.executeJavascript(jsScript);
            } else {
                mIUpdatePresenter.executeJavascriptBelowKitKat(jsScript);
            }
        }

    }


    /**
     * flushMessageQueue
     *
     * @return
     */
    @Override
    public void flushJsMessageQueue(final JavascriptCallback callback) {
        String script = Constant.JS_FETCH_QUEUE_FROM_JAVA;
        if (!isBelowKitKat) {//4.4版本以上
            //Android 4.4系统引入，因此只能在Android4.4系统中才能使用，提供在当前页面显示上下文中异步执行javascript代码
            //这个方法必须在UI线程调用，这个函数的回调也会在UI线程执行。
            mIUpdatePresenter.executeJavascript(script, callback);
        } else {
            String uid = BridgeUntil.getIntance().getUniqueIdForCallBack(uniqueJavaCbId);
            mIUpdatePresenter.bindJavascriptCallback(uid, callback);
            String callBackScript = String.format(Constant.JS_LOAD_FUNCTION_AND_CALLBACK, uid, script);
            mIUpdatePresenter.executeJavascriptBelowKitKat(callBackScript);
        }
    }



    /**
     * 处理从js 队列返回的数据
     *
     * @param messageQueueString
     */
    @Override
    public void processQueueMessage(String messageQueueString) {
        // deserializeMessage
        List<NativeMessage> list = null;
        try {
            list = NativeMessage.toArrayList(messageQueueString);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (list == null || list.size() == 0) {
            return;
        }
        for (NativeMessage m : list) {
            String responseId = m.getResponseId();
            // 是否是response
            if (!TextUtils.isEmpty(responseId)) {//从native调用js后回到native
                dealWithForJsResponse(m, responseId);
            } else {//js
                dealWithForJsRequest(m);
            }
        }
    }

    /**
     * 来自js调用native
     * @param msgFromJs
     */
    private void dealWithForJsRequest(NativeMessage msgFromJs) {
        CallBackFunction responseFunction = null;
        // if had callbackId
        final String callbackId = msgFromJs.getCallbackId();
        if (!TextUtils.isEmpty(callbackId)) {//当消息handler处理完成，重新把消息回调给js
            responseFunction = initJsCallBackFunction(callbackId);
        } else {
            responseFunction = new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    // do nothing
                }
            };
        }
        BridgeHandler handler;
        if (!TextUtils.isEmpty(msgFromJs.getHandlerName())) {
            handler = messageHandlers.get(msgFromJs.getHandlerName());
        } else {
            handler = defaultHandler;
        }
        if (handler != null) {
            handler.handle(msgFromJs.getData(), responseFunction);
        }
    }

    /**
     * 处理JS的响应
     * @param msgFromJs
     * @param responseId
     */
    private void dealWithForJsResponse(NativeMessage msgFromJs, String responseId) {
        CallBackFunction function = responseCallbacks.remove(responseId);
        String responseData = msgFromJs.getResponseData();
        function.onCallBack(responseData);
    }


    /**
     * 先组装 model->json类型消息model
     *
     * @param message native 消息model
     */
    private String dispatchMessage(NativeMessage message) {
        String messageJSON = message.toJson().toString()
                .replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"")
                .replaceAll("\'", "\\\\\'").replaceAll("\n", "\\\\\n")
                .replaceAll("\r", "\\\\\r").replaceAll("\f", "\\\\\f");
        return messageJSON;
    }


    /**
     * 创建js回调，当js调用完native且handler处理完后，给js发送回执（携带callBackId）
     * @param callBackId
     * @return
     */
    private CallBackFunction initJsCallBackFunction(final String callBackId) {
        return new CallBackFunction() {
            @Override
            public void onCallBack(String data) {//当消息handler处理完成，重新把消息回调给js
                //此处data是应该符合双方格式
                queueMessage(new NativeMessage(callBackId, data));
            }
        };
    }

    @Override
    public void registerHandler(String handlerName, BridgeHandler handler) {
        if (handlerName == null || handlerName.length() == 0 || handler == null)
            return;
        messageHandlers.put(handlerName, handler);
    }
}
