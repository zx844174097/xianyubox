package com.mugui.ui.part;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.mugui.Dui.DPanel;
import com.mugui.model.QpHandle;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPanel;

import com.mugui.Dui.DButton;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

public class qpList extends DPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4322642602814670652L;

	private JList<qpOtherPanel> list = null;
	private DefaultListModel<qpOtherPanel> model = null;

	public qpList() {
		setLayout(new BorderLayout(0, 0));
		list = new JList<qpOtherPanel>();
		model = new DefaultListModel<qpOtherPanel>();
		list.setModel(model);
		list.setCellRenderer(new qpListRenderer());
		JScrollPane jScrollPane = new JScrollPane(list);
		add(jScrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		DButton button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QpHandle.stop();
				DataSave.StaticUI.updateUI(DataSave.bodyPanel);
			}
		});

		time = new JLabel("当前时间：");
		panel.add(time);
		button.setText("关闭");
		panel.add(button);
	}

	public void addQPOther(qpOtherPanel mainThread) {
		model.addElement(mainThread);
		list.setModel(model);
		this.validate();
		this.repaint();
	}

	private static JLabel time = null;
	private TimeThread timeThread = null;;

	private Dimension now_Dimension = null;
	private Point now_Point = null;

	@Override
	public void init() {
		DataSave.StaticUI.updateTitle("扫拍时间列表");
		timeThread = TimeThread.getTimeThread();
		timeThread.start();
		model.removeAllElements();
		this.validate();
		this.repaint();
		now_Point = DataSave.StaticUI.getLocation();
		now_Dimension = DataSave.StaticUI.getSize();
		DataSave.StaticUI.setLocation(0, 0);
		DataSave.StaticUI.setAlwaysOnTop(true);
		DataSave.StaticUI.setSize((int) (0.12 * Toolkit.getDefaultToolkit().getScreenSize().width), Toolkit.getDefaultToolkit().getScreenSize().height - 50);

		JIntellitype.getInstance().registerHotKey(101, "f11");
		JIntellitype.getInstance().addHotKeyListener(hotkeyListener);
	}

	private static class TimeThread extends Thread {
		private boolean istrue = true;
		private SimpleDateFormat df = new SimpleDateFormat("yyyy年  MM月 dd日  HH:mm:ss");

		@Override
		public void run() {
			while (istrue) {
				time.setText("当前时间：" + df.format(new Date(System.currentTimeMillis())));
				Other.sleep(200);
			}
		}

		public void close() {
			istrue = false;
		}

		public static TimeThread getTimeThread() {
			return new TimeThread();
		}

	};

	private HotkeyListener hotkeyListener = new HotkeyListener() {
		@Override
		public void onHotKey(int arg0) {
			QpHandle.stop();
		}
	};

	@Override
	public void quit() {
		model.removeAllElements();
		DataSave.StaticUI.setAlwaysOnTop(false);
		DataSave.StaticUI.setLocation(now_Point);
		DataSave.StaticUI.setSize(now_Dimension.width,now_Dimension.height);
		JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
		timeThread.close();
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
