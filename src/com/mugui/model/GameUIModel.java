package com.mugui.model;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

public class GameUIModel {

	public static Point Find(Tool tool, int x1, int y1, int x2, int y2, double d, String img) {
		return tool.区域找图EX(x1, y1, x2, y2, d, img);
	}

	public static Point FindXX(Tool tool, int x1, int y1, int x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.03, "XX.bmp");
	}

	public static Point FindXX2(Tool tool, int x1, int y1, int x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.03, "XX2.bmp");
	}

	public static Point Findjgbb(Tool tool, int x1, int y1, int x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.03, "jgbb.bmp");
	}

	public static Point Findr_XX(Tool tool, int x1, int y1, int x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.03, "r_XX.bmp");
	}

	public static Point Find公共对话框(Tool tool, int x1, int y1, int x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.05, "公共对话框.bmp");
	}

	public static Point Findjgbb3(Tool tool, int x1, int y1, int x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.02, "jgbb3.bmp");
	}

	public static Point FindXX4(Tool tool, int x1, int y1, int x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.03, "new_xx.bmp");
	}

	public static Point Find料理UI(Tool tool, int x1, int y1, int x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.10, "UI_料理窗口.bmp");
	}
	public static Point Find背包UI(Tool tool, int x1, int y1, int x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.10, "UI_背包窗口.bmp");
	}
	public static Point Find仓库UI(Tool tool, int x1, int y1, int x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.10, "UI_仓库窗口.bmp");
	}
	private static Point BB_XY = null;

	public static void startFHCK(Tool tool, int size) {
		if (BB_XY == null) {
			BB_XY = GameUIModel.FindXX(tool, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 100, DataSave.SCREEN_Y + 80, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
					DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT);
			if (BB_XY != null) {
				BB_XY.x -= 403;
				BB_XY.y += 127;
			} else {
				int x1 = DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 448;
				int y1 = DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 - 224;
				BB_XY = new Point(x1, y1);
			}
		}
		System.out.println(BB_XY);
		tool.delay(1000);// 1486 360 317
		for (int i = 0; i < size; i++) {

			if (null != tool.区域找图(BB_XY.x + i * 54, BB_XY.y, BB_XY.x + i * 54 + 18 + 15, BB_XY.y + 18 + 15, 0.05, "空格子.bmp")) {
				return;
			}
			tool.mouseMovePressOne(BB_XY.x + i * 54, BB_XY.y, InputEvent.BUTTON3_MASK);
			tool.delay(1000);
			tool.keyPressOne(KeyEvent.VK_F);
			tool.delay(1000);
			tool.keyPressOne(KeyEvent.VK_ENTER);
			tool.delay(1000);
		}
	}

	public static Point Findjgbb4(Tool tool, int x1, int y1, int  x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.02, "jgbb4.bmp");
	}

	public static Point FindjgbbSafe(Tool tool, int x1, int y1, int  x2, int y2) {
		return Find(tool, x1, y1, x2, y2, 0.02, "jgbb5.bmp");
	}

}
