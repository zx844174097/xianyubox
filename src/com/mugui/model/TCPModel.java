package com.mugui.model;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.mugui.MAIN;
import com.mugui.Dui.DInputDialog;
import com.mugui.Dui.InfoBody;
import com.mugui.Dui.TimeInfo;
import com.mugui.http.NetBagManager;
import com.mugui.http.NetBagManager.NetBagListener;
import com.mugui.http.Bean.FileBean;
import com.mugui.http.Bean.InfoBean;
import com.mugui.http.Bean.JsonBean;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.Bean.WindowListenerBean;
import com.mugui.http.pack.Bag;
import com.mugui.http.pack.TcpBag;
import com.mugui.http.pack.UdpBag;
import com.mugui.http.tcp.TcpSocketClient;
import com.mugui.http.tcp.TcpSocketUserBean;
import com.mugui.ui.DataSave;
import com.mugui.ui.df.BoldShortCutPanel;
import com.mugui.ui.df.FishPriceFrame;
import com.mugui.ui.info.DownloadPanel;
import com.mugui.windows.WindowControlThread;

public class TCPModel {
	static int a = 0;

	public static void sendByteArray(TcpBag bag) {
		send(bag, false);
	}

	public static void SendTcpBag(TcpBag bag) {
		send(bag, true);

	}

	private static void send(TcpBag bag, boolean type) {

		if (DataSave.网络兼容) {
			a = 11;
		}
		if (a >= 10) {
			// UdpBag udpBag = new UdpBag();
			// udpBag.setVersion(bag.getVersion());
			// udpBag.setBody(bag.getBody());
			// udpBag.setBag_id(bag.getBag_id());
			// udpBag.setBody_len(bag.getBody_len());
			// udpBag.setBody_description(bag.getBody_description());
			// UDPModel.SendUdpBag(udpBag);

			return;
		}

		if (DataSave.tcpSocket != null && DataSave.tcpSocket.isSocketRun()) {
			if (type)
				DataSave.tcpSocket.sendCompact(bag);
			else
				DataSave.tcpSocket.sendByteArray(bag);
			a = 0;
		} else {
			System.out.println(new Date(System.currentTimeMillis()).toLocaleString() + "建立连接" + a);
			a++;
			// DataSave.tcpSocket = new
			// TcpSocketUserBean(HttpTool.getSocket("139.224.54.199", 5100), new
			// HsHandle());
			// DataSave.tcpSocket = new
			// TcpSocketUserBean(HttpTool.getSocket("118.190.26.99", 5100), new
			// HsHandle());
			// root zuo123m
			// DataSave.tcpSocket = new
			// TcpSocketUserBean(HttpTool.getSocket("120.76.245.91", 5100), new
			// HsHandle());
			// DataSave.tcpSocket = new
			// TcpSocketUserBean(HttpTool.getSocket("112.74.129.53", 5100), new
			// HsHandle());
			// DataSave.tcpSocket = new TcpSocketUserBean("192.168.0.106",
			// 5100);
			if (DataSave.tcpSocket != null)
				DataSave.tcpSocket.clearUserAllData();
			else
				DataSave.tcpSocket = new TcpSocketUserBean();
			DataSave.tcpSocket.setSocketChannel(new TcpSocketClient("118.190.26.99", 5100));
			//DataSave.tcpSocket.setSocketChannel(new TcpSocketClient("127.0.0.1", 5100));
			DataSave.tcpSocket.reCodeTime();
			DataSave.tcpSocket.setThreadMaxSize(20);
			// DataSave.tcpSocket = new
			// TcpSocketUserBean(HttpTool.getSocket("192.168.0.106", 5100), new
			// HsHandle());
			if (!DataSave.tcpSocket.isSocketRun()) {
				// tcpClose();
				// a = 10;
				SendTcpBag(bag);
				return;
			}
			SendTcpBag(bag);
		}

	}
	// public static void SendByteArraysTCPBag(TcpBag TcpBag) {
	// if (DataSave.网络兼容)
	// a = 11;
	// if (a >= 10) {
	// UdpBag udpBag = new UdpBag();
	// udpBag.setVersion(TcpBag.getVersion());
	// udpBag.setBody(TcpBag.getBody());
	// udpBag.setBody_len(TcpBag.getBody_len());
	// udpBag.setBody_description(TcpBag.getBody_description());
	// udpBag.setBag_id(TcpBag.getBag_id());
	// UDPModel.SendByteArraysUDPBag(udpBag);
	// return;
	// }
	// if (DataSave.tcpSocket != null && DataSave.tcpSocket.isClose()) {
	// DataSave.tcpSocket.SendByteArrays(TcpBag);
	// a = 0;
	// } else {
	// if (a != 0)
	// System.out.println(new Date(System.currentTimeMillis()).toLocaleString()
	// + "断线重连" + a);
	// a++;
	// // 断网重连
	// // DataSave.tcpSocket = new
	// // TcpSocketUserBean(HttpTool.getSocket("139.224.54.199", 5100), new
	// // HsHandle());
	// // DataSave.tcpSocket = new
	// // TcpSocketUserBean(HttpTool.getSocket("118.190.26.99", 5100), new
	// // HsHandle());
	// // root zuo123m
	// // DataSave.tcpSocket = new
	// // TcpSocketUserBean(HttpTool.getSocket("120.76.245.91", 5100), new
	// // HsHandle());
	// // DataSave.tcpSocket = new
	// // TcpSocketUserBean(HttpTool.getSocket("112.74.129.53", 5100), new
	// // HsHandle());
	// DataSave.tcpSocket = new
	// TcpSocketUserBean(HttpTool.getSocket("192.168.0.106", 5100), new
	// HsHandle());
	// if (DataSave.tcpSocket == null || !DataSave.tcpSocket.isClose()) {
	// // tcpClose();
	// // a = 10;
	// SendByteArraysTCPBag(TcpBag);
	// return;
	// }
	// DataSave.tcpSocket.start();
	// SendByteArraysTCPBag(TcpBag);
	// }
	// }

	public static void tcpClose() {
		StackTraceElement element = Thread.currentThread().getStackTrace()[2];
		String className = element.getClassName();
		String methodName = element.getMethodName();
		int lineNumber = element.getLineNumber();
		System.out.println("对象:" + className + " 方法:" + methodName + "(" + lineNumber + ")");
		if (DataSave.tcpSocket != null)
			DataSave.tcpSocket.clearUserAllData();
		HsAllModel.closeAll();
		System.out.println("网络连接已经断开");
		new TimeInfo("错误", "网络连接已经断开", 1500).run(DataSave.login);
	}

	public static void error(TcpBag tcpBag) {
		DataSave.StaticUI.setVisible(true);
		// DataSave.dialog.setVisible(true);
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, tcpBag.getBody());
		new TimeInfo("警告！", infoBean.getMessage(), 1500).run();
		System.out.println(infoBean.getMessage());
	}

	// 登录成功
	public static void login(TcpBag tcpBag, TcpSocketUserBean tcpSocket) {
		// DataSave.StaticUI.setVisible(true);
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, tcpBag.getBody());
		HsAllModel.login(infoBean);
	}

	public static void outLogin(TcpBag tcpBag, TcpSocketUserBean tcpSocket) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, tcpBag.getBody());
		System.out.println(infoBean.getMessage());
		StackTraceElement element = Thread.currentThread().getStackTrace()[2];
		String className = element.getClassName();
		String methodName = element.getMethodName();
		int lineNumber = element.getLineNumber();
		System.out.println("对象:" + className + " 方法:" + methodName + "(" + lineNumber + ")");
		DataSave.StaticUI.setVisible(true);
		HsAllModel.closeAll();
		DataSave.userBean = null;
		DInputDialog dialog = new DInputDialog("消息：", infoBean.getMessage(), true, false);
		UIModel.setUI(dialog);
		dialog.start();
		UIModel.setUI(DataSave.login);
	}

	public static void regCode(TcpBag tcpBag, TcpSocketUserBean tcpSocket) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, tcpBag.getBody());
		new TimeInfo("信息", infoBean.getMessage(), 1500).run();
	}

	public static void reg(TcpBag tcpBag, TcpSocketUserBean tcpSocket) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, tcpBag.getBody());
		new TimeInfo("信息", infoBean.getMessage(), 1500).run();
		UIModel.setlogin("登录");
	}

	// public static void dyCode(TcpBag tcpBag, TcpSocketUserBean tcpSocket) {
	// InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class,
	// tcpBag.getBody());
	// Tool.dy_code = infoBean.getMessage();
	// DataSave.dy.textField.setText(Tool.dy_code);
	// }

	public static void getDyTime(TcpBag tcpBag, TcpSocketUserBean tcpSocket) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, tcpBag.getBody());
		com.mugui.http.DataSave.dyTime = Integer.parseInt(infoBean.getMessage());
		DataSave.StaticUI.setTime(com.mugui.http.DataSave.dyTime);
		if (com.mugui.http.DataSave.dyTime > 0) {
			DataSave.dy.setUse(true);
		}
	}

	public static void getQpTime(TcpBag tcpBag, TcpSocketUserBean tcpSocket) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, tcpBag.getBody());
		com.mugui.http.DataSave.qpTime = Integer.parseInt(infoBean.getMessage());
		DataSave.StaticUI.setTime(com.mugui.http.DataSave.qpTime);
		if (com.mugui.http.DataSave.qpTime > 0) {
			DataSave.qp.setUse(true);
		}
	}

	public static void getDsTime(TcpBag accpet, TcpSocketUserBean tcpSocket) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, accpet.getBody());
		HsAllModel.getDsTime(infoBean);
		if (com.mugui.http.DataSave.dsTime > 0) {
			DataSave.ds.setUse(true);
		}
	}

	public static void getJgTime(TcpBag accpet, TcpSocketUserBean tcpSocket) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, accpet.getBody());
		com.mugui.http.DataSave.jgTime = Integer.parseInt(infoBean.getMessage());
		DataSave.StaticUI.setTime(com.mugui.http.DataSave.jgTime);
		if (com.mugui.http.DataSave.jgTime > 0) {
			DataSave.jg.setUse(true);
		}
	}

	public static void getMyTime(TcpBag accpet, TcpSocketUserBean tcpSocket) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, accpet.getBody());
		com.mugui.http.DataSave.myTime = Integer.parseInt(infoBean.getMessage());
		DataSave.StaticUI.setTime(com.mugui.http.DataSave.myTime);
		if (com.mugui.http.DataSave.myTime > 0) {
			DataSave.my.setUse(true);
		}
	}

	public static void getLjTime(TcpBag accpet, TcpSocketUserBean tcpSocket) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, accpet.getBody());
		com.mugui.http.DataSave.ljTime = Integer.parseInt(infoBean.getMessage());
		DataSave.StaticUI.setTime(com.mugui.http.DataSave.ljTime);
		if (com.mugui.http.DataSave.ljTime > 0) {
			DataSave.lj.setUse(true);
		}
	}

	// 设置昵称返回
	public static void setUserName(TcpBag tcpBag) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, tcpBag.getBody());
		if (infoBean.getId().equals(InfoBean.SUCCESS)) {
			DataSave.userBean = new UserBean(JSONObject.fromObject(infoBean.getBody()));
			DataSave.StaticUI.setUser_name(DataSave.userBean.getUser_name());
			if (DataSave.StaticUI.getUI() == DataSave.Tank)
				DataSave.Tank.startSnake();
		} else {
			new TimeInfo("信息", infoBean.getMessage(), 1500).run();
		}
	}

	public static void saveSnakeMark() {

	}

	public static void snakeMarkAll(TcpBag accpet) {
		@SuppressWarnings("unchecked")
		Iterator<String> i = JSONArray.fromObject(accpet.getBody()).iterator();
		LinkedList<UserBean> linkedList = new LinkedList<UserBean>();
		while (i.hasNext()) {
			linkedList.add(new UserBean(JSONObject.fromObject(i.next())));
		}
		Collections.sort(linkedList, new Comparator<UserBean>() {
			@Override
			public int compare(UserBean o1, UserBean o2) {
				return Integer.parseInt(o2.getUser_snake_mark()) - Integer.parseInt(o1.getUser_snake_mark());
			}
		});
		DataSave.Tank.setList(linkedList);
	}

	public static void appOld(TcpBag accpet) {
		error(accpet);
		TCPModel.selectNewApp();
	}

	public static void updateRegCode(TcpBag accpet) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, accpet.getBody());
		new TimeInfo("信息", infoBean.getMessage(), 1500).run();
	}

	public static void updateUserPawd(TcpBag accpet) {
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, accpet.getBody());
		new TimeInfo("信息", infoBean.getMessage(), 1500).run();
	}

	public static void sendFishLineFeature(TcpBag accpet) {
		HsAllModel.sendFishLineFeature((byte[]) accpet.getBody(), (int) accpet.getBody_description());
	}

	public static void sendOneFishLineFeature(TcpBag accpet) {
		HsAllModel.sendOneFishLineFeature((byte[]) accpet.getBody(), (String) accpet.getBody_description());
	}

	public static void getLineAllYuBody(TcpBag accpet) {
		HsAllModel.getLineAllYuBody((byte[]) accpet.getBody(), (Integer) accpet.getBody_description());
	}

	public static void sendOneBoldLine(TcpBag accpet) {
		byte s[] = (byte[]) accpet.getBody();
		if (s == null)
			return;
		HsAllModel.sendOneBoldLine(s);
	}

	public static void getBoldLines(TcpBag accpet) {
		HsAllModel.getBoldLines(new String((byte[]) accpet.getBody()));
		FishPriceFrame.initBody(DataSave.D_LINE_ID);
		BoldShortCutPanel.updateUI();
	}

	/**
	 * 更新了一个boss的刷新
	 * 
	 * @param accpet
	 */
	public static void sendBossUpdateOne(TcpBag accpet) {
		HsAllModel.sendBossUpdateOne(accpet.getBody().toString());
	}

	public static void getAllBossUpdateTime(TcpBag accpet) {
		HsAllModel.getAllBossUpdateTime(JSONArray.fromObject(accpet.getBody()));
	}

	public static void appSendStartListener1(TcpBag accpet, TcpSocketUserBean tcpSocket) {
		// if (!DyHandle.isRun() || !DataSave.被监视) {
		// UIModel.sendAppInfo("错误", "钓鱼功能未启用，或者未勾选被监视");
		// }
		WindowListenerBean userBean = new WindowListenerBean().newInstanceBean(accpet.getBody());
		userBean.setUser_mail("APP_1.00" + userBean.getUser_mail());
		userBean.setX(DataSave.SCREEN_X);
		userBean.setY(DataSave.SCREEN_Y + DataSave.SCREEN_HEIGHT - 400);
		userBean.setW(350);
		userBean.setH(350);
		userBean.setMark(new Object().hashCode());
		accpet.setBody_description(userBean);
		WindowControlThread.start(accpet, tcpSocket);
		userBean.setX(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 350);
		userBean.setY(0 + DataSave.SCREEN_Y);
		userBean.setW(350);
		userBean.setH(350);
		userBean.setMark(new Object().hashCode());
		accpet.setBody_description(userBean);
		WindowControlThread.start(accpet, tcpSocket);
	}

	public static boolean bool_listener = true;

	public static void sendManListener() {
		if (!bool_listener)
			return;
		// System.out.println("发送消息提醒");
		TcpBag bag = new TcpBag();
		bag.setBag_id(TcpBag.SEND_MAN_LISTENER);
		bag.setBody(new UserBean());
		SendTcpBag(bag);
	}

	public static void stopManListener() {
		// System.out.println("停止消息提醒");
		bool_listener = false;
	}

	public static void sendStopManListener() {
		stopManListener();
		TcpBag bag = new TcpBag();
		bag.setBag_id(TcpBag.SEND_STOP_MAN_LISTENER);
		bag.setBody(new UserBean());
		SendTcpBag(bag);
	}

	public static String sendinfo = null;

	public static void sendPcSendInfo(TcpBag accpet) {
		InfoBean infoBean = InfoBean.newInstanceBean(InfoBean.class, accpet.getBody());
		if (DyHandle.isRun()) {
			sendinfo = infoBean.getBody().toString();
		} else {
			DSHandle.sendPCInfo(infoBean.getBody().toString());
		}
	}

	public static void selectNewApp() {
		TcpBag bag = new TcpBag();
		bag.setBag_id(TcpBag.SELECT_APP_ID);
		bag.setBody(DataSave.StaticUI.getApp_id());

		SendTcpBag(bag);
	}

	public static void selectAppID(TcpBag accpet, TcpSocketUserBean tcpSocket) {
		if (accpet.getBody() == null) {
			return;
		}
		InfoBean infoBean = JsonBean.newInstanceBean(InfoBean.class, accpet.getBody());
		if (infoBean == null) {
			return;
		}
		if (infoBean.getType().equals("error")) {
			InfoBody infoBody = new InfoBody("信息", infoBean.getMessage(), true, true);
			UIModel.setUI(infoBody);
			switch (infoBody.getState()) {
			case 0:
				DataSave.login = null;
				UIModel.setUI(DataSave.downloadList);
				JSONArray array = JSONArray.fromObject(infoBean.getBody());
				@SuppressWarnings("unchecked")
				Iterator<String> iterator = array.iterator();
				while (iterator.hasNext()) {
					DataSave.downloadList.addDownloadPanel(new DownloadPanel(FileBean.newInstanceBean(FileBean.class, iterator.next())));
				}
				break;
			case 1:
				MAIN.exit();
				break;
			default:
				MAIN.exit();
				break;
			}
		}

	}

	public static void file_(TcpBag accpet, TcpSocketUserBean tcpSocket) {
		DataSave.downloadList.File_(accpet);
	}

	// 发送得到用户列表请求
	public static void sendUserList() {
		TcpBag bag = new TcpBag();
		bag.setBag_id(TcpBag.GET_USER_LIST);
		SendTcpBag(bag);
		return;
	}

	/**
	 * 发送等待返回的包
	 * 
	 * @param bag
	 * @return
	 */
	private static NetBagManager manager = new NetBagManager();

	public static TcpBag waitAccpetSend(TcpBag bag) {
		ActionEvent event = new ActionEvent(bag, bag.hashCode(), bag.getHost());
		manager.add((Bag) bag, new NetBagListener(event) {
			@Override
			public void actionPerformed() {
				TcpBag bag = (TcpBag) this.event.getSource();
				synchronized (bag) {
					bag.notify();
				}
			}

		});

		synchronized (bag) {
			try {
				bag.wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		return bag;
	}

	public static void getData(TcpBag accpet) {
		manager.accpetNetBag((Bag) accpet);
	}
}
