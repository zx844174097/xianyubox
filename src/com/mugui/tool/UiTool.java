package com.mugui.tool;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JTextPane;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DTextArea;
import com.mugui.ui.DataSave;

public class UiTool {
	public static void 全体透明(Container container) {
		if (DataSave.低配模式)
			return;
		Component[] components = container.getComponents();
		if (components.length < 1)
			return;
		for (Component c : components) {
			if (c instanceof JTextPane) {
				continue;
			}
			if (c instanceof JComponent && !(c instanceof DButton)) {
				c.setBackground(null);
				((JComponent) c).setOpaque(false);
				全体透明((JComponent) c);
			}

		}
	}
}
