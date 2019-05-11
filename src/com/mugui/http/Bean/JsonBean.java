package com.mugui.http.Bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.json.JSONObject;

public abstract class JsonBean {
	public JsonBean() {
		this(null);
	}

	public JsonBean(JSONObject jsonObject) {
		InitBean(jsonObject);
	}

	public JSONObject toJsonObject() {
		Field[] field = getClass().getDeclaredFields();
		JSONObject object = new JSONObject();
		for (Field f : field) {
			if (Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			try {
				f.setAccessible(true);
				Object object2 = f.get(this);
				if (object2 != null)
					object.put(f.getName(), f.get(this));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	public void InitBean(JSONObject jsonObject) {
		if (jsonObject == null)
			return;
		if (jsonObject.isNullObject()) {
			return;
		}
		Field[] field = getClass().getDeclaredFields();
		for (Field f : field) {
			if (Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			f.setAccessible(true);
			Object object = jsonObject.get(f.getName());
			if (object != null) {
				try {
					Object object2 = null;
					try {
						object2 = f.getType().newInstance();
					} catch (InstantiationException e) {
					}
					if (object2 == null) {
						Method method = Number.class.getMethod(f.getType().getName() + "Value", null);
						f.set(this, method.invoke(object, null));
					} else if (object2 instanceof String) {
						f.set(this, object);
					} else if (object2 instanceof JsonBean) {
						((JsonBean) object2).InitBean(JSONObject.fromObject(object));
						f.set(this, object2);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static <T> T newInstanceBean(Class<T> class1, Object object) {
		try {
			Method method = class1.getMethod("InitBean", JSONObject.class);
			T t = class1.newInstance();
			method.invoke(t, JSONObject.fromObject(object));
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return toJsonObject().toString();
	}
}
