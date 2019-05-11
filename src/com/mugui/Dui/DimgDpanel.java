package com.mugui.Dui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;

public class DimgDpanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4058195223091616466L;
	private DimgFile image = null;
	private JLabel name = null;

	public DimgDpanel(DimgFile dFile) {
		this.image = dFile;
		setLayout(new BorderLayout(0, 0));
		name = new JLabel(dFile.objectname);
		name.setFont(new Font("宋体", Font.PLAIN, 11));
		add(name, BorderLayout.SOUTH);
		setPreferredSize();
		this.setBackground(null);
		this.setOpaque(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D graphics2d = (Graphics2D) g;
		graphics2d.drawImage(image.bufferedImage, 25 - image.bufferedImage.getWidth() / 2, 25 - image.bufferedImage.getHeight() / 2, this);
		graphics2d.setColor(Color.DARK_GRAY);
		graphics2d.draw3DRect(0, 0, 49, 49, true);
	}

	private void setPreferredSize() {
		setPreferredSize(new Dimension(50, 62));
		setSize(50, 62);
	}

	public void removeFile() {
		if (image.file.exists()) {
			image.file.delete();
		}
	}

	public BufferedImage getBufferedImage() {
		return image.bufferedImage;
	}

	public File getImageFile() {
		return image.file;
	}

	public DimgFile getDimgFile() {
		return image;
	}

	public void datainit() {
		name.setText(image.objectname);
	}
}
