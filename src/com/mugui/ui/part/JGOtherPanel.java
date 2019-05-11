package com.mugui.ui.part;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import com.mugui.Dui.DButton;
import com.mugui.http.Bean.JGOtherBean;
import com.mugui.http.Bean.JGOtherBean.JGBean;
import com.mugui.ui.DataSave;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

public class JGOtherPanel extends JPanel {
	public JGOtherPanel() {
		this("加工");
	}

	public JGOtherPanel(String model) {
		this(new JGOtherBean(), model);
	}

	private String model = "加工";

	public JGOtherPanel(JGOtherBean bean, final String model) {
		this.model = model;
		setLayout(new BorderLayout(0, 0));
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		add(panel, BorderLayout.NORTH);

		label = new JLabel(model + "序列号:");
		label.setFont(new Font("宋体", Font.BOLD, 14));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.GRAY);
		if (model.equals("加工")) {
			JLabel label_1 = new JLabel("模式:");
			label_1.setFont(new Font("宋体", Font.BOLD, 16));
			panel_1.add(label_1);
			comboBox = new JComboBox<String>();
			comboBox.setModel(new DefaultComboBoxModel<String>(jgStrings));
			panel_1.add(comboBox);
		}
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel.add(label);
		panel.add(panel_1);

		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JGOtherView view = new JGOtherView(model, JGOtherPanel.this);
				body.add(view);
				jgOtherBean.putBody(view.getJGBean());
				validate();
				repaint();
				DataSave.jg.list.validate();
				DataSave.jg.list.repaint();
			}
		});
		button_1.setText("添加");
		button_1.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(button_1);

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (now_view != null) {
					body.remove(now_view);
					jgOtherBean.removeBody(now_view.getJGBean());
					validate();
					repaint();
					DataSave.jg.list.validate();
					DataSave.jg.list.repaint();
				}
			}
		});
		button_2.setText("删除");
		button_2.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(button_2);

		button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel panel = DataSave.bodyPanel.getUIOne();
				if (panel == DataSave.jg)
					DataSave.jg.list.removeQPOtherPanel(JGOtherPanel.this);
				else if (panel == DataSave.lj)
					DataSave.lj.list.removeQPOtherPanel(JGOtherPanel.this);
			}
		});
		button.setFont(new Font("Dialog", Font.BOLD, 14));
		button.setText("彻底移除");
		panel.add(button);

		label_2 = new JLabel("当前状态：");
		add(label_2, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(560, 150));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBackground(null);
		scrollPane.setOpaque(false);
		DataSave.bodyPanel.全体透明(scrollPane);
		body = new JPanel();
		body.setPreferredSize(new Dimension(320, 500));
		body.setBackground(null);
		body.setOpaque(false);
		scrollPane.setViewportView(body);
		FlowLayout flowLayout_1 = (FlowLayout) body.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		DataInit(bean);
		this.setBackground(null);
		this.setOpaque(false);
	}

	public JComboBox<String> comboBox = null;
	private DButton button = null;
	private JPanel body = null;
	private JLabel label = null;
	private JLabel label_2 = null;
	public JGOtherView now_view = null;
	public final static String[] jgStrings = { "混合", "研磨", "劈柴", "晒干", "稀释", "加热", "锻造", "炼金", "料理", "皇室料理包装", "皇室炼金包装", "长得像盾牌" };

	/**
	 * 
	 */
	public void DataInit(JGOtherBean jgOtherBean) {
		this.jgOtherBean = jgOtherBean;
		Iterator<JGBean> iterator = jgOtherBean.body.iterator();
		while (iterator.hasNext()) {
			JGOtherView view = new JGOtherView(iterator.next(), JGOtherPanel.this);
			body.add(view);
		}
	}

	private JGOtherBean jgOtherBean = null;
	private static final long serialVersionUID = -2668157110248874134L;
	private String numer = null;

	public void setNumber(String numer) {
		this.numer = numer;
		label.setText(model + "序列号:" + numer);
	}

	public String getNumber() {
		return numer;
	}

	public String getJGModel() {
		if (comboBox == null) {
			return "未知";
		}
		return (String) comboBox.getSelectedItem();
	}

	public void setJGModel(String s) {
		if (comboBox == null) {
			return;
		}
		if (!s.equals("未知"))
			comboBox.setSelectedItem(s);
	}

	public void setStateText(String string) {
		label_2.setText("当前状态：" + string);
	}

	public JGOtherBean getJGOtherBean() {
		return jgOtherBean;
	}

}
