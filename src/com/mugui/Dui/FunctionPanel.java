package com.mugui.Dui;

public abstract class FunctionPanel extends DPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3299806055432489823L;

	private String functionName = null;

	public FunctionPanel(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionName() {

		return functionName;
	}

	abstract public void init();

	abstract public void quit();

	abstract public void dataInit();

	abstract public void dataSave();

}
