package com.mugui.ew.ui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DOptionPanel;
import com.mugui.Dui.DPanel;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import com.teamdev.jxbrowser.chromium.events.DownloadEvent;
import com.teamdev.jxbrowser.chromium.events.DownloadListener;

public class DownloadViewItem extends DPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1505980753659638151L;
	private DownloadItem item = null;
	private JProgressBar progressBar;
	private JLabel label;
	private JLabel lblkbs;

	public DownloadViewItem(DownloadItem var1) {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel, BorderLayout.NORTH);

		label = new JLabel("文件名");
		label.setText("文件名：" + var1.getDestinationFile().getName());
		panel.add(label);

		progressBar = new JProgressBar();
		progressBar.setEnabled(false);
		add(progressBar, BorderLayout.CENTER);
		progressBar.setForeground(Color.GRAY);
		progressBar.setStringPainted(true);
		progressBar.setString("资源连接中。。。");
		progressBar.setBackground(Color.WHITE);
		progressBar.setFont(new Font("微软雅黑", Font.BOLD, 15));
		progressBar.setMinimum(0);

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.EAST);

		lblkbs = new JLabel("0kb/s");
		panel_1.add(lblkbs);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		add(panel_2, BorderLayout.SOUTH);

		DButton button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (button.getText().equals("暂停下载")) {
					button.setText("继续下载");
					item.pause();
				} else {
					button.setText("暂停下载");
					item.resume();
				}
			}
		});

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(item.getDestinationFile().getParentFile());
				} catch (IOException e1) {
					progressBar.setString("IO发生错误，无法打开文件夹");
				}
			}
		});
		button_2.setFont(new Font("Dialog", Font.BOLD, 13));
		button_2.setText("打开文件所在位置");
		panel_2.add(button_2);
		button.setFont(new Font("Dialog", Font.BOLD, 13));
		button.setText("暂停下载");
		panel_2.add(button);

		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = DOptionPanel.showMessageDialog(DownloadViewItem.this, "是否彻底删除文件", "提示", DOptionPanel.OPTION_OK_CANCEL);
				if (i == DOptionPanel.RET_YES) {
					item.cancel();
					progressBar.setString("已删除");
					item.getDestinationFile().delete();
					if (listener != null) {
						listener.removeDownloadItem(DownloadViewItem.this);
					}
				}
			}
		});
		button_1.setFont(new Font("Dialog", Font.BOLD, 13));
		button_1.setText("彻底删除文件");
		panel_2.add(button_1);

		item = var1;
		var1.addDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadUpdated(DownloadEvent var1) {
				if (var1.getDownloadItem().isPaused()) {
					progressBar.setString("暂停中。。。");
				} else if (var1.getDownloadItem().isCompleted()) {
					progressBar.setString("已完成");
				} else if (var1.getDownloadItem().isCanceled()) {
					progressBar.setString("已删除");
				} else {
					progressBar.setString(null);
					long kb = var1.getDownloadItem().getCurrentSpeed();
					if (kb < 1024) {
						lblkbs.setText(kb + " b/s");
					} else {
						kb = kb / 1024;
						if (kb < 1024) {
							lblkbs.setText(kb + " kb/s");
						} else {
							kb = kb / 1024;
							lblkbs.setText(kb + " mb/s");
						}
					}
					int i = var1.getDownloadItem().getPercentComplete();
					if (i == -1) {
						progressBar.setString("未知大小");
					} else
						progressBar.setValue(i);
					progressBar.invalidate();
					progressBar.updateUI();
				}
			}
		});
	}

	public DownloadItem getItem() {
		return item;
	}

	@Override
	public void init() {
	}

	@Override
	public void quit() {
	}

	@Override
	public void dataInit() {
	}

	@Override
	public void dataSave() {
	}

	private RemoveListener listener = null;

	public DownloadViewItem onActionListener(RemoveListener listener) {
		this.listener = listener;
		return this;
	}

	public interface RemoveListener {
		public void removeDownloadItem(DownloadViewItem item);
	}
}
