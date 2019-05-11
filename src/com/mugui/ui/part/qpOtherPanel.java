package com.mugui.ui.part;

import javax.swing.JPanel;

import com.mugui.Dui.DimgDpanel;
import com.mugui.Dui.DimgFile;
import com.mugui.ui.DataSave;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class qpOtherPanel extends JPanel {

	public qpOtherPanel(DimgFile dimgFile, long start_time, long end_time) {

		setLayout(new BorderLayout(0, 0));
		if (dimgFile == null) {
			dimgFile = new DimgFile();
			dimgFile.bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			dimgFile.file = new File("");
		}
		this.dimgFile = dimgFile;
		DimgDpanel panel = new DimgDpanel(dimgFile);
		add(panel, BorderLayout.WEST);

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);

		label = new JLabel();
		label_1 = new JLabel();

		lblNewLabel = new JLabel("状态：");

		label_2 = new JLabel("实际结束时间：");
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addGroup(
				gl_panel_1
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(label, GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
										.addComponent(label_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
										.addComponent(label_2, GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
										.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)).addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel_1.createSequentialGroup().addComponent(label).addGap(4).addComponent(label_1).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(label_2).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblNewLabel).addContainerGap(224, Short.MAX_VALUE)));
		panel_1.setLayout(gl_panel_1);
		setStart_time(start_time);
		setEnd_time(end_time);
	}

	private SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	private JLabel label_1 = null;
	private JLabel label = null;
	private JLabel lblNewLabel = null;
	private JLabel label_2 = null;
	private DimgFile dimgFile = null;

	public DimgFile getDimgFile() {
		return dimgFile;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1944993185754659895L;
	private long start_time;
	private long end_time;
	private long zend_time;

	public long getStart_time() {
		return start_time;
	}

	public long getend_time() {
		return end_time;
	}

	public long getZend_time() {
		return zend_time;
	}

	public void setStateText(String text) {
		lblNewLabel.setText("状态：" + text);
		DataSave.qpList.repaint();
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
		label.setText("抢拍开始时间：" + df.format(new Date(start_time)));
		DataSave.qpList.repaint();
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
		label_1.setText("预计结束时间：" + df.format(new Date(end_time)));
		DataSave.qpList.repaint();
	}

	public void setZEnd_time(long zend_time) {
		this.zend_time = zend_time;
		label_2.setText("实际结束时间：" + df.format(new Date(zend_time)));
		DataSave.qpList.repaint();
	}
}