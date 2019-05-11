package com.mugui.model;

import com.mugui.ManagerInterface;
import com.mugui.ModelInterface;

public interface ModelManagerInterface extends ManagerInterface{
	@Override
	ModelInterface del(String name);
	@Override
	ModelInterface get(String name);
	@Override
	void add(String name, Object object);
}
