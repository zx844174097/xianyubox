package com.mugui.ui.df;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.mugui.Dui.DVerticalFlowLayout;

import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;

public class BossPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1450373916736302633L;

	public BossPanel(String url, String name, String last_time, String next_time) {
		setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 14));
		lblNewLabel.setText("<html><img height='50' width='50' src='" + url + "' />" + name + "</html>");
		lblNewLabel.setPreferredSize(new Dimension(100, 50));
		lblNewLabel.setForeground(Color.red);
		add(lblNewLabel, BorderLayout.WEST);
		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new DVerticalFlowLayout());

		JLabel lblNewLabel_1 = new JLabel(last_time);
		lblNewLabel_1.setFont(new Font("宋体", Font.BOLD, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel(next_time);
		lblNewLabel_2.setFont(new Font("宋体", Font.BOLD, 14));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setForeground(SystemColor.activeCaption);
		lblNewLabel_1.setForeground(SystemColor.inactiveCaption);
		panel.add(lblNewLabel_2);
		setBackground(null);
		panel.setBackground(null);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public BossPanel(BufferedImage bufferedImage, Object name, String last_time, String next_time) {
		setLayout(new BorderLayout(0, 0));
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setIcon(new ImageIcon(bufferedImage));
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 14));
		lblNewLabel.setPreferredSize(new Dimension(100, 50));
		lblNewLabel.setForeground(Color.red);
		add(lblNewLabel, BorderLayout.NORTH);
		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new DVerticalFlowLayout());

		JLabel lblNewLabel_1 = new JLabel(last_time);
		lblNewLabel_1.setFont(new Font("宋体", Font.BOLD, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel(next_time);
		lblNewLabel_2.setFont(new Font("宋体", Font.BOLD, 14));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setForeground(SystemColor.activeCaption);
		lblNewLabel_1.setForeground(SystemColor.inactiveCaption);
		panel.add(lblNewLabel_2);
		setBackground(null);
		panel.setBackground(null);

	}

	private JPanel panel = null;
}
