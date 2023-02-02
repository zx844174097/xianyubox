package com.mugui.windows;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Robot;

import com.mugui.Dui.DDialog;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class DRobot extends Robot {

	private static DD INSTANCE = null;

	public DRobot(GraphicsDevice device) throws AWTException {
		super(device); 
		Init();
	}

	public DRobot() throws AWTException {
		super();
		Init();
	}

	private void Init() {
		if (DataSave.兼容 && INSTANCE == null) {
			INSTANCE = (DD) Native.loadLibrary(DataSave.JARFILEPATH + "/mydll2.dll", DD.class);
			INSTANCE.DD_btn(0);
		}
	}

	private DDialog temp_log = null;

	@Override
	public synchronized void mouseWheel(int wheelAmt) {
		if (DataSave.兼容) {
			if (wheelAmt < 0)
				wheelAmt = 1;
			else
				wheelAmt = 2;
			INSTANCE.DD_whl(wheelAmt);

		} else {
			super.mouseWheel(wheelAmt);
			super.delay(20);
		}
	}

	@Override
	public synchronized void mouseMove(int x, int y) {
		Init();
		int n = DataSave.鼠标修正 ? 7 : 1;
		// int n=4;
		while (n-- > 0) {
			if (DataSave.后台支持) {
				if (temp_log == null) {
					temp_log = new DDialog(DataSave.StaticUI, "", false);
					temp_log.setSize(1, 1);
					temp_log.setAlwaysOnTop(true);
					temp_log.setVisible(true);
				}  
				temp_log.setLocation(x, y);
				super.mouseMove(x, y);
			} else if (DataSave.兼容) {
				if (temp_log != null) {
					temp_log.setVisible(false);
					temp_log = null;
				}
				INSTANCE.DD_mov(x, y);

			} else {
				if (temp_log != null) {
					temp_log.setVisible(false);
					temp_log = null;
				}
				super.mouseMove(x, y);

			}
			if (!DataSave.鼠标修正)
				return;
			Point point = java.awt.MouseInfo.getPointerInfo().getLocation();
			if (Math.abs(x-point.x)>3  || Math.abs(y-point.y) >3) {
				delay(40);
			} else {
				break;
			}
		}
		// if (!bool)
		// throw new NullArgumentException("鼠标无法定位到指定地点，请联系管理员");
	}

	@Override
	public synchronized void mousePress(int buttons) {
		Init();
		if (DataSave.后台支持) {
			GameBackstageTool.INSTANCE.mousePress(buttons);
		} else if (DataSave.兼容) {
			if (buttons == 16)
				buttons = 1; 
			else if (buttons == 4)
				buttons = 4;
			INSTANCE.DD_btn(buttons);
		} else
			super.mousePress(buttons);
	}

	@Override
	public synchronized void mouseRelease(int buttons) {
		Init();
		if (DataSave.后台支持) {
			GameBackstageTool.INSTANCE.mouseRelease(buttons);

		} else if (DataSave.兼容) {
			if (buttons == 16)
				buttons = 2;
			else if (buttons == 4)
				buttons = 8;
			INSTANCE.DD_btn(buttons);
		} else
			super.mouseRelease(buttons);
	}

	@Override
	public synchronized void keyPress(int keycode) {
		Init();
		if (DataSave.后台支持) {
			GameBackstageTool.INSTANCE.keyPress(keycode);
		} else if (DataSave.兼容) {
			if (keycode == 10)
				keycode = 0x0d;
			INSTANCE.DD_key(INSTANCE.DD_todc(keycode), 1);
		} else
			super.keyPress(keycode);
	}

	@Override
	public synchronized void keyRelease(int keycode) {
		Init();
		if (DataSave.后台支持) {
			GameBackstageTool.INSTANCE.keyRelease(keycode);
		} else if (DataSave.兼容) {
			if (keycode == 10)
				keycode = 0x0d;
			INSTANCE.DD_key(INSTANCE.DD_todc(keycode), 2);
		} else
			super.keyRelease(keycode);
	}

	@Override
	public synchronized void delay(int ms) {
		Init();
//		if (DataSave.兼容) {
			Other.sleep(ms);
//		} else
//			super.delay(ms);
	}

	public interface DD extends Library {

		// 64位JAVA调用DD64.dll, 32位调用DD32.dll 。与系统本身位数无关。、
		public int DD_mov(int x, int y);

		public int DD_movR(int dx, int dy);

		public int DD_btn(int btn);

		public int DD_whl(int whl);

		public int DD_key(int ddcode, int flag);

		public int DD_todc(int x);

		public int DD_str(String s);
	}

}
