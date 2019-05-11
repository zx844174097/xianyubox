package com.mugui.windows;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;

import org.eclipse.swt.internal.win32.OS;

import com.mugui.jni.Tool.DJni;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.GameListenerThread;

public class GameBackstageTool {

	public static GameBackstageTool INSTANCE = new GameBackstageTool();
	private int hwnd = 0;
	private DJni jni = GameListenerThread.DJNI;
	private int pId = 0;

	public void BindGame(String gameName) {
		if (DataSave.后台支持 == true) {
			return;
		}
		ImeInstall(gameName);

		if (GameListenerThread.APP_ID == 0) {
			pId = jni.getWinAppIDByName(gameName);
			if (pId <= 0) {
				pId = jni.getWinAppIDByName(gameName.toLowerCase());
			}
		} else {
			pId = GameListenerThread.APP_ID;
		}
		if (pId == 0) {
			return;
		}
		hwnd = jni.getWinHwnd(pId);
		BindGame(hwnd);
//		DataSave.后台支持 = true;
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (DataSave.后台支持) {
//
//					OS.PostMessageW(hwnd, OS.WM_ACTIVATE, 1, 0);
//					Other.sleep(40);
//					OS.PostMessageW(hwnd, OS.WM_SETFOCUS, 0, 0);
//					Other.sleep(40); 
//				}
//			} 
//		}).start();

	}

	private void ImeInstall(String gameName) {
		if (jni == null) {
			jni = new DJni();
		}
		if (!jni.IsRunAsAdmin()) {
			JOptionPane.showMessageDialog(DataSave.StaticUI, "软件未获取到超级权限，导致设置失败。。", "严重警告", JOptionPane.OK_OPTION);
		}
		File temp = new File("c:\\1.mugui");
		if (temp.isFile()) {
			temp.delete();
		}
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp), "GBK"));
			writer.write(DataSave.JARFILEPATH);
			writer.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		File father = new File(System.getenv("ComSpec")).getParentFile();
		File f = new File(father.getPath() + "/" + "SougouPYime.ime");
		if (f.isFile()) {
			f.delete();
		}
		FileInits("/com/mugui/ui/data/Freeime1.dll", f);
		String ime = f.getPath();
		father = new File(System.getProperty("java.io.tmpdir") + "/ImeClass/");
		if (!father.isDirectory()) {
			father.mkdirs();
		}
		f = new File(father.getPath() + "/" + "SougouPYime.dll");
		FileInits("/com/mugui/ui/data/Freeime2.dll", f);
		jni.ImeInstall(ime, "微软官方输入法V1.0", f.getPath(), DataSave.JARFILEPATH, gameName);

	}

	private void FileInits(String string, File f) {
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		inputStream = GameBackstageTool.class.getResourceAsStream(string);
		if (inputStream == null) {
			JOptionPane.showMessageDialog(DataSave.StaticUI, "软件内部发生严重异常", "严重警告", JOptionPane.OK_OPTION);
			return;
		}
		try {
			if (f.isFile()) {
				f.delete();
			}
			if (!f.isFile()) {
				outputStream = new FileOutputStream(f);
				byte b[] = new byte[1024];
				int i = 0;
				while ((i = inputStream.read(b)) != -1) {
					outputStream.write(b, 0, i);
				}
				outputStream.close();
				inputStream.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void ImeUnInstall() {
		File father = new File(System.getenv("ComSpec")).getParentFile();
		File f = new File(father.getPath() + "/SougouPYime.ime");
		// System.out.println(f.getPath());
		if (jni == null) {
			jni = new DJni();
		}
		jni.ImeUnInstall("微软官方输入法V1.0", f.getPath());
		if (f.isFile()) {
			f.delete();
		}
		jni = null;
	}

	public void BindGame(int hwnd) {
		this.hwnd = hwnd;
	}

	public void unBindGame() {
		DataSave.后台支持 = false;
		hwnd = 0;
		ImeUnInstall();
	}

	public boolean isBindGame() {
		return hwnd <= 0 ? false : true;
	}

	public void mouseRelease(int buttons) {
		if (!isBindGame()) {
			return;
		}
		switch (buttons) {
		case 16:
			OS.PostMessageW(hwnd, OS.WM_LBUTTONUP, 0, 0);
			break;
		case 4:
			OS.PostMessageW(hwnd, OS.WM_RBUTTONUP, 0, 0);
			break;
		}
	}

	public void mousePress(int buttons) {
		if (!isBindGame()) {
			return;
		}
		switch (buttons) {
		case 16:
			OS.PostMessageW(hwnd, OS.WM_LBUTTONDOWN, 0, 0);
			break;
		case 4:
			OS.PostMessageW(hwnd, OS.WM_RBUTTONDOWN, 0, 0);
			break;

		}
	}

	// s 31 65536
	// ctrl 29
	// a 30
	// d 32
	// enter 28
	// esc 1
	// 2 1
	// 3 2
	// 4 3 到
	// 11 0
	// 15 tab
	// 16 q
	// 17 w
	// 18 E
	// 19 R
	// 20 T
	// 25 p 个人信息
	// 24 任务
	// 34 公会
	// 35 36 无用界面
	// 37 技能
	// 38 加工
	// 39未知
	// 41 ~
	// 44 z
	// 45 x
	// 46 c
	// 47 v
	// 48 b
	// 49 n
	// 50 m
	// 51<
	// 52>
	// 53/
	// 54 shift
	// 57 空格
	// 59 f1
	// 60 f2 f3 f4 61 62
	public void keyPress(int keycode) {
		if (!isBindGame()) {
			return;
		}
		int key = getKeyCode(keycode);
		OS.PostMessageW(hwnd, OS.WM_KEYDOWN, 0, key * 65536);
	}

	public void keyRelease(int keycode) {
		if (!isBindGame()) {
			return;
		}
		int key = getKeyCode(keycode);
		OS.PostMessageW(hwnd, OS.WM_KEYUP, 0, key * 65536);
	}

	private int getKeyCode(int keycode) {
		int key = 0;
		switch (keycode) {
		// enter
		case 10:
			key = 41;
			break;
		// esc 27
		case 27:
			key = 1;
			break;
		// space 32
		case 32:
			key = 57;
			break;
		// ctrl 17
		case 17:
			key = 29;
			break;
		// A 65
		case 65:
			key = 30;
			break;
		// B 66
		case 66:
			key = 48;
			break;
		// C 67
		case 67:
			key = 46;
			break;
		// D 68
		case 68:
			key = 32;
			break;
		// E 69
		case 69:
			key = 18;
			break;
		// F 70
		case 70:
			key = 33;
			break;
		// G 71
		case 71:
			key = 34;
			break;
		// H 72
		case 72:
			key = 35;
			break;
		// I 73
		case 73:
			key = 23;
			break;
		// J 74
		case 74:
			key = 36;
			break;
		// K 75
		case 75:
			key = 37;
			break;
		// L 76
		case 76:
			key = 38;
			break;
		// M 77
		case 77:
			key = 50;
			break;
		// N 78
		case 78:
			key = 49;
			break;
		// O 79
		case 79:
			key = 24;
			break;
		// P 80
		case 80:
			key = 25;
			break;
		// Q 81
		case 81:
			key = 18;
			break;
		// R 82
		case 82:
			key = 19;
			break;
		// S 83
		case 83:
			key = 31;
			break;
		// T 84
		case 84:
			key = 20;
			break;
		// U 85
		case 85:
			key = 22;
			break;
		// V 86
		case 86:
			key = 47;
			break;
		// W 87
		case 87:
			key = 17;
			break;
		// X 88
		case 88:
			key = 45;
			break;
		// Y 89
		case 89:
			key = 21;
			break;
		// Z 90
		case 90:
			key = 44;
			break;
		// 0 48
		case 48:
			key = 11;
			break;
		// 1 49
		case 49:
			key = 2;
			break;
		// 2 50
		case 50:
			key = 3;
			break;
		// 3 51
		case 51:
			key = 4;
			break;
		// 4 52
		case 52:
			key = 5;
			break;
		// 5 53
		case 53:
			key = 6;
			break;
		// 6 54
		case 54:
			key = 7;
			break;
		// 7 55
		case 55:
			key = 8;
			break;
		// 8 56
		case 56:
			key = 9;
			break;
		// 9 57
		case 58:
			key = 10;
			break;
		// F1 112
		case 112:
			key = 59;
			break;
		// F2 113
		case 113:
			key = 60;
			break;
		// F3 114
		case 114:
			key = 61;
			break;
		// F4 115
		case 115:
			key = 62;
			break;
		// F5 116
		case 116:
			key = 63;
			break;
		// F6 117
		case 117:
			key = 64;
			break;
		// F7 118
		case 118:
			key = 65;
			break;
		// F8 119
		case 119:
			key = 66;
			break;
		// F9 120
		case 120:
			key = 67;
			break;
		// F10 121
		// F11 122
		case 121:
			key = 68;
			break;
		// F12 123
		case 122:
			key = 69;
			break;
		case 123:
			key = 70;
			break;

		}
		return key;
	}

	public BufferedImage getGameImage(int x, int y, int x2, int y2) {
		if (!isBindGame()) {
			return null;
		}
		return null;

	}

}
