package com.mugui.GJ.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.eclipse.swt.internal.win32.OS;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DInputDialog;
import com.mugui.Dui.DPanel;
import com.mugui.GJ.model.DyModel;
import com.mugui.GJ.model.GJTool;
import com.mugui.GJ.ui.DataSave;
import com.mugui.model.UIModel;
import com.mugui.tool.Other;
import com.mugui.windows.MouseListenerTool;
import com.mugui.windows.MouseListenerTool.KeyListener;
import com.mugui.windows.Tool;

import javax.swing.SwingConstants;

import com.mugui.Dui.DTextField;
import com.mugui.Dui.DTextField.CodeListener;

import java.awt.Font;
import javax.swing.JRadioButton;
import com.mugui.Dui.DVerticalFlowLayout;
import com.mugui.Dui.ToggleButtonManage;

public class DyPanel extends DPanel {
	private Tool tool = null;

	public DyPanel() {
		JPanel panel_10 = new JPanel();
		setLayout(new BorderLayout(0, 0));
		add(panel_10, BorderLayout.CENTER);
		JPanel panel_11 = new JPanel();
		btnf = new DButton((String) null, Color.white); 

		btnf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dytime <= 0) {
					JOptionPane.showMessageDialog(DataSave.StaticUI, "检测到你天数不足，无法使用这个功能", "警告", JOptionPane.OK_OPTION);
					return;
				}
				// 开始钓鱼dyHandle
				btnf.setEnabled(false);
				button.setEnabled(true);
				tool.mouseMovePressOne(DataSave.SCREEN_X + DataSave.SCREEN_WIDTH - 50, DataSave.SCREEN_HEIGHT / 2 + DataSave.SCREEN_Y, InputEvent.BUTTON1_MASK);
				DataSave.modelManager.get("DyModel").start();
			}
		});
		panel_11.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		panel_11.add(btnf);
		btnf.setText("开始钓鱼");

		button = new DButton((String) null, Color.white);
		panel_11.add(button);
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button.setEnabled(false);
				btnf.setEnabled(true);
				DataSave.modelManager.get("DyModel").stop();
			}
		});
		button.setText("停止钓鱼");

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new BorderLayout(0, 0));

		textField = new JTextField();
		panel_3.add(textField);
		textField.setColumns(10);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setAlignment(FlowLayout.LEFT);

		DButton button_1 = new DButton((String) null, Color.WHITE);
		panel_2.add(button_1);
		button_1.setHorizontalAlignment(SwingConstants.LEFT);
		button_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIModel.setUI(DataSave.uiManager.get("DeBug"));
			}
		});
		button_1.setText("设置");

		draw_panel = new JPanel() {
			private static final long serialVersionUID = 1573540196338410099L;

			public void paint(Graphics g) {
				super.paint(g);
				if (draw_img != null)
					g.drawImage(draw_img, 0, 0, draw_img.getWidth(), draw_img.getHeight(), this);

			}
		};
		GroupLayout gl_panel_10 = new GroupLayout(panel_10);
		gl_panel_10
				.setHorizontalGroup(gl_panel_10.createParallelGroup(Alignment.TRAILING).addComponent(panel_11, GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
						.addGroup(gl_panel_10.createSequentialGroup().addContainerGap()
								.addGroup(gl_panel_10.createParallelGroup(Alignment.LEADING, false)
										.addComponent(panel_3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(panel_2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(Alignment.LEADING, gl_panel_10.createSequentialGroup().addContainerGap()
								.addComponent(draw_panel, GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE).addContainerGap()));
		gl_panel_10.setVerticalGroup(gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_10.createSequentialGroup()
						.addComponent(panel_11, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(draw_panel, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap()));

		JLabel label_1 = new JLabel("启动/停止快捷键:");
		panel_11.add(label_1);

		textField_1 = new DTextField(10);
		textField_1.setText("F10");
		textField_1.setColumns(6);
		textField_1.setKeyCodeListener(true);
		textField_1.addKeyCodeListener(new CodeListener() {
			@Override
			public void callBack(DTextField dTextField) {
				if (!dTextField.getText().trim().equals("")) {
					int keycode;
					if ((keycode = Tool.getkeyCode(dTextField.getText().trim())) > -1) {
						if (OS.MapVirtualKey(keycode, 0) == keyListener.key)
							return;
						MouseListenerTool.newInstance().removeKeyListeners(keyListener);
						keyListener.key = OS.MapVirtualKey(keycode, 0);
						MouseListenerTool.newInstance().addKeyListener(keyListener);
					}
				}
			}

		});
		panel_11.add(textField_1);
		draw_panel.setLayout(new DVerticalFlowLayout());

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(0);
		draw_panel.add(panel_1);
		JLabel label = new JLabel("收杆指数:");
		panel_1.add(label);

		zhishu = new DTextField(5);
		panel_1.add(zhishu);
		zhishu.setText("0.36");
		zhishu.setColumns(5);

		keepFish = new JRadioButton("连续起钓");
		panel_1.add(keepFish);

		DButton button_3 = new DButton((String) null, (Color) null);
		panel_1.add(button_3);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "指针过慢请调小数值，指针过快请调大数值", true, false);
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

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_4.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setHgap(0);
		draw_panel.add(panel_4);

		JLabel label_2 = new JLabel("使用鱼饵类型：");
		panel_4.add(label_2);
		manage = new ToggleButtonManage();
		JRadioButton rdbtnNewRadioButton = new JRadioButton("地龙");
		manage.addRadioButton(rdbtnNewRadioButton);
		panel_4.add(rdbtnNewRadioButton);

		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("玲珑虾米");
		manage.addRadioButton(rdbtnNewRadioButton_1);
		panel_4.add(rdbtnNewRadioButton_1);

		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("夜明珠");
		manage.addRadioButton(rdbtnNewRadioButton_2);
		panel_4.add(rdbtnNewRadioButton_2);
		
		JRadioButton radioButton = new JRadioButton("五仙");
		manage.addRadioButton(radioButton);
		panel_4.add(radioButton);

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UIModel.setUI(DataSave.uiManager.get("Custon"));
			}
		});
		button_2.setText("自定义");
		panel_2.add(button_2); 
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
	private JTextField textField;
	private DButton btnf = null;
	private DButton button = null;
	private Dimension now_Dimension = new Dimension(625, 350);
	private Point now_point = null;
	private long dytime = 0;

	public String getHotKeyListner() {
		return textField_1.getText().trim();
	}

	@Override
	public void init() {
		if (tool == null)
			tool = new Tool();
		if (now_point != null)
			com.mugui.ui.DataSave.StaticUI.setLocation(now_point);
		com.mugui.ui.DataSave.StaticUI.setSize(now_Dimension);
		// com.mugui.ui.DataSave.StaticUI.setAlwaysOnTop(true);
		com.mugui.ui.DataSave.StaticUI.updateTitle("(钓鱼模式)");
		// 注册一个监听事件
		MouseListenerTool.newInstance().addKeyListener(keyListener);
		dataInit();
		if (dytime == 0)
			dytime = ((DyModel) DataSave.modelManager.get("DyModel")).getTime();
		GJTool.SetUseTime(dytime);

	}

	private KeyListener keyListener = new KeyListener(this, OS.MapVirtualKey(KeyEvent.VK_F10, 0)) {
		@Override
		public void callback(Object object) {
			if (!GJTool.isTopHwndIsGJ()) {
				return;
			}
			int arg0 = 99;
			if (btnf.isEnabled()) {
				arg0 = 99;
			} else {
				arg0 = 101;
			}
			if (arg0 == 99) {
				if (dytime <= 0) {
					JOptionPane.showMessageDialog(DataSave.StaticUI, "检测到你天数不足，无法使用这个功能", "警告", JOptionPane.OK_OPTION);
					return;
				}
				// 开始钓鱼dyHandle
				btnf.setEnabled(false);
				button.setEnabled(true);
				DataSave.modelManager.get("DyModel").start();
				System.out.println("开始钓鱼");
			} else if (arg0 == 101) {
				btnf.setEnabled(true);
				button.setEnabled(false);
				DataSave.modelManager.get("DyModel").stop();
				System.out.println("停止钓鱼");
			}

		}
	};
	private JPanel draw_panel;
	private DTextField zhishu;
	private BufferedImage draw_img = null;
	private JRadioButton keepFish;
	private DTextField textField_1;
	private ToggleButtonManage manage;

	public String getFishType() {
		return manage.getSelectButton().getText();
	}

	public boolean isKeepFish() {
		return keepFish.isSelected();
	}

	public double getDelayIndex() {
		if (Other.isDouble(zhishu.getText().trim())) {
			return Double.parseDouble(zhishu.getText().trim());
		}
		return 1.0;
	}

	public void setLogText(String t) {
		textField.setText(t);
	}

	public JPanel getDraw_panel() {
		return draw_panel;
	}

	public void setDraw_panel(JPanel draw_panel) {
		this.draw_panel = draw_panel;
	}

	public BufferedImage getDraw_img() {
		return draw_img;
	}

	public void setDraw_img(BufferedImage draw_img) {
		this.draw_img = draw_img;
	}

	@Override
	public void quit() {
		// MouseListenerTool.newInstance().removeKeyListeners(keyListener);
		now_point = com.mugui.ui.DataSave.StaticUI.getLocation();
		now_Dimension = com.mugui.ui.DataSave.StaticUI.getSize();
	}

	public void setUse(boolean b) {
		com.mugui.ui.DataSave.bodyPanel.setButtonUse("dy", b);
		com.mugui.ui.DataSave.qp.setUse(b);
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
