package com.mugui.ui.part;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mugui.Dui.DTextField;
import com.mugui.tool.Other;

import javax.swing.JCheckBox;
import java.awt.FlowLayout;

public class DSThreadPanel extends JPanel {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1628144778201494177L;

	public DSThreadPanel() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);

		checkBox = new JCheckBox("定时:");
		checkBox.setFont(new Font("宋体", Font.PLAIN, 14));
		add(checkBox);
		textField_1 = new DTextField(10);
		this.add(textField_1);
		textField_1.setFont(new Font("宋体", Font.BOLD, 14));
		JLabel label_2 = new JLabel("秒 ");
		this.add(label_2);
		label_2.setFont(new Font("宋体", Font.PLAIN, 14));
		textField_1.setColumns(4);
		button = new DTextField(20);
		button.setText("");
		this.add(button);
		button.setFont(new Font("宋体", Font.BOLD, 14));
		button.setColumns(6);
		button.setKeyCodeListener(true); 
		JLabel label = new JLabel("按键:");
		this.add(label);
		label.setFont(new Font("宋体", Font.PLAIN, 14));
	}

	private DTextField textField_1 = null;
	private DTextField button = null;
	private JCheckBox checkBox = null;
	long nexttime = 0;

	public String getButton() {
		return button.getText();
	}
	public boolean isSelect() {
		return checkBox.isSelected();
	}

	public void setNextTime() {
		String s = textField_1.getText().trim();
		if (Other.isDouble(s)) {
			long l = (long) Double.parseDouble(s) * 1000;
			nexttime = System.currentTimeMillis() + l;
		}
	}

	public void setNextTime(String time) {
		textField_1.setText(time);
	}

	public long getNextTime() {
		return nexttime;
	}

	public String getNextTime2() {
		return textField_1.getText().trim();
	}
	public void setButton(String string) {
		button.setText(string);
	}



}
