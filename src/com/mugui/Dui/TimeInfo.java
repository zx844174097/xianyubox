package com.mugui.Dui;

import javax.swing.JPanel;

import com.mugui.ui.DataSave;

public class TimeInfo extends InfoBody {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1194314065571104801L;
	private int time;

	public TimeInfo(String title, String message, int time) {
		super(title, message, false, false);
		this.time = time;
	}

	public TimeInfo(String title, String message) {
		this(title, message, 3000);
	}

	public void run() {
		run(DataSave.StaticUI.getUI());
	}

	public void run(final JPanel infoBody) {
		if (infoBody == null)
			return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataSave.StaticUI.updateUI(TimeInfo.this);
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
				}
				DataSave.StaticUI.updateUI(infoBody);
			}
		}).start();
	}
}
