package com.mugui.ui.part;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DFrame;
import com.mugui.Dui.DTextField;
import com.mugui.tool.Other;
import com.mugui.windows.Tool;
import java.awt.FlowLayout;

public class XYPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3286401721670600963L;
	private DButton button = null;
	private DTextField textField = null;
	private Dimension size = null;
	private Point X_Y = new Point(0, 0);
	private BufferedImage image = null;

	public XYPanel() {
		this(new Dimension(1, 1));
	}

	public XYPanel(Dimension p) {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setVgap(0);
		this.size = p;
		listener = new XYMouseListener();
		button = new DButton((String) null, Color.black);
		button.setBackground(Color.WHITE);
		button.addMouseListener(listener);
		button.setFont(new Font("Dialog", Font.BOLD, 12));
		button.setText("按住拖动");
		add(button);
		JLabel label_7 = new JLabel("坐标:");
		add(label_7);
		textField = new DTextField(12);
		textField.setColumns(8);
		add(textField);

	}

	public void setX_Y(Point point) {
		X_Y = point;
		textField.setText(X_Y.x + "-" + X_Y.y);
	}

	public void setX_Y(String string) {
		textField.setText(string);
		String str[] = string.split("-");
		X_Y = new Point(Integer.parseInt(str[0]), Integer.parseInt(str[1]));
	}

	public String getX_YText() {
		return textField.getText();
	}

	public Rectangle getX_Y() {
		return new Rectangle(X_Y.x - size.width / 2, X_Y.y - size.height / 2, size.width, size.height);
	}

	public Point getX_YPoint() {
		return X_Y;
	}

	public BufferedImage getX_YImg() {
		return image;
	}

	private XYMouseListener listener;

	class XYMouseListener implements MouseListener {
		DFrame dFrame = null;
		int state = 0;
		Cursor cursor = null;

		public XYMouseListener() { 
			dFrame = new DFrame();
			dFrame.setSize(size);
			dFrame.getContentPane().setBackground(Color.red);
			dFrame.getIconImage();
			Toolkit tk = Toolkit.getDefaultToolkit();
			BufferedImage bi = new BufferedImage(dFrame.getWidth(), dFrame.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = bi.createGraphics();
			dFrame.print(g2d);
			Image image = bi;
			cursor = tk.createCustomCursor(image, new Point(dFrame.getWidth(), dFrame.getHeight()), "norm");
			dFrame.setCursor(cursor);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

		private Thread thread = null;

		class XYThread extends Thread {
			@Override
			public void run() {
				dFrame.setVisible(true);
				dFrame.setAlwaysOnTop(true);
				while (state == 1) {
					X_Y = MouseInfo.getPointerInfo().getLocation();
					dFrame.setLocation((int) (X_Y.x - size.getWidth() / 2), (int) (X_Y.y - size.getHeight() / 2));
					textField.setText(X_Y.x + "-" + X_Y.y);
					Other.sleep(25);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				state = 1;
				if (thread == null || !thread.isAlive()) {
					thread = new XYThread();
					thread.start();
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			state = 0;
			dFrame.setAlwaysOnTop(false);
			dFrame.setVisible(false);
			image = new Tool().截取屏幕(X_Y.x - size.width / 2, X_Y.y - size.height / 2, X_Y.x + size.width / 2 + 1, X_Y.y + size.height / 2 + 1);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

	}

	public void addXYListener(MouseListener xyListner) {
		button.addMouseListener(xyListner);
	}

}
