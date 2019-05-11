package com.mugui.ui.df;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DTextField;
import com.mugui.Dui.DVerticalFlowLayout;
import com.mugui.http.Bean.FishBean;
import com.mugui.model.FishModel;
import com.mugui.model.FishPrice;
import com.mugui.model.FishPrice.YuAllBody;
import com.mugui.model.FishPrice.YuBody;
import com.mugui.model.UIModel;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

public class FishPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 47356708136477910L;

	public FishPanel() {
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
		panel_4.add(xianlu_id);

		JLabel label_1 = new JLabel("过滤：");
		panel_4.add(label_1);

		yujiaguolv = new DTextField(4);
		yujiaguolv.setText("0");
		yujiaguolv.setColumns(4);
		panel_4.add(yujiaguolv);

		JLabel label_2 = new JLabel("%");
		label_2.setFont(new Font("宋体", Font.PLAIN, 18));
		panel_4.add(label_2);

		DButton button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initBody((LinLabel) comboBox.getSelectedItem());
			}
		});
		button.setText("确定");
		button.setFont(new Font("Dialog", Font.PLAIN, 12));
		button.setBackground(Color.GRAY);
		panel_4.add(button);

		yuguolv = new JRadioButton("只显示收藏夹");
		panel_4.add(yuguolv);
		yuguolv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initBody((LinLabel) comboBox.getSelectedItem());
			}
		});

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		panel_1.add(panel_3);

		JLabel label = new JLabel("线路:");
		panel_3.add(label);

		comboBox = new JComboBox<LinLabel>();
		panel_3.add(comboBox);
		comboBox.setRenderer(new FishTitleLabel());
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (comboBox_bool)
					initBody((LinLabel) comboBox.getSelectedItem());
			}
		});
		comboBox.setPreferredSize(new Dimension(250, 31));

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_1.add(panel_2);

		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LinkedList<FishBean> beans = FishModel.getSaveFishBeans();
				if (beans != null) {
					initBody(beans);
				}
			}

		});
		panel_2.add(button_1);
		button_1.setText("管理已收藏");
		button_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_1.setBackground(Color.GRAY);

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FishModel.reStart();
			}
		});
		button_2.setText("再次上传");
		button_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_2.setBackground(Color.GRAY);
		panel_2.add(button_2);

		newDope = new JLabel("");
		panel_2.add(newDope);

		JPanel panel = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		flowLayout_3.setVgap(0);
		flowLayout_3.setHgap(0);
		panel_1.add(panel);

		JLabel label_3 = new JLabel("提醒:黑色为左侧贸易点。白色为右侧");
		panel.add(label_3);
		body = new JPanel();
		DVerticalFlowLayout dvfl_body = new DVerticalFlowLayout();
		dvfl_body.setVgap(0);
		dvfl_body.setHgap(0);
		dvfl_body.setAlignment(FlowLayout.LEADING);
		body.setLayout(dvfl_body);
		JScrollPane scrollPane = new JScrollPane(body);
		add(scrollPane, BorderLayout.CENTER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
	}

	private JComboBox<LinLabel> comboBox = null;
	private JPanel body = null;
	private int comboBox_indeox = -1;
	private JLabel newDope = null;
	private JPanel panel_1 = null;
	private DTextField yujiaguolv = null;
	private JRadioButton yuguolv = null;
	private JLabel xianlu_id = null;

	public void reSetXianlu_id() {
		xianlu_id.setText("自身线路:" + DataSave.D_LINE_ID);
	}

	public void updateNewDope(String text) {
		newDope.setText(text);
		reSetXianlu_id();
		this.validate();
		this.repaint();
	}

	private static class FishTitleLabel implements ListCellRenderer<LinLabel> {

		@Override
		public Component getListCellRendererComponent(JList<? extends LinLabel> list, LinLabel value, int index, boolean isSelected, boolean cellHasFocus) {
			if (index < 0 && value == null)
				return new LinLabel("", null);
			return value;
		}
	}

	public void initall() {
		if ( DataSave.D_LINE_ID <= -1)
			return;
		initLine();
		comboBox.setSelectedItem(getLinLabelIndex( DataSave.D_LINE_ID));
		initBody( DataSave.D_LINE_ID);
		body.validate();
		body.repaint();
		validate();
		repaint();
		FishPriceFrame.frame.validate();
		FishPriceFrame.frame.repaint();
	}

	public void initBody(final int d_index) {
		if (d_index <= -1)
			return;
		LinLabel label = getLinLabelIndex(d_index);
		if (label == null)
			return;
		initBody(label);
	}

	private void initBody(LinLabel label) {
		if (label == null)
			return;
		if (label.index < 0)
			return;
		comboBox_indeox = label.index;
		YuAllBody yuAllBody = FishPrice.allbody.get(label.index);
		if (yuAllBody == null)
			return;
		ConcurrentHashMap<Integer, YuBody> yuBody = yuAllBody.body;
		body.removeAll();
		if (yuBody == null || yuBody.isEmpty()) {
			if (System.currentTimeMillis() - label.dianji_time < 15 * 1000) {
				newDope.setText("刷新该线路中！请稍后。。。");
				validate();
				repaint();
				FishPriceFrame.frame.validate();
				FishPriceFrame.frame.repaint();
				return;
			}
		} else {
			Iterator<YuBody> iterator = yuBody.values().iterator();
			LinkedList<FishBean> beans = null;
			if (yuguolv.isSelected()) {
				beans = FishModel.getSaveFishBeans();
			}
			while (iterator.hasNext()) {
				FishBean bean = iterator.next().bean;
				String s1 = yujiaguolv.getText();
				int i1 = 0;
				if (Other.isInteger(s1)) {
					i1 = Integer.parseInt(s1);
				}
				if (bean.getFish_price() < i1)
					continue;
				if (yuguolv.isSelected()) {
					BufferedImage img = bean.getFish_img();
					Iterator<FishBean> iterator2 = beans.iterator();
					Tool tool = new Tool();
					while (iterator2.hasNext()) {
						FishBean temp = iterator2.next();
						if (null != tool.图中找图(img, temp.getFish_name_img(), 0.04, 0, 0)) {
							body.add(new FishOnePrice(bean));
							break;
						}
					}
				} else
					body.add(new FishOnePrice(bean));

			}
			body.validate();
			body.repaint();
			validate();
			repaint();
			FishPriceFrame.frame.validate();
			FishPriceFrame.frame.repaint();
			if (System.currentTimeMillis() - label.shuju_time < 15 * 1000) {
				newDope.setText("刷新线路成功");
				return;
			}
		}
		// System.out.println(new
		// Date(System.currentTimeMillis()).toLocaleString()+" "+new
		// Date(label.dianji_time).toLocaleString()+" "+new Date(
		// label.shuju_time).toLocaleString()+"!");
		label.dianji_time = System.currentTimeMillis();
		label.shuju_time = System.currentTimeMillis();
		UIModel.getLineALLYuBody(label.index);
		validate();
		repaint();
		FishPriceFrame.frame.validate();
		FishPriceFrame.frame.repaint();
	}

	private LinLabel getLinLabelIndex(int d_index) {
		ComboBoxModel<LinLabel> size = comboBox.getModel();
		LinLabel temp = null;
		for (int i = 0; i < size.getSize(); i++) {
			temp = size.getElementAt(i);
			if (temp.index == d_index)
				return temp;
		}
		return null;
	}

	public void initBody(LinkedList<FishBean> beans) {
		if (beans == null)
			return;
		body.removeAll();
		Iterator<FishBean> iterator = beans.iterator();
		while (iterator.hasNext()) {
			FishBean bean = iterator.next();
			body.add(new FishOnePrice(bean, 1));
		}
		body.validate();
		body.repaint();
		validate();
		repaint();
		FishPriceFrame.frame.validate();
		FishPriceFrame.frame.repaint();
	}

	private boolean comboBox_bool = true;

	public void initLine() {
		comboBox_bool = false;
		reSetXianlu_id();
		TreeMap<Integer, YuAllBody> body = new TreeMap<Integer, FishPrice.YuAllBody>();
		body.putAll(FishPrice.allbody);
		java.util.List<Entry<Integer, YuAllBody>> list = new LinkedList<Map.Entry<Integer, YuAllBody>>(body.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, YuAllBody>>() {
			@Override
			public int compare(Entry<Integer, YuAllBody> o1, Entry<Integer, YuAllBody> o2) {
				return -(int) (o1.getValue().xianluBody.time - o2.getValue().xianluBody.time);
			}
		});
		DefaultComboBoxModel<LinLabel> model = (DefaultComboBoxModel<LinLabel>) comboBox.getModel();
		model.removeAllElements();
		Iterator<Entry<Integer, YuAllBody>> yu = list.iterator();
		int n = 0;
		while (yu.hasNext()) {
			Entry<Integer, YuAllBody> temp = yu.next();
			if (temp.getValue().xianluBody.yuan == null || System.currentTimeMillis() - temp.getValue().xianluBody.time >= 120 * 60 * 1000)
				continue;
			LinLabel label = new LinLabel(temp.getKey() + "", new ImageIcon(temp.getValue().xianluBody.yuan));
			label.index = temp.getKey();
			label.item_index = n++;
			label.setTime(temp.getValue().xianluBody.time);
			model.addElement(label);
		}
		comboBox.validate();
		comboBox.repaint();
		panel_1.validate();
		panel_1.repaint();
		validate();
		repaint();
		FishPriceFrame.frame.validate();
		FishPriceFrame.frame.repaint();
		comboBox_bool = true;
		// }
	}

	public int getIndex() {
		return comboBox_indeox;
	}

	public void reLineTime(int body_description) {
		LinLabel linLabel = getLinLabelIndex(body_description);
		linLabel.setTime(FishPrice.allbody.get(body_description).xianluBody.time);
		linLabel.repaint();
		linLabel.validate();
		comboBox.removeItemAt(linLabel.item_index);
		Component[] components = comboBox.getComponents();
		for (Component c : components) {
			c.validate();
			c.repaint();
		}
		comboBox.insertItemAt(linLabel, linLabel.item_index);
		comboBox.validate();
		comboBox.repaint();
		panel_1.validate();
		panel_1.repaint();
		validate();
		repaint();
		FishPriceFrame.frame.validate();
		FishPriceFrame.frame.repaint();
		comboBox.setSelectedItem(linLabel);
	}

}
