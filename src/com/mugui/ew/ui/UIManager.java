package com.mugui.ew.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mugui.Dui.DPanel;

public class UIManager extends com.mugui.manager.DefaultUIManager {
	@Override
	public synchronized DPanel get(String name) {
		if (name.indexOf(".") == -1) {
			name = this.getClass().getPackage().getName() + "." + name;
		}
		DPanel panel = super.get(name);

		if (panel == null) {
			DPanel errorPanel = (DPanel) super.get(this.getClass().getPackage().getName() + ".ErrorPanel");
			try {
				Method method = errorPanel.getClass().getMethod("setErrorMessage", String.class);
				method.invoke(errorPanel, name);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			panel = errorPanel;

		}
		return panel;
	}
}
