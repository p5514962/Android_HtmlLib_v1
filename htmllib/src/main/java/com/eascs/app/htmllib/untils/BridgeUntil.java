package com.eascs.app.htmllib.untils;

import android.os.SystemClock;

import com.eascs.app.htmllib.constant.Constant;

/**
 * @author KevinHo
 * @version V1.0
 * @ClassName:
 * @Description:
 * @email 20497342@qq.com
 * @date
 */
public class BridgeUntil {

    public static BridgeUntil intance;

    public static BridgeUntil getIntance() {
        if (null == intance) {
            synchronized (BridgeUntil.class) {
                if (null == intance) {
                    intance = new BridgeUntil();
                }
            }
        }
        return intance;
    }


    public String getUniqueIdForCallBack(long uniqueId) {
        String result = "";
        try {
            result = String.format(Constant.CALLBACK_ID_FORMAT, ++uniqueId + (Constant.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
        } catch (Exception e) {
            result = "";
        }
        return result;
    }


}
