package com.mugui.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.InputEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.mugui.Dui.DDialog;
import com.mugui.jni.Tool.DJni;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.CJ.HS_MAP;
import com.mugui.ui.part.EWtoDirectionCheckBox;
import com.mugui.ui.part.GameListenerThread;
import com.mugui.windows.Tool;
import com.sun.awt.AWTUtilities;
import com.sun.glass.events.KeyEvent;
import com.sun.javafx.geom.Point2D;

public class EWHandle {

	private static boolean isTrue = false;
	public static final String EW_KEEP_DIRECTION = "EW_KEEP_DIRECTION";

	public static void start(String string) {
		isTrue = true;
		switch (string) {
		case EW_KEEP_DIRECTION:
			keepDirection();
			break;

		default:
			break;
		}
	}

	private static Thread keepDirection = null;
	private static Tool shoutool = null;
	private static Thread TDirection = null;

	private static void keepDirection() {
		if (keepDirection == null || !keepDirection.isAlive()) {
			keepDirection = new Thread(new Runnable() {
				private DJni jni = null;

				@Override
				public void run() {
					if (jni == null) {
						jni = GameListenerThread.DJNI;
					}
					if (jni == null) {
						JOptionPane.showMessageDialog(DataSave.StaticUI, "初始化JNA错误，无法使用");
						return;
					}
					// 计算当前点与预计到达的点之间的距离
					DDialog dialog = null;
					DDialog dialog2 = null;
					Point2D datum_point = new Point2D(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1753), DataSave.SCREEN_Y + 160);

					while (isTrue) {
						HS_MAP map = jni.getHsMap();
						Point2D p1 = new Point2D(map.x, map.y);
						EWtoDirectionCheckBox checkBox = DataSave.ew.getDirectionCheckBoxCoordinate();
						Point2D p2 = new Point2D(checkBox.getCoordinate().x, checkBox.getCoordinate().y);
						float jiaodu = mathDirection(p1, p2);
						int juli = (int) countLangth(p1, p2);
						if (dialog == null) {
							dialog = new DDialog(DataSave.StaticUI, "", false);
							dialog.setSize(10, 10);
							dialog.setBackground(Color.RED);
							dialog.setForeground(Color.red);
							dialog.getContentPane().setBackground(Color.RED);
							dialog.setVisible(true);
							dialog.setAlwaysOnTop(true);
							dialog2 = new DDialog(DataSave.StaticUI, "", false);
							dialog2.setSize(250, 50);
							dialog2.setLocation(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1692), DataSave.SCREEN_Y + 64);
							dialog2.setForeground(null);
							dialog2.setBackground(null);
							dialog2.setUndecorated(true);
							AWTUtilities.setWindowOpaque(dialog2, false);
							dialog2.getContentPane().setBackground(null);
							JLabel bJLabel = new JLabel("距离:");
							bJLabel.setBackground(null);
							bJLabel.setOpaque(false);
							bJLabel.setFont(new Font("黑体", Font.BOLD, 16));
							bJLabel.setForeground(Color.RED);
							bJLabel.setHorizontalTextPosition(SwingConstants.CENTER);
							JLabel bJLabel1 = new JLabel("");
							bJLabel1.setBackground(null);
							bJLabel1.setOpaque(false);
							bJLabel1.setFont(new Font("黑体", Font.BOLD, 14));
							bJLabel1.setForeground(Color.RED);
							bJLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
							dialog2.setLayout(new BorderLayout(0, 0));
							dialog2.getContentPane().add(bJLabel, BorderLayout.CENTER);
							dialog2.getContentPane().add(bJLabel1, BorderLayout.NORTH);
							dialog2.setAlwaysOnTop(true);
							dialog2.setVisible(true);
						}
						int x = (int) (-Math.sin(jiaodu) * 50 + datum_point.x);
						int y = (int) (Math.cos(jiaodu) * 50 + datum_point.y);
						dialog.setLocation(x - 5, y - 5);
						((JLabel) dialog2.getContentPane().getComponents()[0]).setText("距离:" + juli);
						((JLabel) dialog2.getContentPane().getComponents()[1]).setText(checkBox.getText());
						Other.sleep(500);
						if (DataSave.ew.isDirectionT()&&isTrue ) {
							DirectionT();
						}
					}
					if (dialog != null) {
						dialog.setVisible(false);
						dialog.dispose();
						dialog = null;
					}
					if (dialog2 != null) {
						dialog2.setVisible(false);
						dialog2.dispose();
						dialog2 = null;
					}

				}

			});
			keepDirection.start();
		}
	}

	private static double countLangth(Point2D p1, Point2D p2) {
		return Math.pow(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2), 0.5);

	}

	private static void DirectionT() {
		if (TDirection == null || !TDirection.isAlive()) {
			TDirection = new Thread(new Runnable() {

				@Override
				public void run() {
					if (GameListenerThread.DJNI == null)
						return;
					DJni jni = GameListenerThread.DJNI;
					if (shoutool == null) {
						shoutool = new Tool();
					}
					shoutool.delay(500);
					shoutool.mouseMovePressOne(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 50, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y,
							InputEvent.BUTTON1_MASK);
					shoutool.delay(3000);
 
					HS_MAP map = jni.getHsMap();
					Point2D p1 = new Point2D(map.x, map.y);
					EWtoDirectionCheckBox checkBox = DataSave.ew.getDirectionCheckBoxCoordinate();
					Point2D p2 = new Point2D(checkBox.getCoordinate().x, checkBox.getCoordinate().y);
					float jiaodu = mathDirection(p1, p2);
					shoutool.lockDirection(jiaodu);
					shoutool.keyPress(KeyEvent.VK_W);
					shoutool.delay(500);
					shoutool.keyPress(KeyEvent.VK_SHIFT);
					while (DataSave.ew.isDirectionT() && isTrue) {
						map = jni.getHsMap();
						p1 = new Point2D(map.x, map.y);
						checkBox = DataSave.ew.getDirectionCheckBoxCoordinate();
						p2 = new Point2D(checkBox.getCoordinate().x, checkBox.getCoordinate().y);
						jiaodu = mathDirection(p1, p2);
						shoutool.lockDirection(jiaodu);
						if (countLangth(p1, p2) < 1000) {
							break;
						}
						shoutool.delay(100);
					}
					shoutool.keyRelease(KeyEvent.VK_SHIFT);
					shoutool.keyRelease(KeyEvent.VK_W);
					shoutool.delay(500);
					shoutool.keyPressOne(KeyEvent.VK_S);
					while (DataSave.ew.isDirectionT() && isTrue && DataSave.监视声音) {
						DSHandle.播放音乐();
						Other.sleep(1000);
					}
					DSHandle.关闭播放();
					DataSave.ew.setDirectionT(false);
				}
			});
			TDirection.start();
		}
	}

	// 方向计算
	private static float mathDirection(Point2D p2, Point2D p1) {
		return (float) StrictMath.atan2((p2.x - p1.x), (p2.y - p1.y));
	}

	public static void stop() {
		isTrue = false;
	}
}
