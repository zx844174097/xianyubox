package com.mugui.ui.df;

import com.mugui.model.FishModel;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;
import com.mugui.windows.呼吸背景;

import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.FlowLayout;

import javax.swing.JComboBox;

public class FishPriceFrame extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9173342166586191186L;
	public static int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

	public FishPriceFrame() {
		// super(301, SCREEN_HEIGHT);
		this.setSize(320, SCREEN_HEIGHT);
		this.setLocation(0, 0);

		panel = new JPanel();
		panel.setBackground(Color.BLACK);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		getContentPane().add(panel, BorderLayout.NORTH);
		lblboos = new JLabel("当前");
		panel.add(lblboos);
		lblboos.setOpaque(true);
		lblboos.setBackground(Color.BLACK);
		lblboos.setForeground(Color.WHITE);
		lblboos.setHorizontalAlignment(SwingConstants.CENTER);
		lblboos.setFont(new Font("宋体", Font.BOLD, 17));

		comboBox = new JComboBox<String>();
		comboBox.setForeground(Color.WHITE);
		comboBox.setFont(new Font("宋体", Font.BOLD, 17));
		comboBox.setBackground(Color.BLACK);
		comboBox.addItem("鱼价");
		comboBox.addItem("黄金钟");
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Init(comboBox.getSelectedIndex());
			}
		});
		panel.add(comboBox);
		// Init(0);
		setUndecorated(true);
	}

	JPanel panel = null;

	public void Init(int i) {
		Component component = getLblPanel();
		if (component != null)
			remove(component);
		switch (i) {
		case 0:
			lbl_index = i;
			if (fishPanel == null)
				fishPanel = new FishPanel();
			getContentPane().add(fishPanel, BorderLayout.CENTER);
			fishPanel.initall();
			break;
		case 1:
			lbl_index = i;
			if (boldPanel == null)
				boldPanel = new BoldPanel();
			getContentPane().add(boldPanel, BorderLayout.CENTER);
			boldPanel.initall();
			break;
		}
		validate();
		repaint();
	}

	private Component getLblPanel() {
		switch (lbl_index) {
		case 0:
			return fishPanel;
		case 1:
			return boldPanel;
		}
		return null;
	}

	FishPanel fishPanel = null;
	BoldPanel boldPanel = null;

	public static class LinThread extends Thread {
		private boolean isTrue = false;
		public int mode = 0;

		public LinThread(int mode) {
			this.mode = mode;
		}

		Point p = null;
		Dimension d = null;

		@Override
		public void run() {
			isTrue = true;
			int n = 15;
			while (isTrue) {
				if (mode == 0) {
					p = frame.getLocation();
					d = frame.getSize();
					long time = System.currentTimeMillis();
					while (mode == 0 && isTrue && System.currentTimeMillis() - time < 500) {
						Other.sleep(100);
						Point p2 = MouseInfo.getPointerInfo().getLocation();
						if (p.x <= p2.x && p.x + d.width >= p2.x && p.y <= p2.y && p.y + d.height >= p2.y) {
							mode = 2;
						}
					}
					if (!isTrue) {
						return;
					}
					if (mode != 0)
						continue;
					int num = 0;
					while (isTrue && mode == 0) {
						if (p.y - n * (1 + num) <= -(d.height - 1)) {
							p.y = -(d.height - 1);
							frame.setLocation(p.x, p.y);
							break;
						}
						p.y -= n * (++num);
						frame.setLocation(p.x, p.y);
						Other.sleep(20);
						Point p2 = MouseInfo.getPointerInfo().getLocation();
						if (p.x <= p2.x && p.x + d.width >= p2.x && p.y <= p2.y && p.y + d.height >= p2.y) {
							mode = 1;
						}
					}
					if (!isTrue)
						return;
					if (mode != 0)
						continue;
					mode = 2;
					frame.setVisible(true);
				} else if (mode == 1) {
					p = frame.getLocation();
					d = frame.getSize();
					int num = 0;
					while (isTrue) {
						if (p.y + n * (1 + num) >= 0) {
							p.y = 0;
							frame.setLocation(p.x, p.y);
							break;
						}
						p.y += n * (++num);
						frame.setLocation(p.x, p.y);
						Other.sleep(20);
					}
					if (!isTrue)
						return;
					if (mode != 1)
						continue;
					mode = 2;
				} else {
					Point p2 = frame.getLocation();
					Dimension d = frame.getSize();
					Point p = MouseInfo.getPointerInfo().getLocation();
					int lin_mode = -1;
					if (p.x >= p2.x && p.x <= p2.x + d.width && p.y >= p2.y && p.y <= p2.y + d.height) {
						lin_mode = 1;
					} else {
						lin_mode = 0;
					}
					while (isTrue) {
						p = MouseInfo.getPointerInfo().getLocation();
						if (p.x >= p2.x && p.x <= p2.x + d.width && p.y >= p2.y && p.y <= p2.y + d.height) {
							if (lin_mode == 0) {
								mode = 1;
								break;
							}
						} else {
							if (lin_mode == 1) {
								mode = 0;
								break;
							}
						}
						Other.sleep(20);
					}
				}
			}
		}

	}

	private static JLabel lblboos = null;
	private static JComboBox<String> comboBox = null;
	public static LinThread thread = null;
	public static FishPriceFrame frame = null;
	public static int lbl_index = -1;

	public static void init() {
		if (frame == null) {
			frame = new FishPriceFrame();
		}
	}

	public static void start() {
		if (BossUpdateView.x == null || !BossUpdateView.x.isAlive()) {
			BossUpdateView.x = new 呼吸背景(lblboos);
			BossUpdateView.x.start();
			BossUpdateView.x.addNew(frame.panel);
			BossUpdateView.x.addNew(comboBox);
		} else {
			BossUpdateView.x.addNew(lblboos);
			BossUpdateView.x.addNew(frame.panel);
			BossUpdateView.x.addNew(comboBox);
		}
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		switch (lbl_index) {
		case 0:
			lblboos.setText("当前" + DataSave.服务器);
			comboBox.setSelectedIndex(0);
			break;
		default:
			break;
		}
 
		thread = new LinThread(0);
		thread.start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (thread.mode != 2) {
					Other.sleep(1000);
				}
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				FishModel.start();
			}
		}).start();
	}

	public static void stop() {
		if (frame == null) {
			return;
		}
		FishModel.stop();
		if (thread != null)
			thread.isTrue = false;
		frame.setAlwaysOnTop(false);
		frame.setVisible(false);
	}

	public static void initLine() {
		switch (lbl_index) {
		case 0:
			frame.fishPanel.initLine();
			break;
		default:
			break;
		}
		frame.validate();
		frame.repaint();
	}

	public static Integer getIndex() {
		switch (lbl_index) {
		case 0:
			return frame.fishPanel.getIndex();
		default:
			break;
		}
		return -1;
	}

	public static void initBody(Integer index) {
		lblboos.setText("当前" + DataSave.服务器);
		switch (lbl_index) {
		case 0:
			frame.fishPanel.initBody(index);
			break;
		case 1:
			frame.boldPanel.initBody();
			break;
		default:
			break;
		}
		frame.validate();
		frame.repaint();
	}

	public void updateNewDope(String string) {
		switch (lbl_index) {
		case 0:
			frame.fishPanel.updateNewDope(string);
			break;
		default:
			break;
		}
		frame.validate();
		frame.repaint();
	}

	public void reSetXianlu_id() {
		switch (lbl_index) {
		case 0:
			frame.fishPanel.reSetXianlu_id();
			break;
		default:
			break;
		}
		frame.validate();
		frame.repaint();
	}

	public  void reLineTime(int body_description) {
		switch (lbl_index) {
		case 0:
			frame.fishPanel.reLineTime(body_description);
			break;
		default:
			break;
		}
		frame.validate();
		frame.repaint();
	}
}
