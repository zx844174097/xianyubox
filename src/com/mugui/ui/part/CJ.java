package com.mugui.ui.part;

import com.mugui.Dui.DPanel;
import com.mugui.jni.Tool.DJni;
import com.mugui.model.CjHandle;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.windows.GameBackstageTool;
import com.mugui.windows.Tool;
import com.sun.jna.Structure;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.mugui.Dui.DButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.awt.event.ActionEvent;
import com.mugui.Dui.DVerticalFlowLayout;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import com.mugui.Dui.DTextField;

public class CJ extends DPanel {
	public CJ() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(panel, BorderLayout.WEST);

		lblNewLabel = new JLabel("人物坐标：");
		panel.add(lblNewLabel);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(0);
		add(panel_1, BorderLayout.NORTH);

		final DButton button = new DButton((String) null, (Color) null);
		final DButton button_1 = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button.setEnabled(false);
				button_1.setEnabled(true);
				new Tool().mouseMovePressOne(DataSave.SCREEN_WIDTH + DataSave.SCREEN_X - 100, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y,
						InputEvent.BUTTON1_MASK);
				new Tool().delay(500);
				GameBackstageTool.INSTANCE.BindGame("BlackDesert64.exe");
				if (thread != null && thread.isAlive())
					return;
				thread = new Thread(new Runnable() {
					DJni jni = GameListenerThread.DJNI;
					HS_MAP map = DataSave.hs_map;

					@Override
					public void run() {
						if (jni == null)
							jni = new DJni();
						DataSave.StaticUI.setAlwaysOnTop(true);
						while (button_1.isEnabled()) {
							if (map == null) {
								map = jni.getHsMap();
							}
							if (map == null)
								System.out.println("error");
							else
								lblNewLabel.setText("<html>人物参数:<br>x:" + map.x + " <br> y:" + map.y + " <br>z:" + map.z + " <br>镜头x:" + map.lens_dx
										+ " <br>镜头y:" + map.lens_dy + "<br>address:" + Long.toHexString(map.address) + "</html>");
							// 143223128 stream
							if (map.x != 0 || map.y != 0 || map.z != 0) {
								GameBackstageTool.INSTANCE.unBindGame();
								break;
							}
							Other.sleep(50);
						}
						while (button_1.isEnabled()) {
							map = jni.getHsMap();
							if (map == null)
								System.out.println("error");
							else
								lblNewLabel.setText("<html>人物参数:<br>x:" + map.x + " <br> y:" + map.y + " <br>z:" + map.z + " <br>镜头x:" + map.lens_dx
										+ " <br>镜头y:" + map.lens_dy + "<br>address:" + Long.toHexString(map.address) + "</html>");

							Other.sleep(50);
						}
						if (jni != null) {
							jni.ungetHsMap();
						}
					}
				});
				thread.start();
			}
		});
		panel_1.add(button);
		button.setText("开始监听");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameBackstageTool.INSTANCE.unBindGame();
				button_1.setEnabled(false);
				button.setEnabled(true);
				DataSave.StaticUI.setAlwaysOnTop(false);
			}
		});
		button_1.setEnabled(false);
		button_1.setText("关闭监听");
		panel_1.add(button_1);

		button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (button_2.getText().equals("开始")) {
					if (com.mugui.http.DataSave.dsTime <= 0) {
						JOptionPane.showMessageDialog(DataSave.StaticUI, "检测到你天数不足，无法使用这个功能", "警告", JOptionPane.OK_OPTION);
						return;
					}
					button_2.setText("关闭");
					new Tool().mouseMovePressOne(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 30, DataSave.SCREEN_Y + (int) (DataSave.SCREEN_HEIGHT * 0.4),
							InputEvent.BUTTON1_MASK);

					CjHandle.start();
				} else {
					button_2.setText("开始");
					CjHandle.stop();
				}
			}
		});
		button_2.setText("开始");
		panel_1.add(button_2);

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.CENTER);
		DVerticalFlowLayout dvfl_panel_2 = new DVerticalFlowLayout();
		dvfl_panel_2.setVgap(0);
		dvfl_panel_2.setHgap(0);
		panel_2.setLayout(dvfl_panel_2);

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_4.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_2.add(panel_4);

		JLabel lblNewLabel_1 = new JLabel("杀羊设置：");
		lblNewLabel_1.setFont(new Font("宋体", Font.BOLD, 14));
		panel_4.add(lblNewLabel_1);

		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_5.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panel_2.add(panel_5);

		radioButton = new JRadioButton("鼠标");
		radioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setAttack_way(0);
			}
		});
		radioButton.setSelected(true);
		panel_5.add(radioButton);

		checkBox = new JCheckBox("左击");
		panel_5.add(checkBox);

		final JCheckBox checkBox_1 = new JCheckBox("右击");
		checkBox_1.setSelected(true);
		panel_5.add(checkBox_1);
		checkBox_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!checkBox_1.isSelected()) {
					checkBox.setSelected(true);
				} else {
					checkBox.setSelected(false);
				}
			}
		});
		checkBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!checkBox.isSelected()) {
					checkBox_1.setSelected(true);
				} else {
					checkBox_1.setSelected(false);
				}
			}
		});
		JLabel label_2 = new JLabel("次数：");
		panel_5.add(label_2);

		textField_2 = new DTextField(2);
		textField_2.setText("2");
		textField_2.setColumns(2);
		panel_5.add(textField_2);

		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_6.getLayout();
		flowLayout_4.setVgap(0);
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		panel_2.add(panel_6);

		radioButton_1 = new JRadioButton("快捷键");
		radioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				setAttack_way(1);
			}
		});
		panel_6.add(radioButton_1);

		JLabel label = new JLabel("按键");
		panel_6.add(label);

		txtfldVkw = new DTextField(20);
		txtfldVkw.setText("VK_W");
		txtfldVkw.setColumns(12);
		panel_6.add(txtfldVkw);

		JPanel panel_7 = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) panel_7.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		flowLayout_5.setVgap(0);
		panel_2.add(panel_7);

		radioButton_2 = new JRadioButton("复合按键");
		radioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				setAttack_way(2);
			}
		});
		panel_7.add(radioButton_2);

		JLabel label_1 = new JLabel("按键1");
		panel_7.add(label_1);

		txtfldVkq = new DTextField(20);
		txtfldVkq.setText("VK_Q");
		txtfldVkq.setColumns(12);
		panel_7.add(txtfldVkq);

		JLabel label_3 = new JLabel("按键2");
		panel_7.add(label_3);

		txtfldVkshift = new DTextField(20);
		txtfldVkshift.setText("VK_SHIFT");
		txtfldVkshift.setColumns(12);
		panel_7.add(txtfldVkshift);

		JPanel panel_8 = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) panel_8.getLayout();
		flowLayout_6.setAlignment(FlowLayout.LEFT);
		flowLayout_6.setVgap(0);
		panel_2.add(panel_8);

		JLabel label_4 = new JLabel("技能释放等待时间:");
		panel_8.add(label_4);

		textField = new DTextField(12);
		textField.setText("3000");
		textField.setColumns(6);
		panel_8.add(textField);

		JLabel lblMs = new JLabel("ms");
		panel_8.add(lblMs);

		JPanel panel_9 = new JPanel();
		FlowLayout flowLayout_7 = (FlowLayout) panel_9.getLayout();
		flowLayout_7.setAlignment(FlowLayout.LEFT);
		flowLayout_7.setVgap(0);
		panel_2.add(panel_9);

		JLabel label_5 = new JLabel("采集行走时间时间:");
		panel_9.add(label_5);

		textField_1 = new DTextField(12);
		textField_1.setText("1500");
		textField_1.setColumns(6);
		panel_9.add(textField_1);

		JLabel label_6 = new JLabel("ms");
		panel_9.add(label_6);

		JPanel panel_10 = new JPanel();
		FlowLayout flowLayout_8 = (FlowLayout) panel_10.getLayout();
		flowLayout_8.setAlignment(FlowLayout.LEFT);
		flowLayout_8.setVgap(0);
		panel_2.add(panel_10);

		JLabel label_7 = new JLabel("采集寻找次数:");
		panel_10.add(label_7);

		textField_3 = new DTextField(2);
		textField_3.setText("3");
		textField_3.setColumns(2);
		panel_10.add(textField_3);

		JPanel panel_11 = new JPanel();
		FlowLayout flowLayout_9 = (FlowLayout) panel_11.getLayout();
		flowLayout_9.setVgap(0);
		flowLayout_9.setAlignment(FlowLayout.LEFT);
		panel_2.add(panel_11);

		radioButton_3 = new JRadioButton("被监视");
		radioButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataSave.被监视 = radioButton_3.isSelected();
			}
		});
		panel_11.add(radioButton_3);

		checkBox_2 = new JCheckBox("声音");
		checkBox_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DataSave.监视声音 = checkBox_2.isSelected();
			}
		});
		checkBox_2.setSelected(true);
		panel_11.add(checkBox_2);

		checkBox_3 = new JCheckBox("工会成员");
		checkBox_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataSave.监视公会 = checkBox_3.isSelected();
			}
		});
		panel_11.add(checkBox_3);

		JPanel panel_3 = new JPanel();
		add(panel_3, BorderLayout.SOUTH);
		JLabel lblwin = new JLabel("如需测试联系群管理，当前仅限win10用户");
		panel_3.add(lblwin);
		txtfldVkw.addKeyListener(kDkeyListener);
		txtfldVkq.addKeyListener(kDkeyListener);
		txtfldVkshift.addKeyListener(kDkeyListener);
	}

	private JLabel lblNewLabel = null;
	private DButton button_2 = null;
	private int attack_way = 0;// 0,鼠标，1，按键，2，复合按键

	/**
	 * 
	 */
	private static final long serialVersionUID = -1356050307444186883L;

	public static class HS_MAP extends Structure {
		public int type;
		public float x;
		public float z;
		public float y;
		public float lens_dx;
		public float lens_dy;
		public long address;

		@Override
		public String toString() {
			Field[] field = this.getClass().getDeclaredFields();
			String string = "";
			for (Field f : field) {
				try {
					string += f.getName() + ":" + String.valueOf(f.get(this)) + " ";
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			return string;
		}
	}

	private Thread thread = null;
	private Dimension now_Dimension = new Dimension(680, 450);
	private Point now_point = null;

	@Override
	public void init() {
		if (now_point != null)
			DataSave.StaticUI.setLocation(now_point);
		DataSave.StaticUI.setSize(now_Dimension);
		DataSave.StaticUI.setAlwaysOnTop(false);
		DataSave.StaticUI.updateTitle("(采集测试)");
		// 注册一个监听事件
		JIntellitype.getInstance().registerHotKey(99, "f10");
		JIntellitype.getInstance().registerHotKey(101, "f11");
		JIntellitype.getInstance().addHotKeyListener(hotkeyListener);
		datainit();
	}

	private void datainit() {
		setAttack_way(0);
		radioButton_3.setSelected(DataSave.被监视);
		checkBox_2.setSelected(DataSave.监视声音);
		checkBox_3.setSelected(DataSave.监视公会);  
	}

	@Override
	public void quit() {

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
				CjHandle.start();
				button_2.setText("关闭");
			} else if (arg0 == 101) {
				CjHandle.stop();
				button_2.setText("开始");
			}
		}
	};
	private JRadioButton radioButton;
	private JRadioButton radioButton_1;
	private JRadioButton radioButton_2;

	public BufferedImage[] getCJGJ() {
		return DataSave.custonimg.getCjImg();
	}

	public int getAttack_way() {
		return attack_way;
	}

	public static class AttackModel {
		public int getAttack_way() {
			return attack_way;
		}

		public void setAttack_way(int attack_way) {
			this.attack_way = attack_way;
		}

		public Object getWay1() {
			return way1;
		}

		public void setWay1(Object way1) {
			this.way1 = way1;
		}

		public Object getWay2() {
			return way2;
		}

		public void setWay2(Object way2) {
			this.way2 = way2;
		}

		private int attack_way;
		private Object way1;
		private Object way2;
	}

	private KeyListener kDkeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void keyPressed(KeyEvent e) {

			Field[] fields = KeyEvent.class.getFields();
			for (Field f : fields) {
				try {
					if (f.get(null) instanceof Integer)
						if (f.getInt(null) == e.getKeyCode() && f.getName().startsWith("VK_")) {
							((DTextField) e.getSource()).setText(f.getName());
						}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Field[] fields = KeyEvent.class.getFields();
			for (Field f : fields) {
				try {
					if (f.get(null) instanceof Integer)
						if (f.getInt(null) == e.getKeyCode() && f.getName().startsWith("VK_")) {
							((DTextField) e.getSource()).setText(f.getName());
						}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			}
		}
	};
	public AttackModel model = new AttackModel();
	private JCheckBox checkBox;
	private DTextField txtfldVkw;
	private DTextField txtfldVkq;
	private DTextField txtfldVkshift;
	private DTextField textField;
	private DTextField textField_1;
	private DTextField textField_3;
	private DTextField textField_2;
	private JRadioButton radioButton_3;
	private JCheckBox checkBox_2;
	private JCheckBox checkBox_3;

	public int getCjFindNum() {
		if (Other.isInteger(textField_3.getText().trim())) {
			return Integer.parseInt(textField_3.getText().trim());
		}
		return 3;
	}

	public int getSkillWaitTime() {
		if (Other.isInteger(textField.getText().trim())) {
			return Integer.parseInt(textField.getText().trim());
		}
		return 3000;
	}

	public int getGotoTime() {
		if (Other.isInteger(textField_1.getText().trim())) {
			return Integer.parseInt(textField_1.getText().trim());
		}
		return 1500;
	}

	public AttackModel getAttack_Model() {

		model.setAttack_way(attack_way);
		switch (attack_way) {
		case 0:
			model.setWay1(checkBox.isSelected());
			if (Other.isInteger(textField_3.getText().trim())) {
				model.setWay2(Integer.parseInt(textField_3.getText().trim()));
			} else
				model.setWay2(3);
			break;
		case 1:
			model.setWay1(txtfldVkw.getText());
			break;
		case 2:
			model.setWay1(txtfldVkq.getText());
			model.setWay2(txtfldVkshift.getText());
			break;
		}
		return model;
	}

	public void setAttack_way(int attack_way) {
		this.attack_way = attack_way;
		switch (attack_way) {
		case 0:
			radioButton.setSelected(true);
			radioButton_1.setSelected(false);
			radioButton_2.setSelected(false);
			break;
		case 1:

			radioButton.setSelected(false);
			radioButton_1.setSelected(true);
			radioButton_2.setSelected(false);
			break;
		case 2:

			radioButton.setSelected(false);
			radioButton_1.setSelected(false);
			radioButton_2.setSelected(true);
			break;
		}
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
