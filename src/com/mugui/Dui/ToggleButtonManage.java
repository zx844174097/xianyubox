package com.mugui.Dui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JToggleButton;

public class ToggleButtonManage {
	private LinkedList<JToggleButton> list = null;
	private JToggleButton button = null;
	private ActionListener listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			button = (JToggleButton) e.getSource();
			for(JToggleButton jToggleButton:list) {
				if(jToggleButton!=button) {
					jToggleButton.setSelected(false);
				}
			} 
		}
	};

	public ToggleButtonManage() {
		list = new LinkedList<>();
	}

	public JToggleButton getSelectButton() {
		return button;
	}

	public void addRadioButton(JToggleButton button) {
		if (this.button == null) {
			this.button = button;
			button.setSelected(true);
		}
		list.add(button);
		button.addActionListener(listener);
	}

	public void removeRadioButton(JToggleButton button) {
		list.remove(button);
		button.removeActionListener(listener);
	}
}
