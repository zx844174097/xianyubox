package com.mugui.model;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.tool.ImgTool;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.GameListenerThread;
import com.mugui.windows.Tool;

public class DyHandle {
	private static dyThread dy = null;
	// private static bjThread bj = null;
	private static boolean isTrue = false;
	private static long yh_time = 0;
	private static long stop_time = 0;

	public static void start() {
		if (!HsFileHandle.isRunModel()) {
			return;
		}
		if (dy == null || !dy.isAlive()) {
			isTrue = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					long time = 0;
					while (isTrue) {
						if (System.currentTimeMillis() - time > 60 * 1000) {
							TcpBag bag = new TcpBag();
							bag.setBag_id(TcpBag.ERROR);
							UserBean userBean = new UserBean();
							userBean.setCode("dy");
							bag.setBody(userBean.toJsonObject());
							TCPModel.SendTcpBag(bag);
							time = System.currentTimeMillis();
						} else {
							Other.sleep(1000);
						}

					}
				}
			}).start();
			yh_time = System.currentTimeMillis() + DataSave.dy.getYh_time();
			if (DataSave.dy.getDyStopTime() > 0)
				stop_time = System.currentTimeMillis() + DataSave.dy.getDyStopTime();
			dy = new dyThread();
			dy.start();
			// if (DataSave.被击换线 && (bj == null || !bj.isAlive())) {
			// bj = new bjThread();
			// bj.start();
			// }
		}
	}

	public static void stop() {

		isTrue = false;
		if (dy != null)
			dy.close();
		// if (bj != null)
		// bj.close();
		// bj = null;
		dy = null;
		DSHandle.stop();

	}

	// private static class bjThread extends Thread {
	// private static Tool tool = new Tool();
	// private boolean isTrue = false;
	//
	// @Override
	// public void run() {
	// isTrue = true;
	// String color = tool.得到点颜色(DataSave.SCREEN_X + 462, DataSave.SCREEN_Y +
	// 60);
	// while (isTrue) {
	// // 检测血条
	// while (tool.点颜色比较(DataSave.SCREEN_X + 462, DataSave.SCREEN_Y + 60, color)
	// < 0.35 && isTrue) {
	// tool.delay(50);
	// }
	// if (!isTrue) {
	// return;
	// }
	// // 播放音乐
	// DSHandle.播放音乐();
	// }
	// }
	//
	// public void close() {
	// isTrue = false;
	// DSHandle.关闭播放();
	// }
	//
	// }

	// private static boolean GM_Bool = false;

	public static boolean ds_bool = false;

	private static class dyThread extends Thread {
		private boolean isTrue = false;

		@Override
		public void run() {
			if (DataSave.dy.isSystemfishing()&&stop_time==0) {
				RUN2();
			} else {
				RUN();
				if(stop_time!=0&&DataSave.dy.isSystemfishing()) {
					RUN2();
				}else {
					DyHandle.stop();
				}
			} 
			System.out.println("钓鱼停止了1");
		}

		private void RUN2() {
			isTrue = true;
			if (shouTool == null)
				shouTool = new Tool();
			if (DSHandle.isRun()) {
				DSHandle.stop();
				ds_bool = true;
			}
			DSHandle.AutoCheckIn();
			shouTool.delay(1000);
			shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
			shouTool.delay(500);
			shouTool.keyPressOne(KeyEvent.VK_ESCAPE);

			// shouTool.delay(500);
			// shouTool.keyPressOne(KeyEvent.VK_CONTROL);
			DSHandle.startDxgj();
			if (DataSave.被监视 && DataSave.被转移) {// 1625 48 1627,49,1635,54
				listener_image = shouTool.截取屏幕(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 40, DataSave.SCREEN_Y + 48,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 38, DataSave.SCREEN_Y + 50);
			}
			long waitTime = DataSave.dy.getDyWaitTime();
			long numTime = System.currentTimeMillis();
			while (isTrue) {

				int liang = Integer.parseInt(DataSave.deBug.调整姿势的前置时间.getText().trim());
				if (liang / 2 > 0)
					shouTool.delay(liang / 2);
				shouTool.mousePressOne(InputEvent.BUTTON3_MASK);
				if (liang / 2 > 0)
					shouTool.delay(liang / 2);

				while (System.currentTimeMillis() - numTime < waitTime && isTrue) {
					if (isTrue && DataSave.工人啤酒 && DSHandle.EatGRPJ()) {
					}
					shouTool.delay(5000);
				}
				if (!isTrue)
					return;
				if (isTrue && DataSave.工人啤酒 && DSHandle.EatGRPJ()) {
				}

				if (!isTrue)
					return;
				while (isTrue) {
					// 查找钓鱼50按钮
					if (isTrue && !findDybutton(-1)) {
						shouTool.delay(50);
					}
					if (!isTrue)
						return;
					// 发送一个按键给程序
					liang = Integer.parseInt(DataSave.deBug.钓鱼空格的按下时间长度.getText().trim());
					if (liang <= 0)
						liang = 0;
					shouTool.keyPressOne(KeyEvent.VK_SPACE);
					if (!isTrue)
						return;
					MonitorBool();
					shouTool.delay(3500);
					int i = Integer.parseInt(DataSave.deBug.打开背包的前置时间.getText().trim());
					if (i > 0)
						shouTool.delay(i);
					if (isTrue && findDybutton(3)) {
						if (DataSave.更换鱼竿 && HuanYG()) {
						}
					}
					break;
				}

				DataSave.dy.textField.setText("额外操作");
				System.out.println("额外操作");
				long time = System.currentTimeMillis();
				while (isTrue) {

					if (ds_bool) {
						DSHandle.start(null);
					}
					shouTool.delay(2500);
					if (ds_bool) {
						DSHandle.stop();
					}

					if (TCPModel.sendinfo != null && !TCPModel.sendinfo.trim().equals("")) {
						DSHandle.sendPCInfo(TCPModel.sendinfo.trim());
						TCPModel.sendinfo = null;
					}
					MonitorBool();
					// 判断GM是否在询问
					// GM_XWBool();
					// 判断周围是否有人
					DSHandle.AutoCheckIn();
					if (!DataSave.白) {
						if (FishModel.start()) {
							// if (DataSave.被击换线 && bj != null) {
							// System.out.println("关闭被击换线");
							// bj.close();
							// }
						}
					}
					boolean bool = false;
					while (FishModel.isClose() && isTrue) {
						System.out.println("等待查看鱼价结束");
						shouTool.delay(1000);
						bool = true;
					}
					if (bool) {
						shouTool.delay(1000);
						while (!DSHandle.AutoCheckIn() && isTrue) {
							shouTool.delay(1000);
						}
					}
					// if (DataSave.被击换线 && (bj == null || !bj.isAlive())) {
					// bj = new bjThread();
					// bj.start();
					// }
					if (DataSave.更换鱼竿 && System.currentTimeMillis() - time > 20 * 1000 * 60) {
						time = System.currentTimeMillis();
						break;
					}
				}
				if (!isTrue)
					return;
				if (DataSave.丢弃鱼竿) {

				}
			}
		}

		// private boolean isHuanYG() {
		// if (yuGan == null)
		// yuGan = DataSave.custonimg.getyuGan();
		// if (yuGan == null)
		// return false;
		// Point p = null;
		// // 1536,131,1536,138 1531
		// p = shouTool.区域找色(DataSave.SCREEN_WIDTH - 384 - 2 - 2 +
		// DataSave.SCREEN_X, 131 + DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH -
		// 383 + DataSave.SCREEN_X,
		// 137 + DataSave.SCREEN_Y, 0.15, 25, "000000");
		// if (p == null && isTrue) {
		// return true;
		// }
		// return false;
		// }

		private void RUN() {

			isTrue = true;
			if (shouTool == null)
				shouTool = new Tool();
			if (DSHandle.isRun()) {
				DSHandle.stop();
				ds_bool = true;
			}
			DSHandle.AutoCheckIn();
			shouTool.delay(1000);
			shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
			shouTool.delay(500);
			shouTool.keyPressOne(KeyEvent.VK_ESCAPE);

			// shouTool.delay(500);
			// shouTool.keyPressOne(KeyEvent.VK_CONTROL);
			DSHandle.startDxgj();
			if (DataSave.被监视 && DataSave.被转移) {// 1625 48 1627,49,1635,54
				listener_image = shouTool.截取屏幕(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 40, DataSave.SCREEN_Y + 48,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 38, DataSave.SCREEN_Y + 50);
			}
			long waitTime = DataSave.dy.getDyWaitTime();
			long numTime = System.currentTimeMillis();
			while (isTrue) {
				if(stop_time!=0&&System.currentTimeMillis()-stop_time>0) {
					isTrue=false;
					return;
				}
				int liang = Integer.parseInt(DataSave.deBug.调整姿势的前置时间.getText().trim());
				if (liang / 2 > 0)
					shouTool.delay(liang / 2);
				shouTool.mousePressOne(InputEvent.BUTTON3_MASK);
				if (liang / 2 > 0)
					shouTool.delay(liang / 2);

				while (System.currentTimeMillis() - numTime < waitTime && isTrue) {
					if (isTrue && DataSave.工人啤酒 && DSHandle.EatGRPJ()) {
					}
					shouTool.delay(5000);
				}
				if (!isTrue)
					return;
				if (isTrue && DataSave.工人啤酒 && DSHandle.EatGRPJ()) {
				}

				while (isTrue) {
					// 查找钓鱼50按钮
					if (isTrue && !findDybutton(-1)) {
						shouTool.delay(50);
					}
					if (!isTrue)
						return;
					// 发送一个按键给程序
					liang = Integer.parseInt(DataSave.deBug.钓鱼空格的按下时间长度.getText().trim());
					if (liang <= 0)
						liang = 0;
					shouTool.keyPressOne(KeyEvent.VK_SPACE);
					if (!isTrue)
						return;
					MonitorBool();
					shouTool.delay(3500);
					int i = Integer.parseInt(DataSave.deBug.打开背包的前置时间.getText().trim());
					if (i > 0)
						shouTool.delay(i);
					if (isTrue && findDybutton(3)) {
						if (DataSave.更换鱼竿 && HuanYG()) {
							continue;
						}
					}
					break;
				}

				DataSave.dy.textField.setText("额外操作");
				long time = System.currentTimeMillis();
				while (isTrue && System.currentTimeMillis() - time < 4000) {
					if (ds_bool) {
						DSHandle.start(null);
					}
					shouTool.delay(1000);
					if (ds_bool) {
						DSHandle.stop();
					}
					if (TCPModel.sendinfo != null && !TCPModel.sendinfo.trim().equals("")) {
						DSHandle.sendPCInfo(TCPModel.sendinfo.trim());
						TCPModel.sendinfo = null;
					}
					MonitorBool();
					// 判断GM是否在询问
					// GM_XWBool();
					// 判断周围是否有人
					if (System.currentTimeMillis() - time > 7000)
						break;
					DSHandle.AutoCheckIn();
					if (!DataSave.白) {
						if (FishModel.start()) {
							// if (DataSave.被击换线 && bj != null) {
							// System.out.println("关闭被击换线");
							// bj.close();
							// }
						}
					}
					boolean bool = false;
					while (FishModel.isClose() && isTrue) {
						System.out.println("等待查看鱼价结束");
						shouTool.delay(1000);
						bool = true;
					}
					if (bool) {
						shouTool.delay(1000);
						while (!DSHandle.AutoCheckIn() && isTrue) {
							shouTool.delay(1000);
						}
						// shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
						// shouTool.delay(500);
						// shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
					}
					if (System.currentTimeMillis() - time > 7000)
						break;

				}
				if (!isTrue)
					return;
				// if (DataSave.被击换线 && (bj == null || !bj.isAlive())) {
				// bj = new bjThread();
				// bj.start();
				// }

				DSHandle.AutoCheckIn();
				shouTool.delay(500);
				if (!isTrue)
					return;
				if (isTrue && !findDybutton(-1)) {
					DSHandle.AutoCheckIn();
					shouTool.delay(50);
				}
				if (!isTrue)
					return;
				if (DataSave.YSSJ) {
					shouTool.delay((int) (Math.random() * 10 + 5) * 1000);
				}
				liang = Integer.parseInt(DataSave.deBug.按下收鱼空格的前置时间.getText().trim());
				if (liang > 0)
					shouTool.delay(liang);
				shouTool.keyPressOne(KeyEvent.VK_SPACE);
				if (isTrue && findDyBlue()) {
					shouTool.keyPressOne(KeyEvent.VK_SPACE);
				} else {
					shouTool.delay(2000);
					continue;
				}
				if (!isTrue)
					return;
				liang = Integer.parseInt(DataSave.deBug.验证码识别前置时间.getText().trim());
				if (liang > 0)
					shouTool.delay(liang);
				if (isTrue && dyProfect()) {
					num = 0;
					ShouY();
					continue;
				}
				if (!isTrue)
					return;
				liang = Integer.parseInt(DataSave.deBug.验证码识别次数.getText().trim());
				if (liang <= 0)
					liang = 0;
				Point p = findDyYZ(liang);
				if (!isTrue)
					return;
				if (p != null && isTrue)
					if (!writeDyYZ(p)) {
						shouTool.delay(6000);
						continue;
					}
				if (!isTrue)
					return;
				num = 0;
				ShouY();
			}

		}

		int i = 600;
		long time = System.currentTimeMillis();

		private void MonitorBool() {
			if (!DataSave.被监视)
				return;
			// 1660,72,1847,247
			if (System.currentTimeMillis() - time < 2000) {
				return;
			}
			long gm_time1 = 60 * 1000;
			long gm_time2 = 5 * 60 * 1000;
			TCPModel.bool_listener = true;
			System.out.println("判断周围是否有人");
			time = System.currentTimeMillis();
			shouTool.mouseMove(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - i % (DataSave.SCREEN_WIDTH + 600), DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y);
			i += 600;
			shouTool.delay(500);

			BufferedImage image = shouTool.截取屏幕(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH,
					DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT);
			BufferedImage linTmp = ImgTool.cutImage(image, image.getWidth() - 260, 72, 260 - 73, 247 - 72);

			boolean bool = true;
			if (DataSave.dy.isMan() && shouTool.图中找图(linTmp, "人.bmp", 0.001, 0, 0) != null && isTrue) {
				System.out.println("有人");
				bool = false;
			}
			if (bool && DataSave.监视公会 && shouTool.图中找图EX(linTmp, "工会成员.bmp", 0.07, 0, 0) != null) {

				System.out.println("有公会成员");
				bool = false;
			}
			if (bool) {
				linTmp = ImgTool.cutImage(image, image.getWidth() - 40, 48, 2, 2);
				if (DataSave.被转移 && shouTool.图中找图(linTmp, listener_image, 0.001, 0, 0) == null) {
					System.out.println("被转移");
					bool = false;
				}
			}
			if (bool) {
				linTmp = ImgTool.cutImage(image, 62, image.getHeight() - 200, 350 - 62, 150);

				if (DataSave.监听对话) {
					if (shouTool.图中找色(linTmp, 0.15, 40, "3AFF41", 0, 0) != null) {
						bool = false;
						System.out.println("发现GM对话色");
					}
					if (shouTool.图中找图(linTmp, "GM.bmp", 0.15, 0, 0) != null) {
						bool = false;
						gm_time1 = 2 * 60 * 60 * 1000;
						gm_time2 = 2 * 60 * 60 * 1000;
						System.out.println("发现GM字样");
					}
				}
			}
			if (bool) {
				return;
			}
			if (!isTrue)
				return;
			System.out.println("有异常");
			long time = System.currentTimeMillis();
			long time2 = 0;
			long time3 = time;
			while (isTrue) {
				DSHandle.AutoCheckIn();
				bool = false;
				if (DataSave.dy.isMan() && (shouTool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 260, DataSave.SCREEN_Y + 72,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 73, DataSave.SCREEN_Y + 247, 0.001, "人.bmp") != null && isTrue)) {
					bool = true;
				}
				if (!bool && DataSave.监视公会 && shouTool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 260, DataSave.SCREEN_Y + 72,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 73, DataSave.SCREEN_Y + 247, 0.05, "工会成员.bmp") != null) {
					bool = true;
				}
				if (!bool && DataSave.被转移 && shouTool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 40, DataSave.SCREEN_Y + 48,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 38, DataSave.SCREEN_Y + 50, 0, listener_image) == null) {
					System.out.println("被转移");
					bool = true;
				}
				// if (!bool && DataSave.监听对话 && DataSave.监听加强 &&
				// shouTool.区域找色(62 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT
				// - 400 + DataSave.SCREEN_Y,
				// 350 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT - 50 +
				// DataSave.SCREEN_Y, 0.36, 40,
				// "!000000|E7E7E7|0FF1F6|919191|545454") != null) {
				// System.out.println("有私聊1");
				// bool = true;
				// } else
				if (!bool && DataSave.监听对话 && shouTool.区域找色(62 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT - 200 + DataSave.SCREEN_Y, 350 + DataSave.SCREEN_X,
						DataSave.SCREEN_HEIGHT - 50 + DataSave.SCREEN_Y, 0.36, 40, "44F547") != null) {
					System.out.println("有私聊2");
					bool = true;
				}
				if (!DataSave.被监视)
					return;
				if (!bool) {
					shouTool.mouseMove(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - i % (DataSave.SCREEN_WIDTH + 600),
							DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y);
					i %= (DataSave.SCREEN_WIDTH);
					i += 600;
					shouTool.delay(100);
				} else {
					time = System.currentTimeMillis();
				}
				if (TCPModel.sendinfo != null && !TCPModel.sendinfo.trim().equals("")) {
					DSHandle.sendPCInfo(TCPModel.sendinfo.trim());
					TCPModel.sendinfo = null;
				}
				if (System.currentTimeMillis() - time2 > 10 * 1000) {
					time2 = System.currentTimeMillis();
					if (DataSave.监视声音) {
						// 播放音乐
						DSHandle.播放音乐();
						// System.out.println("\u0007");
					}
					TCPModel.sendManListener();
				}
				if (System.currentTimeMillis() - time > gm_time1) {
					break;
				}
				if (System.currentTimeMillis() - time3 > gm_time2) {
					break;
				}
				if (bool)
					shouTool.delay(2000);
			}
			TCPModel.sendStopManListener();
			DSHandle.关闭播放();
			if (!isTrue) {
				return;
			}
			if (DataSave.被转移)
				listener_image = shouTool.截取屏幕(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 40, DataSave.SCREEN_Y + 48,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 38, DataSave.SCREEN_Y + 50);
			System.out.println("人离开");
		}

		BufferedImage listener_image = null;

		private boolean dyProfect() {
			int w = DataSave.SCREEN_WIDTH / 2;
			if (shouTool.区域找色(w - 92 + DataSave.SCREEN_X, 479 + DataSave.SCREEN_Y, w + 88 + DataSave.SCREEN_X, 516 + DataSave.SCREEN_Y, 0.1, 50,
					"86DFEC") != null) {
				return true;
			}
			return false;
		}

		private BufferedImage[] yuGan = null;

		private boolean HuanYG() {
			if (yuGan == null)
				yuGan = DataSave.custonimg.getyuGan();
			if (yuGan == null)
				return false;
			DataSave.dy.textField.setText("更换鱼竿");
			// Point p = null;
			// // 1536,131,1536,138 1531
			// p = shouTool.区域找色(DataSave.SCREEN_WIDTH - 384 - 2 - 2 +
			// DataSave.SCREEN_X, 131 + DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH
			// - 383 + DataSave.SCREEN_X,
			// 137 + DataSave.SCREEN_Y, 0.15, 25, "000000");

			shouTool.keyPressOne(KeyEvent.VK_I);
			i = Integer.parseInt(DataSave.deBug.检测鱼竿的前置时间.getText().trim());
			if (i > 0)
				shouTool.delay(i);
			double d = Double.parseDouble(DataSave.deBug.鱼竿检测准确度.getText().trim());
			if (d <= 0)
				d = 0;
			for (int j = 0; j < yuGan.length; j++) {
				Point p = shouTool.区域找图(DataSave.SCREEN_WIDTH - 473 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 - 367 + DataSave.SCREEN_Y,
						DataSave.SCREEN_WIDTH - 5 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 + 366 + DataSave.SCREEN_Y, d, yuGan[j]);
				if (p != null) {
					shouTool.mouseMove(p.x + yuGan[j].getWidth() / 2, p.y + yuGan[j].getHeight() / 2);
					i = Integer.parseInt(DataSave.deBug.更换鱼竿的前置时间.getText().trim());
					if (i > 0)
						shouTool.delay(i);
					shouTool.mousePressOne(InputEvent.BUTTON3_MASK);
					break;
				}
			}
			if (DataSave.丢弃鱼竿) {
				diuqiYG();
			}
			i = Integer.parseInt(DataSave.deBug.关闭背包的前置时间.getText().trim());
			if (i > 0)
				shouTool.delay(i);
			shouTool.keyPressOne(KeyEvent.VK_I);
			return true;

		}

		// 判断坏鱼竿用的
		private void diuqiYG() {
			DataSave.dy.textField.setText("丢弃鱼竿");
			double d = Double.parseDouble(DataSave.deBug.鱼竿检测准确度.getText().trim());
			if (d <= 0)
				d = 0;
			Point p = shouTool.区域找图(DataSave.SCREEN_WIDTH - 420 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 - 222 + DataSave.SCREEN_Y,
					DataSave.SCREEN_WIDTH - 16 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 + 169 + DataSave.SCREEN_Y, d, "坏鱼竿.bmp");
			if (p != null) {
				shouTool.delay(200);
				shouTool.mouseMove(p.x, p.y);
				shouTool.delay(200);
				shouTool.mousePress(InputEvent.BUTTON1_MASK);
				shouTool.delay(200);
				// 1859 858
				shouTool.mouseMove(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 61, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y + 318);
				shouTool.delay(200);
				shouTool.mouseRelease(InputEvent.BUTTON1_MASK);
				shouTool.delay(200);
				shouTool.mousePressOne(InputEvent.BUTTON1_MASK);
				shouTool.delay(200);
				shouTool.keyPressOne(KeyEvent.VK_ENTER);
			}
		}

		private void ShouY() {
			DataSave.dy.textField.setText("鱼判断阶段");
			int i = Integer.parseInt(DataSave.deBug.鱼种类判断前置时间.getText().trim());
			int i2 = Integer.parseInt(DataSave.deBug.收鱼前置时间.getText().trim());
			if (i < 0)
				i = 0;
			shouTool.delay(i + 100);
			if (System.currentTimeMillis() > yh_time)
				if (DataSave.白) {
					System.out.println("捡起白鱼");
					shouTool.delay(i2);
					shouTool.keyPressOne(KeyEvent.VK_R);
					return;
				}
			num = 0;
			yuPanduan(i2);
		}

		private Tool shouTool = null;
		private BufferedImage[] dytu = null;

		private void yuPanduan(int time) {
			if (num == 15)
				return;// 1530 587 1730 650
			int w = DataSave.SCREEN_WIDTH - 385 + DataSave.SCREEN_X;
			int h = DataSave.SCREEN_HEIGHT - 486 + DataSave.SCREEN_Y;
			int w2 = DataSave.SCREEN_WIDTH - 190 + DataSave.SCREEN_X;
			int h2 = h + 10;
			BufferedImage image = shouTool.截取屏幕(w, h, w2, h2);
			Point t = null;
			if (System.currentTimeMillis() > yh_time) {
				if (DataSave.黄) {
					t = shouTool.图中找图EX(image, "黄.bmp", 0.07, w, h);
					if (t != null) {
						// shouTool.保存图片(image, Other.getShortUuid() +
						// "_黄.bmp");
						System.out.println("黄： " + t);
						shouY(t, time, 0);
						yuPanduan(200);
						return;
					}
				}
				if (DataSave.红) {
					t = shouTool.图中找图EX(image, "红.bmp", 0.07, w, h);
					if (t != null) {
						// shouTool.保存图片(image, Other.getShortUuid() +
						// "_黄.bmp");
						System.out.println("红： " + t);
						shouY(t, time, 0);
						yuPanduan(200);
						return;
					}
				}
				if (DataSave.绿) {
					t = shouTool.图中找图EX(image, "绿.bmp", 0.07, w, h);
					if (t != null) {
						// shouTool.保存图片(image, Other.getShortUuid() +
						// "_绿.bmp");
						System.out.println("绿： " + t);
						shouY(t, time, 0);
						yuPanduan(200);
						return;
					}
				}
				if (DataSave.蓝) {
					t = shouTool.图中找图EX(image, "蓝.bmp", 0.07, w, h);
					if (t != null) {
						// shouTool.保存图片(image, Other.getShortUuid() +
						// "_蓝.bmp");
						System.out.println("蓝： " + t);
						shouY(t, time, 0);
						yuPanduan(200);
						return;
					}
				}
			}
			h = h - 5;
			h2 = h + 48 + 10;
			image = shouTool.截取屏幕(w, h, w2, h2);
			if (DataSave.碎片) {
				t = shouTool.图中找图(image, "碎片.bmp", 0.12, w, h);
				if (t != null) {
					// shouTool.保存图片(image, Other.getShortUuid() + "_碎片.bmp");
					System.out.println("碎片.bmp： " + t);
					shouY(t, time, 1);
					yuPanduan(200);
					return;
				}
			}
			if (DataSave.银钥匙) {
				t = shouTool.图中找图(image, "银钥匙.bmp", 0.12, w, h);
				if (t != null) {
					// shouTool.保存图片(image, Other.getShortUuid() + "_银钥匙.bmp");
					System.out.println("银钥匙.bmp: " + t);
					shouY(t, time, 1);
					yuPanduan(200);
					return;
				}
			}
			long temp_time = System.currentTimeMillis();
			if (temp_time > yh_time) {
				shouTool.保存图片(image, temp_time + Other.getVerifyCode(4) + ".bmp");
				// 其他
				if (dytu == null)
					dytu = DataSave.custonimg.getyuTu();
				if (dytu != null) {
					for (int i = 0; i < dytu.length; i++) {
						t = shouTool.图中找图(image, dytu[i], 0.15, w, h);
						if (t != null) {
							yiW = dytu[i].getWidth() / 2;
							yiH = dytu[i].getHeight() / 2;
							shouY(t, time, 1);
							yuPanduan(200);
							return;
						}
					}
					return;
				}
			}
		}

		private int yiW = 0, yiH = 0;
		int num = 0;

		private void shouY(Point t, int time, int type) {
			num++;
			if (num == 14) {
				if (DataSave.BMGJ) {
					if (DataSave.包满提醒) {
						DSHandle.播放音乐();
					} else
						CmdModel.关闭电脑();
				}
				return;
			}

			DataSave.dy.textField.setText("拾取阶段");
			shouTool.delay(time);
			if (!GameListenerThread.DJNI.isCorsurShow())
				shouTool.keyPressOne(KeyEvent.VK_CONTROL);
			shouTool.delay(200);
			if (num == 4 || num == 8 || num == 12 || num == 16) {
				if (!GameListenerThread.DJNI.isCorsurShow())
					shouTool.keyPressOne(KeyEvent.VK_CONTROL);
				shouTool.delay(200);
			}
			Point p = null;
			if (type == 0)
				p = new Point(t.x + 20, t.y + 20);
			else
				p = new Point(t.x + yiW / 2, t.y + yiH / 2);
			shouTool.mouseMove(p.x, p.y);
			int x1 = Integer.parseInt(DataSave.deBug.拾取鱼鼠标右键点击前置时间.getText().trim());
			shouTool.delay(x1);
			shouTool.mousePressOne(InputEvent.BUTTON3_MASK);
			shouTool.delay(500);// 1608,619,1805,645
			if (GameUIModel.FindXX2(shouTool, p.x + 150, p.y, p.x + 230, p.y + 50) != null) {
				shouTool.keyPressOne(KeyEvent.VK_F);
				shouTool.delay(200);
				shouTool.keyPressOne(KeyEvent.VK_ENTER);
			}
			x1 = Integer.parseInt(DataSave.deBug.拾取鱼鼠标右键点击后置时间.getText().trim());
			shouTool.delay(x1);
			shouTool.mouseMove(t.x - 100, t.y - 100);
			shouTool.delay(200);
			if (GameListenerThread.DJNI.isCorsurShow())
				shouTool.keyPressOne(KeyEvent.VK_CONTROL);
		}

		// 输入验证码
		private boolean writeDyYZ(Point p) {
			DataSave.dy.textField.setText("验证码识别2");
			// if (DataSave.DYSW && DataSave.GMXW && GM_Bool && Math.random() >
			// 0.5) {
			// return false;
			// }
			return shouTool.paint(p.x - 50, p.y - 40, p.x + 300, p.y - 30);
		}

		private Point findDyYZ(int i) {
			DataSave.dy.textField.setText("验证码识别1");
			double d = Double.parseDouble(DataSave.deBug.验证码识别准确度.getText().trim());
			if (d <= 0)
				d = 0;
			int w = DataSave.SCREEN_WIDTH / 2 - 179;
			long time = System.currentTimeMillis();
			while (isTrue && System.currentTimeMillis() - time < 1000 * 5) {
				// while (isTrue && i != 0 || i == -1) {
				Point p = shouTool.区域找图(w + DataSave.SCREEN_X, 389 + DataSave.SCREEN_Y, w + 90 + DataSave.SCREEN_X, 471 + DataSave.SCREEN_Y, d,
						!DataSave.服务器.equals("台服") ? "验证码时间条.bmp" : "验证码时间条2.bmp");
				if (p != null) {
					return p;
				}
				shouTool.delay(25);
				if (!isTrue)
					return null;
			}
			return null;
		}

		private boolean isSpace = false;

		private boolean findDyBlue() {
			DataSave.dy.textField.setText("蓝条识别");
			isSpace = false;
			final int w = DataSave.SCREEN_WIDTH / 2;
			Thread blue = new Thread(new Runnable() {
				@Override
				public void run() {
					int ci = Integer.parseInt(DataSave.deBug.收鱼蓝色条扫描次数.getText().trim());
					if (ci <= 0)
						ci = 0;
					double d = Double.parseDouble(DataSave.deBug.收鱼蓝色条准确度.getText().trim());
					if (d <= 0)
						d = 0;
					int liang = Integer.parseInt(DataSave.deBug.收鱼蓝色条扫描像素数量.getText().trim());
					if (liang <= 0)
						liang = 0;
					long time = System.currentTimeMillis();
					Tool tool = new Tool();
					while (isTrue && System.currentTimeMillis() - time < 5000 && !isSpace) {
						Point p = tool.区域找色(w + 34 + DataSave.SCREEN_X, 390 + DataSave.SCREEN_Y, w + 43 + DataSave.SCREEN_X, 430 + DataSave.SCREEN_Y, d, liang,
								"A18659");
						if (p != null) {
							isSpace = true;
							return;
						}
						shouTool.delay(20);
						if (!isTrue)
							return;
					}
					isSpace = true;
					return;
				}
			});

			Thread red = new Thread(new Runnable() {

				@Override
				public void run() {
					double d = Double.parseDouble(DataSave.deBug.收鱼蓝色条红准确度.getText().trim());
					if (d <= 0)
						d = 0;
					int liang = Integer.parseInt(DataSave.deBug.红按下空格前置时间.getText().trim());
					if (liang <= 0)
						liang = 220;
					Tool tool = new Tool();
					while (isTrue && !isSpace) {
						Point p = tool.区域找图(w - 190 + DataSave.SCREEN_X, 367 + DataSave.SCREEN_Y, w - 98 + DataSave.SCREEN_X, 458 + DataSave.SCREEN_Y, d,
								"蓝色条.bmp");
						if (p != null) {
							Other.sleep(liang);
							isSpace = true;
							return;
						}
						shouTool.delay(20);
						if (!isTrue)
							return;
					}
					isSpace = false;
				}
			});
			blue.start();
			red.start();
			while (!isSpace && blue.isAlive() && red.isAlive()) {
				if (!isTrue)
					return false;
			}
			return isSpace;
		}

		private boolean findDybutton(int i) {
			DataSave.dy.textField.setText("查找钓鱼空格按钮");
			System.out.println("查找钓鱼空格按钮");
			int shuliang = Integer.parseInt(DataSave.deBug.关闭背包的前置时间.getText().trim());
			if (shuliang <= 0)
				shuliang = 0;
			double d = Double.parseDouble(DataSave.deBug.钓鱼空格的准确度.getText().trim());
			if (d <= 0)
				d = 0;
			int w = (int) (DataSave.SCREEN_WIDTH - 141) / 2 + DataSave.SCREEN_X;
			int h = 147 + DataSave.SCREEN_Y;
			while (isTrue && i != 0 || i == -1) {
				Point p = shouTool.区域找色(w, h, w + 141, h + 60 * 2, d, shuliang, "006E95");
				if (p != null) {
					System.out.println("找到空格按钮");
					return true;
				}
				if (i == -1)
					i = -1;
				else
					i--;
				DSHandle.AutoCheckIn();
				if (TCPModel.sendinfo != null && !TCPModel.sendinfo.trim().equals("")) {
					DSHandle.sendPCInfo(TCPModel.sendinfo.trim());
					TCPModel.sendinfo = null;
				}
				// GM_XWBool();
				MonitorBool();
				shouTool.delay(200);
				if (!isTrue)
					return false;
			}
			return false;
		}

		public void close() {
			isTrue = false;
			DSHandle.关闭播放();
		}
	}

	public static boolean isRun() {
		return isTrue;
	}
}
