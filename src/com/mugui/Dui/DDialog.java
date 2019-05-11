package com.mugui.Dui;

import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.*;

public class DDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2536369872828357452L;
	int wei = 1200, hei = 800;
	private Window frame = null;

	public DDialog(Window f, String s, boolean b) {
		super(f, s, b ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
		frame = f;
		setKai(wei, hei);
		this.setUndecorated(true);
	}

	public void setKai(int wei, int hei) {
		this.wei = wei;
		this.hei = hei;
		this.setSize(wei, hei);
		if (frame == null)
			this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - wei / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - hei / 2);
		else {
			this.setLocation(frame.getX() + frame.getWidth() / 2 - wei / 2, frame.getY() + frame.getHeight() / 2 - hei / 2);
		}
	}
}
