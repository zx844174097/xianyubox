package com.mugui.ew.ui;

import com.mugui.Dui.DPanel;
import javax.swing.JLabel;

public class ErrorPanel extends DPanel {
	public ErrorPanel() {
		JLabel label = new JLabel("页面加载失败");
		add(label);
	}

	private JLabel label = null;

	public void setErrorMessage(Object message) {
		label.setText("页面加载失败:" + message); 
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8969963183360449193L;

	@Override
	public void init() {
	}

	@Override
	public void quit() {
	}

	@Override
	public void dataInit() {
	}

	@Override
	public void dataSave() {
	}

}
