package com.mugui.ew.ui;

import com.mugui.DataSaveInterface;
import com.mugui.ModelInterface;
import com.mugui.Dui.DButton;
import com.mugui.Dui.DFrame;
import com.mugui.Dui.DPanel;
import com.mugui.Dui.DimgFile;
import com.mugui.ew.EWUIHandel;
import com.mugui.manager.DefaultDataSaveManager;
import com.mugui.model.CmdModel;
import com.mugui.tool.Other;
import com.mugui.tool.UiTool;
import com.mugui.ui.DataSave;
import com.mugui.windows.呼吸背景;

import net.sf.json.JSONObject;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainFrame extends DFrame {
	public MainFrame() {
		super(560, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setIconImage(DimgFile.getImgFile(DataSave.JARFILEPATH + "/data/MainIcon.bmp").bufferedImage);
		setForeground(Color.DARK_GRAY);
		setBackground(Color.WHITE);
		// setTitle("咸鱼盒子");
		getContentPane().setBackground(null);
		getContentPane().setLayout(new BorderLayout(0, 0));
		final JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel, BorderLayout.NORTH);
		label = new JLabel();
		label.setText("额外功能");
		label.setForeground(Color.WHITE);
		label.addMouseListener(l);
		label.addMouseMotionListener(l);

		label.setFont(new Font("宋体", Font.BOLD, 28));
		panel.add(label);
		DButton button = new DButton((String) null, (Color) null);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				dispatchEvent(new WindowEvent(MainFrame.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		setMiniTrayIcon(getIconImage());
		PopupMenu popupMenu = new PopupMenu();
		popupMenu.add(new MenuItem("退出"));
		popupMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("退出")) {
					clearTray();
					EWUIHandel.datasave.quit();
				}
			}
		});
		setPopupMenu(popupMenu);
		miniTray();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				clearTray();
				EWUIHandel.datasave.quit();

			}
		});
		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setExtendedState(JFrame.ICONIFIED);
				setVisible(false);
				miniTray();
			}
		});

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CmdModel.打开浏览器("http://www.mugui.net.cn/");
			}
		});
		button_2.setText("赞助");
		button_2.setForeground(Color.WHITE);
		panel.add(button_2);
		button_1.setText("—");
		panel.add(button_1);
		button.setForeground(Color.WHITE);
		button.setText("退出");
		panel.add(button);
		body_view = new JPanel();
		getContentPane().add(body_view, BorderLayout.CENTER);
		body_view.setLayout(new BorderLayout(0, 0));
		body = new JPanel();
		body_view.add(body);
		body_card = new CardLayout(0, 0);
		body.setLayout(body_card);
		other_body = new JPanel();
		body.add(other_body, "other_body");
		other_body.setLayout(new BorderLayout(0, 0));
		body.setBackground(null);

		functionList = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) functionList.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		body_view.add(functionList, BorderLayout.NORTH);

		DataSave.x.addNew(panel);
		// panel.add(button_2);

		down = new JPanel();
		getContentPane().add(down, BorderLayout.SOUTH);
		down.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		down.add(panel_1);

		user_name = new JLabel("");
		panel_1.add(user_name);
		// user_name.setSize(10, 32);
		user_name.setHorizontalAlignment(SwingConstants.LEFT);
		user_name.setFont(new Font("宋体", Font.BOLD, 12));

		label_1 = new JLabel("感谢您对咸鱼盒子的支持");
		panel_1.add(label_1);
		label_1.setBackground(null);
		label_2 = new JLabel("版本号：1.0");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		down.add(label_2);
		label_2.setBackground(null);
		down.setBackground(null);
		panel_1.setBackground(null);
		uiManager = (UIManager) ((com.mugui.ew.DataSave) EWUIHandel.datasave).getUIManager();
		// 初始化界面
		InitUI();
		呼吸背景 x = new 呼吸背景(body_view, 1);
		x.addNew(down);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setVgap(2);
		flowLayout_2.setHgap(2);
		panel_2.setBackground(null);
		getContentPane().add(panel_2, BorderLayout.WEST);
		x.addNew(panel_2);
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
		flowLayout_3.setHgap(2);
		flowLayout_3.setVgap(0);
		panel_3.setBackground(null);
		x.addNew(panel_3);
		getContentPane().add(panel_3, BorderLayout.EAST);
		x.start();
	}

	/**
	 * 初始化界面
	 */
	private void InitUI() {

		JSONObject object = JSONObject.fromObject(new String(dataSave.getData("functionList.json"), Charset.forName("utf-8")));
		Iterator<Entry<String, String>> iterator = object.entrySet().iterator();
		boolean bool = true;
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			DButton button = new DButton(key, null) {

				private static final long serialVersionUID = 4883041753913052807L;
				{
					// 此管理器，并未彻底实现
					DefaultDataSaveManager dataSaveManager = null;

					addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							// 加入的可能是datasave也可能就是jpanel
							// 咸鱼盒子架构过老，无法随意更新，先这样处理。

							String name = e.getActionCommand();
							Class<?> tempClass = null;
							if (name.indexOf(".") == -1)
								tempClass = EWUIHandel.datasave.loader.loadClass(this.getClass().getPackage().getName() + "." + name);
							else {
								tempClass = EWUIHandel.datasave.loader.loadClass(name);
							}
							if (tempClass == null) {
								throw new NullPointerException("init from  " + name + " .ERROR");
							}
							System.out.println("MainFrame join->" + tempClass.getName());
							DPanel temp = null;
							if (DPanel.class.isAssignableFrom(tempClass)) {
								MainFrame.this.updateUI(temp = (DPanel) uiManager.get(tempClass.getName()));
							} else if (DataSaveInterface.class.isAssignableFrom(tempClass)) {
								if (uiManager.is(name)) {
									MainFrame.this.updateUI(temp = (DPanel) uiManager.get(name));
									return;
								}
								DataSaveInterface dataSaveInterface;
								try {
									dataSaveInterface = (DataSaveInterface) tempClass.newInstance();
									dataSaveInterface.init();
									temp = (DPanel) dataSaveInterface.start();
									uiManager.add(name, temp);
									MainFrame.this.updateUI(temp = (DPanel) uiManager.get(name));
								} catch (InstantiationException e1) {
									e1.printStackTrace();
								} catch (IllegalAccessException e1) {
									e1.printStackTrace();
								}
							} else if (ModelInterface.class.isAssignableFrom(tempClass)) {
								ModelInterface modelInterface;
								modelInterface = dataSave.getModelManager().get(tempClass.getName());
								modelInterface.init();
								modelInterface.start();
							}
						}
					});
				}
			};
			button.setActionCommand(value);
			functionList.add(button);
			if (bool) {
				now_body = uiManager.get(value);
				bool = !bool;
			}
		}

	}

	private com.mugui.ew.DataSave dataSave = (com.mugui.ew.DataSave) EWUIHandel.datasave;
	private UIManager uiManager = null;
	private JPanel down = null;
	private JPanel other_body = null;
	private CardLayout body_card = null;
	private JPanel body = null;
	private JLabel label_1 = null;
	private JLabel label = null;
	private JLabel user_name = null;
	private JLabel label_2 = null;
	private JPanel body_view = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8675985293831999466L;
	private MouseAdapter l = new MouseAdapter() {

		Point origin = new Point();

		@Override
		public void mousePressed(MouseEvent e) {
			// 鼠标按下
			origin.x = e.getX();
			origin.y = e.getY();

		}

		@Override
		public void mouseExited(MouseEvent e) {
			MainFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			MainFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point p = MainFrame.this.getLocation();
			MainFrame.this.setLocation(p.x + (e.getX() - origin.x), p.y + (e.getY() - origin.y));
			Other.sleep(20);
		}

	};

	// 初始化第一个界面
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			updateUI(now_body);
			UiTool.全体透明(body_view);
		}
	}

	private DPanel now_body = null;
	private HashMap<Object, JPanel> map = new HashMap<Object, JPanel>();
	private JPanel functionList;

	// 可用来自动更新ui
	public void updateUI(JPanel jPanel) {
		if (now_body != null) {
			now_body.quit();
		}
		if (jPanel == null)
			return;
		if (jPanel instanceof DPanel) {
			((DPanel) jPanel).init();
			if (map.get(jPanel.getClass().getName()) != null) {
				body_card.show(body, jPanel.getClass().getName());
			} else {
				map.put(jPanel.getClass().getName(), jPanel);
				body.add(jPanel, jPanel.getClass().getName());
				body_card.show(body, jPanel.getClass().getName());
			}
			now_body = (DPanel) jPanel;
		} else {
			body_card.show(body, "other_body");
			this.other_body.removeAll();
			this.other_body.add(jPanel);
			this.other_body.repaint();
			this.other_body.validate();
		}
		validate();
		repaint();

	}
}
