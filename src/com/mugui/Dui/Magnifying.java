package com.mugui.Dui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

public class Magnifying extends DDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6928998663134192412L;
	private boolean isTrue = false;
	private DTextField textField = null;
	private JFrame frame=null;
	public Magnifying(JFrame f, String s, boolean b) {
		super(f, s, b);
		frame=f;
		if(frame==null) frame=DataSave.StaticUI;
		textField = new DTextField(56);
		textField.setColumns(12);
		setLayout(new BorderLayout(0, 0));
		add(textField, BorderLayout.SOUTH);
	}

	public boolean isClose() {
		return isTrue;
	}

	private Thread thread = null;
	private Tool tool = null;

	public void start() {
		tool = new Tool();
		isTrue = true;
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		if (thread == null || !thread.isAlive()) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					Graphics graphics = Magnifying.this.getGraphics();
					Point xy = new Point(0, 0);
					Point zuobiao = new Point();
					int yanse = 0;
					while (isTrue) {
						Point p = MouseInfo.getPointerInfo().getLocation();
						BufferedImage image = tool.截取屏幕(p.x - 10, p.y - 10, p.x + 10, p.y + 10);
						int color = image.getRGB(10, 10);
						graphics.drawImage(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH), 0, 0, null);
						graphics.setColor(Color.red);
						graphics.draw3DRect(100, 100, 10, 10, true);
						Point xy2 = frame.getLocation();
						Dimension size = frame.getSize();
						if (xy2.x + size.width != xy.x || xy2.y != xy.y) {
							xy.x = xy2.x + size.width;
							xy.y = xy2.y;
							Magnifying.this.setLocation(xy);
						}
						if (zuobiao.x != p.x || zuobiao.y != p.y || yanse != color) {
							zuobiao = p;
							yanse = color;
							Color c = new Color(color);
							String buffer = "坐标：" + zuobiao.x + "-" + zuobiao.y + " 颜色：" + tool.getColorInHexFromRGB(c.getRed(), c.getGreen(), c.getBlue());
							// textField.setText("!#$");
							textField.setText(buffer);
						}
						tool.delay(24);
					}
				}
			});
			thread.start();
		}
	}

	public void stop() {
		isTrue = false;
		this.setAlwaysOnTop(false);
		this.setVisible(false);
	}

}
