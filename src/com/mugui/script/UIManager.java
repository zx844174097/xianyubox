package com.mugui.script;

import com.mugui.Dui.DPanel;

public class UIManager extends com.mugui.manager.DefaultUIManager {
	@Override
	public synchronized DPanel get(String name) {
		return super.get("com.mugui.script.ui." + name); 
	}
}
