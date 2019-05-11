package com.mugui.ui.df;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DVerticalFlowLayout;

public class GameSelectPanel extends JPanel {
	public GameSelectPanel() {
		setLayout(new BorderLayout(0, 0));
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		JLabel label = new JLabel("检测到您的电脑可能存在以下游戏，请选择");
		label.setFont(new Font("宋体", Font.BOLD, 16));
		panel.add(label);
		panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new DVerticalFlowLayout());
	}

	private JPanel panel_1 = null;

	public void reInit(List<String> list) {
		panel_1.removeAll();
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			JPanel panel_2 = new JPanel();
			panel_1.add(panel_2);
			DButton button = new DButton((String) null, (Color) null);
			button.addActionListener(actionListener);
			button.setFont(new Font("宋体", Font.BOLD, 20));
			button.setText(iterator.next());
			panel_2.add(button);
		}
	}
	private ActionListener actionListener=null;
	public void addActionListener( ActionListener actionListener) {
		this.actionListener=actionListener;
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = -7879644717871720328L;


}
