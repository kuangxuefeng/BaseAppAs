package com.kuangxf.baseappas.javamail;


import android.text.TextUtils;

import com.kuangxf.baseappas.MyApplication;
import com.kuangxf.baseappas.utils.EncUtil;
import com.kuangxf.baseappas.utils.MyDesKeyUtil;

/**
 * Created by kuangxf on 2017/8/1.
 */

public class UserInfo {
    public static final String KEY_USER_NAME = "key_user_name";
    public static final String KEY_PW = "key_pw";

    private static final String userNameDef = "m18721314176@163.com";
    private static final String pwEncDef = "";

    public static String getUserName() {
        String userName = MyApplication.getShare(KEY_USER_NAME, userNameDef);
        return userName;
    }

    public static void setUserName(String userName) {
        MyApplication.saveShare(KEY_USER_NAME, userName);
    }

    public static String getPw() {
        String pwEnc = MyApplication.getShare(KEY_PW, pwEncDef);
        String pw = EncUtil.desEncryptAsString(MyDesKeyUtil.get3DesKeyDef(), pwEnc);
        return pw;
    }

    public static void setPw(String pw) {
        String pwEnc = EncUtil.encryptAsString(MyDesKeyUtil.get3DesKeyDef(), pw);
        MyApplication.saveShare(KEY_PW, pwEnc);
    }

    public static boolean checkInfo(){
        if ((!TextUtils.isEmpty(getUserName()))&&(!TextUtils.isEmpty(getPw()))){
            return true;
        }else {
            return false;
        }
    }
}
