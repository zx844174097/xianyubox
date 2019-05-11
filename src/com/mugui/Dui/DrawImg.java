package com.mugui.Dui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.JPanel;

import com.mugui.tool.Other;
import com.mugui.windows.Tool;

import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JTextField;

public class DrawImg extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -940193343757697605L;
	private Stack<BufferedImage> imgstace = new Stack<BufferedImage>();
	private BufferedImage nowimg = null;
	private drawMouseListener draw = null;
	private JPanel drawBan = null;
	private JPanel father = null;

	public DrawImg(JPanel custonimg) {
		father = custonimg;
		setLayout(new BorderLayout(0, 0));
		drawBan = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(nowimg, 0, 0, this);
				if (x1 == 0 && y1 == 0 && x2 == 0 && y2 == 0)
					return;
				Graphics2D graphics2d = (Graphics2D) g;
				graphics2d.setColor(Color.red);
				graphics2d.draw3DRect(x1, y1, x2 - x1 + 1, y2 - y1 + 1, true);

			}
		};
		drawBan.setOpaque(false);
		add(drawBan);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		JLabel label = new JLabel("坐标：");
		panel.add(label);

		textField = new JTextField();
		textField.setText(",,,");
		panel.add(textField);
		textField.setColumns(10);

		JLabel label_1 = new JLabel("点颜色(如果选择区域为一个点)：");
		panel.add(label_1);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		panel.add(textField_1);
		draw = new drawMouseListener();
		drawBan.addMouseMotionListener(draw);
		drawBan.addMouseListener(draw);
	}

	private int x1, y1, x2, y2;
	private JTextField textField;
	private JTextField textField_1;

	private class drawMouseListener implements MouseMotionListener, MouseListener {
		private boolean isTrue = false;
		private dThread Thread = null;

		private class dThread extends Thread {
			@Override
			public void run() {
				while (isTrue) {
					DrawImg.this.repaint();
					Other.sleep(50);
					textField.setText(x1 + "," + y1 + "," + x2 + "," + y2);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		private Point init = new Point(0, 0);

		// 鼠标按下
		@Override
		public void mousePressed(MouseEvent e) {
			x1 = x2 = e.getX();
			y1 = y2 = e.getY();
			init.x = x1;
			init.y = y1;
			isTrue = true;
			Thread = new dThread();
			Thread.start();
		}

		// 鼠标释放
		@Override
		public void mouseReleased(MouseEvent e) {
			isTrue = false;
			nextDraw();
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		// 鼠标移动
		@Override
		public void mouseDragged(MouseEvent e) {
			if (!isTrue)
				return;
			if (init.x < e.getX()) {
				x2 = e.getX();
				x1 = init.x;
			} else {
				x2 = init.x;
				x1 = e.getX();
			}
			if (init.y < e.getY()) {
				y1 = init.y;
				y2 = e.getY();
			} else {
				y2 = init.y;
				y1 = e.getY();
			}

		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}

	public void drawImg(BufferedImage img) {
		if (img == null)
			return;
		nowimg = img;
		setPreferredSize();
		validate();
		repaint();
		father.validate();
		father.repaint();
		drawBan.repaint();
	}

	private void setPreferredSize() {
		drawBan.setBounds(0, 0, nowimg.getWidth(), nowimg.getHeight());
		setPreferredSize(new Dimension(nowimg.getWidth(), nowimg.getHeight() + 40));
	}

	private void nextDraw() {
		if (nowimg == null)
			return;
		if (x2 < x1 || y2 < y1 || x2 > nowimg.getWidth() || y2 > nowimg.getHeight() || x1 < 0 || y1 < 0)
			return;
		if (x1 == x2 && y1 == y2) {
			Color c = new Color(nowimg.getRGB(x1, y1));
			textField_1.setText(new Tool().getColorInHexFromRGB(c.getRed(), c.getGreen(), c.getBlue()));
		}
		imgstace.push(nowimg);
		nowimg = nowimg.getSubimage(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
		drawBan.repaint();
		setPreferredSize();
		validate();
		repaint();
		father.validate();
		father.repaint();
	}

	public void lastDraw() {
		if (imgstace.empty())
			return;
		BufferedImage image = imgstace.pop();
		nowimg = image;
		setPreferredSize();
		drawBan.repaint();
		validate();
		repaint();
		father.validate();
		father.repaint();
	}

	public BufferedImage nowDraw() {
		return nowimg;
	}
}
