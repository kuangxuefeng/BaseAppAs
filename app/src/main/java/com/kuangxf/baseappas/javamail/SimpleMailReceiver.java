package com.kuangxf.baseappas.javamail;

import com.kuangxf.baseappas.AppConfig;
import com.kuangxf.baseappas.utils.LogUtil;

import org.apache.commons.mail.util.MimeMessageParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;


/**
 * Created by Administrator on 2017/7/19.
 */

public class SimpleMailReceiver {

    /**
     * 邮件附件存放位置
     */
    public static final String folder = AppConfig.getSDPath("myEmail");//Environment.getExternalStorageDirectory().getPath() + File.separator + "aDir";

    static {
        File f = new File(folder);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    /**
     * 收取收件箱里的邮件
     *
     * @param props         为邮件连接所需的必要属性
     * @param authenticator 用户验证器
     * @return Message数组（邮件数组）
     */
    public static Message[] fetchInbox(Properties props, Authenticator authenticator) {
        return fetchInbox(props, authenticator, null);
    }


    /**
     * 收取收件箱里的邮件
     *
     * @param props         收取收件箱里的邮件
     * @param authenticator 用户验证器
     * @param protocol      使用的收取邮件协议，有两个值"pop3"或者"imap"
     * @return Message数组（邮件数组）
     */
    public static Message[] fetchInbox(Properties props, Authenticator authenticator, String protocol) {
        Message[] messages = null;
        Session session = Session.getDefaultInstance(props, authenticator);
        // session.setDebug(true);
        Store store = null;
        Folder folder = null;
        try {
            store = protocol == null || protocol.trim().length() == 0 ? session.getStore() : session.getStore(protocol);
            store.connect();
            folder = store.getFolder("INBOX");// 获取收件箱
            folder.open(Folder.READ_ONLY); // 以只读方式打开
            messages = folder.getMessages();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return messages;
    }

    private static void parse(Message message) {
        try {
            LogUtil.e("message.getFrom()=" + message.getFrom());
            LogUtil.e("message.getSubject()=" + message.getSubject());
            LogUtil.e("message.getContentType()=" + message.getContentType());
            LogUtil.e("message.getContent()=" + message.getContent());
            LogUtil.e("格式化 ： message.getSentDate()=" + AppConfig.sdf_split.format(message.getSentDate()));
            LogUtil.e("message.getFileName()=" + message.getFileName());

            MimeMessageParser parser = new MimeMessageParser((MimeMessage) message).parse();
            LogUtil.e("parser.getPlainContent()=" + parser.getPlainContent());
            LogUtil.e("parser.getAttachmentList()=" + parser.getAttachmentList());
            List<DataSource> ls = parser.getAttachmentList();
            if (null != ls && ls.size() > 0) {
                for (DataSource ds : ls) {
                    BufferedOutputStream outStream = null;
                    BufferedInputStream ins = null;
                    try {
                        String fileName = folder + File.separator + ds.getName();
                        outStream = new BufferedOutputStream(new FileOutputStream(fileName));
                        ins = new BufferedInputStream(ds.getInputStream());
                        StringBuffer sb = new StringBuffer();
                        byte[] data = new byte[2048];
                        int length = -1;
                        while ((length = ins.read(data)) != -1) {
                            outStream.write(data, 0, length);
                            sb.append(new String(data));
                        }
                        outStream.flush();
                        if (ins != null) {
                            ins.close();
                        }
                        if (outStream != null) {
                            outStream.close();
                        }
                        LogUtil.e("附件:" + fileName);
                        LogUtil.e("附件内容:" + sb.toString());
                    } finally {
                        if (ins != null) {
                            ins.close();
                        }
                        if (outStream != null) {
                            outStream.close();
                        }
                    }
                }
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parse(Message... messages) {
        if (messages == null || messages.length == 0) {
            System.out.println("没有任何邮件");
        } else {
            for (Message m : messages) {
                parse(m);
            }
            // 最后关闭连接
            if (messages[0] != null) {
                Folder folder = messages[0].getFolder();
                if (folder != null) {
                    try {
                        Store store = folder.getStore();
                        folder.close(false);// 参数false表示对邮件的修改不传送到服务器上
                        if (store != null) {
                            store.close();
                        }
                    } catch (MessagingException e) {
                        // ignore
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
