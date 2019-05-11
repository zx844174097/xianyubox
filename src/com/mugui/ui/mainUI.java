package com.mugui.ui;

import com.mugui.Dui.DFrame;
import com.mugui.Dui.DPanel;
import com.mugui.Dui.DimgDpanel;
import com.mugui.Dui.DimgFile;
import com.mugui.model.CmdModel;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.MouseEvent;

import com.mugui.MAIN;
import com.mugui.Dui.DButton;
import com.mugui.tool.Other;
import com.mugui.windows.呼吸背景;

import java.awt.event.WindowAdapter;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.awt.GridLayout;
import java.awt.MenuItem;

import javax.swing.SwingConstants;

import java.awt.CardLayout;
import java.util.HashMap;
import java.awt.FlowLayout;

public class mainUI extends DFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8316019247919644043L;

	public mainUI() {
		super(560, 300);
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
		// setTitle("咸鱼盒子");
		label.setForeground(Color.WHITE);
		label.addMouseListener(l);
		label.addMouseMotionListener(l);

		label.setFont(new Font("宋体", Font.BOLD, 28));
		panel.add(label);
		DButton button = new DButton((String) null, (Color) null);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				dispatchEvent(new WindowEvent(mainUI.this, WindowEvent.WINDOW_CLOSING));
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
					MAIN.exit();
				}
			}
		});
		setPopupMenu(popupMenu);
		miniTray();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				clearTray();
				MAIN.exit();

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
				CmdModel.打开浏览器("http://www.mugui.net.cn/jiaoben/");
			}
		});
		button_2.setText("续费");
		button_2.setForeground(Color.WHITE);
		panel.add(button_2);
		button_1.setText("—");
		panel.add(button_1);
		button.setForeground(Color.WHITE);
		button.setText("退出");
		panel.add(button);
		DataSave.x = new 呼吸背景(panel);
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
		// panel.add(button_2);

		down = new JPanel();
		body_view.add(down, BorderLayout.SOUTH);
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

		label_1 = new JLabel("剩余使用天数："); 
		panel_1.add(label_1); 
		label_1.setBackground(null);
		label_2 = new JLabel("版本号：5.0_3.65_3.1_2.3_1.1_0.03_v6.0");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		down.add(label_2); 
		label_2.setBackground(null); 
		down.setBackground(null);  
		panel_1.setBackground(null); 
		DataSave.x.start(); 
		// this.setUndecorated(true);
		// AWTUtilities.setWindowOpaque(this, false);
		// label.setForeground(Color.black);
	}

	private JPanel down = null;
	private JPanel other_body = null;
	private CardLayout body_card = null;
	private JPanel body = null;
	private JLabel label_1 = null;
	private JLabel label = null;
	private JLabel user_name = null;
	private JLabel label_2 = null;
	private JPanel body_view = null;

	public void setUser_name(String user_name) {
		this.user_name.setText(user_name);
	}

	public void setTime(int time) {
		if (time <= 999)
			setTime("剩余使用天数：" + time);
		else {
			setTime("永久不限时");
		}
	}

	public void setTime(String time) {
		label_1.setText(time);
	}

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
			mainUI.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
 
		@Override
		public void mouseEntered(MouseEvent e) {
			mainUI.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point p = mainUI.this.getLocation();
			mainUI.this.setLocation(p.x + (e.getX() - origin.x), p.y + (e.getY() - origin.y));
			Other.sleep(20);
		}

	};
	private DPanel now_body = null;;
	private HashMap<Object, JPanel> map = new HashMap<Object, JPanel>();
	private File file = null;
	private HashMap<String, Integer> file_map = new HashMap<>();

	public void 全体透明(JComponent body) {
		try {
			if (file == null) {
				file = new File(DataSave.JARFILEPATH + "/temp/文本.txt");
			}
			Component[] components = body.getComponents();
			if (components.length < 1)
				return;

			for (Component c : components) {
				// 将文本写入

				if (c instanceof JComponent && !(c instanceof JTextField)) {
					try {
						Method method = c.getClass().getMethod("getText");
						if (method != null) {
							String object = String.valueOf(method.invoke(c));
							if (!object.trim().equals("") && !Other.isDouble(object)) {
								if (file_map.get(object.trim()) != null) {
									continue;
								}
								BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), Charset.forName("UTF-8")));
								file_map.put(object.trim(), 1);
								writer.write(object);
								writer.newLine();
								writer.newLine();
								writer.close();
							}
						}
					} catch (NoSuchMethodException e) {
					} catch (SecurityException e) {
					} catch (IllegalAccessException e) {
					} catch (IllegalArgumentException e) {
					} catch (InvocationTargetException e) {
					}
					if (!(c instanceof DimgDpanel))
						全体透明((JComponent) c);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateUI(JPanel jPanel) {
		if (now_body != null) {
			now_body.quit();
		}
		if (jPanel == null)
			return;
		// 全体透明(jPanel);
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
		DataSave.StaticUI.validate();
		DataSave.StaticUI.repaint();
		// BufferedImage image=new BufferedImage(DataSave.StaticUI.getWidth(),
		// DataSave.StaticUI.getHeight(), BufferedImage.TYPE_INT_RGB);
		// DataSave.StaticUI.getContentPane().printAll(image.createGraphics());
		// new Tool().保存图片(image, "temp.bmp");
	}

	public void updateTitle(String text) {
		int i = text.indexOf("黑色沙漠咸鱼辅助");
		if (i != -1) {
			text = text.split("黑色沙漠咸鱼辅助")[1];
		}
		label.setText("" + "咸鱼盒子" + text + "");
	}

	@Override
	public void setSize(int width, int height) {
		if (width < 600 && height < 350) {
			width = 600;
			height = 350;
		}
		super.setSize(width, height);
	}

	@Override
	public void setSize(Dimension d) {
		if (d.width < 625) {
			d.width = 625;
		}
		if (d.height < 350) {
			d.height = 350;
		}
		super.setSize(d);
	}

	public JPanel getUI() {
		return now_body;
	}

	public String getApp_id() {
		return label_2.getText().split("：")[1].trim();
	}

	public JPanel getDownPanel() {
		return down;
	}
}
