package com.mugui.ui.part;

import java.awt.Dimension;
import java.awt.Point;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.mugui.Dui.DPanel;
import com.mugui.model.MYHandle;
import com.mugui.model.UIModel;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;

import com.mugui.Dui.DButton;

import java.awt.Color;

import javax.swing.JPanel;

import com.mugui.Dui.DInputDialog;
import com.mugui.Dui.DTextField;
import com.mugui.Dui.DVerticalFlowLayout;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.JScrollPane;
import java.awt.Font;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JRadioButton;

public class MY extends DPanel {
	public MY() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new DVerticalFlowLayout());

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_3.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		panel.add(panel_3);

		startbutton = new DButton((String) null, (Color) null);
		panel_3.add(startbutton);

		startbutton.setText("开始");

		stopbutton = new DButton((String) null, (Color) null);
		panel_3.add(stopbutton);
		stopbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startbutton.setEnabled(true);
				stopbutton.setEnabled(false);
				MYHandle.stop();
			}
		});
		stopbutton.setText("停止");
		stopbutton.setEnabled(false);

		DButton button_4 = new DButton((String) null, (Color) null);
		panel_3.add(button_4);
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 上马测试
				MYHandle.testfindMache();
			}
		});
		button_4.setText("上马测试");

		final JRadioButton radioButton = new JRadioButton("空货换线 强制换线时间:");
		panel_3.add(radioButton);

		hx_time = new DTextField(2);
		hx_time.setText("60");
		hx_time.setColumns(2);
		panel_3.add(hx_time);

		JLabel label_2 = new JLabel("分钟 ");
		panel_3.add(label_2);

		final JRadioButton radioButton_1 = new JRadioButton("定时绑定");
		radioButton_1.setSelected(MYHandle.ds_bool);
		radioButton_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MYHandle.ds_bool = radioButton_1.isSelected();
			}
		});
		panel_3.add(radioButton_1);
		radioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.MYHX = radioButton.isSelected();
				if (radioButton.isSelected()) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							JPanel panel = DataSave.StaticUI.getUI();
							DInputDialog dialog = new DInputDialog("说明：", "勾选此选项，必须将贸易人物移动至角色选择界面第一位", true, false);
							UIModel.setUI(dialog);
							dialog.start();
							UIModel.setUI(panel);
						}
					}).start();
				}
			}
		});
		startbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!MYRun()) {
					JOptionPane.showMessageDialog(MY.this, "贸易点勾选少于2个", "警告", JOptionPane.YES_OPTION);
					return;
				}
				startbutton.setEnabled(false);
				stopbutton.setEnabled(true);
				Tool tool = new Tool();
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH - 10 + DataSave.SCREEN_X, 50 + DataSave.SCREEN_Y, InputEvent.BUTTON1_MASK);
				MYHandle.start();
			}

		});

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_1.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setHgap(0);
		panel.add(panel_1);

		JLabel lblNpc = new JLabel("npc识别度:");
		panel_1.add(lblNpc);

		npc_sb = new DTextField(5);
		npc_sb.setText("0.2");
		npc_sb.setColumns(4);
		panel_1.add(npc_sb);

		JLabel label_1 = new JLabel("马车识别度:");
		panel_1.add(label_1);

		mc_sb = new DTextField(5);
		mc_sb.setText("0.15");
		mc_sb.setColumns(4);
		panel_1.add(mc_sb);

		JLabel lblnpc = new JLabel("   前往npc时间");
		panel_1.add(lblnpc);

		textField_2 = new DTextField(5);
		panel_1.add(textField_2);
		textField_2.setColumns(4);
		textField_2.setText("1200");

		dctNpc = new JRadioButton("多次tNPC");
		panel_1.add(dctNpc);

		dctNpc_n = new DTextField(1);
		panel_1.add(dctNpc_n);
		dctNpc_n.setText("2");
		dctNpc_n.setColumns(1);

		JLabel label_3 = new JLabel("次");
		panel_1.add(label_3);

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_4.getLayout();
		flowLayout_4.setVgap(0);
		panel.add(panel_4);

		xlxz = new JRadioButton("寻路修正");
		panel_4.add(xlxz);

		radioButton_2 = new JRadioButton("回仓库放钱");
		panel_4.add(radioButton_2);

		textField_3 = new DTextField(5);
		panel_4.add(textField_3);
		textField_3.setText("180");
		textField_3.setColumns(4);

		JLabel lblid = new JLabel("分钟 渔村id:");
		panel_4.add(lblid);

		textField_4 = new DTextField(2);
		panel_4.add(textField_4);
		textField_4.setText("1");
		textField_4.setColumns(2);
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		add(panel_2, BorderLayout.SOUTH);
		JLabel lblAltalt = new JLabel("f10开始，f11结束");
		panel_2.add(lblAltalt);

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		body = new JPanel();
		scrollPane.setViewportView(body);
		DVerticalFlowLayout dvfl_body = new DVerticalFlowLayout();
		dvfl_body.setVgap(0);
		dvfl_body.setHgap(0);
		body.setLayout(dvfl_body);

		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_5.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		scrollPane.setColumnHeaderView(panel_5);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		JButton button_3 = new JButton("");
		button_3.setPreferredSize(new Dimension(0, 0));
		button_3.setFont(new Font("宋体", Font.PLAIN, 5));
		panel_5.add(button_3);

		DButton button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MYPlane plane = new MYPlane(seat.size() + 1);
				seat.add(plane);
				body.add(plane);
				body.repaint();
				body.validate();
				repaint();
				validate();
			}
		});
		button.setText("新增");
		panel_5.add(button);

		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (seat.size() == 2)
					return;
				body.remove(seat.removeLast());
				body.repaint();
				body.validate();
				repaint();
				validate();
			}
		});
		button_1.setText("减少");
		panel_5.add(button_1);

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataSave.savedata();
			}
		});
		button_2.setText("保存");
		panel_5.add(button_2);

		JLabel lblnpc_1 = new JLabel("当前贸易npc编号：");
		panel_5.add(lblnpc_1);

		textField_1 = new DTextField(2);
		textField_1.setColumns(2);
		textField_1.setText("1");
		panel_5.add(textField_1);

		bjhx = new JRadioButton("被击喝血");
		panel_5.add(bjhx);

		textField = new DTextField(2);
		textField.setText("0");
		textField.setColumns(2);
		panel_5.add(textField);

		JLabel label = new JLabel("按键");
		panel_5.add(label);
	}

	private class MYPlane extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1376014546596814996L;

		public MYPlane(int i) {
			this(i, null);
		}

		public MYPlane(int i, String place) {
			JLabel label_2 = new JLabel("贸易点" + i + "：");
			add(label_2);
			textField = new DTextField(32);
			textField.setColumns(32);
			textField.setText(place);
			add(textField);
			FlowLayout flowLayout_1 = (FlowLayout) getLayout();
			flowLayout_1.setVgap(0);
			flowLayout_1.setHgap(0);
			box = new JCheckBox();
			add(box);
		}

		public boolean isSelected() {
			return box.isSelected();
		}

		// public void setEditable(boolean istrue) {
		// textField.setEditable(istrue);
		// }
		//
		// public void setPlace(String string) {
		// textField.setText(string.trim());
		// }

		private DTextField textField = null;
		private JCheckBox box = null;

		public String getPlace() {
			return textField.getText().trim();
		}
	}

	private boolean MYRun() {
		Iterator<MYPlane> iterator = seat.iterator();
		int num = 0;
		while (iterator.hasNext()) {
			if (iterator.next().isSelected()) {
				num++;
			}
			if (num >= 2) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1367879060643025365L;
	private Dimension now_Dimension = new Dimension(650, 330);
	private Point now_point = null;
	private DButton startbutton = null;
	private DButton stopbutton = null;
	private LinkedList<MYPlane> seat = null;
	private JPanel body = null;
	private DTextField textField_1 = null;
	private DTextField textField = null;
	private JRadioButton bjhx = null;
	private DTextField textField_2 = null;
	private DTextField npc_sb = null;
	private DTextField mc_sb = null;
	private DTextField hx_time = null;
	private JRadioButton xlxz = null;
	private JRadioButton dctNpc = null;
	private DTextField dctNpc_n = null;

	public int getDctNpc_n() {
		String string = dctNpc_n.getText().trim();
		if (Other.isInteger(string)) {
			return Integer.parseInt(string);
		}
		return 2;
	}

	public boolean isXlxz() {
		return xlxz.isSelected();
	}

	public boolean isDctNPC() {
		return dctNpc.isSelected();
	}

	private long getHx_time() {
		String i = hx_time.getText().trim();
		if (Other.isInteger(i)) {
			return Long.parseLong(i) > 0 ? Long.parseLong(i) * 1000 * 60 : 0;
		}
		return 0;
	}

	private long time_hx = 0;

	public void reHx() {
		time_hx = System.currentTimeMillis() + getHx_time();
	}

	public boolean isHx() {
		if (time_hx == 0) {
			time_hx = System.currentTimeMillis() + getHx_time();
		}
		boolean b = false;
		if (System.currentTimeMillis() > time_hx) {
			time_hx = System.currentTimeMillis() + getHx_time();
			b = true;
		}
		return b;
	}

	public double getNpc_sb() {
		if (Other.isDouble(npc_sb.getText().trim())) {
			return Double.parseDouble(npc_sb.getText());
		}
		return 0.20;
	}

	public double getMc_sb() {
		if (Other.isDouble(mc_sb.getText().trim())) {
			return Double.parseDouble(mc_sb.getText());
		}
		return 0.20;
	}

	@Override
	public void init() {
		DataSave.StaticUI.setAlwaysOnTop(false);
		if (now_point != null)
			DataSave.StaticUI.setLocation(now_point);
		DataSave.StaticUI.setSize(now_Dimension);
		DataSave.StaticUI.updateTitle("黑色沙漠咸鱼辅助(贸易模式)");
		JIntellitype.getInstance().registerHotKey(99, "f10");
		JIntellitype.getInstance().registerHotKey(101, "f11");
		JIntellitype.getInstance().addHotKeyListener(hotkeyListener);
	}

	public void datainit(String s) {
		body.removeAll();
		seat = new LinkedList<MY.MYPlane>();
		MYPlane plane = null;
		if (s == null || s.trim().equals("")) {
			plane = new MYPlane(1, "巴哈尔");
			seat.add(plane);
			body.add(plane);
			plane = new MYPlane(2, "西乌塔");
			seat.add(plane);
			body.add(plane);
		} else {
			String ss[] = s.split(",");
			setPlaceNum(Integer.parseInt(ss[0].trim()));
			for (int i = 1; i < ss.length; i++) {
				plane = new MYPlane(i, ss[i]);
				seat.add(plane);
				body.add(plane);
			}
		}
	}

	public String saveInit() {
		String s = getPlaceNum() + ",";
		if (seat != null) {
			Iterator<MYPlane> iterator = seat.iterator();
			while (iterator.hasNext()) {
				s += iterator.next().getPlace() + ",";
			}
		}
		return s;
	}

	@Override
	public void quit() {
		JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
		now_point = DataSave.StaticUI.getLocation();
		now_Dimension = DataSave.StaticUI.getSize();
		// MYHandle.stop();
	}

	private HotkeyListener hotkeyListener = new HotkeyListener() {

		@Override
		public void onHotKey(int arg0) {
			if (arg0 == 99) {
				if (!MYRun()) {
					JOptionPane.showMessageDialog(MY.this, "贸易点勾选少于2个", "警告", JOptionPane.YES_OPTION);
					return;
				}
				startbutton.setEnabled(false);
				stopbutton.setEnabled(true);
				MYHandle.start();
			} else if (arg0 == 101) {
				MYHandle.stop();
				startbutton.setEnabled(true);
				stopbutton.setEnabled(false);
			}
		}
	};
	private DTextField textField_3;
	private DTextField textField_4;
	private JRadioButton radioButton_2;

	public int getYCid() {
		String s = textField_4.getText().trim();
		if (Other.isInteger(s)) {
			return Integer.parseInt(s) - 1 > 0 ? Integer.parseInt(s) - 1 : 0;
		}
		return 0;
	}

	public long getBackCKTime() {
		String string = textField_3.getText().trim();
		if (Other.isInteger(string)) {
			return Integer.parseInt(string) * 60 * 1000;
		}
		return 180 * 60 * 1000;
	}
	public boolean isBackCK() {
		return radioButton_2.isSelected();
	}

	public String getPlace(int place) {
		return seat.get(place).getPlace();
	}

	public int getPlaceSize() {
		return seat.size();
	}

	public int getPlaceNum() {
		String s = textField_1.getText().trim();
		if (Other.isInteger(s)) {
			return Integer.parseInt(s) - 1 > 0 ? Integer.parseInt(s) - 1 : 0;
		}
		return 0;
	}

	public void setPlaceNum(int place) {
		textField_1.setText((place + 1) + "");
	}

	public String getLastPlace() {
		int num = getPlaceNum();
		do {
			num = (num + 1) % seat.size();
		} while (!seat.get(num).isSelected());
		setPlaceNum(num);
		return seat.get(num).getPlace();
	}

	public boolean isBjhx() {
		return bjhx.isSelected();
	}

	public String getBjhxButton() {
		if (textField.getText() != null || textField.getText().trim().equals("")) {
			return "" + 0;
		}
		return textField.getText().trim();
	}

	public int getToNPCTime() {
		String s = textField_2.getText();
		if (Other.isInteger(s.trim())) {
			return Integer.parseInt(s);
		}
		textField_2.setText("900");
		return 900;
	}

	public void setUse(boolean b) {

		DataSave.bodyPanel.setButtonUse("my", b);
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
