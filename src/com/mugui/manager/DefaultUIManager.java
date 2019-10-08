package com.mugui.manager;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.mugui.Dui.DPanel;
import com.mugui.ui.UIManagerInterface;

public class DefaultUIManager implements UIManagerInterface {

	private  ConcurrentHashMap<String, DPanel> uiMap = null;
	public boolean is(String name) {
		return uiMap.get(name)!=null;
	}
	@Override
	public synchronized DPanel get(String name) {
		DPanel modelInterface = uiMap.get(name);
		if (modelInterface != null) {
			return modelInterface; 
		}
		
		modelInterface = (DPanel) loader.loadClassToObject(name);
		if (modelInterface == null)
			throw new NullPointerException("not find " + name);
		add(name, modelInterface);
		return modelInterface;
	}

	@Override
	public synchronized DPanel del(String name) {
		return uiMap.remove(name);
	}

	@Override
	public synchronized void clearAll() {
		Iterator<Entry<String, DPanel>> iterator = uiMap.entrySet().iterator();
		while (iterator.hasNext()) {
			DPanel panel = iterator.next().getValue();
			panel.dataSave();
			panel.quit();
			panel.dispose();
			iterator.remove();
		}
		uiMap.clear();
	}

	@Override
	public void init() {
		if (uiMap == null)
			uiMap = new ConcurrentHashMap<>();
	}

	@Override
	public void add(String name, Object object) {
		uiMap.put(name, (DPanel) object);

	}

}
