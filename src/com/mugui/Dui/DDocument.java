package com.mugui.Dui;

import javax.swing.text.*;

import com.mugui.tool.Other;

public class DDocument extends PlainDocument {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3468295257544045006L;
	private int len;
	private boolean insertbool = true;

	public void setInsertBool(boolean boo) {
		insertbool = boo;
	}

	public boolean isInsertBool() {
		return insertbool;
	}

	public DDocument(int len) {
		this.len = len;
	}

	public DDocument() {
		this(10);
	}

	// 重载父类的insertString函数
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {

		if (!insertbool) {
			return;
		}
		if (getLength() + str.length() > len) {// 这里假定你的限制长度为10
			return;
		}
		
		
		
		switch (option) {
		case OPTION_INT:
			String temp = getText(0, getLength()) + str;
			if (!Other.isInteger(temp))
				return;
		default:
			break;
		}
		super.insertString(offset, str, a);

	}

	public static final int OPTION_INT = 1;
	private int option = 0;

	public void setOption(int option) {
		this.option = option;
	}

	public int getOption() {
		return option;
	}

}