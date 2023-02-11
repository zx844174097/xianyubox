package com.mugui.http;

import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.TCPModel;

public class DataSave {
	public static int dyTime = 20230118;
	public static int qpTime = 20230118;
	public static int dsTime = 20230118;
	public static int jgTime = 20230118;
	public static int myTime = 20230118;
	public static int ljTime = 20230118;
	private static Thread out_time_limit = null;
	private static boolean isTrue = false;

	private static long time1 = 0;
	private static long time2 = 0;
	private static long time3 = 0;

	public static void StartOutTime() {
		isTrue = true;
		if (out_time_limit != null && out_time_limit.isAlive())
			return;
		out_time_limit = new Thread(new Runnable() {
			@Override
			public void run() {
				reStartOutTime();
				// int n = 0;
				while (isTrue) {
					if (System.currentTimeMillis() - time1 > 60 * 1000 * 1) {
						UserBean userBean = new UserBean();
						TcpBag bag = new TcpBag();
						bag.setBag_id(-1);
						bag.setBody(userBean.toJsonObject());
						TCPModel.SendTcpBag(bag);
					}
					if (System.currentTimeMillis() - time2 > 60 * 1000 * 5) {
						com.mugui.ui.DataSave.tcpSocket.clearUserAllData();
						// TCPModel.tcpClose();
						// stop();
					}
					try {
						Thread.sleep(15 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (System.currentTimeMillis() - time3 > 60 * 1000 * 15) {
						// com.mugui.ui.DataSave.tcpSocket.close();
						TCPModel.tcpClose();
						stop();
					}
				}
			}
		});
		out_time_limit.start();
	}

	public static void reStartOutTime() {
		time1 = time2 = time3 = System.currentTimeMillis();
	}

	public static void stop() {
		isTrue = false;
		if (com.mugui.ui.DataSave.tcpSocket != null)
			com.mugui.ui.DataSave.tcpSocket.clearUserAllData();
	}
}
