package com.kuangxf.baseappas.javamail;

import android.util.Log;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Administrator on 2017/7/19.
 */

public class SimpleMailSend {
    public static boolean sendEmailOnlyText(String emailString) {
        boolean re = false;
        Session session = SimpleMail.getSession_163(); //获取验证会话

        try {
            //配置发送及接收邮箱
            InternetAddress fromAddress, toAddress;
            fromAddress = new InternetAddress("m18721314176@163.com", "况");//knowheart@163.com(发送邮件地址)  发件人
            toAddress = new InternetAddress("1024883177@qq.com", "");

            //配置发送信息
            MimeMessage message = new MimeMessage(session);
//            message.setContent(emailString, "text/plain");
            message.setText(emailString, "GB2312");
            message.setSubject("测试的内容");//主题
            message.setFrom(fromAddress);
            message.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
            message.saveChanges();

            //连接邮箱并发送
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.163.com", UserInfo.getUserName(), UserInfo.getPw());
            transport.send(message);
            transport.close();
            re = true;
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            Log.i("Msg", e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return re;
    }

    public static boolean sendEmail(MimeMultipart partList) {
        boolean re = false;
        Session session = SimpleMail.getSession_163(); //获取验证会话

        try {
            //配置发送及接收邮箱
            InternetAddress fromAddress, toAddress;
            fromAddress = new InternetAddress("m18721314176@163.com", "况");//knowheart@163.com(发送邮件地址)  发件人
            toAddress = new InternetAddress("1024883177@qq.com", "");

            //配置发送信息
            MimeMessage message = new MimeMessage(session);
//            message.setContent(emailString, "text/plain");
            message.setSubject("测试的内容");//主题
            message.setFrom(fromAddress);
            message.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
            message.saveChanges();
            message.setContent(partList);// 把邮件的内容设置为多部件的集合对象

            //连接邮箱并发送
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.163.com", UserInfo.getUserName(), UserInfo.getPw());
            transport.send(message);
            transport.close();
            re = true;
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            Log.i("Msg", e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re;
    }
}
