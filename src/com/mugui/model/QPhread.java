package com.mugui.model;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import com.mugui.Dui.DimgFile;
import com.mugui.tool.ImgTool;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.TitleInfoPanel;
import com.mugui.ui.part.GameListenerThread;
import com.mugui.ui.part.qpOtherPanel;
import com.mugui.windows.Clipboard;
import com.mugui.windows.Tool;

import net.sourceforge.tess4j.Tesseract2;

//一个物品的少拍流程
public class QPhread extends Thread {
	//
	// 输入
	// 1 1 3 3 2 2 3 3 4 5 4 5 6 4
	// 输出
	// 1 0 1 2
	// 2 4 5 2
	// 3 2 7 4
	// 4 8 13 3
	// 5 9 11 2
	// 6 12 12 1
	//

	private static int state = -1;// 0,开始，1，等待,2,暂停,3,终

	public void close() {
		state = 3;
	}

	private ConcurrentHashMap<String, LinkedList<qpOtherPanel>> linkedList = new ConcurrentHashMap<>();
	private LinkedList<String> list_names = new LinkedList<>();
	public static int benX = 0;
	public static int benY = 0;
	public static int souX = 0;
	public static int souY = 0;
	public static Point qp_sou = null;
	private Tool tool = new Tool();
	private int num = -1;
	private int list_num = -1;

	@Override
	public void run() {
		state = 0;
		if (DataSave.MAIDDQ) {
			if (!GameListenerThread.DJNI.isCorsurShow()) {
				tool.keyPressOne(KeyEvent.VK_CONTROL);
				tool.delay(1500);
			}
			tool.delay(200);
			if (state == 3)
				return;
			tool.mouseMovePressOne(DataSave.maidPoint1.x, DataSave.maidPoint1.y, InputEvent.BUTTON1_MASK);
			tool.delay(700);
			if (state == 3)
				return;
			do {
				tool.mouseMovePressOne(DataSave.maidPoint2.x, DataSave.maidPoint2.y, InputEvent.BUTTON1_MASK);
				tool.delay(1500);
				if (state == 3)
					return;
			} while (!isBenXY());
			if (state == 3)
				return;
			setBenXY();// 398 861
			tool.mouseMovePressOne(benX - (1354 - 398), benY + (861 - 203), InputEvent.BUTTON1_MASK);
			tool.delay(500);
			if (state == 3)
				return;

		} else
			setBenXY();
		if (state == 3)
			return;
		// 开启金额检测
		if (DataSave.qp.isQp_jejc()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Tesseract2 instance = new Tesseract2();
					while (state != 3 && DataSave.qp.isQp_jejc()) {
						long money = DataSave.qp.getQp_jejc();
						if (money <= 0)
							return;
						Other.sleep(10000);// 512 838 635 858 1367 218
						if (state == 3)
							return;
						try {
							BufferedImage image = tool.截取屏幕(benX - 855, benY + 620, benX - 732, benY + 640);
							image = tool.得到图的特征图(image, 0.27, "81D0DB");
							if (state == 3)
								return;
							if (image == null)
								continue;
							image = ImgTool.grayscaleImage(image);
							if (state == 3)
								return;
							if (image == null)
								continue;
							image = ImgTool.binaryzationImage(image);
							if (state == 3)
								return;
							if (image == null) {
								continue;
							}
							image = ImgTool.clearAdhesion(image);
							if (state == 3)
								return;
							image = ImgTool.imageEnlarge(image, 6);
							if (state == 3)
								return;
							int i;// JNA
							if (state == 3)
								return;
							String result = instance.doOCR(image);
							if (state == 3)
								return;
							char[] c = result.trim().toCharArray();
							long num = 0;
							for (i = 0; i < c.length; i++) {
								if (c[i] >= '0' && c[i] <= '9') {
									num = num * 10 + c[i] - '0';
								}
							}
							if (num <= 0) {
								continue;
							}
							DataSave.qp.setJe_s(num + "");
							if (num <= money) {
								QpHandle.stop();
								return;
							}
							if (state == 3)
								return;
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}

					}
				}
			}).start();
		}
		if (QpHandle.getModel() == 2) {
			waitQpOther();
			if (state == 3)
				return;
			num = -1;
			RUN();
		} else if (QpHandle.getModel() == 3) {// [x=1354,y=202]
			long time = System.currentTimeMillis();
			while (state != 3) {
				if (System.currentTimeMillis() - time > 10 * 1000) {
					tool.mouseMovePressOne(benX - 400, benY + 130, InputEvent.BUTTON1_MASK);
					tool.delay(DataSave.qp.getQp_Start());
					// 812 769
					tool.mouseMovePressOne(benX - 681, benY + 573, InputEvent.BUTTON1_MASK);
					tool.delay(DataSave.qp.getQp_Color() + 500);
					int n = 0;// 601 749 649 795
					while (state != 3 && tool.区域找图EX(benX - 753, benY + 547, benX - 705, benY + 593, 0.07, "交易上一页.bmp") != null) {
						if (n == 3) {
							tool.keyPressOne(KeyEvent.VK_ENTER);
							tool.delay(200);
						}
						tool.mouseMovePressOne(benX - 681, benY + 573, InputEvent.BUTTON1_MASK);
						tool.delay(DataSave.qp.getQp_Color() + 500);
						n++;
					}
					time = System.currentTimeMillis();
				}
				Point p = null;
				for (int i = 0; i < 8; i++) {// 659D81 861 336 928 352
					p = tool.区域找色(benX - 493, benY + 134 + i * 63, benX - 426, benY + 150 + i * 63, 0.12, 20, "659D81");
					if (p != null) {
						tool.mouseMovePressOne(p.x + 200, p.y, InputEvent.BUTTON1_MASK);
						buyObject();
						time = System.currentTimeMillis();
					}
				}
			}
		} else {
			buyObject();
		}
	}

	private boolean isBenXY() {
		Point point = new Point(DataSave.SCREEN_WIDTH / 2 + 394 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 - 337 + DataSave.SCREEN_Y);
		Point p = null;
		long time = System.currentTimeMillis();
		while (System.currentTimeMillis() - time < 5 * 1000 && (p = GameUIModel.FindXX(tool, point.x - 30, point.y - 20, point.x + 30, point.y + 20)) == null) {
			Other.sleep(40);
			if (state == 3)
				return true;
		}
		if (p != null) {
			return true;
		}
		return false;
	}

	private void RUN() {
		// 初始化收藏
		tool.mouseMovePressOne(qp_sou.x + 206, qp_sou.y + 17, InputEvent.BUTTON1_MASK);

		// tool.mouseMovePressOne(souX-(298-196), souY+(410-63),
		// InputEvent.BUTTON1_MASK);
		// tool.mouseMovePressOne(benX + 196, benY + 410,
		// InputEvent.BUTTON1_MASK);
		tool.delay(500);
		tool.keyPressOne(KeyEvent.VK_ENTER);
		tool.delay(200);
		// 1367 227
		while (state != 3) {
			if (addQp_other()) {
				while (state != 3) {
					// 移动到这个物品的分类处
					moveObjectType();
					// 输入这个物品的等级或者名称分类
					moveObjectLevel();
					// 输入这个物品的名称进行搜索
					writeObjectName();
					// 根据颜色点击这个物品
					tool.delay(DataSave.qp.getQp_Color() + 1200);
					if (moveObjectColor() == null) {
						if (state == 3)
							return;
						tool.mouseMovePressOne(benX - 400, benY + 130, InputEvent.BUTTON1_MASK);
					}
					int n = 1;
					// 判断是不是抢拍这个物品
					tool.delay(DataSave.qp.getQp_ObjectTime());
					while (state != 3 && !isQpObject() && n < 8) {
						// 切换下一个物品
						// 点击上一页 652 774
						tool.mouseMovePressOne(benX - 681, benY + 573, InputEvent.BUTTON1_MASK);
						tool.delay(DataSave.qp.getQp_Color() + 500);
						int nn = 0;
						while (tool.区域找图EX(benX - 753, benY + 547, benX - 705, benY + 593, 0.15, "交易上一页.bmp") != null) {
							if (nn == 3) {
								tool.keyPressOne(KeyEvent.VK_ENTER);
								tool.delay(200);
							}
							tool.mouseMovePressOne(benX - 681, benY + 573, InputEvent.BUTTON1_MASK);
							tool.delay(DataSave.qp.getQp_Color() + 500);
							nn++;
						}
						if (moveObjectColor() == null) {
							if (state == 3)
								return;
							tool.mouseMovePressOne(benX - 400, benY + 130 + n * 62, InputEvent.BUTTON1_MASK);
						}
						tool.delay(DataSave.qp.getQp_ObjectTime());
						n++;
						if (n == 8)
							break;
					}
					if (n != 8) {
						break;
					}
				}
				if (state == 3)
					return;
				// 加入收藏1463,627
				tool.delay(200);
				tool.mouseMovePressOne(qp_sou.x + 67, qp_sou.y + 17, InputEvent.BUTTON1_MASK);

				// tool.mouseMovePressOne(souX-(298-96), souY+(410-63),
				// InputEvent.BUTTON1_MASK);
				// tool.mouseMovePressOne(benX + 96, benY + 410,
				// InputEvent.BUTTON1_MASK);
				tool.delay(500);
			}
			// 开始购买这个物品
			buyObject();
		}
	}

	private boolean addQp_other() {
		qpOtherPanel panel = QPtempData.getQp_other();
		LinkedList<qpOtherPanel> temp = linkedList.get(panel.getDimgFile().objectname);
		if (temp == null) {
			temp = new LinkedList<>();
			temp.add(panel);
			linkedList.put(panel.getDimgFile().objectname, temp);
			list_names.add(panel.getDimgFile().objectname);
			num = list_names.size() - 1;
			list_num = 0;
			main = panel.getDimgFile();
			mainPanel = panel;
			panel.setStateText("开始抢拍");
			return true;
		} else {
			addList_other_one(temp, panel);
			return false;
		}

	}

	private void addList_other_one(LinkedList<qpOtherPanel> temp, qpOtherPanel panel) {
		Iterator<qpOtherPanel> iterator = temp.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getDimgFile() == panel.getDimgFile()) {
				return;
			}
		}
		temp.add(panel);
	}

	private void waitQpOther() {
		while (state != 3 && QPtempData.isQp_other() == null) {
			Other.sleep(50);
		}
	}

	private DimgFile main = null;
	private qpOtherPanel mainPanel = null;

	private Point moveObjectColor() {
		Point p = null;
		if (main.objectcolor.equals("任意")) {
			p = new Point(benX - 400, benY + 130);
			tool.mouseMovePressOne(p.x, p.y, InputEvent.BUTTON1_MASK);
			return p;
		}
		if (main.objectcolor.equals("白")) {
			String string[] = { "黄", "蓝", "绿" };
			for (int i = 0; i < 8; i++) {
				for (String s : string) {
					p = tool.区域找图EX(benX - 768, benY + 103 + i * 62, benX - 721, benY + 110 + i * 62, 0.12, s + "null.bmp");
					if (p != null) {
						break;
					}
				}
				if (p == null) {
					p = new Point(benX - 768 + 200, benY + 103 + i * 62 + 20);
					tool.mouseMovePressOne(p.x, p.y, InputEvent.BUTTON1_MASK);
					return p;
				}
			}
		} else {
			// 598 320 649 806
			for (int i = 0; i < 8; i++) {
				p = tool.区域找图EX(benX - 768, benY + 103 + i * 62, benX - 721, benY + 110 + i * 62, 0.12, main.objectcolor + "null.bmp");
				if (p != null && tool.区域找图(p.x - 3, p.y - 3, p.x + 50, p.y + 50, 0.13, main.bufferedImage) != null) {
					p = new Point(p.x + 200, p.y + 20);
					tool.mouseMovePressOne(p.x + 200, p.y + 20, InputEvent.BUTTON1_MASK);
					return p;
				}
			}
		}
		return null;
	}

	public BufferedImage startTest(DimgFile file) {
		main = file;
		setBenXY();
		// 移动到这个物品的分类处
		moveObjectType();
		// 输入这个物品的等级或者名称分类
		moveObjectLevel();
		// 输入这个物品的名称进行搜索
		writeObjectName();
		tool.delay(DataSave.qp.getQp_Color() + 1200);
		// 根据颜色点击这个物品
		Point p = moveObjectColor2();
		if (p != null) {
			tool.mouseMovePressOne(p.x + 200, p.y, InputEvent.BUTTON1_MASK);
		} else {
			tool.mouseMovePressOne(benX - 400, benY + 130, InputEvent.BUTTON1_MASK);
		}
		addLoginNote();
		return getQpObjectImg();
	}

	public BufferedImage startTest2(DimgFile file) {
		Other.sleep(500);
		return file.bufferedImage = getQpObjectImg();
	}

	private Point moveObjectColor2() {
		tool.delay(500);
		if (main.objectcolor.equals("任意")) {
			return null;
		}
		// 598 320 649 806
		Point p = null;
		long time = System.currentTimeMillis();
		while (state != 3 && System.currentTimeMillis() - time < DataSave.qp.getQp_Color()) {
			for (int i = 0; i < 8; i++) {
				p = tool.区域找图EX(benX - 768, benY + 103 + i * 62, benX - 721, benY + 110 + i * 62, 0.12, main.objectcolor + "null.bmp");
				if (p != null) {
					p.x = p.x + 200;
					return p;
				}
			}
		}
		return null;
	}

	private void addLoginNote() {// 1267 772 1354 203
		tool.delay(300);
		tool.mouseMovePressOne(benX - 88, benY + 569, InputEvent.BUTTON1_MASK);
	}

	private BufferedImage getQpObjectImg() {// 617 248 658 289
		tool.delay(200);
		return tool.截取屏幕(benX - 737 + 5, benY + 45 + 5, benX - 737 + 5 + 32, benY + 45 + 5 + 32);
	}

	private void setBenXY() {
		// 1354 203
		benX = DataSave.SCREEN_WIDTH / 2 + 394 + DataSave.SCREEN_X;
		benY = DataSave.SCREEN_HEIGHT / 2 - 337 + DataSave.SCREEN_Y;
		System.out.println(benX + " " + benY);
		Point p = null;
		long time = System.currentTimeMillis();
		while (System.currentTimeMillis() - time < 5 * 1000 && (p = GameUIModel.FindXX(tool, DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2, DataSave.SCREEN_Y,
				DataSave.SCREEN_WIDTH - 300 + DataSave.SCREEN_X, DataSave.SCREEN_X + DataSave.SCREEN_HEIGHT)) == null) {
			Other.sleep(40);
			if (state == 3)
				return;
		} 
		if (p != null) {
			System.out.println(p); 
			benX = p.x;
			benY = p.y;
		}
		time = System.currentTimeMillis();
		p = null;// 1354 203 1648 247 1740 290
		while (System.currentTimeMillis() - time < 5 * 1000 && (p = GameUIModel.FindXX(tool, benX + 281, benY + 29, benX + 386, benY + 90)) == null) {
			Other.sleep(40);
			if (state == 3)
				return;
		}
		System.out.println("sou:" + p);// 1696 263
		if (p != null) {
			souX = p.x;
			souY = p.y;
		} else {
			// 1354 214 1696 274
			souX = benX + 342;
			souY = benY + 60;
		}
		time = System.currentTimeMillis();
		p = null;
		tool.保存截屏(benX + 25, souY + 350, souX - 230, souY + 500, "QP_SOU.bmp");
		while (System.currentTimeMillis() - time < 5 * 1000 && (p = tool.区域找图EX(benX + 25, souY + 350, souX - 230, souY + 500, 0.11, "QP_SOU.bmp")) == null) {
			tool.delay(200);
			if (state == 3)
				return;
		}
		System.out.println("sou2:" + p);
		if (p != null) {//
			qp_sou = new Point(p.x + 50, p.y + 5);
		} else {// 1463 717
			qp_sou = new Point(souX - 233, souY + 454);
		}
	}

	private long time = 0;
	private static BufferedImage goumai = null;
	private static BufferedImage qptz = null;
	// private static BufferedImage qsgm = null;
	private long tt;

	private void buyObject() {
		time = System.currentTimeMillis();
		while (state != 3 && QpHandle.isClose()) {
			// 1288,338,1315,356
			int xx = 0;
			if (QpHandle.getModel() != 1) {
				xx = DataSave.qp.getQp_Start() / 2;
			}
			tool.delay(xx);
			if (System.currentTimeMillis() - time >= 3 * 1000) {
				tool.mouseMovePressOne(benX - 569, benY + 564, InputEvent.BUTTON1_MASK);
			}
			tool.delay(xx);

			if (state == 1) {
				state = 2;
				while (state == 2) {
					Other.sleep(50);
				}
			}
			if (state == 3) {
				return;
			}
			int x1 = benX - 74;
			int x2 = benX - 47;
			int y1 = benY + 125;
			int y2 = benY + 143;
			int h = 62;
			// 得到抢拍特征图 1300 269 1359 288 B35779 1354 202
			qptz = tool.得到特征点集合图(benX - 54, benY + 67, benX + 5, benY + 86, 0.03, "B35779");
			boolean tz = false;
			tt = System.currentTimeMillis();
			// 当前物品价格储存
			BufferedImage now_meony = null;
			boolean b_menony = false;
			for (int i = 0; i < 7; i++) {
				// 判断抢拍特征图是否变化
				if (qptz != null && tz && System.currentTimeMillis() - tt > 1000
						&& tool.区域找图EX(benX - 54, benY + 67, benX + 5, benY + 86, 0.03, qptz) == null) {
					i = 0;
					tt = System.currentTimeMillis();
					x1 = benX - 74;
					x2 = benX - 26;
					y1 = benY + 125;
					y2 = benY + 147;
					qptz = tool.得到特征点集合图(benX - 54, benY + 67, benX + 5, benY + 86, 0.03, "B35779");
					tool.mouseMovePressOne(benX - 569, benY + 564, InputEvent.BUTTON1_MASK);
					tool.delay(DataSave.qp.getQp_Start() / 2);
				}
				if (qptz == null) {
					qptz = tool.得到特征点集合图(benX - 54, benY + 67, benX + 5, benY + 86, 0.03, "B35779");
				}
				// 1280 327 1328 349
				if (tool.区域找色(x1, y1, x2, y2, 0.15, 10, "CECECE") != null && state != 3) {
					if (i == 0 || !b_menony) {
						// 点击购买
						buy(x1, x2, y1, y2);
						b_menony = false;
					} else {// 记录价格739 334 875 355
						BufferedImage now_meony2 = tool.得到特征点集合图(x1 - 541, y1 + 7, x1 - 405, y1 + 28, 0.05, "6A6AF2");
						// 判断价格是否变化，如果没有就购买
						if (now_meony != null && now_meony2 != null && tool.图中找图EX(now_meony, now_meony2, 0.15, 0, 0) != null) {
							// System.out.println("相同价格购买");
							buy(x1, x2, y1, y2);
						} else {
							if (state == 3) {
								return;
							}
							b_menony = false;
							for (; i < 7; i++) {// 618,315,631,328
								if (tool.区域找色(x1, y1, x2, y2, 0.15, 10, "CECECE") != null
										&& tool.区域找图EX(x1 - 662, y1 - 12, x1 - 649, y1 + 1, 0.05, "B.bmp") != null && state != 3) {
									buy(x1, x2, y1, y2);
								}
								y1 += h;
								y2 += h;
							}
							if (tool.区域找色(benX - 74, benY + 125, benX - 26, benY + 147, 0.15, 10, "CECECE") == null) {
								break;
							} else {
								i = -1;
								x1 = benX - 74;
								x2 = benX - 26;
								y1 = benY + 125 - h;
								y2 = benY + 147 - h;
								tz = true;
							}
						}
					}

					if (!b_menony && tool.区域找图EX(x1 - 662, y1 - 12, x1 - 649, y1 + 1, 0.05, "B.bmp") != null) {
						b_menony = true;
						// 记录价格 715 348 600 322 796 369
						now_meony = tool.得到特征点集合图(x1 - 541, y1 + 7, x1 - 405, y1 + 28, 0.05, "6A6AF2");
					}
				} else {
					if (state == 3) {
						return;
					}
					b_menony = false;
					for (; i < 7; i++) {
						if (tool.区域找色(x1, y1, x2, y2, 0.15, 10, "CECECE") != null && tool.区域找图EX(x1 - 662, y1 - 12, x1 - 649, y1 + 1, 0.05, "B.bmp") != null
								&& state != 3) {
							buy(x1, x2, y1, y2);
						}
						y1 += h;
						y2 += h;
					}
					if (tool.区域找色(benX - 74, benY + 125, benX - 26, benY + 147, 0.15, 10, "CECECE") == null) {
						break;
					} else {
						i = -1;
						x1 = benX - 74;
						x2 = benX - 26;
						y1 = benY + 125 - h;
						y2 = benY + 147 - h;
						tz = true;
					}
				}
				if (state == 1) {
					state = 2;
					while (state == 2) {
						Other.sleep(50);
					}
				}
				y1 += h;
				y2 += h;
				if (DataSave.qp.isForceRefresh() && System.currentTimeMillis() - time >= 3 * 1000) {
					time = System.currentTimeMillis();
					tool.mouseMovePressOne(benX - 569, benY + 564, InputEvent.BUTTON1_MASK);
					tool.delay(DataSave.qp.getQp_Replace());
				}
			}
			// 等待
			if (state == 1) {
				state = 2;
				while (state == 2) {
					Other.sleep(50);
				}
			}
			if (state == 3) {
				return;
			}
			// 6b4d97,1343,179,1350,280 1354 202
			if (QpHandle.getModel() == 2 && mainPanel.getZend_time() >= System.currentTimeMillis()) {
				int i = tool.区域找色点数量(benX - 11, benY + 74, benX - 4, benY + 78, "B15678");
				if (i == 6) {
					mainPanel.setEnd_time(System.currentTimeMillis());
				}
			}
			// 判断是否加入一个新的物品进入这个列表
			if (QpHandle.getModel() == 2 && isAddQp_other()) {
				return;
			}
			// 793,774
			if (QpHandle.getModel() == 2) {
				// 更换下一个抢拍物品1609,331 1367 227 1602,322,1613,361
				// do {
				while (state != 3) {
					EWCZ();
					if (state == 3) {
						return;
					}
					if (linkedList.get(list_names.get(num)).size() - 1 == list_num) {
						num = (num + 1) % linkedList.size();
						// 1696 262 1575 381
						tool.mouseMovePressOne(qp_sou.x + 135, qp_sou.y - 334 + num * 34, InputEvent.BUTTON1_MASK);
						list_num = 0;
						mainPanel = linkedList.get(list_names.get(num)).get(list_num);
						main = mainPanel.getDimgFile();
						// tool.delay(DataSave.qp.getQp_Color() + 1200);
						if (isQpObject()) {
							System.out.println("跳出");
							break;
						}
					} else {
						list_num++;
						mainPanel = linkedList.get(list_names.get(num)).get(list_num);
						main = mainPanel.getDimgFile();
					}
					if (state == 3) {
						return;
					}
					if (moveObjectColor() != null) {// && state != 3);
						// 判断是不是抢拍这个物品
						tool.delay(DataSave.qp.getQp_ObjectTime());
						if (isQpObject()) {
							break;
						}
						tool.mouseMovePressOne(benX - 681, benY + 573, InputEvent.BUTTON1_MASK);
						tool.delay(DataSave.qp.getQp_Color() + 500);
						int n = 0;
						while (tool.区域找图EX(benX - 753, benY + 547, benX - 705, benY + 593, 0.15, "交易上一页.bmp") != null) {
							if (n == 3) {
								tool.keyPressOne(KeyEvent.VK_ENTER);
								tool.delay(200);
							}
							tool.mouseMovePressOne(benX - 681, benY + 573, InputEvent.BUTTON1_MASK);
							tool.delay(DataSave.qp.getQp_Color() + 500);
							n++;
						}
					}
					if (state == 3) {
						return;
					}

				}
			} else if (QpHandle.getModel() == 3) {
				tool.mouseMovePressOne(benX - 681, benY + 573, InputEvent.BUTTON1_MASK);
				tool.delay(DataSave.qp.getQp_Color() + 500);

				// 673 775 1354,y=202
				int n = 0;
				while (state != 3 && tool.区域找图EX(benX - 753, benY + 547, benX - 705, benY + 593, 0.07, "交易上一页.bmp") != null) {
					if (n == 3) {
						tool.keyPressOne(KeyEvent.VK_ENTER);
						tool.delay(200);
					}
					tool.mouseMovePressOne(benX - 681, benY + 573, InputEvent.BUTTON1_MASK);
					tool.delay(DataSave.qp.getQp_Color() + 500);
					n++;
				}
				return;
			} else if (System.currentTimeMillis() - time >= 3 * 1000) {
				time = System.currentTimeMillis();
				tool.mouseMovePressOne(benX - 569, benY + 564, InputEvent.BUTTON1_MASK);
				if (state == 3) {
					return;
				}
				if (DSHandle.isEatGRPJ() || isBMFCK()) {
					// 清空收藏夹并添加
					// 初始化收藏
					tool.mouseMovePressOne(qp_sou.x + 206, qp_sou.y + 17, InputEvent.BUTTON1_MASK);

					// tool.mouseMovePressOne(benX + 196, benY + 410,
					// InputEvent.BUTTON1_MASK);
					tool.delay(500);
					tool.keyPressOne(KeyEvent.VK_ENTER);
					tool.delay(200);
					// 加入收藏1463,627
					tool.delay(200);
					tool.mouseMovePressOne(qp_sou.x + 67, qp_sou.y + 17, InputEvent.BUTTON1_MASK);

					// tool.mouseMovePressOne(souX-(298-96), souY+(410-63),
					// InputEvent.BUTTON1_MASK);
					// tool.mouseMovePressOne(benX + 96, benY + 410,
					// InputEvent.BUTTON1_MASK);
					tool.delay(500);
					EWCZ();
					if (state == 3) {
						return;
					}
					// 点击收藏夹继续抢夺
					tool.delay(2000);
					// souX=benX+298;
					// souY=benY+63; 1400 703 1535 369
					tool.mouseMovePressOne(qp_sou.x + 135, qp_sou.y - 334, InputEvent.BUTTON1_MASK);
					tool.delay(2000);
					if (state == 3) {
						return;
					}
					tool.mouseMovePressOne(benX - 400, benY + 130, InputEvent.BUTTON1_MASK);
					tool.delay(1000);
					if (state == 3) {
						return;
					}
				}
			}
			if (state == 3) {
				return;
			}
		}
	}

	private long bm_time = System.currentTimeMillis();

	private boolean isBMFCK() {
		if (!DataSave.qp.isDs_fck()) {
			return false;
		}
		if (System.currentTimeMillis() - bm_time > DataSave.qp.getCf_time()) {
			return true;
		}
		return false;
	}

	private String npc_name = null;

	private void BMFCK() {
		if (!isBMFCK()) {
			return;
		}
		if (state == 3) {
			return;
		}
		npc_name = "仓库";
		gotoNpc();
		if (state == 3) {
			return;
		}
		// 寻找npc
		npc_name = "仓库";
		openNpc();
		if (state == 3) {
			return;
		}
		GameUIModel.startFHCK(tool, 6);
		if (state == 3) {
			return;
		}
		tool.delay(1000);
		tool.keyPressOne(KeyEvent.VK_ESCAPE);
		if (state == 3) {
			return;
		}
		tool.delay(1000);
		tool.keyPressOne(KeyEvent.VK_ESCAPE);
		if (state == 3) {
			return;
		}
		tool.delay(1000);
		npc_name = "交易所";
		gotoNpc();
		if (state == 3) {
			return;
		}
		bm_time = System.currentTimeMillis();
	}

	private void openNpc() {
		tool.delay(1000);
		findNPC();
		System.out.println("打开" + npc_name + "npc");
		tool.keyPressOne(KeyEvent.VK_R);
		Point p = null;
		long time = System.currentTimeMillis();
		int num = 0;

		int x1 = 0, y1 = 0, x2 = 0, y2 = 0;

		switch (npc_name) {
		case "交易所":
			x1 = DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 50;
			y1 = DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 250;
			x2 = DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 120;
			y2 = DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 206;
			break;
		case "仓库":
			x1 = DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 176;
			y1 = DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 250;
			x2 = DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 50;
			y2 = DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 206;

		default:
			break;
		}
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
		} while ((p = tool.区域找图EX(x1, y1, x2, y2, 0.07, npc_name + ".bmp")) == null && state != 3);
		if (state == 3)
			return;
		// 884, 868
		tool.delay(1000);
		tool.mouseMovePressOne(p.x + 20, p.y + 5, InputEvent.BUTTON1_MASK);
		tool.delay(1000);
		System.out.println("寻找得到" + npc_name + "信息");
	}

	int i = DataSave.SCREEN_WIDTH;

	private void findNPC() {
		Other.sleep(2000);
		DSHandle.AutoCheckIn();
		System.out.println("寻找npc!");
		// 345,91,1594,419
		long time = System.currentTimeMillis();
		System.out.println("寻找npc!" + i);
		while (state != 3 && System.currentTimeMillis() - time < 2000) {
			Point p = null;
			if ((p = tool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH, DataSave.SCREEN_HEIGHT, 0.18, "对话.bmp")) != null) {
				// 659,233,815,381
				if ((tool.区域找图(p.x - 200 + DataSave.SCREEN_X < 0 ? 0 : p.x - 200, p.y,
						p.x + 200 > DataSave.SCREEN_WIDTH + DataSave.SCREEN_X ? DataSave.SCREEN_WIDTH + DataSave.SCREEN_X : p.x + 200, p.y + 500, 0.18,
						"问候.bmp")) != null) {
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
			gotoNpc();
		}
	}

	private void gotoNpc() {
		tool.keyPressOne(KeyEvent.VK_ESCAPE);
		tool.delay(500);
		tool.keyPressOne(KeyEvent.VK_ESCAPE);
		tool.delay(500);
		if (state == 3) {
			return;
		}
		for (int i = 0; i < 2; i++) {
			tool.delay(500);
			DSHandle.AutoCheckIn();
		}
		if (state == 3) {
			return;
		}
		tool.keyPressOne(KeyEvent.VK_CONTROL);
		tool.delay(200);
		Point p = null;
		long time = System.currentTimeMillis();
		while (state != 3) {
			// 1403,63,1534,165
			p = tool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 722, DataSave.SCREEN_Y + 63, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 385,
					DataSave.SCREEN_Y + 350, 0.10, "贸易" + npc_name + ".bmp");
			if (p != null)
				break;
			tool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 354, DataSave.SCREEN_Y + 39, InputEvent.BUTTON1_MASK);
			tool.delay(2000);
			if (state == 3) {
				return;
			}
			if (System.currentTimeMillis() - time > 10 * 1000) {
				tool.keyPressOne(KeyEvent.VK_CONTROL);
				tool.delay(200);
			}
		}
		if (state == 3) {
			return;
		}
		tool.mouseMovePressOne(p.x + 5, p.y + 5, InputEvent.BUTTON3_MASK);
		if (state == 3)
			return;
		tool.delay(1000);
		tool.keyPressOne(KeyEvent.VK_ESCAPE);
		tool.delay(5000);
		tool.delay(230);
		tool.keyPressOne(KeyEvent.VK_CONTROL);
		tool.delay(700);
		if (state == 3)
			return;
		waitSleep();
	}

	private void waitSleep() {
		int time = 0;
		while (state != 3) {
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
						&& state != 3) {
					tool.keyPressOne(Tool.getkeyCode(DataSave.my.getBjhxButton()));
				}
			}
			if (state == 3) {
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

	private void EWCZ() {
		if (DSHandle.isEatGRPJ() || isBMFCK()) {
			// 退出抢拍
			tool.keyPressOne(KeyEvent.VK_ESCAPE);
			tool.delay(500);
			if (state == 3) {
				return;
			}
			if (!DataSave.MAIDDQ) {
				tool.keyPressOne(KeyEvent.VK_ESCAPE);
				tool.delay(500);
				if (state == 3) {
					return;
				}
				tool.keyPressOne(KeyEvent.VK_ESCAPE);
				tool.delay(3500);
				if (state == 3) {
					return;
				}
			}
			DSHandle.EatGRPJ();
			if (state == 3) {
				return;
			}
			if (!JGHandle.isRun())
				BMFCK();
			if (state == 3) {
				return;
			}
			tool.delay(2000);
			while (!DSHandle.AutoCheckIn()) {
				tool.delay(2000);
			}
			if (!DataSave.MAIDDQ) {
				npc_name = "交易所";
				openNpc();
			} else {

				if (!GameListenerThread.DJNI.isCorsurShow()) {
					tool.keyPressOne(KeyEvent.VK_CONTROL);
					tool.delay(1500);
				}
				tool.delay(200);
				if (state == 3) {
					return;
				}
				tool.mouseMovePressOne(DataSave.maidPoint1.x, DataSave.maidPoint1.y, InputEvent.BUTTON1_MASK);
				tool.delay(700);
				if (state == 3) {
					return;
				}
				do {
					tool.mouseMovePressOne(DataSave.maidPoint2.x, DataSave.maidPoint2.y, InputEvent.BUTTON1_MASK);
					tool.delay(1500);
					if (state == 3)
						return;
				} while (!isBenXY());
				// 413 854 1364 218
				if (state == 3)
					return;
				tool.mouseMovePressOne(benX - (1354 - 398), benY + (861 - 203), InputEvent.BUTTON1_MASK);
				tool.delay(500);
				if (state == 3)
					return;
			}
			if (state == 3) {
				return;
			}
			// tool.keyPressOne(KeyEvent.VK_ESCAPE);
			// waitSleep();
		}
	}

	private void buy(int x1, int x2, int y1, int y2) {
		tool.delay(DataSave.qp.getQP_BuyDelay());
		if (tool.区域找色(x1 - 100, y1, x2 - 100, y2, 0.15, 10, "CECECE") != null) {
			tool.mouseMovePressOne(x1 / 2 + x2 / 2 - 100, y1 / 2 + y2 / 2, InputEvent.BUTTON1_MASK);
			return;
		} else
			tool.mouseMovePressOne(x1 / 2 + x2 / 2, y1 / 2 + y2 / 2, InputEvent.BUTTON1_MASK);
		long time2 = System.currentTimeMillis();
		if (QpHandle.getModel() == 2) {
			mainPanel.setZEnd_time(time2);
			mainPanel.setEnd_time(time2 + 2 * 60 * 1000);
		}
		tool.delay(50);
		// 判断是不是复数物品的单扫
		Point p = null;
		Point p2 = null;
		if (state == 1) {
			state = 2;
			while (state == 2) {
				Other.sleep(50);
			}
		}
		int x = DataSave.SCREEN_WIDTH / 2 + DataSave.SCREEN_X;
		int y = DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y;
		// 893,623,927,641 
		while (QpHandle.isClose()) {
			if (state == 3) {
				return;
			}
			//if (goumai == null) {
				// System.out.println(960+" "+540); 880 617 930 634
				p2 = tool.区域找色(x - 80, y + 77, x - 30, y + 94, 0.2, 5, "E4E3E3");
				if (p2 != null) {
					break;
				}
//			} else {
//				p2 = tool.区域找图EX(x - 80, y + 77, x - 30, y + 94, 0.07, goumai);
//				if (p2 != null) {
//					break;
//				}
//			}
			// 861 438 939 451
			p = tool.区域找色(x - 99, y - 102, x - 21, y - 89, 0.2, 5, "E4E3E3");

			if (p != null) {
				break;
			}
			if (System.currentTimeMillis() - time2 > 1000) {
				break;
			}
		}
		if (p2 != null) {
//			if (goumai == null) {
//				// 940,427,974,451
//				goumai = tool.得到特征点集合图(x - 70, y + 65, x - 40, y + 80, 0.2, "E4E3E3");
//			}
			if (DataSave.qp.getQp_Liang() == 1) {

			} else {
				// 复数物品抢拍904 579
				tool.mouseMovePressOne(x - 56, y + 39, InputEvent.BUTTON1_MASK);
				int time = DataSave.qp.getQp_Writertime();
				if (time > 0) {
					tool.delay(time);
				}
				if (DataSave.qp.getQp_Liang() == 0) {
					tool.delay(50);
					tool.keyPressOne(KeyEvent.VK_F);
				} else {
					byte[] liang = (DataSave.qp.getQp_Liang() + "").getBytes();
					for (int k = 0; k < liang.length; k++) {
						tool.delay(100);
						tool.keyPressOne(Tool.getkeyCode(((char) liang[k]) + ""));
					}
				}
				tool.delay(50);
				tool.keyPressOne(KeyEvent.VK_ENTER);
				tool.delay(50);
			}
			//tool.keyPressOne(KeyEvent.VK_ENTER);
			tool.mouseMovePressOne(p2.x, p2.y, InputEvent.BUTTON1_MASK);
			tool.delay(75);
			tt = System.currentTimeMillis();
		} else if (p != null) {
			tool.keyPressOne(KeyEvent.VK_ENTER);
			tool.delay(75);
		}
		if (state == 1) {
			state = 2;
			while (state == 2) {
				Other.sleep(50);
			}
		}
		if (state == 3) {
			return;
		}

	}

	private boolean isAddQp_other() {
		qpOtherPanel otherPanel = QPtempData.isQp_other();

		if (linkedList.size() < 5 && otherPanel != null) {
			return true;
		}
		if (otherPanel == null)
			return false;
		while (otherPanel.getend_time() <= System.currentTimeMillis()) {
			QPtempData.remove(otherPanel);
			otherPanel = QPtempData.isQp_other();
			if (otherPanel == null)
				return false;
		}
		LinkedList<qpOtherPanel> temp = linkedList.get(otherPanel.getDimgFile().objectname);
		if (temp != null) {
			addList_other_one(temp, otherPanel);
			return false;
		}
		long time = System.currentTimeMillis();
		// System.out.println(time+" "+ mainPanel.getend_time() + " " +
		// mainPanel.getDimgFile().file.getName());
		if (Integer.parseInt(otherPanel.getDimgFile().objectPRI) < Integer.parseInt(main.objectPRI)) {
			temp = linkedList.remove(main.objectname);
			list_names.remove(main.objectname);
			deleteQp_other(temp);
			return true;
		}
		if (time >= mainPanel.getend_time()) {
			temp = linkedList.get(main.objectname);
			Iterator<qpOtherPanel> iterator = temp.iterator();
			boolean bool = true;
			while (iterator.hasNext()) {
				qpOtherPanel panel = iterator.next();
				if (panel.getend_time() < time) {
					bool = false;
				} else {
					iterator.remove();
				}
			}
			if (!bool)
				return false;
			// 剔除这个物品从临时列表中
			temp = linkedList.remove(main.objectname);
			list_names.remove(main.objectname);
			deleteQp_other(temp);
			return true;
		}
		return false;

	}

	// private boolean isQpObject(qpOtherPanel otherPanel) {
	// Iterator<qpOtherPanel> iterator = linkedList.iterator();
	// while (iterator.hasNext()) {
	// qpOtherPanel panel = iterator.next();
	// if
	// (panel.getDimgFile().file.getName().equals(otherPanel.getDimgFile().file.getName()))
	// {
	// return true;
	// }
	// }
	// return false;
	// }

	private void deleteQp_other(LinkedList<qpOtherPanel> temp) {// 1637,332 1367
		// souX=benX+298;
		// souY=benY+63; // 227
		tool.mouseMovePressOne(benX - 18, benY + 105 - 63 + num * 40, InputEvent.BUTTON1_MASK);
		tool.delay(400);
		tool.keyPressOne(KeyEvent.VK_ENTER);
		Iterator<qpOtherPanel> iterator = temp.iterator();
		while (iterator.hasNext()) {
			qpOtherPanel panel = iterator.next();
			panel.setZEnd_time(System.currentTimeMillis());
			panel.setStateText("已结束");
		}
	}

	private void writeObjectName() {
		tool.delay(DataSave.qp.getQp_Name());
		// 1144,280,1144,280
		tool.mouseMovePressOne(benX - 223, benY + 65, InputEvent.BUTTON1_MASK);
		Clipboard.setSysClipboardText(main.objectname);
		tool.delay(500);
		tool.keyPress(17);
		tool.delay(150);
		tool.keyPress(KeyEvent.VK_V);
		tool.delay(100);
		tool.keyRelease(KeyEvent.VK_V);
		tool.delay(50);
		tool.keyRelease(17);
		tool.delay(300);
		tool.keyPressOne(KeyEvent.VK_ENTER);
	}

	private boolean isQpObject() {
		// 判断是否是这个物品的前置时间
		long time = System.currentTimeMillis();
		while (tool.区域找图(benX - 737 + 5, benY + 45 + 5, benX - 737 + 32 + 5, benY + 45 + 5 + 32, 0.1, main.bufferedImage) == null
				|| (!main.objectcolor.equals("任意")
						&& (tool.区域找图EX(benX - 737 - 5, benY + 45 - 5, benX - 737 + 50, benY + 45 + 5, 0.15, main.objectcolor + "null.bmp") == null))) {
			if (System.currentTimeMillis() - time > DataSave.qp.getQp_Replace()) {
				return false;
			}
			tool.delay(50);
		}
		return true;
	}

	private void moveObjectLevel() {
		// 903 258 1354 203
		if (main.objectlevel.equals("任意")) {
			return;
		}
		tool.delay(DataSave.qp.getQp_LV());
		tool.mouseMovePressOne(benX - 451, benY + 55, InputEvent.BUTTON1_MASK);
		tool.delay(300);
		for (int i = 1; i < TitleInfoPanel.leveltype.length; i++) {
			// 838,298,842,315
			if (main.objectlevel.equals(TitleInfoPanel.leveltype[i])) {
				tool.mouseMovePressOne(benX - 451, benY + 97 + (i - 1) * 17, InputEvent.BUTTON1_MASK);
				return;
			}
		}
	}

	private void moveObjectType() {
		tool.mouseMovePressOne(benX - 871, benY + 55, InputEvent.BUTTON1_MASK);
		tool.delay(DataSave.qp.getQp_Type());
		if (main.objecttype.equals("不区分")) {
			return;
		}
		for (int i = 1; i < TitleInfoPanel.type.length; i++) {
			if (main.objecttype.equals(TitleInfoPanel.type[i])) {
				if (i <= 12) {
					tool.mouseMovePressOne(benX - 816, benY + 95 + (i - 1) * 41, InputEvent.BUTTON1_MASK);
					tool.delay(500);
					return;
				} else {
					tool.mouseMovePressOne(benX - 816, benY + 579, InputEvent.BUTTON1_MASK);
					tool.delay(500);
					tool.mouseMovePressOne(benX - 816, benY + 579 - (18 - i) * 41, InputEvent.BUTTON1_MASK);
					tool.delay(500);
					return;
				}
			}
		}
	}

	public void stopOrStart() {
		state = (state != 0 ? 0 : 1);
	}

	public boolean isWait() {
		return state == 2;
	}

	public void setStateText(String string) {
		mainPanel.setStateText(string);
		DataSave.qpList.repaint();
	}

	public void setZEnd_Time(long zend_time) {
		mainPanel.setZEnd_time(zend_time);
		DataSave.qpList.repaint();
	}
}
