package com.mugui.jni.Tool;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.eclipse.swt.internal.win32.BITMAPINFOHEADER;

import com.mugui.model.HsAllModel;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.CJ.HS_MAP;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class DJni {
	private static DD INSTANCE = null;

	public DJni() {
		if (INSTANCE != null)
			return;
		try {
			System.load(DataSave.JARFILEPATH + "\\msvcr110d.dll");
			System.load(DataSave.JARFILEPATH + "\\msvcp110d.dll");
			System.setProperty("jna.encoding", "GBK");
			INSTANCE = (DD) Native.loadLibrary(DataSave.JARFILEPATH + "\\mydll.dll", DD.class);

			isTrue = false;
		} catch (Throwable e) {
			isTrue = true;
			e.printStackTrace();
		}
	}

	public static boolean isTrue = true;

	/**
	 * 判断这个进程是否在运行
	 * 
	 * @param win_name
	 * @return
	 */
	public boolean isWinAppRun(String win_name) {
		if (getWinAppIDByName(win_name) <= 0) {
			return false;
		}
		return true;
	}

	public boolean IsRunAsAdmin() {
		return INSTANCE.IsRunAsAdmin();
	}

	public int[] getWinRect(int app_handle) {
		Pointer pointer = INSTANCE.getWinRect(app_handle);
		if (pointer == null)
			return null;
		return pointer.getIntArray(0, 5);
	}

	/**
	 * 通过进程id得到进程句柄
	 * 
	 * @param win_name
	 * @return 进程句柄
	 */
	public int getWinAppHandleByID(int app_id) {
		return INSTANCE.GetProcessHandleByID(app_id);
	}

	/**
	 * 通过进程名得到进程id
	 * 
	 * @param win_name
	 * @return 进程id
	 */
	public int getWinAppIDByName(String win_name) {
		return INSTANCE.GetProcessIDByName(win_name);
	}

	/**
	 * 通过进程句柄得到进程自身所在路径
	 * 
	 * @param handle_id
	 * @return
	 */
	public String getWinAppFilePath(int handle_id) {
		String string = INSTANCE.GetWinAppFilePath(handle_id);
		return string.toString();
	}

	/**
	 * 得到某个窗口的顶级窗口
	 * 
	 * @param hwnd
	 * @return
	 */
	public long GetTopWindow(long hwnd) {
		return INSTANCE.DGetTopWindow(hwnd);
	}

	/**
	 * 得到窗口hwnd
	 * 
	 * @param app_id
	 * @return
	 */
	public int getWinHwnd(int app_id) {
		return INSTANCE.getWinHwnd(app_id);
	}

	public interface DD extends com.sun.jna.Library {
		public Pointer getWinRect(int app_handle);

		public int getWinHwnd(int app_handle);

		public long DGetTopWindow(long hwnd);

		public int GetProcessHandleByID(int app_id);

		public int GetProcessIDByName(String win_name);

		public boolean IsRunAsAdmin(); 

		public String GetWinAppFilePath(int handle_id);

		public long GetDesktopHwnd();

		public int createScreenCapture(byte[] buffer, int x, int y, int i, int j);

		public boolean isCorsurShow();

		public boolean ImeUnInstall(String string, String string2);

		public boolean ImeInstall(String string, String string2, String string3, String jar_path, String string4);

		public HS_MAP getHsMap(int type);

		public void ungetHsMap();

		public String OpenWindowsFileSelector(String parent);

	}

	public long GetDesktopHwnd() {

		return INSTANCE.GetDesktopHwnd();
	}

	/**
	 * 截取屏幕
	 * 
	 * @param x
	 * @param y
	 * @param i
	 * @param j
	 */
	// long hdc = OS.GetDC(0);
	// long hmdc = OS.CreateCompatibleDC(hdc);

	public synchronized BufferedImage createScreenCapture(int x, int y, int w, int h) {
		// long time = System.currentTimeMillis();
		// long hbmp = OS.CreateCompatibleBitmap(hdc, w, h);
		// long hbmpOld = OS.SelectObject(hmdc, hbmp);
		// OS.BitBlt(hmdc, 0, 0, w, h, hdc, x, y, OS.SRCCOPY);
		// hbmp = OS.SelectObject(hmdc, hbmpOld);
		//
		// long hdib = OS.GlobalAlloc(2 | 40, w * h * 3 +
		// BITMAPINFOHEADER.sizeof);
		// long lpbi = OS.GlobalLock(hdib);
		//
		byte[] buffer = new byte[((w * 24 + 31) / 32) * 4 * h + BITMAPINFOHEADER.sizeof + 32];
		// OS.GetDIBits(hmdc, hbmp, 0, h, lpbi + BITMAPINFOHEADER.sizeof,
		// buffer, 0);
		// int len = 0;
		// for (byte b : buffer) {
		// System.out.print((int) b);
		// if (len++ > 10) {
		// len %= 10;
		// System.out.println();
		// }
		// }
		// OS.OpenClipboard(0);
		// OS.SetClipboardData(2, hbmp);
		// OS.CloseClipboard();
		// OS.DeleteDC(hdc);
		// OS.DeleteDC(hmdc);
		// OS.DeleteObject(hbmp);

		int len = INSTANCE.createScreenCapture(buffer, x, y, w, h);
		try {
			BufferedImage image = ImageIO.read(ImageIO.createImageInputStream(new ByteArrayInputStream(buffer, 0, len)));
			return image;
		} catch (IOException e) {
		}
		return null;
	}

	/**
	 * 
	 * @return true 鼠标显示 false 鼠标未显示
	 */
	public boolean isCorsurShow() {
		return INSTANCE.isCorsurShow();
	}

	public boolean ImeInstall(String string, String string2, String string3, String jar_path, String string4) {
		if (INSTANCE == null)
			return false;
		return INSTANCE.ImeInstall(string, string2, string3, jar_path, string4);
	}

	public boolean ImeUnInstall(String string, String string2) {
		if (INSTANCE == null)
			return false;
		return INSTANCE.ImeUnInstall(string, string2);
	}

	public HS_MAP getHsMap() {
		if (INSTANCE == null)
			return null;
		return DataSave.hs_map = INSTANCE.getHsMap(HsAllModel.getServerid());
	}

	public void ungetHsMap() {
		if (INSTANCE == null) 
			return;
		INSTANCE.ungetHsMap();
	}

	public String OpenWindowsFileSelector(String parent) {
		String string = INSTANCE.OpenWindowsFileSelector(parent);
		return string==null?string:string.toString();
	}

}
