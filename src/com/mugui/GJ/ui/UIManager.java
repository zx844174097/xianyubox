package com.mugui.GJ.ui;

import com.mugui.Dui.DPanel;

public class UIManager extends com.mugui.manager.DefaultUIManager {
	@Override
	public synchronized DPanel get(String name) {
		return super.get("com.mugui.GJ.ui." + name); 
	}
}
