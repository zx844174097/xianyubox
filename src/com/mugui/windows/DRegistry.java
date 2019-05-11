package com.mugui.windows;

import java.io.FileNotFoundException;
import com.registry.RegistryKey;
import com.registry.RegistryValue;
import com.registry.ValueType;

public class DRegistry {
	public static final RegistryKey HKEY_CURRENT_USER = RegistryKey.getRootKeyForIndex(RegistryKey.HKEY_CURRENT_USER_INDEX);
	public static final RegistryKey HKEY_LOCAL_MACHINE = RegistryKey.getRootKeyForIndex(RegistryKey.HKEY_LOCAL_MACHINE_INDEX);

	/**
	 * 读取注册表某个位置的key值
	 * 
	 * @param path
	 * @param key
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getHkeyCurrentUserValue(String path, String key) throws FileNotFoundException {
		if (key == null || key.trim().equals("")) {
			throw new NullPointerException("NULL的key值");
		}
		RegistryKey registryKey = new RegistryKey(HKEY_CURRENT_USER, path);
		if (!registryKey.hasValues()) {
			throw new FileNotFoundException("该路径不存在：" + path);
		}
		RegistryValue value = registryKey.getValue(key);
		if (value == null) {
			throw new FileNotFoundException("该路径：" + path + "中的该key：" + key + "无值");
		}
		return value.getValue().toString();
	}

	/**
	 * 判断注册表是否可以读写
	 * 
	 * @param pathRun
	 * @return
	 */
	public static boolean isReadAndWrite(String pathRun) {
		RegistryKey r = new RegistryKey(HKEY_CURRENT_USER, "Software");
		if (r.canWrite()) {
			return true;
		}
		return false;
	}

	public static boolean setHkeyCurrentUserValue(String path, String key, byte[] body) {
		RegistryKey r = new RegistryKey(HKEY_CURRENT_USER, "Software\\JavaSoft\\Prefs");
		RegistryValue value = r.newValue(key, ValueType.REG_NONE);
		if (value == null) {
			value = r.getValue(key);
		}
		int i = value.setByteData(body);
		return i == 0;
	}

	public static String getHkeyLocalMachineValue(String path, String key) throws FileNotFoundException {
		if (key == null || key.trim().equals("")) {
			throw new NullPointerException("NULL的key值");
		}
		RegistryKey registryKey = new RegistryKey(HKEY_LOCAL_MACHINE, path);
		if (!registryKey.hasValues()) {
			throw new FileNotFoundException("该路径不存在：" + path);
		}
		RegistryValue value = registryKey.getValue(key);
		if (value == null) {
			throw new FileNotFoundException("该路径：" + path + "中的该key：" + key + "无值");
		}
		return value.getValue().toString();
	}
}
