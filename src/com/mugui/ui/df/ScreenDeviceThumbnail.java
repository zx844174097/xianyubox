package com.mugui.ui.df;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ScreenDeviceThumbnail extends JPanel {
	private BufferedImage screenThumbnail = null;

	private GraphicsDevice device = null;

	public ScreenDeviceThumbnail() throws AWTException {
		this(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
	}

	public ScreenDeviceThumbnail(GraphicsDevice de) throws AWTException {
		Robot robot = new Robot(de);
		GraphicsConfiguration gc = de.getDefaultConfiguration();
		screenThumbnail = robot.createScreenCapture(gc.getBounds());
		this.device = de;
		setLayout(null);

	}

	public GraphicsDevice getDevice() {
		return device;
	}

	public boolean isSelected() {
		return select;
	}

	private boolean select = false;

	private Color color = getBackground();

	public void setSelected(boolean bool) {
		select = bool;
		color = bool ? new Color(0xcc, 0x00, 0x33) : new Color(240, 240, 240);
		repaint();
	}

	public void removeActionListener(DActionListener actionListener) {
		this.removeMouseListener(actionListener);
	}

	interface DActionListener extends MouseListener {
	}

	public void addActionListener(DActionListener actionListener) {
		this.addMouseListener(actionListener);
	}

	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(color);
		g.fillRect(3, 3, getWidth() - 6, getHeight() - 6);
		g.drawImage(screenThumbnail, 10, 10, getWidth() - 20, getHeight() - 20, null);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3159241216539376669L;

}
