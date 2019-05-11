package com.mugui.ew.ui.component;

import com.mugui.Dui.DPanel;
import com.mugui.Dui.DScrollBar;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.mugui.Dui.DButton;
import com.mugui.Dui.DDialog;

import java.awt.Color;
import java.awt.Font;
import com.mugui.Dui.DVerticalFlowLayout;
import com.mugui.ew.DataSave;
import com.mugui.ew.EWUIHandel;
import com.mugui.ew.ui.component.DownloadViewItem.RemoveListener;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DownloadView extends DPanel {
	public DownloadView() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel label = new JLabel("文件下载列表");
		label.setFont(new Font("宋体", Font.BOLD, 15));
		panel.add(label, BorderLayout.WEST);

		DButton button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				father.setVisible(false);
				father.dispose();
			}
		});
		button.setText("关闭");
		panel.add(button, BorderLayout.EAST);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBar(new DScrollBar());
		add(scrollPane, BorderLayout.CENTER);

		downloadList = new JPanel();
		scrollPane.setViewportView(downloadList);
		downloadList.setLayout(new DVerticalFlowLayout());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4080859684870852780L;

	public boolean addDownloadItem(DownloadItem var1) {
		downloadList.add(new DownloadViewItem(var1).onActionListener(listener));
	
		return true;
	}

	private RemoveListener listener = new RemoveListener() {
		@Override
		public void removeDownloadItem(DownloadViewItem item) {
			downloadList.remove(item);
			downloadList.updateUI();
			downloadList.repaint();
		}
	};
	private DDialog father = null;
	private DataSave datasave = (DataSave) EWUIHandel.datasave;
	private JPanel downloadList;

	@Override
	public void init() {
		if (father == null)
			father = new DDialog(datasave.frame, "", true);
		father.setKai(400, 600);
		father.setLocation(datasave.frame.getWidth() + datasave.frame.getX() - father.getWidth(),
				datasave.frame.getY() + datasave.frame.getHeight() - father.getHeight());
		father.add(this);
		dataInit();
		father.setVisible(true);
	}

	@Override
	public void quit() {
		if (father != null) {
			father.setVisible(false);
			father.dispose();
		}
		dataSave();
	}

	@Override
	public void dataInit() {
	}

	@Override
	public void dataSave() {
	}

}
