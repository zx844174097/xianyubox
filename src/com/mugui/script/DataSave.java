package com.mugui.script;

import java.awt.Point;
import com.mugui.DataClassLoaderInterface;
import com.mugui.model.ModelManagerInterface;
import com.mugui.tool.DataClassLoader;
import com.mugui.ui.UIManagerInterface;
import com.mugui.ui.mainUI;

public class DataSave implements com.mugui.DataSaveInterface {

	public static final mainUI StaticUI = com.mugui.ui.DataSave.StaticUI;
	public static final String JARFILEPATH = com.mugui.ui.DataSave.JARFILEPATH;
	public static DataClassLoaderInterface loader = new DataClassLoader();
	public static ModelManagerInterface modelManager = null;
	public static UIManagerInterface uiManager = null;
	public static Point USER_LOCATION = null;
	public static String USER_LOCATION_NAME = null;
	public static int SCREEN_X;
	public static int SCREEN_Y;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	public Object init() {
		if (uiManager == null) {
			if (null != loader.loadClass("com.mugui.manager.DefaultUIManager")) {
				uiManager = (UIManagerInterface) loader.loadClassToObject("com.mugui.script.UIManager");
				uiManager.init();
			}
		}
		if (modelManager == null) {
			if (null != loader.loadClass("com.mugui.manager.DefaultModelManager")) {
				modelManager = (ModelManagerInterface) DataSave.loader.loadClassToObject("com.mugui.script.ModelManager");
				modelManager.init();
			}
		}
		loader.setDataClassPath("com/mugui/script/data/");
		return null;
	}

	@Override
	public Object quit() {
		System.out.println("script->CloseALL");
		uiManager.clearAll(); 
		modelManager.clearAll();
		return null;
	}

	@Override
	public Object start() {
		uiManager.get("ScriptPanel").dataInit();
		return uiManager.get("ScriptPanel");
	}

	@Override
	public UIManagerInterface getUIManager() {
		return uiManager;
	}

	@Override
	public ModelManagerInterface getModelManager() {
		return modelManager;
	}

}
