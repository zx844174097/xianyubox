package com.mugui.script;

import java.util.Arrays;
import java.util.HashMap;

public class FunctionBean {
	public String name;
	public String class_name;
	public String function;
	public String explanation;
	public int[] parameters = null;

	public FunctionBean(String name, String function, String explanation, int... parameters) {
		this(name, com.mugui.windows.Tool.class.getName(), function, explanation, parameters);
	}

	public FunctionBean(String name, String class_name, String function, String explanation, int... parameters) {
		this.name = name;
		this.class_name = class_name;
		this.function = function;
		this.explanation = explanation;
		this.parameters = parameters;
		functionMap.put(name, this);
	}

	@Override
	public String toString() {
		return name;
	}

	public static class FunctionParameter {
		public final static int INT = 0;
		public final static int STRING = 1;
		public final static int KEY_CODE = 2;
		public final static int MOUSE_BUTTON = 3;
		public final static int LOGIC_BODY = 5;
	}

	public String to() {
		return name + " " + class_name + " " + function + " " + explanation + " " + Arrays.toString(parameters);
	}

	private static final HashMap<String, FunctionBean> functionMap = new HashMap<>();

	/**
	 * 创建一个函数
	 * 
	 * @param body
	 * @return
	 */
	public static FunctionBean createFunctionBean(String body) {
		return functionMap.get(body);
	}
}
