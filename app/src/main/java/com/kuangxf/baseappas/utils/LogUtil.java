package com.kuangxf.baseappas.utils;


import android.text.TextUtils;
import android.util.Log;

import com.kuangxf.baseappas.AppConfig;
import com.kuangxf.baseappas.BuildConfig;
import com.kuangxf.baseappas.MyApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class LogUtil {

	private static boolean isOpenLog = BuildConfig.isOpenLog;
	public final static String KeyLogFileName = "KeyLogFileName";
	private static String logFile;
	private static final long logFileSizeKB = 50 * 1024;//50*1024  为50m
	static {
		getLogFilePath(false);
		File f = new File(logFile);
		double fLen = f.length()/1024.00;
		if (fLen > logFileSizeKB){
			getLogFilePath(true);
		}
	}

	public static void e(String msg) {
		if (isOpenLog){
			String tag = getModule();
			Log.e(tag, msg);
			writeFile(tag, msg);
		}
	}

	public static void i(String msg) {
		if (isOpenLog){
			String tag = getModule();
			Log.i(tag, msg);
			writeFile(tag, msg);
		}
	}

	public static void d(String msg) {
		if (isOpenLog){
			String tag = getModule();
			Log.d(tag, msg);
			writeFile(tag, msg);
		}
	}

	private static String getModule() {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		int stackOffset = -1;
		int methodCount = 2;
		for (int i = 3; i < trace.length; i++) {
			StackTraceElement e = trace[i];
			String name = e.getClassName();
			if (!name.equals(LogUtil.class.getName())) {
				stackOffset = i;
				break;
			}
		}
		for (int i = methodCount; i > 0; i--) {
			int stackIndex = stackOffset;
			String simpleClassName = getSimpleClassName(trace[stackIndex]
					.getClassName());
			if (simpleClassName.startsWith("TLog")) {
				continue;
			} else {
				i = 0;
			}
			StringBuilder builder = new StringBuilder();
			builder.append("")
					.append(" (")
					.append(trace[stackIndex].getFileName())
					.append(":")
					.append(trace[stackIndex].getLineNumber())
					.append(") [")
					.append(getSimpleClassName(trace[stackIndex].getClassName()))
					.append(".").append(trace[stackIndex].getMethodName())
					.append("]");
			return builder.toString();
		}
		return "-----";
	}

	private static void getLogFilePath(boolean isForceUpdate){
		logFile = MyApplication.getShare(KeyLogFileName, "");
		if (TextUtils.isEmpty(logFile) || isForceUpdate){
			logFile = AppConfig.folder_log + File.separator + AppConfig.sdf_no_split.format(new Date()) + ".txt";
			AppConfig.creatFile(new File(logFile));
			MyApplication.saveShare(KeyLogFileName, logFile);
		}
	}

	private static String getSimpleClassName(String name) {
		int lastIndex = name.lastIndexOf(".");
		return name.substring(lastIndex + 1);
	}

	private static boolean writeFile(String TAG, String value) {
		String str = EncUtil.encryptAsString(MyDesKeyUtil.get3DesKeyDef(), TAG + " : " + value);
		return writeFile(str);
	}

	private static boolean writeFile(String str) {
		boolean re = false;
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(logFile), true));
			bw.write(str);
			bw.newLine();
			bw.flush();
			bw.close();
			re = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return re;
	}

	public static void readFile() {
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(new File(logFile));
			br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null) {
				Log.e("line = ", EncUtil.desEncryptAsString(MyDesKeyUtil.get3DesKeyDef(), line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
