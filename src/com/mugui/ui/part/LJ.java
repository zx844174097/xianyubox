package com.mugui.ui.part;

import com.mugui.Dui.DPanel;
import com.mugui.http.Bean.JGOtherBean.JGBean;
import com.mugui.model.LjHandle;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.mugui.Dui.DButton;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import com.mugui.Dui.DTextField;
import javax.swing.JRadioButton;
import com.mugui.Dui.DVerticalFlowLayout;

public class LJ extends DPanel {
	public LJ() {
		setLayout(new BorderLayout(0, 0));

		head = new JPanel();
		add(head, BorderLayout.NORTH);
		DVerticalFlowLayout dvfl_panel = new DVerticalFlowLayout();
		dvfl_panel.setVgap(0);
		dvfl_panel.setHgap(0);
		head.setLayout(dvfl_panel);

		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_5.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		head.add(panel_5);

		button = new DButton((String) null, (Color) null);
		panel_5.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isRunJGHandle()) {
					return;
				}
				Tool tool = new Tool();
				tool.delay(500);
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH - 10 + DataSave.SCREEN_X, (int) (DataSave.SCREEN_HEIGHT * 0.4) + DataSave.SCREEN_Y,
						InputEvent.BUTTON1_MASK);
				tool.delay(500);
				button.setEnabled(false);
				button_1.setEnabled(true);
				LjHandle.start();
				list.saveJGOther();
				DataSave.savedata();
			}
		});
		button.setText("开始炼金");

		button_1 = new DButton((String) null, (Color) null);
		panel_5.add(button_1);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button.setEnabled(true);
				button_1.setEnabled(false);
				LjHandle.stop();
				list.saveJGOther();
				list.init();
			}
		});
		button_1.setText("结束炼金");
		button_1.setEnabled(false);

		JLabel label = new JLabel("单次炼金组数：");
		panel_5.add(label);

		textfield = new DTextField(3);
		panel_5.add(textfield);
		textfield.setColumns(3);
		textfield.setText("50");

		JPanel panel_4 = new JPanel();
		panel_5.add(panel_4);
		FlowLayout flowLayout_1 = (FlowLayout) panel_4.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);

		换台子 = new JRadioButton("换台子");
		panel_4.add(换台子);

		xypanel = new XYPanel(new Dimension(1, 1));
		xypanel.setX_Y(new Point(0, 0));
		panel_4.add(xypanel);

		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_6.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setHgap(20);
		head.add(panel_6);

		DButton button_3 = new DButton((String) null, (Color) null);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tool tool = new Tool();
				tool.delay(500);
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH - 10 + DataSave.SCREEN_X, (int) (DataSave.SCREEN_HEIGHT * 0.4) + DataSave.SCREEN_Y,
						InputEvent.BUTTON1_MASK);
				tool.delay(500);
				LjHandle.start(e.getActionCommand().toString());
			}
		});

		final JRadioButton radioButton = new JRadioButton("定时绑定");
		radioButton.setSelected(LjHandle.ds_bool);
		radioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LjHandle.ds_bool = radioButton.isSelected();
			}
		});
		panel_6.add(radioButton);

		JPanel panel_7 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_7.getLayout();
		flowLayout_4.setVgap(0);
		flowLayout_4.setHgap(0);
		panel_6.add(panel_7);

		JLabel label_1 = new JLabel("回收识别度");
		panel_7.add(label_1);

		lj_sbd = new DTextField(4);
		lj_sbd.setText("0.15");
		lj_sbd.setColumns(4);
		panel_7.add(lj_sbd);
		button_3.setFont(new Font("Dialog", Font.BOLD, 12));
		button_3.setText("放置测试");
		panel_6.add(button_3);

		DButton button_4 = new DButton((String) null, (Color) null);
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tool tool = new Tool();
				tool.delay(500);
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH - 10 + DataSave.SCREEN_X, (int) (DataSave.SCREEN_HEIGHT * 0.4) + DataSave.SCREEN_Y,
						InputEvent.BUTTON1_MASK);
				tool.delay(500);
				LjHandle.start(e.getActionCommand().toString());
			}
		});
		button_4.setFont(new Font("Dialog", Font.BOLD, 12));
		button_4.setText("回收测试");
		panel_6.add(button_4);

		gaojiSet = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) gaojiSet.getLayout();
		flowLayout_5.setVgap(0);
		flowLayout_5.setHgap(0);
		gaojiSet.setBackground(null);
		//head.add(gaojiSet);
		JPanel panel_9 = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) panel_9.getLayout();
		flowLayout_6.setVgap(0);
		flowLayout_6.setHgap(0);
		panel_9.setBackground(null); 
		gaojiSet.add(panel_9);

		JLabel label_2 = new JLabel("保险箱");
		panel_9.add(label_2);

		taizi_shijiao = new DTextField(8);
		taizi_shijiao.setText("0");
		taizi_shijiao.setColumns(6);
		taizi_shijiao.setBackground(null);
		panel_9.add(taizi_shijiao);

		DButton button_5 = new DButton((String) null, (Color) null);
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(GameListenerThread.DJNI!=null)
				taizi_shijiao.setText(GameListenerThread.DJNI.getHsMap().lens_dx+"");
			}
		});
		button_5.setText("锁定");
		button_5.setFont(new Font("Dialog", Font.BOLD, 12));
		gaojiSet.add(button_5);

		JPanel panel_10 = new JPanel();
		FlowLayout flowLayout_7 = (FlowLayout) panel_10.getLayout();
		flowLayout_7.setHgap(0);
		flowLayout_7.setVgap(0); 
		panel_10.setBackground(null);
		gaojiSet.add(panel_10);

		JLabel label_3 = new JLabel("料理台");
		panel_10.add(label_3);

		baoxianxiang_shijiao = new DTextField(8);
		baoxianxiang_shijiao.setText("0"); 
		baoxianxiang_shijiao.setColumns(6);
		baoxianxiang_shijiao.setBackground(null);
		panel_10.add(baoxianxiang_shijiao);

		DButton button_7 = new DButton((String) null, (Color) null);
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(GameListenerThread.DJNI!=null)
					baoxianxiang_shijiao.setText(GameListenerThread.DJNI.getHsMap().lens_dx+""); 
			}
		});
		button_7.setText("锁定");
		button_7.setFont(new Font("Dialog", Font.BOLD, 12));
		gaojiSet.add(button_7);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayou = (FlowLayout) panel_2.getLayout();
		flowLayou.setVgap(0);
		flowLayou.setHgap(0);
		add(panel_2, BorderLayout.SOUTH);
		panel_2.add(lblAltAlt);

		body = new JPanel();
		add(body, BorderLayout.CENTER);
		body.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		body.add(scrollPane);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		scrollPane.setColumnHeaderView(panel_3);

		JLabel lblor = new JLabel("料理Or炼金列表：");
		lblor.setFont(new Font("宋体", Font.BOLD, 16));
		panel_3.add(lblor);

		DButton button_6 = new DButton((String) null, (Color) null);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list.addJGOtherPanel(new JGOtherPanel(JGList.LJ));
				list.repaint();
			}
		});
		button_6.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_6.setText("新建料理");
		panel_3.add(button_6);

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list.saveJGOther();
			}
		});
		button_2.setText("保存");
		button_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_3.add(button_2);

		list = new JGList();
		list.setModel(JGList.LJ);
		scrollPane.setViewportView(list);
		list.setPreferredSize(new Dimension(450, this.getWidth()));
		list.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6502012143177898245L;
	private final JLabel lblAltAlt = new JLabel("f10开始 f11结束");

	private Dimension now_Dimension = new Dimension(570, Toolkit.getDefaultToolkit().getScreenSize().height - 50);
	private Point now_point = new Point(0, 0);
	public JGList list = null;
	private DTextField textfield = null;
	private XYPanel xypanel = null;
	private DTextField lj_sbd = null;
	private JPanel gaojiSet = null;
	private JPanel body = null;
	private JPanel head = null;

	public double getLj_sbd() {
		String string = lj_sbd.getText().trim();
		if (Other.isDouble(string)) {
			return Double.parseDouble(string);
		}
		return 0.15;
	}

	public void setLj_sbd(String s) {
		lj_sbd.setText(s);
	}

	@Override
	public void init() {
		if (DataSave.hs_map!=null&&DataSave.hs_map.address != 0) 
			head.add(gaojiSet);
		if (now_point != null)
			DataSave.StaticUI.setLocation(now_point);
		DataSave.StaticUI.setSize(now_Dimension);
		DataSave.StaticUI.setAlwaysOnTop(false);
		DataSave.StaticUI.updateTitle("(炼金模式)");
		datainit();
		// 注册一个监听事件
		JIntellitype.getInstance().registerHotKey(99, "f10");
		JIntellitype.getInstance().registerHotKey(101, "f11");
		JIntellitype.getInstance().addHotKeyListener(hotkeyListener);
	}

	private boolean isRunJGHandle() {
		if (com.mugui.http.DataSave.ljTime <= 0) {
			JOptionPane.showMessageDialog(DataSave.StaticUI, "检测到你天数不足，无法使用这个功能", "警告", JOptionPane.OK_OPTION);
			return false;
		}
		JGOtherPanel[] otherPanels = list.getJGOtherLIst();
		if (otherPanels.length == 0) {
			JOptionPane.showMessageDialog(DataSave.StaticUI, "没有料理物品，无法开始炼金", "警告", JOptionPane.OK_OPTION);
			return false;
		}
		for (JGOtherPanel panel : otherPanels) {
			Iterator<JGBean> bean = panel.getJGOtherBean().getBody().iterator();
			if (!bean.hasNext()) {
				JOptionPane.showMessageDialog(DataSave.StaticUI, "编号:" + panel.getNumber() + "未添加料理物，无法开始炼金", "警告", JOptionPane.OK_OPTION);
				return false;
			}
			while (bean.hasNext()) {
				JGBean bean2 = bean.next();
				if (bean2.getColumn() <= 0 || bean2.getRow() <= 0 || bean2.getColumn() > 8 || bean2.getRow() > 24) {
					JOptionPane.showMessageDialog(DataSave.StaticUI, "编号:" + panel.getNumber() + "参数超过限制，无法开始炼金", "警告", JOptionPane.OK_OPTION);
					return false;
				}
			}
		}

		return true;
	}

	private HotkeyListener hotkeyListener = new HotkeyListener() {
		@Override
		public void onHotKey(int arg0) {
			if (arg0 == 99) {
				if (!isRunJGHandle()) {
					return;
				}

				button.setEnabled(false);
				button_1.setEnabled(true);
				LjHandle.start();
				list.saveJGOther();
			} else if (arg0 == 101) {
				button.setEnabled(true);
				button_1.setEnabled(false);
				LjHandle.stop();
				list.saveJGOther();
				list.init();
			}
		}
	};
	private DButton button;
	private DButton button_1;
	private JRadioButton 换台子 = null;
	private DTextField taizi_shijiao = null;
	private DTextField baoxianxiang_shijiao = null;

	public float getTaziDirection() {
		try {
			return Float.valueOf(taizi_shijiao.getText());
		} catch (Exception e) {
			return 0;
		}
	}

	public float getBaoxianDirection() {
		try {
			return Float.valueOf(baoxianxiang_shijiao.getText());
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public void quit() {
		JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
		now_point = DataSave.StaticUI.getLocation();
		now_Dimension = DataSave.StaticUI.getSize();
		LjHandle.stop();
		saveInit();

	}

	public void datainit() {
		list.setModel(JGList.LJ);
		list.init();
	}

	public JGOtherPanel[] getJGOtherList() {
		return list.getJGOtherLIst();
	}

	public boolean isLj_htz() {
		return 换台子.isSelected();
	}

	public Point getLj_XY() {
		Rectangle rectangle = xypanel.getX_Y();
		return new Point(rectangle.x, rectangle.y);
	}

	public int getGroupNum() {
		String string = textfield.getText().trim();
		if (Other.isInteger(string)) {
			return Integer.parseInt(string) > 0 ? Integer.parseInt(string) : 1;
		}
		return 1;
	}

	public void setGroupNum(String num) {
		textfield.setText(num.trim());
	}

	public void datainit(String readLine) {
		if (readLine == null || readLine.trim().equals("")) {
			return;
		}
		String string[] = readLine.trim().split(";");
		换台子.setSelected(Boolean.parseBoolean(string[0]));
		textfield.setText(string[1]);
		xypanel.setX_Y(string[2]);
		if (string.length > 3)
			setLj_sbd(string[3]);
	}

	public String saveInit() {
		list.saveJGOther();
		String string = 换台子.isSelected() + ";";
		string += textfield.getText() + ";";
		string += xypanel.getX_YText() + ";";
		string += getLj_sbd();
		return string;
	}

	public void setUse(boolean b) {

		DataSave.bodyPanel.setButtonUse("lj", b);
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
