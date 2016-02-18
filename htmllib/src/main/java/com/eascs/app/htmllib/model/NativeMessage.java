package com.eascs.app.htmllib.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KevinHo
 * @version V1.0
 * @ClassName:
 * @Description:
 * @email 20497342@qq.com
 * @date
 */
public class NativeMessage {

    private final static String CALLBACK_ID_TAG = "callbackId";
    private final static String RESPONSE_ID_TAG = "responseId";
    private final static String RESPONSE_DATA_TAG = "responseData";
    private final static String DATA_TAG = "data";
    private final static String HANDLER_NAME_TAG = "handlerName";

    String data = null;
    String callbackId = null;
    String handlerName = null;
    String responseId = null;
    String responseData = null;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public NativeMessage(String responseId,String responseData){
        this.responseId = responseId;
        this.responseData = responseData;
    }

    public NativeMessage(){
    }


    public String toJson() {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put(CALLBACK_ID_TAG, getCallbackId());
            jsonObject.put(DATA_TAG, getData());
            jsonObject.put(HANDLER_NAME_TAG, getHandlerName());
            jsonObject.put(RESPONSE_DATA_TAG, getResponseData());
            jsonObject.put(RESPONSE_ID_TAG, getResponseId());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NativeMessage toMessage(String jsonStr) {
        NativeMessage m =  new NativeMessage();
        try {
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            m.setHandlerName(jsonObject.getString(HANDLER_NAME_TAG));
            m.setCallbackId(jsonObject.getString(CALLBACK_ID_TAG));
            m.setResponseData(jsonObject.getString(RESPONSE_DATA_TAG));
            m.setResponseId(jsonObject.getString(RESPONSE_ID_TAG));
            m.setData(jsonObject.getString(DATA_TAG));
            return m;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return m;
    }

    public static List<NativeMessage> toArrayList(String jsonStr){
        List<NativeMessage> list = new ArrayList<NativeMessage>();
        try {
            JSONArray jsonArray = JSON.parseArray(jsonStr);
            for(int i = 0; i < jsonArray.size(); i++){
                NativeMessage m = new NativeMessage();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                m.setHandlerName(jsonObject.getString(HANDLER_NAME_TAG));
                m.setCallbackId(jsonObject.getString(CALLBACK_ID_TAG));
                m.setResponseData(jsonObject.getString(RESPONSE_DATA_TAG));
                m.setResponseId(jsonObject.getString(RESPONSE_ID_TAG));
                m.setData(jsonObject.getString(DATA_TAG));
                list.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
