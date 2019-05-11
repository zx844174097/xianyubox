package com.mugui.script;

import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;

import com.mugui.windows.Tool;

public class ScriptThread extends Thread {
	private ScriptBean bean = null;

	public ScriptThread(ScriptBean object) {
		this.bean = object;
	}

	private static final HashMap<String, Object> ObjectMap = new HashMap<>();

	@Override
	public void run() {
		if (bean.body == null)
			return;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bean.body), Charset.forName("utf-8")));
		try {
			String string;
			while ((string = reader.readLine()) != null) {
				string = string.trim();
				String[] body = string.split(" ");
				FunctionBean bean = FunctionBean.createFunctionBean(body[0]);
				if (bean == null)
					continue;
				Object object = ObjectMap.get(bean.class_name);
				if (object == null) {
					object = DataSave.loader.loadClassToObject(bean.class_name);
					if (object == null)
						throw new NullPointerException(bean.class_name + " not find");
					ObjectMap.put(bean.class_name, object);
				}
				Class<?> c[] = new Class[body.length - 1];
				Object zhi[] = new Object[c.length];
				for (int i = 0; i < c.length; i++) {
					switch (bean.parameters[i]) {
					case FunctionBean.FunctionParameter.INT:
						c[i] = int.class;
						zhi[i] = Integer.parseInt(body[i + 1]);
						break;
					case FunctionBean.FunctionParameter.MOUSE_BUTTON:
						c[i] = int.class;
						int s = Integer.parseInt(body[i + 1]);
						s = 4 - s; 
						zhi[i] = (int) Math.pow(2, s + 1);
						break;
					case FunctionBean.FunctionParameter.STRING:
						c[i] = String.class;
						zhi[i] = body[i + 1];
						break;
					case FunctionBean.FunctionParameter.KEY_CODE:
						c[i] = int.class;
						zhi[i] = Tool.getkeyCode(body[i + 1]);
						break;

					}
				}
				Method method = object.getClass().getDeclaredMethod(bean.function, c);
				if (method != null)
					method.invoke(object, zhi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
