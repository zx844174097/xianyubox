package com.mugui.ui.df;

import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.WebBrowrer2;
import com.mugui.windows.呼吸背景;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mugui.Dui.DVerticalFlowLayout;
import com.mugui.Dui.DimgFile;
import com.mugui.model.FishPrice;
import com.mugui.model.UIModel;
import com.mugui.model.FishPrice.YuAllBody;
import com.sun.awt.AWTUtilities;

import java.awt.FlowLayout;

public class BossUpdateView extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5785961822933386677L;
	// private static boos刷新时间窗口 frame = new boos刷新时间窗口();
	private JPanel body = null;

	public BossUpdateView() {
		this.setSize(270, 1900);
		this.setLocation(DataSave.SCREEN_X + 15, DataSave.SCREEN_Y + 180);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().setBackground(null);
		lblboos = new JLabel(DataSave.服务器 + "boos刷新时间表");
		lblboos.setOpaque(true);
		lblboos.setBackground(Color.BLACK);
		lblboos.setForeground(Color.WHITE);
		lblboos.addMouseListener(l);
		lblboos.addMouseMotionListener(l);
		lblboos.setHorizontalAlignment(SwingConstants.CENTER);
		lblboos.setFont(new Font("宋体", Font.BOLD, 16));
		getContentPane().add(lblboos, BorderLayout.NORTH);

		body = new JPanel();
		getContentPane().add(body, BorderLayout.CENTER);
		body.setLayout(new DVerticalFlowLayout());

		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setBackground(null);
		panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel.add(panel_1);
		panel_1.setPreferredSize(new Dimension(1, 1));
		setUndecorated(true);
		AWTUtilities.setWindowOpaque(this, false);
		body.setBackground(null);
		panel_1.setBackground(null);
	}

	private JPanel panel_1 = null;
	private JLabel lblboos = null;
	private MouseInputListener l = new MouseInputListener() {

		Point origin = new Point();

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// 鼠标按下
			origin.x = e.getX();
			origin.y = e.getY();

		}

		@Override
		public void mouseExited(MouseEvent e) {
			BossUpdateView.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			BossUpdateView.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point p = BossUpdateView.this.getLocation();
			BossUpdateView.this.setLocation(p.x + (e.getX() - origin.x), p.y + (e.getY() - origin.y));
			Other.sleep(20);
		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}
	};
	public static 呼吸背景 x = DataSave.x;

	public static void start() {
		if (DataSave.服务器.equals("台服") && webBrowrer2 == null) {
			webBrowrer2 = new WebBrowrer2();
		}
		if (x == null || !x.isAlive()) {
			x = new 呼吸背景(frame.lblboos);
			x.start();
		} else {
			x.addNew(frame.lblboos);
		}
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		if (boosThread == null || !boosThread.isAlive()) {
			if (DataSave.服务器.equals("台服")) {
				frame.panel_1.add(webBrowrer2);
				webBrowrer2.showHttp("http://bd.youxidudu.com/");
			}
			boosThread = new boos刷新Thread();
			boosThread.start();
		}
	}

	static JLabel world_label = null;
	static JLabel lins_label = null;

	private static void updateUI() {
		if (frame.isVisible()) {
			frame.body.removeAll();
			if (DataSave.服务器.equals("台服")) {
				Iterator<BossPanel> iterator = BD_view_list.iterator();
				while (iterator.hasNext()) {
					frame.body.add(iterator.next());
				}
				frame.body.add(new JLabel("以上数据由黑沙宝典提供"));
			}
			if (world_label == null) {
				world_label = new JLabel("世界boss刷新时间:");
				world_label.setOpaque(true);
				x.addNew(world_label);
			}
			frame.body.add(world_label);
			for (String boss : world_boss) {
				Object object = boss_data.get(boss);
				if (object == null)
					continue;
				DimgFile file = (DimgFile) object;
				String last_time = "上次:" + new Date(Long.parseLong(file.objectPRI));
				String next_time = "预计:" + new Date(Long.parseLong(file.objectPRI) + 8 * 60 * 60 * 1000);
				BossPanel panel = new BossPanel((String) null, file.objectname, last_time, next_time);
				frame.body.add(panel);
			}
			if (lins_label == null) {
				lins_label = new JLabel("线路boss刷新时间:");
				lins_label.setOpaque(true);
				x.addNew(lins_label);
			}
			frame.body.add(lins_label);
			Iterator<Object> iterator = boss_data.values().iterator();
			while (iterator.hasNext()) {
				Object temp = iterator.next();
				if (temp instanceof DimgFile) {

				} else {
					@SuppressWarnings("unchecked")
					Iterator<DimgFile> dIterator = ((ConcurrentHashMap<Integer, DimgFile>) temp).values().iterator();
					boolean bool = false;
					while (dIterator.hasNext()) {
						DimgFile dimgFile = dIterator.next();
						if (!bool) {
							frame.body.add(new JLabel("boss名字:" + dimgFile.objectname));
							bool = true;
						}
						String last_time = "上次:" + new Date(Long.parseLong(dimgFile.objectPRI)).toLocaleString();
						String next_time = "预计:" + new Date(Long.parseLong(dimgFile.objectPRI) + 8 * 60 * 60 * 1000).toLocaleString();
						BossPanel panel = new BossPanel(dimgFile.bufferedImage, null, last_time, next_time);
						frame.body.add(panel);
					}
				}
			}
			frame.pack();
			frame.body.repaint();
			frame.body.validate();
			frame.repaint();
			frame.validate();
		}
	}

	public static void stop() {
		// if (x != null)
		// x.close();
		boosThread.close();
		frame.setAlwaysOnTop(false);
		frame.setVisible(false);
		frame.dispose();
	}

	private static boos刷新Thread boosThread = null;

	private static WebBrowrer2 webBrowrer2 = null;

	static BossUpdateView frame = new BossUpdateView();
	private static ConcurrentLinkedDeque<BossPanel> BD_view_list = new ConcurrentLinkedDeque<>();

	static class boos刷新Thread extends Thread {
		private boolean isTrue = false;

		@Override
		public void run() {
			Other.sleep(1000);
			isTrue = true;
			while (isTrue) {
				if (webBrowrer2 != null)
					webBrowrer2.refresh();
				BD_view_list.clear();
				long time = System.currentTimeMillis();
				if (webBrowrer2 != null) {
					while (isTrue) {
						String html = webBrowrer2.getHttp();
						Document document = Jsoup.parse(html);
						Elements element = document.getElementsByClass("boss_tools_h_box");
						if (element.toString().trim().equals("")) {
							if (System.currentTimeMillis() - time > 10 * 1000) {
								webBrowrer2.showHttp("http://bd.youxidudu.com/");
								webBrowrer2.refresh();
								time = System.currentTimeMillis();
							}
							Other.sleep(50);
							continue;
						}
						int size = element.size();
						boolean is = false;
						for (int j = 0; j < size; j++) {
							Element element2 = element.get(j);
							// System.out.println(element2);
							String boss_name = element2.getElementsByClass("boss_name").text();
							String boss_headpic = element2.getElementsByClass("boss_headpic").attr("src");
							String boos_pre_time = element2.getElementsByClass("boos_pre_time").text();
							if (boos_pre_time.trim().equals("上次：00-00 00:00")) {
								break;
							}
							String boos_next_time = element2.getElementsByClass("boos_next_time").text();
							boos_next_time = boos_next_time.replaceFirst("預計", "预计");
							boos_next_time = boos_next_time.replaceFirst(" ~ ", "至");
							BD_view_list.add(new BossPanel(boss_headpic, boss_name, boos_pre_time, boos_next_time));
							is = true;
						}

						if (is) {
							updateUI();
							break;
						} else {
							Other.sleep(200);
						}
					}
				}
				// 向服务器请求这个服务器的所有的boss刷新时间
				UIModel.sendAllBossUpdateTime();

				time = System.currentTimeMillis();
				int y = DataSave.SCREEN_Y;
				int x = DataSave.SCREEN_X;
				while (System.currentTimeMillis() - time < 5 * 60 * 1000) {
					Other.sleep(25);
					if (x != DataSave.SCREEN_X || y != DataSave.SCREEN_Y)
						frame.setLocation(DataSave.SCREEN_X + 15, DataSave.SCREEN_Y + 180);
					if (!isTrue) {
						return;
					}
				}
			}
		}

		public void close() {
			isTrue = false;
		}
	}

	private static String[] world_boss = { "怒贝尔", "科扎卡", "库图姆", "卡兰达" };
	private static ConcurrentHashMap<String, Object> boss_data = new ConcurrentHashMap<>();

	public static boolean isUpdateTime(String objectname) {
		Object temp = boss_data.get(objectname);
		if (temp == null)
			return true;
		DimgFile dimgFile = null;
		if (temp instanceof DimgFile) {
			dimgFile = (DimgFile) temp;
		} else {
			@SuppressWarnings("unchecked")
			ConcurrentHashMap<Integer, DimgFile> concurrentHashMap = (ConcurrentHashMap<Integer, DimgFile>) temp;
			dimgFile = concurrentHashMap.get(DataSave.D_LINE_ID);
		}
		if (dimgFile == null)
			return true;
		long time = Long.parseLong(dimgFile.objectPRI);
		if (System.currentTimeMillis() - time > 8 * 60 * 60 * 1000) {
			return true;
		}
		return false;
	}

	/**
	 * 更新一个boss的刷新
	 * 
	 * @param line_id
	 * @param boss_time
	 * @param boss_name
	 * @param user_name
	 */
	public static void updateBossTime(int line_id, long boss_time, String boss_name, String user_name) {
		Object temp = boss_data.get(boss_name);
		if (temp == null) {
			temp = initBossOne(boss_name);
			boss_data.put(boss_name, temp);
		}
		DimgFile dimgFile = null;
		if (temp instanceof DimgFile) {
			dimgFile = (DimgFile) temp;
		} else {
			@SuppressWarnings("unchecked")
			ConcurrentHashMap<Integer, DimgFile> concurrentHashMap = (ConcurrentHashMap<Integer, DimgFile>) temp;
			dimgFile = concurrentHashMap.get(line_id);
			if (dimgFile == null) {
				dimgFile = new DimgFile();
				concurrentHashMap.put(line_id, dimgFile);
			}
		}
		dimgFile.objectname = boss_name;
		dimgFile.objectPRI = boss_time + "";
		dimgFile.objectcolor = user_name;
		dimgFile.objectlevel = line_id + "";
		YuAllBody body = FishPrice.allbody.get(line_id);
		if (body == null) {
			DataSave.D_LINE_ID = -1;
			long time = System.currentTimeMillis();
			while (System.currentTimeMillis() - time < 15000) {
				Other.sleep(1000);
				if (DataSave.D_LINE_ID != -1) {
					break;
				}
			}
			body = FishPrice.allbody.get(line_id);
			if(body==null) return;
		}
		if (body.xianluBody == null || body.xianluBody.yuan == null) {
		} else
			dimgFile.bufferedImage = body.xianluBody.yuan;
		updateUI();
	}

	private static Object initBossOne(String boss_name) {
		for (String name : world_boss) {
			if (name.equals(boss_name)) {
				return new DimgFile();
			}
		}
		return new ConcurrentHashMap<Integer, DimgFile>();
	}
}
