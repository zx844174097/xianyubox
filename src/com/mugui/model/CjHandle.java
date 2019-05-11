package com.mugui.model;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.mugui.jni.Tool.DJni;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.GameListenerThread;
import com.mugui.ui.part.CJ.AttackModel;
import com.mugui.ui.part.CJ.HS_MAP;
import com.mugui.windows.Tool;
import com.sun.javafx.geom.Point2D;

public class CjHandle {

	private static boolean isTrue = false;
	private static DJni jni = GameListenerThread.DJNI;
	private static CjThread thread = null;
	private static Tool shoutool = null;
	private static HS_MAP hs_map = DataSave.hs_map;

	public static void start() {
		if (jni == null)
			jni = new DJni();
		hs_map = jni.getHsMap();
		if (hs_map.address != 0) {

		} else
			return;
		isTrue = true;
		if (thread != null && thread.isAlive()) {
			return;
		}
		thread = new CjThread();
		thread.start();
	}

	public static void stop() {
		isTrue = false;
	}

	private static class CjThread extends Thread {
		@Override
		public void run() {
			if (shoutool == null)
				shoutool = new Tool();
			shoutool.delay(500);
			// 确定视角
			float shijiao_y = -0.7f;
			while (isTrue) {
				PointerInfo mouse_info = MouseInfo.getPointerInfo();
				hs_map = jni.getHsMap();
				if (hs_map.lens_dy < shijiao_y - 0.05) {
					shoutool.mouseMove(mouse_info.getLocation().x, mouse_info.getLocation().y - 1);
				} else if (hs_map.lens_dy > shijiao_y + 0.05) {
					shoutool.mouseMove(mouse_info.getLocation().x, mouse_info.getLocation().y + 1);
				} else
					break;
			}
			while (isTrue) {
				// 旋转搜索，发现猎物
				pullMouse(false);
				if (!isTrue)
					return;
				findPrey();
				if (!isTrue)
					return;
				// 拉鼠标镜头远
				pullMouse(true);
				if (!isTrue)
					return;

				shoutool.delay(500);
				AttackModel model = DataSave.cj.getAttack_Model();
				switch (model.getAttack_way()) {
				case 0:
					for (int i = 0; i < (int) model.getWay2(); i++) {
						shoutool.mousePressOne((boolean) model.getWay1() ? InputEvent.BUTTON1_MASK : InputEvent.BUTTON3_MASK);
						shoutool.delay(200);
					}
					break;
				case 1:
					shoutool.keyPressOne(Tool.getkeyCode((String) model.getWay1()));
					break;
				case 2:
					shoutool.keyPress(Tool.getkeyCode((String) model.getWay1()));
					shoutool.delay(50);
					shoutool.keyPress(Tool.getkeyCode((String) model.getWay2()));
					shoutool.delay(200);
					shoutool.keyRelease(Tool.getkeyCode((String) model.getWay2()));
					shoutool.keyRelease(Tool.getkeyCode((String) model.getWay1()));
					break;
				}

				shoutool.delay(DataSave.cj.getSkillWaitTime());

				// 周边找到可以采集的图标
				collection();
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
			TCPModel.bool_listener = true;
			time = System.currentTimeMillis();
			BufferedImage image = shoutool.截取屏幕(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 260, DataSave.SCREEN_Y + 72,
					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 73, DataSave.SCREEN_Y + 247 - 72);
			if (shoutool.图中找图(image, "人.bmp", 0.001, 0, 0) == null && isTrue) {
				boolean bool = true;
				if (DataSave.监视公会 && shoutool.图中找图EX(image, "工会成员.bmp", 0.07, 0, 0) != null) {
					bool = false;
				}
				if (bool) {
					return;
				}
			}
			if (!isTrue)
				return;
			long time = System.currentTimeMillis();
			long time2 = 0;
			long time3 = time;
			while (isTrue) {
				DSHandle.AutoCheckIn();
				boolean bool = false;
				if ((shoutool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 260, DataSave.SCREEN_Y + 72, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 73,
						DataSave.SCREEN_Y + 247, 0.001, "人.bmp") != null && isTrue)) {
					bool = true;
				}
				if (!bool && DataSave.监视公会 && shoutool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 260, DataSave.SCREEN_Y + 72,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 73, DataSave.SCREEN_Y + 247, 0.05, "工会成员.bmp") != null) {
					bool = true;
				}
				if (!DataSave.被监视)
					return;
				if (!bool) {
					shoutool.mouseMove(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - i % (DataSave.SCREEN_WIDTH + 600),
							DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y);
					i %= (DataSave.SCREEN_WIDTH);
					i += 600;
					shoutool.delay(100);
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
				if (System.currentTimeMillis() - time > 60 * 1000) {
					break;
				}
				if (System.currentTimeMillis() - time3 > 3 * 60 * 1000) {
					break;
				}
				if (bool)
					shoutool.delay(2000);
			}
			TCPModel.sendStopManListener();
			DSHandle.关闭播放();
			if (!isTrue) {
				return;
			}
			System.out.println("人离开");
		}

		private void collection() {
			float jiaodu = jni.getHsMap().lens_dx;
			float yuan = jiaodu;
			shoutool.lockDirection(jiaodu);
			int num = 0;
			int ci = 0;
			System.out.println("采集周边打掉的");
			shoutool.delay(1000);
			// BufferedImage image = shoutool.截取屏幕(DataSave.SCREEN_X,
			// DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
			// DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y);
			// String uuid = Other.getUUID();
			// shoutool.保存图片(image, uuid + "_tu.bmp");
			// image = shoutool.得到图的特征图(image, 0.32, "C0BCBC");
			// shoutool.保存图片(image, uuid + "_chu.bmp");

			while (isTrue && ci < DataSave.cj.getCjFindNum()) {
				// 判断被监视
				MonitorBool();
				Point point = shoutool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
						DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, 0.15, "采集.bmp");
				if (point != null) {
					num = 0;
					shoutool.delay(200);
					shoutool.keyPressOne(KeyEvent.VK_R);
					long time = System.currentTimeMillis();
					while (isTrue) {// 833 865
						String color = shoutool.得到点颜色(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X - 127,
								DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - (1080 - 865));
						if (color.equals("9A9A9A"))
							break;
						point = shoutool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
								DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, 0.15, "采集.bmp");
						if (point != null) {
							shoutool.keyPressOne(KeyEvent.VK_R);
							continue;
						}
						if (System.currentTimeMillis() - time > 3000) {
							// 换一个新的采集武器
							if (huanCollencte()) {
								shoutool.delay(1000); 
								shoutool.keyPressOne(KeyEvent.VK_R);
							} else {
								System.out.println("!!!!!!!!!STOP");
								CjHandle.stop();
								return;
							}
						}
					}
					while (isTrue
							&& shoutool.得到点颜色(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X - 127, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - (1080 - 865))
									.equals("9A9A9A")) {
						shoutool.delay(500);
					}
					shoutool.delay(200);
					shoutool.keyPressOne(KeyEvent.VK_R);
					shoutool.delay(700);

				} else if (num++ < 3) {
					jiaodu += 3.14f / 2;
					if (jiaodu > 3.14f) {
						jiaodu -= 3.14f;
						jiaodu += -3.14;
					}
					shoutool.lockDirection(jiaodu);
				} else {
					shoutool.lockDirection(yuan);
					shoutool.keyPress(KeyEvent.VK_W);
					shoutool.delay(DataSave.cj.getGotoTime());
					shoutool.keyRelease(KeyEvent.VK_W);
					ci++;
					num = 0;
				}
			}
		}

		BufferedImage cjgj[] = null;

		private boolean huanCollencte() {
			if (cjgj == null)
				cjgj = DataSave.cj.getCJGJ();
			if (cjgj == null)
				return false;
			System.out.println("更换采集工具");
			Point p = null;
			// 1536,131,1536,138 1531
			int i = Integer.parseInt(DataSave.deBug.打开背包的前置时间.getText().trim());
			if (i > 0)
				shoutool.delay(i);
			shoutool.keyPressOne(KeyEvent.VK_I);
			i = Integer.parseInt(DataSave.deBug.检测鱼竿的前置时间.getText().trim());
			if (i > 0)
				shoutool.delay(i);
			double d = Double.parseDouble(DataSave.deBug.鱼竿检测准确度.getText().trim());
			if (d <= 0)
				d = 0;
			for (int j = 0; j < cjgj.length; j++) {
				p = shoutool.区域找图(DataSave.SCREEN_WIDTH - 420 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 - 222 + DataSave.SCREEN_Y,
						DataSave.SCREEN_WIDTH - 16 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 + 169 + DataSave.SCREEN_Y, d, cjgj[j]);
				if (p != null) {
					shoutool.mouseMove(p.x + cjgj[j].getWidth() / 2, p.y + cjgj[j].getHeight() / 2);
					i = Integer.parseInt(DataSave.deBug.更换鱼竿的前置时间.getText().trim());
					if (i > 0)
						shoutool.delay(i);
					shoutool.mousePressOne(InputEvent.BUTTON3_MASK);
					break;
				}
				shoutool.delay(25);
			}
			if (DataSave.丢弃采集) {
				DSHandle.丢弃();
			}
			i = Integer.parseInt(DataSave.deBug.关闭背包的前置时间.getText().trim());
			if (i > 0)
				shoutool.delay(i);
			shoutool.keyPressOne(KeyEvent.VK_I);
			return true;
		}

		private static class CJPoint {
			public Point2D location = null;
			public Rectangle center = null;
			public float x;
			public float y;

			public CJPoint() {
				location = new Point2D(0, 0);
				center = new Rectangle(0, 0, 0, 0);
			}

			public float getJiaodu() {
				// Point2D p2 = new Point2D( (center.width - center.x) /
				// 2,(center.height - center.y) / 2);
				// if (p2.x == location.x && p2.y == location.y) {
				return -999;
				// }
				// return thread.mathDirection(p2, location);
			}
		}

		private void findPrey() {
			// 1753 160
			while (isTrue) {

				// shoutool.区域找色点数量( )>15

				//int par_n = 10;
				int par_size = 70;
				// 搜索到一个可以打羊的坐标点
				int max = 0;
				CJPoint max_point = null;
				float jiaodu = -3.14f;
				while (isTrue && jiaodu <= 3.14f) {
					// PointerInfo mouse_info = MouseInfo.getPointerInfo();
					// shoutool.mouseMove(mouse_info.getLocation().x - 200,
					// mouse_info.getLocation().y);
					shoutool.lockDirection(jiaodu);
					jiaodu += 3.14f / 2;
					CJPoint point = new CJPoint();
					BufferedImage image = shoutool.截取屏幕(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1753 + par_size),
							DataSave.SCREEN_Y + 160 - par_size, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1753 - par_size),
							DataSave.SCREEN_Y + 160 + par_size);
					if (image == null)
						continue;
					image = shoutool.得到图的特征图(image, 0.24, "0525A9");
					if (image == null)
						continue;
					int size = getKillSheepPoint(image, point);
					if (size > max) {
						// -27245.525 2790.1409 -25976.65 1654.3528 1738,147
						// 1753 160
						double hen = (-27245.525f + 25976.65f) / (1738.0f - 1753.0f);
						double shu = (2790.1409f - 1654.3528f) / (147.0f - 160.0f);
						// CJPoint point2d = new CJPoint();
						hs_map = jni.getHsMap();

						point.x = (float) ((point.location.x - par_size) * hen + hs_map.x);
						point.y = (float) ((point.location.y - par_size) * shu + hs_map.y);

						if (!isOverspill(point)) {
							max_point = point;
							max = size;
						}
					}
				}
				if (max_point == null)
					continue;
				gotoPoint(max_point, new Point2D(hs_map.x, hs_map.y));
				break;
			}

		}

		private static final int black = Color.black.getRGB();

		private int getKillSheepPoint(BufferedImage image, CJPoint point) {
			int x = image.getHeight() / 2;
			int y = image.getWidth() / 2;
			int num = image.getWidth() * x + y;

			return searchMaxPoint(image, num, point);
		}

		// private int max_num = 0;
		private int biao[] = null;
		private boolean tu[][] = null;
		private int biao_l = 0;
		private int biao_r = 1;

		private int searchMaxPoint(BufferedImage image, int num, CJPoint point) {

			biao_l = 0;
			biao_r = 0;
			tu = new boolean[image.getWidth()][image.getHeight()];
			biao = new int[image.getWidth() * image.getHeight() + 1];
			biao[biao_r++] = num;
			tu[num % image.getWidth()][num / image.getWidth()] = true;
			int max_size = 0;
			while (isTrue && biao_l < biao_r) {
				// while (isTrue&&biao_l < biao_r) {
				num = biao[biao_l++];
				// System.out.println(max_size + " " + biao_l + " " + biao_r + "
				// " + num);
				int x = num % image.getWidth();
				int y = num / image.getWidth();
				if (image.getRGB(x, y) != black) {
					zuo = shang = 2 << 16;
					you = xia = 0;
					int temp_size = fill(image, x, y, 0);
					if (temp_size > max_size) {
						// System.out.println(zuo+" "+you+" "+shang+" "+xia+"
						// "+x+" "+y);
						max_size = temp_size;
						point.location.x = x;
						point.location.y = y;
						point.center.x = zuo;
						point.center.width = you;
						point.center.y = shang;
						point.center.height = xia;
					}
				}
				int temp = num - 1;
				if (temp >= 0 && !tu[temp % image.getWidth()][temp / image.getWidth()]) {
					biao[biao_r++] = temp;
					tu[temp % image.getWidth()][temp / image.getWidth()] = true;

				}
				temp = num - image.getWidth();
				if (temp >= 0 && !tu[temp % image.getWidth()][temp / image.getWidth()]) {
					biao[biao_r++] = temp;
					tu[temp % image.getWidth()][temp / image.getWidth()] = true;
				}
				temp = num + 1;
				if (temp < image.getWidth() * image.getHeight() && !tu[temp % image.getWidth()][temp / image.getWidth()]) {
					biao[biao_r++] = temp;
					tu[temp % image.getWidth()][temp / image.getWidth()] = true;
				}
				temp = num + image.getWidth();
				if (temp < image.getWidth() * image.getHeight() && !tu[temp % image.getWidth()][temp / image.getWidth()]) {
					biao[biao_r++] = temp;
					tu[temp % image.getWidth()][temp / image.getWidth()] = true;
				}
			}
			return max_size;
		}

		int zuo, you, shang, xia;

		private int fill(BufferedImage image, int x, int y, int size) {
			if (image.getRGB(x, y) != black) {
				if (zuo > x)
					zuo = x;
				if (you < x)
					you = x;
				if (shang > y)
					shang = y;
				if (xia < y)
					xia = y;
				size++;
				// tu[x][y] = true;
				image.setRGB(x, y, black);
			} else {
				return size;
			}
			if (x - 1 > 0) {
				size = fill(image, x - 1, y, size);
			}
			if (x - 2 > 0) {
				size = fill(image, x - 2, y, size);
			}
			if (y - 1 > 0) {
				size = fill(image, x, y - 1, size);
			}
			if (y - 2 > 0) {
				size = fill(image, x, y - 2, size);
			}
			if (x + 1 < image.getWidth()) {
				size = fill(image, x + 1, y, size);
			}
			if (x + 2 < image.getWidth()) {
				size = fill(image, x + 2, y, size);
			}
			if (y + 1 < image.getHeight()) {
				size = fill(image, x, y + 1, size);
			}
			if (y + 2 < image.getHeight()) {
				size = fill(image, x, y + 2, size);
			}
			return size;
		}

		private void pullMouse(boolean b) {
			for (int i = 0; i < 60 && isTrue; i++) {
				shoutool.mouseWheel(b ? 1 : -1);
			}
		}

		// private void roleWalk() {
		// long time = System.currentTimeMillis();
		// while (isTrue) {
		// shoutool.keyPressLong(KeyEvent.VK_W);
		// // -27000 -3283 -16000 5888
		// while (isTrue) {
		// if (isOverspill()) {
		// shoutool.keyRelease(KeyEvent.VK_W);
		// //回到边界
		// backToOverspill();
		// }
		// }
		//
		// }
		// }
		// 过界恢复
		private void backToOverspill() {
			// 锁定方向

			System.out.println("返回界限内");
			hs_map = jni.getHsMap();
			Point2D p2 = new Point2D(hs_map.x, hs_map.y);
			Point2D p1 = getRandomPoint();
			if (isOverspill(p1)) {

				System.out.println("这是一个越界的坐标");
				return;
			}
			float jiaodu = mathDirection(p2, p1);
			shoutool.lockDirection(jiaodu);
			if (!isTrue)
				return;
			HS_MAP last_hsmap = null;
			hs_map = jni.getHsMap();
			last_hsmap = new HS_MAP();
			last_hsmap.x = hs_map.x;
			last_hsmap.y = hs_map.y;
			long time = System.currentTimeMillis();
			shoutool.keyPress(KeyEvent.VK_W);
			while (isTrue) {
				hs_map = jni.getHsMap();
				if (Math.abs(hs_map.x - p1.x) < 100 && Math.abs(hs_map.y - p1.y) < 100) {
					shoutool.keyRelease(KeyEvent.VK_W);
					return;
				}
				long time2 = System.currentTimeMillis();
				if (time2 - time > 1000) {
					time = time2;
					if (Math.abs(last_hsmap.x - hs_map.x) < 50 && Math.abs(last_hsmap.y - hs_map.y) < 50) {
						shoutool.keyRelease(KeyEvent.VK_W);
						gotoPointTime(1000);
						shoutool.keyPress(KeyEvent.VK_W);
					}
					last_hsmap.x = hs_map.x;
					last_hsmap.y = hs_map.y;
				}
				p2 = new Point2D(hs_map.x, hs_map.y);
				jiaodu = mathDirection(p2, p1);
				// 锁定方向
				shoutool.lockDirection(jiaodu);
			}
			shoutool.keyRelease(KeyEvent.VK_W);
		}

		// 随机跑向另一个坐标一段时间
		private void gotoPointTime(long time) {
			hs_map = jni.getHsMap();
			Point2D p2 = new Point2D(hs_map.x, hs_map.y);
			Point2D p1 = getRandomPoint();
			float jiaodu = mathDirection(p2, p1);
			shoutool.lockDirection(jiaodu);
			if (!isTrue)
				return;
			shoutool.keyPress(KeyEvent.VK_W);
			long time1 = System.currentTimeMillis();
			while (isTrue) {
				hs_map = jni.getHsMap();
				if (isOverspill(hs_map)) {
					System.out.println("gotoPointTime1:" + hs_map.toString());
					backToOverspill();
				}
				if (System.currentTimeMillis() - time1 > time) {
					break;
				}
				p2 = new Point2D(hs_map.x, hs_map.y);
				jiaodu = mathDirection(p2, p1);
				// 锁定方向
				shoutool.lockDirection(jiaodu);
			}
			shoutool.keyRelease(KeyEvent.VK_W);
		}

		// 方向计算
		private float mathDirection(Point2D p2, Point2D p1) {
			return (float) StrictMath.atan2((p2.x - p1.x), (p2.y - p1.y));
		}

		// 得到随机有效坐标
		private Point2D getRandomPoint() {
			// 判断是否是x 方向过界
			// 选取x，y点；map.lens_dx*180.0f/Math.PI/1.0f
			double random = Math.random();
			float xian_x1 = (float) random * (-16000.0f + 27000.0f) - 16000.0f;
			random = Math.random();
			float xian_y1 = (float) random * (5888.0f + 3283.0f) - 3283.0f;
			Point2D p1 = new Point2D(xian_x1, xian_y1);
			return p1;
		}

		private void gotoPoint(CJPoint max_point, Point2D p2) {
			float jiaodu = (float) StrictMath.atan2((p2.x - max_point.x), (p2.y - max_point.y));
			System.out.println(max_point + " " + p2 + " " + jiaodu);

			// 锁定方向
			shoutool.lockDirection(jiaodu);
			HS_MAP last_hsmap = null;
			hs_map = jni.getHsMap();
			last_hsmap = new HS_MAP();
			last_hsmap.x = hs_map.x;
			last_hsmap.y = hs_map.y;
			long time = System.currentTimeMillis();
			shoutool.keyPress(KeyEvent.VK_W);
			while (isTrue) {
				hs_map = jni.getHsMap();
				if (Math.abs(hs_map.x - max_point.x) < 100 && Math.abs(hs_map.y - max_point.y) < 100) {
					// shoutool.delay(500);

					shoutool.keyRelease(KeyEvent.VK_W);
					jiaodu = max_point.getJiaodu();
					if (jiaodu > -100f) {
						shoutool.lockDirection(jiaodu);
					}
					return;
				}
				long time2 = System.currentTimeMillis();
				if (time2 - time > 500) {
					time = time2;
					if (Math.abs(last_hsmap.x - hs_map.x) < 50 && Math.abs(last_hsmap.y - hs_map.y) < 50) {
						shoutool.keyRelease(KeyEvent.VK_W);
						gotoPointTime(1000);
						shoutool.keyPress(KeyEvent.VK_W);
					}
					last_hsmap.x = hs_map.x;
					last_hsmap.y = hs_map.y;
				}
				p2 = new Point2D(hs_map.x, hs_map.y);
				jiaodu = (float) StrictMath.atan2((p2.x - max_point.x), (p2.y - max_point.y));
				// 锁定方向
				shoutool.lockDirection(jiaodu);
				// System.out.println(p1 + " " + p2+" "+jiaodu);
				shoutool.delay(50);
			}
			shoutool.keyRelease(KeyEvent.VK_W);
		}

		// 是否过界
		private boolean isOverspill(CJPoint point2d) {
			return isOverspill(new Point2D(point2d.x, point2d.y));
		}

		private boolean isOverspill(HS_MAP hs_map) {
			// TODO 自动生成的方法存根
			return isOverspill(new Point2D(hs_map.x, hs_map.y));
		}

		private boolean isOverspill(Point2D point2d) {
			// System.out.println((point2d.x < -16000f) + " " + (point2d.x >
			// -27000f) + " " + (point2d.y > -3283f) + " " + (point2d.y <
			// 6100));
			if (point2d.x < -16000f && point2d.x > -28747 && point2d.y > -3283f && point2d.y < 5888f) {
				return false;
			} else {
				return true;
			}
		}
	}
}
