package com.kuangxf.baseappas.javamail;

import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.mail.Session;

/**
 * Created by Administrator on 2017/7/19.
 */

public class SimpleMail {
    public static final SimpleDateFormat sdf_split = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss SSS");
    public static final SimpleDateFormat sdf_no_split = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static Properties getProperties_163() {
        Properties props = new Properties();

        props.put("mail.smtp.protocol", "smtp");
        props.put("mail.smtp.auth", "true");        //设置要验证
        props.put("mail.smtp.host", "smtp.163.com");    //设置host
        props.put("mail.smtp.port", "25");  //设置端口

        props.put("mail.pop3.host", "pop.163.com");
        props.put("mail.imap.host", "imap.163.com");
        props.put("mail.store.protocol", "pop3"); // 默认使用pop3收取邮件

        return props;
    }

    public static Session getSession_163() {
        PassAuthenticator pass = new PassAuthenticator();   //获取帐号密码
        Properties props = getProperties_163();
        Session session = Session.getInstance(props, pass); //获取验证会话
        return session;
    }
}
