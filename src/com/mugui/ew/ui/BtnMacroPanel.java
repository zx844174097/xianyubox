package com.mugui.ew.ui;

import com.mugui.DataSaveInterface;
import com.mugui.ManagerInterface;
import com.mugui.Dui.DFrame;
import com.mugui.Dui.DPanel;
import com.mugui.ew.DataSave;
import com.mugui.ew.EWUIHandel;
import com.mugui.tool.UiTool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

public class BtnMacroPanel extends DPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -767635980566543556L;
	private DPanel body = null;
	private DataSave dataSave = (DataSave) EWUIHandel.datasave;
	private DFrame father = null;

	public BtnMacroPanel() {
		setLayout(new BorderLayout(0, 0));
		ManagerInterface datasaveManager = (ManagerInterface) dataSave.loader.loadClassToObject("com.mugui.manager.DefaultDataSaveManager");
		datasaveManager.init();

		DataSaveInterface scriptDataSave = (DataSaveInterface) datasaveManager.get("com.mugui.script.DataSave");
		scriptDataSave.init();

		body = (DPanel) scriptDataSave.start();
		add(body, BorderLayout.CENTER);
	}

	private Dimension now_Dimension = new Dimension(1200, 800);
	private Point now_point = null;

	@Override
	public void init() {
		body.init();
		UiTool.全体透明(this);
		if (father == null)
			father = dataSave.frame;
		if (now_point != null)
			father.setLocation(now_point);
		father.setSize(now_Dimension);
	}

	@Override
	public void quit() {
		if (body != null)
			body.quit();
	}

	@Override
	public void dataInit() {
	}

	@Override
	public void dataSave() {
	}
}
