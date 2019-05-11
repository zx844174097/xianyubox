package com.mugui.model;

import com.mugui.MAIN;
import com.mugui.tool.CMD;
import com.mugui.tool.Other;

public class CmdModel {

	static CMD cmd = null;

	public static void 关闭电脑() {
		StackTraceElement element = Thread.currentThread().getStackTrace()[2];
		String className = element.getClassName();
		String methodName = element.getMethodName();
		int lineNumber = element.getLineNumber();
		System.out.println("对象:" + className + " 方法:" + methodName + "(" + lineNumber + ")");

		init();
		cmd.send("shutdown -s -t 10");
		Other.sleep(7000);
		cmd.send("exit");
		MAIN.exit();
	}

	private static void init() {
		if (cmd == null || !cmd.isColose()) {
			cmd = new CMD();
			cmd.start();
		}
	}

	public static void 关闭应用(int app_id) {
		init();
		cmd.send("taskkill /f /t /PID " + app_id);
		Other.sleep(200);
	} 

	public static void 关闭应用(String string) {
		init();
		cmd.send("taskkill /f /t /im " + string);
		Other.sleep(200);
	}

	public static void 打开浏览器(String string) {
		init();
		cmd.send("\"C:\\Program Files\\Internet Explorer\\iexplore.exe\"" + "  \"" + string + "\"");
		Other.sleep(200);
	}

	public static void 打开网页(String string) {
		init();
		cmd.send("start \"" + string + "\"");
		Other.sleep(200);
	}

	public static void 打开应用(String path) {
		init();
		cmd.send("\"" + path + "\"");
		Other.sleep(200);
	}

	public static void run(String path) {
		init();
		cmd.send(path);
		Other.sleep(50);
	}

	public static void close() {
		if (cmd != null)
			cmd.send("exit");
	}
	// public static String run1(String path) {
	// init();
	// cmd.reInfo();
	// cmd.send(path);
	// while (cmd.getInfo().trim().equals("")) {
	// Other.sleep(50);
	// }
	// Other.sleep(2000);
	// return cmd.getInfo();
	// }

}
