package com.mugui.ew.model;

import com.mugui.ModelInterface;
import com.mugui.manager.DefaultModelManager;

public class ModelManager extends DefaultModelManager {
	@Override
	public ModelInterface get(String name) {
		ModelInterface modelInterface = super.get(this.getClass().getPackage().getName() + "." + name);
		if (modelInterface == null) {
			modelInterface = super.get(this.getClass().getPackage().getName() + ".ErrorModel");
		} 
		return modelInterface;
	}
}
