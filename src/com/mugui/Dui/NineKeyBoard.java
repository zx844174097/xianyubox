package com.mugui.Dui;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NineKeyBoard extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5378549441070226996L;

	public NineKeyBoard() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		title = new JLabel("\u6807\u9898");
		title.setFont(new Font("宋体", Font.BOLD, 18));

		DButton button = new DButton("\u6E05\u9664", Color.black);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTextField("");
			}
		});
		button.setFont(new Font("宋体", Font.BOLD, 25));

		unit = new JLabel("ml");
		unit.setFont(new Font("宋体", Font.BOLD, 25));

		 textField = new DTextField(10);
		textField.setFont(new Font("宋体", Font.PLAIN, 17));
		textField.setColumns(7);
		textField.setText("");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(title, GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(unit, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(button, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
					.addComponent(button, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
					.addComponent(unit, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 52, Short.MAX_VALUE))
				.addComponent(title, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
		);
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 1, 1, 1, 1 };
		gbl_panel_1.rowHeights = new int[] { 1, 1, 1, 1 };
		gbl_panel_1.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		gbl_panel_1.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		panel_1.setLayout(gbl_panel_1);

		DButton[] buttons = new DButton[10];
		for (int i = 1; i <= 10; i++) {
			buttons[i - 1] = new DButton(i % 10 + "", Color.white);
			buttons[i - 1].setFont(new Font("Dialog", Font.BOLD, 30));
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = (i-1) % 3;
			gbc.gridy =(i -1) / 3;
			panel_1.add(buttons[i - 1], gbc);
			buttons[i-1].addActionListener(al);
		}
		
		DButton button_11 = new DButton((String) null, Color.white);
		button_11.setFont(new Font("Dialog", Font.BOLD, 30));
		button_11.setText("●");
		button_11.setActionCommand(".");
		GridBagConstraints gbc_button_11 = new GridBagConstraints();
		gbc_button_11.fill = GridBagConstraints.BOTH;
		gbc_button_11.gridwidth = 2;
		gbc_button_11.gridx = 1;
		gbc_button_11.gridy = 3;
		panel_1.add(button_11, gbc_button_11);
		button_11.addActionListener(al);

		trueButton = new DButton((String) null, (Color) null);
		trueButton.setFont(new Font("Dialog", Font.BOLD, 30));
		trueButton.setText("<html>确<br>认</html>");
		trueButton.setActionCommand("true");
		GridBagConstraints gbc_button_12 = new GridBagConstraints();
		gbc_button_12.fill = GridBagConstraints.BOTH;
		gbc_button_12.gridheight = 2;
		gbc_button_12.gridx = 3;
		gbc_button_12.gridy = 0;
		panel_1.add(trueButton, gbc_button_12);

		falseButton = new DButton((String) null, (Color) null);
		falseButton.setFont(new Font("Dialog", Font.BOLD, 30));
		falseButton.setText("<html>取<br>消</html>");
		falseButton.setActionCommand("false");
		GridBagConstraints gbc_button_13 = new GridBagConstraints();
		gbc_button_13.fill = GridBagConstraints.BOTH;
		gbc_button_13.gridheight = 2;
		gbc_button_13.gridx = 3;
		gbc_button_13.gridy = 2;
		panel_1.add(falseButton, gbc_button_13);

	}

	private JLabel title = null;
	private JLabel unit = null;

	public void setTitle(String title) {
		this.title.setText(title);
	}

	public void setUnit(String unit) {
		this.unit.setText(unit);
	}

	private DButton trueButton = null;
	private DButton falseButton = null;

	public void addActionListener(ActionListener actionListener) { 
		trueButton.addActionListener(actionListener);
		falseButton.addActionListener(actionListener);
	}
	private DTextField textField=null;
	public void setTextField(String text){
		textField.setText(text);
	}
	public String getTextField(){
		return textField.getText();
	}
	private ActionListener al = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals(".")){
				if(getTextField().indexOf(".")>=0) return ;
				else 
					setTextField(textField.getText() +".");
				return ;
			}
			setTextField(textField.getText() + e.getActionCommand());
		}
	};

}
