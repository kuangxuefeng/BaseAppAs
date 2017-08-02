package com.kuangxf.baseappas;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class AppConfig {
    public static final String BASE_ACTIVITY_LOG_INFO_STRING = "  run...(send by base)";
    public static final SimpleDateFormat sdf_split = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss SSS");
    public static final SimpleDateFormat sdf_no_split = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    //----------------------------数据库--------------------------
    public static final String DB_NAME = "MyDB.db";//数据库名称
    public static final int Db_Version = 1;//数据库名称
    public static final int number = 10;//db 分页
    public static final String FOLDER_ROOT = "baseAppWorks";//整个应用存放在内存卡的根目录
    /**
     * 数据库存放位置
     */
    public static final String folder_db = AppConfig.getSDPath("db");

    /**
     * log存放位置
     */
    public static final String folder_log = AppConfig.getSDPath("log");

    /**
     * 获取存放路径，需要读写sdk的权限
     *
     * @return
     */
    public static String getSDPath(String childDir) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString() + File.separator + FOLDER_ROOT;
        File file = new File(dir);
        makeDir(file);
        if (!TextUtils.isEmpty(childDir)) {
            dir = dir + File.separator + childDir;
        }
        file = new File(dir);
        makeDir(file);
        return dir;
    }

    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    public static void creatFile(File file) {
        if (!file.getParentFile().exists()) {
            makeDir(file.getParentFile());
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
