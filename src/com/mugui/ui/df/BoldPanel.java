package com.mugui.ui.df;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DVerticalFlowLayout;
import com.mugui.http.Bean.FishBean;
import com.mugui.model.FishPrice;
import com.mugui.model.UIModel;
import com.mugui.ui.DataSave;

import javax.swing.JRadioButton;

public class BoldPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 47356708136477910L;

	public BoldPanel() {
		setLayout(new BorderLayout(0, 0));
		panel_1 = new JPanel();
		add(panel_1, BorderLayout.NORTH);
		DVerticalFlowLayout dvfl_panel_1 = new DVerticalFlowLayout();
		dvfl_panel_1.setHgap(0);
		dvfl_panel_1.setVgap(0);
		panel_1.setLayout(dvfl_panel_1);

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel_1.add(panel_4);

		xianlu_id = new JLabel("自身线路:");
		xianlu_id.setFont(new Font("宋体", Font.BOLD, 16));
		panel_4.add(xianlu_id);

		final JRadioButton radioButton = new JRadioButton("快捷窗口");
		radioButton.setSelected(DataSave.黄金钟快捷窗口);
		radioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DataSave.黄金钟快捷窗口=radioButton.isSelected();
				DataSave.savedata();
				BoldShortCutPanel.start(); 
			}
		});
		panel_4.add(radioButton);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		panel_1.add(panel_3);

		JLabel label = new JLabel("当前黄金钟线路列表：");
		label.setFont(new Font("宋体", Font.PLAIN, 15));
		panel_3.add(label);

		DButton button_2 = new DButton((String) null, (Color) null);
		panel_3.add(button_2);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initall();
			}
		});
		button_2.setText("刷新");
		button_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_2.setBackground(Color.GRAY);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_1.add(panel_2);

		newDope = new JLabel("");
		panel_2.add(newDope);
		body = new JPanel();
		JScrollPane scrollPane = new JScrollPane(body);
		body.setLayout(new DVerticalFlowLayout());
		add(scrollPane, BorderLayout.CENTER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
	}

	private JPanel body = null;
	private JLabel newDope = null;
	private JPanel panel_1 = null;
	private JLabel xianlu_id = null;

	public void reSetXianlu_id() {
		xianlu_id.setText("自身线路:" + DataSave.D_LINE_ID);
	}

	public void updateNewDope(String text) {
		newDope.setText(text);
	}

	long time = 0;

	public void initall() {
		if ( DataSave.D_LINE_ID <= -1)
			return;
		initLine();
		if (System.currentTimeMillis() - time < 10 * 1000)
			return;
		time = System.currentTimeMillis();
		UIModel.getBoldLines();
		FishPriceFrame.frame.validate();
		FishPriceFrame.frame.repaint();
	}

	public void initBody(LinkedList<FishBean> beans) {
		if (beans == null)
			return;
		body.removeAll();
		Iterator<FishBean> iterator = beans.iterator();
		while (iterator.hasNext()) {
			FishBean bean = iterator.next();
			body.add(new FishOnePrice(bean, 2));
		}
		body.validate();
		body.repaint();
		validate();
		repaint();
		FishPriceFrame.frame.validate();
		FishPriceFrame.frame.repaint();
	}

	public void initLine() {
		xianlu_id.setText("自身线路：" +  DataSave.D_LINE_ID);
	}

	public void initBody() {
		initBody(FishPrice.getBoldAll());
		FishPriceFrame.frame.validate();
		FishPriceFrame.frame.repaint();
	}

}
