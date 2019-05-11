package com.mugui.ui.df;

import javax.swing.JFrame;

import com.mugui.Dui.DDialog;
import com.mugui.ui.DataSave;
import com.mugui.windows.呼吸背景;
import com.sun.awt.AWTUtilities;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import com.mugui.Dui.DVerticalFlowLayout;
import com.mugui.http.Bean.FishBean;
import com.mugui.model.FishPrice;
import com.mugui.model.UIModel;
import com.mugui.tool.Other;

public class BoldShortCutPanel extends DDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8510532434818654924L;

	public BoldShortCutPanel(JFrame d, String s, boolean b) {
		super(d, s, b);

		this.setSize(270, 2000);
		this.setLocation(DataSave.SCREEN_X + 15, DataSave.SCREEN_Y + 180);
		setAlwaysOnTop(true);
		getContentPane().setLayout(new BorderLayout(0, 0));

		lblboos = new JLabel("最新黄金钟线路");
		lblboos.setForeground(new Color(102, 51, 153));
		lblboos.setFont(new Font("宋体", Font.BOLD, 21));
		lblboos.setOpaque(true);
		lblboos.setBackground(Color.BLACK);
		lblboos.setForeground(Color.WHITE);
		lblboos.addMouseListener(l);
		lblboos.addMouseMotionListener(l);
		getContentPane().add(lblboos, BorderLayout.NORTH);

		body = new JPanel();
		body.setBackground(null);
		getContentPane().add(body, BorderLayout.CENTER);
		body.setLayout(new DVerticalFlowLayout());
		setUndecorated(true);
		getContentPane().setBackground(null);
		AWTUtilities.setWindowOpaque(this, false);
	}

	private MouseInputListener l = new MouseInputListener() {

		Point origin = new Point();

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// 鼠标按下
			origin.x = e.getX();
			origin.y = e.getY();

		}

		@Override
		public void mouseExited(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point p = getLocation();
			setLocation(p.x + (e.getX() - origin.x), p.y + (e.getY() - origin.y));
			Other.sleep(20);
		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}
	};
	private JPanel body = null;
	private JLabel lblboos = null;
	private static BoldShortCutPanel boldShortCutPanel = null;

	public static 呼吸背景 x = DataSave.x;
	private static 黄金钟刷新Thread thread = null;

	public static void start() {
		if (DataSave.黄金钟快捷窗口) {
			if (boldShortCutPanel == null) {
				boldShortCutPanel = new BoldShortCutPanel(DataSave.StaticUI, null, false);
			}
			if (x == null || !x.isAlive()) {
				x = new 呼吸背景(boldShortCutPanel.lblboos);
				x.start();
			} else {
				x.addNew(boldShortCutPanel.lblboos);
			}
			if (thread == null || !thread.isTrue) {
				thread = new 黄金钟刷新Thread();
				thread.start();
			}
			boldShortCutPanel.setVisible(true);
		} else {
			if (boldShortCutPanel != null) {
				boldShortCutPanel.setVisible(false);
			}
			if (thread != null) {
				thread.isTrue = false;
			}
		}
	}

	private static class 黄金钟刷新Thread extends Thread {
		private boolean isTrue = false;

		@Override
		public void run() {
			Other.sleep(1000);
			isTrue = true;
			while (isTrue) {
				// 请求黄金钟
				UIModel.getBoldLines();

				long time = System.currentTimeMillis();
				int y = DataSave.SCREEN_Y;
				int x = DataSave.SCREEN_X;
				while (System.currentTimeMillis() - time < 5 * 60 * 1000) {
					Other.sleep(25);
					if (x != DataSave.SCREEN_X || y != DataSave.SCREEN_Y)
						BoldShortCutPanel.boldShortCutPanel.setLocation(DataSave.SCREEN_X + 15, DataSave.SCREEN_Y + 180);
					if (!isTrue) {
						return;
					}
				}
			}
		}
	}

	public static void updateUI() {
		if (boldShortCutPanel != null && boldShortCutPanel.isVisible())
			boldShortCutPanel.updateBoldUI();
	}

	private void updateBoldUI() {
		LinkedList<FishBean> linked = FishPrice.getBoldAll();
		if (body == null)
			return;
		body.removeAll();
		for (FishBean bean : linked) {
			body.add(new FishOnePrice(bean,2));
		}
		body.validate();
		validate();
	}

}
