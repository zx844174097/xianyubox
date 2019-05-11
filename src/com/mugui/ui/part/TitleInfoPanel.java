package com.mugui.ui.part;

import com.mugui.Dui.DPanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.BorderLayout;
import java.awt.Font;

import com.mugui.Dui.DTextField;
import com.mugui.Dui.DButton;

import java.awt.Color;

public class TitleInfoPanel extends DPanel {
	public static final String[] type = new String[] { "不区分", "主武器", "辅助武器", "觉醒武器", "防具", "首饰", "材料", "潜力/改良", "消耗品", "生活道具", "炼金石", "魔力水晶", "珍珠商品", "染色药",
			"坐骑", "船舶", "马车", "家具", "特价商品" };
	public static final String[] colortype = new String[] { "任意", "白", "绿", "蓝", "黄" };
	public static final String[] leveltype = new String[] { "任意", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22" };
	public static final String[] time = { "10-15", "0-1", "15-16" }; 

	public TitleInfoPanel(String mode) {
		setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(panel_3);
		JPanel panel_2 = new JPanel();
		JLabel label_2 = new JLabel("物品类型：");
		panel_2.add(label_2);
		objecttype = new JComboBox<String>();
		objecttype.setModel(new DefaultComboBoxModel<String>(type));
		panel_2.add(objecttype);
		JPanel panel = new JPanel();
		JLabel label = new JLabel("物品颜色：");
		panel.add(label);
		objectcolor = new JComboBox<String>();
		objectcolor.setModel(new DefaultComboBoxModel<String>(colortype));
		panel.add(objectcolor);
		JPanel panel_1 = new JPanel();
		JLabel label_1 = new JLabel("物品等级：");
		panel_1.add(label_1);
		objectlevel = new JComboBox<String>();
		objectlevel.setModel(new DefaultComboBoxModel<String>(leveltype));
		panel_1.add(objectlevel);

		JPanel panel_5 = new JPanel();

		JLabel label_3 = new JLabel("上架时间段");
		panel_5.add(label_3);

		// timeleft = new DTextField(3);
		// timeleft.setText("0");
		// timeleft.setColumns(3);
		// panel_5.add(timeleft);

		JLabel label_4 = new JLabel("—");
		panel_5.add(label_4);

		// timeright = new DTextField(4);
		// timeright.setText("15");
		// timeright.setColumns(4);
		// panel_5.add(timeright);

		objecttime = new JComboBox<String>();
		objecttime.setModel(new DefaultComboBoxModel<String>(time));
		panel_5.add(objecttime);

		JLabel label_5 = new JLabel("分钟");
		panel_5.add(label_5);

		JPanel panel_6 = new JPanel();

		JLabel label_6 = new JLabel("物品优先级：");
		panel_6.add(label_6);

		objectPRI = new DTextField(4);
		objectPRI.setText("999");
		objectPRI.setColumns(4);
		panel_6.add(objectPRI);

		DButton button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reSetBodyPanel();
			}
		});
		button.setText("保存");
		// if (mode.equals("qp_other")) {
		panel_3.add(panel_2);
		panel_3.add(panel);
		panel_3.add(panel_1);
		panel_3.add(panel_5);
		panel_3.add(panel_6);
		// } else if (mode.equals("dy_Liaoli")) {
		// label_1.setText("料理类型区分");
		// panel_3.add(panel_1);
		// label_3.setText("吃料理耗费(秒):");
		// label_4.setText("作用时间(分):");
		// timeleft.setText("10");
		// timeright.setText("5");
		// panel_3.add(panel_5);
		//
		// }
		panel_3.add(button);
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel_4, BorderLayout.NORTH);

		lblNewLabel = new JLabel("物品名称：");
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 16));
		panel_4.add(lblNewLabel);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5874368011102926289L;
	private JLabel lblNewLabel = null;
	private JComboBox<String> objecttype = null;
	private JComboBox<String> objectcolor = null;
	private JComboBox<String> objectlevel = null;
	// private DTextField timeleft = null;
	// private DTextField timeright = null;
	private JComboBox<String> objecttime = null;
	private DTextField objectPRI = null;

	@Override
	public void init() {

	}

	@Override
	public void quit() {

	}

	private void reSetBodyPanel() {
		if (objectcolor == null)
			return;
		bodyPanel.nowDimg.getDimgFile().objectcolor = objectcolor.getSelectedItem().toString();
		bodyPanel.nowDimg.getDimgFile().objectlevel = objectlevel.getSelectedItem().toString();
		bodyPanel.nowDimg.getDimgFile().objecttype = objecttype.getSelectedItem().toString();
		bodyPanel.nowDimg.getDimgFile().objecttime = objecttime.getSelectedItem().toString();
		bodyPanel.nowDimg.getDimgFile().objectPRI = objectPRI.getText();
		bodyPanel.nowDimg.getDimgFile().saveAllData();
		bodyPanel.nowDimg.datainit();
	}

	private TitleBodyPanel bodyPanel = null;

	public void datainit(TitleBodyPanel titleBodyPanel) {
		bodyPanel = titleBodyPanel;
		lblNewLabel.setText("物品名称：" + bodyPanel.nowDimg.getDimgFile().objectname);
		if (bodyPanel.nowDimg.getDimgFile().objectcolor.equals("橙")) {
			bodyPanel.nowDimg.getDimgFile().objectcolor = "黄";
		}
		objectcolor.setSelectedItem(bodyPanel.nowDimg.getDimgFile().objectcolor);
		objectlevel.setSelectedItem(bodyPanel.nowDimg.getDimgFile().objectlevel);
		objecttype.setSelectedItem(bodyPanel.nowDimg.getDimgFile().objecttype);
		String s = bodyPanel.nowDimg.getDimgFile().objecttime;
		if (s.equals("0-1") || s.equals("10-15") || s.equals("15-16")) {
 
		} else {
			bodyPanel.nowDimg.getDimgFile().objecttime = "10-15"; 
		}
		objecttime.setSelectedItem(bodyPanel.nowDimg.getDimgFile().objecttime);
		// timeleft.setText(s[0]);
		// timeright.setText(s[1]);
		objectPRI.setText(bodyPanel.nowDimg.getDimgFile().objectPRI);
	}

	public void addLeveltypeListener(ItemListener actionListener) {
		objectlevel.addItemListener(actionListener);
	}

	public String getObjecttype() {
		return (String) objecttype.getSelectedItem();
	}

	public String getObjectcolor() {
		return (String) objectcolor.getSelectedItem();
	}

	public String getObjectlevel() {
		return (String) objectlevel.getSelectedItem();
	}

	public String getObjectPRI() {
		return objectPRI.getText();
	}

	public String getObjectTime() {
		return objecttime.getSelectedItem().toString();
	}

	public void setObjectTime(String time) {
		objecttime.setSelectedItem(time);
		reSetBodyPanel();
	}

	public void addObjectTimeListener(ItemListener actionListener) {
		// timeleft.addActionListener(actionListener);
		// timeright.addActionListener(actionListener);
		objecttime.addItemListener(actionListener);
	}

	public void setObjectType(String objecttype2) {
		objecttype.setSelectedItem(objecttype2);
	}

	public void setObjectPRI(String objectPRI2) {
		objectPRI.setText(objectPRI2);

	}

	public void setObjectLevel(String objectlevel2) {
		objectlevel.setSelectedItem(objectlevel2);
	}

	public void addObjecttypeListener(ItemListener mouseListener) {
		objecttype.addItemListener(mouseListener);
	}

	public String getObjectName() {
		return bodyPanel.nowDimg.getImageFile().getName();
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
