package com.mugui.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.mugui.tool.FileTool;
import com.mugui.ui.DataSave;

public class HsFileHandle { 

	public static boolean isRunModel() {
		File f = new File(FileTool.getWindowsPath().getPath() + "/Black Desert/GameOption.txt");
		Properties properties = new Properties();
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(f);
			properties.load(fileInputStream);
			String uiScale = properties.getProperty("uiScale");
			if (!uiScale.equals("1.00")) {
				JOptionPane.showMessageDialog(DataSave.StaticUI, "检测到游戏ui不为100%.请调整后再启动辅助");
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
