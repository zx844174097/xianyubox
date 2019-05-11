package com.teamdev.jxbrowser.chromium.swing.internal;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;

import com.teamdev.jxbrowser.chromium.swing.internal.HeavyWeightWidget;

final class k implements Runnable {
	HeavyWeightWidget a;

	k(HeavyWeightWidget var1) {
		this.a = var1;
		a.setDragAndDropEnabled(false);
	}

	private static Method method_d = null;
	private static Method method_e = null;
	static {
		try {
			method_d = HeavyWeightWidget.class.getDeclaredMethod("d", HeavyWeightWidget.class);
			method_e = HeavyWeightWidget.class.getDeclaredMethod("e", HeavyWeightWidget.class);
			method_d.setAccessible(true);
			method_e.setAccessible(true);

		} catch (NoSuchMethodException | SecurityException e) {
			//e.printStackTrace();
		}

	}

	public final void run() {
		try {
			method_d.invoke(null, a);
			method_e.invoke(null, a);
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			//e.printStackTrace();
		}

		// HeavyWeightWidget.d(this.a);
		// HeavyWeightWidget.e(this.a);
	}
}