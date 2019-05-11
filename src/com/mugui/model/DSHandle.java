package com.mugui.model;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import org.eclipse.swt.internal.win32.OS;

import net.sourceforge.tess4j.Tesseract2;

import com.mugui.MAIN;
import com.mugui.Dui.DimgFile;
import com.mugui.MP3.decoder.JavaLayerException;
import com.mugui.MP3.player.Player;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.tool.ImgTool;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.DS.HPMPBean;
import com.mugui.ui.part.DSThreadPanel;
import com.mugui.ui.part.GameListenerThread;
import com.mugui.windows.Clipboard;
import com.mugui.windows.Tool;

public class DSHandle {
	private static boolean isTrue = false;
	private static keyThread1 ajthread = null;
	// private static keyThread2 zouthread = null;
	private static String mode = null;
	private static Thread dxgjThread = null;

	public static void start(String string) {
		if (isTrue == true)
			return;
		if (!HsFileHandle.isRunModel()) {
			return;
		}
		isTrue = true;
		startDxgj();
		new Thread(new Runnable() {
			@Override
			public void run() {
				long time = 0;
				while (isTrue) {
					if (System.currentTimeMillis() - time > 60 * 1000) {
						TcpBag bag = new TcpBag();
						bag.setBag_id(TcpBag.ERROR);
						UserBean userBean = new UserBean();
						userBean.setCode("ds");
						bag.setBody(userBean.toJsonObject());
						TCPModel.SendTcpBag(bag);
						time = System.currentTimeMillis();
					} else {
						Other.sleep(1000);
					}

				}
			}
		}).start();
		if (string == null) {
			string = "默认";
		}
		mode = string;
		if (string.equals("默认")) {
			ajthread = new keyThread1();
			// zouthread = new keyThread2();
			ajthread.start();
			// zouthread.start();
			// System.out.println("默认定时器开启");
		} else if (string.equals("卡负重")) {
			卡负重();
		} else if (string.equals("自动上架")) {
			自动上架();
		} else if (string.equals("练马")) {
			ajthread = new keyThread1();
			练马();
			ajthread.start();
		} else if (string.equals("自动躺床")) {
			自动躺床();
		} else if (string.equals("扫马")) {
			扫马();
		} else if (string.equals("挤牛奶")) {
			挤牛奶();
		} else if (string.equals("收邮箱钱")) {
			收邮箱钱();
		} else if (string.equals("采集水")) {
			采集水();
		} else if (string.equals("公会内胆出售")) {
			公会内胆出售();
		} else if (string.equals("自动练马技能")) {
			自动练马技能();
		}
	}

	private static Thread 自动练马技能 = null;

	private static void 自动练马技能() {
		if (自动练马技能 != null && 自动练马技能.isAlive()) {
			return;
		}
		自动练马技能 = new Thread(new Runnable() {

			// 1057 344 1084 369
			// while (isTrue) {
			// // 1233 310
			// // 838 499
			// String color = shouTool.得到点颜色(board_xy.x - (1233 - 838),
			// board_xy.y + 499 - 310);
			// if (!color.equals("41A784")) {
			// 播放音乐();
			// shouTool.delay(5000);
			// } else {
			// 关闭播放();
			// }
			// shouTool.delay(20);
			// }

			@Override
			public void run() {
				shouTool.delay(1500);
				Point board_xy = null;
				long time = System.currentTimeMillis();
				while (isTrue && null == (board_xy = GameUIModel.FindXX(shouTool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 100,
						DataSave.SCREEN_HEIGHT / 2 - 500 + DataSave.SCREEN_Y, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 500,
						DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y - 100))) {
					shouTool.delay(1000);
					if (System.currentTimeMillis() - time > 3000) {
						break;
					}
				}
				if (board_xy == null) {
					JOptionPane.showMessageDialog(DataSave.StaticUI, "搭乘物窗口未发现", "警告", JOptionPane.OK_OPTION);
					return;
				}
				BufferedImage image = shouTool.截取屏幕(board_xy.x + (1057 - 1233), board_xy.y + (344 - 310), board_xy.x + (1084 - 1233), board_xy.y + (369 - 310));
				image = shouTool.得到图的特征图(image, 0.25, "2FC0F7");
				if (image == null) {
					JOptionPane.showMessageDialog(DataSave.StaticUI, "特征图截取失败", "警告", JOptionPane.OK_OPTION);
					return;
				}
				while (isTrue) {
					Point temp = shouTool.区域找图EX(board_xy.x + (1000 - 1233), board_xy.y + (330 - 310), board_xy.x + (1100 - 1233), board_xy.y + (380 - 310),
							0.07, image);
					if (temp == null) {
						播放音乐();
					} else {
						关闭播放();
					}
					shouTool.delay(1500);
				}

			}
		});
		自动练马技能.start();

	}

	private static Thread 公会内胆出售 = null;

	private static void 公会内胆出售() {
		if (公会内胆出售 != null && 公会内胆出售.isAlive()) {
			return;
		}
		公会内胆出售 = new Thread(new Runnable() {

			@Override
			public void run() {
				shouTool.delay(2000);
				while (isTrue) {
					shouTool.keyPressOne(KeyEvent.VK_G);
					shouTool.delay(500);
					Point point = null;
					while ((point = shouTool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
							DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, 0.15, "公会仓库.bmp")) == null && isTrue)
						;
					if (!isTrue)
						return;
					shouTool.mouseMovePressOne(point.x + 5, point.y + 5, InputEvent.BUTTON1_MASK);
					shouTool.delay(500);
					while ((point = shouTool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
							DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, 0.15, "公会内胆.bmp")) == null && isTrue)
						;

					if (!isTrue)
						return;
					long time = System.currentTimeMillis();
					shouTool.delay(200);
					do {
						shouTool.mouseMovePressOne(point.x + 10, point.y + 2, InputEvent.BUTTON3_MASK);
						shouTool.delay(200);
						shouTool.mouseMove(point.x - 300, point.y);
						long time2 = System.currentTimeMillis();
						boolean bool = true;
						while (isTrue && GameUIModel.FindXX2(shouTool, point.x - 100, point.y, point.x + 250, point.y + 60) == null) {
							if (System.currentTimeMillis() - time2 > 700) {
								bool = false;
								break;
							}
						}
						if (bool) {
							break;
						}
					} while (System.currentTimeMillis() - time < 5 * 1000 && isTrue);
					shouTool.delay(100);

					if (!isTrue)
						return;
					byte[] b = (DataSave.ds.getGhLiner_number() + "").getBytes(Charset.forName("UTF-8"));
					for (int i = 0; i < b.length; i++) {
						shouTool.keyPressOne(Tool.getkeyCode("" + ((char) b[i])));
						shouTool.delay(100);
					}
					if (!isTrue)
						return;
					shouTool.keyPressOne(KeyEvent.VK_ENTER);
					shouTool.delay(200);
					shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
					shouTool.delay(700);
					shouTool.keyPressOne(KeyEvent.VK_R);
					if (!isTrue)
						return;
					Point p = null;
					time = System.currentTimeMillis();
					System.out.println("查找公会商店图标");
					while ((p = shouTool.区域找图EX(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 330, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 250,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 100, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 206, 0.08, "仓库.bmp")) == null
							&& isTrue) {
						Other.sleep(10);
						if (System.currentTimeMillis() - time > 5 * 1000) {
							shouTool.保存截屏(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 330, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 250,
									DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 100, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 206, "仓库.bmp");

							System.out.println("未查找到公会商店图标");
							return;
						}
					}
					if (!isTrue)
						return;
					shouTool.delay(1000);
					shouTool.mouseMovePressOne(p.x + 50, p.y + 5, InputEvent.BUTTON1_MASK);

					if (!isTrue)
						return;

					while ((point = GameUIModel.FindXX(shouTool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 400, DataSave.SCREEN_Y,
							DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y)) == null && isTrue) {

					}

					// while ((point = shouTool.区域找图(DataSave.SCREEN_X,
					// DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH +
					// DataSave.SCREEN_X,
					// DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, 0.15,
					// "公会内胆.bmp")) == null && isTrue)
					// ;
					// if (!isTrue)
					// return;

					shouTool.mouseMovePressOne(point.x - 392, point.y + 139, InputEvent.BUTTON3_MASK);
					shouTool.delay(500);
					shouTool.keyPressOne(KeyEvent.VK_F);
					shouTool.delay(200);
					shouTool.keyPressOne(KeyEvent.VK_ENTER);
					if (!isTrue)
						return;
					shouTool.delay(500);
					shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
					shouTool.delay(500);
					shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
					shouTool.delay(500);
					if (!isTrue)
						return;

				}
			}
		});
		公会内胆出售.start();
	}

	private static Thread 采集水 = null;

	private static void 采集水() {
		if (采集水 != null && 采集水.isAlive()) {
			return;
		}
		采集水 = new Thread(new Runnable() {

			@Override
			public void run() {
				shouTool.delay(2000);
				while (isTrue) {
					shouTool.keyPressOne(Tool.getkeyCode(DataSave.ds.getCj_shui()));

					shouTool.delay(2500);
					while (isTrue && GameUIModel.FindXX(shouTool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - (1920 - 1880),
							DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y - (540 - 190), DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - (1920 - 1898),
							DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y - (540 - 207)) == null) {
						shouTool.keyPressOne(KeyEvent.VK_F6);
						shouTool.delay(2500);
					}
					if (!isTrue) {
						return;
					}
					shouTool.delay(200);
					shouTool.mouseMovePressOne(DataSave.SCREEN_X + 991, DataSave.SCREEN_Y + 558, InputEvent.BUTTON3_MASK);
					shouTool.delay(500);
					shouTool.delay(500);
					shouTool.keyPressOne(KeyEvent.VK_F);
					shouTool.delay(500);
					shouTool.keyPressOne(KeyEvent.VK_ENTER);
					// 1530 349
					shouTool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1483),
							DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 - (1080 / 2 - 350), InputEvent.BUTTON3_MASK);
					shouTool.delay(500);
					shouTool.keyPressOne(KeyEvent.VK_F);
					shouTool.delay(500);
					shouTool.keyPressOne(KeyEvent.VK_ENTER);
					shouTool.delay(500);
					shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
					shouTool.delay(500);
				}
			}
		});
		采集水.start();
	}

	private static Thread 收邮箱钱 = null;

	private static void 收邮箱钱() {

		if (收邮箱钱 != null && 收邮箱钱.isAlive()) {
			return;
		}
		收邮箱钱 = new Thread(new Runnable() {
			@Override
			public void run() {
				shouTool.delay(1000);
				Point mail_point = GameUIModel.FindXX(shouTool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 300, DataSave.SCREEN_Y,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
				if (mail_point == null) {
					JOptionPane.showMessageDialog(DataSave.StaticUI, "未发现邮箱", "警告", JOptionPane.OK_OPTION);
					return;
				}
				int max = 8;
				int n = 0;
				while (isTrue) {
					// 1723 230 1699 308 1739 832
					Point point = shouTool.区域找图EX(mail_point.x - 24, mail_point.y + (319 - 230) + n * 64, mail_point.x + 16,
							mail_point.y + (369 - 230) + n * 64, 0.12, "礼物.bmp");

					if (point != null) {
						shouTool.mouseMovePressOne(point.x - 50, point.y, InputEvent.BUTTON1_MASK);
						shouTool.delay(700);
						// 860 810 915 864
						point = shouTool.区域找图EX(mail_point.x - (1723 - 860), mail_point.y + (810 - 230), mail_point.x - (1723 - 915),
								mail_point.y + (864 - 230), 0.07, "礼物2.bmp");
						if (point != null) {// 1268,795,1
							shouTool.mouseMovePressOne(point.x + 5, point.y + 5, InputEvent.BUTTON1_MASK);
							shouTool.delay(200);
							// 900 767 880 828
							shouTool.mouseMovePressOne(point.x + (900 - 880), point.y - 828 + 767, InputEvent.BUTTON3_MASK);
							shouTool.delay(200);
							shouTool.keyPressOne(KeyEvent.VK_F);
							shouTool.delay(200);
							shouTool.keyPressOne(KeyEvent.VK_ENTER);
							shouTool.delay(500);
						}

					}
					n++;
					if (n == 8) {
						n = 0;// 1593 284
						shouTool.mouseMovePressOne(mail_point.x + (1593 - 1723), mail_point.y + (284 - 230), InputEvent.BUTTON1_MASK);
						shouTool.delay(500);
					}
				}
			}
		});
		收邮箱钱.start();

	}

	private static Thread 挤牛奶 = null;

	private static void 挤牛奶() {
		if (挤牛奶 != null && 挤牛奶.isAlive()) {
			return;
		}
		挤牛奶 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isTrue) {
					// 等待挤奶
					while (isTrue) {
						// 1164,173,1206,213
						Point point = shouTool.区域找色(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X + 204, DataSave.SCREEN_Y + 173,
								DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X + 246, DataSave.SCREEN_Y + 213, 0.25, 100, "006E95");
						if (point != null)
							break;
						;
						shouTool.delay(50);
					}
					if (!isTrue)
						return;
					// 开始挤奶
					while (isTrue) {
						Point point = shouTool.区域找色(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X + 204, DataSave.SCREEN_Y + 173,
								DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X + 246, DataSave.SCREEN_Y + 213, 0.25, 100, "006E95");
						if (point == null)
							break;
						shouTool.mousePressOne(InputEvent.BUTTON1_MASK, 300);
						point = shouTool.区域找色(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X + 204, DataSave.SCREEN_Y + 173,
								DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X + 246, DataSave.SCREEN_Y + 213, 0.25, 100, "006E95");
						if (point == null)
							break;
						shouTool.mousePressOne(InputEvent.BUTTON3_MASK, 300);
						shouTool.delay(500);
					}
				}
			}
		});
		挤牛奶.start();
	}

	private static boolean dsgj_bool = false;

	public static void stopDxgj() {
		dsgj_bool = false;
	}

	public static boolean isDxgj() {
		return dsgj_bool;
	}

	public static void startDxgj() {
		if (DataSave.DXGJ && (dxgjThread == null || !dxgjThread.isAlive())) {
			dsgj_bool = true;
			dxgjThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (DataSave.DXGJ && dsgj_bool) {
						Shutdown();
						if (!dsgj_bool) {
							break;
						}
						Other.sleep(5 * 1000);
					}
					if (TCPModel.bool_listener)
						TCPModel.sendStopManListener();
					DSHandle.关闭播放();
					dsgj_bool = false;
				}

				long dxgj_time = 0;

				private void Shutdown() {
					// 21,1028,162,1045
					if (!DataSave.DXGJ || !dsgj_bool) {
						return;
					}
					if (System.currentTimeMillis() - dxgj_time < 60 * 1000) {
						return;
					}
					dxgj_time = System.currentTimeMillis();// 11,1026,163,1048
					if (shouTool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 100, DataSave.SCREEN_X + 200,
							DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 0, 0.10, "DXGJ.bmp") != null) {
						if (DataSave.DXTX && TCPModel.bool_listener) {
							DSHandle.播放音乐();
							TCPModel.sendManListener();
						} else
							CmdModel.关闭电脑();
					}

				}
			});
			dxgjThread.start();
		}
	}

	private static Thread 扫马 = null;

	static int nuuu = 0;

	private static void 扫马() {
		if (扫马 != null && 扫马.isAlive())
			return;
		扫马 = new Thread(new Runnable() {
			Point ben = null;
			Tesseract2 instance = new Tesseract2();

			@Override
			public void run() {
				Other.sleep(1000);
				while (isTrue && (ben = GameUIModel.FindXX(shouTool, DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
						DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y)) == null) {
					Other.sleep(500);
					System.out.println("未发现坐标");
				}
				while (isTrue) {
					if (DataSave.游戏锁定) {
						if (hwnd == 0 && GameListenerThread.APP_ID != 0) {
							hwnd = GameListenerThread.DJNI.getWinHwnd(GameListenerThread.APP_ID);
						}
						if (hwnd != 0) {
							long hForeWnd = OS.GetForegroundWindow();
							if (hForeWnd != hwnd) {
								int dwForeID = OS.GetWindowThreadProcessId(hForeWnd, null);
								int dwCurID = OS.GetCurrentThreadId();
								OS.AttachThreadInput(dwCurID, dwForeID, true);
								OS.ShowWindow(hwnd, 1);
								OS.SetWindowPos(hwnd, OS.HWND_TOPMOST, 0, 0, 0, 0, OS.SWP_NOSIZE | OS.SWP_NOMOVE);
								OS.SetWindowPos(hwnd, OS.HWND_NOTOPMOST, 0, 0, 0, 0, OS.SWP_NOSIZE | OS.SWP_NOMOVE);
								OS.SetForegroundWindow(hwnd);
								OS.AttachThreadInput(dwCurID, dwForeID, false);
								System.out.println("游戏切换至前台");
							}
						}
					}
					String zhu = "";
					if (!(zhu = 价格判断()).equals("")) {
						char c[] = zhu.toCharArray();
						for (int i = 0; i < c.length; i++) {
							if (!isTrue)
								return;
							if (技能判断(c[i] - '0')) {
								if (!isTrue)
									return;
								购买(c[i] - '0');
							}
						}
					} else {
						if (!bb)
							bool = !bool;
						// 1273,243
						// shouTool.mouseMovePressOne(ben.x - 67, ben.y + 44,
						// InputEvent.BUTTON1_MASK);
					}
					if (!now_money.equals("") && now_money.equals(last_money)) {
						bool = !bool;
					}
					last_money = now_money;
					if (!isTrue)
						return;
					// System.out.println("翻页");
					翻页();
					if (!isTrue)
						return;
					// shouTool.delay(500);
				}
			}

			// 1340,199
			// l 919 805 r 998 805
			private boolean bool = true;
			private int time = 0;

			private void 翻页() {
				if (System.currentTimeMillis() - time > 3000) {
					// if (!bool) {
					// shouTool.mouseMovePressOne(ben.x - 421, ben.y + 606,
					// InputEvent.BUTTON1_MASK);
					// } else {
					// shouTool.mouseMovePressOne(ben.x - 342, ben.y + 606,
					// InputEvent.BUTTON1_MASK);
					// }
					shouTool.delay(200);
					shouTool.mouseMovePressOne(ben.x - 74, ben.y + 41, InputEvent.BUTTON1_MASK);
					shouTool.delay(200);
				}
			}

			// 高127 1270 356
			private void 购买(int i) {
				shouTool.mouseMovePressOne(ben.x - 37, ben.y + 129 + i * 127, InputEvent.BUTTON1_MASK);
				shouTool.delay(400);
				shouTool.keyPressOne(KeyEvent.VK_ENTER);
			}

			// 1340,199
			private boolean 技能判断(int i) {
				// 893 276 1232 341
				boolean dp = false;
				boolean sp = false;
				boolean sq = false;
				// 1240,312,1256,327
				BufferedImage te = shouTool.得到特征点集合图(ben.x - 100, ben.y + 113 + i * 127, ben.x - 84, ben.y + 128 + i * 127, 0.07, "C7C7C7");
				// shouTool.保存图片(te, "lin6.bmp");
				while (isTrue) {
					BufferedImage image = shouTool.截取屏幕(ben.x - 447, ben.y + 77 + i * 127, ben.x - 108, ben.y + 142 + i * 127);
					if (!dp && DataSave.ds.isSm_dp()) {
						if (shouTool.图中找图(image, "瞬间加速.bmp", 0.12, 0, 0) != null)
							dp = true;
					} else
						dp = true;
					if (!sp && DataSave.ds.isSm_sp()) {
						if (shouTool.图中找图(image, "被动瞬间加速.bmp", 0.12, 0, 0) != null)
							sp = true;
					} else
						sp = true;
					if (!sq && DataSave.ds.isSm_sq()) {
						if (shouTool.图中找图(image, "双人乘骑.bmp", 0.12, 0, 0) != null)
							sq = true;
					} else
						sq = true;
					if (dp && sp && sq) {
						return true;
					}
					if (te == null)
						return false;
					// 翻下一个技能页 1248 338
					shouTool.mouseMovePressOne(ben.x - 92, ben.y + 139 + i * 127, InputEvent.BUTTON1_MASK);
					shouTool.delay(200);
					if (shouTool.区域找图EX(ben.x - 100, ben.y + 113 + i * 127, ben.x - 84, ben.y + 128 + i * 127, 0.05, te) != null) {
						return false;
					}
					te = shouTool.得到特征点集合图(ben.x - 100, ben.y + 113 + i * 127, ben.x - 84, ben.y + 128 + i * 127, 0.07, "C7C7C7");
				}
				return false;
			}

			private String last_money = "";
			private String now_money = "";
			private boolean bb = false;

			// 1340,199
			private String 价格判断() {
				bb = false;
				// 第一个价格坐标
				Rectangle money = new Rectangle(ben.x - 183, ben.y + 169, ben.x - 183 + 98, ben.y + 169 + 17);
				String zhu = "";
				BufferedImage image = null;
				int n = 0;
				now_money = "";
				while (isTrue && n < 4) {
					if (!特种马判断(ben.x - 783, ben.y + 87 + n * 127, ben.x - 783 + 115, ben.y + 87 + n * 127 + 100)) {
						n++;
						continue;
					}
					image = shouTool.截取屏幕(money.x + 4, money.y + n * 127, money.width, money.height + n * 127);
					image = ImgTool.grayscaleImage(image);
					// shouTool.保存图片(image, nuuu++ + "_" + "ss.bmp");
					image = ImgTool.binaryzationImage(image);
					if (image == null) {
						n++;
						continue;
					}
					image = ImgTool.clearAdhesion(image);
					image = ImgTool.imageEnlarge(image, 6);
					int i;// JNA
					String result = instance.doOCR(image);
					now_money += result;
					char[] c = result.trim().toCharArray();
					long num = 0;
					for (i = 0; i < c.length; i++) {
						if (c[i] >= '0' && c[i] <= '9') {
							num = num * 10 + c[i] - '0';
						}
					}
					if (num == 0) {
						n++;
						continue;
					}
					bb = true;
					// shouTool.保存图片(image, nuuu++ + "_" + num + "ss.bmp");
					// System.out.println("num=" + num);
					if (num < DataSave.ds.getSm_lmoney() || num > DataSave.ds.getSm_rmoney()) {
						n++;
						continue;
					}
					zhu += n;
					n++;
				}
				return zhu;
			}

			private boolean 特种马判断(int x, int y, int x2, int y2) {
				boolean bbbb = true;

				if (DataSave.ds.isSm_db()) {
					bbbb = false;
					if (shouTool.区域找图(x, y, x2, y2, 0.14, "sm_db.bmp") != null) {
						return true;
					}
				}
				if (DataSave.ds.isSm_jm()) {
					bbbb = false;
					if (shouTool.区域找图(x, y, x2, y2, 0.14, "sm_jm.bmp") != null) {
						return true;
					}
				}
				if (DataSave.ds.isSm_hl()) {
					bbbb = false;
					if (shouTool.区域找图(x, y, x2, y2, 0.14, "sm_hl.bmp") != null) {
						return true;
					}
				}
				return bbbb;
			}
		});
		扫马.start();
	}

	private static Thread 自动躺床 = null;

	private static void 自动躺床() {
		if (自动躺床 != null && 自动躺床.isAlive())
			return;
		自动躺床 = new Thread(new Runnable() {
			private int num = 0;
			private int m = 1;

			@Override
			public void run() {
				num = DataSave.ds.getRoleNumber();
				if (num == 0)
					return;
				// 上床
				shouTool.delay(2000);
				while (isTrue) {
					if (!isTrue)
						return;
					System.out.println("上床");
					上床();
					if (m >= num) {
						return;
					}
					if (!isTrue)
						return;
					System.out.println("等待精力满");
					等待精力满();
					if (!isTrue)
						return;
					System.out.println("切换人物");
					切换人物();
					if (!isTrue)
						return;

				}
				stop();
			}

			private void 切换人物() {
				if (!isTrue)
					return;
				gotRoleView();
				if (!isTrue)
					return;
				chooseRole(m++);
				if (!isTrue)
					return;
			}

			private void 等待精力满() {
				int x, y;
				x = DataSave.SCREEN_X + 396;
				y = DataSave.SCREEN_Y + 36;
				ajthread = new keyThread1();
				ajthread.start();
				while (isTrue && shouTool.点颜色比较(x, y, "3ABAF5") > 0.25) {
					Other.sleep(5000);
				}
				ajthread.close();
			}

			private void 上床() {
				Point p = null;
				long time = System.currentTimeMillis();
				int i = DataSave.SCREEN_WIDTH;
				while (isTrue) {
					for (int s = 0; s < 4; s++) {
						AutoCheckIn();
						Other.sleep(500);
					}
					shouTool.keyPressOne(KeyEvent.VK_S);
					shouTool.delay(200);
					shouTool.keyPress(KeyEvent.VK_A);
					shouTool.delay(750);
					shouTool.keyRelease(KeyEvent.VK_A);
					shouTool.delay(1000);
					p = shouTool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
							DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, 0.18, "操作.bmp");
					if (p != null)
						break;
					shouTool.keyPress(KeyEvent.VK_D);
					shouTool.delay(750);
					shouTool.keyRelease(KeyEvent.VK_D);
					shouTool.delay(1000);
					p = shouTool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
							DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, 0.18, "操作.bmp");
					if (p != null)
						break;
					shouTool.keyPress(KeyEvent.VK_S);
					shouTool.delay(1250);
					shouTool.keyRelease(KeyEvent.VK_S);
					shouTool.delay(1000);
					p = shouTool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
							DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, 0.18, "操作.bmp");
					if (p != null)
						break;
					shouTool.keyPress(KeyEvent.VK_W);
					shouTool.delay(500);
					shouTool.keyRelease(KeyEvent.VK_W);
					shouTool.delay(1000);
					p = shouTool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
							DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, 0.18, "操作.bmp");
					if (p != null)
						break;
					if (System.currentTimeMillis() - time > 20 * 1000) {
						shouTool.mouseMove(i, DataSave.SCREEN_HEIGHT / 2 + 100 + DataSave.SCREEN_Y);
						i -= 600;
						time = System.currentTimeMillis();
					}
				}
				if (!isTrue)
					return;
				shouTool.keyPressOne(KeyEvent.VK_R);

			}
		});
		自动躺床.start();
	}

	private static Thread 练马 = null;

	private static void 练马() {
		if (练马 == null || !练马.isAlive()) {
			练马 = new Thread(new Runnable() {
				private Thread benpao = null;
				private boolean bool = true;
				private LinkedList<Integer> keys = new LinkedList<>();

				private void init() {
					benpao = new Thread(new Runnable() {
						@Override
						public void run() {
							bool = true;
							shouTool.keyPress(KeyEvent.VK_W);
							shouTool.delay(10);
							shouTool.keyPress(KeyEvent.VK_SHIFT);
							keys.add(KeyEvent.VK_W);
							keys.add(KeyEvent.VK_SHIFT);
							shouTool.delay(3000);
							while (isTrue && bool) {
								int s = 0;
								boolean b = isMache();
								System.out.println("!!!：" + b);
								if (!b)
									break;
								if (DataSave.ds.islm_P()) {
									System.out.println("尝试漂移1");
									double i = 0;
									for (int key : keys) {
										shouTool.keyRelease(key);
									}
									keys.clear();
									while (isTrue && bool && s++ < 4) {
										Other.sleep(10);
										if (!bool || !isTrue) {
											System.out.println("结束1");
											return;
										}
									}
									System.out.println("尝试漂移2");
									if ((i = Math.random()) < 0.5) {
										shouTool.keyPress(KeyEvent.VK_A);
										keys.add(KeyEvent.VK_A);
									} else {
										shouTool.keyPress(KeyEvent.VK_D);
										keys.add(KeyEvent.VK_D);
									}
									shouTool.delay(10);
									shouTool.keyPress(KeyEvent.VK_S);
									keys.add(KeyEvent.VK_S);
									s = 0;
									System.out.println("尝试漂移3");
									while (isTrue && bool && s++ < 5) {
										Other.sleep(10);
										if (!bool || !isTrue) {
											for (int key : keys) {
												shouTool.keyRelease(key);
											}
											keys.clear();
											System.out.println("结束2");
											return;
										}
									}
									for (int key : keys) {
										shouTool.keyRelease(key);
									}
									keys.clear();
									s = 0;
									System.out.println("尝试漂移4");
									while (isTrue && bool && s++ < 5) {
										Other.sleep(10);
										if (!bool || !isTrue) {
											System.out.println("结束3");
											return;
										}
									}
									shouTool.keyPress(KeyEvent.VK_W);
									keys.addFirst(KeyEvent.VK_W);
									shouTool.delay(10);
									shouTool.keyPress(KeyEvent.VK_SHIFT);
									keys.addFirst(KeyEvent.VK_SHIFT);
									s = 0;
									System.out.println("尝试漂移5");
									while (isTrue && bool && s < 250) {
										Other.sleep(10);
										s++;
										if (s == 20) {
											if (DataSave.ds.islm_F()) {
												System.out.println("漂移喷气");
												shouTool.keyPressOne(KeyEvent.VK_F);
											}
										}
										if (!bool || !isTrue) {
											for (int key : keys) {
												shouTool.keyRelease(key);
											}
											keys.clear();

											System.out.println("结束4");
											return;
										}
									}
									System.out.println("尝试漂移6");
									if (!bool || !isTrue) {
										for (int key : keys) {
											shouTool.keyRelease(key);
										}
										keys.clear();
										System.out.println("结束5");
										return;
									}
									System.out.println("尝试漂移7");
									continue;
								}
								if (DataSave.ds.islm_F()) {
									System.out.println("喷气");
									shouTool.keyPressOne(KeyEvent.VK_F);
									s = 0;
									while (isTrue && bool && s < 40) {
										Other.sleep(10);
										s++;
										if (!bool) {
											for (int key : keys) {
												shouTool.keyRelease(key);
											}
											keys.clear();
											System.out.println("结束6");
											return;
										}
									}
								}
								System.out.println("循环1");
							}
							for (int key : keys) {
								shouTool.keyRelease(key);
							}
							keys.clear();
							System.out.println("循环结束");
							bool = false;
							if (!isTrue)
								return;
						}
					});
				}

				@Override
				public void run() {
					while (isTrue) {
						// 上马
						goTomache();
						// findMache();
						if (!isTrue)
							return;
						if (benpao == null || !benpao.isAlive()) {
							System.out.println("启动奔跑线程");
							init();
							benpao.start();
						}
						// 检测按键
						System.out.println("检测按键");
						// 1098 274 1121 295 911 152 973 280
						while (bool && isTrue && shouTool.区域找色(DataSave.SCREEN_X + 911, DataSave.SCREEN_Y + 152, DataSave.SCREEN_X + 973,
								DataSave.SCREEN_Y + 280, 0.15, 20, "006E95") == null) {
						}
						if (!bool) {
							continue;
						}
						System.out.println("停止奔跑");
						bool = false;
						if (!isTrue)
							return;
						Point p = null;// 1073 339 1093 545
						int state = 0;
						while (benpao.isAlive()) {
							Other.sleep(1); 
						}
						System.out.println("已停止奔跑");
						int s = 0;
						long time=System.currentTimeMillis();
						while (isTrue && s++ < 80) {
							while (isTrue && (p = shouTool.区域找图EX(DataSave.SCREEN_X + 1070, DataSave.SCREEN_Y + 290, DataSave.SCREEN_X + 1093,
									DataSave.SCREEN_Y + 590, 0.15, "lm_sx.bmp")) != null) {
								System.out.println(p);
								if (state == 0) { 
									if (p.y > 458) {
										state = 1;
										shouTool.keyPressOne(KeyEvent.VK_W);
									} else if (p.y < 398) {
										shouTool.keyPressOne(KeyEvent.VK_S);
										state = 2; 
									}
								} else if (state == 1) {
									if (p.y > 458) {
										state = 1;
										shouTool.keyPressOne(KeyEvent.VK_W);
									} else if (p.y < 428) {
										shouTool.keyPressOne(KeyEvent.VK_S);
										state = 3;
									}
								} else if (state == 2) {
									if (p.y < 398) {
										shouTool.keyPressOne(KeyEvent.VK_S);
										state = 2;
									} else if (p.y > 428) {
										shouTool.keyPressOne(KeyEvent.VK_W);
										state = 3;
									}
								} else if (state == 3) {
									if (p.y < 428) {
										shouTool.keyPressOne(KeyEvent.VK_S);
									} else {
										shouTool.keyPressOne(KeyEvent.VK_W);
									}
								}
								Other.sleep(40);
							}
							Other.sleep(20);
							if (p == null) {
								break;
							}
						}
						if(System.currentTimeMillis()-time<3000) { 
							Other.sleep((int)(System.currentTimeMillis()-time));
						}
					}
				}

				private boolean shangma() {
					long time = System.currentTimeMillis();
					do {
						shouTool.delay(500);
						shouTool.keyPressOne(KeyEvent.VK_R);
						shouTool.delay(500);
						if (System.currentTimeMillis() - time > 5000)
							return false;
					} while ((shouTool.区域找色(864, 907, 1022, 922, 0.1, 40, "579AA0") == null || shouTool.区域找色(864, 907, 1022, 922, 0.1, 40, "403123") == null)
							&& isTrue);
					return true;
				}

				private void goTomache() {
					// 判断是否已在马车上
					if (isMache()) {
						return;
					}
					if (!isTrue)
						return;
					shouTool.delay(1000);
					shouTool.keyPressOne(KeyEvent.VK_R);
					shouTool.delay(1500);
					if ((shouTool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 96, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 173,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 62, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 158, 0.15, 40, "5CA3AA") == null)
							&& isTrue) {
					} else {
						return;
					}

					if (!GameListenerThread.DJNI.isCorsurShow())
						shouTool.keyPressOne(KeyEvent.VK_CONTROL);
					shouTool.delay(500);
					if (!isTrue)
						return;
					Point p1 = null;
					Point p2 = null;
					long time = 0;
					do {
						shouTool.mouseMovePressOne(DataSave.SCREEN_X + 40, DataSave.SCREEN_Y + 123, InputEvent.BUTTON3_MASK);
						shouTool.delay(1000);
						time += 500;
					} while ((p2 = shouTool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 234, DataSave.SCREEN_Y + 108,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 106, DataSave.SCREEN_Y + 204, 0.07, 5, "E5E6FE")) == null
							&& (p1 = shouTool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 234, DataSave.SCREEN_Y + 108,
									DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 106, DataSave.SCREEN_Y + 204, 0.07, 1, "C7A002")) == null
							&& isTrue && time < 3000);
					if (!isTrue)
						return;
					if (p1 != null || p2 != null) {
						shouTool.keyPressOne(KeyEvent.VK_T);
						shouTool.delay(200);
					}

					if (GameListenerThread.DJNI.isCorsurShow())
						shouTool.keyPressOne(KeyEvent.VK_CONTROL);
					shouTool.delay(200);
					if (!isTrue)
						return;
					while (isTrue) {
						BufferedImage image = shouTool.截取屏幕(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 51, DataSave.SCREEN_Y + 43,
								DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 32, DataSave.SCREEN_Y + 141);
						Other.sleep(1000);
						if (shouTool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 51, DataSave.SCREEN_Y + 43,
								DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 32, DataSave.SCREEN_Y + 141, 0.01, image) != null) {
							break;
						}
					}
					shouTool.keyPressOne(KeyEvent.VK_S);
					shouTool.delay(200);
					shouTool.keyPressOne(KeyEvent.VK_S);
					shouTool.delay(200);
					if (!isTrue)
						return;
					findMache();
					if (!isTrue)
						return;
				}

				private boolean isMache() {

					if ((shouTool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 96, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 173,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 62, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 158, 0.15, 40, "5CA3AA") == null)
							&& isTrue) {
						return false;
					} else {
						return true;
					}
				}

				private void findMache() {
					while (isTrue) {
						long time = System.currentTimeMillis();
						while (isTrue && System.currentTimeMillis() - time < 2000) {
							Other.sleep(1000);
							if (shouTool.区域找图EX(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH, DataSave.SCREEN_HEIGHT, 0.15, "马车.bmp") != null) {

								if (!shangma()) {
									break;
								}
								shouTool.delay(2000);
								return;
								// }
							}
							// 判断是否已在马车上
							if (!isMache() && isTrue) {
							} else {
								return;
							}
							// i -= 600;
						}
						shouTool.keyPressOne(KeyEvent.VK_W);
						shouTool.delay(200);
						if (!GameListenerThread.DJNI.isCorsurShow())
							shouTool.keyPressOne(KeyEvent.VK_CONTROL);
						shouTool.delay(200);
						if (!isTrue)
							return;
						shouTool.mouseMovePressOne(DataSave.SCREEN_X + 40, DataSave.SCREEN_Y + 123, InputEvent.BUTTON1_MASK);
						shouTool.delay(1500);
						if (!isTrue)
							return;
						if (GameListenerThread.DJNI.isCorsurShow())
							shouTool.keyPressOne(KeyEvent.VK_CONTROL);
						shouTool.delay(200);
					}
				}

			});
			练马.start();
		}
	}

	private static Thread 自动上架 = null;

	private static void 自动上架() {
		if (自动上架 == null || !自动上架.isAlive()) {
			自动上架 = new Thread(new Runnable() {
				private BufferedImage image = null;
				private Point sp = null;

				@Override
				public void run() {
					// 1150,275,1316,376
					shouTool.delay(1000);
					Rectangle r = DataSave.ds.getZDSJ_XYWH();
					image = shouTool.截取屏幕(r.x, r.y, r.x + r.width, r.y + r.height);
					long time = System.currentTimeMillis();
					while ((sp = shouTool.区域找色(DataSave.SCREEN_WIDTH / 2 + 190 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 - 265 + DataSave.SCREEN_Y,
							DataSave.SCREEN_WIDTH / 2 + 356 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 - 164 + DataSave.SCREEN_Y, 0, 20, "85AFCE")) == null
							&& System.currentTimeMillis() - time < 5000 && isTrue) {
						shouTool.delay(500);
					}
					if (!isTrue) {
						return;
					}
					if (sp == null) {
						JOptionPane.showMessageDialog(DataSave.StaticUI, "未找到上架区域坐标", "警告", JOptionPane.YES_OPTION);
						return;
					}
					Point p = null;
					while (isTrue) {
						do {
							if (p != null
									&& shouTool.区域找图(p.x - 10, p.y - 10, p.x + image.getWidth() + 10, p.y + image.getHeight() + 20, 0.2, "空格子.bmp") == null)
								break;
							p = shouTool.区域找图(DataSave.SCREEN_WIDTH - 420 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 - 222 + DataSave.SCREEN_Y,
									DataSave.SCREEN_WIDTH - 16 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 + 169 + DataSave.SCREEN_Y, 0.25, image);
						} while (p == null && isTrue);
						if (!isTrue) {
							return;
						}
						shouTool.mouseMovePressOne(p.x, p.y, InputEvent.BUTTON3_MASK);
						shouTool.delay(200);
						shouTool.keyPressOne(KeyEvent.VK_F);
						shouTool.delay(100);
						shouTool.keyPressOne(KeyEvent.VK_ENTER);
						shouTool.delay(200);
						// 1214,400 1242,324 900,749
						shouTool.mouseMovePressOne(sp.x - 28, sp.y + 76, InputEvent.BUTTON1_MASK);
						shouTool.delay(200);
						// shouTool.mouseMovePressOne(sp.x - 342, sp.y + 425,
						// InputEvent.BUTTON1_MASK);
						// shouTool.delay(500);
						shouTool.keyPressOne(KeyEvent.VK_ENTER);
						shouTool.delay(500);
						shouTool.keyPressOne(KeyEvent.VK_ENTER);
						shouTool.delay(500);
					}
				}
			});
			自动上架.start();
		}
	}

	private static Thread 卡负重 = null;

	public static void 卡负重() {
		if (卡负重 == null || !卡负重.isAlive()) {
			卡负重 = new Thread(new Runnable() {
				Point cangku = null;
				Point beibao = null;
				BufferedImage image = null;
				// 识别出坐骑背包位置
				Point macheX = null;

				@Override
				public void run() {
					shouTool.delay(1000);
					// 确定，坐骑，仓库，背包坐标
					long time = System.currentTimeMillis();

					cangku = null;
					while (isTrue && System.currentTimeMillis() - time < 5000) {
						cangku = GameUIModel.Findjgbb3(shouTool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y + 80,
								DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 300, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_X);
						if (cangku != null) {
							break;
						}
						cangku = GameUIModel.FindXX(shouTool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y + 80,
								DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 300, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_X);
						if (cangku != null) {
							break;
						}
						shouTool.delay(50);
					}

					if (cangku == null) {
						JOptionPane.showMessageDialog(DataSave.StaticUI, "未找到仓库所在坐标", "警告", JOptionPane.YES_OPTION);
						return;
					}
					Point lin = DataSave.ds.getKfz_XY();
					if (lin.x == -1 || lin.x > 8 || lin.y > 8) {
						JOptionPane.showMessageDialog(DataSave.StaticUI, "未设置仓库物品坐标，或坐标非法", "警告", JOptionPane.YES_OPTION);
						return;
					}
					time = System.currentTimeMillis();
					while ((macheX = GameUIModel.FindXX(shouTool, 0, 0, DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X - 200,
							DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y)) == null && isTrue && System.currentTimeMillis() - time < 5000) {
						Other.sleep(10);
					}
					if (macheX == null) {
						JOptionPane.showMessageDialog(DataSave.StaticUI, "未找到马车背包所在坐标", "警告", JOptionPane.YES_OPTION);
						return;
					}

					time = System.currentTimeMillis();
					while ((beibao = GameUIModel.FindXX(shouTool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 200, DataSave.SCREEN_X + 80,
							DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 - 80 + DataSave.SCREEN_Y)) == null && isTrue
							&& System.currentTimeMillis() - time < 5000) {
						Other.sleep(10);
					}
					if (beibao == null) {
						JOptionPane.showMessageDialog(DataSave.StaticUI, "未找到仓库背包所在坐标", "警告", JOptionPane.YES_OPTION);
						return;
					}

					int x1 = cangku.x - 410;
					int y1 = cangku.y + 91;
					cangku.x = x1 + (lin.x - 1) * 54;
					cangku.y = y1 + (lin.y - 1) * 54;

					// 从仓库取出
					ckqc();
					if (!isTrue)
						return;
					// 放入马车
					frmc();
					if (!isTrue)
						return;
					while (isTrue) {
						ckqc();
						if (!isTrue)
							return;
						// 放入背包
						frbb();
						if (!isTrue)
							return;

						// 放入马车
						frmc();
						if (!isTrue)
							return;
					}
				}

				private BufferedImage mc_img = null;

				private void frbb() {
					if (!isTrue)
						return;
					if (mc_img == null) {// 669 167 320 255
						macheX.x = macheX.x - 353;
						macheX.y = macheX.y + 88;
						mc_img = shouTool.截取屏幕(macheX.x - 2, macheX.y - 4, macheX.x + 24, macheX.y + 6);
						shouTool.保存图片(mc_img, "temp.bmp");
					}
					while (shouTool.区域找图(macheX.x - 15, macheX.y - 10, macheX.x + 30, macheX.y + 10, 0.15, mc_img) == null && isTrue) {
						shouTool.delay(10);
					}
					if (!isTrue)
						return;
					shouTool.delay(300);
					String color = shouTool.得到点颜色(macheX.x + 5 + 32, macheX.y + 5 + 20);
					shouTool.mouseMovePressOne(macheX.x + 5, macheX.y + 5, InputEvent.BUTTON3_MASK);
					long time = System.currentTimeMillis();
					if (!isTrue)
						return;
					while (shouTool.点颜色比较(macheX.x + 5 + 32, macheX.y + 5 + 20, color) < 0.07 && isTrue) {
						if (System.currentTimeMillis() - time > 700) {
							shouTool.mouseMovePressOne(macheX.x + 5, macheX.y + 5, InputEvent.BUTTON3_MASK);
							time = System.currentTimeMillis();
						}
					}
					if (!isTrue)
						return;
					Point lin = new Point(macheX.x + 5 + 10, macheX.y + 5 + 10);

					if (!isTrue)
						return;
					shouTool.delay(500);
					time = System.currentTimeMillis();
					do {
						shouTool.mouseMovePressOne(lin.x + 10, lin.y + 2, InputEvent.BUTTON1_MASK);
						shouTool.delay(200);
						shouTool.mouseMove(lin.x - 50, lin.y - 200);
						long time2 = System.currentTimeMillis();
						boolean bool = true;
						while (isTrue && GameUIModel.FindXX2(shouTool, lin.x - 100, lin.y, lin.x + 250, lin.y + 60) == null) {
							if (System.currentTimeMillis() - time2 > 700) {
								bool = false;
								break;
							}
						}
						if (bool) {
							break;
						}
					} while (System.currentTimeMillis() - time < 5 * 1000 && isTrue);
					shouTool.delay(100);
					shouTool.keyPressOne(KeyEvent.VK_F);
					shouTool.delay(100);
					shouTool.keyPressOne(KeyEvent.VK_ENTER);
				}

				private void frmc() {
					if (!isTrue)
						return;
					if (image == null) {
						beibao.x = beibao.x - 410;
						beibao.y = beibao.y + 128;
						image = shouTool.截取屏幕(beibao.x - 2, beibao.y - 4, beibao.x + 24, beibao.y + 6);
						shouTool.保存图片(image, "temp.bmp");
					}

					while (shouTool.区域找图(beibao.x - 15, beibao.y - 10, beibao.x + 26, beibao.y + 8, 0.15, image) == null && isTrue) {
						shouTool.delay(10);
					}
					if (!isTrue)
						return;
					shouTool.delay(300);

					String color = shouTool.得到点颜色(beibao.x + 32, beibao.y + 20);

					shouTool.mouseMovePressOne(beibao.x, beibao.y, InputEvent.BUTTON3_MASK);

					long time = System.currentTimeMillis();
					if (!isTrue)
						return;
					while (shouTool.点颜色比较(beibao.x + 32, beibao.y + 20, color) < 0.07 && isTrue) {
						if (System.currentTimeMillis() - time > 700) {
							shouTool.mouseMovePressOne(beibao.x, beibao.y, InputEvent.BUTTON3_MASK);
							time = System.currentTimeMillis();
						}
					}
					if (!isTrue)
						return;
					Point lin = new Point(beibao.x + 10, beibao.y + 10);
					time = System.currentTimeMillis();
					do {
						shouTool.delay(200);
						shouTool.mouseMovePressOne(lin.x + 10, lin.y + 2, InputEvent.BUTTON1_MASK);
						long time2 = System.currentTimeMillis();
						boolean bool = true;
						while (isTrue && GameUIModel.FindXX2(shouTool, lin.x - 100, lin.y, lin.x + 250, lin.y + 60) == null) {
							if (System.currentTimeMillis() - time2 > 700) {
								bool = false;
								break;
							}
						}
						if (bool) {
							break;
						}
					} while (System.currentTimeMillis() - time < 5 * 1000 && isTrue);
					shouTool.delay(100);
					shouTool.keyPressOne(KeyEvent.VK_F);
					shouTool.delay(100);
					shouTool.keyPressOne(KeyEvent.VK_ENTER);
				}

				private void ckqc() {
					shouTool.delay(500);
					String color = shouTool.得到点颜色(cangku.x + 32, cangku.y + 20);
					shouTool.mouseMovePressOne(cangku.x, cangku.y, InputEvent.BUTTON3_MASK);
					if (!isTrue)
						return;
					while (shouTool.点颜色比较(cangku.x + 32, cangku.y + 20, color) < 0.07 && isTrue) {
						shouTool.delay(10);
					}
					if (!isTrue)
						return;
					Point lin = new Point(cangku.x + 22, cangku.y + 20 + 34);
					long time = System.currentTimeMillis();
					shouTool.delay(200);
					do {
						shouTool.mouseMovePressOne(lin.x + 10, lin.y + 2, InputEvent.BUTTON1_MASK);
						shouTool.delay(200);
						shouTool.mouseMove(lin.x - 300, lin.y);
						long time2 = System.currentTimeMillis();
						boolean bool = true;
						while (isTrue && GameUIModel.FindXX2(shouTool, lin.x - 100, lin.y, lin.x + 250, lin.y + 60) == null) {
							if (System.currentTimeMillis() - time2 > 700) {
								bool = false;
								break;
							}
						}
						if (bool) {
							break;
						}
					} while (System.currentTimeMillis() - time < 5 * 1000 && isTrue);
					shouTool.delay(100);
					byte[] b = DataSave.ds.getKfz_num().getBytes(Charset.forName("UTF-8"));
					for (int i = 0; i < b.length; i++) {
						shouTool.keyPressOne(Tool.getkeyCode("" + ((char) b[i])));
						shouTool.delay(100);
					}
					shouTool.keyPressOne(KeyEvent.VK_ENTER);
				}
			});
			卡负重.start();
		}
	}

	public static void stop() {

		关闭播放();
		isTrue = false;
		if (ajthread != null) {
			ajthread.close();
		}

		DataSave.ds.Stop();
		while (isRun()) {
			Other.sleep(100);
		}
	}

	public static String mode() {
		return mode;
	}

	public static boolean isRun() {
		return isTrue || (ajthread != null ? ajthread.isAlive() : false);
	}

	private static class keyThread1 extends Thread {
		private boolean bool = false;

		public void close() {
			bool = false;
			while (keyThread1.this.isAlive()) {
				Other.sleep(50);
			}
		}

		@Override
		public void run() {
			bool = true;
			DSThreadPanel[] threadPanel = DataSave.ds.getDSThreadPanels();
			long time = System.currentTimeMillis();
			while (bool && isTrue) {
				if (System.currentTimeMillis() - time > 2000 && !DyHandle.isRun() && !MYHandle.isRun()) {
					AutoCheckIn();
					time = System.currentTimeMillis();
				}
				EatGRPJ();
				EatHPMP();
				for (int i = 0; i < threadPanel.length; i++) {
					if (threadPanel[i].isSelect() && threadPanel[i].getNextTime() < System.currentTimeMillis()) {
						int code = Tool.getkeyCode(threadPanel[i].getButton());
						shouTool.keyPressOne(code);
						// System.out.println(i + ": " +
						// threadPanel[i].getNextTime() + " " +
						// System.currentTimeMillis());
						threadPanel[i].setNextTime();
						shouTool.delay(500);
					}
				}
			}
		}

	}

	static long time3 = System.currentTimeMillis();
	static double length = 1107 - 810;
	static String mp_color = null;

	public static void EatHPMP() {
		if (System.currentTimeMillis() - time3 > 40) {
			time3 = System.currentTimeMillis();
			HPMPBean bean = DataSave.ds.getHPBean();
			HPMPBean bean2 = DataSave.ds.getMPBean();
			boolean bool = false;
			if (bean != null && bean.isBool()) {// 810 978 1107 978 0000CC
				int i = Tool.getkeyCode(bean.getButton().trim());
				if (i > 0) {
					double x = DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 150 + length * bean.getRatio() / 100;
					double d = shouTool.点颜色比较((int) x, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 102, "0000CC");
					if (d > 0.07) {
						shouTool.keyPressOne(i);
						shouTool.delay(25);
						bool = true;
					}
				}
			}
			if (bean2 != null && bean2.isBool()) {// 810 994 1107 994 C87E00
				int i = Tool.getkeyCode(bean2.getButton().trim());
				if (i > 0) {
					double x = DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 150 + length * bean2.getRatio() / 100;
					if (mp_color == null) {
						mp_color = shouTool.得到点颜色((int) x, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 86);
					}
					double d = shouTool.点颜色比较((int) x, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 86, mp_color);
					if (d > 0.07) {
						shouTool.keyPressOne(i);
						bool = true;
					}
				}
			}
			if (bool)
				shouTool.delay(500);
		}
	}

	// private static class keyThread2 extends Thread {
	// @Override
	// public void run() {
	// int state = DataSave.ds.isRightOrLeft();
	// if (state == 1) {
	// shouTool.keyPressLong(KeyEvent.VK_W);
	// shouTool.keyPressLong(KeyEvent.VK_A);
	// } else if (state == 2) {
	// shouTool.keyPressLong(KeyEvent.VK_W);
	// shouTool.keyPressLong(KeyEvent.VK_D);
	// }
	// while (isTrue) {
	// shouTool.delay(100);
	// if (DataSave.ds.isRightOrLeft() != state) {
	// if (state == 1) {
	// shouTool.keyRelease(KeyEvent.VK_D);
	// shouTool.keyPressLong(KeyEvent.VK_W);
	// shouTool.keyPressLong(KeyEvent.VK_A);
	// } else if (state == 2) {
	// shouTool.keyRelease(KeyEvent.VK_A);
	// shouTool.keyPressLong(KeyEvent.VK_W);
	// shouTool.keyPressLong(KeyEvent.VK_D);
	// } else {
	// shouTool.keyRelease(KeyEvent.VK_A);
	// shouTool.keyRelease(KeyEvent.VK_W);
	// shouTool.keyRelease(KeyEvent.VK_D);
	// }
	// state = DataSave.ds.isRightOrLeft();
	// }
	// }
	// if (state != 0) {
	// shouTool.keyRelease(KeyEvent.VK_A);
	// shouTool.keyRelease(KeyEvent.VK_W);
	// shouTool.keyRelease(KeyEvent.VK_D);
	// }
	// }
	//
	// }

	private static long grpj_time = 0;
	private static DimgFile grpj = null;
	private static Tool shouTool = new Tool();

	public static boolean isEatGRPJ() {
		if (DataSave.工人啤酒 && System.currentTimeMillis() - grpj_time > DataSave.ds.getGrpj_time())
			return true;
		return false;
	}

	public static boolean EatGRPJ() {
		掉血提醒();
		TZJD();
		精灵冒险();
		AutoCheckIn();
		shouTool.delay(1000);
		if (!DataSave.工人啤酒)
			return false;
		if (System.currentTimeMillis() - grpj_time < DataSave.ds.getGrpj_time())
			return false;
		System.out.println(System.currentTimeMillis() + " " + grpj_time + " " + DataSave.ds.getGrpj_time());
		// 打开背包
		if (grpj == null) {
			grpj = DimgFile.getImgFile(DataSave.JARFILEPATH + "/data/grpj.bmp");
		}
		int j = Integer.parseInt(DataSave.deBug.打开背包的前置时间.getText().trim());
		if (j > 0)
			shouTool.delay(j);
		shouTool.keyPressOne(KeyEvent.VK_I);
		j = Integer.parseInt(DataSave.deBug.检测鱼竿的前置时间.getText().trim());
		if (j > 0)
			shouTool.delay(j);
		double d = Double.parseDouble(DataSave.deBug.鱼竿检测准确度.getText().trim());
		if (d <= 0)
			d = 0;
		Point p = shouTool.区域找图(DataSave.SCREEN_WIDTH - 473 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 - 367 + DataSave.SCREEN_Y,
				DataSave.SCREEN_WIDTH - 5 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 + 366 + DataSave.SCREEN_Y, d, grpj.bufferedImage);
		if (p != null) {
			shouTool.mouseMove(p.x + grpj.bufferedImage.getWidth() / 2, p.y + grpj.bufferedImage.getHeight() / 2);
			j = Integer.parseInt(DataSave.deBug.更换鱼竿的前置时间.getText().trim());
			if (j > 0)
				shouTool.delay(j);
			shouTool.mousePressOne(InputEvent.BUTTON3_MASK);
			shouTool.delay(2000);

			Point point = GameUIModel.FindXX(shouTool, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 100, DataSave.SCREEN_Y + 50,
					DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
			if (point == null)
				return false;
			// 1874 220
			// stream 1520 846
			int x = point.x - (1874 - 1520);
			int y = point.y + (846 - 220);
			shouTool.mouseMovePressOne(x, y, InputEvent.BUTTON1_MASK);
			shouTool.delay(500);
			// 1188 498
			shouTool.mouseMovePressOne(x - 332, y - 348, InputEvent.BUTTON1_MASK);
			if (DataSave.ds.isGrpj_cf()) {// 1652 905
				shouTool.delay(500);
				shouTool.mouseMovePressOne(x + 134, y, InputEvent.BUTTON1_MASK);
			}
			grpj_time = System.currentTimeMillis();

		}
		shouTool.delay(1000);
		shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
		return true;
	}

	private static String hp_color = null;

	private static void 掉血提醒() {
		if (!DataSave.被击换线)
			return;
		if (hp_color == null)
			hp_color = shouTool.得到点颜色(DataSave.SCREEN_X + 462, DataSave.SCREEN_Y + 60);
		if (!isTrue)
			return;
		if (shouTool.点颜色比较(DataSave.SCREEN_X + 462, DataSave.SCREEN_Y + 60, hp_color) < 0.35 && isTrue) {
			return;
		}
		if (isTrue)
			播放音乐();
	}

	static long time2 = System.currentTimeMillis();
	static int hwnd = 0;

	public static boolean AutoCheckIn() {

		if (DataSave.游戏锁定) {
			if (hwnd == 0 && GameListenerThread.APP_ID != 0) {
				hwnd = GameListenerThread.DJNI.getWinHwnd(GameListenerThread.APP_ID);
			}
			if (hwnd != 0) {
				long hForeWnd = OS.GetForegroundWindow();
				if (hForeWnd != hwnd) {
					int dwForeID = OS.GetWindowThreadProcessId(hForeWnd, null);
					int dwCurID = OS.GetCurrentThreadId();
					OS.AttachThreadInput(dwCurID, dwForeID, true);
					OS.ShowWindow(hwnd, 1);
					OS.SetWindowPos(hwnd, OS.HWND_TOPMOST, 0, 0, 0, 0, OS.SWP_NOSIZE | OS.SWP_NOMOVE);
					OS.SetWindowPos(hwnd, OS.HWND_NOTOPMOST, 0, 0, 0, 0, OS.SWP_NOSIZE | OS.SWP_NOMOVE);
					OS.SetForegroundWindow(hwnd);
					OS.AttachThreadInput(dwCurID, dwForeID, false);
					System.out.println("游戏切换至前台");
				}
			}

			// 判断鼠标是否显示
			if (GameListenerThread.DJNI.isCorsurShow()) {
				System.out.println("鼠标显示1");
				shouTool.keyPressOne(KeyEvent.VK_CONTROL);
				shouTool.delay(1500);
			}
			if (GameListenerThread.DJNI.isCorsurShow()) {
				System.out.println("鼠标显示2");
				shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
			}
			if (!DataSave.ESC) {
				time2 = System.currentTimeMillis();
			}
		}
		if (DataSave.ESC && System.currentTimeMillis() - time2 > 2000) {
			int n = 0;
			while (DataSave.D_LINE_ID >= 0 && getLineTeImg() == null && n++ < 2) {
				shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
				shouTool.delay(500);
			}
			Point point = null;
			// 1315,136,1411,206
			if ((point = GameUIModel.FindXX(shouTool, DataSave.SCREEN_X + 300, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
					DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y)) != null) {
				System.out.println("FindXX:" + point);
				shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
				time2 = System.currentTimeMillis();
				return false;
			}
			if ((point = GameUIModel.FindXX2(shouTool, DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X, DataSave.SCREEN_Y,
					DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y)) != null) {
				System.out.println("FindXX2:" + point);
				shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
				time2 = System.currentTimeMillis();
				return false;
			}
			if ((point = GameUIModel.FindXX4(shouTool, DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X, DataSave.SCREEN_Y,
					DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y)) != null) {
				System.out.println("FindXX4:" + point);
				shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
				time2 = System.currentTimeMillis();
				return false;
			}

			// 1098,868,1223,942
			if ((point = GameUIModel.Find公共对话框(shouTool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 138, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 212,
					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 100, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 138)) != null) {
				// if (System.currentTimeMillis() - time2 > 2000) {
				System.out.println("Find公共对话框:" + point);
				shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
				time2 = System.currentTimeMillis();
				return false;
				// }
			}

			time2 = System.currentTimeMillis();
		}
		return true;
	}

	public static BufferedImage getLineTeImg() {
		BufferedImage temp = shouTool.截取屏幕(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 192, DataSave.SCREEN_Y + 4,
				DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 14, DataSave.SCREEN_Y + 24);
		// 从左往右搜索，看看坐标位置
		int leftx = 0;
		int right = 0;
		Color rgb = shouTool.StringColor("61CDB0");
		for (int i = 0; i < temp.getWidth(); i++) {
			for (int j = 0; j < temp.getHeight(); j++) {
				if (shouTool.颜色比较(new Color(temp.getRGB(i, j)), rgb) < 0.12) {
					if (leftx == 0)
						leftx = i;
					right = i;
				}
			}
		}
		if (leftx != 0 && right != 0 && leftx != right) {
			int x = leftx - 10 < 0 ? 0 : leftx - 10;
			int x2 = right - leftx + 29;
			x2 = (x2 >= temp.getWidth() - x ? temp.getWidth() - x : x2);
			temp = ImgTool.cutImage(temp, x, 0, x2, temp.getHeight());
		} else
			temp = null;
		if (temp == null) {
			return null;
		}
		return temp;
	}

	public static void hx(int n) {
		int ds = 0;
		// 停止定时
		System.out.println("停止定时");
		if (DSHandle.isRun()) {
			ds = 1;
			DSHandle.stop();
		}
		isTrue = true;
		// 换线
		if (!isTrue) {
			return;
		}
		// 前往角色界面
		System.out.println("前往角色界面");
		gotRoleView();
		if (!isTrue) {
			return;
		}
		// 选择一个随机线路
		System.out.println("选择一个随机线路");
		gotoRandomLine();
		if (!isTrue) {
			return;
		}
		System.out.println("选择一个角色进入游戏");
		// 选择一个角色进入游戏
		chooseRole(n);
		if (!isTrue) {
			return;
		}
		isTrue = false;
		System.out.println("恢复定时");
		if (ds == 1)
			DSHandle.start(null);
	}

	private static void chooseRole(int n) {
		// 1821,71,1830,135
		shouTool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1829), DataSave.SCREEN_Y + 135 + (int) (n * 72.5),
				MouseEvent.BUTTON1_MASK);
		long time = System.currentTimeMillis();
		while (isTrue && System.currentTimeMillis() - time < 1 * 1000 * 60) {
			Other.sleep(1000);
		}
	}

	private static void gotoRandomLine() {
		shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
		long time = System.currentTimeMillis();
		while (isTrue && System.currentTimeMillis() - time < 30 * 1000) {
			Other.sleep(1000);
		}
		if (!isTrue) {
			return;
		}
		Point p = null;
		p = new Point(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 1920 + 1864, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 1080 + 1014);
		shouTool.mouseMovePressOne(p.x, p.y, InputEvent.BUTTON1_MASK);
		time = System.currentTimeMillis();
		while (isTrue && System.currentTimeMillis() - time < 30 * 1000) {
			Other.sleep(1000);
		}
		if (!isTrue) {
			return;
		}
	}

	private static void gotRoleView() {
		// while (isTrue) {
		shouTool.delay(150);
		shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
		shouTool.delay(150);
		shouTool.keyPress(KeyEvent.VK_ALT);
		shouTool.delay(50);
		shouTool.keyPressOne(KeyEvent.VK_F4);
		shouTool.delay(50);
		shouTool.keyRelease(KeyEvent.VK_ALT);
		shouTool.delay(150);
		shouTool.mouseMovePressOne(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X - 265, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y + 102,
				MouseEvent.BUTTON1_MASK);
		if (!isTrue) {
			return;
		}
		shouTool.delay(100);
		shouTool.keyPressOne(KeyEvent.VK_ENTER);

		long time = System.currentTimeMillis();
		while (isTrue && System.currentTimeMillis() - time < 1 * 60 * 1000) {
			Other.sleep(1000);
		}
	}

	public static void hx() {
		hx(0);
	}

	static long tzjd_time = 0;

	public static void TZJD() {
		String jdname = "";
		if (!DataSave.ds.isTzjd() || (jdname = DataSave.ds.getJd_name()).equals("")) {
			return;
		}
		if (System.currentTimeMillis() - tzjd_time > 1 * 60 * 1000) {
			tzjd_time = System.currentTimeMillis();
			if (!is满力气()) {
				return;
			}
			if (!打开地图())
				return;
			shouTool.mouseMovePressOne(DataSave.SCREEN_X + 95, DataSave.SCREEN_Y + 111, InputEvent.BUTTON1_MASK);
			shouTool.delay(500);
			shouTool.mouseMovePressOne(DataSave.SCREEN_X + 95, DataSave.SCREEN_Y + 111, InputEvent.BUTTON1_MASK);
			shouTool.delay(500);
			粘贴名字(jdname);
			int n = DataSave.ds.getJd_n();
			for (int i = 0; i < 3; i++) {
				shouTool.mouseMovePressOne(DataSave.SCREEN_X + 53, DataSave.SCREEN_Y + 281 + n * 28, InputEvent.BUTTON1_MASK);
				shouTool.delay(500);
			}
			shouTool.mouseMovePressOne(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y + (int) (DataSave.SCREEN_HEIGHT / 2.0 * 0.93),
					InputEvent.BUTTON1_MASK);
			shouTool.delay(2500);
			投资();
			关闭地图();
		}
	}

	private static boolean is满力气() {
		int x, y;
		x = DataSave.SCREEN_X + 396;
		y = DataSave.SCREEN_Y + 36;
		if (shouTool.点颜色比较(x, y, "3ABAF5") > 0.25) {
			return false;
		}
		return true;
	}

	private static void 关闭地图() {
		shouTool.keyPressOne(KeyEvent.VK_M);
		shouTool.delay(2000);
	}

	private static void 投资() {
		Point point = new Point(117, 314);
		// if ((point = shouTool.区域找图(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y +
		// 137, DataSave.SCREEN_X + 48, DataSave.SCREEN_Y + 580, 0.15,
		// "tz_lq.bmp")) == null) {
		// return;
		// }

		shouTool.mouseMovePressOne(point.x, point.y, InputEvent.BUTTON1_MASK);
		shouTool.delay(500);
		String num = DataSave.ds.getJd_num();
		if (num.equals(0 + "")) {
			shouTool.keyPressOne(KeyEvent.VK_F);
		} else {
			输入数字(num);
		}
		shouTool.delay(500);
		shouTool.keyPressOne(KeyEvent.VK_ENTER);
		shouTool.delay(500);
	}

	public static void 输入数字(String num) {
		byte[] liang = num.getBytes();
		for (int k = 0; k < liang.length; k++) {
			shouTool.delay(100);
			shouTool.keyPressOne(Tool.getkeyCode(((char) liang[k]) + ""));
		}
	}

	public static void 粘贴名字(String name) {
		Clipboard.setSysClipboardText(name);
		shouTool.delay(500);
		shouTool.keyPress(17);
		shouTool.delay(150);
		shouTool.keyPress(KeyEvent.VK_V);
		shouTool.delay(100);
		shouTool.keyRelease(KeyEvent.VK_V);
		shouTool.delay(50);
		shouTool.keyRelease(17);
		shouTool.delay(300);
		shouTool.keyPressOne(KeyEvent.VK_ENTER);
		shouTool.delay(1000);
	}

	private static BufferedImage te = null;

	private static boolean 打开地图() {
		// if (DataSave.D_LINE_ID < 0)
		// return false;
		// te = FishPrice.allbody.get(DataSave.D_LINE_ID).xianluBody.te;
		// if (te == null)
		// return false;
		//
		te = getLineTeImg();
		shouTool.delay(1000);
		int sdf = 0;
		while (sdf < 3 && shouTool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 200, DataSave.SCREEN_Y + 2, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 4,
				DataSave.SCREEN_Y + 28, 0.07, te) != null) {
			shouTool.keyPressOne(KeyEvent.VK_M);
			System.out.println("打开地图");
			shouTool.delay(2000);
			sdf++;
		}
		if (sdf == 0 || sdf == 3) {
			return false;
		}
		return true;
	}

	private static long jlmx_time = 0;

	public static void 精灵冒险() {
		if (!DataSave.黑精灵冒险) {
			return;
		}
		if (System.currentTimeMillis() - jlmx_time < 60 * 1000 * 60) {
			return;
		}
		jlmx_time = System.currentTimeMillis();
		// 打开esc
		shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
		shouTool.delay(2000);
		long time = System.currentTimeMillis();
		Point point = null;
		while ((point = GameUIModel.FindXX(shouTool, DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X, DataSave.SCREEN_Y,
				DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y)) == null) {
			shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
			if (System.currentTimeMillis() - time > 3000) {
				shouTool.delay(1000);
				AutoCheckIn();
				return;
			}
			shouTool.delay(2000);
		}
		if ((point = shouTool.区域找图(point.x - 500, point.y, point.x, point.y + 520, 0.15, "精灵冒险.bmp")) == null) {
			shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
			return;
		}
		shouTool.mouseMovePressOne(point.x, point.y, InputEvent.BUTTON1_MASK);
		shouTool.delay(15 * 1000);
		// 1327 781
		shouTool.mouseMovePressOne(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 367, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 + 257,
				InputEvent.BUTTON1_MASK);
		shouTool.delay(5 * 1000);
		shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
		shouTool.delay(2000);
	}

	public static void 转动镜头(double du) {
		int i = MouseInfo.getPointerInfo().getLocation().x;
		int len = (int) (DataSave.SCREEN_WIDTH / du);
		转动镜头(i, len);
	}

	private static void 转动镜头(int i, int len) {
		if (len <= 10)
			return;
		// if (i + 1 < len) {
		// len = len - i - 1;
		// shouTool.mouseMove(21, (int) (DataSave.SCREEN_HEIGHT * 0.4) +
		// DataSave.SCREEN_Y);
		// shouTool.delay(50);
		// shouTool.mouseMove(-2, (int) (DataSave.SCREEN_HEIGHT * 0.4) +
		// DataSave.SCREEN_Y);
		// shouTool.delay(50);
		// 转动镜头(MouseInfo.getPointerInfo().getLocation().x, len);
		// } else {

		shouTool.mouseMove(i - len, MouseInfo.getPointerInfo().getLocation().y);
		shouTool.delay(50);
		// }

	}

	public static void 转动镜头1(double d) {
		int i = MouseInfo.getPointerInfo().getLocation().x;
		i += DataSave.SCREEN_WIDTH / d;
		shouTool.mouseMove(i, (int) (DataSave.SCREEN_HEIGHT * 0.4) + DataSave.SCREEN_Y);
		// if (i >= DataSave.SCREEN_X + DataSave.SCREEN_WIDTH)
		// i = DataSave.SCREEN_X + i;
		// shouTool.mouseMove(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - i %
		// (DataSave.SCREEN_WIDTH + 600),
		// (int) (DataSave.SCREEN_HEIGHT * 0.4) + DataSave.SCREEN_Y);

	}

	public static boolean 丢弃() {
		DataSave.dy.textField.setText("丢弃");
		double d = Double.parseDouble(DataSave.deBug.鱼竿检测准确度.getText().trim());
		if (d <= 0)
			d = 0;

		Point point = GameUIModel.FindXX(shouTool, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 100, 80 + DataSave.SCREEN_Y,
				DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
		if (point == null) {
			return false;
		}

		Point p = shouTool.区域找图(point.x - 500, point.y, point.x + 12, point.y + 700, d, "坏鱼竿.bmp");
		if (p != null) {
			shouTool.delay(200);
			shouTool.mouseMove(p.x, p.y);
			shouTool.delay(200);
			shouTool.mousePress(InputEvent.BUTTON1_MASK);
			shouTool.delay(200);
			// 1865,783
			shouTool.mouseMove(point.x, point.y + 638);
			shouTool.delay(200);
			shouTool.mouseRelease(InputEvent.BUTTON1_MASK);
			shouTool.delay(200);
			shouTool.mousePressOne(InputEvent.BUTTON1_MASK);
			shouTool.delay(200);
			shouTool.keyPressOne(KeyEvent.VK_ENTER);
			return true;
		}
		return false;
	}

	public static void sendPCInfo(String sendinfo) {
		if (sendinfo == null || sendinfo.trim().equals("")) {
			return;
		}
		DSHandle.AutoCheckIn();
		shouTool.delay(500);
		shouTool.keyPressOne(KeyEvent.VK_ENTER);
		shouTool.delay(500);
		DSHandle.粘贴名字(sendinfo);
		shouTool.delay(500);
	}

	private static Player player = null;

	private static Thread playTherad = null;
	public static boolean play_bool = false;

	public synchronized static void 播放音乐() {
		play_bool = true;
		if (DataSave.监视声音 && (playTherad == null || !playTherad.isAlive()) && (player == null || !player.isComplete())) {
			playTherad = new Thread(new Runnable() {
				@Override
				public void run() {
					while (DataSave.监视声音 && play_bool) {
						try {
							if (player == null || !player.isComplete()) {
								if (player != null) {
									player.close();
								}
								InputStream inputStream = MAIN.class.getResourceAsStream("/com/mugui/ui/data/muise.mp3");
								player = new Player(inputStream);
								player.play();
							}
						} catch (JavaLayerException e) {
							e.printStackTrace();
						} finally {
							if (player != null && player.isComplete()) {
								player.close();
								player = null;
							}
						}
					}

					System.out.println("声音播放停止了");
				}
			});
			playTherad.start();
		}

	}

	public static void 关闭播放() {
		play_bool = false;
		if (player != null) {
			player.close();
			player = null;
		}
	}
}