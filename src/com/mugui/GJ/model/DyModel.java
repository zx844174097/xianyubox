package com.mugui.GJ.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.mugui.ModelInterface;
import com.mugui.GJ.ui.Custon;
import com.mugui.GJ.ui.DataSave;
import com.mugui.GJ.ui.DyPanel;
import com.mugui.http.Bean.DataTypeBean;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.TCPModel;
import com.mugui.tool.Other;
import com.mugui.windows.Tool;
import com.sun.javafx.geom.Point2D;

public class DyModel implements ModelInterface {

	private Tool shouTool = null;
	private boolean isTrue = false;
	private Thread mainThread = null;

	public long getTime() {
		UserBean bean = new UserBean();
		TcpBag bag = new TcpBag();
		bag.setBag_id(TcpBag.GET_DATA);
		bag.setBody(bean.toJsonObject());
		DataTypeBean dataTypeBean = new DataTypeBean();
		dataTypeBean.setType(DataTypeBean.GET_TIME);
		dataTypeBean.setBody("dy");
		dataTypeBean.setCode(bag.hashCode());
		bag.setBody_description(dataTypeBean);
		bag = TCPModel.waitAccpetSend(bag);
		String time = bag.getBody().toString();
		if (Other.isLong(time)) {
			return Long.parseLong(time);
		}
		return 0;
	}

	@Override
	public void init() {
		shouTool = new Tool();
		mainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Run();
				Point point = shouTool.区域找图(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 100, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 300,
						DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 100, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 50, 0.15, "钓鱼状态.bmp");
				if (point == null)
					return;
				if (!GameListenerThread.DJNI.isCorsurShow()) {
					shouTool.keyPressOne(KeyEvent.VK_CONTROL);
					shouTool.delay(200);
				}
				shouTool.mouseMovePressOne(point.x + 5, point.y + 5, InputEvent.BUTTON1_MASK);
				if (GameListenerThread.DJNI.isCorsurShow()) {
					shouTool.keyPressOne(KeyEvent.VK_CONTROL);
					shouTool.delay(200);
				}
				((DyPanel) DataSave.uiManager.get("DyPanel")).setLogText("结束钓鱼");
			}

		});

	}

	private DyPanel dyPanel = null;

	private void Run() {
		long time = 0;
		dyPanel = ((DyPanel) DataSave.uiManager.get("DyPanel"));
		while (isTrue) {
			if (System.currentTimeMillis() - time > 60 * 1000) {
				TcpBag bag = new TcpBag();
				bag.setBag_id(TcpBag.ERROR);
				UserBean userBean = new UserBean();
				userBean.setCode("gj_dy");
				bag.setBody(userBean.toJsonObject());
				TCPModel.SendTcpBag(bag);
				time = System.currentTimeMillis();
			} else {
				Other.sleep(1000);
			}
			while (isTrue && shouTool.区域找图(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 100, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 300,
					DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 100, DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 10, 0.15, "钓鱼状态.bmp") == null) {
				// 从背包中拿鱼竿
				checkBackpackFishRod(); 
				if (!isTrue)
					return; 
				switch (dyPanel.getFishType()) {
				case "地龙":
					shouTool.keyPressOne(KeyEvent.VK_E);
					shouTool.delay(1500);
					break;
				case "玲珑虾米":
					shouTool.keyPressOne(KeyEvent.VK_R);
					shouTool.delay(1500);
					break;
				case "五仙":
					shouTool.keyPressOne(KeyEvent.VK_Z);
					shouTool.delay(1500);
					break;
				default:
					break;
				}
			}
			if (!isTrue)
				return;
			shouTool.keyPressOne(KeyEvent.VK_Q);
			// 等待鱼上钩
			Point point2 = waitFish();
			if (!isTrue)
				return;
			// 开始收杆
			BufferedImage image = shouTool.截取屏幕(point2.x - 85, point2.y - 85, point2.x - 80 + 210, point2.y - 80 + 210);
			image = shouTool.得到图的特征图(image, 0.07, "68CFE8");
			// 找到集群
			findP1P2(image);
			if (!isTrue)
				return;
			if (!dyPanel.isKeepFish()) {
				shouTool.keyPressOne(Tool.getkeyCode(dyPanel.getHotKeyListner()));
				return;
			}
		}

	}

	private BufferedImage[] yugan = null;

	private void checkBackpackFishRod() {
		shouTool.keyPressOne(KeyEvent.VK_B);
		Point point = null;
		if (yugan == null) {
			Custon custon = (Custon) DataSave.uiManager.get("Custon");
			custon.dataInit();
			yugan = custon.getyuGan();
		}
		BufferedImage temp = null;
		while (isTrue && point == null) {
			temp = shouTool.截取屏幕(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y);
			for (BufferedImage image : yugan) { 
				point = shouTool.图中找图(temp, image, 0.15, 0, 0);
				if (point != null)
					break;
			}
			if (point == null) {
				point = shouTool.图中找图(temp, "鱼竿.bmp", 0.15, 0, 0);
			}
		}
		if (!isTrue)
			return;
		shouTool.mouseMovePressOne(point.x + 5, point.y + 5, InputEvent.BUTTON3_MASK);
		if (!isTrue)
			return;
		shouTool.delay(2500);
		if (dyPanel.getFishType().equals("夜明珠")) {
			while (isTrue && (point = shouTool.区域找图(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH + DataSave.SCREEN_X,
					DataSave.SCREEN_HEIGHT + DataSave.SCREEN_Y, 0.15, "夜明珠.bmp")) == null) {

			}
			if (!isTrue)
				return;
			shouTool.mouseMovePressOne(point.x + 5, point.y + 5, InputEvent.BUTTON3_MASK);
			shouTool.delay(2500);
		}
		shouTool.keyPressOne(KeyEvent.VK_B);
		shouTool.delay(500);

	}

	public void findP1P2(BufferedImage image) {
		Point2D p1 = null, p2 = null;
		for (int i = 0; i < image.getWidth() && isTrue; i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				// 发现一个集群
				if (image.getRGB(i, j) != black) {
					Point2D point = new Point2D(0, 0);
					double num = findCluster(image, i, j, point);
					point.x /= num;
					point.y /= num;
					if (num >= 2 && num < 12) {
						p1 = point;
					} else if (num > 380) {
						p2 = point;
					}
					if (p1 != null && p2 != null) {
						((DyPanel) DataSave.uiManager.get("DyPanel")).setLogText("p1=" + p1 + " " + "p2=" + p2);
						Graphics graphics = image.getGraphics();
						graphics.setColor(Color.RED);
						graphics.fillRect((int) p1.x - 1, (int) p1.y - 1, 2, 2);
						graphics.fillRect((int) p2.x - 1, (int) p2.y - 1, 2, 2);
						Point zhong = new Point((int) (image.getWidth() / 2.0), (int) (image.getHeight() / 2.0));
						graphics.fillRect((int) (zhong.x) - 1, (int) (zhong.y) - 1, 2, 2);
						graphics.setColor(Color.blue);
						graphics.drawLine((int) p1.x, (int) p1.y, zhong.x, zhong.y);
						graphics.drawLine((int) p1.x, (int) p1.y, zhong.x, zhong.y);
						graphics.setColor(Color.yellow);
						graphics.drawLine((int) p2.x, (int) p2.y, zhong.x, zhong.y);
						graphics.drawLine((int) p2.x, (int) p2.y, zhong.x, zhong.y);
						graphics.drawOval(zhong.x - 50, zhong.y - 50, 100, 100);
						double hu1 = Math.atan2(p1.y - zhong.y, p1.x - zhong.x);
						double hu2 = Math.atan2(p2.y - zhong.y, p2.x - zhong.x);
						Point point2 = new Point(zhong.x + (int) (Math.cos(hu1) * 50), zhong.y + (int) (Math.sin(hu1) * 50));
						graphics.setColor(Color.red);
						graphics.fillRect(point2.x - 5, point2.y - 5, 10, 10);
						point2.x = (int) (zhong.x + Math.cos(hu2) * 50);
						point2.y = zhong.y + (int) (Math.sin(hu2) * 50);

						graphics.setColor(Color.gray);
						graphics.fillRect(point2.x - 5, point2.y - 5, 10, 10);
						// ((DyPanel)
						// DataSave.uiManager.get("DyPanel")).getDraw_panel().repaint();
						if (!isTrue)
							return;

						double cang = hu2 > hu1 ? (hu2 - hu1) : Math.PI * 2 - (hu1 - hu2);
						((DyPanel) DataSave.uiManager.get("DyPanel")).setLogText("时间：" + cang + "");
						shouTool.delay((int) (cang / ((DyPanel) DataSave.uiManager.get("DyPanel")).getDelayIndex() * 100));
						shouTool.keyPressOne(KeyEvent.VK_Q);
						if (!dyPanel.isKeepFish()) {
							shouTool.delay(4500);
							shouTool.keyPressOne(Tool.getkeyCode(dyPanel.getHotKeyListner()));
							return;
						}
						shouTool.delay(6000);
						shouTool.keyPressOne(KeyEvent.VK_Q);
						shouTool.delay(700);
						return;
					}
				}
			}
		}
		shouTool.delay(500);
		if (!isTrue)
			return;
		shouTool.keyPressOne(KeyEvent.VK_Q);
		if (!isTrue)
			return;
		shouTool.delay(6000);
		shouTool.keyPressOne(KeyEvent.VK_Q);
		shouTool.delay(700);

	}

	int black = new Color(0, 0, 0).getRGB();

	private int findCluster(BufferedImage image, int i, int j, Point2D point2) {
		image.setRGB(i, j, black);
		point2.x += i;
		point2.y += j;
		int num = 1;
		for (int n = i - 5; n < i + 5; n++) {
			if (n >= 0 && n < image.getWidth()) {
				for (int m = j - 5; m < j + 5; m++) {
					if (m >= 0 && m < image.getHeight()) {
						if (image.getRGB(n, m) != black) {
							num += findCluster(image, n, m, point2);
						}
					}
				}
			}
		}
		return num;
	}

	private Point waitFish() {
		long time = System.currentTimeMillis();
		while (isTrue) { // 487 255 811 509
			Point point = shouTool.区域找图(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - (1920 / 2 - 487),
					DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 - (1080 / 2 - 255), DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - (1920 / 2 - 811),
					DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT / 2 - (1080 / 2 - 509), 0.07, "鱼_上钩.bmp");
			if (point != null) {
				return point;
			}
			if (System.currentTimeMillis() - time > 11000) {
				time = System.currentTimeMillis();
				shouTool.keyPressOne(KeyEvent.VK_Q);
			}
		}
		return null;
	}

	@Override
	public void start() {
		if (isrun()) {
			return;
		}
		init();
		isTrue = true;
		mainThread.start();
	}

	@Override
	public void stop() {
		isTrue = false;
	}

	public boolean isrun() {

		return isTrue;
	}

}
