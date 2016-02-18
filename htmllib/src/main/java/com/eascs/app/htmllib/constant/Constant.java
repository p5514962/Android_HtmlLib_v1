package com.eascs.app.htmllib.constant;

/**
 * @author KevinHo
 * @version V1.0
 * @ClassName:
 * @Description: 全局常量类
 * @email 20497342@qq.com
 * @date
 */
public class Constant {
    public static final String kTag = "WEBVIEW_JAVA_BRIDGE";
    public static final String kInterface = kTag + "_INTERFACE";
    public static final String kCustomProtocolScheme = kTag+"_SCHEME";
    public static final String kQueueHasMessage = kTag+"_QUEUE_MESSAGE_";

    public final static String CALLBACK_ID_FORMAT = "JAVA_CB_%s";
    public final static String JS_HANDLE_MESSAGE_FROM_JAVA = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');";
    public final static String JS_FETCH_QUEUE_FROM_JAVA = "javascript:WebViewJavascriptBridge._fetchQueue();";
    public final static String JAVASCRIPT_STR = "javascript:";
    public final static String UNDERLINE_STR = "_";

    public final static String JS_HANDLER = "testJavascriptHandler";

//    public final static String NATIVE_HANDLER = "testNativeHandler";

    public final static String JS_LOAD_FUNCTION_AND_CALLBACK = "javascript:window." + kInterface
            + ".onResultForScript(%s,%s)";


}
