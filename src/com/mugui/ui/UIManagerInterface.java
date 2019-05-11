package com.mugui.ui;

import com.mugui.ManagerInterface;
import com.mugui.Dui.DPanel;

public interface UIManagerInterface extends ManagerInterface{
	@Override
	DPanel del(String name);
	@Override
	DPanel get(String name);
}
