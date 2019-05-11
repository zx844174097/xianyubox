package com.mugui.GJ.ui;

import java.awt.Point;
import com.mugui.DataClassLoaderInterface;
import com.mugui.model.ModelManagerInterface;
import com.mugui.ui.UIManagerInterface;
import com.mugui.ui.mainUI;
import com.mugui.windows.Tool;

public class DataSave implements com.mugui.DataSaveInterface {

	public static final mainUI StaticUI = com.mugui.ui.DataSave.StaticUI;
	public static final String JARFILEPATH = com.mugui.ui.DataSave.JARFILEPATH;
	public static DataClassLoaderInterface loader = com.mugui.ui.DataSave.loader;
	public static ModelManagerInterface modelManager = null;
	public static UIManagerInterface uiManager = null;
	public static Point USER_LOCATION = null;
	public static String USER_LOCATION_NAME = null;
	public static int SCREEN_X;
	public static int SCREEN_Y;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	public Object init() {
		loader.setDataClassPath("com/mugui/GJ/Data/");
		Tool.setRootPath(JARFILEPATH + "\\古剑奇谭");

		if (uiManager == null) {
			if (null != loader.loadClass("com.mugui.manager.DefaultUIManager")) {
				uiManager = (UIManagerInterface) loader.loadClassToObject("com.mugui.GJ.ui.UIManager");
				uiManager.init();
			}
		}
		if (modelManager == null) {
			if (null != loader.loadClass("com.mugui.manager.DefaultModelManager")) {
				modelManager = (ModelManagerInterface) DataSave.loader.loadClassToObject("com.mugui.GJ.model.ModelManager");
				modelManager.init();
			}
		}
		loader.loadClassToObject("com.mugui.GJ.model.GJTool");
		return null;
	}

	@Override
	public Object quit() {
		System.out.println("古剑->CloseALL");
		uiManager.clearAll();
		modelManager.clearAll();
		return null;
	}

	@Override
	public Object start() {
		modelManager.get("GameListenerThread").start();
		uiManager.get("BodyPanel").dataInit();
		return uiManager.get("BodyPanel");
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
