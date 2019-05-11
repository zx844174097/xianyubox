package com.mugui.model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.PriorityQueue;

import com.mugui.Dui.DimgFile;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.qpOtherPanel;
import com.mugui.windows.Tool;

public class QpHandle {
	private static boolean isTure = false;
	static QPhread mainThread = null;
	public static TXThread txThread = null;
	private static int mode = 0;
	private static qpOtherPanel dimgFile[] = null;

	public static void setModel(int i) {
		mode = i;
	}

	public static int getModel() {
		return mode;
	}

	public static void setISTrue(boolean b) {
		isTure = b;
	}

	public static BufferedImage start(DimgFile file) {
		if (runThread == null || !runThread.isAlive()) {
			runThread = new KeepThread();
			runThread.start();
		}
		if (mode == 3) {
			mainThread = new QPhread();
			return mainThread.startTest(file);
		} else if (mode == 4) {
			mainThread = new QPhread();
			return mainThread.startTest2(file);
		}
		return null;

	}

	private static class KeepThread extends Thread {
		@Override
		public void run() {
			while (isTure) {
				TcpBag bag = new TcpBag();
				bag.setBag_id(TcpBag.ERROR);
				UserBean userBean = new UserBean();
				if (mode == 1 && com.mugui.http.DataSave.qpTime <= 0)
					userBean.setCode("ds");
				else
					userBean.setCode("qp");
				bag.setBody(userBean.toJsonObject());
				TCPModel.SendTcpBag(bag);
				int i = 0;
				while (i <= 60) {
					Other.sleep(1000);
					// QPhread.benX = DataSave.SCREEN_WIDTH / 2 + 402 +
					// DataSave.SCREEN_X;
					// QPhread.benY = DataSave.SCREEN_HEIGHT / 2 - 327 +
					// DataSave.SCREEN_Y;
					i++;
					if (!isTure)
						return;
				}
			}
		}
	}

	static Thread runThread = null;

	public static void start() {
		if (!HsFileHandle.isRunModel()) {
			return;
		}
		if (isTure || (runThread != null && runThread.isAlive()))
			return;
		isTure = true;
		runThread = new KeepThread();
		runThread.start();
		// 图片资源
		mainThread = null;
		txThread = null;
		QPtempData.init();
		DSHandle.startDxgj();
		if (mode == 1) {
		} else if (mode == 2) {
			DimgFile[] files = DataSave.qp.getDimgFiles();
			dimgFile = new qpOtherPanel[files.length];
			int s = 0;
			for (qpOtherPanel panel : dimgFile) {
				panel = new qpOtherPanel(files[s++], 0, 0);
				dimgFile[s - 1] = panel;
				if (s <= 5) {
					long time = System.currentTimeMillis();
					QPtempData.add(panel, time, time);
				}
			}
			// int d = DataSave.qp.getDeaultOne();
			// dd = dimgFile[d];
			txThread = new TXThread(dimgFile);
		} else if (mode == 3) {
		}
		mainThread = new QPhread();
		mainThread.start();
		if (txThread != null) {
			txThread.start();
		}
		// QPtempData.run();
	}

	public static boolean isClose() {
		return isTure;
	}

	public static void stop() {
		isTure = false;
		QPtempData.clear();
		if (mainThread != null) {
			mainThread.close();
			while(mainThread.isAlive()) {
				Other.sleep(25); 
			}
		}
		close();
	}

	public static void close() {
		if (txThread != null) {
			txThread.close();
			while(mainThread.isAlive()) {
				Other.sleep(25); 
			}
		}
	}

	public static boolean isUseMaid() {
		return false;
	}
}

// 扫描右下键是否有新的物品加入

class TXThread extends Thread {
	// 启动一个物品搜索线程。
	private Tool qpTool = null;
	private boolean isTrue = false;
	private qpOtherPanel file[] = null;

	public TXThread(qpOtherPanel[] file) {
		this.file = file;
	}

	public void close() {
		isTrue = false;
	}

	@Override
	public void run() {
		if (qpTool == null)
			qpTool = new Tool();
		isTrue = true;
		while (QpHandle.mainThread == null) {
			Other.sleep(1000);
		}
		while (isTrue) {
			// 主线程暂停
			findObjectThread();
			if (!isTrue)
				return;
		}
	}

	private void findObjectThread() {
		// 1623,865,1677,920
		// 1625,841,1675,890
		// 1920 1080 //1070,531,1360,767//1630,842,1919,1079
		QPhread phread = QpHandle.mainThread;
		for (int i = 0; i < file.length; i++) {
			Point p1 = null;
			//Point p2 = null;
			Point pxy = new Point(DataSave.SCREEN_X, DataSave.SCREEN_Y);
			Dimension psize = new Dimension(DataSave.SCREEN_WIDTH, DataSave.SCREEN_HEIGHT);

			//1563,910,1626,966
			if (((p1 = qpTool.区域找图(psize.width - (1920-1563) + pxy.x, psize.height - (1080-910) + pxy.y, psize.width - (1920-1626) + pxy.x, psize.height - (1080-966) + pxy.y, 0.15,
					file[i].getDimgFile().bufferedImage)) != null) && isTrue) {
				Other.sleep(2000); 
				// 扫描到之后关闭这个物品的显示
				// 使当前运行中的程序暂停
				phread.stopOrStart();
				while (!phread.isWait()) {
					Other.sleep(10);
					if (!isTrue || !phread.isAlive()) {
						return;
					}
				}
				// 1712,1012 
				if (p1 != null) {
					qpTool.keyPressOne(KeyEvent.VK_ENTER);
					qpTool.delay(500);
					//qpTool.mouseMovePressOne(psize.width - 208 + pxy.x, psize.height - 98 + pxy.y, InputEvent.BUTTON1_MASK);
					//1875,913,
					qpTool.mouseMovePressOne(psize.width - (1920-1875) + pxy.x, psize.height - (1080-913) + pxy.y, InputEvent.BUTTON1_MASK);
					 
					qpTool.delay(500);
					QPtempData.add(file[i]);
					phread.stopOrStart();
					Other.sleep(500);
				}

			} else if (!isTrue)
				return;

		}
	}
}

class QPtempData {
	private static PriorityQueue<qpOtherPanel> link = null;

	public static void init() {
		if (link == null) {
			link = new PriorityQueue<qpOtherPanel>(256, new Comparator<qpOtherPanel>() {
				@Override
				public int compare(qpOtherPanel o1, qpOtherPanel o2) {
					return (int) (o1.getStart_time() - o2.getStart_time());
				}
			});
		}
		clear();

	}

	public static void clear() {
		if (link != null)
			link.clear();
	}

	public static void remove(qpOtherPanel dimgFile) {
		link.remove(dimgFile);
		dimgFile.setZEnd_time(System.currentTimeMillis());
		dimgFile.setStateText("被抛弃");
	}

	public static void add(qpOtherPanel dimgFile) {
		String s[] = dimgFile.getDimgFile().objecttime.trim().split("-");
		long time = System.currentTimeMillis();
		add(dimgFile, time + Long.parseLong(s[0]) * 60 * 1000, time + Long.parseLong(s[1]) * 60 * 1000);
	}

	public static void add(qpOtherPanel dimgFile, long start_time, long end_time) {
		dimgFile.setStart_time(start_time);
		dimgFile.setEnd_time(end_time);
		link.add(dimgFile);
		DataSave.qpList.addQPOther(dimgFile);
		dimgFile.setStateText("等待中");
	}

	public static qpOtherPanel getQp_other() {
		if (link != null) {
			qpOtherPanel panel = link.peek();
			if (panel != null) {
				return link.poll();
			}
		}
		return null;
	}

	public static qpOtherPanel isQp_other() {
		if (link == null) {
			return null;
		}
		qpOtherPanel panel = link.peek();
		return panel;
	}
}