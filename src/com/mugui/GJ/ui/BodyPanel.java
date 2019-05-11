package com.mugui.GJ.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mugui.MAIN;
import com.mugui.Dui.DButton;
import com.mugui.Dui.DPanel;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.TCPModel;
import com.mugui.tool.UiTool;
import com.mugui.windows.GameBackstageTool;
import com.mugui.windows.呼吸背景;

public class BodyPanel extends DPanel {
	public BodyPanel() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		title = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) title.getLayout();
		flowLayout_1.setHgap(3);
		flowLayout_1.setVgap(0);

		panel.add(title, BorderLayout.WEST);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.EAST);

		JPanel panel_2 = new JPanel();
		panel_3.add(panel_2);
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
				com.mugui.ui.DataSave.兼容 = checkBox_2.isSelected();
				checkBox_1.setSelected(false);
			}
		});
		checkBox_2.setSelected(com.mugui.ui.DataSave.兼容);
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
		panel_3.add(checkBox);
		checkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				com.mugui.ui.DataSave.网络兼容 = checkBox.isSelected();
			}
		});
		body = new JPanel();
		add(body, BorderLayout.CENTER);
		body_card = new CardLayout(0, 0);
		body.setLayout(body_card);

	}

	private JPanel body = null;
	private JPanel title = null;
	private CardLayout body_card = null;
	private static DPanel now = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7262150520131768488L;
	private 呼吸背景 x = null;
	private JLabel fw_label = null;
	private String nowtype = null;

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
		if (com.mugui.ui.DataSave.userBean.getUser_name() != null && !com.mugui.ui.DataSave.userBean.getUser_name().trim().equals("")) {

		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						String s = JOptionPane.showInputDialog(com.mugui.ui.DataSave.StaticUI, "设置你的昵称", "提醒", JOptionPane.OK_OPTION).trim();
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
			if (x == null || !x.isAlive()) {
				x = new 呼吸背景(this, 1);
				x.addNew(com.mugui.ui.DataSave.StaticUI.getDownPanel());
			}
			fw_label.setText(com.mugui.ui.DataSave.服务器);
			if (now != null)
				now.quit();
			DataSave.uiManager.get(nowtype).init();
			now = DataSave.uiManager.get(nowtype);
			body_card.show(body, nowtype);
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
	}

	public final TitleBody[] body_string = { new TitleBody("DyPanel", "钓鱼"), new TitleBody("CJPanel", "采集"), new TitleBody("BtnPanel", "按键宏") };

	private class TitleBody {
		public String type = null;
		public String name = null;

		public TitleBody(String type, String name) {
			this.name = name;
			this.type = type;
		}
	}

	@Override
	public void dataInit() {
		for (TitleBody name : body_string) {
			DPanel panel2 = DataSave.uiManager.get(name.type);
			DButton button = new DButton((String) null, (Color) null);
			if (nowtype == null)
				nowtype = name.type;
			title.add(button);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					nowtype = ((DButton) e.getSource()).getActionCommand();
					init();
				}
			});
			button.setText(name.name);
			button.setActionCommand(name.type);
			body.add(panel2, name.type);
		}
		DataSave.uiManager.get("Custon");
		DataSave.uiManager.get("DeBug");
		UiTool.全体透明(this);
		x = new 呼吸背景(this, 1);
		x.addNew(com.mugui.ui.DataSave.StaticUI.getDownPanel());
		x.start();
	}

	@Override
	public void dataSave() {
		// TODO 自动生成的方法存根

	}

}
