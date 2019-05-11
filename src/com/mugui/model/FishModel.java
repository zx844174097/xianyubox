package com.mugui.model;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

import net.sourceforge.tess4j.Tesseract2;

import com.mugui.Dui.DimgFile;
import com.mugui.http.Bean.FishBean;
import com.mugui.tool.FileTool;
import com.mugui.tool.ImgTool;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

public class FishModel {
	private static FishPriceThread 得到鱼价 = null;
	private static long yu_time = System.currentTimeMillis();
	private static Tool shouTool = null;
	private static boolean isTrue = false;

	public static void reStart() {
		// if (isTrue) 
		// return;
		// isTrue = false;
		// if (得到鱼价 != null && 得到鱼价.isAlive())
		// return;
		// if (!DataSave.服务器.equals("韩服") && !DataSave.服务器.equals("台服")) {
		// return;
		// }
		// if (shouTool == null) {
		// shouTool = new Tool();
		// }
		// isTrue = true;
		// shouTool.mouseMovePressOne(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH
		// - 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
		// long time = System.currentTimeMillis();
		// while (System.currentTimeMillis() - time < 10000 &&
		// FishPriceFrame.thread.mode != 2) {
		// Other.sleep(200);
		// }
		// if (FishPriceFrame.thread.mode != 2) {
		// return;
		// }
		// yu_time = System.currentTimeMillis();
		// 得到鱼价 = new FishPriceThread();
		// 得到鱼价.start();
		//
	}

	public static boolean start() {
		// if (isTrue)
		// return false;
		// isTrue = false;
		// if (得到鱼价 != null && 得到鱼价.isAlive())
		// return false;
		// if (!DataSave.服务器.equals("韩服") && !DataSave.服务器.equals("台服")) {
		// return false;
		// }
		// if (shouTool == null) {
		// shouTool = new Tool();
		// }
		// // 判断字体和地图大小
		// if (!isRun()) {
		// return false;
		// }
		// yu_time = System.currentTimeMillis();
		// System.out.println("运行");
		// isTrue = true;
//		 得到鱼价 = new FishPriceThread();
//		 得到鱼价.start();
		return true;
	}

	private static boolean isRun() {
		FileInputStream fileInputStream = null;
		try {
			if (!Tesseract2.isInit())
				return false;
			if (DataSave.D_LINE_ID < 0)
				return false;
			if (System.currentTimeMillis() - yu_time < 40 * 60 * 1000) {
				return false;
			}
			yu_time = System.currentTimeMillis();
			System.out.println("检测是否可以看鱼价");
			BufferedImage image = DSHandle.getLineTeImg();
			if (image == null)
				return false;
			int a;
			if ((a = FishPrice.getYuAllBodyOneKey(image)) != DataSave.D_LINE_ID) {
				DataSave.D_LINE_ID = a;
				return false;
			}
			if (DataSave.D_LINE_ID < 0)
				return false;

			System.out.println("检测是否可以看鱼价2");
			File f = new File(FileTool.getWindowsPath().getPath() + "/Black Desert/GameOption.txt");
			if (!f.exists()) {
				return false;
			}
			Properties properties = new Properties();
			fileInputStream = new FileInputStream(f);
			properties.load(fileInputStream);
			String fov = properties.getProperty("fov");
			String UIFontSizeType = properties.getProperty("UIFontSizeType");
			if (!fov.trim().equals("70.00") || !UIFontSizeType.trim().equals("0")) {
				System.out.println("游戏配置不正确，无法开始");
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
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

	public static boolean isClose() {
		return isTrue;
	}

	public static void stop() {
		isTrue = false;
	}

	private static BufferedImage te = null;

	private static class FishPriceThread extends Thread {

		private boolean dsBool = false;

		@Override
		public void run() {
			if (DSHandle.isRun()) {
				DSHandle.stop();
				dsBool = true;
			}
			try {
				main();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			if (dsBool) {
				DSHandle.start(null);
			}
			FishModel.stop();
		}

		private void main() {
			if (DataSave.D_LINE_ID < 0)
				return;
			te = FishPrice.allbody.get(DataSave.D_LINE_ID).xianluBody.te;
			if (te == null)
				return;
			// ,92,70 275,211
			// 1728,4,1906,24
			shouTool.delay(500);
			shouTool.mousePressOne(InputEvent.BUTTON1_MASK);
			shouTool.delay(200);
			shouTool.mousePressOne(InputEvent.BUTTON1_MASK);
			if (!isTrue)
				return;
			shouTool.delay(2000);
			shouTool.keyPressOne(KeyEvent.VK_M);
			boolean sdf = false;
			long time = System.currentTimeMillis();
			while (System.currentTimeMillis() - time < 6000 && isTrue) {
				shouTool.delay(1500);
				if (shouTool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 200, DataSave.SCREEN_Y + 2, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 4,
						DataSave.SCREEN_Y + 28, 0.07, te) == null) {
					sdf = true;
					break;
				}
				shouTool.keyPressOne(KeyEvent.VK_M);
				System.out.println("打开地图");
			}
			if (!sdf) {
				return;
			}
			shouTool.mouseMovePressOne(DataSave.SCREEN_X + 92, DataSave.SCREEN_Y + 70, InputEvent.BUTTON1_MASK);
			shouTool.delay(200);
			Point p = null;
			// 237,227,307,244 EFEFEF
			if (shouTool.区域找色(300 + DataSave.SCREEN_X, 227 + DataSave.SCREEN_Y, 315 + DataSave.SCREEN_X, 244 + DataSave.SCREEN_Y, 0.15, 5, "EFEFEF") != null) {
				p = new Point(275 + DataSave.SCREEN_X, 235 + DataSave.SCREEN_Y);
			} else if (shouTool.区域找色(300 + DataSave.SCREEN_X, 204 + DataSave.SCREEN_Y, 315 + DataSave.SCREEN_X, 220 + DataSave.SCREEN_Y, 0.15, 5,
					"EFEFEF") != null) {
				p = new Point(275 + DataSave.SCREEN_X, 211 + DataSave.SCREEN_Y);
			}
			if (p == null) {
				long longtime = System.currentTimeMillis();
				shouTool.keyPressOne(KeyEvent.VK_M);
				while (isTrue
						&& shouTool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 200, DataSave.SCREEN_Y + 2,
								DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 4, DataSave.SCREEN_Y + 28, 0.07, te) == null
						&& System.currentTimeMillis() - longtime < 5 * 1000) {
					shouTool.delay(1000);
					shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
				}
				shouTool.delay(500);
				return;
			}
			// 233,204,306,220
			shouTool.mouseMovePressOne(p.x, p.y, InputEvent.BUTTON1_MASK);
			shouTool.delay(500);
			shouTool.mouseMovePressOne(p.x, p.y, InputEvent.BUTTON1_MASK);
			shouTool.delay(500);
			shouTool.mouseMovePressOne(p.x, p.y, InputEvent.BUTTON1_MASK);
			shouTool.delay(500);
			// 1651,112,1668,133

			if (!isTrue)
				return;
			if (shouTool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 290, DataSave.SCREEN_Y + 110, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 230,
					DataSave.SCREEN_Y + 140, 0.15, "M知识.bmp") == null) {
				shouTool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 268, DataSave.SCREEN_Y + 120, InputEvent.BUTTON1_MASK);
				shouTool.delay(300);
			}
			// 1729,108,1760,134
			if (shouTool.区域找图(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 191, DataSave.SCREEN_Y + 110, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 160,
					DataSave.SCREEN_Y + 140, 0.15, "M方向信息.bmp") == null) {
				shouTool.mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 175, DataSave.SCREEN_Y + 120, InputEvent.BUTTON1_MASK);
				shouTool.delay(300);
			}
			if (!isTrue)
				return;
			// 600,623 1039,287 960 540
			// 查看左边贸易点 400,411 640 360 960 540 2.271031614 1.5 2.25 520 599 200
			// 80
			// 719,588
			if (!isTrue)
				return;
			double temp_w = DataSave.SCREEN_HEIGHT / 0.5625;
			shouTool.mouseMovePressOne((int) (719.0 * temp_w / 1920.0 - (temp_w - DataSave.SCREEN_WIDTH) / 2 + DataSave.SCREEN_X),
					(int) (588.0 * DataSave.SCREEN_HEIGHT / 1080.0 + DataSave.SCREEN_Y), InputEvent.BUTTON1_MASK);
			shouTool.delay(200);
			shouTool.keyPressOne(KeyEvent.VK_ENTER);
			if (!isTrue)
				return;
			// 看鱼价
			if (!看鱼价()) {
				long longtime = System.currentTimeMillis();
				shouTool.keyPressOne(KeyEvent.VK_M);
				while (isTrue
						&& shouTool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 200, DataSave.SCREEN_Y + 2,
								DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 4, DataSave.SCREEN_Y + 28, 0.07, te) == null
						&& System.currentTimeMillis() - longtime < 5 * 1000) {
					shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
					shouTool.delay(1000);
				}
				shouTool.delay(500);
				return;
			}
			if (!isTrue)
				return;
			temp_w = DataSave.SCREEN_HEIGHT / 0.5625; // 1897 26 1000
			// 查看右边边贸易点1012,368, 752 737
			shouTool.mouseMovePressOne((int) (1012.0 * temp_w / 1920.0 - (temp_w - DataSave.SCREEN_WIDTH) / 2 + DataSave.SCREEN_X),
					(int) (368.0 * DataSave.SCREEN_HEIGHT / 1080.0 + DataSave.SCREEN_Y), InputEvent.BUTTON1_MASK);
			shouTool.delay(200);
			shouTool.keyPressOne(KeyEvent.VK_ENTER);
			if (!isTrue)
				return;
			linkedList.add(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
			// 看鱼价
			if (!看鱼价()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						UIModel.sendFishPrice(linkedList);
					}
				}).start();
				if (!isTrue)
					return;
				long longtime = System.currentTimeMillis();
				shouTool.keyPressOne(KeyEvent.VK_M);
				while (isTrue
						&& shouTool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 200, DataSave.SCREEN_Y + 2,
								DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 4, DataSave.SCREEN_Y + 28, 0.07, te) == null
						&& System.currentTimeMillis() - longtime < 5 * 1000) {
					shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
					shouTool.delay(1000);
				}
				shouTool.delay(500);
				return;
			}
			if (!isTrue)
				return;
			new Thread(new Runnable() {
				@Override
				public void run() {
					//UIModel.sendFishPrice(linkedList);
					UIModel.saveFishPrie(linkedList);
				}
			}).start();
			
			if (!isTrue)
				return;
			long longtime = System.currentTimeMillis();
			shouTool.keyPressOne(KeyEvent.VK_M);
			while (isTrue && shouTool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 200, DataSave.SCREEN_Y + 2,
					DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 4, DataSave.SCREEN_Y + 28, 0.07, te) == null
					&& System.currentTimeMillis() - longtime < 5 * 1000) {
				shouTool.keyPressOne(KeyEvent.VK_ESCAPE);
				shouTool.delay(1000);
			}
			shouTool.delay(500);
		}

		LinkedList<BufferedImage> linkedList = new LinkedList<BufferedImage>();

		private boolean 看鱼价() {
			long time = System.currentTimeMillis();
			Point p = null;
			// 1891,45,1917,81 X
			while (System.currentTimeMillis() - time < 5000) {
				if ((p = shouTool.区域找图EX(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 29, DataSave.SCREEN_Y + 45, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 3,
						DataSave.SCREEN_Y + 81, 0.10, "XX.bmp")) != null) {
					break;
				}
			}
			if (p == null) {
				return false;
			}
			shouTool.delay(200);
			// 1754,102,1899,60 1547,388
			shouTool.mouseMovePressOne(p.x - 150, p.y + 40, InputEvent.BUTTON1_MASK);
			shouTool.delay(200);
			shouTool.mouseMovePressOne(p.x - 380, p.y + 328, InputEvent.BUTTON1_MASK);
			shouTool.delay(200);
			// 1910,158
			Point p1 = new Point(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 10, DataSave.SCREEN_Y + 158);
			// 1910,878 1080
			Point p2 = new Point(p1.x, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y - 200);
			// int h = p2.y - p1.y;
			shouTool.mouseMove(p1.x, p1.y);
			shouTool.delay(200);
			shouTool.mousePress(InputEvent.BUTTON1_MASK);
			shouTool.delay(100);
			// 1645,133,1693,136
			double leng = (p2.y - p1.y) / 300.0;
			leng = (leng <= 0 ? 1 : leng);
			while (p1.y < p2.y) {
				// 1650 134 1899,60
				BufferedImage linimg = shouTool.截取屏幕(p.x - 249 - 2, p.y + 75, p.x - 249 - 2 + 150, p.y + 75 + 46);
				if (shouTool.图中找图(linimg, "黄.bmp", 0.15, 0, 0) != null) {
					linkedList.add(linimg);
				}
				p1.y = (int) (p1.y * 1.0 + leng);
				shouTool.mouseMove(p1.x, p1.y);
			}
			shouTool.mouseRelease(InputEvent.BUTTON1_MASK);
			shouTool.mouseMovePressOne(p.x + 2, p.y + 2, InputEvent.BUTTON1_MASK);
			shouTool.delay(500);
			return true;
		}
	}

	/**
	 * 保存一个收藏的鱼图片
	 * 
	 * @param bean
	 */
	public static void saveFishBean(FishBean bean) {
		File f = new File(DataSave.JARFILEPATH + "\\自定义鱼收藏\\");
		if (!f.isDirectory()) {
			f.mkdirs();
		}

		f = new File(f.getPath() + "\\" + Other.getShortUuid() + ".bmp");
		DimgFile file = new DimgFile();
		file.bufferedImage = bean.getFish_img();
		file.file = f;
		file.saveAllData();
		FishBean bean2 = new FishBean();
		bean2.setFish_name(file.file.getName().split("\\.")[0]);
		bean2.setFish_img(file.bufferedImage);
		BufferedImage img = file.bufferedImage;
		img = ImgTool.cutImage(img, 5, 5, img.getWidth() - 10, img.getHeight() - 10);
		bean2.setFish_name_img(img);
		beans.add(bean2);
	}

	private static final LinkedList<FishBean> beans = new LinkedList<FishBean>();
	static {
		File f = new File(DataSave.JARFILEPATH + "\\自定义鱼收藏\\");
		if (f.isDirectory()) {
			String list[] = f.list();
			for (int i = 0; i < list.length; i++) {
				File temp = new File(f.getPath() + "\\" + list[i]);
				if (temp.isFile()) {
					DimgFile dimgFile =  DimgFile.getImgFile(temp);
					if (dimgFile != null && dimgFile.bufferedImage != null) {
						FishBean bean = new FishBean();
						bean.setFish_img(dimgFile.bufferedImage);
						bean.setFish_name(dimgFile.objectname);
						BufferedImage img = dimgFile.bufferedImage;
						img = ImgTool.cutImage(img, 5, 5, img.getWidth() - 10, img.getHeight() - 10);
						bean.setFish_name_img(img);
						beans.add(bean);
					}
				}
			}
		}
	}

	/**
	 * 得到所有的收藏夹文件
	 * 
	 * @return
	 */
	public static LinkedList<FishBean> getSaveFishBeans() {
		return beans;
	}

	/**
	 * 删除一个收藏的项目
	 * 
	 * @param bean
	 */
	public static void delFishBean(FishBean bean) {
		File f = new File(DataSave.JARFILEPATH + "\\自定义鱼收藏\\");
		if (!f.isDirectory()) {
			f.mkdirs();
		}
		f = new File(f.getPath() + "\\" + bean.getFish_name() + ".bmp");
		f.delete();
		beans.remove(bean);
	}

}
