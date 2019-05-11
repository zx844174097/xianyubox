package com.mugui.GJ.ui;

import com.mugui.DataSaveInterface;
import com.mugui.ManagerInterface;
import com.mugui.Dui.DPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

public class BtnPanel extends DPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -767635980566543556L;
	private DPanel body = null;

	public BtnPanel() {
		setLayout(new BorderLayout(0, 0));

		DataSaveInterface scriptDataSave = (DataSaveInterface) ((ManagerInterface) System.getProperties().get("DataSaveManager"))
				.get("com.mugui.script.DataSave");
		scriptDataSave.init();

		body = (DPanel) scriptDataSave.start();
		add(body, BorderLayout.CENTER);
		setBackground(null);
		setOpaque(false);

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		now_point = new Point();
		now_point.x = (int) (dimension.getWidth() / 2 - now_Dimension.getWidth() / 2);
		now_point.y = (int) (dimension.getHeight() / 2 - now_Dimension.getHeight() / 2);
	}

	private Dimension now_Dimension = new Dimension(1200, 800);
	private Point now_point = null;

	@Override
	public void init() {
		if (now_point != null)
			com.mugui.ui.DataSave.StaticUI.setLocation(now_point);
		com.mugui.ui.DataSave.StaticUI.setSize(now_Dimension);
		com.mugui.ui.DataSave.StaticUI.updateTitle("(按键宏)");
		body.init();
	}

	@Override
	public void quit() {
		if (body != null)
			body.quit();
		now_point = com.mugui.ui.DataSave.StaticUI.getLocation();
		now_Dimension = com.mugui.ui.DataSave.StaticUI.getSize();
	}

	@Override
	public void dataInit() {
	}

	@Override
	public void dataSave() {
	}
}
