package com.mugui.ui.part;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class qpListRenderer implements ListCellRenderer<qpOtherPanel>{

	@Override
	public Component getListCellRendererComponent(
			JList<? extends qpOtherPanel> list, qpOtherPanel value, int index,
			boolean isSelected, boolean cellHasFocus) {
		value.setPreferredSize(new Dimension(300, 80));
		return value;
	}

}
