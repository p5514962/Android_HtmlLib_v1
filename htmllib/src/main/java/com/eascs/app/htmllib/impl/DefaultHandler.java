package com.eascs.app.htmllib.impl;

import com.eascs.app.htmllib.interfaces.BridgeHandler;
import com.eascs.app.htmllib.interfaces.CallBackFunction;

/**
 * (对外)所有从来自JS 消息，先经过消磁处理器，再回调出去
 */
public class DefaultHandler implements BridgeHandler {
	String TAG = "DefaultHandler";
	@Override
	public void handle(String data, CallBackFunction function) {
		if(function != null){
			function.onCallBack("DefaultHandler response data:"+data);
		}
	}
}
