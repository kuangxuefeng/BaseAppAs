package com.kuangxf.baseappas.javamail;

import android.text.TextUtils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by Administrator on 2017/7/19.
 */

public class PassAuthenticator extends Authenticator {

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        if (TextUtils.isEmpty(UserInfo.getUserName()) || TextUtils.isEmpty(UserInfo.getPw())) {
            return null;
        }
        return new PasswordAuthentication(UserInfo.getUserName(), UserInfo.getPw());
    }
}