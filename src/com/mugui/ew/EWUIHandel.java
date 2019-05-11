package com.mugui.ew;

import com.mugui.DataClassLoaderInterface;
import com.mugui.DataSaveInterface;
import com.mugui.ModelInterface;

public class EWUIHandel implements ModelInterface {

	public static DataSaveInterface datasave = null;
	public static DataClassLoaderInterface loader = null;

	@Override
	public void init() {
		if (loader == null) {
			loader = (DataClassLoaderInterface) new DataClassLoaderInterface().loadClassToObject("com.mugui.tool.DataClassLoader");
			loader.setDataClassPath("com/mugui/ew/data/");
		}
		if (datasave == null)
			datasave = (DataSaveInterface) loader.loadClassToObject(this.getClass().getPackage().getName() + ".DataSave");
		if (datasave != null)
			datasave.init();

	}

	@Override
	public void start() {
		if (datasave != null)
			datasave.start();
	}

	@Override
	public boolean isrun() {

		return false;
	}

	@Override
	public void stop() {
		if (datasave != null)
			datasave.quit();
	}

}
