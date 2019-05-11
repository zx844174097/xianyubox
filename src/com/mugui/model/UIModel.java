package com.mugui.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sf.json.JSONObject;

import com.mugui.Dui.DimgFile;
import com.mugui.Dui.TimeInfo;
import com.mugui.http.Bean.InfoBean;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.Bag;
import com.mugui.http.pack.TcpBag;
import com.mugui.http.pack.UdpBag;
import com.mugui.model.FishPrice.YuAllBody;
import com.mugui.tool.ImgTool;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.df.BossUpdateView;
import com.mugui.ui.df.FishPriceFrame;
import com.mugui.ui.part.WebBrowrer2;
import com.mugui.windows.Tool;

public class UIModel {
	public static void setUI(JPanel panel) {
		DataSave.StaticUI.updateUI(panel);
	}

	public static void login(TcpBag bag) {
		UserBean userBag = new UserBean((JSONObject) bag.getBody());
		if (userBag.getUser_mail().equals("") || userBag.getUser_passwd().equals("") || !Other.isMailString(userBag.getUser_mail())) {
			new TimeInfo("错误", "邮箱或者密码出错", 1500).run();
			return;
		}

		userBag.setUser_pc_name(Other.getPCUser_NAME());
		userBag.setUser_sequence(Other.getPC_sequence());
		userBag.setUser_pc_win(Other.getPC_NAME());
		bag.setBody(userBag.toJsonObject());
		DataSave.StaticUI.setVisible(false); 
		TCPModel.SendTcpBag(bag);
	}

	public static void reg(TcpBag bag) {
		UserBean userBag = new UserBean((JSONObject) bag.getBody());
		if (userBag.getUser_mail().equals("") || userBag.getUser_passwd().equals("") || !Other.isMailString(userBag.getUser_mail())
				|| userBag.getCode().equals("")) {
			new TimeInfo("错误", "邮箱或者密码出错,或为空", 1500).run();
			return;
		}
		TCPModel.SendTcpBag(bag);
	}

	public static void reg_code(TcpBag bag) {
		UserBean userBag = new UserBean((JSONObject) bag.getBody());
		if (userBag.getUser_mail().equals("") || !Other.isMailString(userBag.getUser_mail())) {
			new TimeInfo("错误", "请输入邮箱地址！");
			return;
		}
		TCPModel.SendTcpBag(bag);
	}

	public static void setlogin(String string) {
		DataSave.login.setCardLayout(string);
	}

//	public static void login(UdpBag bag) {
//		UserBean userBag = new UserBean((JSONObject) bag.getBody());
//		if (userBag.getUser_mail().equals("") || userBag.getUser_passwd().equals("") || !Other.isMailString(userBag.getUser_mail())) {
//			new TimeInfo("错误", "邮箱或者密码出错", 1500).run();
//			return;
//		}
//		userBag.setUser_pc_name(Other.getPCUser_NAME());
//		userBag.setUser_pc_win(Other.getPC_NAME());
//		userBag.setUser_sequence(Other.getPC_sequence());
//		bag.setBody(userBag.toJsonObject());
//		DataSave.StaticUI.setVisible(false);
//		TCPModel.SendTcpBag(bag);
//	}

//	public static void reg(UdpBag bag) {
//		UserBean userBag = new UserBean((JSONObject) bag.getBody());
//		if (userBag.getUser_mail().equals("") || userBag.getUser_passwd().equals("") || !Other.isMailString(userBag.getUser_mail())
//				|| userBag.getCode().equals("")) {
//			new TimeInfo("错误", "邮箱或者密码出错,或为空", 1500).run();
//			return;
//		}
//		UDPModel.SendUdpBag(bag);
//	}

//	public static void reg_code(UdpBag bag) {
//		UserBean userBag = new UserBean((JSONObject) bag.getBody());
//		if (userBag.getUser_mail().equals("") || !Other.isMailString(userBag.getUser_mail())) {
//			new TimeInfo("错误", "请输入邮箱地址！");
//			return;
//		}
//		UDPModel.SendUdpBag(bag);
//	}

	public static void sendFishPrice(LinkedList<BufferedImage> linkedList) {
		long time = System.currentTimeMillis();
		while (System.currentTimeMillis() - time < 30 * 1000 && DataSave.D_LINE_ID == -1) {
			Other.sleep(50);
		}
		if (DataSave.D_LINE_ID == -1) {
			new TimeInfo("错误", "网络连接出现问题，无法获取到鱼价线路。请开关鱼价界面，或点击鱼价界面刷新按钮", 3000).run(DataSave.bodyPanel);
			return;
		}
		FishPriceFrame.frame.reSetXianlu_id();
		// 设置某条线的鱼
		FishPrice.setLineAllYuBody(DataSave.D_LINE_ID, linkedList);
		// 鱼价窗口重置
		FishPriceFrame.initLine();
		// FishPriceFrame.initall();
		// 打包发送鱼价到服务器
		byte body[] = FishPrice.getLineAllBody(DataSave.D_LINE_ID);
		if (body == null) {
			throw new NullPointerException("打包鱼价失败！");
		}
		TcpBag bag = new TcpBag();
		bag.setBody_description(DataSave.D_LINE_ID);
		bag.setBody(body);
		bag.setBag_id(TcpBag.SEND_LINE_ALL_FISH_PRICE);
		TCPModel.SendTcpBag(bag);
	}

	private static int num = 0;

	public static void sendFishLineFeature(BufferedImage yujia) {
		DataSave.D_LINE_ID = -1;
		if (num++ > 7)
			return;
		byte b[] = ImgTool.ImgToByteArray(yujia);
		if (b == null)
			return;
		TcpBag bag = new TcpBag();
		bag.setBag_id(Bag.SEND_FISH_LINE_FEATURE);
		bag.setBody_description(DataSave.服务器);
		bag.setBody(b);
		TCPModel.SendTcpBag(bag);
		System.out.println("发送游戏特征码给服务器");
	}

	public static void getLineALLYuBody(int index) {
		TcpBag bag = new TcpBag();
		bag.setBag_id(Bag.GET_LINE_ALL_YU_BODY);
		UserBean userBean = new UserBean();
		bag.setBody_description(index);
		bag.setBody(userBean.toJsonObject());
		TCPModel.SendTcpBag(bag);
	}

	public static void getBoldLines() {
		System.out.println("请求得到黄金线路");
		TcpBag bag = new TcpBag();
		bag.setBag_id(Bag.GET_BOLD_LINE);
		bag.setBody(new UserBean().toJsonObject());
		DataSave.服务器 = "韩服";
		bag.setBody_description(DataSave.服务器);
		TCPModel.SendTcpBag(bag);
	}

	public static void sendBoldOne(BufferedImage te) {
		Entry<Integer, YuAllBody> body = FishPrice.getYuAllBodyOne(te);
		if (body != null) {
			if (System.currentTimeMillis() - body.getValue().xianluBody.bold_time < 60 * 60 * 1000) {
				return;
			}
		}
		TcpBag bag = new TcpBag();
		bag.setBag_id(Bag.SEND_BOLD_ONE);
		byte b[] = ImgTool.ImgToByteArray(te);
		if (b == null)
			return;
		bag.setBody(b);
		bag.setBody_description(DataSave.服务器);
		TCPModel.SendTcpBag(bag);
	}

	public static void sendDelBoldone(BufferedImage temp) {
		Entry<Integer, YuAllBody> body = FishPrice.getYuAllBodyOne(temp);
		if (body == null) {
			return;
		}
		body.getValue().xianluBody.bold_time = 0;
		TcpBag bag = new TcpBag();
		bag.setBag_id(Bag.SEND_DEL_BOLD_ONE);
		bag.setBody(body.getKey() + "");
		bag.setBody_description(DataSave.服务器);
		TCPModel.SendTcpBag(bag);
	}

	public static void sendNewBossUpdateTime(DimgFile boss) {
		// 判断是否可以刷新当前boss的时间
		if (BossUpdateView.isUpdateTime(boss.objectname)) {
			// 更新一个boss的刷新时间
			TcpBag bag = new TcpBag();
			bag.setBag_id(Bag.SEND_NEW_BOSS_UPDATE_TIME);
			String string = DataSave.D_LINE_ID + ":" + boss.objectname;
			bag.setBody(string);
			bag.setBody_description(DataSave.userBean);
			TCPModel.SendTcpBag(bag);
			BossUpdateView.updateBossTime(DataSave.D_LINE_ID, System.currentTimeMillis(), boss.objectname, DataSave.userBean.getUser_name());
			System.out.println("发送一个boss的刷新给服务器!!" + string);
		}
	}

	public static void sendAllBossUpdateTime() {
		if (DataSave.D_LINE_ID < 0)
			return;
		TcpBag bag = new TcpBag();
		bag.setBag_id(Bag.GET_ALL_BOSS_UPDATE_TIME);
		String string = DataSave.D_LINE_ID + "";
		bag.setBody_description(string);
		bag.setBody(DataSave.userBean.toJsonObject());
		TCPModel.SendTcpBag(bag);
		System.out.println("向服务器请求所有的boss刷新时间");
	}

	public static void sendAppInfo(String title, String message) {
		TcpBag bag = new TcpBag();
		InfoBean bean = new InfoBean(title, message);
		bag.setBag_id(TcpBag.PC_SEND_APP_INFO);
		bag.setBody(new UserBean());
		bag.setBody_description(bean.toJsonObject());
		TCPModel.SendTcpBag(bag);
	}

	public static void updateDyState(Object body, boolean b) {
		InfoBean infoBean = new InfoBean(JSONObject.fromObject(body));
		switch (infoBean.getType()) {
		case "钓鱼":
			if (b)
				DyHandle.start();
			else
				DyHandle.stop();
			break;

		case "被监视":
			DataSave.被监视 = b;
			DataSave.dy.setBjs();
			break;
		case "关机":
			CmdModel.关闭电脑();
		}
	}

	public static void saveFishPrie(LinkedList<BufferedImage> linkedList) {
		String string = "鱼_";
		int num = 1;
		for (BufferedImage image : linkedList)
			new Tool().保存图片(image, string + (num++) + ".bmp");
	}

	private static Thread cdk_thread = null;
	private static String cdk_code = "";
	private static String cdk_name = "";
	private static WebBrowrer2 webBrowrer2 = null;
	private static JFrame frame = new JFrame();

	public static void RecoveryCDK(String code, String name) {
		if (!DataSave.服务器.equals("韩服"))
			return;
		if (code.trim().equals("") || name.trim().equals("") || name.trim().equals("false")) {
			return;
		}
		code = code.trim().replaceAll(" ", "");
		code = code.replaceAll("-", "");
		// 判断这个cdk是否可以领取
		if (!isReceiveCdk(code))
			return;
		DataSave.ds.cdk_code.setText(cdk_code);
		cdk_code = code;
		cdk_name = Base64.getEncoder().encodeToString(name.trim().getBytes(Charset.forName("UTF-8")));
		cdk_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL("http://game.daum.net/coupon/popup/complete.json?gamecode=black&couponId=" + cdk_code
							+ "&ticketId=0&serverName=&characterName=" + cdk_name);

					if (webBrowrer2 == null) {
						webBrowrer2 = new WebBrowrer2();

						frame.setSize(800, 600);
						frame.add(webBrowrer2);
						frame.setTitle("CDK领取测试");
					}
					frame.setVisible(true);
					webBrowrer2.showHttp(url.toString());
					DataSave.游戏锁定 = true;
					DSHandle.AutoCheckIn();
					Other.sleep(5000);
					webBrowrer2.stopBrowser();
					frame.setVisible(false);
					frame.dispose();
					DSHandle.AutoCheckIn();
					DataSave.游戏锁定 = DataSave.ds.yxsd.isEnabled();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		cdk_thread.start();
		sendReceiveCdk(code);
	}

	private static void sendReceiveCdk(String code) {
		// 将这个cdk 发送给服务器
		// System.out.println("sendReceiveCdk:"+code);
		TcpBag bag = new TcpBag();
		bag.setBag_id(Bag.SEND_RECEIVE_CDK);
		bag.setBody(code);
		TCPModel.SendTcpBag(bag);
	}

	private static ConcurrentHashMap<String, Long> cdk_map = null;
	private static long cleanTime = 0;

	private static boolean isReceiveCdk(String cdk_code2) {
		if (cdk_map == null) {
			cdk_map = new ConcurrentHashMap<>();
			cleanTime = System.currentTimeMillis();
		}
		if (cdk_map.get(cdk_code2) != null) {
			return false;
		}
		long time = System.currentTimeMillis();
		cdk_map.put(cdk_code2, time);
		if (time - cleanTime > 30 * 60 * 1000) {
			Iterator<Entry<String, Long>> iterator = cdk_map.entrySet().iterator();
			while (iterator.hasNext()) {
				if (time - iterator.next().getValue() > 4 * 60 * 60 * 1000) {
					iterator.remove();
				}
			}
		}
		return true;
	}

	// 向服务器请求盒子辅助的UI界面
	public static void setUI(String type) {
		setUI((JPanel) DataSave.loader.loadClassToObject(type));
	}

}
