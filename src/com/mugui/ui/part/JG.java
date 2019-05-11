package com.mugui.ui.part;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.mugui.Dui.DPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPanel;

import com.mugui.Dui.DButton;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

import java.awt.Font;

import com.mugui.http.Bean.JGOtherBean.JGBean;
import com.mugui.model.JGHandle;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.util.Iterator;

import javax.swing.JCheckBox;

import com.mugui.Dui.DTextField;
import javax.swing.JRadioButton;
import com.mugui.Dui.DVerticalFlowLayout;

public class JG extends DPanel {
	public JG() {
		setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("加工停止快捷键(f11)");
		add(lblNewLabel, BorderLayout.SOUTH);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		panel_1.add(scrollPane);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		scrollPane.setColumnHeaderView(panel_3);

		JLabel label = new JLabel("加工列表：");
		label.setFont(new Font("宋体", Font.BOLD, 16));
		panel_3.add(label);

		DButton button_6 = new DButton((String) null, (Color) null);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list.addJGOtherPanel(new JGOtherPanel());
				list.repaint();
			}
		});
		button_6.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_6.setText("新建加工");
		panel_3.add(button_6);

		list = new JGList();
		scrollPane.setViewportView(list);
		list.setPreferredSize(new Dimension(450, this.getWidth()));
		list.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new DVerticalFlowLayout());

		JPanel panel = new JPanel();
		panel_2.add(panel);

		startbutton = new DButton((String) null, (Color) null);
		startbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 判断是否可以运行加工
				if (!isRunJGHandle()) {
					return;
				}
				tool.mouseMovePressOne(DataSave.SCREEN_WIDTH - 50 + DataSave.SCREEN_X, 50 + DataSave.SCREEN_Y, InputEvent.BUTTON1_MASK);
				tool.delay(500);
				JGHandle.start();
				startbutton.setEnabled(false);
				list.saveJGOther();
			}

		});
		startbutton.setText("开始加工");
		panel.add(startbutton);

		JCheckBox checkBox = new JCheckBox("女仆服");
		checkBox.setSelected(true);
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				DataSave.JGNP = !DataSave.JGNP;
			}
		});
		panel.add(checkBox);

		JLabel label_1 = new JLabel("单次取出个数：");
		panel.add(label_1);

		textField = new DTextField(5);
		textField.setColumns(5);
		textField.setText("50");
		panel.add(textField);

		JLabel label_2 = new JLabel("个");
		panel.add(label_2);

		ts = new JRadioButton("加工石");

		panel.add(ts);

		final JRadioButton radioButton = new JRadioButton("定时绑定");
		radioButton.setSelected(JGHandle.ds_bool);
		radioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JGHandle.ds_bool = radioButton.isSelected();
			}
		});
		panel.add(radioButton);
		
				JPanel panel_5 = new JPanel();
				FlowLayout flowLayout_2 = (FlowLayout) panel_5.getLayout();
				flowLayout_2.setVgap(0);
				panel_2.add(panel_5);
						
								checkBox_2 = new JCheckBox("保险箱加工");
								panel_5.add(checkBox_2);
				
						JLabel label_4 = new JLabel("自行设置仓库加工坐标");
						panel_5.add(label_4);
						
								warehousePoint = new XYPanel(new Dimension(1, 1));
								panel_5.add(warehousePoint);
								warehousePoint.addXYListener(new MouseAdapter() {
									@Override
									public void mouseReleased(MouseEvent e) {
										DataSave.warehousePoint = warehousePoint.getX_YPoint();
									}
								});

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_4.getLayout();
		flowLayout_1.setHgap(0);
		flowLayout_1.setVgap(0);
		panel_2.add(panel_4);

		checkBox_1 = new JCheckBox("多抢");
		panel_4.add(checkBox_1);
		checkBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataSave.MAIDDQ = checkBox_1.isSelected();
			}
		});

		JLabel label_3 = new JLabel("仓库女仆坐标设置");
		panel_4.add(label_3);

		maidPoint1 = new XYPanel(new Dimension(1, 1));
		panel_4.add(maidPoint1);
		maidPoint1.addXYListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				DataSave.maidPoint1 = maidPoint1.getX_YPoint();
			}
		});
		maidPoint2 = new XYPanel(new Dimension(1, 1));
		panel_4.add(maidPoint2);
		maidPoint2.addXYListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				DataSave.maidPoint2 = maidPoint2.getX_YPoint();
			}
		});
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8442022729090723539L;
	private Dimension now_Dimension = new Dimension(680, Toolkit.getDefaultToolkit().getScreenSize().height - 50);
	private Point now_point = new Point(0, 0);
	private Tool tool = new Tool();
	public JGList list = null;
	private DButton startbutton = null;
	private DTextField textField = null;
	private JRadioButton ts = null;

	public boolean isJGStone() {
		return ts.isSelected();
	}

	public String getJGGS() {
		if (Other.isInteger(textField.getText().trim())) {
			return textField.getText();
		}
		return 50 + "";
	}

	@Override
	public void init() {
		DataSave.StaticUI.setAlwaysOnTop(false);
		DataSave.StaticUI.updateTitle("黑色沙漠咸鱼辅助(加工模式)");
		DataSave.StaticUI.setSize(now_Dimension);
		DataSave.StaticUI.setLocation(now_point);
		datainit();
		JIntellitype.getInstance().registerHotKey(99, "f10");
		JIntellitype.getInstance().registerHotKey(101, "f11");
		JIntellitype.getInstance().addHotKeyListener(hotkeyListener);
	}

	private boolean isRunJGHandle() {
		if (com.mugui.http.DataSave.jgTime <= 0) {
			JOptionPane.showMessageDialog(DataSave.StaticUI, "检测到你天数不足，无法使用这个功能", "警告", JOptionPane.OK_OPTION);
			return false;
		}
		JGOtherPanel[] otherPanels = list.getJGOtherLIst();
		if (otherPanels.length == 0) {
			JOptionPane.showMessageDialog(DataSave.StaticUI, "没有加工物，无法开始加工", "警告", JOptionPane.OK_OPTION);
			return false;
		}
		for (JGOtherPanel panel : otherPanels) {
			Iterator<JGBean> bean = panel.getJGOtherBean().getBody().iterator();
			if (!bean.hasNext()) {
				JOptionPane.showMessageDialog(DataSave.StaticUI, "编号:" + panel.getNumber() + "未添加加工物，无法开始加工", "警告", JOptionPane.OK_OPTION);
				return false;
			}
			while (bean.hasNext()) {
				JGBean bean2 = bean.next();
				if (bean2.getColumn() <= 0 || bean2.getRow() <= 0 || bean2.getColumn() > 8 || bean2.getRow() > 24) {
					JOptionPane.showMessageDialog(DataSave.StaticUI, "编号:" + panel.getNumber() + "参数超过限制，无法开始加工", "警告", JOptionPane.OK_OPTION);
					return false;
				}
			}
		}

		return true;
	}

	private HotkeyListener hotkeyListener = new HotkeyListener() {
		@Override
		public void onHotKey(int arg0) {
			if (arg0 == 99) {// 判断是否可以运行加工
				if (!isRunJGHandle()) {
					return;
				}
				JGHandle.start();
				startbutton.setEnabled(false);
				list.saveJGOther();
			} else if (arg0 == 101) {
				JGHandle.stop();
				startbutton.setEnabled(true);
				list.saveJGOther();
				list.init();
			}
		}
	};
	private XYPanel maidPoint1;
	private XYPanel maidPoint2;
	private JCheckBox checkBox_1;
	private JCheckBox checkBox_2;
	private XYPanel warehousePoint;

	public boolean isSafeJG() {
		return checkBox_2.isSelected();
	}

	public boolean isMaidDQ() {
		return checkBox_1.isSelected();
	}

	public Point getMaidPoint1() {
		return maidPoint1.getX_YPoint();
	}

	public Point getMaidPoint2() {
		return maidPoint2.getX_YPoint();
	}

	public void datainit() {
		list.setModel(JGList.JG);
		list.init();
		maidPoint1.setX_Y(DataSave.maidPoint1);
		maidPoint2.setX_Y(DataSave.maidPoint2);
		warehousePoint.setX_Y(DataSave.warehousePoint); 
		checkBox_1.setSelected(DataSave.MAIDDQ);
	}

	@Override
	public void quit() {
		// JGHandle.stop();
		startbutton.setEnabled(true);
		JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
		now_point = DataSave.StaticUI.getLocation();
		now_Dimension = DataSave.StaticUI.getSize();
		list.saveJGOther();
	}

	public JGOtherPanel[] getJGOtherList() {
		return list.getJGOtherLIst();
	}

	public void setUse(boolean b) {

		DataSave.bodyPanel.setButtonUse("jg", b);
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
