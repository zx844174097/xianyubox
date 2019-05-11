package com.mugui.Dui;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.Window;

public class DOptionPanel extends DDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6793075875875477330L;
	public final static int OPTION_OK = 0;
	public final static int OPTION_OK_CANCEL = 1;
	public static final int AGREE = 0;
	public static final int RET_YES = DInputDialog.RET_YES;

	public DOptionPanel(Window parentComponent, String title) {
		this(parentComponent, title, null, OPTION_OK);
	}

	public DOptionPanel(Window parentComponent, String title, String message, int optionType) {
		super(parentComponent, title, true);
		switch (optionType) {
		case OPTION_OK:
			dialog = new DInputDialog(title, message, true, false);
			break;
		case OPTION_OK_CANCEL:
			dialog = new DInputDialog(title, message, true, true);
			break;
		}
		setKai(320, 120);
		add(dialog);
		dialog.setLock(this);
	}

	private DInputDialog dialog = null;

	private Object getInputValue() {
		return dialog.getInputBoxText();
	}

	public static String showInputDialog(Container parentComponent, String message, String title, int optionType) throws HeadlessException {
		DOptionPanel pane = new DOptionPanel(GetWindow(parentComponent), title, message, optionType);
		pane.dialog.openInputBox();
		pane.setVisible(true);

		return pane.getInputValue().toString();
	}

	public static int showMessageDialog(Container parentComponent, String message, String title, int optionType) {
		DOptionPanel panel = new DOptionPanel(GetWindow(parentComponent), title, message, optionType);
		panel.setVisible(true);
		return (int) panel.getInputValue();
	}

	private static Window GetWindow(Container parentComponent) {
		if (parentComponent == null)
			return null;
		if (parentComponent instanceof Window)
			return (Window) parentComponent;
		GetWindow(parentComponent.getParent());
		return null;
	}

}
