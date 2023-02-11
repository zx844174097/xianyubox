package com.mugui.GJ.model;

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.POINT;

public class GJTool {
	public static void SetUseTime(long time) {

		com.mugui.ui.DataSave.StaticUI.setTime(20230118);

//		int day = (int) (time / (1000 * 60 * 60 * 24.0));
//		if (day > 999) {
//			com.mugui.ui.DataSave.StaticUI.setTime(1000);
//			return;
//		}
//		time %= (1000l * 60 * 60 * 24);
//		int hour = (int) (time / (1000 * 60 * 60.0));
//		com.mugui.ui.DataSave.StaticUI.setTime("剩余使用时间：" + day + "天 " + hour + "时");
	}

	public static boolean isTopHwndIsGJ() {
		POINT p = new POINT();
		OS.GetCursorPos(p);
		return GameListenerThread.HWND == OS.WindowFromPoint(p);

	}
}
