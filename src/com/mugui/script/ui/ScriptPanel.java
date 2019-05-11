package com.mugui.script.ui;

import com.mugui.Dui.DPanel;
import com.mugui.script.DataSave;
import com.mugui.tool.UiTool;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScriptPanel extends DPanel {
	public ScriptPanel() {
		setLayout(new BorderLayout(0, 0));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3188462545264146370L;
	private DPanel left = null;
	private DPanel center = null;

	@Override
	public void init() {
		if (left == null) {
			viewInit();
		}
		left.init();
		UiTool.全体透明(this);
	}

	private void viewInit() {
		DataSave.loader.loadClass("com.mugui.script.ScriptThread");
		DataSave.loader.loadClass("com.mugui.script.ScriptBean");
		DataSave.loader.loadClass("com.mugui.script.ui.FunctionTree"); 
		DataSave.loader.loadClass("com.mugui.script.ui.EditViewPanel");
		left = DataSave.uiManager.get("ListPanel");
		add(left, BorderLayout.WEST);
		left.addMouseListener(left_Listener);
		center = DataSave.uiManager.get("EditPanel");
	}

	private MouseAdapter left_Listener = new MouseAdapter() {
		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}
	};

	@Override
	public void quit() {
		if (left != null)
			left.quit();
		if (center != null)
			center.quit();
	}

	@Override
	public void dataInit() {

	}

	@Override
	public void dataSave() {

	}

	public DPanel GetCenter() {
		return center;
	}

}
