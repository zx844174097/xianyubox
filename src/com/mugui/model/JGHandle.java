package com.mugui.model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.mugui.http.Bean.JGOtherBean;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.Bean.JGOtherBean.JGBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.JGOtherPanel;
import com.mugui.ui.part.GameListenerThread;
import com.mugui.windows.Tool;

public class JGHandle {
	private static JGThread jgThread = null;
	private static JGExceptionThread exceptionThread = null;
	private static boolean isTrue = false;
	// private static Dimension yuan = null;
	// private static Point point_yuan = null;
	private static Dimension SCREEN_SIZE = null;
	private static Point SCREEN_LOCTION = null;

	public static boolean ds_bool = false;

	public static void start() {
		if (!HsFileHandle.isRunModel()) {
			return;
		}
		if (jgThread != null && jgThread.isAlive()) {
			return;
		}
		model_point = null;
		npcheck_point = null;
		SCREEN_SIZE = new Dimension(DataSave.SCREEN_WIDTH, DataSave.SCREEN_HEIGHT);
		SCREEN_LOCTION = new Point(DataSave.SCREEN_X, DataSave.SCREEN_Y);
		jgOtherPanel = DataSave.jg.getJGOtherList();
		isTrue = true;
		DSHandle.startDxgj();
		a = false;
		if (DSHandle.isRun()) {
			DSHandle.stop();
			ds_bool = true;
		}
		jgThread = new JGThread();
		jgThread.setNumber(0);
		jgThread.start();
		// 加工异常检测
		// exceptionThread = new JGExceptionThread();
		// exceptionThread.start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (JGHandle.isTrue) {
					TcpBag bag = new TcpBag();
					bag.setBag_id(TcpBag.ERROR);
					UserBean userBean = new UserBean();
					userBean.setCode("jg");
					bag.setBody(userBean.toJsonObject());
					TCPModel.SendTcpBag(bag);
					int i = 0;
					while (i <= 60) {
						Other.sleep(1000);
						i++;
						if (!JGHandle.isTrue)
							return;
					}
				}
			}
		}).start();
		DSHandle.AutoCheckIn();
	}

	public static void stop() {
		isTrue = false;
		if (jgThread != null) {
			jgThread.close();
			System.out.println("停止加工");
		}
		if (exceptionThread != null) {
			exceptionThread.close();
		}
		model_point = null;
		npcheck_point = null;
		if (QpHandle.isClose()) {
			QpHandle.close();
		}
		// if (point_yuan != null)
		// DataSave.StaticUI.setLocation(point_yuan);
		// if (yuan != null)
		// DataSave.StaticUI.setSize(yuan);
		// DataSave.StaticUI.setAlwaysOnTop(true);
	}

	private static JGOtherBean jgOtherBean = null;

	private static JGOtherPanel[] jgOtherPanel = null;
	private static int number = 0;
	private static boolean a = false;

	private static Point model_point = null;
	private static Point npcheck_point = null;

	// 加工主线程
	private static class JGThread extends Thread {
		private Tool tool = new Tool();
		private boolean isTrue = false;

		public void setNumber(int number) {
			JGHandle.number = number;
		}

		public JGThread() {
			isTrue = true;
		}

		@Override
		public void run() {

			// 从背包中选取色块
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (isTrue) {
				if (number == jgOtherPanel.length) {
					jgOtherPanel[number - 1].setStateText("所有加工已完成，正在重新开始进行校验！");
					number = 0;
				}
				System.out.println("加工编号" + number + "：时间：" + df.format(new Date(System.currentTimeMillis())));
				jgOtherBean = jgOtherPanel[number].getJGOtherBean();
				if (DataSave.jg.isSafeJG()) {
					openSafe();
				} else
					openCKBB();
				if (!isTrue)
					return;
				saveJGimg();
				if (!isTrue)
					return;
				boolean points = readyJGOthers();
				if (!points) {
					System.out.println("加工编号" + number + "：结束时间：" + df.format(new Date(System.currentTimeMillis())));
					number++;
					continue;
				}
				if (!DataSave.jg.isSafeJG() && DataSave.JGNP) {
					// 1259 715 1393,y=185
					if (DataSave.服务器.equals("私服")) {
						tool.mouseMovePressOne(bb_xy.x - 134 - 180, bb_xy.y + 565, InputEvent.BUTTON1_MASK);
					} else {// 1311 890 1409 168
						tool.mouseMovePressOne(bb_xy.x - 98, bb_xy.y + 782, InputEvent.BUTTON1_MASK);
					}
					tool.delay(2000);
				} else {
					readyJG();
				}
				// points = checkJGOthers();
				if (!isTrue)
					return;
				jgOtherPanel[number].setStateText("加工中!");
				System.out.println("number=" + number + " 加工中!");

				checkJGModel();
				if (!isTrue)
					return;
				checkBBOther();
				if (!isTrue)
					return;
				// 开始加工915, 455
				boolean sss = false;
				if (startJG()) {
					if (!isTrue)
						return;
					if (ds_bool && !DataSave.MAIDDQ) {
						DSHandle.start(null);
					}
					if (!isTrue)
						return;
					// 检测加工是否完成.
					checkJGOver();
					if (ds_bool) {
						DSHandle.stop();
					}
					if (!isTrue)
						return;
					jgOtherPanel[number].setStateText("加工已完成!");
					System.out.println("number=" + number + " 加工已完成!");
					sss = startJCNumber();
					if (!isTrue)
						return;
					if (!sss) {
						tool.keyPressOne(KeyEvent.VK_S);
						tool.delay(500);
						tool.keyPressOne(KeyEvent.VK_W);
					}
				}
				if (!isTrue)
					return;
				if (DataSave.jg.isSafeJG()) {
					openSafe();
				} else
					openCKBB();
				if (!isTrue)
					return;
				startFHCK(jgOtherBean.getBody().size() + 2);
				if (!isTrue)
					return;
				tool.keyPressOne(KeyEvent.VK_ESCAPE);
				tool.delay(1000);
				tool.keyPressOne(KeyEvent.VK_ESCAPE);
				tool.delay(1000);
				if (sss) {
					jgOtherPanel[number].setStateText("该加工物结束!");
					System.out.println("number=" + number + " 该加工物结束!");
					number++;
				}
			}

			System.out.println("停止加工·3");
		}

		private void openSafe() {

			// long time = System.currentTimeMillis();
			if (DataSave.hs_map != null && DataSave.hs_map.address != 0) {
				return;
			}
			DSHandle.AutoCheckIn();
			while (isTrue) {
				// int i = MouseInfo.getPointerInfo().getLocation().x;
				Point point = tool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH,
						DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT, 0.15, "操作.bmp");
				if (point != null) {
					tool.keyPressOne(KeyEvent.VK_R);
					tool.delay(1000);
					return;
				}
				DSHandle.转动镜头(8);

				tool.delay(1);
				DSHandle.AutoCheckIn();
			}

		}

		private Point bb_xy = null;

		private boolean readyJGOthers() {
			jgOtherPanel[number].setStateText("读取加工材料!");
			// Point[] points = new Point[jgOtherBean.getBody().size()];
			int x1 = SCREEN_LOCTION.x + SCREEN_SIZE.width;
			int y1 = SCREEN_LOCTION.y + SCREEN_SIZE.height / 2;

			x1 = bb_xy.x - 397;
			y1 = bb_xy.y + 131;

			Iterator<JGBean> iterator = jgOtherBean.getBody().iterator();
			// int n = 0;
			int state = 0;
			Point pp1 = new Point(bb_xy.x + 27, bb_xy.y + 188);
			tool.delay(500);
			tool.mouseMovePressOne(pp1.x, pp1.y, InputEvent.BUTTON1_MASK);
			tool.delay(500);
			while (iterator.hasNext()) {
				JGBean bean = iterator.next();
				// int w = x1 + (bean.getColumn() - 1) * 47;
				int row = bean.getRow();
				int linrow = (row - 1) / 8;
				if (row > 8) {
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
				int weizhi = ((bean.getRow() - 1) % 8) * 8 + bean.getColumn() - 1;

				int wx = x1 + weizhi % 8 * 54;
				int zx = y1 + weizhi / 8 * 54;
				tool.保存截屏(wx - 4, zx - 6, wx + 24, zx + 6, weizhi + ".bmp");

				int x = 0;
				Point point = null;
				while (isTrue) {
					if (weizhi - x < 0 && weizhi + x >= 64) {
						return false;
					}
					int l = weizhi - x;
					if (l >= 0) {
						int w = x1 + l % 8 * 54;
						int z = y1 + l / 8 * 54;
						point = tool.区域找图(w - 4, z - 6, w + 24, z + 6, 0.05, bean.getImage());
						if (point != null) {
							point.x = l % 8 + 1;
							point.y = l / 8 + linrow * 8 + 1;
							// points[n++] = point;
							bean.setColumn(point.x);
							bean.setRow(point.y);
							break;
						}
					}
					l = weizhi + x;
					if (l < 64) {
						int w = x1 + l % 8 * 54;
						int z = y1 + l / 8 * 54;
						point = tool.区域找图(w - 4, z - 6, w + 24, z + 6, 0.05, bean.getImage());
						if (point != null) {
							point.x = l % 8 + 1;
							point.y = l / 8 + linrow * 8 + 1;
							// points[n++] = point;
							bean.setColumn(point.x);
							bean.setRow(point.y);
							break;
						}
					}
					x++;
				}
			}
			return true;
		}

		private boolean readyJG() {
			Point pp1 = new Point(bb_xy.x + 27, bb_xy.y + 188);
			tool.delay(500);
			tool.mouseMovePressOne(pp1.x, pp1.y, InputEvent.BUTTON1_MASK);
			tool.delay(500);
			int state = 0;
			Iterator<JGBean> iterator = jgOtherBean.getBody().iterator();
			JGBean bean = null;
			while (iterator.hasNext()) {
				bean = iterator.next();
				int x1 = bb_xy.x - 397;
				int y1 = bb_xy.y + 131;
				int row = bean.getRow();
				int linrow = (row - 1) / 8;
				System.out.println(row);
				if (row > 8) {
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
				tool.mouseMovePressOne(x1 + (bean.getColumn() - 1) * 54 + 5, y1 + (bean.getRow() - 1) % 8 * 54 + 5,
						InputEvent.BUTTON3_MASK);
				tool.delay(500);
				byte[] b = DataSave.jg.getJGGS().trim().getBytes(Charset.forName("UTF-8"));
				for (int i = 0; i < b.length; i++) {
					tool.keyPressOne(Tool.getkeyCode("" + ((char) b[i])));
					tool.delay(500);
				}
				tool.keyPressOne(KeyEvent.VK_ENTER);
				tool.delay(1000);
				if (!isTrue)
					return false;
			}

			// if (DataSave.SCREEN_WIDTH <= 1440) {
			// int x1 = SCREEN_LOCTION.x + SCREEN_SIZE.width - 392;
			// int y1 = SCREEN_LOCTION.y + SCREEN_SIZE.height / 2 - 189;
			// int n = 0;
			// if (jgOtherBean.body.size() > 1) {
			// while (n < 2) {
			// tool.mouseMove(x1, y1);
			// tool.delay(200);
			// tool.mousePress(InputEvent.BUTTON1_MASK);
			// tool.delay(100);
			// tool.mouseMove(x1 + jgOtherBean.getBody().size() * 54, y1);
			// tool.delay(200);
			// tool.mouseRelease(InputEvent.BUTTON1_MASK);
			// tool.delay(200);
			// tool.mousePressOne(InputEvent.BUTTON1_MASK);
			// tool.delay(1000);
			// tool.keyPressOne(KeyEvent.VK_F);
			// tool.delay(1000);
			// tool.keyPressOne(KeyEvent.VK_ENTER);
			// tool.delay(1000);
			// n++;
			// x1 += 54;
			// }
			// } else {
			// tool.mouseMove(x1, y1);
			// tool.delay(200);
			// tool.mousePress(InputEvent.BUTTON1_MASK);
			// tool.delay(100);
			// tool.mouseMove(x1 + 2 * 54, y1);
			// tool.delay(200);
			// tool.mouseRelease(InputEvent.BUTTON1_MASK);
			// tool.delay(200);
			// tool.mousePressOne(InputEvent.BUTTON1_MASK);
			// tool.delay(1000);
			// tool.keyPressOne(KeyEvent.VK_F);
			// tool.delay(1000);
			// tool.keyPressOne(KeyEvent.VK_ENTER);
			// tool.delay(1000);
			// }
			// }
			// 关闭背包
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_L);
			tool.delay(500);
			return true;
		}

		private void checkBBOther() {
			jgOtherPanel[number].setStateText("选取加工材料!");
			// 1481 340
			int x1 = SCREEN_LOCTION.x + SCREEN_SIZE.width - 438;
			int y1 = SCREEN_LOCTION.y + SCREEN_SIZE.height / 2 - 200;
			// 选取物品
			Iterator<JGBean> iterator = jgOtherBean.getBody().iterator();
			int len = 0;
			int state = 0;
			Point pp1 = null;
			if (DataSave.JGNP) {
				tool.delay(500);
				if (npcheck_point == null) {
					npcheck_point = GameUIModel.FindXX2(tool,
							(int) (DataSave.SCREEN_WIDTH / 2) + 500 + DataSave.SCREEN_X, DataSave.SCREEN_Y + 80,
							DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
				}
				if (npcheck_point == null) {
					npcheck_point = GameUIModel.FindXX(tool,
							(int) (DataSave.SCREEN_WIDTH / 2) + 500 + DataSave.SCREEN_X, DataSave.SCREEN_Y + 80,
							DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
				}
				if (npcheck_point == null) {
					npcheck_point = GameUIModel.Findjgbb4(tool,
							(int) (DataSave.SCREEN_WIDTH / 2) + 500 + DataSave.SCREEN_X, DataSave.SCREEN_Y + 80,
							DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2);
				}
				System.out.println("npcheck_point:" + npcheck_point);
				if (npcheck_point == null) {
					npcheck_point = new Point(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 223,
							SCREEN_LOCTION.y + SCREEN_SIZE.height / 2 - 322);
				} // 1697 217

				pp1 = new Point(npcheck_point.x + 14, npcheck_point.y + 145);
				tool.mouseMovePressOne(pp1.x, pp1.y, InputEvent.BUTTON1_MASK);
				tool.delay(500);
			}
			while (iterator.hasNext()) {
				JGBean bean = iterator.next();
				if (DataSave.JGNP) {// 1347 353
					int row = bean.getRow();
					int linrow = (row - 1) / 8;
					if (row > 8) {
						// 1254,263,1267,349, 606 128 1712 391
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

					int x = npcheck_point.x - 397 - 54;
					int y = npcheck_point.y + 89;
					tool.mouseMovePressOne((bean.getColumn() - 1) * 54 + x + 5, (bean.getRow() - 1) % 8 * 54 + y + 5,
							InputEvent.BUTTON3_MASK);

				} else {
					// if (DataSave.SCREEN_WIDTH <= 1440) {
					// tool.mouseMovePressOne(x1 + (len + 2) * 54, y1,
					// InputEvent.BUTTON3_MASK);
					// } else
					tool.mouseMovePressOne(x1 + len * 54, y1, InputEvent.BUTTON3_MASK);
				}
				len++;
				tool.delay(1500);
				if (!isTrue)
					return;
			}
		}

		private void saveJGimg() {
			jgOtherPanel[number].setStateText("存储加工材料图鉴!");
			int x1, y1;// 仓库第一格坐标895 283 15 20

			bb_xy = findBB_XY();
			if (!isRun()) {
				return;
			}
			// 13 40
			x1 = bb_xy.x - 397;
			y1 = bb_xy.y + 131;
			if (!a) {
				a = true;
				// int s = 0;
				int state = 0;
				Point pp1 = new Point(bb_xy.x + 27, bb_xy.y + 188);
				tool.delay(500);
				tool.mouseMovePressOne(pp1.x, pp1.y, InputEvent.BUTTON1_MASK);
				tool.delay(500);
				for (JGOtherPanel panel : jgOtherPanel) {
					Iterator<JGBean> iterator = panel.getJGOtherBean().getBody().iterator();
					while (iterator.hasNext()) {
						JGBean bean = iterator.next();
						int w = x1 + (bean.getColumn() - 1) * 54;
						int row = bean.getRow();
						int linrow = (row - 1) / 8;
						if (row > 8) {
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
						int z = y1 + ((bean.getRow() - 1) % 8) * 54;
						BufferedImage bufferedImage = tool.截取屏幕(w - 2, z - 4, w + 22, z + 4);
						bean.setImage(bufferedImage);
						tool.保存图片(bufferedImage, bean.getColumn() + "." + bean.getRow() + ".bmp");
					}
				}
			}
		}

		private Point findBB_XY() {
			long time = System.currentTimeMillis();
			Point point = null;
			while (isTrue) {

				if (!DataSave.jg.isSafeJG()) {
					point = GameUIModel.Findjgbb4(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2,
							DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 400,
							DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 - 150);
					if (point != null) {
						point.x -= 13;
						point.y -= 40;
						break;
					}
					System.out.println("point:" + point);
					point = GameUIModel.Findjgbb3(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2,
							DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 400,
							DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 - 150);
					if (point != null) {
						point.x -= 13;
						point.y -= 40;
						break;
					}
					// 1393 144
					System.out.println("point:" + point);
					point = GameUIModel.FindXX(tool, SCREEN_LOCTION.x + SCREEN_SIZE.width / 2, SCREEN_LOCTION.y,
							SCREEN_LOCTION.x + SCREEN_SIZE.width - 400, SCREEN_LOCTION.y + SCREEN_SIZE.height / 2);
					if (point != null) {
						point.x -= 13;
						point.y -= 40;
						break;
					}
				} else {
//					tool.保存截屏(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y,
//							DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 400,
//							DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 - 150, "sala.bmp");
					point = GameUIModel.FindjgbbSafe(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2,
							DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 400,
							DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 - 150);
					if (point != null) {
						point.x -= 13;
						point.y -= 40 - 10;
						break;
					}
					point = GameUIModel.Findjgbb3(tool, SCREEN_LOCTION.x + 100, SCREEN_LOCTION.y + 50,
							SCREEN_LOCTION.x + SCREEN_SIZE.width / 2, SCREEN_LOCTION.y + SCREEN_SIZE.height / 2);
					if (point != null) {
						point.x -= 13;
						point.y -= 40;
						break;
					}
					System.out.println("point2:" + point);
					point = GameUIModel.FindXX(tool, SCREEN_LOCTION.x + 100, SCREEN_LOCTION.y + 50,
							SCREEN_LOCTION.x + SCREEN_SIZE.width / 2, SCREEN_LOCTION.y + SCREEN_SIZE.height / 2);
					if (point != null) {
						point.x -= 13;
						point.y -= 40;
						break;
					}
				}

				if (System.currentTimeMillis() - time > 3000) {
					time = System.currentTimeMillis();
					if (DataSave.jg.isSafeJG())
						openSafe();
					else {
						Point p = tool.区域找图EX(SCREEN_LOCTION.x + SCREEN_SIZE.width / 2 - 176,
								SCREEN_LOCTION.y + SCREEN_SIZE.height - 250,
								SCREEN_LOCTION.x + SCREEN_SIZE.width / 2 - 136,
								SCREEN_LOCTION.y + SCREEN_SIZE.height - 206, 0.07, "仓库.bmp");
						if (p != null) {
							tool.mouseMovePressOne(p.x + 5, p.y + 5, InputEvent.BUTTON1_MASK);
						} else {
							DSHandle.AutoCheckIn();
							openCKBB();
							tool.delay(1000);
						}
					}
				}
				tool.delay(1000);
			}
			System.out.println("仓库坐标:" + point);
			return point;
		}

		private void checkJGModel() {
			// 得到加工模式657,693,717,754 662 657
			jgOtherPanel[number].setStateText("选择加工模式!");
			// 搜索第一个X窗口
			// tool.保存截屏(DataSave.SCREEN_X + 600, DataSave.SCREEN_Y + 112,
			// DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 200,
			// DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2, "Lin2.bmp");
			while (model_point == null && isTrue
					&& (model_point = GameUIModel.Findjgbb3(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2,
							DataSave.SCREEN_Y + 100, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 400,
							DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 - 100)) == null) {
				tool.delay(500);
			}
			if (model_point == null)
				return;
			System.out.println("model_point:" + model_point);
			// 1156 219 772 614
			int x = model_point.x - 386;
			int y = DataSave.服务器.equals("私服") ? model_point.y + 385 : model_point.y + 395;
			int w = 62;
			int h = 62;
			String model = jgOtherPanel[number].getJGModel();
			for (int i = 1; i <= JGOtherPanel.jgStrings.length; i++) {
				if (JGOtherPanel.jgStrings[i - 1].equals(model)) {
					x += (i - 1) % 7 * w;
					y += (i - 1) / 7 * h;
					tool.mouseMovePressOne(x, y, InputEvent.BUTTON1_MASK);
					System.out.println("加工模式选择：model=" + model + " x=" + x + " y=" + y);
					break;
				}
				if (!isTrue)
					return;
			}
			tool.delay(1500);
		}

		private boolean startJG() {
			jgOtherPanel[number].setStateText("开始加工!");
			if (model_point == null)
				return true;

			if (DataSave.jg.isJGStone()) {
				tool.mouseMovePressOne(model_point.x - 206 + 143, model_point.y + 618, InputEvent.BUTTON1_MASK);
			} else {// 958 840 1156 219
				tool.mouseMovePressOne(model_point.x - 206, model_point.y + 618, InputEvent.BUTTON1_MASK);
			}
			tool.delay(3000);
			if (model_point == null && isTrue && (GameUIModel.FindXX2(tool, model_point.x - 20, model_point.y - 20,
					model_point.x + 50, model_point.y + 50)) != null) {
				// 负重
				if (DataSave.JGNP)
					tool.keyPressOne(KeyEvent.VK_ESCAPE);
				else {
					tool.keyPressOne(KeyEvent.VK_ESCAPE);
					tool.delay(500);
					tool.keyPressOne(KeyEvent.VK_ESCAPE);
				}
				tool.delay(1500);
				return false;
			}
			return true;

		}

		private void checkJGOver() {
			jgOtherPanel[number].setStateText("开始检查加工结束!");
			System.out.println("number=" + number + " 开始检查加工结束!");
			// 1072, 884, 1077, 902 1077 817
			int x1 = SCREEN_LOCTION.x + SCREEN_SIZE.width / 2 + 112;
			int y1 = SCREEN_LOCTION.y + SCREEN_SIZE.height - 196;
			Point p = null;
			long time = System.currentTimeMillis();
			while ((p = tool.区域找色(x1, y1 - 100, x1 + 5, y1 + 100, 0.07, 1, "89CF11")) == null && isTrue
					&& System.currentTimeMillis() - time < 2000) {
				Other.sleep(40);
			}
			if (p == null) {
				System.out.println("number=" + number + " 未检测到加工物开始加工!");
				jgOtherPanel[number].setStateText("未检测到加工物开始加工");
				return;
			}

			// 加工开始，启动女仆多抢
			if (DataSave.MAIDDQ) {
				tool.delay(1500);
				DSHandle.EatGRPJ();
				tool.delay(1500);
				DataSave.maidPoint1 = DataSave.jg.getMaidPoint1();
				DataSave.maidPoint2 = DataSave.jg.getMaidPoint2();
				DataSave.StaticUI.updateUI(DataSave.qpList);
				QpHandle.setModel(2);
				QpHandle.start();
			}
			while (tool.区域找色(p.x, p.y, p.x + 7, p.y + 21, 0.15, 1, "89CF11") != null) {
				tool.delay(50);
				if (!DataSave.MAIDDQ) {
					if (DSHandle.isEatGRPJ()) {
						if (DSHandle.isRun()) {
							DSHandle.stop();
						}
						DSHandle.EatGRPJ();
						if (ds_bool) {
							DSHandle.start(null);
							tool.delay(500);
						}
					}
				}
				if (!isTrue)
					return;
			}
			if (DataSave.MAIDDQ) {
				QpHandle.stop();
				DSHandle.AutoCheckIn();
				tool.delay(2000);
				DSHandle.AutoCheckIn();
			}
			tool.delay(1000);
		}

		private boolean startJCNumber() {
			// 1864, 298, 1884, 317
			jgOtherPanel[number].setStateText("开始检查是否更换下一个加工物!");
			System.out.println("number=" + number + "开始检查是否更换下一个加工物!");
			if (DataSave.JGNP) {
				return true;
			} else {
				int x1 = SCREEN_LOCTION.x + SCREEN_SIZE.width - 56;
				int y1 = SCREEN_LOCTION.y + SCREEN_SIZE.height / 2 - 242;
				Point p = tool.区域找图(x1, y1, x1 + 20, y1 + 19, 0.15, "背包.bmp");
				if (p != null) {
					tool.keyPressOne(KeyEvent.VK_ESCAPE);
					tool.delay(500);
					return true;
				}
			}
			return false;
		}

		private Point BB_XY = null;

		private void startFHCK(int size) {
			jgOtherPanel[number].setStateText("开始将加工物放回背包!");
			System.out.println("number=" + number + "开始将加工物放回背包!");
			if (BB_XY == null) {

				int x1 = SCREEN_LOCTION.x + SCREEN_SIZE.width - 448;
				int y1 = SCREEN_LOCTION.y + SCREEN_SIZE.height / 2 - 224;
				BB_XY = GameUIModel.FindXX(tool, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_Y,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT);

				if (BB_XY != null) {
					BB_XY.x -= 403;
					BB_XY.y += 120;
				} else {
					BB_XY = new Point(x1, y1);
				}
			}
			System.out.println(BB_XY);
			tool.delay(1000);// 1486 360 317
			boolean bool = false;
			for (int i = 0; i < size; i++) {

				if (null != tool.区域找图(BB_XY.x + i * 54, BB_XY.y, BB_XY.x + i * 54 + 18 + 15, BB_XY.y + 18 + 15, 0.05,
						"空格子.bmp")) {
					if (bool)
						return;
					else
						continue;
				}
				bool = true;
				tool.mouseMovePressOne(BB_XY.x + i * 54, BB_XY.y, InputEvent.BUTTON3_MASK);
				tool.delay(1000);
				tool.keyPressOne(KeyEvent.VK_F);
				tool.delay(1000);
				tool.keyPressOne(KeyEvent.VK_ENTER);
				tool.delay(1000);
			}
		}

		private Point CK_point = null;

		private void openCKBB() {
			jgOtherPanel[number].setStateText("打开背包!");
			tool.delay(1000);
			findNPC();
			System.out.println("打开仓库npc");
			tool.keyPressOne(KeyEvent.VK_R);
			tool.delay(1000);
			if (DataSave.warehousePoint.getX() == 0 || DataSave.warehousePoint.getY() == 0) {
				Point p = null;
				long time = System.currentTimeMillis();
				long time2 = System.currentTimeMillis();
				do {
					tool.delay(100);
					if (System.currentTimeMillis() - time > 3000) {
						if (CK_point != null && System.currentTimeMillis() - time2 > 20000) {
							p = CK_point;
							break;
						}
						tool.保存截屏(SCREEN_LOCTION.x + SCREEN_SIZE.width / 2 - 176,
								SCREEN_LOCTION.y + SCREEN_SIZE.height - 250,
								SCREEN_LOCTION.x + SCREEN_SIZE.width / 2 - 50,
								SCREEN_LOCTION.y + SCREEN_SIZE.height - 206, "仓库npc.bmp");
						tool.keyPressOne(KeyEvent.VK_ESCAPE);
						tool.delay(1000);
						if (!DSHandle.AutoCheckIn()) {
							tool.keyPressOne(KeyEvent.VK_S);
							tool.delay(1000);
						}
						System.out.println("未能发现仓库信息");
						findNPC();
						tool.keyPressOne(KeyEvent.VK_R);
						time = System.currentTimeMillis();
					} // 784 830 824 874
				} while ((p = tool.区域找图EX(SCREEN_LOCTION.x + SCREEN_SIZE.width / 2 - 176,
						SCREEN_LOCTION.y + SCREEN_SIZE.height - 250, SCREEN_LOCTION.x + SCREEN_SIZE.width / 2 - 50,
						SCREEN_LOCTION.y + SCREEN_SIZE.height - 206, 0.07, "仓库.bmp")) == null && isTrue);
				if (!isTrue)
					return;
				CK_point = p;
			} else {
				CK_point = DataSave.warehousePoint;
			}
			tool.mouseMovePressOne(CK_point.x + 50, CK_point.y + 15, InputEvent.BUTTON1_MASK);

			tool.delay(2000);
			System.out.println("寻找得到仓库信息");
		}

		public void close() {
			isTrue = false;
			if (ds_bool) {
				DSHandle.stop();
			}
			System.out.println("停止加工·");
		}

		// private BufferedImage npc_img1=null;
		// private BufferedImage npc_img2=null;
		int i = DataSave.SCREEN_WIDTH;

		private void findNPC() {
			Other.sleep(2000);
			DSHandle.AutoCheckIn();
			// 345,91,1594,419
			long time = System.currentTimeMillis();
			System.out.println("寻找npc!" + i);
			while (isTrue && System.currentTimeMillis() - time < 2000) {
				Point p = null;
				if ((p = tool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH, DataSave.SCREEN_HEIGHT,
						0.18, "对话.bmp")) != null) {
					// 659,233,815,381
					if ((tool.区域找图(p.x - 200 + DataSave.SCREEN_X < 0 ? 0 : p.x - 200, p.y,
							p.x + 200 > DataSave.SCREEN_WIDTH + DataSave.SCREEN_X
									? DataSave.SCREEN_WIDTH + DataSave.SCREEN_X
									: p.x + 200,
							p.y + 500, 0.18, "问候.bmp")) != null) {
						System.out.println("成功寻找npc!");
						return;
					}
				} else {
					if (System.currentTimeMillis() - time > 2000) {
						break;
					}
				}
			}
			tool.mouseMove(i, (int) (DataSave.SCREEN_HEIGHT * 0.4) + DataSave.SCREEN_Y);
			i -= 600;
			DSHandle.AutoCheckIn();
			if (i < -600 * 4) {
				i = 0;
				// 前往npc身边
				gotoNPC();
			}
		}

		private void gotoNPC() {
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			for (int i = 0; i < 3; i++) {
				tool.delay(500);
				DSHandle.AutoCheckIn();
			}
			if (!GameListenerThread.DJNI.isCorsurShow())
				tool.keyPressOne(KeyEvent.VK_CONTROL);
			tool.delay(200);
			Point p = null;
			long time = System.currentTimeMillis();
			while (isTrue) {
				// 1403,63,1534,165
				p = tool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 517, DataSave.SCREEN_Y + 63,
						DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 385, DataSave.SCREEN_Y + 160, 0.15, "贸易仓库.bmp");
				if (p != null)
					break;
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - (1920 - 1566),
						DataSave.SCREEN_Y + 17, InputEvent.BUTTON1_MASK);
				Other.sleep(2000);
				if (System.currentTimeMillis() - time > 5 * 1000) {
					if (!GameListenerThread.DJNI.isCorsurShow())
						tool.keyPressOne(KeyEvent.VK_CONTROL);
					tool.delay(200);
				}
			}
			if (!isTrue)
				return;
			tool.mouseMovePressOne(p.x, p.y, InputEvent.BUTTON3_MASK);
			if (!isTrue)
				return;
			tool.delay(1000);
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(5000);
			tool.delay(230);
			if (GameListenerThread.DJNI.isCorsurShow())
				tool.keyPressOne(KeyEvent.VK_CONTROL);
			tool.delay(700);
			if (!isTrue)
				return;
			tool.keyPressOne(KeyEvent.VK_S);
		}
	}

	// 加工异常检测
	private static class JGExceptionThread extends Thread {
		private boolean isTrue = false;
		private Tool tool = new Tool();

		@Override
		public void run() {
			isTrue = true;
			while (isTrue) {
				// 1566, 85, 1578, 102
				int x = SCREEN_LOCTION.x + SCREEN_SIZE.width - 354;
				int y = SCREEN_LOCTION.y + 85;
				while (isTrue && tool.区域找色(x, y, x + 12, y + 17, 0.07, 10, "33039F") == null) {
					tool.delay(50);
				}
				if (!isTrue)
					return;
				jgOtherPanel[number].setStateText("被异常检查停止的加工");
				System.out.println("number=" + number + " 被异常检查停止的加工!");
				jgThread.close();
				tool.delay(500);
				tool.keyPressOne(KeyEvent.VK_S);
				tool.delay(500);
				if (!isTrue)
					return;
				// tool.keyPressOne(KeyEvent.VK_W);
				tool.delay(1000);
				if (!isTrue)
					return;
				jgThread.isTrue = true;
				jgThread.openCKBB();
				jgThread.isTrue = false;
				if (!isTrue)
					return;
				jgThread.startFHCK(JGHandle.jgOtherBean.getBody().size() + 2);
				if (!isTrue)
					return;
				tool.keyPressOne(KeyEvent.VK_ESCAPE);
				tool.delay(1000);
				tool.keyPressOne(KeyEvent.VK_ESCAPE);
				tool.delay(3000);
				jgOtherPanel[number].setStateText("被异常检查开始的加工");
				System.out.println("number=" + number + " 被异常检查开始的加工");
				jgThread = new JGThread();
				jgThread.start();
			}
		}

		public void close() {
			isTrue = false;
		}
	}

	public static boolean isRun() {
		return isTrue;
	}

}
