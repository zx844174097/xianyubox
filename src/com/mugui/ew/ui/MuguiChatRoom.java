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

public class MuguiChatRoom extends DPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1536840243839923382L;
	private DataSave dataSave = (DataSave) EWUIHandel.datasave;
	private DFrame father = null;
	private Dimension now_Dimension = new Dimension(1200, 800);
	private Point now_point = null;

	public MuguiChatRoom() {
		setLayout(new BorderLayout(0, 0));
		setBackground(null);
		ManagerInterface datasaveManager = (ManagerInterface) dataSave.loader.loadClassToObject("com.mugui.manager.DefaultDataSaveManager");
		datasaveManager.init();

		DataSaveInterface chatroomDataSave = (DataSaveInterface) datasaveManager.get("com.mugui.chatroom.DataSave");
		chatroomDataSave.init();

		DPanel body = (DPanel) chatroomDataSave.start();
		add(body, BorderLayout.CENTER);
		UiTool.全体透明(this);
	}

	@Override
	public void init() {
		if (father == null)
			father = dataSave.frame;
		if (now_point != null)
			father.setLocation(now_point);
		father.setSize(now_Dimension);
	}

	@Override
	public void quit() {
		now_point = father.getLocation();
		now_Dimension = father.getSize();
	}

	@Override
	public void dataInit() {
	}

	@Override
	public void dataSave() {
	}

}
