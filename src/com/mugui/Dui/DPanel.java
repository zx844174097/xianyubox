package com.mugui.Dui;

import javax.swing.JPanel;

import com.mugui.Mugui;

public abstract class DPanel extends JPanel implements Mugui {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5138980830621085170L;

	abstract public void init();

	abstract public void quit();

	abstract public void dataInit();

	abstract public void dataSave();

	public void start() {
	}

	public void dispose() {
	}
}
