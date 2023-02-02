package com.mugui.windows;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.peer.RobotPeer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.mugui.Dui.DimgFile;
import com.mugui.jni.Tool.DJni;
import com.mugui.model.DSHandle;
import com.mugui.model.UIModel;
import com.mugui.tool.ImgTool;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.df.ScreenDevicesSetPanel;
import com.mugui.ui.part.GameListenerThread;
import com.mugui.ui.part.CJ.HS_MAP;

import sun.awt.windows.WToolkit;

@SuppressWarnings("restriction")
public class Tool {
	private DRobot robot;
	private RobotPeer peer = null;
	public static Dimension win_size = Toolkit.getDefaultToolkit().getScreenSize();

	public static void reSetDevice() {
		GraphicsEnvironment graphicsEnvironmen = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] de = graphicsEnvironmen.getScreenDevices();
		GraphicsDevice device = null;
		if (de.length == 1) {
			device = de[0];
		} else {
			DataSave.StaticUI.setVisible(true);
			ScreenDevicesSetPanel panel = new ScreenDevicesSetPanel();
			JPanel panel2 = DataSave.StaticUI.getUI();
			UIModel.setUI(panel);
			device = panel.getSelectScreenDevice();
			UIModel.setUI(panel2);
		}
		Tool.device = device;
		Tool.configuration = device.getDefaultConfiguration().getBounds();
	}

	private static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static Rectangle configuration = device.getDefaultConfiguration().getBounds();

	public Tool() {
		try {
			robot = new DRobot(device);
			peer = new WToolkit().createRobot(robot, device);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private DJni jni = GameListenerThread.DJNI;

	public void lockDirection(float jiaodu) {
		if (jni == null)
			jni = GameListenerThread.DJNI;
		if (jni == null)
			jni = new DJni();
		DataSave.hs_map = jni.getHsMap();
		// DSHandle.AutoCheckIn();
		long time = System.currentTimeMillis();
		long time2 = time;
		HS_MAP hs_map = jni.getHsMap();
		DataSave.hs_map = hs_map;
		while (System.currentTimeMillis() - time2 < 5000) {
			if (System.currentTimeMillis() - time > 1500) {
				DSHandle.AutoCheckIn();
				time = System.currentTimeMillis();
			}
			// DataSave.cj.setCJBound((float) (jiaodu), hs_map.lens_dx, jiaodu,
			// 0);
			PointerInfo mouse_info = MouseInfo.getPointerInfo();

			if (Math.abs(hs_map.lens_dx - jiaodu) > 0.09f) {
				if (hs_map.lens_dx > jiaodu) {
					float len_1 = hs_map.lens_dx - jiaodu;
					float len_2 = 3.14f - hs_map.lens_dx - (-3.14f - jiaodu);
					if (len_1 > len_2) {
						mouseMove(mouse_info.getLocation().x + 1, mouse_info.getLocation().y);
					} else {
						mouseMove(mouse_info.getLocation().x - 1, mouse_info.getLocation().y);
					}
				} else {
					float len_1 = jiaodu - hs_map.lens_dx;
					float len_2 = 3.14f - jiaodu - (-3.14f - hs_map.lens_dx);
					if (len_1 > len_2) {
						mouseMove(mouse_info.getLocation().x - 1, mouse_info.getLocation().y);
					} else {
						mouseMove(mouse_info.getLocation().x + 1, mouse_info.getLocation().y);
					}
				}
			} else {
				break;
			}
			hs_map = jni.getHsMap();
		}

	}

	public int[] getScreenBufferInt(int x, int y, int x2, int y2) {
		// Toolkit.getDefaultToolkit().sync();
		Rectangle rectangle = new Rectangle(configuration.x + x, configuration.y + y, x2, y2);
		// if (WinJNI.DJNI == null) {
		// WinJNI.DJNI = new DJni();
		// }
		// if (!DJni.isTrue) {
		// return 截取屏幕(x, y, x2, y2).getRGB(x, y, x2 - x, y2 - y, null, 0, x2);
		// } else
		return peer.getRGBPixels(rectangle);
	}

	long time = 0;
	private BufferedImage bufferedImage1 = null;

	public BufferedImage 截取屏幕(int x, int y, int x2, int y2) {
		// if (RDPMAIN.isRun()) {
		// return RDPMAIN.getDesktopImg().getSubimage(x, y, x2 - x + 1, y2 - y +
		// 1);
		// }
		if (GameListenerThread.DJNI == null) {
			GameListenerThread.DJNI = new DJni();
		}
		if (!DJni.isTrue && DataSave.图像截图) {
			bufferedImage1 = GameListenerThread.DJNI.createScreenCapture(configuration.x + x, configuration.x + y,
					x2 - x, y2 - y);
		} else
			bufferedImage1 = robot
					.createScreenCapture(new Rectangle(configuration.x + x, configuration.y + y, x2 - x, y2 - y));
		if (bufferedImage1 == null) {
			return bufferedImage1 = robot
					.createScreenCapture(new Rectangle(configuration.x + x, configuration.y + y, x2 - x, y2 - y));

		}
		return bufferedImage1;

	}

	public static int getkeyCode(String code) {
		try {
			if (code.startsWith("VK_")) {
				Field f = KeyEvent.class.getField(code);
				return f.getInt(null);
			}
			Field f = KeyEvent.class.getField("VK_" + code);
			return f.getInt(null);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Point 图中找色(BufferedImage image, double d, int k, String color, int x, int y) {
		boolean bool = true;
		if (color.charAt(0) == '!') {
			bool = false;
			color = color.substring(1);
		}
		String s_c[] = color.split("\\|");
		Color[] colors = new Color[s_c.length];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = StringColor(s_c[i]);
		}
		Point point = new Point();
		point.x = -1;
		point.y = -1;
		int w = image.getWidth();
		int h = image.getHeight();
		int max = 0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color cc = new Color(image.getRGB(i, j));
				boolean b = !bool;
				for (int m = 0; m < colors.length; m++) {
					double ddd = 颜色比较(cc, colors[m]);
					if (bool) {
						if (ddd <= d) {
							b = true;
						}
					} else {
						if (ddd <= d) {
							b = false;
						}
					}
				}
				if (b) {
					// System.out.println( " " + cc + " " +
					// Arrays.toString(colors));
					point.x = i + x;
					point.y = j + y;
					max++;
					if (max >= k) {
						return point;
					}
				}

			}
		}
		return null;
	}

	public Point 区域找色(int x, int y, int x2, int y2, double d, int k, String color) {
		BufferedImage image = 截取屏幕(x, y, x2, y2);

		return 图中找色(image, d, k, color, x, y);
	}

	// 得到由字符串创造的颜色
	public Color StringColor(String color) {
		int b = Integer.parseInt(color.substring(0, 2), 16);
		int g = Integer.parseInt(color.substring(2, 4), 16);
		int r = Integer.parseInt(color.substring(4, 6), 16);
		return new Color(r, g, b);
	}

	public double 颜色比较(Color color1, Color color2) {
		// double h = color1.getRed()/255.0- color2.getRed()/255.0;
		// double s = color1.getGreen()/255.0- color2.getGreen()/255.0;
		// double v = color1.getBlue()/255.0 - color2.getBlue()/255.0;
		// h =Math.sqrt(h*h+s*s+v*v);
		// return h;
		return 得到颜色相似度(RGB转HLS(color1), RGB转HLS(color2));
	}

	public double 颜色比较(int rgb1, int rgb2) {
		int r1 = (rgb1 >> 16) & 0xFF;
		int g1 = (rgb1 >> 8) & 0xFF;
		int b1 = (rgb1 >> 0) & 0xFF;
		int r2 = (rgb2 >> 16) & 0xFF;
		int g2 = (rgb2 >> 8) & 0xFF;
		int b2 = (rgb2 >> 0) & 0xFF;
		return 255 - Math.abs(r1 - r2) * 0.297 - Math.abs(g1 - g2) * 0.593 - Math.abs(b1 - b2) * 0.11;
	}

	public double 得到颜色相似度(hls hls1, hls hls2) {
		double h = hls1.h - hls2.h;
		double s = hls1.s - hls2.s;
		double v = hls1.v - hls2.v;
		h = Math.sqrt(h * h + s * s + v * v);
		return Math.abs(h);
	}

	private hls RGB转HLS(Color color1) {
		hls h = new hls();
		double r = color1.getRed() / 255.0;
		double g = color1.getGreen() / 255.0;
		double b = color1.getBlue() / 255.0;
		h.h = r;
		h.s = g;
		h.v = b;
		return h;
	}

	private class hls {
		double h;
		double s;
		double v;
	}

	public void mouseWheel(int wmt) {
		robot.mouseWheel(wmt);
	}

	public void mousePress(int button1DownMask) {
		MouseInfo.getNumberOfButtons();

		robot.mousePress(button1DownMask);
	}

	public void mouseMove(int i, int j) {
		robot.mouseMove(configuration.x + i, configuration.y + j);
	}

	public void mouseMovePressOne(int i, int j, int button) {
		mouseMove(i, j);
		delay(200);
		mousePressOne(button);
	}
	private boolean isDelay=false;
	public void delay(int i) {
		isDelay=true;
		long time=System.currentTimeMillis();
		while(isDelay&&System.currentTimeMillis()-time<i){
			robot.delay(1);
		}
	}
	public void delayStop(){
		isDelay=false;
	}

	public void mousePressOne(int button1) {
		int i = Integer.parseInt(DataSave.deBug.鼠标按下与释放时间.getText());
		mousePressOne(button1, i);
	}

	public void mouseRelease(int button1) {
		robot.mouseRelease(button1);
	}

	public void mousePressOne(int button1, int time) {
		mousePress(button1);
		delay(time);
		mouseRelease(button1);
	}

	public void keyPressOne(int vkSpace) {
		int i = Integer.parseInt(DataSave.deBug.键盘按下与释放间隔.getText());
		keyPressOne(vkSpace, i);
	}

	public void keyPressOne(int vkSpace, int time) {
		keyPress(vkSpace);
		delay(time);
		keyRelease(vkSpace);
	}

	public void keyRelease(int vkSpace) {
		robot.keyRelease(vkSpace);
	}

	public void keyPress(int vk) {
		robot.keyPress(vk);
	}

	// int lss = 0;
	// private hls tuqu[][] = null;
	// private hls tuyu[][] = null;
	// private int tuquW;
	// private int tuquH;
	// private int tuyuW;
	// private int tuyuH;

	public Point 区域找图(int x, int y, int x2, int y2, double d, BufferedImage img) {
		if (x2 - x <= 0)
			return null;
		return 图中找图(截取屏幕(x, y, x2, y2), img, d, x, y);
	}

	public Point 图中找图(BufferedImage image, String img_name, double d, int x, int y) {
		try {
			return 图中找图(image, getDImg(img_name).bufferedImage, d, x, y);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Point 图中找图EX(BufferedImage image, String img_name, double d, int x, int y) {
		try {
			return 图中找图EX(image, getDImg(img_name).bufferedImage, d, x, y);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String IMGPATH = DataSave.JARFILEPATH;

	public static void setRootPath(String imgpath) {
		IMGPATH = imgpath;
	}

	public static String getRootPath() {
		return IMGPATH;
	}

	public DimgFile getDImg(String img_name) throws IOException {
		DimgFile dimgFile = null;
		if ((dimgFile = hashMap.get(img_name)) == null) {

			dimgFile = DimgFile.getImgFile(IMGPATH + "\\data\\" + img_name);
			if (dimgFile == null || dimgFile.bufferedImage == null) {
				throw new IOException("空的图片->" + IMGPATH + "\\data\\" + img_name);
			}

			hashMap.put(img_name, dimgFile);
		}
		return dimgFile;
	}

	// 最后的坐标是坐标偏转
	public Point 图中找图(BufferedImage image, BufferedImage img, double d, int x, int y) {
		// this.image = image;
		// tuquW = image.getWidth();
		// tuquH = image.getHeight();
		// tuyuW = img.getWidth();
		// tuyuH = img.getHeight();
		hls[][] tuqu = 得到图片数组(image);
		hls[][] tuyu = 得到图片数组(img);
		Point p = 从A图查找是否有B图(d, tuqu, tuyu);
		if (p != null) {
			p.x += x;
			p.y += y;
		}
		return p;
	}

	// 最后的坐标是坐标偏转
	public Point 图中找图EX(BufferedImage image, BufferedImage img, double d, int x, int y) {
		// this.image = image;
		// tuquW = image.getWidth();
		// tuquH = image.getHeight();
		// tuyuW = img.getWidth();
		// tuyuH = img.getHeight();
		hls[][] tuqu = 得到图片数组(image);
		hls[][] tuyu = 得到图片数组EX(img);
		Point p = 从A图查找是否有B图(d, tuqu, tuyu);
		if (p != null) {
			p.x += x;
			p.y += y;
		}
		return p;
	}

	public Point 区域找图EX(int x, int y, int x2, int y2, double d, BufferedImage img) {
		if (x2 - x <= 0)
			return null;
		return 图中找图EX(截取屏幕(x, y, x2, y2), img, d, x, y);
	}

	private HashMap<String, DimgFile> hashMap = new HashMap<String, DimgFile>();

	public Point 区域找图(int x, int y, int x2, int y2, double d, String string2) {
		try {
			return 区域找图(x, y, x2, y2, d, getDImg(string2).bufferedImage);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Point 区域找图EX(int x, int y, int x2, int y2, double d, String string2) {
		try {
			return 区域找图EX(x, y, x2, y2, d, getDImg(string2).bufferedImage);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Point 从A图查找是否有B图(double d, hls[][] tuqu, hls[][] tuyu) {
		// System.out.println(tuqu.length + " " + tuyu.length + " " +
		// tuqu[0].length + " " + tuyu[0].length);
		for (int i = 0; i < tuqu.length - tuyu.length + 1; i++) {
			for (int j = 0; j < tuqu[0].length - tuyu[0].length + 1; j++) {
				double dd = 判断两个图的某区域相似性(i, j, d, tuqu, tuyu);
				if (dd <= d) {
					Point p = new Point(i, j);
					return p;
				}
			}
		}

		return null;
	}

	private double 判断两个图的某区域相似性(int i, int j, double d, hls[][] tuqu, hls[][] tuyu) {
		int f = 0;
		double zo = 0;
		int w = 1;
		int h = 1;
		// if (tuyuW > 128)
		// w = tuyuW / 4;
		// if (tuyuH > 128)
		// h = tuyuH / 4;
		for (int n = 0; n < tuyu.length && i + n < tuqu.length; n += w) {
			for (int m = 0; m < tuyu[n].length && j + m < tuqu[i + n].length; m += h) {
				if (tuyu[n][m] == null || tuqu[i + n][j + m] == null)
					continue;
				// if (tuqu[i + n][j + m] == black)
				// return 99;
				double z = 得到颜色相似度(tuqu[i + n][j + m], tuyu[n][m]);
				if (z > 0.5)
					return 99;
				if (f > 20 && zo / f >= d * 2)
					return 99;
				zo += z;
				f++;
			}
		}
		if (f == 0)
			return 99;
		return zo / f;
	}

	hls black = RGB转HLS(new Color(255, 255, 255));

	public hls[][] 得到图片数组(BufferedImage image) {
		hls[][] hl = new hls[image.getWidth()][image.getHeight()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				hl[i][j] = RGB转HLS(new Color(image.getRGB(i, j)));
			}
		}
		return hl;
	}

	public hls[][] 得到图片数组EX(BufferedImage image) {
		hls[][] hl = new hls[image.getWidth()][image.getHeight()];
		Color color = null;
		if (image.getRGB(0, 0) == image.getRGB(image.getWidth() - 1, 0)
				&& image.getRGB(0, 0) == image.getRGB(0, image.getHeight() - 1)
				&& image.getRGB(0, 0) == image.getRGB(image.getWidth() - 1, image.getHeight() - 1)) {
			color = new Color(image.getRGB(0, 0));
		}
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				Color color2 = new Color(image.getRGB(i, j));
				if (color != null && 颜色比较(color, color2) == 0) {
					hl[i][j] = null;
					continue;
				}
				hl[i][j] = RGB转HLS(new Color(image.getRGB(i, j)));
			}
		}
		return hl;
	}

	public void 保存截屏(int x, int y, int x2, int y2, String string) {
		保存图片(截取屏幕(x, y, x2, y2), string);
	}

	public void 保存图片(BufferedImage bufferedImage, String string) {
		try {
			ImageIO.write(bufferedImage, "bmp", new File(DataSave.JARFILEPATH + "\\temp\\" + string));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String dy_code = "";

	public boolean paint(int x1, int y1, int x2, int y2) {
		BufferedImage image = 截取屏幕(x1, y1, x2, y2);
		DataSave.dy.init(image);
		保存图片(image, System.currentTimeMillis() + "_识别区.bmp");
		if (chuli(image).trim().equals("")) {
			return false;
		}
		return true;
	}

	// private int height = 0;
	// private int width = 0;
	// private int tu[][] = null;
	// private int tu2[][] = null;
	private int zuox;
	private int zuoy;
	private int youx;
	private int youy;
	// private BufferedImage image = null;

//	private int bianli(int rgb, int yuanx, int x, int y, int j, BufferedImage image, int[][] tu) {
//		tu[x][y] = 1;
//		if (Math.abs(yuanx - x) > 6)
//			return 0;
//		if (x - 1 >= 0 && image.getRGB(x - 1, y) == rgb && tu[x - 1][y] == 0) {
//			j = bianli(rgb, yuanx, x - 1, y, j + 1, image, tu);
//		}
//		if (x + 1 < 150 && image.getRGB(x + 1, y) == rgb && tu[x + 1][y] == 0) {
//			j = bianli(rgb, yuanx, x + 1, y, j + 1, image, tu);
//		}
//		if (y - 1 >= 0 && image.getRGB(x, y - 1) == rgb && tu[x][y - 1] == 0) {
//			j = bianli(rgb, yuanx, x, y - 1, j + 1, image, tu);
//		}
//		if (y + 1 < image.getHeight() && image.getRGB(x, y + 1) == rgb && tu[x][y + 1] == 0) {
//			j = bianli(rgb, yuanx, x, y + 1, j + 1, image, tu);
//		}
//		return j;
//	}

//	private int chuli1(BufferedImage image, int[][] tu) {
//		Color c = StringColor("5B5B5B");
//		int max = 0;
//		int g = 0;
//		for (int i = 0; i < 50; i++) {
//			Color c2 = new Color(image.getRGB(i, 4));
//			if (颜色比较(c, c2) > 0.07) {
//				int l = bianli(c2.getRGB(), i, i, 4, 0, image, tu);
//				if (l > max) {
//					max = l;
//					g = c2.getRGB();
//				}
//			}
//		}
//		return g;
//	}

	private int chuli2(BufferedImage image) {
		if (DataSave.服务器.equals("韩服") || DataSave.服务器.equals("台服") || DataSave.服务器.equals("steam服")) {

			image = ImgTool.cutImage(image, 0, 0, 200, image.getHeight());
			int color = image.getRGB(0, 5);
			for (int i = 1; i < image.getWidth() && i < 200; i++) {
				if (颜色比较(new Color(color), new Color(image.getRGB(i, 5))) > 0.07) {
					color = image.getRGB(i, 5);
					if (color == -1) {
						continue;
					}
					int num = searchColorNum(color, image, i, 5);
					System.out.println("num=" + num + " ");
					if (num >= 20 && num <= 40) {
						return color;
					}
				}
			}

		} else {
			Color c = StringColor("5B5B5B");
			Color c2 = new Color(image.getRGB(19, 4));
			if (颜色比较(c, c2) > 0.07 && 颜色比较(c2, new Color(image.getRGB(19 + 35, 4))) <= 0.07) {
				return c2.getRGB();
			}
		}
		return 0;
	}

	private int searchColorNum(int color, BufferedImage image, int i, int j) {
		if (image.getRGB(i, j) == -1)
			return 0;
		image.setRGB(i, j, -1);
		if (i <= 0 || j <= 0 || i >= image.getWidth() - 1 || j >= image.getHeight() - 1) {
			return 0;
		}
		int num = 0;
		if (颜色比较(new Color(color), new Color(image.getRGB(i - 1, j))) <= 0.07) {
			num++;
			num += searchColorNum(color, image, i - 1, j);
		}
		if (颜色比较(new Color(color), new Color(image.getRGB(i + 1, j))) <= 0.07) {
			num++;
			num += searchColorNum(color, image, i + 1, j);
		}
		if (颜色比较(new Color(color), new Color(image.getRGB(i, j - 1))) <= 0.07) {
			num++;
			num += searchColorNum(color, image, i, j - 1);
		}
		if (颜色比较(new Color(color), new Color(image.getRGB(i, j + 1))) <= 0.07) {
			num++;
			num += searchColorNum(color, image, i, j + 1); 
		}
		return num;
	}

	public String chuli(BufferedImage image) {
		// image = image2;
		String key = "";
		int time = Integer.parseInt(DataSave.deBug.验证码输入间隔时间.getText().trim());
		int maxbody = 0;
		int height = image.getHeight();
		int width = image.getWidth();
		int[][] tu = new int[width][height];
		int[][] tu2 = new int[width][height];
		// 韩
		maxbody = chuli2(image);
		// maxbody = chuli1(image, tu);
		System.out.println("锁定的颜色" + maxbody);
		if (maxbody == 0)
			return "";
		tu = new int[width][height];
		for (int j = 0; j < width; j++)
			for (int i = 0; i < height; i++) {
				// System.out.println(image.getHeight() + " " +
				// image.getWidth());

				if (颜色比较(new Color(image.getRGB(j, i)), new Color(maxbody)) <= 0.2) {
					tu[j][i] = 1;
					tu2[j][i] = 1;
					image.setRGB(j, i, Color.white.getRGB());
				} else {
					tu[j][i] = 0;
					tu2[j][i] = 0;
					image.setRGB(j, i, Color.black.getRGB());
				}
			}
		保存图片(image, System.currentTimeMillis() + "_识别区2.bmp");
		for (int j = 0; j < width; j++) {
			for (int i = 0; i < height; i++) {
				if (tu[j][i] == 1) {
					zuox = 9999;
					zuoy = 9999;
					youx = -1;
					youy = -1;
					int size = getBImages(j, i, 0, tu);
					if (size < 25 || size > 45) {
						break;
					}

					// System.out.println(size + " " + i + " " + j);
					// System.out.println(zuox + " " + zuoy + " " + youx + " " +
					// youy);
					if ((tu2[zuox][zuoy] == 1 || tu2[zuox + 1][zuoy] == 1)
							&& (tu2[zuox][youy] == 1 || tu2[zuox + 1][youy] == 1) && tu2[youx][youy] != 1
							&& tu2[youx][zuoy] != 1) {
						key += "D";
					} else if ((tu2[zuox][zuoy] == 1 || tu2[zuox][zuoy + 1] == 1) && tu2[zuox][youy] != 1
							&& tu2[youx][youy] != 1 && (tu2[youx][zuoy + 1] == 1 || tu2[youx][zuoy] == 1)) {
						key += "S";
					} else if (tu2[zuox][zuoy] != 1 && tu2[zuox][youy] != 1
							&& (tu2[youx][youy] == 1 || tu2[youx - 1][youy] == 1)
							&& (tu2[youx][zuoy] == 1 || tu2[youx - 1][zuoy] == 1)) {
						key += "A";
					} else if (tu2[zuox][zuoy] != 1 && (tu2[zuox][youy] == 1 || tu2[zuox][youy - 1] == 1)
							&& (tu2[youx][youy] == 1 || tu2[youx][youy - 1] == 1) && tu2[youx][zuoy] != 1) {
						key += "W";
					} else {
						int x = zuox - 2;
						int y = 0;
						BufferedImage img = ImgTool.cutImage(image, x, y, 18, image.getHeight());
						Point point = 图中找图EX(img, "A.bmp", 0.1f, 0, 0);
						if (point != null) {
							key += "A";
						} else if ((point = 图中找图EX(img, "S.bmp", 0.1f, 0, 0)) != null) {
							key += "S";
						} else if ((point = 图中找图EX(img, "D.bmp", 0.1f, 0, 0)) != null) {
							key += "D";
						} else if ((point = 图中找图EX(img, "W.bmp", 0.1f, 0, 0)) != null) {
							key += "W";
						}
					}
					j = youx + 10;
					break;
				}
			}
		}
		DataSave.dy.textField.setText(key);
		保存图片(image, key + ".bmp");
		// System.out.println("code=" + key);
		if (key.trim().equals("")) {
			return key;
		}
		// 8 10 黄 //6可能是蓝，可能是碎片 //2必定是绿 //4 碎片
		if (!DataSave.黄) {
			if (key.length() >= 7 && DataSave.黄鱼失误) {
				Other.sleep(time);
				if (key.charAt(0) == 'A')
					keyPressOne(KeyEvent.VK_W);
				else
					keyPressOne(KeyEvent.VK_A);
				return key;
			}
		}
		if (!DataSave.蓝 && !DataSave.绿) {
			if (DataSave.黄 && key.length() >= 1) {

			} else if (key.length() <= 1 && DataSave.黄鱼失误) {
				Other.sleep(time);
				if (key.charAt(0) == 'A')
					keyPressOne(KeyEvent.VK_W);
				else
					keyPressOne(KeyEvent.VK_A);
				return key;
			}
		}
		char c[] = key.toCharArray();
		for (char cc : c) {
			Other.sleep(time);
			keyPressOne(getkeyCode(cc + ""));
		}
		return key;
	}

	public int getBImages(int x1, int y1, int size, int[][] tu) {
		tu[x1][y1] = 0;
		if (zuox > x1)
			zuox = x1;
		if (zuoy > y1)
			zuoy = y1;
		if (youx < x1)
			youx = x1;
		if (youy < y1)
			youy = y1;
		if (x1 - 1 >= 0 && tu[x1 - 1][y1] == 1)
			size = getBImages(x1 - 1, y1, size + 1, tu);
		if (y1 - 1 >= 0 && tu[x1][y1 - 1] == 1)
			size = getBImages(x1, y1 - 1, size + 1, tu);
		if (x1 + 1 < tu.length && tu[x1 + 1][y1] == 1)
			size = getBImages(x1 + 1, y1, size + 1, tu);
		if (y1 + 1 < tu[0].length && tu[x1][y1 + 1] == 1)
			size = getBImages(x1, y1 + 1, size + 1, tu);
		return size;

	}

	public String getColorInHexFromRGB(int r, int g, int b) {
		return vali(getHexNum(b)) + vali(getHexNum(g)) + vali(getHexNum(r));
	}

	private String vali(String s) {
		while (s.length() < 2) {
			s = "0" + s;
		}
		return s;
	}

	private String getHexNum(int num) {
		int result = num / 16;
		int mod = num % 16;
		StringBuilder s = new StringBuilder();
		hexHelp(result, mod, s);
		return s.toString();
	}

	private void hexHelp(int result, int mod, StringBuilder s) {
		char[] H = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		if (result > 0) {
			hexHelp(result / 16, result % 16, s);
		}
		s.append(H[mod]);
	}

	// private Point move_xy = new Point();
	//
	// public void initMouseXY(Point point) {
	// move_xy = point;
	// }

	public void 删除图片(String string) {
		File f = new File(DataSave.JARFILEPATH + "\\data\\" + string);
		if (f.exists())
			f.delete();
	}

	public int 区域找色点数量(int x1, int y1, int x2, int y2, String color) {
		BufferedImage image = 截取屏幕(x1, y1, x2, y2);
		int k = 0;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				if (image.getRGB(i, j) == StringColor(color).getRGB()) {
					k++;
				}
			}
		}
		return k;
	}

	public double 点颜色比较(int i, int j, String string) {
		Color c1 = robot.getPixelColor(i, j);
		Color c2 = StringColor(string);
		return 颜色比较(c1, c2);
	}

	public BufferedImage 按比例截取屏幕(int i, int j, int width, int height, double k) {
		BufferedImage image = 截取屏幕(i, j, width, height);
		BufferedImage img = new BufferedImage((int) ((width - i) * k), (int) ((height - j) * k),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.drawImage(image, 0, 0, (int) ((width - i) * k), (int) ((height - j) * k), null);
		g.dispose();
		return img;
	}

	public BufferedImage 得到特征点集合图(int x1, int y1, int x2, int y2, double d, String string) {
		return 得到图的特征图(截取屏幕(x1, y1, x2, y2), d, string);
	}

	public BufferedImage 得到图的特征图(BufferedImage img, double d, String string) {
		Color color = StringColor(string);
		boolean bool = false;
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				if (i == 0 && j == 0 || i == 0 && j == image.getHeight() - 1 || i == image.getWidth() - 1 && j == 0
						|| image.getWidth() - 1 == i && j == image.getHeight() - 1)
					image.setRGB(i, j, new Color(0, 0, 0).getRGB());
				else if (颜色比较(new Color(img.getRGB(i, j)), color) <= d) {
					bool = true;
					image.setRGB(i, j, img.getRGB(i, j));
				} else
					image.setRGB(i, j, new Color(0, 0, 0).getRGB());
			}
		}
		return bool ? image : null;
	}

	public String 得到点颜色(int x, int y) {
		Color c1 = robot.getPixelColor(x, y);
		return getColorInHexFromRGB(c1.getRed(), c1.getGreen(), c1.getBlue());
	}


}
