package com.mugui.GJ.ui;

import com.mugui.Dui.DPanel;
import com.mugui.GJ.model.GJTool;
import com.mugui.GJ.model.CjModel;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.mugui.Dui.DButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import com.mugui.Dui.DInputDialog;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import java.awt.FlowLayout;

public class CJPanel extends DPanel {
	public CJPanel() {
		setLayout(new BorderLayout(0, 0));
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		button = new DButton("开启辅助画面", (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (button.getText().equals("开启辅助画面")) {
					button.setText("关闭辅助画面");
					DataSave.modelManager.get("CjModel").start();
				} else {
					button.setText("开启辅助画面");
					DataSave.modelManager.get("CjModel").stop();
				}
			}
		});
		panel.add(button);
		DInputDialog inputDialog = new DInputDialog((String) "说明", (String) "辅助采集，自动标记地点，自动记录时间,按键盘f键即可标记", false, false);
		add(inputDialog, BorderLayout.SOUTH);

		draw_panel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				if (draw_img != null)
					g.drawImage(draw_img, 0, 0, draw_img.getWidth(), draw_img.getHeight(), this);
			}
		};
		add(draw_panel, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		draw_panel.add(panel_1);

		checkBox = new JCheckBox("记录点");
		panel_1.add(checkBox);

		radioButton = new JRadioButton("个人");
		panel_1.add(radioButton);

		checkBox_1 = new JCheckBox("列表显示");
		draw_panel.add(checkBox_1);
	}

	private DButton button = null;
	public JPanel draw_panel = null;
	public BufferedImage draw_img = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dimension now_Dimension = new Dimension(625, 350);
	private Point now_point = null;
	private long user_time = 0;

	@Override
	public void init() {
		if (now_point != null)
			com.mugui.ui.DataSave.StaticUI.setLocation(now_point);
		com.mugui.ui.DataSave.StaticUI.setSize(now_Dimension);
		// com.mugui.ui.DataSave.StaticUI.setAlwaysOnTop(true);
		com.mugui.ui.DataSave.StaticUI.updateTitle("(采集模式)");
		// 注册一个监听事件
		JIntellitype.getInstance().registerHotKey(99, "f10");
		JIntellitype.getInstance().registerHotKey(101, "f11");
		JIntellitype.getInstance().addHotKeyListener(hotkeyListener);
		dataInit();
		if (user_time == 0)
			user_time = ((CjModel) DataSave.modelManager.get("CjModel")).getTime();
		GJTool.SetUseTime(user_time);
	}

	private HotkeyListener hotkeyListener = new HotkeyListener() {
		@Override
		public void onHotKey(int arg0) {
			if (arg0 == 99) {
				if (user_time <= 0) {
					JOptionPane.showMessageDialog(DataSave.StaticUI, "检测到你天数不足，无法使用这个功能", "警告", JOptionPane.OK_OPTION);
					return;
				}
				com.mugui.ui.DataSave.StaticUI.setAlwaysOnTop(true);
				button.setText("关闭辅助画面");
				DataSave.modelManager.get("CjModel").start();
				System.out.println("开启采集辅助");
			} else if (arg0 == 101) {
				com.mugui.ui.DataSave.StaticUI.setAlwaysOnTop(false);
				button.setText("开启辅助画面");
				DataSave.modelManager.get("CjModel").stop();
				System.out.println("关闭采集辅助");
			}
		}
	};
	private JCheckBox checkBox;
	private JCheckBox checkBox_1;
	private JRadioButton radioButton;

	public boolean isRecordOneself() {
		return radioButton.isSelected();
	}

	public boolean isShowList() {
		return checkBox_1.isSelected();
	}

	public boolean isRecord() {
		return checkBox.isSelected();
	}

	@Override
	public void quit() {
		JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
		now_point = DataSave.StaticUI.getLocation();
		now_Dimension = DataSave.StaticUI.getSize();
	}

	@Override
	public void dataInit() {

	}

	@Override
	public void dataSave() {

	}

}
