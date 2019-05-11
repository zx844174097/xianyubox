package com.mugui.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.GameListenerThread;
import com.mugui.windows.Tool;

public class MYHandle {
	private static boolean isTrue = false;
	private static MYThread mythread = null;
	public static boolean ds_bool = false;

	public static void start() {
		if (isTrue)
			return;
		if (!HsFileHandle.isRunModel()) {
			return;
		}
		DSHandle.startDxgj();
		isTrue = true;
		mythread = new MYThread();
		// mythread.tool.delay(500);
		// mythread.markPlace();
		mythread.start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isTrue) {
					TcpBag bag = new TcpBag();
					bag.setBag_id(TcpBag.ERROR);
					UserBean userBean = new UserBean();
					userBean.setCode("my");
					bag.setBody(userBean.toJsonObject());
					TCPModel.SendTcpBag(bag);
					int i = 0;
					while (i <= 60) {
						Other.sleep(1000);
						i++;
						if (!isTrue)
							return;
					}
				}
			}
		}).start();
	}

	public static void stop() {
		isTrue = false;
	}

	public static void testfindMache() {
		isTrue = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				mythread = new MYThread();
				mythread.tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, 50, InputEvent.BUTTON1_MASK);
				mythread.tool.delay(500);
				mythread.findMache();
			}
		}).start();
	}

	public static boolean isRun() {
		return isTrue;
	}

	private static class MYThread extends Thread {
		private Tool tool = new Tool();;

		@Override
		public void run() {
			if (DSHandle.isRun()) {
				DSHandle.stop();
				ds_bool = true;
				System.out.println("定时关闭");
			}
			backCKlasttime = System.currentTimeMillis();
			while (isTrue) {
				long time = System.currentTimeMillis();
				while (isTrue && System.currentTimeMillis() - time < 60 * 1000) {
					tool.delay(500);
					if (DataSave.my.isBackCK())
						if (DataSave.my.getYCid() == DataSave.my.getPlaceNum() && System.currentTimeMillis() - backCKlasttime >= DataSave.my.getBackCKTime()) {
							MoneyToCK();
							backCKlasttime = System.currentTimeMillis();
						}
					if (!isTrue)
						return;
					tool.delay(500);
					DSHandle.AutoCheckIn();
					tool.delay(500);
					tool.keyPressOne(KeyEvent.VK_R);
					if (!isTrue)
						return;
					if (!findMaoyi()) {
						tool.keyPressOne(KeyEvent.VK_ESCAPE);
						tool.delay(1500);
						DSHandle.AutoCheckIn();
						findNPC();
						tool.delay(1500);
						continue;
					}
					if (!isTrue)
						return;
					// 选购物品
					boolean bool = shopping();
					if ((!bool || DataSave.my.isHx()) && DataSave.MYHX) {
						if (!isTrue)
							return;
						tool.delay(1000);
						System.out.println("换线开始");
						DSHandle.hx();
						DataSave.my.reHx();
						System.out.println("换线结束");
						time = System.currentTimeMillis();
						image = null;
						// if (bool)
						break;
						// continue;
					}
					if (!isTrue)
						return;
					break;
				}
				tool.delay(1500);
				place_name = DataSave.my.getLastPlace();
				// 前往下一个点，然后吹口哨上马
				markPlace2(place_name);
				tool.keyPressOne(KeyEvent.VK_T);
				tool.delay(1150);
				tool.keyPressOne(KeyEvent.VK_S);
				tool.delay(200);
				while (isTrue && !findMache() && isTrue) {
					tool.delay(500);
					goTomache();
					if (!isTrue)
						return;
					tool.delay(500);
					place_name = DataSave.my.getLastPlace();
					// 前往下一个点，然后吹口哨上马
					markPlace2(place_name);
				}
				// 前往目的地
				if (!isTrue)
					return;
				System.out.println("前往下一个点：" + place_name);
				goLastPlace();

				if (!isTrue)
					return;
				time = System.currentTimeMillis();
				while (isTrue && System.currentTimeMillis() - time < 15 * 1000) {
					Other.sleep(1000);
					if (tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 234, DataSave.SCREEN_Y + 108, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 106,
							DataSave.SCREEN_Y + 204, 0.07, 5, "E5E6FE") == null && isTrue) {
						markPlace2(place_name);
					}
				}
				if (!isTrue)
					return;
				if (ds_bool) {
					DSHandle.start(null);
					tool.delay(2000);
					System.out.println("定时启动");
				}
				if (!isTrue)
					return;
				System.out.println("等待停止");
				waitSleep(1);
				System.out.println("停止");
				if (ds_bool) {
					DSHandle.stop();
					tool.delay(2000);
					System.out.println("定时关闭");
				}
				if (!isTrue)
					return;
				tool.keyPressOne(KeyEvent.VK_T);
				tool.delay(1000);
				tool.keyPressOne(KeyEvent.VK_S);
				tool.delay(3000);
				// System.out.println("重新标记当前点：" + place_name);
				// markPlace(place_name);
				// tool.delay(500);
				// while (isTrue) {
				tool.keyPressOne(KeyEvent.VK_R);
				tool.delay(2000);
				tool.keyPressOne(KeyEvent.VK_W);

				goToNPC();
				// waitSleep();
				if (!isTrue)
					return;
				tool.delay(2000);
				System.out.println("卖出物品");
				buyOther();

			}
		}

		private String npc_name = null;

		private void MoneyToCK() {
			if (!isTrue)
				return;
			npc_name = "仓库";
			gotoNpc();
			if (!isTrue) {
				return;
			}
			// 寻找npc
			npc_name = "仓库";
			openNpc();
			if (!isTrue) {
				return;
			}
			startFHMoney();
			npc_name = "交易所";
			gotoNpc();
			npc_name = "贸易";
			gotoNpc();
		}

		private void startFHMoney() {
			Point bb_xy = getCKPoint();
			// 1532 796
			tool.mouseMovePressOne(bb_xy.x + 63, bb_xy.y + 615, InputEvent.BUTTON1_MASK);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_F);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ENTER);
			tool.delay(500);
			System.out.println("bb_xy:" + bb_xy);
			// 1406 184
			// 1242 740 1380 762
			Point point = tool.区域找图(bb_xy.x - (1406 - 1242), bb_xy.y + (736 - 184), bb_xy.x - (1406 - 1380), bb_xy.y + (772 - 184), 0.15, "仓库钱.bmp");
			if (point != null) {
				tool.mouseMovePressOne(point.x, point.y, InputEvent.BUTTON1_MASK);
				tool.delay(500);
				DSHandle.输入数字("200000");
				tool.delay(500);
				tool.keyPressOne(KeyEvent.VK_ENTER);
			}
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(1500);
		}

		private Point getCKPoint() {
			// 仓库第一格坐标895 283 15 20
			Point bb_xy = null;
			long time = System.currentTimeMillis();
			while (isTrue) {

				bb_xy = GameUIModel.FindXX(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 100, DataSave.SCREEN_Y + 50,
						DataSave.SCREEN_X + DataSave.SCREEN_WIDTH, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 - 100);
				if (bb_xy != null) {
					bb_xy.x -= 477;
					bb_xy.y -= 5;
					break;
				}
				System.out.println("bb_xy:" + bb_xy);
				if (System.currentTimeMillis() - time > 3000) {
					time = System.currentTimeMillis();
					Point p = tool.区域找图(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 550, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 260,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 195, 0.07, npc_name + ".bmp");
					if (p != null) {
						tool.mouseMovePressOne(p.x + 340 + 20, p.y + 10, InputEvent.BUTTON1_MASK);
					} else {
						DSHandle.AutoCheckIn();
						openNpc();
						tool.delay(1000);
					}
				}
				tool.delay(1000);
			}
			System.out.println("仓库坐标:" + bb_xy);
			return bb_xy;
		}

		private void openNpc() {

			tool.delay(1000);
			findNPC();
			System.out.println("打开" + npc_name + "npc");
			tool.keyPressOne(KeyEvent.VK_R);
			Point p = null;
			long time = System.currentTimeMillis();
			int num = 0;
			do {
				tool.delay(100);
				if (System.currentTimeMillis() - time > 3000) {
					tool.keyPressOne(KeyEvent.VK_ESCAPE);
					tool.delay(1000);
					if (!DSHandle.AutoCheckIn()) {
						tool.keyPressOne(KeyEvent.VK_S);
						tool.delay(1000);
					}
					System.out.println("未能发现" + npc_name + "信息");
					num++;
					if (num > 3) {
						gotoNpc();
						num = 0;
					}
					findNPC();
					tool.keyPressOne(KeyEvent.VK_R);
					time = System.currentTimeMillis();

				}
			} while ((p = tool.区域找图EX(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 550, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 260,
					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 195, 0.07, npc_name + ".bmp")) == null
					&& isTrue);
			if (!isTrue)
				return;
			// 884, 868
			tool.delay(1000);
			tool.mouseMovePressOne(p.x + 340 + 20, p.y + 10, InputEvent.BUTTON1_MASK);
			tool.delay(1000);
			System.out.println("寻找得到" + npc_name + "信息");

		}

		private void gotoNpc() {
			for (int i = 0; i < 2; i++) {
				tool.delay(500);
				DSHandle.AutoCheckIn();
			}
			if (!isTrue) {
				return;
			}
			if (!GameListenerThread.DJNI.isCorsurShow()) {
				tool.keyPressOne(KeyEvent.VK_CONTROL);
				tool.delay(200);
			}
			Point p = null;
			long time = System.currentTimeMillis();
			while (isTrue) {
				// 1403,63,1534,165
				p = tool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 722, DataSave.SCREEN_Y + 63, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 385,
						DataSave.SCREEN_Y + 350, 0.10, "贸易" + npc_name + ".bmp");
				if (p != null)
					break;
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1566), DataSave.SCREEN_Y + 17, InputEvent.BUTTON1_MASK);
				tool.delay(2000);
				if (!isTrue) {
					return;
				}
				if (System.currentTimeMillis() - time > 10 * 1000) {
					tool.keyPressOne(KeyEvent.VK_CONTROL);
					tool.delay(200);
				}
			}
			if (!isTrue) {
				return;
			}
			tool.mouseMovePressOne(p.x + 5, p.y + 5, InputEvent.BUTTON3_MASK);
			if (!isTrue)
				return;
			tool.delay(1000);
			// tool.keyPressOne(KeyEvent.VK_ESCAPE);
			// tool.delay(5000);
			// tool.delay(230);
			// if (WinJNI.DJNI.isCorsurShow()) {
			// tool.keyPressOne(KeyEvent.VK_CONTROL);
			// tool.delay(700);
			// }
			if (!isTrue)
				return;

			waitSleep();
			if (!isTrue) {
				return;
			}
			if (!GameListenerThread.DJNI.isCorsurShow()) {
				tool.keyPressOne(KeyEvent.VK_CONTROL);
				tool.delay(200);
			}
			p = null;
			time = System.currentTimeMillis();
			while (isTrue) {
				// 1403,63,1534,165
				p = tool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 722, DataSave.SCREEN_Y + 63, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 385,
						DataSave.SCREEN_Y + 350, 0.10, "贸易" + npc_name + ".bmp");
				if (p != null)
					break;
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1566), DataSave.SCREEN_Y + 17, InputEvent.BUTTON1_MASK);
				tool.delay(2000);
				if (!isTrue) {
					return;
				}
				if (System.currentTimeMillis() - time > 10 * 1000) {
					tool.keyPressOne(KeyEvent.VK_CONTROL);
					tool.delay(200);
				}
			}
			if (!isTrue) {
				return;
			}
			tool.mouseMovePressOne(p.x + 5, p.y + 5, InputEvent.BUTTON3_MASK);
			if (!isTrue)
				return;
			tool.delay(1000);
			if (DataSave.my.isDctNPC()) {
				int i = DataSave.my.getDctNpc_n();
				while (i > 0) {
					if (!GameListenerThread.DJNI.isCorsurShow())
						tool.keyPressOne(KeyEvent.VK_CONTROL);
					tool.delay(500);
					tool.mouseMovePressOne(p.x + 5, p.y + 5, InputEvent.BUTTON3_MASK);
					tool.delay(DataSave.my.getToNPCTime());
					tool.keyPressOne(KeyEvent.VK_S);
					tool.delay(200);
					tool.keyPressOne(KeyEvent.VK_S);
					tool.delay(500);
					i--;
				}
			}
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(700);
			if (GameListenerThread.DJNI.isCorsurShow()) {
				tool.keyPressOne(KeyEvent.VK_CONTROL);
				tool.delay(700);
			}
		}

		private void waitSleep() {

			int time = 0;
			while (isTrue) {
				System.out.println("判断是否停止");
				BufferedImage image = tool.截取屏幕(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 51, DataSave.SCREEN_Y + 43,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 32, DataSave.SCREEN_Y + 141);
				Other.sleep(2500);
				if (tool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 51, DataSave.SCREEN_Y + 43, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 32,
						DataSave.SCREEN_Y + 141, 0.01, image) != null) {
					return;
				}
				if (DataSave.my.isBjhx()) {
					if (tool.点颜色比较(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X + 57, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 102, "0000CC") > 0.07
							&& !isTrue) {
						tool.keyPressOne(Tool.getkeyCode(DataSave.my.getBjhxButton()));
					}
				}
				if (!isTrue) {
					return;
				}
				time += 2000;

				if (DSHandle.isEatGRPJ()) {
					System.out.println("吃啤酒");
					DSHandle.EatGRPJ();
				}
				System.out.println("关闭阻挡界面");
				DSHandle.AutoCheckIn();
				int i = 0;
				if (time > 60 * 1000) {
					System.out.println("视角旋转");
					tool.mouseMove((i * 600) % (DataSave.SCREEN_WIDTH + 600), (int) (DataSave.SCREEN_HEIGHT * 0.4) * 1 + DataSave.SCREEN_Y);
					i++;
					time = 0;
				}
			}

		}

		private long backCKlasttime = System.currentTimeMillis();

		private void markPlace2(String place_name2) {

			boolean temp = DataSave.ESC;
			DataSave.ESC = true;
			for (int i = 0; i < 4; i++) {
				tool.delay(500);
				if (DSHandle.AutoCheckIn())
					break;
			}
			DataSave.ESC = temp;
			// int i = DataSave.SCREEN_WIDTH;
			System.out.println("打开地图");
			BufferedImage te = DSHandle.getLineTeImg();
			tool.keyPressOne(KeyEvent.VK_M);
			long time = System.currentTimeMillis();
			while (System.currentTimeMillis() - time < 6000 && isTrue) {
				tool.delay(2500);
				if (te == null)
					break;
				if (tool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 200, DataSave.SCREEN_Y + 2, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 4,
						DataSave.SCREEN_Y + 28, 0.07, te) == null) {
					break;
				}
				tool.keyPressOne(KeyEvent.VK_M);
			}
			// 988 880 1440 900
			while (isTrue) {
				while (isTrue) {
					tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 452, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 20,
							InputEvent.BUTTON1_MASK);
					tool.delay(2000);
					if (image == null) {
						// 1178,78,1565,762
						mark = FindNPCMark1_2();
					} else {
						mark = FindNPCMark2_2();
					}
					if (!isTrue)
						return;
					tool.delay(500);
					tool.mouseMovePressOne(mark.x + 100, mark.y + 10, InputEvent.BUTTON1_MASK);
					DSHandle.粘贴名字(place_name);
					if (GameUIModel.Find公共对话框(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 138, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 212,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 100, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 138) == null) {
						break;
					} else {
						tool.keyPressOne(KeyEvent.VK_ENTER);
					}
					tool.delay(500);
				}
				if (!isTrue)
					return;
				tool.delay(700);
				if (image == null) {
					// 提取特征码坐标集合//1200 747 1251 576 1288 593
					image = tool.得到特征点集合图(mark.x + 58, mark.y - 167, mark.x + 95, mark.y - 154, 0.13, "EEEEEE");
					mark_1 = new Point(58, -167);
					Point point = tool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 722, DataSave.SCREEN_Y + 63,
							DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 385, DataSave.SCREEN_Y + 350, 0.10, "贸易贸易.bmp");
					mark_2 = new Point(point.x - mark.x, point.y - mark.y);
					if (image != null)
						;
					else {
						continue;
					}
				}

				if (!isTrue)
					return;
				if (mark == null) {
					continue;
				}
				mark = new Point(mark.x + 58, mark.y - 167);
				// lin_mark = new Point(mark.x + 50, mark.y + 80);
				time = System.currentTimeMillis();
				boolean bool = true;
				tool.delay(200);
				do {
					tool.mouseMovePressOne(mark.x + 20 + (int) (Math.random() * 10 - 5), mark.y + 4, InputEvent.BUTTON1_MASK);
					tool.delay(2000);
					if (System.currentTimeMillis() - time > 10 * 1000) {
						bool = false;
						break;
					}
				} while (tool.区域找图EX(mark.x - 20, mark.y - 15, mark.x + 60, mark.y + 15, 0.03, this.image) != null && isTrue);
				if (!isTrue) {
					return;
				}
				if (bool)
					break;
			}
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(2000);

		}

		private Point FindNPCMark2_2() {

			long time = System.currentTimeMillis();
			long time3 = System.currentTimeMillis();
			Rectangle rectangle = new Rectangle(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 750, DataSave.SCREEN_Y + 84,
					DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 326, DataSave.SCREEN_Y + 848);

			while (isTrue) {

				if (System.currentTimeMillis() - time > 2 * 1000) {
					tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 452, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 20,
							InputEvent.BUTTON1_MASK);
					tool.delay(1500);
					time = System.currentTimeMillis();
				}

				BufferedImage image = tool.截取屏幕(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
				tool.保存图片(image, "NPC搜索区域.bmp");
				Point p1 = tool.图中找图(image, "贸易贸易.bmp", 0.10, rectangle.x, rectangle.y);
				if (p1 != null) {
					p1.x = p1.x - mark_2.x;
					p1.y = p1.y - mark_2.y;
				}
				Point p2 = tool.图中找图EX(image, this.image, 0.03, rectangle.x, rectangle.y);
				if (p2 != null) {
					p2.x -= mark_1.x;
					p2.y -= mark_1.y;
				}
				Point p3 = tool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 700, DataSave.SCREEN_Y + 676,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 600, DataSave.SCREEN_Y + 770, 0.15, "NPC搜索.bmp");
				// tool.图中找图EX(image, , 0.15, rectangle.x, rectangle.y);
				if (System.currentTimeMillis() - time3 > 30 * 1000) {
					mark = null;
					if (p1 != null) {
						mark = p1;
					} else if (p2 != null) {
						mark = p2;
					} else if (p3 != null) {
						mark = p3;
					}
					if (mark != null)
						break;
					continue;
				}
				Point lin1 = null;
				Point lin2 = null;
				if (p1 != null) {
					lin1 = p1;
				}
				if (lin1 == null && p2 != null) {
					lin1 = p2;
				}
				if (lin1 == null)
					continue;
				if (lin1 != p2 && p2 != null) {
					lin2 = p2;
				}
				if (lin2 == null && p3 != null) {
					lin2 = p3;
				}
				if (lin2 == null) {
					continue;
				}
				System.out.println("FindNPCMark2_2->" + lin1 + " " + lin2);
				if (Math.abs(lin1.x - lin2.x) <= 5 && Math.abs(lin1.y - lin2.y) <= 5) {
					mark = lin1;
					break;
				}
				if (p2 != null) {
					mark = p2;
					break;
				}

			}

			// while ((mark = tool.区域找图EX(DataSave.SCREEN_WIDTH +
			// DataSave.SCREEN_X - 709, DataSave.SCREEN_Y + 390,
			// DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 326,
			// DataSave.SCREEN_Y + 625, 0.03, image)) == null && isTrue) {
			// tool.delay(500);
			//
			// }
			// if (mark != null)
			// mark.y = mark.y + 157 + 6;
			// else {
			// mark = FindNPCMark1();
			// }
			return mark;
		}

		private Point FindNPCMark1_2() {
			long time = System.currentTimeMillis();
			// long time2 = System.currentTimeMillis();
			Rectangle rectangle = null;
			rectangle = new Rectangle(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 750, DataSave.SCREEN_Y + 725, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 600,
					DataSave.SCREEN_Y + 848);
			// tool.保存截屏(rectangle.x, rectangle.y, rectangle.width,
			// rectangle.height, "FindNPCMark1_2.bmp");
			while ((mark = tool.区域找图EX(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 0.12, "NPC搜索.bmp")) == null && isTrue) {
				tool.delay(500);
				if (System.currentTimeMillis() - time > 2 * 1000) {
					tool.delay(200);
					tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 452, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 20,
							InputEvent.BUTTON1_MASK);
					tool.delay(1500);
					time = System.currentTimeMillis();
				}

			}
			System.out.println(mark);
			return mark;
		}

		private void goToNPC() {
			DSHandle.AutoCheckIn();
			tool.delay(500);
			if (!GameListenerThread.DJNI.isCorsurShow())
				tool.keyPressOne(KeyEvent.VK_CONTROL);
			tool.delay(500);
			while (isTrue) {
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1566), DataSave.SCREEN_Y + 17, InputEvent.BUTTON1_MASK);
				tool.delay(2000);
				if (image == null) {
					// 1178,78,1565,762
					mark = FindNPCMark1();
				} else {
					mark = FindNPCMark2();
				}
				if (!isTrue)
					return;
				if (mark != null) {
					mark.x += mark_2.x;
					mark.y += mark_2.y;
					break;
				} else {
					DSHandle.AutoCheckIn();
					if (!GameListenerThread.DJNI.isCorsurShow())
						tool.keyPressOne(KeyEvent.VK_CONTROL);
					tool.delay(500);
				}
			}
			if (!isTrue)
				return;

			tool.mouseMove(mark.x + 5, mark.y + 5);
			tool.delay(500);
			tool.mousePressOne(InputEvent.BUTTON3_MASK);
			tool.delay(DataSave.my.getToNPCTime());
			tool.keyPressOne(KeyEvent.VK_S);
			tool.delay(500);
			if (DataSave.my.isDctNPC()) {
				int i = DataSave.my.getDctNpc_n();
				while (i > 0) {
					if (!GameListenerThread.DJNI.isCorsurShow())
						tool.keyPressOne(KeyEvent.VK_CONTROL);
					tool.delay(500);
					tool.mouseMovePressOne(mark.x + 5, mark.y + 5, InputEvent.BUTTON3_MASK);
					tool.delay(DataSave.my.getToNPCTime());
					tool.keyPressOne(KeyEvent.VK_S);
					tool.delay(500);
					i--;
				}
			}
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
		}

		private boolean findMaoyi() {
			tool.delay(500);
			Point p = null;
			long time = System.currentTimeMillis();
			System.out.println("查找贸易图标");// 697 829 740 878
			while ((p = tool.区域找图EX(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X - 500, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 251,
					DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X - 100, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 202, 0.07, "贸易.bmp")) == null && isTrue) {
				// Other.sleep(10);
				tool.delay(10);
				if (System.currentTimeMillis() - time > 5 * 1000) {
					System.out.println("未查找到贸易图标1");
					return false;
				}
			}
			tool.delay(500);
			System.out.println(p);
			if ((p = tool.区域找图EX(p.x + 170, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 251, DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X,
					DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 202, 0.12, "贸易.bmp")) == null && isTrue) {
				System.out.println("未查找到贸易图标2");
				return false;
			}
			if (!isTrue)
				return false;
			tool.delay(1000);
			tool.mouseMovePressOne(p.x + 50, p.y + 10, InputEvent.BUTTON1_MASK);
			time = System.currentTimeMillis();
			int n = 0;
			while (GameUIModel.FindXX(tool, DataSave.SCREEN_X, 2 + DataSave.SCREEN_Y, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2,
					100 + DataSave.SCREEN_Y) == null && isTrue) {
				Other.sleep(50);
				if (System.currentTimeMillis() - time > 3 * 1000) {
					tool.mouseMovePressOne(p.x + 50, p.y + 5, InputEvent.BUTTON1_MASK);
					time = System.currentTimeMillis();
					n++;
				}
				if (n >= 2)
					break;
			}
			return true;
		}

		// BufferedImage mache = null;

		private boolean findMache() {
			long time2 = System.currentTimeMillis();
			while (isTrue && System.currentTimeMillis() - time2 < 45 * 1000) {
				tool.keyPressOne(KeyEvent.VK_W);
				tool.delay(200);
				if (!GameListenerThread.DJNI.isCorsurShow())
					tool.keyPressOne(KeyEvent.VK_CONTROL);
				tool.delay(300);
				if (!isTrue)
					return true;
				tool.mouseMovePressOne(DataSave.SCREEN_X + 36, DataSave.SCREEN_Y + 177, InputEvent.BUTTON1_MASK);
				tool.delay(1500);
				if (!isTrue)
					return true;
				if (GameListenerThread.DJNI.isCorsurShow())
					tool.keyPressOne(KeyEvent.VK_CONTROL);
				tool.delay(200);
				// 345,91,1594,419
				int i = DataSave.SCREEN_WIDTH;
				long time = System.currentTimeMillis();
				while (isTrue && System.currentTimeMillis() - time < 6100) {
					DSHandle.AutoCheckIn();
					System.out.println("寻找马车");
					long times = System.currentTimeMillis();
					boolean bool = true;
					while ((tool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH, DataSave.SCREEN_HEIGHT, DataSave.my.getMc_sb(),
							"马车.bmp")) == null && isTrue) {
						if (System.currentTimeMillis() - times > 2000) {
							bool = false;
							break;
						}
					}
					if (!isTrue)
						return true;
					if (bool) {
						System.out.println("开始上马");
						if (!shangma()) {
							System.out.println("上马失败");
							break;
						}
						System.out.println("上马成功");
						return true;
					}
					// 判断是否已在马车上
					if ((tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 96, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 173,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 62, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 158, 0.15, 40, "5CA3AA") == null
							&& tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 96, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 173,
									DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 62, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 158, 0.15, 40,
									"4E8285") == null)
							&& isTrue) {
						tool.mouseMove(i % (DataSave.SCREEN_WIDTH + 600) + DataSave.SCREEN_X, (int) (DataSave.SCREEN_HEIGHT * 0.4) + DataSave.SCREEN_Y);
					} else {
						return true;
					}
					i -= 600;
				}

			}
			return false;
		}

		private void goTomache() {
			// tool.delay(500);
			// tool.keyPressOne(KeyEvent.VK_ESCAPE);
			// tool.delay(500);
			// tool.keyPressOne(KeyEvent.VK_ESCAPE);
			// tool.delay(500);
			boolean temp = DataSave.ESC;
			DataSave.ESC = true;
			for (int i = 0; i < 4; i++) {
				if (DSHandle.AutoCheckIn())
					break;
				tool.delay(500);
			}
			DataSave.ESC = temp;
			if (!GameListenerThread.DJNI.isCorsurShow())
				tool.keyPressOne(KeyEvent.VK_CONTROL);
			tool.delay(200);
			if (!isTrue)
				return;
			Point p1 = null;
			Point p2 = null;
			long time = 0;
			do {
				tool.mouseMovePressOne(DataSave.SCREEN_X + 36, DataSave.SCREEN_Y + 177, InputEvent.BUTTON3_MASK);
				tool.delay(1000);
				time += 500;
				DSHandle.AutoCheckIn();
			} while ((p2 = tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 234, DataSave.SCREEN_Y + 108, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 106,
					DataSave.SCREEN_Y + 204, 0.07, 5, "E5E6FE")) == null
					&& (p1 = tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 234, DataSave.SCREEN_Y + 108,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 106, DataSave.SCREEN_Y + 204, 0.07, 1, "C7A002")) == null
					&& isTrue && time < 2000);
			if (!isTrue)
				return;
			if (p1 != null || p2 != null) {
				tool.keyPressOne(KeyEvent.VK_T);
				tool.delay(200);
			}
			tool.delay(200);
			if (GameListenerThread.DJNI.isCorsurShow())
				tool.keyPressOne(KeyEvent.VK_CONTROL);
			tool.delay(200);

			if (!isTrue)
				return;
			// if (type == 0) {
			// tool.delay(750 + DataSave.my.getToNPCTime());
			// tool.keyPressOne(KeyEvent.VK_S);
			// tool.delay(200);
			// tool.keyPressOne(KeyEvent.VK_S);
			// tool.delay(200);
			// } else {
			// 判断是否已经在马车上

			waitSleep(0);
			// }

		}

		private static Point buy = null;

		private void buyOther() {
			long time2 = System.currentTimeMillis();
			while (isTrue && System.currentTimeMillis() - time2 < 60 * 1000) {
				tool.delay(500);
				tool.keyPressOne(KeyEvent.VK_R);
				if (!isTrue)
					return;
				if (!findMaoyi()) {
					if (!isTrue)
						return;
					tool.keyPressOne(KeyEvent.VK_ESCAPE);
					tool.delay(1500);
					DSHandle.AutoCheckIn();
					findNPC();
					continue;
				}
				// 查找贸易计量
				buy = new Point(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - (1920 - (!DataSave.服务器.equals("steam服") ? 1836 : 1733)),
						DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 1080 + (!DataSave.服务器.equals("steam服") ? 727 : 756));
				tool.delay(3000);
				tool.mouseMovePressOne(buy.x, buy.y, InputEvent.BUTTON1_MASK);
				tool.delay(200);
				tool.mouseMovePressOne(buy.x, buy.y, InputEvent.BUTTON1_MASK);
				tool.delay(200);
				tool.mouseMovePressOne(buy.x, buy.y, InputEvent.BUTTON1_MASK);

				break;
			}
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ENTER);
			tool.delay(2000);
			if (!isTrue)
				return;
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			if (!isTrue)
				return;
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			if (!isTrue)
				return;
		}

		private void findNPC() {
			int i = DataSave.SCREEN_WIDTH;
			long time2 = System.currentTimeMillis();
			while (isTrue && System.currentTimeMillis() - time2 < 10 * 1000) {
				// 345,91,1594,419
				long time = System.currentTimeMillis();
				while (isTrue && System.currentTimeMillis() - time < 2500) {
					Point p = null;
					if ((p = tool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH, DataSave.SCREEN_HEIGHT, DataSave.my.getNpc_sb(),
							"对话.bmp")) != null) {
						// 659,233,815,381
						if (tool.区域找图(p.x - 200 + DataSave.SCREEN_X < 0 ? 0 : p.x - 200, p.y,
								p.x + 200 > DataSave.SCREEN_WIDTH + DataSave.SCREEN_X ? DataSave.SCREEN_WIDTH + DataSave.SCREEN_X : p.x + 200, p.y + 500,
								DataSave.my.getNpc_sb(), "问候.bmp") != null) {
							return;
						}
					} else {
						if (System.currentTimeMillis() - time > 2000) {
							break;
						}
					}
				}
				tool.mouseMove(i % (DataSave.SCREEN_WIDTH + 600) + DataSave.SCREEN_X, (int) (DataSave.SCREEN_HEIGHT * 0.4) + DataSave.SCREEN_Y);
				i -= 600;
			}
		}

		private String place_name = null;

		private void goLastPlace() {
			DSHandle.AutoCheckIn();
			// 标记坐标
			// markPlace(place_name);
			tool.keyPressOne(KeyEvent.VK_T);
			tool.delay(4000);
			if (DataSave.my.isXlxz()) {
				int i = 0;
				while (isTrue && i++ < 8) {
					tool.keyPressOne(KeyEvent.VK_W);
					tool.delay(20);
					tool.keyPressOne(KeyEvent.VK_D);
					tool.delay(20);
				}
			}
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_T);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_T);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_T);
			tool.delay(3000);
		}

		private boolean shangma() {
			long time = System.currentTimeMillis();
			do {
				if (System.currentTimeMillis() - time > 6000)
					return false;
				DSHandle.AutoCheckIn();
				tool.delay(500);
				tool.keyPressOne(KeyEvent.VK_R);
				tool.delay(3000);
				if (!isTrue)
					return false;
			} while (tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 96, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 173,
					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 62, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 158, 0.15, 40, "5CA3AA") == null
					&& tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 96, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 173,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 62, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 158, 0.15, 40, "4E8285") == null);
			return true;
		}

		private Point mark = null;
		// private Point lin_mark = null;
		private BufferedImage image = null;
		private Point mark_1 = null;
		private Point mark_2 = null;

		// private void markPlace(String place_name) {
		// boolean temp = DataSave.ESC;
		// DataSave.ESC = true;
		// for (int i = 0; i < 4; i++) {
		// tool.delay(500);
		// if (DSHandle.AutoCheckIn())
		// break;
		// }
		// DataSave.ESC = temp;
		// int i = DataSave.SCREEN_WIDTH;
		// // 避免干扰界面
		// while (isTrue) {
		// tool.delay(500);
		// DSHandle.AutoCheckIn();
		// boolean bool = false;
		// BufferedImage image = tool.截取屏幕(DataSave.SCREEN_WIDTH +
		// DataSave.SCREEN_X - 709, DataSave.SCREEN_Y + 63,
		// DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 326, DataSave.SCREEN_Y +
		// 762);
		// if (tool.图中找图(image, "对话.bmp", DataSave.my.getNpc_sb(), 0, 0) !=
		// null) {
		// bool = true;
		// }
		// if (tool.图中找图(image, "马车.bmp", DataSave.my.getMc_sb(), 0, 0) != null)
		// {
		// bool = true;
		// }
		// if (!bool)
		// break;
		//
		// tool.mouseMove(i % (DataSave.SCREEN_WIDTH + 600) + DataSave.SCREEN_X,
		// (int) (DataSave.SCREEN_HEIGHT * 0.4) + DataSave.SCREEN_Y);
		// i -= 600;
		// }
		// tool.keyPressOne(KeyEvent.VK_CONTROL);
		// tool.delay(200);
		// while (isTrue) {
		// while (isTrue) {
		// tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X -
		// 354, DataSave.SCREEN_Y + 39, InputEvent.BUTTON1_MASK);
		// tool.delay(2000);
		// if (image == null) {
		// // 1178,78,1565,762
		// mark = FindNPCMark1();
		// } else {
		// mark = FindNPCMark2();
		// }
		// if (!isTrue)
		// return;
		// tool.delay(500);
		// tool.mouseMovePressOne(mark.x + 100, mark.y + 10,
		// InputEvent.BUTTON1_MASK);
		// DSHandle.粘贴名字(place_name);
		// if (GameUIModel.Find公共对话框(tool, DataSave.SCREEN_X +
		// DataSave.SCREEN_WIDTH / 2 + 138, DataSave.SCREEN_HEIGHT +
		// DataSave.SCREEN_Y - 212,
		// DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 100,
		// DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 138) == null) {
		// break;
		// }
		// }
		// if (!isTrue)
		// return;
		// tool.delay(700);
		// if (image == null) {
		// // 提取特征码坐标集合
		// image = tool.得到特征点集合图(mark.x + 20, mark.y - 157 -
		// (DataSave.服务器.equals("stream服") ? 0 : 6), mark.x + 80,
		// mark.y - 157 + (DataSave.服务器.equals("stream服") ? 12 : 6), 0.13,
		// "EEEEEE");
		// mark_1 = new Point(20, -157 - (DataSave.服务器.equals("stream服") ? 0 :
		// 6));
		// Point point = tool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X -
		// 722, DataSave.SCREEN_Y + 63,
		// DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 385, DataSave.SCREEN_Y +
		// 350, 0.10, "贸易贸易.bmp");
		// System.out.println(mark + " " + point);
		// mark_2 = new Point(point.x - mark.x, point.y - mark.y);
		// System.out.println("mark:" + mark_1 + " " + mark_2);
		// if (image != null)
		// tool.保存图片(image, "lin2.bmp");
		// else {
		// continue;
		// }
		// }
		// long time = System.currentTimeMillis();
		// do {
		// tool.delay(200);
		// if (System.currentTimeMillis() - time > 3000) {
		// mark = null;
		// break;
		// }
		// } while (tool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 709,
		// DataSave.SCREEN_Y + 390, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X -
		// 326,
		// DataSave.SCREEN_Y + 625, 0.05, image) == null && isTrue);
		// if (!isTrue)
		// return;
		// if (mark == null) {
		// continue;
		// }
		// mark = new Point(mark.x + 20, mark.y - 157 -
		// (DataSave.服务器.equals("stream服") ? 0 : 6));
		// // lin_mark = new Point(mark.x + 50, mark.y + 80);
		// time = System.currentTimeMillis();
		// boolean bool = true;
		// tool.delay(1000);
		// do {
		// tool.mouseMovePressOne(mark.x + 30, mark.y + 4,
		// InputEvent.BUTTON1_MASK);
		// tool.delay(700);
		// if (System.currentTimeMillis() - time > 5 * 1000) {
		// bool = false;
		// break;
		// }
		// } while (tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 234,
		// DataSave.SCREEN_Y + 108, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH -
		// 106,
		// DataSave.SCREEN_Y + 204, 0.07, 5, "E5E6FE") == null
		// && tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 234,
		// DataSave.SCREEN_Y + 108, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH -
		// 106,
		// DataSave.SCREEN_Y + 204, 0.07, 1, "C7A002") == null
		// && isTrue);
		// if (!isTrue) {
		// return;
		// }
		// if (bool)
		// break;
		// }
		// tool.keyPressOne(KeyEvent.VK_ESCAPE);
		// tool.delay(500);
		// Point p = null;
		// if ((p = tool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 735,
		// DataSave.SCREEN_Y + 576, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X -
		// 679,
		// DataSave.SCREEN_Y + 716, 0.15, "NPC搜索.bmp")) != null && p.x == mark.x
		// && isTrue) {
		// tool.keyPressOne(KeyEvent.VK_ESCAPE);
		// tool.delay(500);
		// }
		// tool.delay(200);
		// tool.keyPressOne(KeyEvent.VK_CONTROL);
		// tool.delay(500);
		// }

		private Point FindNPCMark1() {
			long time = System.currentTimeMillis();
			long time2 = System.currentTimeMillis();
			Rectangle rectangle = null;
			rectangle = new Rectangle(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 441, DataSave.SCREEN_Y + 39, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 340,
					DataSave.SCREEN_Y + 126);
			while ((mark = GameUIModel.FindXX(tool, rectangle.x, rectangle.y, rectangle.width, rectangle.height)) == null && isTrue) {
				tool.delay(500);
				if (System.currentTimeMillis() - time > 2 * 1000) {
					DSHandle.AutoCheckIn();
					tool.delay(200);
					tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1566), DataSave.SCREEN_Y + 17, InputEvent.BUTTON1_MASK);
					time = System.currentTimeMillis();
				}
				if (System.currentTimeMillis() - time2 > 5 * 1000) {
					DSHandle.AutoCheckIn();
					if (!GameListenerThread.DJNI.isCorsurShow())
						tool.keyPressOne(KeyEvent.VK_CONTROL);
					tool.delay(200);
					tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1566), DataSave.SCREEN_Y + 17, InputEvent.BUTTON1_MASK);
					time2 = System.currentTimeMillis();
				}
			}
			// 1538 71 1164 735
			mark.x -= (1538 - 1164);
			mark.y += (735 - 71);

			return mark;
		}

		private Point FindNPCMark2() {
			// tool.mouseMovePressOne(lin_mark.x, lin_mark.y,
			// InputEvent.BUTTON1_MASK);
			tool.delay(500);
			long time = System.currentTimeMillis();
			long time2 = System.currentTimeMillis();
			long time3 = System.currentTimeMillis();
			Rectangle rectangle = new Rectangle(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 781, DataSave.SCREEN_Y + 63,
					DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 360, DataSave.SCREEN_Y + 826);
			while (isTrue) {
				// mark = tool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X -
				// 722, DataSave.SCREEN_Y + 63, DataSave.SCREEN_WIDTH +
				// DataSave.SCREEN_X - 385,
				// DataSave.SCREEN_Y + 350, 0.10, "贸易贸易.bmp");

				if (System.currentTimeMillis() - time > 2 * 1000) {
					DSHandle.AutoCheckIn();
					tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1566), DataSave.SCREEN_Y + 17, InputEvent.BUTTON1_MASK);
					tool.delay(500);
					// tool.mouseMovePressOne(lin_mark.x, lin_mark.y,
					// InputEvent.BUTTON1_MASK);
					// tool.delay(500);
					time = System.currentTimeMillis();
				}
				if (System.currentTimeMillis() - time2 > 5 * 1000) {
					DSHandle.AutoCheckIn();
					if (!GameListenerThread.DJNI.isCorsurShow())
						tool.keyPressOne(KeyEvent.VK_CONTROL);
					tool.delay(200);
					tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1566), DataSave.SCREEN_Y + 17, InputEvent.BUTTON1_MASK);
					tool.delay(500);
					// tool.mouseMovePressOne(lin_mark.x, lin_mark.y,
					// InputEvent.BUTTON1_MASK);
					// tool.delay(500);
					time2 = System.currentTimeMillis();
				}

				BufferedImage image = tool.截取屏幕(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
				Point p1 = tool.图中找图(image, "贸易贸易.bmp", 0.10, rectangle.x, rectangle.y);
				if (p1 != null) {
					p1.x = p1.x - mark_2.x;
					p1.y = p1.y - mark_2.y;
				}
				Point p2 = tool.图中找图EX(image, this.image, 0.03, rectangle.x, rectangle.y);
				if (p2 != null) {
					p2.x -= mark_1.x;
					p2.y -= mark_1.y;
				}
				Point p3 = GameUIModel.FindXX(tool, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 441, DataSave.SCREEN_Y + 39,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 340, DataSave.SCREEN_Y + 126);
				// p3 = tool.图中找图EX(image, , 0.15, rectangle.x, rectangle.y);
				if (p3 != null) {
					p3.x -= (1538 - 1164);
					p3.y += (735 - 71);
				}
				if (System.currentTimeMillis() - time3 > 15 * 1000) {
					mark = null;
					if (p1 != null) {
						mark = p1;
					} else if (p2 != null) {
						mark = p2;
					} else if (p3 != null) {
						mark = p3;
					}
					if (mark != null)
						break;
					continue;
				}
				System.out.println(p1 + " " + p2 + " " + p3);
				Point lin1 = null;
				Point lin2 = null;
				if (p1 != null) {
					lin1 = p1;
				}
				if (lin1 == null && p2 != null) {
					lin1 = p2;
				}
				if (lin1 == null)
					continue;
				if (lin1 != p2 && p2 != null) {
					lin2 = p2;
				}
				if (lin2 == null && p3 != null) {
					lin2 = p3;
				}
				if (lin2 == null) {
					continue;
				}
				if (lin1 != null && lin2 != null && Math.abs(lin1.x - lin2.x) <= 5 && Math.abs(lin1.y - lin2.y) <= 5) {
					mark = lin1;
					break;
				}
				if (p2 != null) {
					mark = p2;
					break;
				}
			}

			// while ((mark = tool.区域找图EX(DataSave.SCREEN_WIDTH +
			// DataSave.SCREEN_X - 709, DataSave.SCREEN_Y + 390,
			// DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 326,
			// DataSave.SCREEN_Y + 625, 0.03, image)) == null && isTrue) {
			// tool.delay(500);
			//
			// }
			// if (mark != null)
			// mark.y = mark.y + 157 + 6;
			// else {
			// mark = FindNPCMark1();
			// }
			return mark;
		}

		// 在线判断
		private boolean isZaixian() {
			if (DSHandle.getLineTeImg() == null) {
				Other.sleep(500);
				while (DSHandle.getLineTeImg() == null && isTrue) {
					System.out.println("等待在线");
					Other.sleep(5000);
				}
				if (!isTrue)
					return true;
				Other.sleep(10 * 1000);
				while (isTrue) {
					goTomache();
					if (!isTrue)
						return true;
					tool.delay(500);
					// place_name = DataSave.my.getLastPlace();
					// 前往下一个点，然后吹口哨上马
					if (!isKazhu()) {
						return true;
					}
					markPlace2(place_name);
					if (findMache()) {
						break;
					}
				}
				// 前往目的地
				goLastPlace();
				if (!isTrue)
					return true;
				long time = System.currentTimeMillis();
				while (isTrue && System.currentTimeMillis() - time < 15 * 1000) {
					Other.sleep(1000);
					if (tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 234, DataSave.SCREEN_Y + 108, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 106,
							DataSave.SCREEN_Y + 204, 0.07, 5, "E5E6FE") == null && isTrue) {
						markPlace2(place_name);
					}
				}
				if (!isTrue)
					return true;
				System.out.println("不在线");
				return false;
			}
			System.out.println("在线");
			return true;
		}

		private void waitSleep(int type) {
			int time = 0;
			long time2 = System.currentTimeMillis();
			while (isTrue) {
				System.out.println("判断是否停止");
				BufferedImage image = tool.截取屏幕(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 51, DataSave.SCREEN_Y + 43,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 32, DataSave.SCREEN_Y + 141);
				tool.delay(2500);
				if (tool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 51, DataSave.SCREEN_Y + 43, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 32,
						DataSave.SCREEN_Y + 141, 0.01, image) != null) {
					if (isZaixian()) {
						// 卡住判断
						if (isKazhu()) {
							continue;
						}
						return;
					}
				}
				if (DataSave.my.isBjhx()) {
					if (tool.点颜色比较(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X + 57, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 102, "0000CC") > 0.07
							&& isTrue) {
						tool.keyPressOne(Tool.getkeyCode(DataSave.my.getBjhxButton()));
					}
				}
				isZaixian();
				if (!isTrue) {
					return;
				}
				// 判断是否在马车上
				if (type == 1) {
					tool.保存截屏(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 96, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 173,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 62, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 158, "是否在马车上.bmp");

					// 判断是否已在马车上
					if ((tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 96, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 173,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 62, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 158, 0.15, 40, "5CA3AA") == null
							&& tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 96, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 173,
									DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 62, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 158, 0.15, 40,
									"4E8285") == null)
							&& isTrue) {
						while (isTrue) {
							goTomache();
							if (!isTrue)
								return;
							tool.delay(500);
							// 判断是否已经到了
							if (!isKazhu()) {
								return;
							}
							// place_name = DataSave.my.getLastPlace();
							// 前往下一个点，然后吹口哨上马
							markPlace2(place_name);
							if (findMache()) {
								break;
							}
						}
						goLastPlace();
					} else {
					}
				} else {
					if ((tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 96, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 173,
							DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 62, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 158, 0.15, 40, "5CA3AA") == null
							&& tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 96, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 173,
									DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 62, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 158, 0.15, 40,
									"4E8285") == null)
							&& isTrue) {

					} else {
						return;
					}
				}
				if (System.currentTimeMillis() - time2 > 10 * 1000 * 60) {
					time2 = System.currentTimeMillis();
					for (int i = 0; i < 40; i++) {
						tool.keyPressOne(KeyEvent.VK_W);
						tool.delay(25);
						tool.keyPressOne(KeyEvent.VK_D);
						tool.delay(25);
					}
					tool.keyPressOne(KeyEvent.VK_T);
					tool.delay(500);
					tool.keyPressOne(KeyEvent.VK_T);
					tool.delay(500);
					tool.keyPressOne(KeyEvent.VK_T);
					tool.delay(500);
				}

				if (!isTrue) {
					return;
				}
				if (DSHandle.isEatGRPJ()) {
					System.out.println("吃啤酒");
					if (DSHandle.isRun()) {
						DSHandle.stop();
					}
					DSHandle.EatGRPJ();
					if (ds_bool) {
						DSHandle.start(null);
						tool.delay(500);
					}
				}
				DSHandle.AutoCheckIn();
				int i = 0;
				time += 2000;
				if (time > 60 * 1000) {
					tool.mouseMove((i * 600) % (DataSave.SCREEN_WIDTH + 600), (int) (DataSave.SCREEN_HEIGHT * 0.4) * 1 + DataSave.SCREEN_Y);
					i++;
					time = 0;
				}
			}
		}

		private boolean isKazhu() {
			System.out.println("卡主判断");
			if ((tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 234, DataSave.SCREEN_Y + 108, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 106,
					DataSave.SCREEN_Y + 204, 0.07, 5, "E5E6FE")) != null
					&& tool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 234, DataSave.SCREEN_Y + 108, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 106,
							DataSave.SCREEN_Y + 204, 0.07, 1, "C7A002") == null
					&& isTrue) {
				System.out.println("卡主");
				return tuoKa();
			}
			System.out.println("没有卡主");
			return false;
		}

		private boolean tuoKa() {
			// int x = 0;
			int key = 0;
			key = KeyEvent.VK_D;
			do {
				BufferedImage image = tool.截取屏幕(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 51, DataSave.SCREEN_Y + 43,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 32, DataSave.SCREEN_Y + 141);
				tool.delay(1500);

				if (tool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 51, DataSave.SCREEN_Y + 43, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 32,
						DataSave.SCREEN_Y + 141, 0.01, image) != null) {

					tool.keyPress(KeyEvent.VK_W);
					tool.delay(50);
					tool.keyPress(key);
					tool.delay(50);
					for (int i = 0; i < 4; i++) {
						tool.delay(1000);
					}
					tool.keyRelease(KeyEvent.VK_W);
					tool.delay(200);
					tool.keyRelease(key);
					tool.delay(200);
					tool.keyPressOne(KeyEvent.VK_T);
					tool.delay(500);
					tool.keyPressOne(KeyEvent.VK_T);
					tool.delay(500);
					if (Math.random() < 0.5) {
						key = KeyEvent.VK_D;
					} else {
						key = KeyEvent.VK_A;
					}
				} else {
					tool.keyPressOne(KeyEvent.VK_T);
					tool.delay(500);
					System.out.println("脱卡");
					return true;
				}
				// x -= 600;
				DSHandle.转动镜头1(4.75);
			} while (isTrue);
			return false;
		}

		// 121,93,125,132
		private boolean shopping() {
			int x = 120;
			int y = 98;
			int height = 46;
			int i = 0;
			while (i < 6 && isTrue) {
				tool.delay(500);
				tool.mouseMovePressOne(DataSave.SCREEN_X + x, DataSave.SCREEN_Y + y + i * height, InputEvent.BUTTON1_MASK);
				tool.delay(500);
				i++;
				// 625,253,890,259
				int j = 0;
				while (j < 3 && isTrue) {
					Point p = isOther(j);
					if (p == null) {
						j++;
						continue;
					}
					// 装载
					if (loadOther(p)) {
						tool.keyPressOne(KeyEvent.VK_ESCAPE);
						tool.delay(500);
						tool.keyPressOne(KeyEvent.VK_ESCAPE);
						tool.delay(1500);
						return true;
					}
					j++;
				}
			}
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(1500);
			return false;
		}

		private Point ppX = null;

		private BufferedImage other_image = null;

		private boolean loadOther(Point point) {
			// 保存物品图片
			tool.mouseMovePressOne(point.x, point.y, InputEvent.BUTTON1_MASK);
			tool.delay(200);
			tool.keyPressOne(KeyEvent.VK_F);
			tool.delay(200);
			tool.keyPressOne(KeyEvent.VK_ENTER);
			tool.delay(300);
			Point p = null;
			// if (DataSave.台服) {
			p = tool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 176, DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X,
					DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 110, 0.07, other_image);
			// } else {
			// p = tool.区域找色(592, 934, 598, 934, 0, 6, "888888");
			// }
			if (p == null) {
				return false;
			} else {
				ppX = p;
			}
			int l = 5;
			boolean wan = false;
			while (isTrue) {
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 99, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 27,
						InputEvent.BUTTON1_MASK);
				tool.delay(300);
				tool.keyPressOne(KeyEvent.VK_ENTER);
				tool.delay(300);
				tool.keyPressOne(KeyEvent.VK_ENTER);
				tool.delay(300);
				if (l == 0) {
					tool.keyPressOne(KeyEvent.VK_ENTER);
					tool.delay(300);
					return true;
				}
				// 判断是否超载
				p = tool.区域找色(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X - 38, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y - 71,
						DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X + 38, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y - 48, 0.15, 20, "BEBEC4");
				if (p != null) {
					tool.keyPressOne(KeyEvent.VK_ENTER);
					tool.delay(300);

					oneLoad(l);
					l--;
				} else {
					tool.delay(500);
					// if (DataSave.台服) {
					p = tool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 176, DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X,
							DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 110, 0.07, other_image);
					// } else {
					// p = tool.区域找色(592, 934, 598, 934, 0, 6, "888888");
					// }
					if (p == null) {
						if (l >= 4) {
							return loadOther(point);
						}
						return wan;
					} else {
						oneLoad(l);
						l--;
					}
				}
				wan = true;
			}
			return wan;
		}

		private void oneLoad(int l) {
			tool.mouseMovePressOne(ppX.x + 180, ppX.y + 2, InputEvent.BUTTON1_MASK);
			tool.delay(300);
			tool.keyPressOne(Tool.getkeyCode("" + l));
			tool.delay(300);
			tool.keyPressOne(KeyEvent.VK_ENTER);
		}

		private Point isOther(int i) {
			int num = !DataSave.服务器.equals("台服") ? 257 : 265;
			// 564,86,593,119
			Point point = tool.区域找图(DataSave.SCREEN_X + 542 + i * num, DataSave.SCREEN_Y + 95, DataSave.SCREEN_X + 613 + i * num, DataSave.SCREEN_Y + 159, 0.15,
					"贸易物品.bmp");
			if (point == null)
				return null;
			other_image = tool.截取屏幕(point.x + 20, point.y + 20, point.x + 35, point.y + 35);

			point.x = point.x + 54;
			point.y = point.y + 156;
			return point;
		}
	}

}
