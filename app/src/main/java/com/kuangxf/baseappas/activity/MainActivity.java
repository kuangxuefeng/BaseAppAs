package com.kuangxf.baseappas.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kuangxf.baseappas.AppConfig;
import com.kuangxf.baseappas.MyApplication;
import com.kuangxf.baseappas.R;
import com.kuangxf.baseappas.baseactivity.BaseActivity;
import com.kuangxf.baseappas.db.DeviceIdBean;
import com.kuangxf.baseappas.javamail.PassAuthenticator;
import com.kuangxf.baseappas.javamail.SimpleMail;
import com.kuangxf.baseappas.javamail.SimpleMailReceiver;
import com.kuangxf.baseappas.javamail.SimpleMailSend;
import com.kuangxf.baseappas.javamail.UserInfo;
import com.kuangxf.baseappas.utils.DeviceIdUtil;
import com.kuangxf.baseappas.utils.EncUtil;
import com.kuangxf.baseappas.utils.LogUtil;
import com.kuangxf.baseappas.utils.MyDesKeyUtil;

import org.xutils.ex.DbException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import static com.kuangxf.baseappas.MyApplication.db;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_db, btn_send_email, btn_re_email, btn_enc, btn_save, btn_read;
    private EditText et_name, et_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_db = (Button) findViewById(R.id.btn_db);
        btn_send_email = (Button) findViewById(R.id.btn_send_email);
        btn_re_email = (Button) findViewById(R.id.btn_re_email);
        btn_enc = (Button) findViewById(R.id.btn_enc);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_read = (Button) findViewById(R.id.btn_read);

        btn_db.setOnClickListener(this);
        btn_send_email.setOnClickListener(this);
        btn_re_email.setOnClickListener(this);
        btn_enc.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_read.setOnClickListener(this);

        et_name = (EditText) findViewById(R.id.et_name);
        et_pw = (EditText) findViewById(R.id.et_pw);
        et_name.setText(UserInfo.getUserName());
        et_pw.setText(UserInfo.getPw());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_db:
                DeviceIdBean idBean = new DeviceIdBean();
                idBean.setAndroidId(DeviceIdUtil.getAndroidId(this));
                idBean.setBlueToothAddressId(DeviceIdUtil.getMacAddress(this));
                idBean.setIdFromRomId(DeviceIdUtil.getIdFromRom());
                idBean.setImeiId(DeviceIdUtil.getIMEI(this));
                idBean.setMacAddressId(DeviceIdUtil.getMacAddress(this));
                idBean.setUuidId(DeviceIdUtil.getUuid());
                try {
                    db().save(idBean);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                List<DeviceIdBean> ids = null;
                try {
                    ids = db().findAll(DeviceIdBean.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                LogUtil.e("ids=" + ids);
                break;
            case R.id.btn_send_email:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Date d = new Date();
                        LogUtil.e("开始发送");
                        StringBuffer sb = new StringBuffer();
                        sb.append("时间：" + AppConfig.sdf_split.format(d) + "\n\r");
                        sb.append("DeviceIdUtil.getAndroidId(activity)：" + DeviceIdUtil.getAndroidId(mActivity) + "\n\r");
                        sb.append("DeviceIdUtil.getIMEI(activity)：" + DeviceIdUtil.getIMEI(mActivity) + "\n\r");
                        sb.append("DeviceIdUtil.getMacAddress(activity)：" + DeviceIdUtil.getMacAddress(mActivity) + "\n\r");
                        sb.append("DeviceIdUtil.getBlueToothAddress()：" + DeviceIdUtil.getBlueToothAddress() + "\n\r");
                        sb.append("DeviceIdUtil.getIdFromRom()：" + DeviceIdUtil.getIdFromRom() + "\n\r");
                        sb.append("DeviceIdUtil.getUuidFen()：" + DeviceIdUtil.getUuidFen() + "\n\r");
                        sb.append("DeviceIdUtil.getUuid()：" + DeviceIdUtil.getUuid() + "\n\r");

//                        SimpleMailSend.sendEmailOnlyText(sb.toString());
                        MimeMultipart partList = new MimeMultipart();
                        MimeBodyPart part1 = new MimeBodyPart();
                        MimeBodyPart part2 = new MimeBodyPart();
                        try {
                            part1.setText(sb.toString());
                            part2.attachFile(MyApplication.getShare(LogUtil.KeyLogFileName));
                            partList.addBodyPart(part1);
                            partList.addBodyPart(part2);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SimpleMailSend.sendEmail(partList);
                        LogUtil.e("over");
                    }
                }).start();
                break;
            case R.id.btn_re_email:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e("开始接收");
                        SimpleMailReceiver.parse(SimpleMailReceiver.fetchInbox(SimpleMail.getProperties_163(), new PassAuthenticator()));
                        LogUtil.e("over");
                    }
                }).start();
                break;
            case R.id.btn_enc:
                String key = MyDesKeyUtil.get3DesKey(null, true);
                LogUtil.e("key：" + key);
                String str = "abc1234EFG000哈哈来了吗来了吗！！！！！66666666GGGGGG结束了";
                LogUtil.e("元数据：" + str);
                String str1 = EncUtil.encryptAsString(key, str);
                LogUtil.e("加密后数据：" + str1);
                String str2 = EncUtil.desEncryptAsString(key, str1);
                LogUtil.e("加密后再解密的数据：" + str2);
                break;
            case R.id.btn_save:
                String name = et_name.getText().toString().trim();
                String pw = et_pw.getText().toString().trim();
                if (!TextUtils.isEmpty(name)){
                    UserInfo.setUserName(name);
                }
                if (!TextUtils.isEmpty(pw)){
                    UserInfo.setPw(pw);
                }
                break;
            case R.id.btn_read:
                LogUtil.readFile();
                break;
        }
    }
}
