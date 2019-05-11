package com.mugui.model;

import javax.swing.JPanel;

import com.mugui.DataSaveInterface;
import com.mugui.MAIN;
import com.mugui.ManagerInterface;
import com.mugui.Dui.DInputDialog;
import com.mugui.jni.Tool.DJni;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.GameListenerThread;

public class GJUIHandle {
	private static DJni jni = null;
	static {
		jni = GameListenerThread.DJNI;
		if (jni == null) {
			jni = new DJni();
		}
		if (jni == null) {
			System.out.println("古剑奇谭：严重错误，盒子无法启动，崩溃");
			MAIN.exit();
		}
	}

	public static boolean isRunGame() {
		return jni.getWinAppIDByName("gujianol.exe") > 0;
	}

	public static void enterHZ() {
		JPanel panel = DataSave.StaticUI.getUI();
		DInputDialog dialog = new DInputDialog("提醒", "加载中。。。", false, false);
		UIModel.setUI(dialog);
		DataSave.服务器 = "古剑奇谭";
		ManagerInterface datasaveManager = (ManagerInterface) DataSave.loader.loadClassToObject("com.mugui.manager.DefaultDataSaveManager");
		if (datasaveManager == null) {
			dialog.setMessage("加载失败");
			dialog.init(true, false, false);
			dialog.start();
			UIModel.setUI(panel);
			return;
		}
		datasaveManager.init();
		System.getProperties().put("DataSaveManager", datasaveManager);
		DataSaveInterface dataSaveInterface = (DataSaveInterface) datasaveManager.get("com.mugui.GJ.ui.DataSave");
		dataSaveInterface.init();
		UIModel.setUI((JPanel) dataSaveInterface.start());
	}

	public static void closeAll() {
		ManagerInterface dataSaveInterface = ((ManagerInterface) System.getProperties().get("DataSaveManager"));
		if (dataSaveInterface != null)
			dataSaveInterface.clearAll();
	}

	public static int getGameAppid() {
		return jni.getWinAppIDByName("gujianol.exe");
	}

	public static String getGamePath() {
		int app_id = jni.getWinAppIDByName("gujianol.exe");
		int handle_id = jni.getWinAppHandleByID(app_id);
		return jni.getWinAppFilePath(handle_id);
	}
}
