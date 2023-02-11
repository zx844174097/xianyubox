package com.mugui.ui;

import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JPanel;

import com.mugui.DataClassLoaderInterface;
import com.mugui.CD.Tank;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.UdpBag;
import com.mugui.http.tcp.TcpSocketUserBean;
import com.mugui.model.ModelManagerInterface;
import com.mugui.model.TCPModel;
import com.mugui.model.UIModel;
import com.mugui.ui.df.FishPriceFrame;
import com.mugui.ui.df.HsInitPanel;
import com.mugui.ui.part.BodyPanel;
import com.mugui.ui.part.CJ;
import com.mugui.ui.part.DS;
import com.mugui.ui.part.DownloadList;
import com.mugui.ui.part.EW;
import com.mugui.ui.part.JG;
import com.mugui.ui.part.LJ;
import com.mugui.ui.part.MY;
import com.mugui.ui.part.WebBrowrer2;
import com.mugui.ui.part.admin;
import com.mugui.ui.part.debug;
import com.mugui.ui.part.DY;
import com.mugui.ui.part.Custon;
import com.mugui.ui.part.qp;
import com.mugui.ui.part.qpList;
import com.mugui.ui.part.CJ.HS_MAP;
import com.mugui.windows.呼吸背景;

public class DataSave implements com.mugui.DataSaveInterface {
	public static final JPanel GJ_bodyPanel = null;
	public static int D_LINE_ID = -1;
	private static String 原数组[] = { 默认值.鼠标按下与释放时间, 默认值.键盘按下与释放间隔, 默认值.打开背包的前置时间, 默认值.检测鱼竿的前置时间, 默认值.鱼竿检测准确度, 默认值.更换鱼竿的前置时间, 默认值.关闭背包的前置时间, 默认值.钓鱼空格的像素数量,
			默认值.钓鱼空格的准确度, 默认值.钓鱼空格的按下时间长度, 默认值.按下收鱼空格的前置时间, 默认值.收鱼蓝色条扫描次数, 默认值.收鱼蓝色条准确度, 默认值.验证码识别前置时间, 默认值.验证码识别次数, 默认值.验证码识别准确度, 默认值.验证码输入间隔时间, 默认值.收鱼前置时间,
			默认值.收鱼蓝色条扫描像素数量, 默认值.鱼种类判断前置时间, 默认值.拾取鱼鼠标右键点击前置时间, 默认值.拾取鱼鼠标右键点击后置时间, 默认值.调整姿势的前置时间, 默认值.收鱼蓝色条红准确度, 默认值.红按下空格前置时间 };
	public static String 配置数组[] = null;
	public static String JARFILEPATH = null;
	public static mainUI StaticUI = null;
	public static admin login = null;
	public static debug deBug = null;
	public static UserBean userBean = null;
	public static TcpSocketUserBean tcpSocket = null;
	public static DY dy = null;
	public static qp qp = null;
	public static DS ds = null;
	public static JG jg = null;
	public static EW ew = null;
	public static Tank Tank = null;
	public static WebBrowrer2 WEB = null;
	public static CJ cj = null;
	public static HS_MAP hs_map = null;
	// public static SZDesktop SZDESKTOP = null;
	public static MY my = null;
	public static LJ lj = null;
	public static BodyPanel bodyPanel = null;
	public static Custon custonimg = null;
	public static qpList qpList = null;
	public static DownloadList downloadList = null;
	public static boolean 黄 = false;
	public static boolean 红 = false;
	public static boolean 绿 = false;
	public static boolean 蓝 = false;
	public static boolean 白 = false;
	public static boolean 碎片 = false;
	public static boolean 更换鱼竿 = false;
	public static boolean 丢弃鱼竿 = false;
	public static boolean 银钥匙 = false;
	public static boolean 黄鱼失误 = false;
	public static boolean 兼容 = false;
	public static boolean 被击换线 = false;
	public static boolean 工人啤酒 = false;
	public static boolean ESC = false;
	public static boolean YSSJ = false;
	public static boolean DYSW = false;
	public static boolean JGNP = true;
	public static boolean BMGJ = false;
	public static boolean 被监视 = false;
	public static boolean MYHX = false;
	public static boolean DXGJ = false;
	public static boolean 监视公会 = true;
	public static boolean MAIDDQ = false;
	public static Point maidPoint1 = new Point(392, 128);
	public static Point maidPoint2 = new Point(914, 309);
	public static Point warehousePoint = new Point(0, 0);
	public static File data_1 = null;
	public static File data_2 = null;
	// public static DYGm dyGm = null;
	public static int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	public static int SCREEN_X = 0;
	public static int SCREEN_Y = 0;
	public static 呼吸背景 x = null;
	public static String 服务器 = "未知";
	// ENTRY_CREATE：创建条目时返回的事件类型
	// ENTRY_DELETE：删除条目时返回的事件类型
	// ENTRY_MODIFY：修改条目时返回的事件类型
	// OVERFLOW：表示事件丢失或者被丢弃，不必要注册该事件类型
	static Thread thread = null;
	public static boolean 网络兼容 = false;
	public static boolean 后台支持 = false;
	public static boolean 海鸥点 = false;
	public static boolean 投资据点 = false;
	public static boolean 黑精灵冒险 = false;
	public static boolean 鼠标修正 = false;
	public static boolean 游戏锁定 = true;
	public static boolean 监视声音 = true;
	public static boolean 监听对话 = true;
	public static boolean 包满提醒 = true;
	public static boolean 图像截图 = true;
	public static boolean 被转移 = true;
	public static boolean 黄金钟快捷窗口 = true;
	public static boolean DXTX = false;
	public static boolean 丢弃采集 = false;
	public static boolean 低配模式 = false;
	public static DataClassLoaderInterface loader = null;

	public static void datainit() {
		data_1 = new File(DataSave.JARFILEPATH + "/PIC.jar");
		if (data_1.isFile()) {
			data_1.delete();
		}
		data_1 = new File(DataSave.JARFILEPATH + "/PIC2.jar");
		if (data_1.isFile()) {
			data_1.delete();
		}
		data_1 = new File(DataSave.JARFILEPATH + "/data/配置.txt");
		// data_2 = new File(DataSave.JARFILEPATH + "/data/配置2.txt");
		if (data_1.exists())
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data_1), "utf-8"));
				String s = br.readLine();
				if (s != null && !s.trim().equals("")) {
					配置数组 = s.split(",");
					s = br.readLine();
					if (s != null) {
						String ss[] = s.split(",");
						黄 = Boolean.parseBoolean(ss[0]);
						绿 = Boolean.parseBoolean(ss[1]);
						蓝 = Boolean.parseBoolean(ss[2]);
						白 = Boolean.parseBoolean(ss[3]);
						碎片 = Boolean.parseBoolean(ss[4]);
						更换鱼竿 = Boolean.parseBoolean(ss[5]);
						丢弃鱼竿 = Boolean.parseBoolean(ss[6]);
						银钥匙 = Boolean.parseBoolean(ss[7]);
						黄鱼失误 = Boolean.parseBoolean(ss[8]);
						黄金钟快捷窗口 = Boolean.parseBoolean(ss[9]);
						DataSave.ds.cdk_name.setText(ss[10]);
						被击换线 = Boolean.parseBoolean(ss[11]);
						工人啤酒 = Boolean.parseBoolean(ss[12]);
						ESC = Boolean.parseBoolean(ss[13]);
						if (ss.length > 14)
							UdpBag.USER_CODE = ss[14];
						if (ss.length > 15)
							被监视 = Boolean.parseBoolean(ss[15]);
						if (ss.length > 16)
							包满提醒 = Boolean.parseBoolean(ss[16]);
						if (ss.length > 17)
							BMGJ = Boolean.parseBoolean(ss[17]);
						if (ss.length > 18)
							DXGJ = Boolean.parseBoolean(ss[18]);
						if (ss.length > 19)
							海鸥点 = Boolean.parseBoolean(ss[19]);
						if (ss.length > 20) {
							投资据点 = Boolean.parseBoolean(ss[20]);
						}
						if (ss.length > 21) {
							黑精灵冒险 = Boolean.parseBoolean(ss[21]);
						}
						if (ss.length > 22) {
							监视公会 = Boolean.parseBoolean(ss[22]);
						}
						if (ss.length > 23) {
							鼠标修正 = Boolean.parseBoolean(ss[23]);
						}
						if (ss.length > 24) {
							游戏锁定 = Boolean.parseBoolean(ss[24]);
						}
						if (ss.length > 25) {
							监视声音 = Boolean.parseBoolean(ss[25]);
						}
						if (ss.length > 27) {
							监听对话 = Boolean.parseBoolean(ss[26]);
							DXTX = Boolean.parseBoolean(ss[27]);
						}
						if (ss.length > 28) {
							图像截图 = Boolean.parseBoolean(ss[28]);
						}
						if (ss.length > 29)
							被转移 = Boolean.parseBoolean(ss[29]);
						if (ss.length > 30) {
							低配模式 = Boolean.parseBoolean(ss[30]);
							if (bodyPanel != null)
								if (低配模式) {
									bodyPanel.setButtonEnabled("ew", false);
								} else {
									bodyPanel.setButtonEnabled("ew", true);
								}
						}
						if (ss.length > 31)
							红 = Boolean.parseBoolean(ss[31]);
					}
					s = br.readLine();
					if (s != null && !s.trim().equals("")) {
						String ss[] = s.split(",");
						qp.setQp_Color(ss[0]);
						qp.setQp_Liang(ss[1]);
						qp.setQp_LV(ss[2]);
						qp.setQp_Name(ss[3]);
						qp.setQp_Start(ss[4]);
						qp.setQp_Type(ss[5]);
						if (ss.length > 6) {
							qp.setQp_Writetime(ss[6]);
						}
						if (ss.length > 7) {
							qp.setQp_Replace(ss[7]);
						}
						if (ss.length > 8) {
							qp.setQp_BuyDelay(ss[8]);
						}
					}
				} else
					配置数组 = 原数组;
				DataSave.my.datainit(br.readLine());
				DataSave.ds.datainit(br.readLine());
				DataSave.lj.datainit(br.readLine());
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			配置数组 = 原数组;
	}

	public Object init() {
		loader = (DataClassLoaderInterface) new DataClassLoaderInterface().loadClassToObject("com.mugui.tool.DataClassLoader");

		StaticUI = new mainUI();
		login = new admin();
		downloadList = new DownloadList();
		deBug = new debug();
		// dyGm = new DYGm();
		if (dy == null)
			dy = new DY();
		qp = new qp();
		ds = new DS();
		jg = new JG();
		my = new MY();
		// WEB = new WebBrowrer2();
		cj = new CJ();
		lj = new LJ();
		Tank = new Tank();
		custonimg = new Custon();
		qpList = new qpList();
		ew = new EW();
		datainit();
		bodyPanel = new BodyPanel();
		deBug.datainit();
		custonimg.datainit();
		dy.datainit();
		// dyGm.init();
		ds.datainit();
		lj.datainit();
		FishPriceFrame.init();
		return null;
	}

	public Object start() {
		File file = new File(JARFILEPATH + "/temp");
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		File[] files = file.listFiles();
		if (files != null)
			for (File f : files) {
				f.delete();
			}
		StaticUI.setVisible(true);

		//绕过登录
//		UIModel.setUI(login);
		UIModel.setUI(HsInitPanel.main);

		// 发送一个版本检测包
		TCPModel.selectNewApp();
		return null;
	}

	public static void savedata() {
		if (data_1.exists())
			data_1.delete();
		try {
			if (!data_1.getParentFile().isDirectory()) {
				data_1.getParentFile().mkdirs();
			}
			data_1.createNewFile();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(data_1), "utf-8"));
			String str = deBug.鼠标按下与释放时间.getText().trim() + "," + deBug.键盘按下与释放间隔.getText().trim() + "," + deBug.打开背包的前置时间.getText().trim() + ","
					+ deBug.检测鱼竿的前置时间.getText().trim() + "," + deBug.鱼竿检测准确度.getText().trim() + "," + deBug.更换鱼竿的前置时间.getText().trim() + ","
					+ deBug.关闭背包的前置时间.getText().trim() + "," + deBug.钓鱼空格的像素数量.getText().trim() + "," + deBug.钓鱼空格的准确度.getText().trim() + ","
					+ deBug.钓鱼空格的按下时间长度.getText().trim() + "," + deBug.按下收鱼空格的前置时间.getText().trim() + "," + deBug.收鱼蓝色条扫描次数.getText().trim() + ","
					+ deBug.收鱼蓝色条准确度.getText().trim() + "," + deBug.验证码识别前置时间.getText().trim() + "," + deBug.验证码识别次数.getText().trim() + ","
					+ deBug.验证码识别准确度.getText().trim() + "," + deBug.验证码输入间隔时间.getText().trim() + "," + deBug.收鱼前置时间.getText().trim() + ","
					+ deBug.收鱼蓝色条扫描像素数量.getText().trim() + "," + deBug.鱼种类判断前置时间.getText().trim() + "," + deBug.拾取鱼鼠标右键点击前置时间.getText().trim() + ","
					+ deBug.拾取鱼鼠标右键点击后置时间.getText().trim() + "," + deBug.调整姿势的前置时间.getText().trim() + "," + deBug.收鱼蓝色条红准确度.getText().trim() + ","
					+ deBug.红按下空格前置时间.getText().trim();
			bw.write(str);
			bw.newLine();
			str = 黄 + ",";
			str += 绿 + ",";
			str += 蓝 + ",";
			str += 白 + ",";
			str += 碎片 + ",";
			str += 更换鱼竿 + ",";
			str += 丢弃鱼竿 + ",";
			str += 银钥匙 + ",";
			str += 黄鱼失误 + ",";
			str += 黄金钟快捷窗口 + ",";
			str += DataSave.ds.cdk_name.getText().trim() + ",";
			str += 被击换线 + ",";
			str += 工人啤酒 + ",";
			str += ESC + ",";
			str += UdpBag.USER_CODE + ",";
			str += 被监视 + ",";
			str += 包满提醒 + ",";
			str += BMGJ + ",";
			str += DXGJ + ",";
			str += 海鸥点 + ",";
			str += 投资据点 + ",";
			str += 黑精灵冒险 + ",";
			str += 监视公会 + ",";
			str += deBug.鼠标修正.isSelected() + ",";
			str += 游戏锁定 + ",";
			str += 监视声音 + ",";
			str += 监听对话 + ",";
			str += DXTX + ",";
			str += deBug.图像截图.isSelected() + ",";
			str += 被转移 + ",";
			str += deBug.低配模式.isSelected() + ",";
			str += 红 + "";
			bw.write(str);
			bw.newLine();
			str = qp.getQp_Color() + ",";
			str += qp.getQp_Liang() + ",";
			str += qp.getQp_LV() + ",";
			str += qp.getQp_Name() + ",";
			str += qp.getQp_Start() + ",";
			str += qp.getQp_Type() + ",";
			str += qp.getQp_Writertime() + ",";
			str += qp.getQp_Replace() + ",";
			str += qp.getQP_BuyDelay() + "";
			bw.write(str);
			bw.newLine();
			str = my.saveInit();
			bw.write(str);
			bw.newLine();
			str = ds.saveInit();
			bw.write(str);
			bw.newLine();
			str = lj.saveInit();
			bw.write(str);
			bw.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public Object quit() {
		savedata();
		ew.quit();
		return null;

	}

	@Override
	public UIManagerInterface getUIManager() {
		return null;
	}

	@Override
	public ModelManagerInterface getModelManager() {
		return null;
	}

}
