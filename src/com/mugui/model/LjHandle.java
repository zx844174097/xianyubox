package com.mugui.model;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import com.mugui.http.Bean.JGOtherBean;
import com.mugui.http.Bean.JGOtherBean.JGBean;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.GameListenerThread;
import com.mugui.ui.part.JGOtherPanel;
import com.mugui.windows.Tool;

public class LjHandle {
	private static boolean isTrue = false;
	private static LjThread thread = null;
	private static Tool tool = null;

	public static boolean ds_bool = false;

	public static void start() {
		if (thread != null && thread.isAlive()) {
			return;
		}
		isTrue = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isTrue) {
					TcpBag bag = new TcpBag();
					bag.setBag_id(TcpBag.ERROR);
					UserBean userBean = new UserBean();
					userBean.setCode("lj");
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
		thread = new LjThread();
		thread.start();
	}

	public static void start(String string) {
		if (thread != null && thread.isAlive()) {
			return;
		}
		isTrue = true;
		thread = new LjThread();
		thread.test(string);
		thread.start();
	}

	public static void stop() {
		isTrue = false;
	}

	private static JGOtherBean ljOtherBean = null;

	private static JGOtherPanel[] ljOtherPanel = null;
	private static int number = 0;
	private static boolean a = false;
	private static BufferedImage[] taizi = null;

	private static class LjThread extends Thread {
		private int SAFE = 2;
		private int NULL = 0;
		private int COOK = 1;

		@Override
		public void run() {
			if (tool == null)
				tool = new Tool();
			tool.delay(1000);
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			a = false;
			groupNum = DataSave.lj.getGroupNum();
			ljOtherPanel = DataSave.lj.getJGOtherList();
			if (ljOtherPanel == null || ljOtherPanel.length <= 0) {
				LjHandle.stop();
				return;
			}
			ljOtherBean = ljOtherPanel[number].getJGOtherBean();
			taizi = DataSave.custonimg.getLjTai();
			if (test != null) {
				if (test.equals("放置测试")) {
					tool.delay(1000);
					PlaceCook(0);
				} else if (test.equals("回收测试")) {
					tool.delay(1000);
					ThrowCook();
				}
				LjHandle.stop();
				return;
			}

			DSHandle.startDxgj();
			if (DSHandle.isRun()) {
				DSHandle.stop();
				ds_bool = true;
			}
			System.out.println("开始炼金");
			while (isTrue) {
				if (isTrue && sy && qieh1 && DataSave.lj.isLj_htz()) {

					System.out.println("换台子");
					HuanTaizi();
					if (!isTrue) {
						return;
					}
				} else {
					tool.delay(1000);
					// 找到保险箱
					System.out.println("找到保险箱");

					Find(SAFE);
					if (!isTrue)
						return;
					// 存储图像
					System.out.println("存储材料图像");
					saveLjimg();
					if (!isTrue)
						return;
					// 取出物品
					System.out.println("取出物品");
					while (!TakeOutRes() && isTrue) {
						// 放入物品

						System.out.println("物品不足");
						startFHCK(0, ljOtherBean.getBody().size());
						number++;
						number %= ljOtherPanel.length;
						ljOtherBean = ljOtherPanel[number].getJGOtherBean();
						groupNum = DataSave.lj.getGroupNum();
					}

					if (!isTrue)
						return;
					tool.delay(500);
					tool.keyPressOne(KeyEvent.VK_ESCAPE);
					tool.delay(500);
					tool.keyPressOne(KeyEvent.VK_ESCAPE);
					tool.delay(500);
					DSHandle.AutoCheckIn();
					if (!isTrue)
						return;
				}
				// 找到炼金台
				System.out.println("找到炼金台");
				FindConsole(false);

				// DSHandle.转动镜头(2);
				if (!isTrue)
					return;
				Find(COOK);
				if (!isTrue)
					return;
				// 开始炼金
				System.out.println("开始炼金");
				startCook();
				if (!isTrue)
					return;
				System.out.println("等待炼金结束");
				waitCook();
				// 找到保险箱
				System.out.println("找到保险箱");
				FindConsole(false);
				// DSHandle.转动镜头(2);
				if (!isTrue)
					return;
				Find(SAFE);
				if (!isTrue)
					return;

				// 判断是否有剩余材料1885 263 1519 340
				System.out.println("判断是否有剩余材料");
				int x1 = beibao.x - 366;
				int y1 = beibao.y + 77;
				tool.delay(700);
				sy = true;
				for (int i = 0; i < ljOtherBean.getBody().size(); i++) {
					Point p = new Point(x1 + i % 9 * 54, y1 + i / 9 * 54);
					if (null != tool.区域找图(p.x, p.y, p.x + 30, p.y + 30, 0.05, "空格子.bmp")) {
						sy = false;
						break;
					}
				}
				// 放入物品
				System.out.println("材料放入保险箱物品");
				startFHCK(ljOtherBean.getBody().size(), 16);
				if (!isTrue)
					return;
				tool.delay(500);
				tool.keyPressOne(KeyEvent.VK_ESCAPE);
				tool.delay(500);

			}
		}

		private void HuanTaizi() {
			// 更换料理或者炼金用的台子 931 94
			if (!isTrue) {
				return;
			}
			System.out.println("回收台子");
			ThrowCook();
			if (!isTrue) {
				return;
			}
			System.out.println("找到保险箱");
			Find(SAFE);
			if (!isTrue) {
				return;
			} // 208,316,603,705
			Point point = null;
			// 寻找台子
			point = FindCook();
			System.out.println("point");
			if (point == null) {
				LjHandle.stop();
				return;
			}
			tool.mouseMovePressOne(point.x + taizi[cook_i].getWidth() / 2, point.y + taizi[cook_i].getHeight() / 2,
					InputEvent.BUTTON3_MASK);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			if (!isTrue) {
				return;
			}
			System.out.println("放置台子");
			PlaceCook(cook_i);
			if (!isTrue) {
				return;
			}

		}

		private int cook_i;

		private Point FindCook() {
			Point pp1 = new Point(cangku.x + 474, cangku.y + 217);
			int j = 0;
			while (j < 3) {
				tool.delay(500);
				tool.mouseMovePressOne(pp1.x, pp1.y + j * 143, InputEvent.BUTTON1_MASK);
				tool.delay(500);
				BufferedImage image = tool.截取屏幕(cangku.x - 458, cangku.y + 68, cangku.x + 10, cangku.y + 479 + 68);
				for (cook_i = 0; cook_i < taizi.length; cook_i++) {
					Point point = tool.图中找图(image, taizi[cook_i], 0.12, cangku.x - 458, cangku.y + 68);
					if (point != null) {
						return point;
					}
				}
				j++;
			}
			return null;
		}

		private String test = null;

		public void test(String string) {
			test = string;
		}

		private Point taiziPoint = null;

		private void PlaceCook(int i) {
			tool.delay(500);
			do {
				DSHandle.AutoCheckIn();
				if (!GameListenerThread.DJNI.isCorsurShow())
					tool.keyPressOne(KeyEvent.VK_CONTROL);
				tool.delay(500);
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X + 65, 160 + DataSave.SCREEN_Y,
						InputEvent.BUTTON1_MASK);
				tool.delay(1000);
			} while (DSHandle.getLineTeImg() != null && isTrue);
			long time = System.currentTimeMillis();
			Point point = null;
			// 271,946,1277,1055 299 977
			while ((point = tool.区域找图(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 430,
					DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y - 387,
					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 10, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y,
					0.15, taizi[i])) == null && isTrue) {
				tool.delay(10);
				if (System.currentTimeMillis() - time > 5000) {
					break;
				}
			}
			if (!isTrue)
				return;
			if (point == null) {
				System.out.println("没有发现台子坐标，采用上次发现的台子坐标");
				if (taiziPoint == null)// 1519 186
					point = new Point(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 401,
							DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y - 354);
				else
					point = taiziPoint;
			} else {
				point.x = point.x + taizi[i].getWidth() / 2;
				point.y = point.y + taizi[i].getHeight() / 2;
				taiziPoint = point;
			}

			while (isTrue) {
				tool.delay(500);
				tool.mouseMovePressOne(point.x, point.y, InputEvent.BUTTON1_MASK);
				tool.delay(500);
				Point point2 = DataSave.lj.getLj_XY();
				tool.mouseMovePressOne(point2.x, point2.y, InputEvent.BUTTON1_MASK);
				tool.delay(500);
				if (tool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
						DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, DataSave.lj.getLj_sbd(), "房屋配置_放下.bmp") != null) {
					break;
				}
			}
			if (!isTrue) {
				return;
			}
			while (isTrue) {
				tool.delay(500);
				tool.keyPressOne(KeyEvent.VK_SPACE);
				tool.delay(1000);
				if (tool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
						DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, DataSave.lj.getLj_sbd(), "房屋配置_放下.bmp") == null) {
					break;
				}
			}
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ENTER);
			tool.delay(1500);
		}

		private void ThrowCook() {
			DSHandle.AutoCheckIn();
			if (!GameListenerThread.DJNI.isCorsurShow())
				tool.keyPressOne(KeyEvent.VK_CONTROL);
			tool.delay(500);
			tool.mouseMovePressOne(DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X + 65, 160 + DataSave.SCREEN_Y,
					InputEvent.BUTTON1_MASK);
			tool.delay(1500);
			Point point = DataSave.lj.getLj_XY();
			tool.mouseMovePressOne(point.x, point.y, InputEvent.BUTTON1_MASK);
			long time = System.currentTimeMillis();
			while (isTrue) {
				point = tool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
						DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, DataSave.lj.getLj_sbd(), "房屋配置_回收.bmp");
				if (point != null)
					break;
				if (System.currentTimeMillis() - time > 3000) {
					time = System.currentTimeMillis();
					point = DataSave.lj.getLj_XY();
					tool.mouseMovePressOne(point.x, point.y, InputEvent.BUTTON1_MASK);
					tool.delay(1000);
				}
			}
			if (point == null) {
				System.out.println("回收失败，停止炼金");
				LjHandle.stop();
				return;
			}
			if (!isTrue) {
				return;
			}
			tool.mouseMovePressOne(point.x + 5, point.y + 5, InputEvent.BUTTON1_MASK);
			tool.delay(1000);
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(1000);
			tool.keyPressOne(KeyEvent.VK_ENTER);
			tool.delay(3000);
			tool.keyPressOne(KeyEvent.VK_I);
			tool.delay(1000);
			if (!isTrue) {
				return;
			}
			while (DSHandle.丢弃()) {
				tool.delay(500);
			}
			if (!isTrue) {
				return;
			}
			tool.keyPressOne(KeyEvent.VK_I);
			tool.delay(1000);
		}

		boolean sy = false;
		boolean qieh1 = false;

		private void waitCook() {
			// 等待炼金结束
			System.out.println("等待炼金结束");
			tool.delay(2000);
			if (ds_bool) {
				DSHandle.start(null);
			}

			long time = System.currentTimeMillis();
			qieh1 = false;
			while (isTrue && tool.区域找图EX(DataSave.SCREEN_X, DataSave.SCREEN_Y ,
					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT, 0.25,
					"操作.bmp") == null) {
				// 检测炼金是否开始832, 865
				tool.delay(2000);
				if (DSHandle.isEatGRPJ()) {
					if (ds_bool && DSHandle.isRun()) {
						DSHandle.stop();
					}
					DSHandle.EatGRPJ();
					if (ds_bool) {
						DSHandle.start(null);
						tool.delay(500);
					}
				}
			}
			if (isTrue) {
				qieh1 = System.currentTimeMillis() - time < 20 * 1000;
			}
			if (ds_bool) {
				DSHandle.stop();
			}
		}

		private void startCook() {
			System.out.println("开始炼金");
			Point BB_XY = null;
			if (BB_XY == null) {// 2113 488

				int x1 = DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 447;
				int y1 = DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 - 232;
				BB_XY = GameUIModel.Find背包UI(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 500, DataSave.SCREEN_Y,
						DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 300,
						DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
				if (BB_XY != null) {
					BB_XY.x = BB_XY.x + 5;
					BB_XY.y = BB_XY.y + 145;
//					BB_XY.x -= 403;
//					BB_XY.y += 127;
				} else {
					BB_XY = new Point(x1, y1);
				}
			}
			tool.delay(1000);
			Iterator<JGBean> bean = ljOtherBean.getBody().iterator();
			int i = 0;
			while (bean.hasNext() && isTrue) {
				Point p = new Point(BB_XY.x + i % 10 * 54, BB_XY.y + i / 10 * 54);
				i++;
				if (null != tool.区域找图(p.x, p.y, p.x + 33, p.y + 33, 0.05, "空格子.bmp")) {
					return;
				}
				tool.mouseMovePressOne(p.x, p.y, InputEvent.BUTTON3_MASK);
				tool.delay(500);

				if (GameUIModel.FindXX2(tool, p.x - 300, p.y, p.x + 300, p.y + 60) != null) {
					DSHandle.输入数字(bean.next().getN() + "");
					tool.delay(200);
					tool.keyPressOne(KeyEvent.VK_ENTER);
					tool.delay(500);
				}
			} // 852 363 1140 1047
			System.out.println("料理坐标：" + liaoli);
			// 831 789 1314 270
			tool.mouseMovePressOne(liaoli.x + 288, liaoli.y + 684, InputEvent.BUTTON1_MASK);
			// tool.delay(500);
			// DSHandle.输入数字(DataSave.lj.getGroupNum() + "");
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ENTER);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_F);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ENTER);
			tool.delay(500);

		}

		private void saveLjimg() {
			System.out.println("存储炼金图鉴");
			int x1, y1;// 仓库第一格坐标1607 441
			System.out.println("仓库坐标:" + cangku);// 1595 342
			x1 = cangku.x + 12 + 7;// 1611 446
			y1 = cangku.y + 99 + 15;
			if (!a) {
				a = true;
				// int s = 0;
				int state = 0;
				// 鼠标下拉位置
				Point pp1 = new Point(cangku.x + 474, cangku.y + 217);// 2069 559
				tool.delay(500);
				tool.mouseMovePressOne(pp1.x, pp1.y, InputEvent.BUTTON1_MASK);
				tool.delay(500);

				for (JGOtherPanel panel : ljOtherPanel) {
					Iterator<JGBean> iterator = panel.getJGOtherBean().getBody().iterator();
					while (iterator.hasNext()) {
						JGBean bean = iterator.next();
						int w = x1 + (bean.getColumn() - 1) * 51;
						int row = bean.getRow();
						int linrow = (row - 1) / 9;
						if (row > 9) {
							// 1254,263,1267,349, 606 128
							// 计算拉取深度
							if (state != linrow) {
								tool.mouseMovePressOne(pp1.x, pp1.y + linrow * 143, InputEvent.BUTTON1_MASK);
								tool.delay(500);
								state = linrow;
							}
						} else {
							if (state != 0) {
								tool.mouseMovePressOne(pp1.x, pp1.y, InputEvent.BUTTON1_MASK);
								tool.delay(500);
							}
							state = 0;
						}
						int z = y1 + ((bean.getRow() - 1) % 9) * 51;
						BufferedImage bufferedImage = tool.截取屏幕(w - 2, z - 4, w + 22, z + 4);
						bean.setImage(bufferedImage);
						tool.保存图片(bufferedImage, "截图" + bean.getRow() + "_" + bean.getColumn() + ".bmp");
					}
				}
			}

		}

		int groupNum = DataSave.lj.getGroupNum();

		private boolean TakeOutRes() {

			int x1, y1;// 仓库第一格坐标1607 441
			System.out.println("读取炼金材料!");
			x1 = cangku.x + 12 + 7;// 1611 446
			y1 = cangku.y + 99 + 15;
			Iterator<JGBean> iterator = ljOtherBean.getBody().iterator();
			// 判断物品够不够加工
			while (iterator.hasNext()) {
				JGBean bean = iterator.next();
				if (bean.getNum() - bean.getN() * groupNum < 0) {
					int tempNum = bean.getNum() / bean.getN();
					if (tempNum < groupNum) {
						groupNum = tempNum;
					}
				}
			}
			if (groupNum == 0) {
				return sy;
			}
			int state = 0;
			Point pp1 = new Point(cangku.x + 474, cangku.y + 217);
			tool.delay(500);
			tool.mouseMovePressOne(pp1.x, pp1.y, InputEvent.BUTTON1_MASK);
			tool.delay(500);
			iterator = ljOtherBean.getBody().iterator();
			while (iterator.hasNext()) {
				JGBean bean = iterator.next();
				// int w = x1 + (bean.getColumn() - 1) * 54;
				int row = bean.getRow();
				int linrow = (row - 1) / 9;
				if (row > 9) {
					// 1254,263,1267,349, 606 128
					// 计算拉取深度
					if (state != linrow) {
						tool.mouseMovePressOne(pp1.x, pp1.y + linrow * 143, InputEvent.BUTTON1_MASK);
						tool.delay(500);
						state = linrow;
					}
				} else {
					if (state != 0) {
						tool.mouseMovePressOne(pp1.x, pp1.y, InputEvent.BUTTON1_MASK);
						tool.delay(500);
					}
					state = 0;
				}
				int weizhi = ((bean.getRow() - 1) % 9) * 9 + bean.getColumn() - 1;
				int x = 0;
				Point point = null;
				while (isTrue) {
					if (weizhi - x < 0 && weizhi + x >= 90) {
						return false;
					}
					int l = weizhi - x;
					if (l >= 0) {
						int w = x1 + l % 9 * 51;
						int z = y1 + l / 9 * 51;
						point = tool.区域找图(w - 4, z - 6, w + 24, z + 6, 0.05, bean.getImage());
						System.out.println(bean.getRow() + " " + bean.getColumn() + " " + point);
						if (point != null) {
							point.x = l % 9 + 1;
							point.y = l / 9 + linrow * 9 + 1;
							bean.setRow(point.y);
							bean.setColumn(point.x);
							tool.mouseMovePressOne(w, z, InputEvent.BUTTON3_MASK);
							tool.delay(50);
							tool.mouseMove(w + 480, z);
							long time = System.currentTimeMillis();
							// tool.区域找色(w + 40, z, w + 197, z + 40, 0.03, 20,
							// "7DA4C1")
							while (isTrue && GameUIModel.FindXX2(tool, w + 120, z, w + 300, z + 60) == null) {
								tool.delay(100);
								if (System.currentTimeMillis() - time > 1000) {
									tool.保存截屏(w + 120, z, w + 300, z + 60, "7DA4C1.bmp");
									tool.mouseMovePressOne(w, z, InputEvent.BUTTON3_MASK);
									tool.delay(50);
									tool.mouseMove(w + 480, z);
									time = System.currentTimeMillis();
								}
							}
							if (!isTrue)
								return isTrue;
							DSHandle.输入数字(bean.getN() * groupNum + "");
							tool.delay(500);
							tool.keyPressOne(KeyEvent.VK_ENTER);
							tool.delay(500);
							bean.setNum(bean.getNum() - bean.getN() * groupNum);

							break;
						}
					}
					l = weizhi + x;
					if (l < 90) {
						int w = x1 + l % 9 * 51;
						int z = y1 + l / 9 * 51;
						point = tool.区域找图(w - 4, z - 6, w + 24, z + 6, 0.05, bean.getImage());
						if (point != null) {
							point.x = l % 9 + 1;
							point.y = l / 9 + linrow * 9 + 1;
							bean.setRow(point.y);
							bean.setColumn(point.x);
							tool.mouseMovePressOne(w, z, InputEvent.BUTTON3_MASK);
							long time = System.currentTimeMillis();

							while (isTrue && GameUIModel.FindXX2(tool, w + 120, z, w + 300, z + 60) == null) {
								tool.delay(100);
								if (System.currentTimeMillis() - time > 1000) {
									tool.mouseMovePressOne(w, z, InputEvent.BUTTON3_MASK);
									time = System.currentTimeMillis();
								}
							}
							DSHandle.输入数字(bean.getN() * groupNum + "");
							tool.delay(500);
							tool.keyPressOne(KeyEvent.VK_ENTER);
							tool.delay(500);
							bean.setNum(bean.getNum() - bean.getN() * groupNum);
							break;
						}
					}
					x++;
				}
			}
			return true;

		}

		private void Find(int model) {
			if (isTrue && DataSave.hs_map != null && DataSave.hs_map.address != 0) {
				while (isTrue) {
					if (model == COOK) {
						tool.lockDirection(DataSave.lj.getTaziDirection());
						tool.delay(500);
						int type = OpenConsole();
						if (type == NULL) {
							HuanTaizi();
						} else if (type == model) {
							return;
						} else {
							tool.keyPressOne(KeyEvent.VK_ESCAPE);
							tool.delay(500);
							DSHandle.AutoCheckIn();
							tool.delay(500);
						}
					} else {
						tool.lockDirection(DataSave.lj.getBaoxianDirection());
						tool.delay(500);
						int type = OpenConsole();
						if (type == model)
							return;
						else {
							tool.keyPressOne(KeyEvent.VK_ESCAPE);
							tool.delay(500);
							DSHandle.AutoCheckIn();
							tool.delay(500);
						}
					}
				}
				return;
			}
			while (isTrue) {
				DSHandle.AutoCheckIn();
				// 旋转并找到一个可操作的
				FindConsole(true);
				// 打开操作台
				int type = OpenConsole();
				if (!isTrue) {
					return;
				}
				if (model == COOK && type == NULL) {
					HuanTaizi();
				}

				if (type == model) {
					return;
				} else {
					tool.keyPressOne(KeyEvent.VK_ESCAPE);
					tool.delay(500);
					DSHandle.AutoCheckIn();
					tool.delay(500);
				}
				// 旋转并丢失一个可操作的
				FindConsole(false);
			}
		}

		private void startFHCK(int zuo, int size) {
			System.out.println("开始物品放回仓库!");// 2104 340 2109 485
			int x1 = beibao.x + 5;
			int y1 = beibao.y + 145;
			boolean bool = false;
			for (int i = sy ? zuo : 0; i < size && isTrue; i++) {
				Point p = new Point(x1 + i % 10 * 54, y1 + i / 10 * 54);
				if (null != tool.区域找图(p.x, p.y, p.x + 30, p.y + 30, 0.05, "空格子.bmp")) {
					if (!bool || i < zuo)
						continue;
					return;
				}
				bool = true;
				tool.mouseMovePressOne(p.x, p.y, InputEvent.BUTTON3_MASK);
				tool.delay(500);

				if (GameUIModel.FindXX2(tool, p.x - 300, p.y, p.x + 300, p.y + 60) != null) {
					tool.keyPressOne(KeyEvent.VK_F);
					tool.delay(200);
					tool.keyPressOne(KeyEvent.VK_ENTER);
					tool.delay(500);
				}
			}
		}

		Point liaoli, cangku, beibao;

		// 0未识别 1料理台 2 保险箱
//		private int OpenConsole() {
//			tool.keyPressOne(KeyEvent.VK_R);
//			tool.delay(1000);
//			// 查找烹饪的x
//			liaoli = GameUIModel.FindXX(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y,
//					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 200, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
//			if (liaoli == null) {
//				liaoli = GameUIModel.Find料理UI(tool, DataSave.SCREEN_X + 100, DataSave.SCREEN_Y + 80,
//						DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
//				if (liaoli != null) {
//					tool.delay(100);
//					tool.mouseMove(liaoli.x, liaoli.y + 5);
//					tool.delay(100);
//					tool.mousePress(InputEvent.BUTTON1_MASK);
//					tool.delay(100);
//					tool.mouseMove(DataSave.SCREEN_X + 50, liaoli.y + 5);
//					tool.delay(100);
//					tool.mouseRelease(InputEvent.BUTTON1_MASK);
//					liaoli.x += (DataSave.SCREEN_X + 50 - liaoli.x);
//					liaoli.x += 721;
//					liaoli.y += 4;
//				}
//				if (!isTrue)
//					return NULL;
//			}
//			cangku = GameUIModel.Findjgbb3(tool, DataSave.SCREEN_X + 100, DataSave.SCREEN_Y,
//					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
//			if (cangku == null) {
//				cangku = GameUIModel.FindXX(tool, DataSave.SCREEN_X + 100, DataSave.SCREEN_Y,
//						DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
//			}
//			beibao = GameUIModel.FindXX(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 100, DataSave.SCREEN_Y,
//					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
//			if (liaoli != null && beibao != null)
//				return COOK;
//			if (beibao != null && cangku != null) {
//				// 窗口拖拽 // 295-267
//				return SAFE;
//			}
//			return NULL;
//		}
		// 0未识别 1料理台 2 保险箱
		private int OpenConsole() {
			tool.keyPressOne(KeyEvent.VK_R);
			tool.delay(1000);
			if (!isTrue)
				return NULL;
			// 查找烹饪的x
			liaoli = GameUIModel.Find料理UI(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 500, DataSave.SCREEN_Y,
					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);

			cangku = GameUIModel.Find仓库UI(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 500, DataSave.SCREEN_Y,
					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 500, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);

			beibao = GameUIModel.Find背包UI(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 500, DataSave.SCREEN_Y,
					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 300, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
			if (liaoli != null && beibao != null) {
				System.out.println("找到料理台");
				return COOK;
			}
			if (beibao != null && cangku != null) {
				System.out.println("找到仓库台");
				// 窗口拖拽 // 295-267
				return SAFE;
			}
			return NULL;
		}

		private void FindConsole(boolean b) {
			// long time = System.currentTimeMillis();
			if (DataSave.hs_map != null && DataSave.hs_map.address != 0) {
				return;
			}
			DSHandle.AutoCheckIn();
			while (isTrue) {
				// int i = MouseInfo.getPointerInfo().getLocation().x;
				Point point = tool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH,
						DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT, 0.15, "操作.bmp");
				if (b) {
					if (point != null)
						return;
				} else {
					if (point == null) {
						return;
					}
				}
				// i -= DataSave.SCREEN_WIDTH / 8;
				DSHandle.转动镜头(8);
				// tool.mouseMove(i, (int) (DataSave.SCREEN_HEIGHT * 0.4) +
				// DataSave.SCREEN_Y);
				tool.delay(1);
				// if (System.currentTimeMillis() - time > 2000) {
				DSHandle.AutoCheckIn();
				// time = System.currentTimeMillis();
				// }
			}
		}
	}

}
