package com.mugui.ui.df;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import com.mugui.Dui.DButton;
import com.mugui.http.Bean.FishBean;
import com.mugui.model.FishModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;

public class FishOnePrice extends JPanel {
	private FishBean bean = null;

	/**
	 * @wbp.parser.constructor
	 */
	public FishOnePrice(FishBean bean) {
		this(bean, 0);
	}

	public FishOnePrice(FishBean bean, final int i) {
		this.bean = bean;
		setBackground(null);
		setLayout(new BorderLayout(0, 0));
		JLabel lblNewLabel = null;
		if (bean.getFish_img() == null) {
		} else {
			lblNewLabel = new JLabel(new ImageIcon(bean.getFish_img()));
			add(lblNewLabel, BorderLayout.WEST);
		}
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(2, 0, 0, 0));
		panel.setBackground(null);
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(null);
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.add(panel_1);
		JLabel lblNewLabel_1 = null;
		if (i == 0) {
			if ((bean.getFish_name() == null || bean.getFish_name().trim().equals(""))) {
				lblNewLabel_1 = new JLabel(new ImageIcon(bean.getFish_name_img()));
			} else {
				lblNewLabel_1 = new JLabel(bean.getFish_name());
			}
		} else if (i == 1) {
			lblNewLabel_1 = new JLabel(bean.getFish_name());
		} else if (i == 2) {
			if (bean.getFish_name_img() != null) {
				lblNewLabel_1 = new JLabel(new ImageIcon(bean.getFish_name_img()));
			}
		}
		if (lblNewLabel_1 != null)
			panel_1.add(lblNewLabel_1);
		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (i == 0)
					FishModel.saveFishBean(FishOnePrice.this.bean);
				else if (i == 1) {
					FishModel.delFishBean(FishOnePrice.this.bean);
					FishPriceFrame.frame.fishPanel.initBody(FishModel.getSaveFishBeans());
				}
			}
		});
		if (i == 0)
			button_1.setText("加入收藏夹");
		else if (i == 1)
			button_1.setText("移出收藏夹");
		else if (i == 2) {
			button_1.setPreferredSize(new Dimension(0, 0));
		}
		button_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_1.setBackground(Color.GRAY);
		panel_1.add(button_1);
		if (i == 0) {
			JLabel lblNewLabel_2 = new JLabel("当前鱼价：" + bean.getFish_price() + "%");
			panel.add(lblNewLabel_2);
		} else if (i == 2) {
			JLabel lblNewLabel_2 = new JLabel("时间：" + new Date(bean.getFish_price()).toLocaleString());
			panel.add(lblNewLabel_2);
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3854570700611886404L;

}
