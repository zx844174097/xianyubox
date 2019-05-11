package com.mugui.ui.df;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LinLabel extends JPanel {
	/**
		 * 
		 */
	private static final long serialVersionUID = -5483455168180879115L;
	int index = -1;
	int item_index=-1;
	long dianji_time = 0;
	long shuju_time = 0;

	/**
	 * @wbp.parser.constructor
	 */
	public LinLabel(String string, ImageIcon imageIcon) {

		init(string, imageIcon);

	}

	private void init(String string, ImageIcon object) {
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		lblNewLabel = new JLabel(string);
		add(lblNewLabel);
		lblNewLabel_1 = new JLabel(object);
		add(lblNewLabel_1);
		time = new JLabel("");
		add(time);
	}

	private JLabel lblNewLabel, lblNewLabel_1, time;
	Date currentTime = new Date();
	SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm:ss");

	public void setText(String string) {
		lblNewLabel.setText(string);
	}

	public void setTime(long time) {
		String dateString = formatter.format(time);
		this.time.setText(dateString);
	}

	public void setIcon(ImageIcon imageIcon) {
		lblNewLabel_1.setIcon(imageIcon);
	}
}