package com.mugui.ui.part;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.eclipse.swt.internal.win32.OS;

import com.mugui.jni.Tool.DJni;
import com.mugui.model.CmdModel;
import com.mugui.model.DSHandle;
import com.mugui.model.FishPrice;
import com.mugui.model.HsAllModel;
import com.mugui.model.TCPModel;
import com.mugui.model.UIModel;
import com.mugui.model.FishPrice.YuAllBody;
import com.mugui.tool.FileTool;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

import net.sourceforge.tess4j.Tesseract2;

public class GameListenerThread {
	private static boolean isTrue = false;
	private static long bold_time = 0;
	public static int APP_ID = 0;
	public static DJni DJNI = null;
	private static boolean bold_bool = false;
	private static Tool tool = new Tool();

	public static void start() {
		if (isTrue)
			return;
		isTrue = true;
		Tesseract2.initData();
		if (thread == null || !thread.isAlive()) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					DJNI = new DJni();
					if (DJni.isTrue) {
						return;
					}
					while (isTrue) {
						// 判断当前窗口是不是前置窗口
						long s = OS.GetForegroundWindow();
						APP_ID = DJNI.getWinAppIDByName("BlackDesert64.exe");
						if (APP_ID <= 0) {
							APP_ID = DJNI.getWinAppIDByName("blackdesert64.exe");
						}
						if (APP_ID <= 0) {
							APP_ID = DJNI.getWinAppIDByName("blackdesert64.bin");
						} 
						if (APP_ID <= 0) {
							APP_ID = DJNI.getWinAppIDByName("BlackDesert64.bin");
						}
						if (APP_ID == 0) {
							System.out.println("0检测到游戏以关闭");
							int ci = 0;
							while (ci < 20) {
								APP_ID = DJNI.getWinAppIDByName("BlackDesert64.exe");
								if (APP_ID <= 0) {
									APP_ID = DJNI.getWinAppIDByName("blackdesert64.exe");
								}
								if (APP_ID <= 0) {
									APP_ID = DJNI.getWinAppIDByName("blackdesert64.bin");
								} 
								if (APP_ID <= 0) {
									APP_ID = DJNI.getWinAppIDByName("BlackDesert64.bin");
								}
								if (APP_ID != 0) {
									break;
								}
								Other.sleep(40);
								ci++;
							}
							if (ci < 20) {
								System.out.println("检测到游戏未关闭");
								continue;
							}
							System.out.println("确认游戏以关闭");
							if (DSHandle.isDxgj()) {
								if (DataSave.DXTX) {
									DSHandle.播放音乐();
									while (DSHandle.isDxgj() && TCPModel.bool_listener) {
										TCPModel.sendManListener();
										Other.sleep(5 * 1000);
									}
									TCPModel.sendStopManListener();
									DSHandle.关闭播放();
								} else
									CmdModel.关闭电脑();
								Other.sleep(1000);
							}
							HsAllModel.closeAll();
							DataSave.StaticUI.updateUI(com.mugui.ui.df.HsInitPanel.main);
							return;
						}
						a = getRect(DJNI, APP_ID);
						if (a != null) {
							DataSave.SCREEN_X = a[0];
							DataSave.SCREEN_Y = a[1];
							DataSave.SCREEN_WIDTH = a[2];
							DataSave.SCREEN_HEIGHT = a[3];
						}
						Other.sleep(200);
						if (a[4] != s) {
							continue;
						}
						if (DataSave.服务器.equals("私服"))
							continue;
						BufferedImage temp = DSHandle.getLineTeImg();
						Other.sleep(200);
						if (temp == null)
							continue;

						if (DataSave.D_LINE_ID < 0) {
							// 发送自身所在线路给服务器
							UIModel.sendFishLineFeature(temp);
							long time = System.currentTimeMillis();
							while (System.currentTimeMillis() - time < 30 * 1000 && DataSave.D_LINE_ID == -1) {
								tool.delay(50);
							}
						} else {
							// 更新自身所在线路
							DataSave.D_LINE_ID = FishPrice.getYuAllBodyOneKey(temp);
							if (DataSave.D_LINE_ID == -1)
								continue;
							findBold();
							// BoldShortCutPanel.start();
							// findUpdateBoss();
						}
					}
					isTrue = false;
				}

				private int old_a[] = new int[5];
				private int old_rect[] = new int[5];
				int b[] = { 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, 0 };
				private Point biankuang = null;
				private String windowstate = null;

				private int[] getRect(DJni jni, int s) {

					int[] a = jni.getWinRect(s);
					if (a == null)
						return b;
					if (a[2] <= 0 || a[3] <= 0) {
						return b;
					}

					if (old_a != null) {
						if (a[2] == old_a[2] && a[3] == old_a[3] && a[0] == old_a[0] && a[1] == old_a[1]) {
							for (int i = 0; i < a.length; i++)
								old_a[i] = a[i];
							a[0] = a[0] + (a[2] - old_rect[2]) / 2;
							a[1] = a[1] + a[3] - old_rect[3] - (a[2] - old_rect[2]) / 2;
							a[2] = old_rect[2];
							a[3] = old_rect[3];
							for (int i = 0; i < a.length; i++)
								old_rect[i] = a[i];
							return a;
						}
					} else {
						System.out.println(Arrays.toString(a));
					}
					for (int i = 0; i < a.length; i++)
						old_a[i] = a[i];
					if (windowstate != null) {
						Other.sleep(5000);
					}
					String state = getWindowState();
					if (state != null && state.equals("2")) {
						if (biankuang == null) {
							// 计算边框
							biankuang = getBianKuang(a);
							if (biankuang == null) {
								biankuang = new Point(0, 0);
							}
						}
						a[0] = a[0] + biankuang.x;
						a[1] = a[1] + biankuang.y;
						a[2] = a[2] - biankuang.x * 2;
						a[3] = a[3] - biankuang.y - biankuang.x;
						for (int i = 0; i < a.length; i++)
							old_rect[i] = a[i];
						return a;
					} else {
						for (int i = 0; i < a.length; i++)
							old_rect[i] = a[i];
						return a;
					}

				}

				private String getWindowState() {

					File f = new File(FileTool.getWindowsPath().getPath() + "/Black Desert/GameOption.txt");
					if (!f.exists()) {
						return null;
					}
					Properties properties = new Properties();
					FileInputStream fileInputStream = null;
					try {
						fileInputStream = new FileInputStream(f);
						properties.load(fileInputStream);
						windowstate = properties.getProperty("windowed");
						return windowstate;
					} catch (IOException e) {
						return null;
					} finally {
						if (fileInputStream != null) {
							try {
								fileInputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}

				private Point getBianKuang(int[] a) {
					File f = new File(FileTool.getWindowsPath().getPath() + "/Black Desert/GameOption.txt");
					if (!f.exists()) {
						return null;
					}
					Properties properties = new Properties();
					FileInputStream fileInputStream = null;
					try {
						fileInputStream = new FileInputStream(f);
						properties.load(fileInputStream);
						String width = properties.getProperty("width");
						String height = properties.getProperty("height");
						return new Point((a[2] - Integer.parseInt(width)) / 2, a[3] - Integer.parseInt(height) - (a[2] - Integer.parseInt(width)) / 2);
					} catch (IOException e) {
						return null;
					} finally {
						if (fileInputStream != null) {
							try {
								fileInputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
			thread.start();
		}
	}

	public static void stop() {
		isTrue = false;
	}

	// public static void findUpdateBoss() {// 698 95 1216 152
	// int n = 1;
	// BufferedImage temp = ImgTool.cutImage(desktop, desktop.getWidth() / 2 -
	// 262, 95, 256 + 262, 152 - 95);
	// try {
	// while (isTrue) {
	// DimgFile boos = null;
	// boos = tool.getDImg("boss" + (n++) + ".bmp");
	// Point point = tool.图中找图EX(temp, boos.bufferedImage, 0.12, 0, 0);
	// if (point != null) {
	// // 发送一个boss的刷新给服务器
	// UIModel.sendNewBossUpdateTime(boos);
	// }
	// }
	//
	// } catch (IOException e) {
	// }
	// }

	public static void findBold() {
		if (!bold_bool && System.currentTimeMillis() - bold_time > 10000) {
			bold_time = System.currentTimeMillis();
			// 每10秒检测一次是否有黄金钟
			if (isCheckBold()) {
				bold_bool = true;
				System.out.println("发现黄金钟图标");
				// 得到线路特征图。
				BufferedImage temp = DSHandle.getLineTeImg();
				if (temp == null)
					return;
				UIModel.sendBoldOne(temp);
			}
		} else if (bold_bool && System.currentTimeMillis() - bold_time > 10000) {
			bold_time = System.currentTimeMillis();
			if (!isCheckBold()) {
				bold_bool = false;
				BufferedImage temp = DSHandle.getLineTeImg();
				if (temp == null)
					return;
				UIModel.sendDelBoldone(temp);
			} else {
				if (DataSave.D_LINE_ID > -1) {
					YuAllBody yuAllBody = FishPrice.allbody.get(DataSave.D_LINE_ID);
					if (yuAllBody != null && System.currentTimeMillis() - yuAllBody.xianluBody.bold_time > 2 * 60 * 60) {
						bold_bool = true;
					}
				}
			}
		}
	}

	private static boolean isCheckBold() {
		BufferedImage temp = tool.截取屏幕(DataSave.SCREEN_WIDTH / 2 - 526, DataSave.SCREEN_Y + 7, DataSave.SCREEN_WIDTH / 2 + 365, DataSave.SCREEN_Y + 63);
		Point p = tool.图中找图(temp, "黄金钟.bmp", 0.15, 0, 0);
		if (p != null) {
			return true;
		}
		return false;

	}

	private static Thread thread = null;
	private static int a[] = new int[4];

}
