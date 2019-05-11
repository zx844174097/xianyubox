package com.mugui.GJ.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.eclipse.swt.internal.win32.OS;
import com.mugui.ModelInterface;
import com.mugui.Dui.DButton;
import com.mugui.Dui.DDialog;
import com.mugui.Dui.DVerticalFlowLayout;
import com.mugui.GJ.ui.CJPanel;
import com.mugui.GJ.ui.DataSave;
import com.mugui.http.Bean.DataTypeBean;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.TCPModel;
import com.mugui.tool.Other;
import com.mugui.windows.MouseListenerTool;
import com.mugui.windows.MouseListenerTool.KeyListener;
import com.mugui.windows.Tool;
import com.sun.awt.AWTUtilities;

public class CjModel implements ModelInterface {
	private boolean isTrue = false;
	private Tool shouTool = null;
	private Thread mainThread = null;
	private MainView mainView = null;

	private CJPanel cjPanel = (CJPanel) DataSave.uiManager.get("CJPanel");
	private final static String[] location_name = { "步云州", "拔仙台", "补天岭", "怀秀村", "江都城", "上淮青野", "渭川塬", "长安", "终南山" };

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
		if (mainView == null) {
			mainView = new MainView(DataSave.StaticUI, "", false);
			mainView.initData();
			shouTool = new Tool();
		}
		mainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Run();
			}

		});
	}

	private void Run() {
		mainView.setVisible(true);
		mainView.setAlwaysOnTop(true);
		Rectangle rectangle = new Rectangle(DataSave.SCREEN_X, DataSave.SCREEN_Y, DataSave.SCREEN_WIDTH, DataSave.SCREEN_HEIGHT);
		mainView.setSize(rectangle.width, rectangle.height);
		mainView.setLocation(rectangle.x, rectangle.y);
		while (isTrue) {
			mainView.setAlwaysOnTop(true);
			if (!(rectangle.height == DataSave.SCREEN_HEIGHT)) {
				rectangle.width = 500;
				rectangle.height = DataSave.SCREEN_HEIGHT;
				mainView.setSize(rectangle.width, rectangle.height);
			}
			if (!(rectangle.x == DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 500 && rectangle.y == DataSave.SCREEN_Y)) {
				rectangle.x = DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 500;
				rectangle.y = DataSave.SCREEN_Y;
				mainView.setLocation(rectangle.x, rectangle.y);
			}
			// shouTool.保存图片(panel.draw_img, "temp_" + s++ + ".bmp");
			AWTUtilities.setWindowOpaque(mainView, false);
			mainView.update();
			shouTool.delay(20);
			// panel.draw_panel.repaint();
		}
	}

	@Override
	public void start() {
		if (isrun()) {
			return;
		}
		init();
		isTrue = true;
		mainThread.start();
		// 监听f 按键
		MouseListenerTool.newInstance().addKeyListener(new KeyListener(mainView, OS.MapVirtualKey(KeyEvent.VK_F, 0)) {
			// long time = System.currentTimeMillis();

			@Override
			public void callback(Object object) {
				if (!GJTool.isTopHwndIsGJ()) {
					return;
				}
				// if (System.currentTimeMillis() - time < 10 * 1000) {
				// return;
				// }
				// time = System.currentTimeMillis();
				MainView view = (MainView) object;
				if (DataSave.USER_LOCATION == null)
					return;
				Point now_point = new Point(DataSave.USER_LOCATION.x, DataSave.USER_LOCATION.y);
				if (now_point.x == 0 && now_point.y == 0) {
					return;
				}

				MainViewTime viewTime = new MainViewTime(now_point, MainViewTime.Default);
				if (cjPanel.isRecord()) {
					if (!cjPanel.isRecordOneself()) {
						Point point = shouTool.区域找色(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 - 75, DataSave.SCREEN_HEIGHT / 2 - 75 + DataSave.SCREEN_Y,
								DataSave.SCREEN_X + DataSave.SCREEN_WIDTH / 2 + 75, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y + 75, 0.25, 200, "87E1F0");
						if (point == null) {
							return;
						}
					}
					view.addMainViewTime(new ViewTime(viewTime, view));
				} else {
					view.Update(new ViewTime(viewTime, view));
				}
			}
		});
		// 监听f12
		MouseListenerTool.newInstance().addKeyListener(new KeyListener(mainView, OS.MapVirtualKey(KeyEvent.VK_F12, 0)) {
			@Override
			public void callback(Object object) {
				System.out.println("启动");

			}
		});
	}

	@Override
	public boolean isrun() {
		return isTrue;
	}

	@Override
	public void stop() {
		isTrue = false;
		if (mainView != null) {
			mainView.saveDate();
			mainView.setVisible(false);
			MouseListenerTool.newInstance().removeKeyListeners(33);
			MouseListenerTool.newInstance().removeKeyListener(mainView, OS.MapVirtualKey(KeyEvent.VK_F12, 0));
		}
	}

	private class MainViewTime {
		private Point point = null;
		private long start_time = 0;
		public static final long Default = 60 * 60 * 1000;
		private long end_time = 0;
		private Color color = null;

		public MainViewTime(Point point, long time) {
			this.point = point;
			this.start_time = System.currentTimeMillis();
			this.end_time = start_time + time;
			color = new Color(Other.random(1, 255), Other.random(1, 255), Other.random(1, 255));
		}

		@Override
		public String toString() {
			return point.x + "|" + point.y + "|" + start_time + "|" + end_time + "|" + color.getRGB();
		}
	}

	public class ViewTime extends JPanel {
		private MainViewTime time = null;
		private MainView view = null;
		public final String code = Other.getUUID();

		public ViewTime(MainViewTime time, MainView view) {
			this.view = view;
			this.time = time;
			setBackground(Color.DARK_GRAY);
			setLayout(new BorderLayout(0, 0));
			JPanel panel = new JPanel();
			panel.setBackground(null);
			add(panel, BorderLayout.CENTER);
			panel.setLayout(new DVerticalFlowLayout());
			label = new JLabel("坐标:" + time.point.x + "|" + time.point.y);
			panel.add(label);
			label.setBackground(Color.DARK_GRAY);
			label.setForeground(Color.WHITE);
			label.setFont(new Font("微软雅黑", Font.PLAIN, 13));
			long t = time.end_time - System.currentTimeMillis();
			long fen = t / (60 * 1000);
			t %= (60 * 1000);
			long miao = t / 1000;
			label_1 = new JLabel("剩余时间:" + fen + "分 " + miao + "秒");
			panel.add(label_1);
			label_1.setForeground(Color.WHITE);
			label_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));

			JPanel panel_1 = new JPanel();
			panel_1.setBackground(null);
			FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			add(panel_1, BorderLayout.EAST);

			DButton button_1 = new DButton("删除", (Color) time.color);
			button_1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ViewTime.this.view.removeViewTime(ViewTime.this);
				}
			});
			button_1.setFont(new Font("Dialog", Font.PLAIN, 12));
			panel_1.add(button_1);

			DButton button = new DButton("重置时间", (Color) time.color);

			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					long t = ViewTime.this.time.end_time - ViewTime.this.time.start_time;
					ViewTime.this.time.start_time = System.currentTimeMillis();
					ViewTime.this.time.end_time = ViewTime.this.time.start_time + t;
				}
			});
			button.setFont(new Font("Dialog", Font.PLAIN, 12));
			panel_1.add(button);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 5461771643742510713L;
		private JLabel label;
		private JLabel label_1;

		public void resetTime() {
			label.setText("坐标：" + time.point.x + "," + time.point.y);
			long t = System.currentTimeMillis() - time.start_time;
			long fen = t / (60 * 1000);
			t %= (60 * 1000);
			long miao = t / 1000;
			label_1.setText("上次采集之后:" + fen + "分 " + miao + "秒");
		}
	}

	private class MainView extends DDialog {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MainView(JFrame d, String s, boolean b) {
			super(d, s, b);
			getContentPane().setBackground(null);
			setUndecorated(true);
			AWTUtilities.setWindowOpaque(this, false);
			getContentPane().setLayout(null);
			body = new JPanel();
			scrollPane = new JScrollPane(body);
			scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			body.setLayout(new DVerticalFlowLayout());
			scrollPane.setBounds(287 + (1762 - 1798) - 20, 310, 270, DataSave.SCREEN_HEIGHT - 310);
			scrollPane.setBackground(null);
			body.setBackground(null);
			add(scrollPane);
			全体透明(scrollPane);
		}

		private JScrollPane scrollPane = null;
		private final File save_file = new File(com.mugui.ui.DataSave.JARFILEPATH + "\\古剑奇谭\\采集\\");

		public void initData() {
			if (!save_file.isDirectory()) {
				save_file.mkdirs();
			}
			for (String name : location_name) {
				loadServerData(name);
				loadClientData(name);
			}

		}

		private void loadClientData(String name) {
			File file = new File(save_file.getPath() + "\\" + name + ".txt");
			if (!file.isFile()) {
				return;
			}
			try {
				loadData(new FileInputStream(file), name);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		private void loadData(InputStream inputStream, String name) {
			if (inputStream == null) 
				return;
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(inputStream));
				String string = null;
				while ((string = reader.readLine()) != null) {
					String data[] = string.split("\\|");
					if (data.length < 4)
						continue;
					MainViewTime viewTime = new MainViewTime(new Point(0, 0), MainViewTime.Default);
					viewTime.point = new Point(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
					viewTime.start_time = Long.parseLong(data[2]);
					viewTime.end_time = Long.parseLong(data[3]);
					if (data.length > 4) {
						viewTime.color = new Color(Integer.parseInt(data[4]));
					}
					addMainViewTime(name, new ViewTime(viewTime, this));
				}
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

		private void loadServerData(String name) {
			File file = new File(save_file.getPath() + "\\" + name + ".txt");
			loadData(DataSave.loader.getResourceAsStream("采集/" + file.getName()), name);
		}

		public void saveDate() {
			System.out.println("古剑->saveDate");
			if (!save_file.isDirectory()) {
				save_file.mkdirs();
			}
			BufferedWriter reader = null;
			try {
				Iterator<Entry<String, ConcurrentHashMap<String, ViewTime>>> iterator = hashMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, ConcurrentHashMap<String, ViewTime>> entry = iterator.next();
					reader = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(save_file.getPath() + "\\" + entry.getKey() + ".txt")),
							Charset.forName("UTF-8")));
					for (ViewTime viewTime : entry.getValue().values()) {
						reader.write(viewTime.time.toString());
						reader.newLine();
					}
					reader.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

		public void 全体透明(JComponent body) {
			Component[] components = body.getComponents();
			if (components.length < 1)
				return;
			for (Component c : components) {
				if (c instanceof JComponent && !(c instanceof DButton)) {
					c.setBackground(null);
					((JComponent) c).setOpaque(false);
					全体透明((JComponent) c);
				}
			}
		}

		private JPanel body = null;

		private ConcurrentHashMap<String, ConcurrentHashMap<String, ViewTime>> hashMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, ViewTime>>();

		public void addMainViewTime(ViewTime viewTime) {
			location_n = DataSave.USER_LOCATION_NAME;
			if (location_n == null)
				return;
			addMainViewTime(location_n, viewTime);
		}

		private void addMainViewTime(String location_name, ViewTime viewTime) {
			ConcurrentHashMap<String, ViewTime> teMap = hashMap.get(location_name);
			if (teMap == null) {
				teMap = new ConcurrentHashMap<>();
				hashMap.put(location_name, teMap);
			}
			if (isAdd(teMap, viewTime)) {
				teMap.put(viewTime.code, viewTime);
			} else {
				Update(viewTime);
			}
		}

		public void removeViewTime(ViewTime viewTime) {
			hashMap.get(location_n).remove(viewTime.code);
			body.remove(viewTime);
		}

		public void Update(ViewTime viewTime) {
			location_n = DataSave.USER_LOCATION_NAME;
			if (location_n == null)
				return;
			ConcurrentHashMap<String, ViewTime> teMap = hashMap.get(location_n);
			if (teMap == null) {
				teMap = new ConcurrentHashMap<>();
				hashMap.put(location_n, teMap);
			}

			for (ViewTime tt : teMap.values()) {
				MainViewTime time = tt.time;
				if (Math.pow(time.point.x - viewTime.time.point.x, 2) + Math.pow(time.point.y - viewTime.time.point.y, 2) < 49) {
					if (viewTime.time.start_time - time.start_time > 10 * 60 * 1000) {
						long t = time.end_time - time.start_time;
						time.start_time = viewTime.time.start_time;
						time.end_time = viewTime.time.start_time + t;
					}
				}
			}
		}

		private boolean isAdd(ConcurrentHashMap<String, ViewTime> teMap, ViewTime viewTime) {

			for (ViewTime tt : teMap.values()) {
				MainViewTime time = tt.time;
				if (Math.pow(time.point.x - viewTime.time.point.x, 2) + Math.pow(time.point.y - viewTime.time.point.y, 2) < 49) {
					// if (Math.abs(viewTime.time.start_time - time.start_time)
					// < 15 * 1000) {
					return false;
					// } else
					// return true;
				}
			}
			return true;
		}

		private String location_n = null;

		public void update() {
			String temp_location_name = DataSave.USER_LOCATION_NAME;
			if (temp_location_name == null)
				return;

			if (location_n != null && !location_n.equals(temp_location_name)) {
				body.removeAll();
			}
			location_n = temp_location_name;
			ConcurrentHashMap<String, ViewTime> map = null;
			if ((map = hashMap.get(location_n)) == null)
				return;

			Object objects[] = map.values().toArray();
			Arrays.sort(objects, new Comparator<Object>() {

				@Override
				public int compare(Object o1, Object o2) {
					ViewTime v1 = (ViewTime) o1;
					ViewTime v2 = (ViewTime) o2;
					return (int) (v1.time.start_time - v2.time.start_time);
				}
			});
			int n = 0;
			for (Object object : objects) {
				((ViewTime) object).resetTime();
				if (cjPanel.isShowList()) {
					if (body.getComponents().length < objects.length)
						body.add((Component) object, n);
				}
				n++;
			}
			if (cjPanel.isShowList()) {
				while (body.getComponents().length > objects.length) {
					body.remove(n);
				}
			} else {
				body.removeAll();
			}
		}

		@Override
		public void paint(Graphics graphics) {
			super.paint(graphics);
			Graphics2D dd = (Graphics2D) graphics;
			dd.setBackground(null);
			dd.setColor(Color.red);
			dd.setFont(new Font("微软雅黑", Font.BOLD, 13));
			if (DataSave.USER_LOCATION != null) {
				Point point = new Point(DataSave.USER_LOCATION.x, DataSave.USER_LOCATION.y);
				String name = DataSave.USER_LOCATION_NAME;
				dd.drawString(name + " 坐标：" + point.x + ":" + point.y, 30, 60);
				if (name != null) {
					ConcurrentHashMap<String, ViewTime> map = hashMap.get(name);
					if (map != null)
						for (ViewTime component : map.values()) {
							drawToPoint((component).time, dd, point);
						}
				}
			}
		}

		private Point zhong = new Point((1756 - 1459) / 2 + 1459 - 1261, (363 - 126) / 2 + 126 - 89);

		private void drawToPoint(MainViewTime time, Graphics2D dd, Point now_point) {
			if (Math.pow(now_point.x - time.point.x, 2) + Math.pow(now_point.y - time.point.y, 2) > 150000) {
				return;
			}
			Point point = time.point;
			double hu = Math.atan2(now_point.y - point.y, now_point.x - point.x);
			int len = (int) (Math.pow(Math.pow(now_point.y - point.y, 2) + Math.pow(now_point.x - point.x, 2), 0.5) * 0.75);

			// System.out.println(mainView.getX()+" "+mainView.getY());
			// 1459 126 1756 363
			Point xy = new Point((int) (zhong.x - len * Math.cos(hu)), (int) (zhong.y + len * Math.sin(hu)));
			if (xy.x < 1459 - 1261) {
				xy.x = 1459 - 1261;
			}
			if (xy.x > 1756 - 1459 + (1459 - 1261)) {
				xy.x = 1756 - 1459 + (1459 - 1261);
			}
			if (xy.y < 126 - 89) {
				xy.y = 126 - 89;
			}
			if (xy.y > 363 - 126 + (126 - 89)) {
				xy.y = 363 - 126 + (126 - 89);
			}
			// dd.draw3DRect(1459 - 1261, 126 - 89, 1756 - 1459, 363 - 126,
			// true);
			Color c = new Color(255 - time.color.getRed(), 255 - time.color.getGreen(), 255 - time.color.getBlue());

			dd.setColor(c);
			dd.drawRect(xy.x - 3, xy.y - 3, 5, 5);
			dd.setColor(time.color);
			dd.fillRect(xy.x - 2, xy.y - 2, 4, 4);
			// long t = System.currentTimeMillis() - time.start_time;
			// long fen = t / (60 * 1000);
			// t %= (60 * 1000);
			// long miao = t / 1000;
			if (xy.y <= 126 - 85) {
				dd.drawString(len + "", xy.x - 15, xy.y + 10);
			} else
				dd.drawString(len + "", xy.x - 15, xy.y - 4);
		}

	}

}
