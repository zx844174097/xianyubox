package com.mugui.Dui;

import java.awt.*;
import java.io.Serializable;

import javax.swing.*;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.plaf.metal.MetalButtonUI;

public class DButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6655761286238492290L;

	private class DMetalButtonUI extends MetalButtonUI implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -508986144537417765L;

		@Override
		protected void paintButtonPressed(Graphics g, AbstractButton b) {
			if (b.isContentAreaFilled()) {
				Dimension size = b.getSize();
				g.setColor(pressColor);
				g.fillRect(0, 0, size.width, size.height);
			}
		}
	}

	private final static String[] lstr = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

	@Override
	public void setText(String text) {
		if (text != null && text.length() != 0) {
			Font font = getFont();
			if (font.canDisplayUpTo(text) == -1) {
				super.setText(text);
			} else {
				for (String str : lstr) {
					Font f = new Font(str, font.getStyle(), font.getSize());
					if (f.canDisplayUpTo(text) == -1) {
						super.setText(text);
						super.setFont(f);
						return;
					}
				}

			}
		}
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		String text = getText();
		if (text != null && text.length() != 0) {
			if (font.canDisplayUpTo(text) == -1) {
				super.setFont(font);
			} else {
				for (String str : lstr) {
					Font f = new Font(str, font.getStyle(), font.getSize());
					if (f.canDisplayUpTo(text) == -1) {
						super.setFont(f);
						return;
					}
				}

			}
		}

	}

	public DButton(String text, Color color) {
		this.setUI(new DMetalButtonUI());
		this.setText(text);
		this.setFont(new Font("华文行魏", Font.CENTER_BASELINE, 17));
		this.setFocusPainted(false);
		this.setBackground(Color.DARK_GRAY);
		if (color == null)
			color = Color.white;
		this.setForeground(color);
		this.setBorder(new MetalBorders.ButtonBorder());// 

	}

	private Color pressColor = DEFAULT_PRESS_COLOR;
	private static final Color DEFAULT_PRESS_COLOR = Color.DARK_GRAY;

	public void setButtonPressColor(Color color) {
		if (color == null) {
			pressColor = DEFAULT_PRESS_COLOR;
			return;
		}
		pressColor = color;
	}
}