package com.mugui.ui.part;

import com.mugui.Dui.DPanel;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.mugui.MAIN;
import com.mugui.Dui.DButton;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.TCPModel;
import com.mugui.ui.DataSave;
import com.mugui.windows.GameBackstageTool;
import com.mugui.windows.呼吸背景;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class BodyPanel extends DPanel {
	public BodyPanel() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setHgap(3);
		flowLayout_1.setVgap(0);

		dybutton = new DButton((String) null, (Color) null);
		panel_1.add(dybutton);
		dybutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u_id = 0;
				init();
			}
		});
		dybutton.setText("钓鱼");

		qpbutton = new DButton((String) null, (Color) null);
		panel_1.add(qpbutton);
		qpbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u_id = 1;
				init();
			}
		});
		qpbutton.setText("抢拍");

		jgbutton = new DButton((String) null, (Color) null);
		panel_1.add(jgbutton);
		jgbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u_id = 3;
				init();
			}
		});
		jgbutton.setText("加工");

		mybutton = new DButton((String) null, (Color) null);
		panel_1.add(mybutton);
		mybutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u_id = 7;
				init();
			}
		});
		mybutton.setText("贸易");

		dsbutton = new DButton((String) null, (Color) null);
		panel_1.add(dsbutton);
		dsbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u_id = 2;
				init();
			}
		});
		dsbutton.setText("定时");

		ewbutton = new DButton((String) null, (Color) null);
		panel_1.add(ewbutton);
		ewbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u_id = 8;
				init();
			}
		});
		ewbutton.setText("额外");

		cjbutton = new DButton((String) null, (Color) null);
		panel_1.add(cjbutton);
		ljbutton = new DButton((String) null, (Color) null);
		panel_1.add(ljbutton);

		ljbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u_id = 6;
				init();
			}
		});
		ljbutton.setText("炼金");
		cjbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u_id = 5;
				init();
				qpbutton.setFocusable(false);
				dsbutton.setFocusable(false);
				jgbutton.setFocusable(false);
				dybutton.setFocusable(false);
				cjbutton.setFocusable(false);
				ljbutton.setFocusable(false);
			}
		});
		cjbutton.setText("采集");

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setHgap(0);
		flowLayout_2.setVgap(0);

		fw_label = new JLabel("未知");
		panel_2.add(fw_label);

		final JCheckBox checkBox_2 = new JCheckBox("兼容");
		final JCheckBox checkBox_1 = new JCheckBox("新兼容");
		panel_2.add(checkBox_2);
		checkBox_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.兼容 = checkBox_2.isSelected();
				checkBox_1.setSelected(false);
			}
		});
		checkBox_2.setSelected(DataSave.兼容);
		checkBox_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkBox_1.isSelected()) {
					GameBackstageTool.INSTANCE.BindGame("BlackDesert64.exe");
				} else {
					GameBackstageTool.INSTANCE.unBindGame();
				}
				checkBox_2.setSelected(false);
			}
		});
		panel_2.add(checkBox_1);

		final JCheckBox checkBox = new JCheckBox("网络兼容");
		checkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.网络兼容 = checkBox.isSelected();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(4).addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 397, Short.MAX_VALUE).addGap(6)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(4)
						.addComponent(checkBox).addGap(4)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(2).addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup().addGap(6).addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup().addGap(6).addComponent(checkBox)));
		panel.setLayout(gl_panel);
		body = new JPanel();
		add(body, BorderLayout.CENTER);
		body_card = new CardLayout(0, 0);
		body.setLayout(body_card);
		int i = 0;
		body.add(DataSave.dy, "" + i++);
		body.add(DataSave.qp, "" + i++);
		body.add(DataSave.ds, "" + i++);
		body.add(DataSave.jg, "" + i++);
		body.add(DataSave.Tank, "" + i++);
		body.add(DataSave.cj, "" + i++);
		body.add(DataSave.lj, "" + i++);
		body.add(DataSave.my, "" + i++);
		body.add(DataSave.ew, "" + i++);
		全体透明(this);
		x = new 呼吸背景(this, 1);
		x.addNew(DataSave.StaticUI.getDownPanel());
		x.start();
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
	private CardLayout body_card = null;
	static int u_id = 0;
	private static DPanel now = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7262150520131768488L;
	private Queue<Integer> queue = new LinkedBlockingQueue<Integer>();
	private String s_1 = "012301";
	private long time = 0;
	public 呼吸背景 x = null;
	private JLabel fw_label = null;
	private DButton dybutton;
	private DButton qpbutton;
	private DButton jgbutton;
	private DButton mybutton;
	private DButton dsbutton;
	private DButton ewbutton;
	private DButton cjbutton;
	private DButton ljbutton;

	public boolean setButtonUse(String type, boolean bool) {
		try {
			Field field = this.getClass().getDeclaredField(type + "button");
			if (field != null) {
				Object object = field.get(BodyPanel.this);
				if (object instanceof DButton) {
					if (bool) {
						((DButton) object).setBackground(new Color(255, 105, 180));
						((DButton) object).setButtonPressColor(new Color(255, 105, 180));
					} else {
						((DButton) object).setBackground(Color.DARK_GRAY);
						((DButton) object).setButtonPressColor(null);
					}
				}
			}
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		return false;
	}
	public boolean setButtonEnabled(String type, boolean bool) {
		try {
			Field field = this.getClass().getDeclaredField(type + "button");
			if (field != null) {
				Object object = field.get(BodyPanel.this);
				if (object instanceof DButton) {
					((DButton) object).setEnabled(bool); 
				}
			}
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		return false;
	}
	@Override
	public void init() {
		if (DataSave.userBean.getUser_name() != null && !DataSave.userBean.getUser_name().trim().equals("")) {

		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						String s = JOptionPane.showInputDialog(DataSave.StaticUI, "设置你的昵称", "提醒", JOptionPane.OK_OPTION).trim();
						if (s == null || s.trim().equals(""))
							continue;
						UserBean userBean = new UserBean();
						userBean.setUser_name(s);
						TcpBag tcpBag = new TcpBag();
						tcpBag.setBag_id(TcpBag.SET_USER_NAME);
						tcpBag.setBody(userBean.toJsonObject());
						TCPModel.SendTcpBag(tcpBag);
						break;
					}
				}
			}).start();
		}
		try {

			DataSave.bodyPanel.setButtonUse("ew", true);
			DataSave.bodyPanel.setButtonUse("cj", true);
			// if (last_id == u_id)
			// return;
			// else
			// last_id = u_id;
			if (x == null || !x.isAlive()) {
				x = new 呼吸背景(this, 1);
				x.addNew(DataSave.StaticUI.getDownPanel());
				// x.start();
			}
			fw_label.setText(DataSave.服务器);
			if (now != null)
				now.quit();
			if (System.currentTimeMillis() - time > 5000)
				queue.clear();
			time = System.currentTimeMillis();
			queue.add(u_id);
			if (queue.size() > 20)
				queue.remove();
			Iterator<Integer> i = queue.iterator();
			String s = "";
			while (i.hasNext()) {
				s += i.next();
			}
			if (s.indexOf(this.s_1) != -1) {
				u_id = 4;
				queue.clear();
			}
			TcpBag bag = new TcpBag();
			UserBean userBean = new UserBean();
			switch (u_id) {
			case 0:
				DataSave.dy.init();
				now = DataSave.dy;
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_DYTIME);
				// TCPModel.SendTcpBag(bag);
				break;
			case 1:
				DataSave.qp.init();
				now = DataSave.qp;
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_QPTIME);
				break;
			case 2:
				DataSave.ds.init();
				now = DataSave.ds;
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_DSTIME);
				break;
			case 3:
				DataSave.jg.init();
				now = DataSave.jg;
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_JGTIME);
				break;
			case 4:
				DataSave.Tank.init();
				now = DataSave.Tank;
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_DYTIME);
				break;
			case 5:
				DataSave.cj.init();
				now = DataSave.cj;
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_DSTIME);
				break;
			case 6:
				DataSave.lj.init();
				now = DataSave.lj;
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_LJTIME);
				break;
			case 7:
				DataSave.my.init();
				now = DataSave.my;
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_MYTIME);
				break;
			case 8:
				DataSave.ew.init();
				now = DataSave.ew;
				bag.setBody(userBean.toJsonObject());
				bag.setBag_id(TcpBag.GET_DSTIME);
				break;
			}
			TCPModel.SendTcpBag(bag);
			body_card.show(body, u_id + "");
		} catch (Throwable e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "检测到辅助发生严重错误，请手动重启", "错误", JOptionPane.OK_OPTION);
			MAIN.exit();
		}
	}

	public JPanel getUIOne() {
		try {
			return (JPanel) now;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void quit() {
		now.quit();
		// x.close();
	}

	@Override
	public void dataInit() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void dataSave() {
		// TODO 自动生成的方法存根
		
	}

}
