package com.kuangxf.baseappas.utils;

import android.text.TextUtils;

/**
 * Created by kuangxf on 2017/7/31.
 */

public class MyDesKeyUtil {
    private static final String SOURCE_DATA = "kuangxuefeng";
    private static final String HEX_STR = "0123456789ABCDEF";
    private static final int DES_KEY_LEN = 32;

    public static String get3DesKey(String sourceData, boolean isUp) {
        if (TextUtils.isEmpty(sourceData)) {
            sourceData = SOURCE_DATA;
        }
        String key = "";
        for (int i = 0; i < DES_KEY_LEN; i++) {
            char c = sourceData.charAt(i % sourceData.length());
            int index = (int) c;
            key = key + HEX_STR.charAt((index + i) % HEX_STR.length());
        }
        if (isUp) {
            key.toUpperCase();
        } else {
            key.toLowerCase();
        }
        return key;
    }

    public static String get3DesKeyDef() {
        return get3DesKey(null, true);
    }
}
