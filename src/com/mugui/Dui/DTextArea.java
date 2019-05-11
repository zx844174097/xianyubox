package com.mugui.Dui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class DTextArea extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4065828707219596672L;
	private final static String[] lstr = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	{
		this.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				Font font = DTextArea.this.getFont();
				String text = DTextArea.this.getText();
				if (font.canDisplayUpTo(text) != -1) {
					for (String str : lstr) {
						Font f = new Font(str, font.getStyle(), font.getSize());
						if (f.canDisplayUpTo(text) == -1) {
							DTextArea.this.setFont(f);
							return;
						}
					}
				}
			}
		});
		setLineWrap(true);
		
		setWrapStyleWord(true);
	}

	@Override
	public void setText(String text) {
		if (text == null || text.length() == 0)
			return;
		Font font = DTextArea.this.getFont();
		if (font.canDisplayUpTo(text) != -1) {
			for (String str : lstr) {
				Font f = new Font(str, font.getStyle(), font.getSize());
				if (f.canDisplayUpTo(text) == -1) {
					DTextArea.this.setFont(f);
					super.setText(text);
					return;
				}
			}
		}
		super.setText(text);

	}
}
