package com.mugui.ew.ui.component;

import com.mugui.Dui.DDialog;
import com.mugui.Dui.DOptionPanel;
import com.mugui.Dui.DPanel;
import com.mugui.ew.DataSave;
import com.mugui.ew.EWUIHandel;
import com.mugui.jni.Tool.DJni;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JPanel;

import com.mugui.Dui.DTextField;
import java.awt.FlowLayout;
import com.mugui.Dui.DButton;
import java.awt.Color;
import com.mugui.Dui.DVerticalFlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class DownloadSelector extends DPanel {
	public DownloadSelector() {
		setLayout(new BorderLayout(0, 0));

		label = new JLabel("新建下载：");
		label.setBackground(Color.GRAY);
		label.setFont(new Font("宋体", Font.BOLD, 15));
		add(label, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new DVerticalFlowLayout());

		textField = new DTextField(128);
		textField.setFont(new Font("宋体", Font.PLAIN, 14));
		textField.setColumns(46);
		panel_1.add(textField);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setVgap(0);
		panel.add(panel_2, BorderLayout.EAST);

		DButton button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DJni jni = new DJni();
				System.out.println(textField.getText());
				String file = jni.OpenWindowsFileSelector(new File(textField.getText()).getParent());
				if (file != null)
					textField.setText(file + "\\" + downloadItem.getDestinationFile().getName());
			}
		});
		button.setFont(new Font("Dialog", Font.PLAIN, 14));
		button.setText("重设");
		panel_2.add(button);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setVgap(3);
		panel.add(panel_3, BorderLayout.WEST);

		JLabel label_1 = new JLabel("存储路径：");
		panel_3.add(label_1);
		label_1.setFont(new Font("宋体", Font.PLAIN, 14));

		JPanel panel_4 = new JPanel();
		add(panel_4, BorderLayout.SOUTH);

		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File down = new File(textField.getText());
				if (!down.getParentFile().isDirectory() && !down.getParentFile().mkdirs()) {
					DOptionPanel.showMessageDialog(father, "无法将文件保存到此路径下：" + down.getParent(), "错误", DOptionPanel.OPTION_OK);
					return;
				}
				if (down.isFile()) {
					int i = DOptionPanel.showMessageDialog(father, "当前文件" + down.getName() + "已存在，是否覆盖下载", "警告", DOptionPanel.OPTION_OK_CANCEL);
					if (i == DOptionPanel.RET_YES) {
						if (!down.delete()) {
							DOptionPanel.showMessageDialog(father, "无法覆盖原文件" + down.getParent(), "错误", DOptionPanel.OPTION_OK);
							return;
						}
					} else {
						return;
					}
				}
				System.out.println("新建下载:" + down.getAbsolutePath());

				downloadItem.setDestinationFile(down);
				quit();
			}
		});
		button_1.setFont(new Font("Dialog", Font.BOLD, 15));
		button_1.setText("确认下载");
		panel_4.add(button_1);

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downloadItem = null;
				quit();
			}
		});
		button_2.setFont(new Font("Dialog", Font.BOLD, 15));
		button_2.setText("取消下载");
		panel_4.add(button_2);

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DownloadItem downloadItem = null;

	public DownloadItem init(DownloadItem item) {
		father.setKai(450, 78);
		downloadItem = item;
		label.setText("新建下载：" + item.getDestinationFile().getName());
		textField.setText(datasave.JARFILEPATH + "\\browser\\download\\" + item.getDestinationFile().getName());
		father.setVisible(true);
		return downloadItem;
	}

	public DDialog father = null;
	public DataSave datasave = (DataSave) EWUIHandel.datasave;
	private JLabel label;
	private DTextField textField;

	@Override
	public void init() {
		if (father != null)
			return;
		father = new DDialog(datasave.frame, "", true);
		father.getContentPane().add(this);
	}

	@Override
	public void quit() {
		father.setVisible(false);
		father.dispose();
	}

	@Override
	public void dataInit() {
	}

	@Override
	public void dataSave() {
	}

}
