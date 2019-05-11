package com.mugui.ui.part;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.mugui.Dui.DInputDialog;
import com.mugui.Dui.DPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;

import com.mugui.Dui.DButton;
import com.mugui.model.DyHandle;
import com.mugui.model.UIModel;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

import java.awt.Color;

import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

import com.mugui.Dui.DTextField;

import java.awt.Font;
import javax.swing.JButton;

public class DY extends DPanel {
	public DY() {

		JPanel panel_10 = new JPanel();

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_8 = new JPanel();
		panel_8.setPreferredSize(new Dimension(100, 600));
		panel_3.add(panel_8);
		FlowLayout fl_panel_8 = new FlowLayout(FlowLayout.LEFT, 0, 0);
		panel_8.setLayout(fl_panel_8);

		huang = new JRadioButton("黄");
		huang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.黄 = !DataSave.黄;
				DataSave.savedata();
			}
		});

		JButton btnNewButton = new JButton("");
		btnNewButton.setPreferredSize(new Dimension(0, 0));
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 5));
		panel_8.add(btnNewButton);
		panel_8.add(huang);

		red = new JRadioButton("红");
		red.setSelected(DataSave.红);
		panel_8.add(red);

		lan = new JRadioButton("蓝");
		lan.setSelected(DataSave.蓝);
		panel_8.add(lan);

		lv = new JRadioButton("绿");
		lv.setSelected(DataSave.绿);
		panel_8.add(lv);

		bai = new JRadioButton("海");
		bai.setSelected(DataSave.白);
		panel_8.add(bai);

		yaosi = new JRadioButton("钥匙");
		panel_8.add(yaosi);
		yaosi.setSelected(DataSave.银钥匙);

		suipian = new JRadioButton("碎片");
		panel_8.add(suipian);
		suipian.setSelected(DataSave.碎片);

		DButton button_10 = new DButton((String) null, (Color) null);
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "如果选择碎片和钥匙，大于或等于7按键直接判定失败。", true, false);
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
		panel_8.add(button_10);

		suipian.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.碎片 = !DataSave.碎片;
				DataSave.savedata();

			}
		});
		yaosi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.银钥匙 = !DataSave.银钥匙;
				DataSave.savedata();
			}
		});
		bai.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.白 = !DataSave.白;
				DataSave.savedata();
			}
		});
		lv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.绿 = !DataSave.绿;
				DataSave.savedata();

			}
		});
		lan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.蓝 = !DataSave.蓝;
				DataSave.savedata();
			}
		});

		JPanel panel_7 = new JPanel();
		panel_3.add(panel_7);
		panel_7.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		JLabel label_4 = new JLabel("钓鱼延后:");
		panel_7.add(label_4);

		yh_time = new DTextField(6);
		panel_7.add(yh_time);
		yh_time.setColumns(4);

		JLabel label_5 = new JLabel("分");
		panel_7.add(label_5);

		DButton button_9 = new DButton((String) null, (Color) null);
		panel_7.add(button_9);
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "只捡取碎片，钥匙，将在这个时间段之后才会捡取其他鱼", true, false);
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

		JRadioButton radioButton_7 = new JRadioButton("延时随机");
		panel_7.add(radioButton_7);

		DButton button_4 = new DButton((String) null, (Color) null);
		panel_7.add(button_4);
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "钓鱼各个阶段将会出现不同情况的延时随机", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_4.setText("说明");
		button_4.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_4.setBackground(Color.GRAY);
		radioButton_7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.YSSJ = !DataSave.YSSJ;
			}
		});

		JPanel panel_6 = new JPanel();
		panel_3.add(panel_6);
		panel_6.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_15 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_15.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		flowLayout_4.setVgap(0);
		flowLayout_4.setHgap(0);
		panel_6.add(panel_15);

		huangan = new JRadioButton("自动更换鱼竿");
		panel_15.add(huangan);
		huangan.setSelected(DataSave.更换鱼竿);

		DButton button_6 = new DButton((String) null, (Color) null);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "伽马值拉最左，对比的最右，小地图为默认大小，旁边耐久度提示处为全黑，方可背包不反复打开", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_6.setText("说明");
		button_6.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_6.setBackground(Color.GRAY);
		panel_15.add(button_6);
		huangan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.更换鱼竿 = !DataSave.更换鱼竿;
				DataSave.savedata();
			}
		});

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_6.add(panel_1);

		diuqi = new JRadioButton("丢弃（不建议）");
		panel_1.add(diuqi);
		diuqi.setSelected(DataSave.丢弃鱼竿);

		DButton button_12 = new DButton((String) null, (Color) null);
		button_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "丢弃背包中无用的物品（坏鱼竿，未勾选的鱼种类，和非自定义拾取物品）", true, false);
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
		panel_1.add(button_12);
		diuqi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (DataSave.丢弃鱼竿 == false) {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							DInputDialog dialog = new DInputDialog("警告！", "如发生丢弃错误事件，本软件开发者概不负责！！！", true, true);
							UIModel.setUI(dialog);
							int i = dialog.start();
							if (i == 0) {
								DataSave.丢弃鱼竿 = !DataSave.丢弃鱼竿;
								DataSave.savedata();
							} else {
								diuqi.setSelected(false);
							}
							UIModel.setUI(DataSave.bodyPanel);
						}
					});
					t.start();
					return;
				}
				DataSave.丢弃鱼竿 = !DataSave.丢弃鱼竿;
				DataSave.savedata();
			}
		});

		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);
		panel_5.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		radioButton = new JRadioButton("包满关机");
		radioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.BMGJ = radioButton.isSelected();
			}
		});
		radioButton.setSelected(DataSave.BMGJ);
		panel_5.add(radioButton);

		DButton button_11 = new DButton((String) null, (Color) null);
		button_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "当检测到背包满包是会关闭电脑，不过可能出现判断失误\n勾选提醒，将会将关机行为改为用声音（需勾选监视中的声音）提醒用户", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});

		checkBox_1 = new JCheckBox("提醒");
		checkBox_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.包满提醒 = checkBox_1.isSelected();
			}
		});
		panel_5.add(checkBox_1);
		button_11.setText("说明");
		button_11.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_11.setBackground(Color.GRAY);
		panel_5.add(button_11);
		bjs = new JRadioButton("监视↓");
		panel_5.add(bjs);

		DButton button_8 = new DButton((String) null, (Color) null);
		panel_5.add(button_8);
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：",
								"勾选被监视之后，镜头会转动，当发现周围有人时，会进行后后面勾选的某种操作:\n换线:换线时间较长（5分钟），耐心等待\n工会:监视工会成员\n对话:监视左下角对话\n声音:被监视电脑会发出声音\n被转移:监听小地图变化，如果发生变化，将会触发，如果这个经常误判断，可关闭。",
								true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_8.setText("说明");
		button_8.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_8.setBackground(Color.GRAY);

		jtbzy = new JCheckBox("被转移");
		panel_5.add(jtbzy);
		jtbzy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.被转移 = jtbzy.isSelected();
				DataSave.savedata();
			}
		});
		jtbzy.setSelected(true);
		bjs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.被监视 = bjs.isSelected();
				DataSave.savedata();
			}
		});

		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_16 = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) panel_16.getLayout();
		flowLayout_5.setVgap(0);
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		flowLayout_5.setHgap(0);
		panel_4.add(panel_16);

		syll = new JRadioButton("黄鱼失误");
		panel_16.add(syll);
		syll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.黄鱼失误 = !DataSave.黄鱼失误;
				DataSave.savedata();
			}
		});

		JPanel panel_14 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_14.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		flowLayout_3.setHgap(0);
		panel_4.add(panel_14);

		bjhx = new JRadioButton("掉血提醒");
		panel_14.add(bjhx);

		DButton button_5 = new DButton((String) null, (Color) null);
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "人物掉血后提醒，需要勾选被监视中的声音选项", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_5.setText("说明");
		button_5.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_5.setBackground(Color.GRAY);
		panel_14.add(button_5);
		bjhx.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.被击换线 = !DataSave.被击换线;
				DataSave.savedata();
			}
		});

		JPanel panel_9 = new JPanel();
		panel_3.add(panel_9);
		panel_9.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		xtdy = new JCheckBox("系统钓");
		xtdy.setSelected(true);
		panel_9.add(xtdy);

		checkBox = new JCheckBox("人");
		checkBox.setSelected(true);
		panel_9.add(checkBox);

		xthx = new JCheckBox("换线");
		panel_9.add(xthx);
		xtdy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (xtdy.isSelected()) {
					xthx.setSelected(false);
				} else {
					xthx.setSelected(true);
				}
			}
		});
		xthx.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (xthx.isSelected()) {
					xtdy.setSelected(false);
				} else {
					xtdy.setSelected(true);
				}
			}
		});
		sjgh = new JCheckBox("工会");
		sjgh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.监视公会 = sjgh.isSelected();
				DataSave.savedata();
			}
		});
		panel_9.add(sjgh);

		fcsy = new JCheckBox("声音");
		fcsy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.监视声音 = fcsy.isSelected();
				DataSave.savedata();
			}
		});
		fcsy.setSelected(DataSave.监视声音);
		panel_9.add(fcsy);

		jtdh = new JCheckBox("对话");
		jtdh.setSelected(DataSave.监听对话);
		jtdh.setSelected(true);
		panel_9.add(jtdh);

		// jtjq = new JCheckBox("加强");
		// jtjq.setSelected(DataSave.监听加强);
		// jtjq.setSelected(true);
		// panel_9.add(jtjq);
		setLayout(new BorderLayout(0, 0));
		add(panel_10, BorderLayout.CENTER);
		JPanel panel_11 = new JPanel();
		btnf = new DButton((String) null, Color.white);

		btnf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 开始钓鱼dyHandle
				Tool tool = new Tool();
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH - 50 + DataSave.SCREEN_X, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y, InputEvent.BUTTON1_MASK);
				btnf.setEnabled(false);
				button.setEnabled(true);
				DyHandle.start();
			}
		});
		panel_11.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		panel_11.add(btnf);
		btnf.setText("开始钓鱼");

		panel_2 = new JPanel();

		DButton button_2 = new DButton((String) null, Color.WHITE);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UIModel.setUI(DataSave.custonimg);
			}
		});
		button_2.setText("自定义");

		DButton button_1 = new DButton((String) null, Color.WHITE);
		button_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIModel.setUI(DataSave.deBug);
			}
		});
		button_1.setText("设置");

		DButton button_7 = new DButton((String) null, (Color) null);
		button_7.setText("查看录像");

		textField = new JTextField();
		textField.setColumns(10);
		GroupLayout gl_panel_10 = new GroupLayout(panel_10);
		gl_panel_10.setHorizontalGroup(gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_10.createSequentialGroup().addContainerGap()
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_10.createParallelGroup(Alignment.LEADING).addComponent(textField, GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
								.addGroup(gl_panel_10.createSequentialGroup()
										.addComponent(button_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(button_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(button_7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(1))
				.addGroup(gl_panel_10.createSequentialGroup().addContainerGap().addComponent(panel_11, GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(gl_panel_10.createSequentialGroup().addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 546, Short.MAX_VALUE).addGap(1)));
		gl_panel_10
				.setVerticalGroup(gl_panel_10.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_10.createSequentialGroup()
								.addComponent(panel_11, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panel_10.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, gl_panel_10.createSequentialGroup()
										.addGroup(gl_panel_10.createParallelGroup(Alignment.BASELINE)
												.addComponent(button_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(button_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(button_7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
								.addContainerGap()));

		// panel_9.add(panel_16);
		button = new DButton((String) null, Color.white);
		panel_11.add(button);
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button.setEnabled(false);
				btnf.setEnabled(true);
				DyHandle.stop();
				// RDPMAIN.startListener();
			}
		});
		button.setText("停止钓鱼");

		final JRadioButton radioButton_1 = new JRadioButton("定时绑定");
		radioButton_1.setSelected(DyHandle.ds_bool);
		radioButton_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DyHandle.ds_bool = radioButton_1.isSelected();
			}
		});
		panel_11.add(radioButton_1);

		JLabel label = new JLabel("延后启动:");
		panel_11.add(label);

		textField_1 = new DTextField(6);
		textField_1.setText("0");
		textField_1.setColumns(4);
		panel_11.add(textField_1);

		JLabel label_1 = new JLabel("分");
		panel_11.add(label_1);
		
		JLabel label_2 = new JLabel("延后停止:");
		panel_11.add(label_2);
		
		stop_time = new DTextField(6);
		stop_time.setText("0");
		stop_time.setColumns(4);
		panel_11.add(stop_time);
		
		JLabel label_3 = new JLabel("分");
		panel_11.add(label_3);

		radioButton_2 = new JRadioButton("系统钓鱼模式");
		panel_11.add(radioButton_2);

		DButton button_3 = new DButton((String) null, (Color) null);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "延后停止：延后停止时间大于0分钟时生效，将再指定分钟数之后停止钓鱼。若勾选了系统钓鱼，则在指定分钟后切换为系统钓鱼。\n系统钓鱼：勾选的功能依然有效，如: 换杆，丢弃，被监视，以及定时绑定中的定时功能等", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();

			}
		});
		button_3.setText("说明");
		button_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_3.setBackground(Color.GRAY);
		panel_11.add(button_3);
		panel_10.setLayout(gl_panel_10);

		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		add(panel, BorderLayout.SOUTH);

		JLabel lblAltAlt = new JLabel("f10 开始 f11 结束");
		panel.add(lblAltAlt);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8796119459522641785L;
	private JPanel panel_2;
	private Graphics g = null;
	private BufferedImage image = null;
	public JTextField textField;
	private DButton btnf = null;
	private DButton button = null;
	private DTextField yh_time = null;
	private Dimension now_Dimension = new Dimension(700, 350);
	private Point now_point = null;
	private JRadioButton huang = null;
	private JRadioButton lan = null;
	private JRadioButton lv = null;
	private JRadioButton bai = null;
	private JRadioButton yaosi = null;
	private JRadioButton suipian = null;
	private JRadioButton huangan = null;
	private JRadioButton diuqi = null;
	private JRadioButton bjhx = null;
	private JRadioButton syll = null;
	private JRadioButton bjs = null;
	private JCheckBox xtdy = null;
	private JCheckBox xthx = null;
	private JCheckBox sjgh = null;
	private JCheckBox fcsy = null;
	private JCheckBox jtdh = null;
	// private JCheckBox jtjq = null;
	private JCheckBox jtbzy = null;
	private JRadioButton radioButton = null;

	
	public boolean isMan() {
		return checkBox.isSelected();
	}

	public boolean getXthx() {
		return xthx.isSelected();
	}

	public boolean getXtdy() {
		return xtdy.isSelected();
	}

	public long getYh_time() {

		if (Other.isInteger(yh_time.getText())) {
			return Integer.parseInt(yh_time.getText()) * 1000 * 60;
		}
		return 0;
	}
	public long getDyStopTime() {
		if (Other.isInteger(stop_time.getText().trim())) {
			return Integer.parseInt(stop_time.getText().trim()) * 1000 * 60;
		}
		return 0;
	}
	public void init(BufferedImage img) {
		if (!(DataSave.StaticUI.getUI() == DataSave.bodyPanel && DataSave.bodyPanel.getUIOne() == this))
			return;
		init1(img);
	}

	private void init1(BufferedImage img) {

		image = img;
		if (g == null) {
			g = panel_2.getGraphics();
		}
		g.clearRect(0, 0, panel_2.getWidth(), panel_2.getHeight());
		g.drawImage(img, 0, 0, panel_2);

	}

	@Override
	public void init() {
		if (now_point != null)
			DataSave.StaticUI.setLocation(now_point);
		DataSave.StaticUI.setSize(now_Dimension);
		DataSave.StaticUI.setAlwaysOnTop(false);
		DataSave.StaticUI.updateTitle("(钓鱼模式)");
		// 注册一个监听事件
		JIntellitype.getInstance().registerHotKey(99, "f10");
		JIntellitype.getInstance().registerHotKey(101, "f11");
		JIntellitype.getInstance().addHotKeyListener(hotkeyListener);
		datainit();
	}

	private HotkeyListener hotkeyListener = new HotkeyListener() {
		@Override
		public void onHotKey(int arg0) {
			if (arg0 == 99) {
				// 开始钓鱼dyHandle
				btnf.setEnabled(false);
				button.setEnabled(true);
				DyHandle.start();
				// RDPMAIN.stopListener();
			} else if (arg0 == 101) {
				btnf.setEnabled(true);
				button.setEnabled(false);
				DyHandle.stop();
				// RDPMAIN.startListener();
			}
		}
	};
	private DTextField textField_1;
	private JCheckBox checkBox;
	private JCheckBox checkBox_1;
	private JRadioButton radioButton_2;
	private JRadioButton red;
	private DTextField stop_time;

	public boolean isSystemfishing() {
		return radioButton_2.isSelected();
	}

	public boolean isBMTX() {
		return DataSave.包满提醒;
	}

	public long getDyWaitTime() {
		String string = textField_1.getText().trim();
		if (Other.isLong(string)) {
			long l = Long.parseLong(string);
			if (l > 0) {
				return l * 60 * 1000;
			}
		}
		return 0;
	}

	public void writeColor(Color cc, int i, int j) {
		if (!(DataSave.StaticUI.getUI() == DataSave.bodyPanel && DataSave.bodyPanel.getUIOne() == this))
			return;
		if (g == null) {
			g = panel_2.getGraphics();
		}
		g.setColor(cc);
		if (image == null)
			g.fill3DRect(i, j, 1, 1, true);
		else
			g.fill3DRect(image.getWidth() + i, j, 1, 1, true);
	}

	@Override
	public void quit() {
		// button.setEnabled(false);
		// btnf.setEnabled(true);
		// DyHandle.stop();
		JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
		now_point = DataSave.StaticUI.getLocation();
		now_Dimension = DataSave.StaticUI.getSize();
	}

	public void datainit() {
		huang.setSelected(DataSave.黄);
		lan.setSelected(DataSave.蓝);
		lv.setSelected(DataSave.绿);
		bai.setSelected(DataSave.白);
		yaosi.setSelected(DataSave.银钥匙);
		suipian.setSelected(DataSave.碎片);
		huangan.setSelected(DataSave.更换鱼竿);
		diuqi.setSelected(DataSave.丢弃鱼竿);
		bjhx.setSelected(DataSave.被击换线);
		syll.setSelected(DataSave.黄鱼失误);
		bjs.setSelected(DataSave.被监视);
		radioButton.setSelected(DataSave.BMGJ);
		sjgh.setSelected(DataSave.监视公会);
		fcsy.setSelected(DataSave.监视声音);
		checkBox_1.setSelected(DataSave.包满提醒);
		jtdh.setSelected(DataSave.监听对话);
		jtbzy.setSelected(DataSave.被转移);
		red.setSelected(DataSave.红);
		// hod.setSelected(false);
	}

	public void setBjs() {
		bjs.setSelected(DataSave.被监视);
	}

	public void setUse(boolean b) {
		DataSave.bodyPanel.setButtonUse("dy", b);
		DataSave.qp.setUse(b);

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
