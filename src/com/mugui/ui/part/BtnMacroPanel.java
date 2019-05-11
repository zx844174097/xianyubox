package com.mugui.ui.part;

import com.mugui.DataSaveInterface;
import com.mugui.ManagerInterface;
import com.mugui.Dui.DPanel;
import com.mugui.tool.UiTool;
import com.mugui.ui.DataSave;

import java.awt.BorderLayout;

public class BtnMacroPanel extends DPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -767635980566543556L;
	private DPanel body = null;

	public BtnMacroPanel() {
		setLayout(new BorderLayout(0, 0));
		ManagerInterface datasaveManager = (ManagerInterface) DataSave.loader.loadClassToObject("com.mugui.manager.DefaultDataSaveManager");
		datasaveManager.init();

		DataSaveInterface scriptDataSave = (DataSaveInterface) datasaveManager.get("com.mugui.script.DataSave");
		scriptDataSave.init();

		body = (DPanel) scriptDataSave.start();
		add(body, BorderLayout.CENTER);
	}

	@Override
	public void init() {
		body.init();
		UiTool.全体透明(this);
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
