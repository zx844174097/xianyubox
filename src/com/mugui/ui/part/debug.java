package com.mugui.ui.part;

import com.mugui.Dui.DPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;

import com.mugui.Dui.DTextField;

import javax.swing.LayoutStyle.ComponentPlacement;

import com.mugui.Dui.DButton;
import com.mugui.model.UIModel;
import com.mugui.ui.DataSave;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

public class debug extends DPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3731380536039280838L;

	public debug() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		JLabel label = new JLabel("高级调试模式（测试版）");
		panel.add(label);

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.WEST);

		JPanel panel_2 = new JPanel();

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		JLabel label_3 = new JLabel("键盘按下与释放间隔：");
		label_3.setHorizontalAlignment(SwingConstants.LEFT);
		panel_3.add(label_3);

		键盘按下与释放间隔 = new DTextField(7);
		键盘按下与释放间隔.setText("0");
		键盘按下与释放间隔.setColumns(5);
		panel_3.add(键盘按下与释放间隔);

		JLabel label_4 = new JLabel("毫秒");
		panel_3.add(label_4);

		JLabel label_5 = new JLabel("换鱼竿操作：(最好在伽马值为默认时调试)");

		JPanel panel_4 = new JPanel();
		panel_4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_6 = new JLabel("打开背包之前的前置时间");
		label_6.setHorizontalAlignment(SwingConstants.LEFT);
		panel_4.add(label_6);

		打开背包的前置时间 = new DTextField(7);
		打开背包的前置时间.setText("200");
		打开背包的前置时间.setColumns(5);
		panel_4.add(打开背包的前置时间);

		JLabel label_7 = new JLabel("毫秒");
		panel_4.add(label_7);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_8 = new JLabel("检测鱼竿的前置时间：");
		label_8.setHorizontalAlignment(SwingConstants.LEFT);
		panel_5.add(label_8);

		检测鱼竿的前置时间 = new DTextField(7);
		检测鱼竿的前置时间.setText("500");
		检测鱼竿的前置时间.setColumns(5);
		panel_5.add(检测鱼竿的前置时间);

		JLabel label_9 = new JLabel("毫秒");
		panel_5.add(label_9);

		JPanel panel_6 = new JPanel();
		panel_6.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_10 = new JLabel("鱼竿检测准确度：");
		label_10.setHorizontalAlignment(SwingConstants.LEFT);
		panel_6.add(label_10);

		鱼竿检测准确度 = new DTextField(7);
		鱼竿检测准确度.setText("0.2");
		鱼竿检测准确度.setColumns(5);
		panel_6.add(鱼竿检测准确度);

		JLabel label_11 = new JLabel("（越低越高）");
		panel_6.add(label_11);

		JPanel panel_7 = new JPanel();
		panel_7.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_12 = new JLabel("更换鱼竿的前置时间：");
		label_12.setHorizontalAlignment(SwingConstants.LEFT);
		panel_7.add(label_12);

		更换鱼竿的前置时间 = new DTextField(7);
		更换鱼竿的前置时间.setText("500");
		更换鱼竿的前置时间.setColumns(5);
		panel_7.add(更换鱼竿的前置时间);

		JLabel label_13 = new JLabel("毫秒");
		panel_7.add(label_13);

		JPanel panel_8 = new JPanel();
		panel_8.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_14 = new JLabel("关闭背包的前置时间：");
		label_14.setHorizontalAlignment(SwingConstants.LEFT);
		panel_8.add(label_14);

		关闭背包的前置时间 = new DTextField(7);
		关闭背包的前置时间.setText("200");
		关闭背包的前置时间.setColumns(5);
		panel_8.add(关闭背包的前置时间);

		JLabel lblMs = new JLabel("ms");
		panel_8.add(lblMs);

		JPanel panel_9 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_9.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		JLabel label_15 = new JLabel("查找钓鱼以及收鱼空格：");

		JPanel panel_10 = new JPanel();
		panel_10.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_16 = new JLabel("探测到钓鱼空格颜色的像素数量");
		label_16.setHorizontalAlignment(SwingConstants.LEFT);
		panel_10.add(label_16);

		钓鱼空格的像素数量 = new DTextField(7);
		钓鱼空格的像素数量.setText("300");
		钓鱼空格的像素数量.setColumns(5);
		panel_10.add(钓鱼空格的像素数量);

		JLabel label_17 = new JLabel("个");
		panel_10.add(label_17);

		JPanel panel_11 = new JPanel();
		panel_11.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_20 = new JLabel("钓鱼空格按下时间（可控制钓鱼使用力气）");
		label_20.setHorizontalAlignment(SwingConstants.LEFT);
		panel_11.add(label_20);

		钓鱼空格的按下时间长度 = new DTextField(7);
		钓鱼空格的按下时间长度.setEnabled(false);
		钓鱼空格的按下时间长度.setText("1800");
		钓鱼空格的按下时间长度.setColumns(5);
		panel_11.add(钓鱼空格的按下时间长度);

		JPanel panel_12 = new JPanel();
		panel_12.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_23 = new JLabel("蓝色条识别时间：");

		JPanel panel_13 = new JPanel();
		panel_13.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

		JLabel label_24 = new JLabel("收鱼蓝色条（蓝）扫描次数");
		label_24.setHorizontalAlignment(SwingConstants.LEFT);
		panel_13.add(label_24);

		收鱼蓝色条扫描次数 = new DTextField(7);
		收鱼蓝色条扫描次数.setText("200");
		收鱼蓝色条扫描次数.setColumns(5);
		panel_13.add(收鱼蓝色条扫描次数);

		JLabel label_25 = new JLabel("次");
		panel_13.add(label_25);

		JPanel panel_14 = new JPanel();
		panel_14.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

		JLabel label_26 = new JLabel("上下左右验证识别前置时间：");
		label_26.setHorizontalAlignment(SwingConstants.LEFT);
		panel_14.add(label_26);

		验证码识别前置时间 = new DTextField(7);
		验证码识别前置时间.setText("1500");
		验证码识别前置时间.setColumns(5);
		panel_14.add(验证码识别前置时间);

		JLabel lblMs_2 = new JLabel("ms");
		panel_14.add(lblMs_2);

		JPanel panel_19 = new JPanel();
		panel_19.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_35 = new JLabel("验证码输入时间隔时间：");
		label_35.setHorizontalAlignment(SwingConstants.LEFT);
		panel_19.add(label_35);

		验证码输入间隔时间 = new DTextField(7);
		验证码输入间隔时间.setText("50");
		验证码输入间隔时间.setColumns(5);
		panel_19.add(验证码输入间隔时间);

		JLabel label_36 = new JLabel("ms");
		panel_19.add(label_36);

		JPanel panel_20 = new JPanel();
		panel_20.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_37 = new JLabel("拾取鱼的前置时间：");
		label_37.setHorizontalAlignment(SwingConstants.LEFT);
		panel_20.add(label_37);

		收鱼前置时间 = new DTextField(7);
		收鱼前置时间.setText("4000");
		收鱼前置时间.setColumns(5);
		panel_20.add(收鱼前置时间);

		JLabel lblMs_3 = new JLabel("ms");
		panel_20.add(lblMs_3);

		JPanel panel_21 = new JPanel();
		panel_21.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_39 = new JLabel("鱼种类判断前置时间");
		label_39.setHorizontalAlignment(SwingConstants.LEFT);
		panel_21.add(label_39);

		鱼种类判断前置时间 = new DTextField(7);
		鱼种类判断前置时间.setText("1500");
		鱼种类判断前置时间.setColumns(5);
		panel_21.add(鱼种类判断前置时间);

		JLabel label_40 = new JLabel("ms");
		panel_21.add(label_40);

		JPanel panel_22 = new JPanel();
		panel_22.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_38 = new JLabel("拾取鱼鼠标右键点击前置时间:");
		label_38.setHorizontalAlignment(SwingConstants.LEFT);
		panel_22.add(label_38);

		拾取鱼鼠标右键点击前置时间 = new DTextField(7);
		拾取鱼鼠标右键点击前置时间.setText("300");
		拾取鱼鼠标右键点击前置时间.setColumns(5);
		panel_22.add(拾取鱼鼠标右键点击前置时间);

		JLabel label_41 = new JLabel("ms");
		panel_22.add(label_41);

		JPanel panel_23 = new JPanel();
		panel_23.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_42 = new JLabel("拾取鱼鼠标右键点击后置时间");
		label_42.setHorizontalAlignment(SwingConstants.LEFT);
		panel_23.add(label_42);

		拾取鱼鼠标右键点击后置时间 = new DTextField(7);
		拾取鱼鼠标右键点击后置时间.setText("500");
		拾取鱼鼠标右键点击后置时间.setColumns(5);
		panel_23.add(拾取鱼鼠标右键点击后置时间);

		JLabel label_43 = new JLabel("ms");
		panel_23.add(label_43);

		JPanel panel_24 = new JPanel();
		panel_24.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_44 = new JLabel("调整姿势的前置时间：");
		panel_24.add(label_44);
		label_44.setHorizontalAlignment(SwingConstants.LEFT);

		调整姿势的前置时间 = new DTextField(7);
		调整姿势的前置时间.setText("300");
		调整姿势的前置时间.setColumns(5);
		panel_24.add(调整姿势的前置时间);

		JLabel label_45 = new JLabel("ms");
		panel_24.add(label_45);

		JLabel label_46 = new JLabel("收鱼阶段：");

		JPanel panel_15 = new JPanel();
		panel_15.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

		JLabel label_28 = new JLabel("收鱼蓝色条（红）准确度");
		label_28.setHorizontalAlignment(SwingConstants.LEFT);
		panel_15.add(label_28);

		收鱼蓝色条红准确度 = new DTextField(7);
		收鱼蓝色条红准确度.setText("300");
		收鱼蓝色条红准确度.setColumns(5);
		panel_15.add(收鱼蓝色条红准确度);

		JLabel label_47 = new JLabel("ms");
		panel_15.add(label_47);

		JLabel label_22 = new JLabel("额外设置：");

		JPanel panel_18 = new JPanel();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(label_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 1739, Short.MAX_VALUE).addGap(0))
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 1739, Short.MAX_VALUE).addGap(14))
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 1731, Short.MAX_VALUE).addContainerGap())
				.addGroup(gl_panel_1.createSequentialGroup()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 224,
										GroupLayout.PREFERRED_SIZE))
								.addComponent(label_15))
						.addContainerGap(1747, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
										.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
												.addComponent(panel_12, GroupLayout.PREFERRED_SIZE, 279, GroupLayout.PREFERRED_SIZE)
												.addComponent(panel_11, GroupLayout.DEFAULT_SIZE, 1195, Short.MAX_VALUE)))
								.addComponent(label_23))
						.addGap(776))
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, 436, GroupLayout.PREFERRED_SIZE).addContainerGap(1535, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addComponent(panel_21, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(panel_20, GroupLayout.PREFERRED_SIZE, 247, GroupLayout.PREFERRED_SIZE).addContainerGap(1490, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addComponent(panel_24, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE).addContainerGap(1699, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(panel_9, GroupLayout.DEFAULT_SIZE, 1961, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(panel_13, GroupLayout.DEFAULT_SIZE, 1961, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_14, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 963, Short.MAX_VALUE)
								.addComponent(panel_19, GroupLayout.DEFAULT_SIZE, 963, Short.MAX_VALUE))
						.addGap(1008))
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addComponent(panel_15, GroupLayout.PREFERRED_SIZE, 517, GroupLayout.PREFERRED_SIZE).addContainerGap(1454, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup().addComponent(label_46, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(1875, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup().addComponent(label_22, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_18, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(Alignment.LEADING,
										gl_panel_1.createSequentialGroup().addComponent(panel_22, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(panel_23, GroupLayout.PREFERRED_SIZE, 273, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(1432, Short.MAX_VALUE)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup()
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_5).addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_15).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_11, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_12, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(label_23).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_13, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_15, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE).addGap(20)
				.addComponent(label_46).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_14, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_19, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(panel_21, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_20, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(panel_22, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_23, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_24, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_22).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_18, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)));
		panel_18.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		图像截图 = new JRadioButton("另一种图像截取方式");
		图像截图.setSelected(true);
		panel_18.add(图像截图);
		
		低配模式 = new JRadioButton("低配模式(某些功能失效)");
		panel_18.add(低配模式);

		鼠标修正 = new JRadioButton("鼠标修正");
		panel_3.add(鼠标修正);

		JPanel panel_17 = new JPanel();
		panel_15.add(panel_17);
		panel_17.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_48 = new JLabel("（红）按下空格前置时间：");
		label_48.setHorizontalAlignment(SwingConstants.LEFT);
		panel_17.add(label_48);

		红按下空格前置时间 = new DTextField(7);
		红按下空格前置时间.setText("230");
		红按下空格前置时间.setColumns(5);
		panel_17.add(红按下空格前置时间);

		JLabel label_49 = new JLabel("ms");
		panel_17.add(label_49);

		JPanel panel_16 = new JPanel();
		panel_14.add(panel_16);
		panel_16.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_29 = new JLabel("识别次数：");
		label_29.setHorizontalAlignment(SwingConstants.LEFT);
		panel_16.add(label_29);

		验证码识别次数 = new DTextField(7);
		验证码识别次数.setText("200");
		验证码识别次数.setColumns(5);
		panel_16.add(验证码识别次数);

		JLabel label_30 = new JLabel("次");
		panel_16.add(label_30);

		JLabel label_31 = new JLabel("识别准确度：");
		panel_16.add(label_31);
		label_31.setHorizontalAlignment(SwingConstants.LEFT);

		验证码识别准确度 = new DTextField(7);
		panel_16.add(验证码识别准确度);
		验证码识别准确度.setText("0.1");
		验证码识别准确度.setColumns(5);

		JLabel label_27 = new JLabel("准确度：");
		panel_13.add(label_27);
		label_27.setHorizontalAlignment(SwingConstants.LEFT);

		收鱼蓝色条准确度 = new DTextField(7);
		panel_13.add(收鱼蓝色条准确度);
		收鱼蓝色条准确度.setText("0.1");
		收鱼蓝色条准确度.setColumns(5);

		JLabel label_33 = new JLabel("像素数量：");
		panel_13.add(label_33);
		label_33.setHorizontalAlignment(SwingConstants.LEFT);

		收鱼蓝色条扫描像素数量 = new DTextField(7);
		panel_13.add(收鱼蓝色条扫描像素数量);
		收鱼蓝色条扫描像素数量.setText("5");
		收鱼蓝色条扫描像素数量.setColumns(5);

		JLabel label_34 = new JLabel("ms");
		panel_13.add(label_34);

		JLabel lblms = new JLabel("(你可以试试最短输入时间，不妨改成0ms)");
		panel_19.add(lblms);

		JLabel lblMs_4 = new JLabel("ms(舍弃)");
		panel_11.add(lblMs_4);

		JLabel label_21 = new JLabel("按下收鱼空格的前置时间：");
		panel_12.add(label_21);
		label_21.setHorizontalAlignment(SwingConstants.LEFT);

		按下收鱼空格的前置时间 = new DTextField(7);
		panel_12.add(按下收鱼空格的前置时间);
		按下收鱼空格的前置时间.setText("300");
		按下收鱼空格的前置时间.setColumns(5);

		JLabel lblMs_1 = new JLabel("ms");
		panel_12.add(lblMs_1);

		JLabel label_18 = new JLabel("  准确度");
		panel_10.add(label_18);

		钓鱼空格的准确度 = new DTextField(7);
		钓鱼空格的准确度.setText("0.15");
		钓鱼空格的准确度.setColumns(5);
		panel_10.add(钓鱼空格的准确度);

		JLabel label_19 = new JLabel("（越低越高）");
		panel_10.add(label_19);

		DButton button = new DButton((String) null, Color.WHITE);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (DataSave.data_1.exists())
					DataSave.data_1.delete();
				DataSave.datainit();
				datainit();
			}
		});
		button.setText("恢复默认");
		panel_9.add(button);

		DButton button_1 = new DButton((String) null, Color.WHITE);
		button_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.savedata();
				DataSave.datainit();
				datainit();
			}
		});
		button_1.setText("保存");
		panel_9.add(button_1);

		DButton button_2 = new DButton((String) null, Color.WHITE);
		button_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIModel.setUI(DataSave.bodyPanel);
			}
		});
		button_2.setText("返回");
		panel_9.add(button_2);
		panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel label_1 = new JLabel("鼠标左右键按下与释放间隔：");
		label_1.setHorizontalAlignment(SwingConstants.LEFT);
		panel_2.add(label_1);
		鼠标按下与释放时间 = new DTextField(7);
		鼠标按下与释放时间.setColumns(5);
		鼠标按下与释放时间.setText("100");
		panel_2.add(鼠标按下与释放时间);

		JLabel label_2 = new JLabel("毫秒");
		panel_2.add(label_2);
		panel_1.setLayout(gl_panel_1);
	}

	public DTextField 鼠标按下与释放时间 = null;
	public DTextField 键盘按下与释放间隔 = null;
	public DTextField 打开背包的前置时间 = null;
	public DTextField 检测鱼竿的前置时间 = null;
	public DTextField 鱼竿检测准确度 = null;
	public DTextField 更换鱼竿的前置时间 = null;
	public DTextField 关闭背包的前置时间 = null;
	public DTextField 钓鱼空格的像素数量 = null;
	public DTextField 钓鱼空格的准确度 = null;
	public DTextField 钓鱼空格的按下时间长度 = null;
	public DTextField 按下收鱼空格的前置时间 = null;
	public DTextField 收鱼蓝色条扫描次数 = null;
	public DTextField 收鱼蓝色条准确度 = null;
	public DTextField 验证码识别前置时间 = null;
	public DTextField 验证码识别次数 = null;
	public DTextField 验证码识别准确度 = null;
	public DTextField 验证码输入间隔时间 = null;
	public DTextField 收鱼前置时间 = null;
	public DTextField 收鱼蓝色条扫描像素数量 = null;
	public DTextField 鱼种类判断前置时间 = null;
	public DTextField 拾取鱼鼠标右键点击前置时间 = null;
	public DTextField 拾取鱼鼠标右键点击后置时间 = null;
	public DTextField 调整姿势的前置时间 = null;
	public DTextField 收鱼蓝色条红准确度 = null;
	public DTextField 红按下空格前置时间 = null;
	public JRadioButton 鼠标修正 = null;
	public JRadioButton 图像截图 = null;
	public JRadioButton 低配模式=null;
	public void datainit() {
		鼠标按下与释放时间.setText(DataSave.配置数组[0]);
		键盘按下与释放间隔.setText(DataSave.配置数组[1]);
		打开背包的前置时间.setText(DataSave.配置数组[2]);
		检测鱼竿的前置时间.setText(DataSave.配置数组[3]);
		鱼竿检测准确度.setText(DataSave.配置数组[4]);
		更换鱼竿的前置时间.setText(DataSave.配置数组[5]);
		关闭背包的前置时间.setText(DataSave.配置数组[6]);
		钓鱼空格的像素数量.setText(DataSave.配置数组[7]);
		钓鱼空格的准确度.setText(DataSave.配置数组[8]);
		钓鱼空格的按下时间长度.setText(DataSave.配置数组[9]);
		按下收鱼空格的前置时间.setText(DataSave.配置数组[10]);
		收鱼蓝色条扫描次数.setText(DataSave.配置数组[11]);
		收鱼蓝色条准确度.setText(DataSave.配置数组[12]);
		验证码识别前置时间.setText(DataSave.配置数组[13]);
		验证码识别次数.setText(DataSave.配置数组[14]);
		验证码识别准确度.setText(DataSave.配置数组[15]);
		验证码输入间隔时间.setText(DataSave.配置数组[16]);
		收鱼前置时间.setText(DataSave.配置数组[17]);
		收鱼蓝色条扫描像素数量.setText(DataSave.配置数组[18]);
		鱼种类判断前置时间.setText(DataSave.配置数组[19]);
		拾取鱼鼠标右键点击前置时间.setText(DataSave.配置数组[20]);
		拾取鱼鼠标右键点击后置时间.setText(DataSave.配置数组[21]);
		调整姿势的前置时间.setText(DataSave.配置数组[22]);
		收鱼蓝色条红准确度.setText(DataSave.配置数组[23]);
		红按下空格前置时间.setText(DataSave.配置数组[24]);
		鼠标修正.setSelected(DataSave.鼠标修正);
		图像截图.setSelected(DataSave.图像截图);
		低配模式.setSelected(DataSave.低配模式);
	}

	private Dimension now_Dimension = null;
	private Point now_Point = null;

	@Override
	public void init() {
		DataSave.StaticUI.updateTitle("钓鱼高级设置");
		now_Point = DataSave.StaticUI.getLocation();
		now_Dimension = DataSave.StaticUI.getSize();
		DataSave.StaticUI.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 600 / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 750 / 2);
		datainit();
		DataSave.StaticUI.setSize(600, 750);
	}

	@Override
	public void quit() {
		DataSave.StaticUI.setLocation(now_Point);
		DataSave.StaticUI.setSize(now_Dimension);
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
