package com.kuangxf.baseappas.utils;


import android.util.Log;

import com.kuangxf.baseappas.BuildConfig;

public class LogUtil {

	private static boolean isOpenLog = BuildConfig.isOpenLog;

	public static void e(String msg) {
		if (isOpenLog){
			Log.e(getModule(), msg);
		}
	}

	public static void i(String msg) {
		if (isOpenLog){
			Log.i(getModule(), msg);
		}
	}

	public static void d(String msg) {
		if (isOpenLog){
			Log.d(getModule(), msg);
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

	private static String getSimpleClassName(String name) {
		int lastIndex = name.lastIndexOf(".");
		return name.substring(lastIndex + 1);
	}
}
