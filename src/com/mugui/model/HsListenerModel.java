package com.mugui.model;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import net.sf.json.JSONObject;

import com.mugui.http.Bean.CMDBean;
import com.mugui.http.Bean.JsonBean;
import com.mugui.http.Bean.KeyInfoBean;
import com.mugui.http.Bean.MouseInfoBean;
import com.mugui.http.Bean.WindowListenerBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.http.tcp.TcpSocketUserBean;
import com.mugui.tool.CMD;
import com.mugui.tool.Other;
import com.mugui.windows.Tool;
import com.mugui.windows.WindowControlThread;

public class HsListenerModel {
	private static CMD cmd = null;
	private static Thread thread = null;
	public static final int LIUNX = 1;
	public static final int WINDOWS = 2;
	private static Dimension WINDOW_SIZE = null;
	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1080;
	public static int SYSTEM_OS = -1;
	static {
		String os = System.getProperties().getProperty("os.name");
		if (!os.startsWith("win") && !os.startsWith("Win")) {
			SYSTEM_OS = LIUNX;
		} else {
			SYSTEM_OS = WINDOWS;
		}
	}

	public static void cmdInfo(TcpBag accpet, TcpSocketUserBean tcpSocket) {
		if (cmd == null || !cmd.isColose()) {
			if (cmd == null)
				cmd = new CMD();
			cmd.start();
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (cmd.isColose()) {
						Other.sleep(20);
						if (!cmd.getInfo().equals("")) {
							Other.sleep(20);
							CMDBean cmdBean = new CMDBean();
							cmdBean.setUser_text(cmd.getInfo());
							cmd.reInfo();
							TcpBag tcpBag = new TcpBag();
							tcpBag.setBody(cmdBean.toJsonObject());
							tcpBag.setBag_id(TcpBag.ADMIN_CMD_INFO);
							TCPModel.SendTcpBag(tcpBag);
						}
					}
				}
			});
			thread.start();
		}
		CMDBean cmdBean = JsonBean.newInstanceBean(CMDBean.class, accpet.getBody());
		cmd.send(cmdBean.getUser_text());
	}

	public static void getUserWindows(TcpBag bag, TcpSocketUserBean tcpSocket) {
		ListenerWindows(bag, tcpSocket);
	}

	private static void ListenerWindows(TcpBag bag, TcpSocketUserBean tcpSocket) {
		if (SYSTEM_OS == 2) {
			WindowListenerBean userBean = new WindowListenerBean().newInstanceBean(bag.getBody());
			userBean.setX(0);
			userBean.setY(0);
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			userBean.setW(dimension.width);
			userBean.setH(dimension.height);
			userBean.setMark(new Object().hashCode());
			bag.setBody_description(userBean);
			if (WindowControlThread.start(bag, tcpSocket)) {
				return;
			}
			UIModel.sendAppInfo("错误", "监视功能开启时发生了不可预计的错误，请联系开发人员QQ:844174097");
			return;
		}

		UIModel.sendAppInfo("错误", "该设备无法开启监视服务");
	}

	public static void stopUserWindow(TcpBag bag, TcpSocketUserBean tcpSocket) {
		WindowListenerBean imgBean = WindowListenerBean.newInstanceBean(WindowListenerBean.class, bag.getBody());
		WindowControlThread.close(imgBean.getMark());
	}

	private static Tool tool = null;
	private static long time = -1;

	public static void mouseInfo(TcpBag accpet, TcpSocketUserBean tcpSocket) {

		if (SYSTEM_OS == 2) {
			if (WINDOW_SIZE == null) {
				WINDOW_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
			}
			if (tool == null)
				tool = new Tool();
			MouseInfoBean bean = new MouseInfoBean((JSONObject) accpet.getBody());
			if (bean.getButton() == -1) {
				tool.mouseWheel(bean.getChick());
				return;
			}
			
			if (bean.getX() > 0 && bean.getY() > 0 && bean.getX() < WINDOW_SIZE.width && bean.getY() < WINDOW_SIZE.height)
				;
			else {
				return;
			}
			double dw = WINDOW_SIZE.width > WIDTH ? WIDTH / WINDOW_SIZE.width : 1;
			double dh = WINDOW_SIZE.height > HEIGHT ? HEIGHT / WINDOW_SIZE.height : 1;
			if (bean.getTime() > time) {
				time = bean.getTime();
				tool.mouseMove((int) (bean.getX() / dw), (int) (bean.getY() / dh));
			}
			if (bean.getChick() == 0) {
				tool.mouseRelease(bean.getButton());
			} else if (bean.getChick() == 1) {
				tool.mousePress(bean.getButton());
			}

		}

	}

	public static void keyInfo(TcpBag accpet, TcpSocketUserBean tcpSocket) {
		if (SYSTEM_OS == 2) {
			if (tool == null)
				tool = new Tool();
			KeyInfoBean bean = new KeyInfoBean((JSONObject) accpet.getBody());
			if (KeyEvent.getKeyText(bean.getKey()) == null)
				return;
			if (bean.getChick() == 0) {
				tool.keyRelease(bean.getKey());
			} else if (bean.getChick() == 1) {
				tool.keyPress(bean.getKey());
			}
		}
	}

}
