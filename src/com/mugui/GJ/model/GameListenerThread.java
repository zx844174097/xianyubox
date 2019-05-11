package com.mugui.GJ.model;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.mugui.ModelInterface;
import com.mugui.Dui.DimgFile;
import com.mugui.GJ.ui.DataSave;
import com.mugui.jni.Tool.DJni;
import com.mugui.model.GJUIHandle;
import com.mugui.tool.ImgTool;
import com.mugui.tool.Other;
import com.mugui.windows.Tool;

public class GameListenerThread implements ModelInterface {
	private static boolean isTrue = false;
	public static int APP_ID = 0;
	public static DJni DJNI = null;
	private HashMap<String, BufferedImage> map = null;
	private String string[] = { "(", "9", "8", "7", "6", "5", "4", "3", "2", "0", "1", "-", "," };
	public static Tool shouTool = null;
	public static int HWND = 0;

	private Point getUserPoint(BufferedImage draw_img) {
		map = readFile();
		BufferedImage image = map.get(string[0] + ".bmp");
		// shouTool.保存图片(image, string[0] + ".bmp");
		while (isTrue) {
			Point point = shouTool.图中找图EX(draw_img, image, 0.15, 0, 0);
			if (point == null)
				break;
			draw_img = ImgTool.cutImage(draw_img, point.x + image.getWidth() - 1, 0, draw_img.getWidth() - point.x - image.getWidth(), draw_img.getHeight());
			if (draw_img == null) {
				return new Point(0, 0);
			}
		}
		if (draw_img == null)
			return new Point(0, 0);
		CharTemp temp[] = new CharTemp[21];
		int temp_n = 0;
		for (int i = 0; i < string.length && isTrue; i++) {
			if (temp_n == temp.length)
				return new Point(0, 0);
			image = map.get(string[i] + ".bmp");
			Point point = shouTool.图中找图EX(draw_img, image, 0.15, 0, 0);
			if (point != null) {
				temp[temp_n] = new CharTemp();
				temp[temp_n].c = string[i];
				temp[temp_n++].n = point.x;
				i = -1;
				for (int k = 0; k < image.getWidth(); k++) {
					for (int j = 0; j < image.getHeight(); j++) {
						draw_img.setRGB(k + point.x, j + point.y, 0);
					}
				}

			}

		}
		Arrays.sort(temp, new Comparator<CharTemp>() {
			@Override
			public int compare(CharTemp o1, CharTemp o2) {
				if (o1 == null || o2 == null)
					return 999;
				return o1.n - o2.n;
			}
		});
		Point point = new Point(0, 0);
		boolean a = true;
		boolean b = true;
		int s = 1;
		int x = 0;
		int y = 0;
		for (int i = 0; i < temp.length && isTrue; i++) {
			if (temp[i] == null)
				continue;
			if (temp[i].c.equals("-")) {
				if (s == 1)
					a = false;
				else
					b = false;
			} else if (temp[i].c.equals(",")) {
				s = 2;
			} else {
				if (s == 1)
					x = 10 * x + Integer.parseInt(temp[i].c);
				else
					y = 10 * y + Integer.parseInt(temp[i].c);
			}
		}
		if (!a)
			x = -x;
		if (!b)
			y = -y;
		point.x = x;
		point.y = y;
		return point;
	}

	private class CharTemp {
		public String c;
		public int n;

		@Override
		public String toString() {
			return c + ":" + n;
		}
	}

	public HashMap<String, BufferedImage> readFile() {
		if (map != null)
			return map;
		HashMap<String, BufferedImage> map = new HashMap<>();
		try {
			File file = new File(DataSave.JARFILEPATH + "\\古剑奇谭\\data\\数字.bmp");
			InputStream inputStream = DimgFile.getInputString(file);
			if (inputStream == null) {
				throw new NullPointerException("not find");
			}
			ZipInputStream Zin = new ZipInputStream(inputStream);
			BufferedInputStream Bin = new BufferedInputStream(Zin);
			ZipEntry entry;
			while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int len;
				byte[] b = new byte[512];
				while ((len = Bin.read(b)) > 0) {
					out.write(b, 0, len);
				}
				out.close();
				map.put(entry.getName(), ImgTool.byteArrayToImg(out.toByteArray()));
			}
			Bin.close();
			Zin.close();
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private final static String[] location_name = { "拔仙台", "补天岭", "怀秀村", "江都城", "上淮青野", "渭川塬", "长安", "终南山" };

	private String getUserLocationName(BufferedImage temp) {
		if (DataSave.USER_LOCATION_NAME == null) {
		} else if (shouTool.图中找图EX(temp, DataSave.USER_LOCATION_NAME + ".bmp", 0.15, 0, 0) != null) {
			return DataSave.USER_LOCATION_NAME;
		}
		for (String s : location_name) {
			Point point = shouTool.图中找图EX(temp, s + ".bmp", 0.15, 0, 0);
			if (point != null) {
				return s;
			}
		}
		return null;
	}

	public void start() {
		if (isTrue)
			return;
		isTrue = true;
		// Tesseract2.initData();
		if (thread == null || !thread.isAlive()) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					DJNI = new DJni();
					if (DJni.isTrue) {
						return;
					}
					while (isTrue) {
						APP_ID = GJUIHandle.getGameAppid();
						if (HWND == 0) {
							HWND = DJNI.getWinHwnd(APP_ID);
						}
						if (APP_ID == 0) {
							System.out.println("0检测到游戏以关闭");
							int ci = 0;
							while (ci < 20) {
								APP_ID = GJUIHandle.getGameAppid();
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
							GJUIHandle.closeAll();
							com.mugui.ui.DataSave.StaticUI.updateUI(com.mugui.ui.df.HsInitPanel.main);
							return;
						}
						a = getRect(DJNI, APP_ID);
						if (a == null || com.mugui.ui.DataSave.兼容) {
							DataSave.SCREEN_X = 0;
							DataSave.SCREEN_Y = 0;
							DataSave.SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
							DataSave.SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
						} else {
							DataSave.SCREEN_X = a[0];
							DataSave.SCREEN_Y = a[1];
							DataSave.SCREEN_WIDTH = a[2];
							DataSave.SCREEN_HEIGHT = a[3];
						}
						if (shouTool == null)
							shouTool = new Tool();
						BufferedImage temp = shouTool.截取屏幕(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 135, DataSave.SCREEN_Y + 0,
								DataSave.SCREEN_X + DataSave.SCREEN_WIDTH, DataSave.SCREEN_Y + 30);
						DataSave.USER_LOCATION_NAME = getUserLocationName(temp);
						if (DataSave.USER_LOCATION_NAME == null)
							DataSave.USER_LOCATION = new Point(0, 0);
						else
							DataSave.USER_LOCATION = getUserPoint(temp);
						shouTool.delay(10);
					}
					isTrue = false;
				}

				private int b[] = { 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, 0 };
				private long time = 0;
				private int file_rect[] = null;

				private int[] getRect(DJni jni, int s) {

					int[] temp = jni.getWinRect(s);

					if (temp == null || temp[2] <= 0 || temp[3] <= 0) {
						return b;
					}
					if (System.currentTimeMillis() - time > 5000) {
						file_rect = readFileRect(jni, s);
						time = System.currentTimeMillis();
					}
					if (file_rect == null)
						return temp;

					temp[0] = temp[0] + (temp[2] - file_rect[0]) / 2;
					temp[1] = temp[1] + temp[3] - file_rect[1] - (temp[2] - file_rect[0]) / 2;
					temp[2] = file_rect[0];
					temp[3] = file_rect[1];
					return temp;
				}

			});
			thread.start();
		}
	}

	protected int[] readFileRect(DJni jni, int s) {
		int handle_id = jni.getWinAppHandleByID(s);
		String now_path = jni.getWinAppFilePath(handle_id).trim();
		if (now_path == null) {
			return a;
		}
		File file = new File(now_path);
		file = new File(file.getParentFile().getParent() + "\\settings\\GameSetting.dat");

		try {
			RandomAccessFile read = new RandomAccessFile(file, "r");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int len = 0;
			if (read.getChannel().isOpen()) {
				while ((len = read.read(b)) > 0) {
					outputStream.write(b, 0, len);
				}
			}
			read.close();
			outputStream.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()), Charset.forName("UTF-8")));
			String string = reader.readLine();
			int[] rect = new int[2];
			while ((string = reader.readLine()) != null) {
				String str[] = string.split("=");
				if (str[0].trim().equals("[\"Height\"]")) {
					rect[1] = Integer.parseInt(str[1].split(",")[0].trim());
				} else if (str[0].trim().equals("[\"Width\"]")) {
					rect[0] = Integer.parseInt(str[1].split(",")[0].trim());
				}
				if (rect[0] != 0 && rect[1] != 0)
					return rect;
			}
			return rect;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void stop() {
		isTrue = false;
	}

	private static Thread thread = null;
	private static int a[] = new int[4];

	@Override
	public void init() {

	}

	@Override
	public boolean isrun() {
		return false;
	}

}
