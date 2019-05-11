package com.mugui.ew;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mugui.DataClassLoaderInterface;
import com.mugui.DataSaveInterface;
import com.mugui.Dui.DFrame;
import com.mugui.ew.ui.MainFrame;
import com.mugui.model.ModelManagerInterface;
import com.mugui.ui.UIManagerInterface;

public class DataSave implements DataSaveInterface {

	private UIManagerInterface uiManager = null;

	public DataClassLoaderInterface loader = null;
	public DFrame frame = null;
	public String JARFILEPATH = com.mugui.ui.DataSave.JARFILEPATH + "\\额外功能";

	public DataSave() {
		loader = EWUIHandel.loader;
	}

	public UIManagerInterface getUIManager() {
		return uiManager;
	}

	private ModelManagerInterface modelManager = null;

	public ModelManagerInterface getModelManager() {
		return modelManager;
	}

	@Override
	public Object init() {
		System.out.println(this.getClass().getName() + " init");
		if (uiManager == null)
			uiManager = (UIManagerInterface) loader.loadClassToObject(this.getClass().getPackage().getName() + ".ui.UIManager");
		if (modelManager == null)
			modelManager = (ModelManagerInterface) loader.loadClassToObject(this.getClass().getPackage().getName() + ".model.ModelManager");
		uiManager.init();
		modelManager.init();
		if (frame == null)
			frame = (DFrame) loader.loadClassToObject(this.getClass().getPackage().getName() + ".ui.MainFrame");

		System.getProperties().put("Frame", frame);
		return null;
	}

	@Override
	public Object quit() {

		uiManager.clearAll();
		modelManager.clearAll();
		System.out.println(this.getClass().getName() + " quit");
		if (frame != null)
			frame.dispose();
		frame = null;
		return null;
	}

	@Override
	public Object start() {
		System.out.println(this.getClass().getName() + " start");
		frame.setVisible(true);
		return null;
	}

	/**
	 * 得到资源
	 * 
	 * @param string
	 */
	public byte[] getData(String string) {
		InputStream inputStream = loader.getResourceAsStream(string);
		if (inputStream == null)
			return null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] bs = new byte[1024];
		int len = 0;
		try {
			while ((len = inputStream.read(bs)) > 0) {
				outputStream.write(bs, 0, len);
			}
			outputStream.close();
			inputStream.close();
			return outputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
