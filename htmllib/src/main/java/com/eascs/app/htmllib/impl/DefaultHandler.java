package com.eascs.app.htmllib.impl;

import com.eascs.app.htmllib.interfaces.BridgeHandler;
import com.eascs.app.htmllib.interfaces.CallBackFunction;

/**
 * (����)���д�����JS ��Ϣ���Ⱦ������Ŵ��������ٻص���ȥ
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
