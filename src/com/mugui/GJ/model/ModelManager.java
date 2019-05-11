package com.mugui.GJ.model;

import com.mugui.ModelInterface;
import com.mugui.manager.DefaultModelManager;

public class ModelManager extends DefaultModelManager {

	@Override
	public ModelInterface get(String name) {
		return super.get("com.mugui.GJ.model." + name);
	}

}
