/*
 * WinRegistryAPI.java    2011/08/09
 * Copyright (C) 2011  Yinon Michaeli
 *
 * This file is part of JRegistry.
 *
 * JRegistry is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact by e-mail if you discover any bugs or if you have a suggestion
 * to myinon2005@hotmail.com
 */

package com.registry;

final class WinRegistryAPI {
	private WinRegistryAPI() {
	}
	
	static native String ExpandEnvironmentStrings(String str);
	static native RegistryQuota GetSystemRegistryQuota();
	static native int RegCloseKey(long hKey);
	static native long[] RegConnectRegistry(String machine, long hKey);
	static native int RegCopyTree(long hKey, String subKey, long hKey2);
	static native long[] RegCreateKeyEx(long hKey, String subKey, int options, int sam, String clazz);
	static native int RegDeleteKey(long hKey, String subKey);
	static native int RegDeleteKeyEx(long hKey, String subKey, int sam);
	static native int RegDeleteKeyValue(long hKey, String subKey, String value);
	static native int RegDeleteLink(long hKey);
	static native int RegDeleteTree(long hKey, String subKey);
	static native int RegDeleteValue(long hKey, String value);
	static native int RegDisableReflectionKey(long hKey);
	static native int RegEnableReflectionKey(long hKey);
	static native String RegEnumKeyEx(long hKey, int subKeyIndex, int maxKeyLen);
	static native String RegEnumValue(long hKey, int valueIndex, int maxValueLen);
	static native int RegFlushKey(long hKey);
	static native String RegGetLinkLocation(long hKey);
	static native int[] RegIsLinkKey(long hKey);
	static native int RegLoadKey(long hKey, String subKey, String filePath);
	static native int RegNotifyChangeKeyValue(long hKey, boolean watchSubtree, int dwNotifyFilter, long hEvent, boolean asynchronous);
	static native long[] RegOpenKeyEx(long hKey, String subKey, int sam);
	static native int[] RegQueryInfoKey(long hKey);
	static native Object[] RegQueryMultipleValues(long hKey, String[] valueNames);
	static native boolean RegQueryReflectionKey(long hKey);
	static native Object[] RegQueryValueEx(long hKey, String value);
	static native int RegRenameKey(long hKey, String newKeyName);
	static native int RegReplaceKey(long hKey, String subKey, String newFile, String oldFile);
	static native int RegRestoreKey(long hKey, String filePath, int restoreFlag);
	static native int RegSaveKey(long hKey, String filePath);
	static native int RegSaveKeyEx(long hKey, String filePath, int flags);
	static native int RegSetLinkValue(long hKey, String value, String data);
	static native int RegSetValueEx(long hKey, String value, int type, Object data);
	static native int RegUnLoadKey(long hKey, String subKey);
	static native boolean SetPrivilege(boolean enable, String privilege);
}