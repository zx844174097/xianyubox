package com.mugui.ui.part;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.mugui.Dui.DInputDialog;
import com.mugui.Dui.DPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.GridLayout;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;

import com.mugui.Dui.DTextField;
import com.mugui.model.DSHandle;
import com.mugui.model.UIModel;
import com.mugui.tool.Other;
import com.mugui.tool.UiTool;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

import java.awt.Font;

import javax.swing.JRadioButton;
import javax.swing.event.MouseInputListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;

import com.mugui.Dui.DVerticalFlowLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;

public class DS extends DPanel {
	public DS() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		JLabel lblctrlaltctrlalt = new JLabel("定时器：用于每隔一段时间执行某个操作(开始:f10,关闭:f11)");
		panel.add(lblctrlaltctrlalt);

		JPanel panel_10 = new JPanel();
		add(panel_10, BorderLayout.NORTH);

		button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (com.mugui.http.DataSave.dsTime <= 0) {
					JOptionPane.showMessageDialog(DataSave.StaticUI, "检测到你天数不足，无法使用这个功能", "警告", JOptionPane.OK_OPTION);
					return;
				}
				DataSave.savedata();
				// for (int i = 0; i < dsThreadPanels.length; i++) {
				// dsThreadPanels[i].setNextTime();
				// }
				// if (radioButton.isSelected() || radioButton_1.isSelected())
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				DSHandle.start(null);
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});

		DButton button_10 = new DButton((String) null, (Color) null);
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "如与其他功能同时使用，请先开定时器再开其他功能，内置时间，重启辅助重置定时器时间", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_10.setText("说明");
		button_10.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_10.setBackground(Color.GRAY);
		panel_10.add(button_10);
		button.setText("开始");
		panel_10.add(button);

		button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DSHandle.stop();
				Stop();
			}

		});
		button_1.setText("关闭");
		panel_10.add(button_1);
		button_1.setEnabled(false);
		DButton button_3 = new DButton((String) null, (Color) null);
		button_3.addActionListener(new ActionListener() {
			BtnMacroPanel btnMacroPanel = null;

			public void actionPerformed(ActionEvent e) {

				DDialog dialog = new DDialog(DataSave.StaticUI, "", false);
				dialog.getContentPane().setLayout(new BorderLayout());
				JPanel panel = new JPanel();
				JLabel label = new JLabel("按键宏");
				label.setForeground(Color.WHITE);
				label.setFont(new Font("宋体", Font.BOLD, 28));
				panel.add(label);
				new WindowsDragTool(panel, dialog);
				DButton dButton = new DButton("退出", null);
				dButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Container component = ((Container) e.getSource());
						while (!(component instanceof Window)) {
							component = component.getParent();
						}
						((Window) component).dispose();
						btnMacroPanel.quit();
					}
				});
				panel.add(dButton);
				dialog.getContentPane().add(panel, BorderLayout.NORTH);
				btnMacroPanel = new BtnMacroPanel();
				btnMacroPanel.init();
				dialog.getContentPane().add(btnMacroPanel, BorderLayout.CENTER);
				dialog.setVisible(true);
				UiTool.全体透明(btnMacroPanel);
				UiTool.全体透明(panel);
				DataSave.x.addNew(panel);
				DataSave.bodyPanel.x.addNew(btnMacroPanel);
			}
		});
		button_3.setText("按键宏");
		panel_10.add(button_3);

		JPanel panel_2 = new JPanel();

		add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.EAST);

		JPanel panel_15 = new JPanel();
		FlowLayout flowLayout_9 = (FlowLayout) panel_15.getLayout();
		flowLayout_9.setAlignment(FlowLayout.LEFT);
		flowLayout_9.setVgap(0);
		flowLayout_9.setHgap(0);

		JPanel panel_20 = new JPanel();
		FlowLayout flowLayout_12 = (FlowLayout) panel_20.getLayout();
		flowLayout_12.setHgap(0);
		flowLayout_12.setAlignment(FlowLayout.LEFT);
		flowLayout_12.setVgap(0);

		JPanel panel_23 = new JPanel();

		JPanel panel_29 = new JPanel();
		FlowLayout flowLayout_20 = (FlowLayout) panel_29.getLayout();
		flowLayout_20.setHgap(0);
		flowLayout_20.setAlignment(FlowLayout.LEFT);
		flowLayout_20.setVgap(0);

		rdbtnesc = new JRadioButton("关闭阻挡界面");
		panel_29.add(rdbtnesc);
		rdbtnesc.setSelected(DataSave.ESC);

		yxsd = new JRadioButton("游戏锁定");
		panel_29.add(yxsd);
		yxsd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.游戏锁定 = yxsd.isSelected();
				DataSave.savedata();
			}
		});
		yxsd.setSelected(true);
		rdbtnesc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.ESC = !DataSave.ESC;
				DataSave.savedata();
			}
		});
		panel_23.setLayout(new DVerticalFlowLayout());

		JPanel panel_27 = new JPanel();
		FlowLayout flowLayout_15 = (FlowLayout) panel_27.getLayout();
		flowLayout_15.setVgap(0);
		flowLayout_15.setAlignment(FlowLayout.LEFT);
		panel_23.add(panel_27);

		DButton button_17 = new DButton((String) null, (Color) null);
		panel_27.add(button_17);
		button_17.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				DSHandle.start("采集水");
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});
		button_17.setText("采集水");
		button_17.setFont(new Font("Dialog", Font.BOLD, 13));

		JLabel label = new JLabel("按键：");
		panel_27.add(label);

		cj_shui = new DTextField(1);
		panel_27.add(cj_shui);
		cj_shui.setText("0");
		cj_shui.setColumns(2);

		JPanel panel_28 = new JPanel();
		FlowLayout flowLayout_19 = (FlowLayout) panel_28.getLayout();
		flowLayout_19.setVgap(0);
		flowLayout_19.setAlignment(FlowLayout.LEFT);
		panel_23.add(panel_28);

		DButton button_19 = new DButton("内胆出售", (Color) null);
		panel_28.add(button_19);
		button_19.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				DSHandle.start("公会内胆出售");
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});
		button_19.setFont(new Font("Dialog", Font.BOLD, 13));

		JLabel label_2 = new JLabel("单次:");
		panel_28.add(label_2);

		textField = new DTextField(6);
		panel_28.add(textField);
		textField.setColumns(2);
		textField.setText("500");

		DButton button_20 = new DButton((String) null, (Color) null);
		button_20.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "站在公会船厂管理NPC面前使用", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();

			}
		});
		button_20.setText("说明");
		button_20.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_20.setBackground(Color.GRAY);
		panel_28.add(button_20);

		JPanel panel_21 = new JPanel();
		FlowLayout flowLayout_14 = (FlowLayout) panel_21.getLayout();
		flowLayout_14.setVgap(0);
		panel_20.add(panel_21);

		JLabel lblNewLabel_1 = new JLabel("  投资消耗点数:");
		panel_21.add(lblNewLabel_1);

		jd_num = new DTextField(3);
		jd_num.setText("999");
		jd_num.setColumns(3);
		panel_21.add(jd_num);

		mp_bool = new JRadioButton("蓝");
		mp_bool.setEnabled(false);
		mp_bool.setVerticalAlignment(SwingConstants.TOP);
		panel_15.add(mp_bool);

		mp_ratio = new DTextField(3);
		mp_ratio.setText("50");
		mp_ratio.setColumns(3);
		panel_15.add(mp_ratio);

		JLabel label_11 = new JLabel("%");
		label_11.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		panel_15.add(label_11);

		JLabel label_13 = new JLabel("按键");
		panel_15.add(label_13);

		mp_button = new DTextField(2);
		mp_button.setText("9");
		mp_button.setColumns(2);
		panel_15.add(mp_button);
		panel_3.setLayout(new DVerticalFlowLayout());

		JPanel panel_14 = new JPanel();
		FlowLayout flowLayout_8 = (FlowLayout) panel_14.getLayout();
		flowLayout_8.setAlignment(FlowLayout.LEFT);
		flowLayout_8.setVgap(0);
		flowLayout_8.setHgap(0);

		rdbtdxgj = new JRadioButton("断线");
		panel_14.add(rdbtdxgj);
		rdbtdxgj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.DXGJ = rdbtdxgj.isSelected();
				DataSave.savedata();
			}
		});
		rdbtdxgj.setSelected(false);

		断线关机 = new JCheckBox("关机");
		panel_14.add(断线关机);

		断线提醒 = new JCheckBox("提醒");
		panel_14.add(断线提醒);
		断线关机.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				断线提醒.setSelected(!断线关机.isSelected());
				DataSave.DXTX = 断线提醒.isSelected();
			}
		});
		断线提醒.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				断线关机.setSelected(!断线提醒.isSelected());
				DataSave.DXTX = 断线提醒.isSelected();
			}
		});
		jlmx = new JRadioButton("黑精灵冒险");
		panel_14.add(jlmx);
		jlmx.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.黑精灵冒险 = jlmx.isSelected();
				DataSave.savedata();
			}
		});
		JPanel panel_16 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_16.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		grpj = new JRadioButton("工人啤酒");
		panel_16.add(grpj);
		grpj.setSelected(DataSave.工人啤酒);

		grpj_time = new DTextField(3);
		grpj_time.setText("20");
		panel_16.add(grpj_time);
		grpj_time.setColumns(3);

		JLabel label_6 = new JLabel("分");
		panel_16.add(label_6);
		grpj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.工人啤酒 = !DataSave.工人啤酒;
				DataSave.savedata();
			}
		});

		grpj_cf = new JCheckBox("全体重复");
		grpj_cf.setSelected(true);
		panel_16.add(grpj_cf);
		panel_3.add(panel_16);
		panel_3.add(panel_14);
		panel_3.add(panel_29);

		dxtx = new JRadioButton("掉血提醒");
		dxtx.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.被击换线 = dxtx.isSelected();
				DataSave.datainit();
			}
		});

		panel_29.add(dxtx);
		panel_3.add(panel_15);

		JPanel panel_17 = new JPanel();
		panel_15.add(panel_17);
		FlowLayout flowLayout_10 = (FlowLayout) panel_17.getLayout();
		flowLayout_10.setAlignment(FlowLayout.LEFT);
		flowLayout_10.setVgap(0);
		flowLayout_10.setHgap(0);

		hp_bool = new JRadioButton("红");
		panel_17.add(hp_bool);
		hp_bool.setEnabled(false);
		hp_ratio = new DTextField(3);
		hp_ratio.setText("50");
		hp_ratio.setColumns(3);
		panel_17.add(hp_ratio);

		JLabel label_12 = new JLabel("%");
		label_12.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		panel_17.add(label_12);

		JLabel label_14 = new JLabel("按键");
		panel_17.add(label_14);

		hp_button = new DTextField(2);
		hp_button.setText("0");
		hp_button.setColumns(2);
		panel_17.add(hp_button);

		JPanel panel_30 = new JPanel();
		panel_3.add(panel_30);
		FlowLayout flowLayout_21 = (FlowLayout) panel_30.getLayout();
		flowLayout_21.setHgap(0);
		flowLayout_21.setAlignment(FlowLayout.LEFT);
		flowLayout_21.setVgap(0);

		tzjd = new JRadioButton("投资据点");
		panel_30.add(tzjd);

		JLabel jLabel = new JLabel("据点名：");
		panel_30.add(jLabel);

		jdname = new DTextField(6);
		panel_30.add(jdname);
		jdname.setColumns(5);

		JPanel panel_22 = new JPanel();
		panel_30.add(panel_22);
		FlowLayout flowLayout_13 = (FlowLayout) panel_22.getLayout();
		flowLayout_13.setVgap(0);
		flowLayout_13.setHgap(0);

		JLabel label_15 = new JLabel("第");
		panel_22.add(label_15);

		jd_n = new DTextField(1);
		jd_n.setText("1");
		jd_n.setColumns(1);
		panel_22.add(jd_n);

		JLabel label_16 = new JLabel("个");
		panel_22.add(label_16);
		tzjd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.投资据点 = tzjd.isSelected();
				DataSave.savedata();
			}
		});
		panel_3.add(panel_20);

		JPanel panel_25 = new JPanel();
		panel_3.add(panel_25);
		FlowLayout flowLayout_17 = (FlowLayout) panel_25.getLayout();
		flowLayout_17.setAlignment(FlowLayout.LEFT);
		flowLayout_17.setVgap(0);

		JLabel lblCdk = new JLabel("CDK:");
		panel_25.add(lblCdk);
		lblCdk.setFont(new Font("宋体", Font.BOLD, 14));

		cdk_code = new DTextField(25);
		panel_25.add(cdk_code);
		cdk_code.setColumns(15);

		DButton button_18 = new DButton((String) null, (Color) null);
		button_18.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "填写角色名，即可自动领取cdk", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_18.setText("说明");
		button_18.setFont(new Font("宋体", Font.BOLD, 14));
		panel_25.add(button_18);

		JPanel panel_26 = new JPanel();
		panel_3.add(panel_26);
		FlowLayout flowLayout_18 = (FlowLayout) panel_26.getLayout();
		flowLayout_18.setAlignment(FlowLayout.LEFT);
		flowLayout_18.setVgap(0);

		JLabel label_1 = new JLabel("角色:");
		label_1.setFont(new Font("宋体", Font.BOLD, 12));
		panel_26.add(label_1);

		cdk_name = new DTextField(20);
		cdk_name.setColumns(15);
		panel_26.add(cdk_name);

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UIModel.RecoveryCDK(cdk_code.getText(), cdk_name.getText());
			}
		});
		panel_26.add(button_2);
		button_2.setText("领取");
		button_2.setFont(new Font("Dialog", Font.BOLD, 13));
		panel_3.add(panel_23);

		JPanel panel_24 = new JPanel();
		FlowLayout flowLayout_16 = (FlowLayout) panel_24.getLayout();
		flowLayout_16.setAlignment(FlowLayout.LEFT);
		flowLayout_16.setVgap(0);
		panel_23.add(panel_24);

		DButton button_21 = new DButton((String) null, (Color) null);
		button_21.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				DSHandle.start("自动练马技能");
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});
		button_21.setFont(new Font("Dialog", Font.BOLD, 13));
		button_21.setText("练马技能辅助提醒");
		panel_24.add(button_21);

		DButton button_22 = new DButton((String) null, (Color) null);
		button_22.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("练马技能辅助提醒功能说明：", "骑马，并且处于自动寻路阶段，然后按下键盘P键，打开属性界面，然后点击盒子功能（练马技能辅助提醒）启动该功能，当发现马等级上升时，将播放音乐提醒(需勾上钓鱼界面->声音)。", true, false);

						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_22.setText("说明");
		button_22.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_22.setBackground(Color.GRAY);
		panel_24.add(button_22);

		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_4.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		for (int i = 0; i < dsThreadPanels.length; i++) {
			dsThreadPanels[i] = new DSThreadPanel();
			panel_1.add(dsThreadPanels[i]);
		}
		JPanel panel_5 = new JPanel();
		panel_4.add(panel_5, BorderLayout.SOUTH);
		DVerticalFlowLayout dvfl_panel_5 = new DVerticalFlowLayout();
		dvfl_panel_5.setHgap(0);
		dvfl_panel_5.setVgap(0);
		panel_5.setLayout(dvfl_panel_5);

		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_6.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_5.add(panel_6);

		DButton button_4 = new DButton((String) null, (Color) null);
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				DSHandle.start("卡负重");
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});
		panel_6.add(button_4);
		button_4.setFont(new Font("Dialog", Font.BOLD, 13));
		button_4.setText("卡负重");

		JLabel label_3 = new JLabel("仓库物品：行");
		panel_6.add(label_3);

		kfz_hang = new DTextField(2);
		kfz_hang.setColumns(2);
		panel_6.add(kfz_hang);

		JLabel label_4 = new JLabel("列");
		panel_6.add(label_4);

		kfz_lie = new DTextField(2);
		kfz_lie.setColumns(2);
		panel_6.add(kfz_lie);

		JLabel label_5 = new JLabel("单次数量：");
		panel_6.add(label_5);

		kfz_number = new DTextField(4);
		kfz_number.setColumns(4);
		panel_6.add(kfz_number);

		JPanel panel_7 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_7.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		flowLayout_2.setVgap(0);
		panel_5.add(panel_7);

		DButton button_5 = new DButton((String) null, (Color) null);
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				DSHandle.start("自动上架");
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});

		button_5.setText("自动上架");
		button_5.setFont(new Font("Dialog", Font.BOLD, 13));
		panel_7.add(button_5);
		xyPanel = new XYPanel(new Dimension(20, 20));
		panel_7.add(xyPanel);

		DButton button_12 = new DButton((String) null, (Color) null);
		button_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "打开交易所登录物品界面，中间是上架，右边是背包，拖动到你需要反复上架的物品上，点击【上架】", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_12.setText("说明");
		button_12.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_12.setBackground(Color.GRAY);
		panel_7.add(button_12);

		JPanel panel_8 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_8.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		flowLayout_3.setVgap(0);
		panel_5.add(panel_8);

		DButton button_6 = new DButton((String) null, (Color) null);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				DSHandle.start("练马");
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});
		button_6.setText("练马");
		button_6.setFont(new Font("Dialog", Font.BOLD, 13));
		panel_8.add(button_6);

		lm_F = new JRadioButton("F单喷");
		lm_F.setSelected(true);
		panel_8.add(lm_F);

		lm_p = new JRadioButton("漂移");
		lm_p.setSelected(false);
		panel_8.add(lm_p);

		DButton button_13 = new DButton((String) null, (Color) null);
		button_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "人物骑在马上，卡在一个角落，在奔跑中点击【练马】", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_13.setText("说明");
		button_13.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_13.setBackground(Color.GRAY);
		panel_8.add(button_13);

		JLabel lblNewLabel_2 = new JLabel("      ");
		panel_8.add(lblNewLabel_2);

		DButton button_15 = new DButton((String) null, (Color) null);
		button_15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				DSHandle.start("挤牛奶");
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});
		button_15.setText("挤牛奶");
		button_15.setFont(new Font("Dialog", Font.BOLD, 13));
		panel_8.add(button_15);

		DButton button_16 = new DButton((String) null, (Color) null);
		button_16.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				DSHandle.start("收邮箱钱");
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});
		button_16.setText("收邮箱钱");

		button_16.setFont(new Font("Dialog", Font.BOLD, 13));
		panel_8.add(button_16);

		JPanel panel_9 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_9.getLayout();
		flowLayout_4.setVgap(0);
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		panel_5.add(panel_9);

		DButton button_7 = new DButton((String) null, (Color) null);
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				DSHandle.start("自动躺床");
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});
		button_7.setText("自动躺床");
		button_7.setFont(new Font("Dialog", Font.BOLD, 13));
		panel_9.add(button_7);

		JLabel label_7 = new JLabel("前");
		panel_9.add(label_7);

		zdtc = new DTextField(2);
		zdtc.setColumns(2);
		panel_9.add(zdtc);

		JLabel label_8 = new JLabel("名角色(建议勾选左侧关闭阻挡界面)");
		panel_9.add(label_8);

		DButton button_14 = new DButton((String) null, (Color) null);
		button_14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "需要趟床的角色放在床边，并且把需要躺床角色在角色选择界面调至12345N 位置，准备好后从第一名角色开始点【躺床】", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_14.setText("说明");
		button_14.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_14.setBackground(Color.GRAY);
		panel_9.add(button_14);

		JPanel panel_11 = new JPanel();
		panel_5.add(panel_11);

		DButton button_8 = new DButton((String) null, (Color) null);
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tool().mouseMovePressOne(DataSave.SCREEN_X + 5, DataSave.SCREEN_Y + 5, InputEvent.BUTTON1_MASK);
				DSHandle.start("扫马");
				button.setEnabled(false);
				button_1.setEnabled(true);
			}
		});
		panel_11.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		button_8.setText("扫马");
		button_8.setFont(new Font("Dialog", Font.BOLD, 13));
		panel_11.add(button_8);

		JPanel panel_12 = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) panel_12.getLayout();
		flowLayout_6.setVgap(0);
		flowLayout_6.setHgap(0);
		panel_11.add(panel_12);

		sm_dp = new JRadioButton("单喷");
		panel_12.add(sm_dp);
		sm_dp.setSelected(false);
		sm_sp = new JRadioButton("双喷");
		panel_12.add(sm_sp);
		sm_sp.setSelected(false);
		sm_dp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sm_dp.isSelected()) {
					sm_sp.setSelected(false);
				}
			}
		});
		sm_sp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sm_sp.isSelected()) {
					sm_dp.setSelected(false);
				}
			}
		});
		sm_sq = new JRadioButton("双骑");
		panel_12.add(sm_sq);
		sm_sq.setSelected(false);

		JLabel label_9 = new JLabel("价格:");
		panel_12.add(label_9);

		sm_lmoney = new DTextField(9);
		panel_12.add(sm_lmoney);
		sm_lmoney.setColumns(7);

		JLabel label_10 = new JLabel("-");
		panel_12.add(label_10);

		sm_rmoney = new DTextField(9);
		panel_12.add(sm_rmoney);
		sm_rmoney.setColumns(7);

		DButton button_11 = new DButton((String) null, (Color) null);
		button_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "马放在仓库旁边，打开仓库界面，输入目标位置几行几列，点击【卡负重】盒子切勿置于左上角", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_11.setText("说明");
		button_11.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_11.setBackground(Color.GRAY);
		panel_6.add(button_11);

		JPanel panel_13 = new JPanel();
		FlowLayout flowLayout_7 = (FlowLayout) panel_13.getLayout();
		flowLayout_7.setVgap(0);
		flowLayout_7.setHgap(0);
		panel_6.add(panel_13);
		panel_13.setPreferredSize(new Dimension(0, 0));

		JPanel panel_19 = new JPanel();
		FlowLayout flowLayout_11 = (FlowLayout) panel_19.getLayout();
		flowLayout_11.setVgap(0);
		flowLayout_11.setAlignment(FlowLayout.LEFT);
		panel_5.add(panel_19);

		JPanel panel_18 = new JPanel();
		panel_19.add(panel_18);
		FlowLayout flowLayout_5 = (FlowLayout) panel_18.getLayout();
		flowLayout_5.setVgap(0);
		flowLayout_5.setHgap(0);

		sm_db = new JRadioButton("大白");
		panel_18.add(sm_db);
		sm_db.setSelected(false);

		sm_jm = new JRadioButton("金毛");
		panel_18.add(sm_jm);
		sm_jm.setSelected(false);

		sm_hl = new JRadioButton("黑龙");
		panel_18.add(sm_hl);
		sm_hl.setSelected(false);

		DButton button_9 = new DButton((String) null, (Color) null);
		panel_19.add(button_9);
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "打开马市场，选择代数，点击【扫马】", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_9.setText("说明");
		button_9.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_9.setBackground(Color.GRAY);

	}

	private DSThreadPanel[] dsThreadPanels = new DSThreadPanel[8];
	private JRadioButton radioButton = null;
	private JRadioButton radioButton_1 = null;
	private DButton button = null;
	private DButton button_1 = null;
	private Dimension now_Dimension = new Dimension(680, 455);
	private Point now_point = null;
	private JRadioButton grpj = null;
	private JCheckBox grpj_cf = null;
	private JRadioButton rdbtnesc = null;
	private JRadioButton rdbtdxgj = null;
	private JRadioButton tzjd = null;
	private JRadioButton jlmx = null;
	private DTextField jdname = null;
	private DTextField jd_num = null;
	private DTextField jd_n = null;
	private DTextField kfz_hang = null;
	private DTextField kfz_lie = null;
	private DTextField kfz_number = null;
	private DTextField zdtc = null;
	private XYPanel xyPanel = null;
	private JRadioButton lm_F = null;
	private JRadioButton lm_p = null;
	private JRadioButton sm_dp = null;
	private JRadioButton sm_sp = null;
	private JRadioButton sm_sq = null;
	private DTextField sm_lmoney = null;
	private DTextField sm_rmoney = null;
	private JRadioButton sm_db = null;
	private JRadioButton sm_jm = null;
	private JRadioButton sm_hl = null;
	private JRadioButton mp_bool = null;
	private DTextField mp_ratio = null;
	private DTextField mp_button = null;
	private JRadioButton hp_bool = null;
	private DTextField hp_ratio = null;
	private DTextField hp_button = null;
	public JRadioButton yxsd = null;
	private JCheckBox 断线提醒 = null;
	private JCheckBox 断线关机 = null;
	private DTextField cj_shui = null;
	public DTextField cdk_name = null;
	public DTextField cdk_code = null;

	public String getCj_shui() {
		return cj_shui.getText();
	}

	public HPMPBean getHPBean() {
		String string = hp_ratio.getText().trim();
		if (!Other.isDouble(string)) {
			return null;
		}
		String string2 = hp_button.getText().trim();
		return new HPMPBean(hp_bool.isSelected(), Double.parseDouble(string), string2);
	}

	public HPMPBean getMPBean() {
		String string = mp_ratio.getText().trim();
		if (!Other.isDouble(string)) {
			return null;
		}
		String string2 = mp_button.getText().trim();
		return new HPMPBean(mp_bool.isSelected(), Double.parseDouble(string), string2);
	}

	public static class HPMPBean {
		private boolean bool;
		private double ratio;
		private String button;

		public HPMPBean(boolean bool, double ratio, String button) {
			this.bool = bool;
			this.ratio = ratio;
			this.button = button;
		}

		public boolean isBool() {
			return bool;
		}

		public void setBool(boolean bool) {
			this.bool = bool;
		}

		public double getRatio() {
			return ratio;
		}

		public void setRatio(double ratio) {
			this.ratio = ratio;
		}

		public String getButton() {
			return button;
		}

		public void setButton(String button) {
			this.button = button;
		}
	}

	public boolean isSm_dp() {
		return sm_dp.isSelected();
	}

	public boolean isSm_sp() {
		return sm_sp.isSelected();
	}

	public boolean isSm_sq() {
		return sm_sq.isSelected();
	}

	public boolean isSm_db() {
		return sm_db.isSelected();
	}

	public boolean isSm_jm() {
		return sm_jm.isSelected();
	}

	public boolean isSm_hl() {
		return sm_hl.isSelected();
	}

	public long getSm_lmoney() {
		String money = sm_lmoney.getText().trim();
		long mon = 0;
		if (Other.isInteger(money)) {
			mon = Integer.parseInt(money);
		}
		return mon;
	}

	public long getSm_rmoney() {
		String money = sm_rmoney.getText().trim();
		long mon = 9999999999l;
		if (Other.isInteger(money)) {
			mon = Integer.parseInt(money);
		}
		return mon;
	}

	public boolean isGrpj_cf() {
		return grpj_cf.isSelected();
	}

	public Rectangle getZDSJ_XYWH() {
		return xyPanel.getX_Y();
	}

	public boolean islm_F() {
		return lm_F.isSelected();
	}

	public boolean islm_P() {
		return lm_p.isSelected();
	}

	public Point getKfz_XY() {
		String lie = kfz_hang.getText();
		String hang = kfz_lie.getText();
		if (Other.isInteger(hang) && Other.isInteger(lie) && Integer.parseInt(hang) > 0 && Integer.parseInt(lie) > 0) {
			return new Point(Integer.parseInt(hang), Integer.parseInt(lie));
		}
		return new Point(-1, -1);
	}

	public String getKfz_num() {
		if (Other.isInteger(kfz_number.getText().trim())) {
			return kfz_number.getText().trim();
		}
		return 50 + "";
	}

	public boolean isTzjd() {
		return tzjd.isSelected();
	}

	public String getJd_name() {
		return jdname.getText().trim();
	}

	public String getJd_num() {
		String string = jd_num.getText().trim();
		if (Other.isInteger(string)) {
			int i = Integer.parseInt(string);
			if (i <= 0 || i >= 500) {
				i = 0;
			}
			return i + "";
		}
		return 0 + "";
	}

	public int getJd_n() {
		String s = jd_n.getText().trim();
		if (Other.isInteger(s)) {
			return Integer.parseInt(s) - 1 >= 0 ? Integer.parseInt(s) - 1 : 0;
		}
		return 0;
	}

	public void datainit() {
		grpj.setSelected(DataSave.工人啤酒);
		rdbtnesc.setSelected(DataSave.ESC);
		rdbtdxgj.setSelected(DataSave.DXGJ);
		断线提醒.setSelected(DataSave.DXTX);
		断线关机.setSelected(!断线提醒.isSelected());
		tzjd.setSelected(DataSave.投资据点);
		jlmx.setSelected(DataSave.黑精灵冒险);
		yxsd.setSelected(DataSave.游戏锁定);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3311827051456517133L;

	@Override
	public void init() {
		if (now_point != null)
			DataSave.StaticUI.setLocation(now_point);
		DataSave.StaticUI.setSize(now_Dimension);
		DataSave.StaticUI.setAlwaysOnTop(false);
		DataSave.StaticUI.updateTitle("(循环定时器)");
		// 注册一个监听事件
		JIntellitype.getInstance().registerHotKey(99, "f10");
		JIntellitype.getInstance().registerHotKey(101, "f11");
		JIntellitype.getInstance().addHotKeyListener(hotkeyListener);
		dataInit();
	}

	public int isRightOrLeft() {
		return radioButton.isSelected() ? 1 : radioButton_1.isSelected() ? 2 : 0;
	}

	private HotkeyListener hotkeyListener = new HotkeyListener() {
		@Override
		public void onHotKey(int arg0) {
			if (arg0 == 99) {
				if (com.mugui.http.DataSave.dsTime <= 0) {
					JOptionPane.showMessageDialog(DataSave.StaticUI, "检测到你天数不足，无法使用这个功能", "警告", JOptionPane.OK_OPTION);
					return;
				}
				// for (int i = 0; i < dsThreadPanels.length; i++) {
				// dsThreadPanels[i].setNextTime();
				// }
				DSHandle.start(null);
				button_1.setEnabled(true);
				button.setEnabled(false);
			} else if (arg0 == 101) {
				DSHandle.stop();
				Stop();
			}
		}
	};
	private DTextField grpj_time = null;
	private DTextField textField;
	private JRadioButton dxtx;

	public int getGhLiner_number() {
		if (Other.isInteger(textField.getText())) {
			return Integer.parseInt(textField.getText());
		}
		return 200;
	}

	public long getGrpj_time() {
		if (Other.isInteger(grpj_time.getText())) {
			return Integer.parseInt(grpj_time.getText()) * 1000 * 60;
		}
		return 0;
	}

	@Override
	public void quit() {
		JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
		now_point = DataSave.StaticUI.getLocation();
		now_Dimension = DataSave.StaticUI.getSize();
	}

	public void Stop() {
		button_1.setEnabled(false);
		button.setEnabled(true);
	}

	public DSThreadPanel[] getDSThreadPanels() {
		return dsThreadPanels;
	}

	public int getRoleNumber() {
		if (Other.isInteger(zdtc.getText().trim())) {
			return Integer.parseInt(zdtc.getText().trim());
		}
		return 0;
	}

	public String saveInit() {
		String str = "";
		for (int i = 0; i < dsThreadPanels.length; i++) {
			String string = dsThreadPanels[i].getNextTime2() + ":" + dsThreadPanels[i].getButton();
			str += string + ";";
		}
		return str;
	}

	public void datainit(String readLine) {
		if (readLine == null || readLine.trim().equals("")) {
			return;
		}
		String string[] = readLine.split(";");
		for (int i = 0; i < string.length - 1 && i < dsThreadPanels.length; i++) {
			String temp[] = string[i].split(":");
			if (temp.length != 2)
				continue;
			dsThreadPanels[i].setNextTime(temp[0].equals("0") ? "" : temp[0]);
			dsThreadPanels[i].setButton(temp[1]);
		}
	}

	public void setUse(boolean b) {
		DataSave.bodyPanel.setButtonUse("ds", b);
		DataSave.qp.setUse(b);

	}

	@Override
	public void dataInit() {
		dxtx.setSelected(DataSave.被击换线);
	}

	@Override
	public void dataSave() {

	}

	private class WindowsDragTool {
		private Window window = null;

		public WindowsDragTool(Component component, Window dialog) {
			component.addMouseListener(l);
			component.addMouseMotionListener(l);
			window = (Window) dialog;
		}

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
				window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				window.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Point p = window.getLocation();
				window.setLocation(p.x + (e.getX() - origin.x), p.y + (e.getY() - origin.y));
				Other.sleep(20);
			}

			@Override
			public void mouseMoved(MouseEvent e) {

			}
		};
	}
}
