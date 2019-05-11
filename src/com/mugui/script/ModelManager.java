package com.mugui.script;

import com.mugui.ModelInterface;
import com.mugui.manager.DefaultModelManager;

public class ModelManager extends DefaultModelManager {

	@Override
	public ModelInterface get(String name) {
		return super.get("com.mugui.script.model." + name);
	}

}
