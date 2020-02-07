package com.mugui.model;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JPanel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

import com.mugui.DataSaveInterface;
import com.mugui.MAIN;
import com.mugui.ModelInterface;
import com.mugui.Dui.DInputDialog;
import com.mugui.Dui.TimeInfo;
import com.mugui.http.Bean.FishBean;
import com.mugui.http.Bean.InfoBean;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.Bag;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.FishPrice.YuAllBody;
import com.mugui.tool.ImgTool;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.df.BossUpdateView;
import com.mugui.ui.df.FishPriceFrame;
import com.mugui.ui.df.HsInitPanel;
import com.mugui.ui.part.GameListenerThread;
import com.mugui.windows.GameBackstageTool;

public class HsAllModel {
	public static void sendFishLineFeature(byte[] body, int index) {
		DataInputStream dataInputStream = null;
		try {
			dataInputStream = new DataInputStream(new ByteArrayInputStream(body));
			// Tool tool = new Tool();
			byte[] b = null;
			while (true) {
				int key = dataInputStream.readInt();
				long time = dataInputStream.readLong();
				int size = dataInputStream.readInt();
				b = new byte[size];
				dataInputStream.read(b);
				ImageInputStream stream = ImageIO.createImageInputStream(new ByteArrayInputStream(b));
				BufferedImage image = ImageIO.read(stream);
				FishPrice.addLineFeature(key, image);
				FishPrice.setLineTime(key, time);
			}
		} catch (Exception e) {
			DataSave.D_LINE_ID = index;
		} finally {
			if (dataInputStream != null) {
				try {
					dataInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private final static String[] server_names = { "韩服", "台服", "私服", "国服", "steam服", "日服", "东南亚" };

	public static void sendOneFishLineFeature(byte[] body, String string) {
		BufferedImage image = ImgTool.byteArrayToImg(body);
		String ss[] = string.split(":");
		if (ss.length < 2 || !server_names[Integer.parseInt(ss[2])].equals(DataSave.服务器)) {
			return;
		}
		FishPrice.addLineFeature(Integer.parseInt(ss[0]), image);
		FishPrice.setLineTime(Integer.parseInt(ss[0]), Long.parseLong(ss[1]));
		FishPriceFrame.initLine();
	}

	public static int getServerid() {
		for (int i = 0; i < server_names.length; ++i) {
			if (DataSave.服务器.trim().equals(server_names[i])) {
				return i;
			}
		}
		return -1;
	}

	public static void getLineAllYuBody(byte[] body, int body_description) {
		sendLineAllFishPrice(body, body_description);
		// System.out.println("new:"+body_description);
		FishPriceFrame.frame.reLineTime(body_description);
		if (FishPriceFrame.getIndex() == body_description) {
			FishPriceFrame.initBody(body_description);
			FishPriceFrame.frame.updateNewDope("鱼价刷新成功");
		}
	}

	public static boolean sendLineAllFishPrice(byte[] bb, int line_index) {

		ByteArrayInputStream inputStream = new ByteArrayInputStream(bb);
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		try {
			int len = 0;
			byte b[] = null;
			FishPrice.setLineTime(line_index, dataInputStream.readLong());
			FishPrice.clearLineAllBody(line_index);
			while (true) {
				FishBean bean = new FishBean();
				int body_index = dataInputStream.readInt();
				bean.setFish_price(dataInputStream.readInt());
				len = dataInputStream.readInt();
				b = new byte[len];
				dataInputStream.readFully(b);
				bean.setFish_name(new String(b, Charset.forName("UTF-8")).trim());
				len = dataInputStream.readInt();
				b = new byte[len];
				dataInputStream.readFully(b);
				bean.setFish_name_img(ImgTool.byteArrayToImg(b));
				len = dataInputStream.readInt();
				b = new byte[len];
				dataInputStream.readFully(b);
				bean.setFish_img(ImgTool.byteArrayToImg(b));
				FishPrice.addAllBody(line_index, bean, body_index);
			}
		} catch (IOException e) {
			if (FishPrice.allbody.get(line_index) != null) {
				return true;
			}
			return false;
		} finally {
			try {
				if (dataInputStream != null)
					dataInputStream.close();
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}

	}

	public static void login(final InfoBean infoBean) {
		final UserBean userBag = new UserBean((JSONObject) infoBean.getBody());
		DataSave.userBean = userBag;
		if (DataSave.login.isZhanghao())
			DataSave.login.saveZhanghao(userBag.getUser_mail());
		UserBean.CODE = userBag.getUser_mac();
		DataSave.StaticUI.setVisible(true);
		if (infoBean.getType().equals("success_login_no")) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					JPanel panel = DataSave.StaticUI.getUI();
					DInputDialog dialog = new DInputDialog("警告:", infoBean.getMessage(), true, true);
					dialog.openInputBox();
					UIModel.setUI(dialog);
					int i = dialog.start();
					if (i == 0) {
						String code = dialog.getInputBoxText().toString();
						TcpBag bag = new TcpBag();
						bag.setBag_id(Bag.LOGIN_SEND);
						userBag.setUser_passwd(code);
						bag.setBody(userBag.toJsonObject());
						UIModel.login(bag);
					} else {
						MAIN.exit();
					}
					UIModel.setUI(panel);
				}
			}).start();
			return;
		} else if (infoBean.getType().equals("success_login")) {
			DataSave.StaticUI.setUser_name(userBag.getUser_name());
			new TimeInfo("信息", infoBean.getMessage(), 1500).run(HsInitPanel.main);

		} else {

		}
		Other.sleep(1600);
		com.mugui.http.DataSave.StartOutTime();
	}

	public static void sendGetHsUserAllTime() {
		TcpBag bag = new TcpBag();
		UserBean userBean = new UserBean();
		for (int u_id = 0; u_id <= 8; u_id++) {
			switch (u_id) {
			case 0:
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_DYTIME);
				TCPModel.SendTcpBag(bag);
				// TCPModel.SendTcpBag(bag);
				break;
			case 1:
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_QPTIME);
				TCPModel.SendTcpBag(bag);
				break;
			case 2:
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_DSTIME);
				TCPModel.SendTcpBag(bag);
				break;
			case 3:
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_JGTIME);
				TCPModel.SendTcpBag(bag);
				break;
			case 4:
				// bag.setBody(userBean.toJsonObject());
				// bag.setBag_id(TcpBag.GET_DYTIME);
				break;
			case 5:
				// bag.setBody(userBean.toJsonObject());
				// bag.setBag_id(TcpBag.GET_DSTIME);
				break;
			case 6:
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_LJTIME);
				TCPModel.SendTcpBag(bag);
				break;
			case 7:
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_MYTIME);
				TCPModel.SendTcpBag(bag);
				break;
			case 8:
				// bag.setBody(userBean.toJsonObject());
				// bag.setBag_id(TcpBag.GET_DSTIME);
				break;
			}
		}
	}

	public static void closeAll() {
		try {
			GameBackstageTool.INSTANCE.unBindGame();
			DSHandle.stopDxgj();
			com.mugui.http.DataSave.stop();
			GameListenerThread.stop();
			closeAPP();
			((DataSaveInterface) System.getProperties().get("DataSave")).quit();
			GJUIHandle.closeAll();
			ModelInterface object = (ModelInterface) DataSave.loader.loadClassToObject("com.mugui.ew.EWUIHandel");
			if (object != null) {
				object.stop();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public static void closeAPP() {
		DyHandle.stop();
		DSHandle.stop();
		QpHandle.stop();
		JGHandle.stop();
		CjHandle.stop();
		MYHandle.stop();
		EWHandle.stop();
	}

	public static void sendOneBoldLine(byte[] s) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(s);
		DataInputStream inputStream2 = new DataInputStream(inputStream);
		try {
			int parseInt = inputStream2.readInt();
			long parseLong = inputStream2.readLong();
			YuAllBody body = FishPrice.addLineFeature(parseInt, ImgTool.byteArrayToImg(s));
			if (body == null)
				return;
			body.xianluBody.bold_time = parseLong;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream2 != null)
				try {
					inputStream2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	public static void getBoldLines(String body) {
		if (body == null || body.equals("")) {
			System.out.println("未发现任何黄金钟线路");
			return;
		}
		System.out.println("刷新所有的黄金线路成功");
		ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes());
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String s = null;
		try {
			while ((s = bufferedReader.readLine()) != null) {
				String ss[] = s.split(":");
				FishPrice.updateBoldTime(Integer.parseInt(ss[0]), Long.parseLong(ss[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();
				if (inputStream != null)
					inputStream.close();
			} catch (Exception e2) {
			}
		}
	}

	/**
	 * 更新一个boss的刷新
	 * 
	 * @param string
	 */
	public static void sendBossUpdateOne(String string) {
		System.out.println("收到一个boss的刷新" + string);
		String s[] = string.split(":");
		if (!s[s.length - 1].equals(DataSave.服务器)) {
			return;
		}
		long boss_time = Long.parseLong(s[0]);
		String boss_name = s[1];
		String user_name = s[2];
		int line_id = Integer.parseInt(s[3]);
		BossUpdateView.updateBossTime(line_id, boss_time, boss_name, user_name);
	}

	public static void getDsTime(InfoBean infoBean) {
		com.mugui.http.DataSave.dsTime = Integer.parseInt(infoBean.getMessage());
		if (com.mugui.http.DataSave.dsTime <= 0) {
			closeAll();
			new TimeInfo("信息：", "您的天数不足，无法使用任何辅助功能", 2000).run(DataSave.login);
			return;
		}

		DataSave.StaticUI.setTime(com.mugui.http.DataSave.dsTime);
	}

	public static void getAllBossUpdateTime(JSONArray fromObject) {
		if (fromObject == null)
			return;
		Iterator iterator = fromObject.iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			if (object == null || object.toString().equals("null"))
				continue;
			object = new JSONTokener(object.toString()).nextValue();
			if (object instanceof JSONArray) {
				JSONArray array = (JSONArray) object;
				@SuppressWarnings("unchecked")
				Iterator<String> iterator2 = array.iterator();
				while (iterator.hasNext()) {
					String string = iterator2.next();
					String s[] = string.split(":");
					if (!s[s.length - 1].equals(DataSave.服务器)) {
						return;
					}
					long boss_time = Long.parseLong(s[0]);
					String boss_name = s[1];
					String user_name = s[2];
					int line_id = Integer.parseInt(s[3]);
					BossUpdateView.updateBossTime(line_id, boss_time, boss_name, user_name);
				}
			} else {
				String string = (String) object;
				String s[] = string.split(":");
				if (!s[s.length - 1].equals(DataSave.服务器)) {
					return;
				}
				long boss_time = Long.parseLong(s[0]);
				String boss_name = s[1];
				String user_name = s[2];
				int line_id = Integer.parseInt(s[3]);
				BossUpdateView.updateBossTime(line_id, boss_time, boss_name, user_name);
			}
		}
	}

}
