/*
 * RegistryStringValue.java    2011/08/07
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

import static com.registry.RegistryKey.ERROR_SUCCESS;
import static com.registry.RegistryKey.KEY_QUERY_VALUE;
import static com.registry.RegistryKey.KEY_SET_VALUE;
import static com.registry.RegistryKey.NATIVE_HANDLE;
import static com.registry.RegistryKey.NULL_NATIVE_HANDLE;
import static com.registry.RegistryKey.REG_EXPAND_SZ;
import static com.registry.RegistryKey.REG_SZ;
import static com.registry.RegistryKey.REG_VALUE_DATA;
import static com.registry.RegistryKey.REG_VALUE_TYPE;
import static com.registry.WinRegistryAPI.RegQueryValueEx;
import static com.registry.WinRegistryAPI.RegSetValueEx;

/**
 * <p>
 * This class represents a named value of type REG_SZ or REG_EXPAND_SZ.
 * The type specific data for the value will be a {@link String}.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 * @see RegistryValue
 */
public class RegStringValue extends RegistryValue {
	static final long serialVersionUID = -329641674663305163L;
	
	/**
	 * Constructor used by {@link RegistryKey} to create a new representation of a registry value
	 * of type REG_SZ or REG_EXPAND_SZ.
	 *
	 * @param key The {@link RegistryKey} that contains the value.
	 * @param name The name of the value.
	 * @param type The type of the value.
	 * @param value A {@link String} representing the data for the value.
	 */
	RegStringValue(RegistryKey key, String name, ValueType type, String value) {
		super(key, name, type, value);
	}
	
	/**
	 * Retrieves the type specific form of <code>this</code> value
	 * which is a {@link String}.
	 * <br><br>
	 * If the type was changed through a call to {@link #setValueType(ValueType)}
	 * and is no longer of type REG_SZ or REG_EXPAND_SZ,
	 * then a {@link RegistryException} is thrown.
	 *
	 * @return A {@link String} representing the data.
	 * @throws RegistryException if the type is not REG_SZ or REG_EXPAND_SZ.
	 */
	public String getValue() {
		if (!super.type.equals(ValueType.REG_SZ) && !super.type.equals(ValueType.REG_EXPAND_SZ))
			throw new RegistryException(super.name + " is no longer of type REG_SZ or REG_EXPAND_SZ.");
		return (String) super.value;
	}
	
	/**
	 * Used to set the data of <code>this</code> value.
	 * If the type is no longer of type REG_SZ or REG_EXPAND_SZ,
	 * then a {@link RegistryException} is thrown.
	 *
	 * @param value A {@link String} representing the new data for the value.
	 * @return An integer error code which tells if the operation was successful or not.
	 *         To obtain a description of the error, use {@link RegistryKey#formatErrorMessage(int)}.
	 * @throws RegistryException if the type is not REG_SZ or REG_EXPAND_SZ.
	 */
	public int setValue(String value) {
		if (!super.type.equals(ValueType.REG_SZ) && !super.type.equals(ValueType.REG_EXPAND_SZ)) // not correct type
			throw new RegistryException(super.name + " is no longer of type REG_SZ or REG_EXPAND_SZ."); // throw exception
		long[] open = super.key.openKey0(KEY_SET_VALUE); // open key for writing
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) { // open operation successful
			int error = RegSetValueEx(
				handle, super.name, super.type.getValue(), value); // try to change data
			if (error == ERROR_SUCCESS) {
				if (super.typei == REG_EXPAND_SZ) {
					if (RegistryKey.isAutoExpandEnvironmentVariables())
						super.value = RegistryKey.expandEnvironmentVariables(value);
					else
						super.value = value;
				} else {
					super.value = value; // set to new data if successful
				}
			}
			super.key.closeKey0(open[0]); // close the key
			open = null;
			return error;
		}
		super.key.closeKey0(open[0]); // close the key if unsuccessful
		open = null;
		return super.key.getLastError();
	}
	
	/**
	 * This method is used to update the data for <code>this</code> value
	 * because the data does not update automatically if it was changed.
	 *
	 * @return An integer error code which tells if the operation was successful or not.
	 *         To obtain a description of the error, use {@link RegistryKey#formatErrorMessage(int)}.
	 * @throws RegistryException if the type is not REG_SZ or REG_EXPAND_SZ.
	 * @since 1.5
	 */
	public int refreshData() {
		long[] open =  super.key.openKey0(KEY_QUERY_VALUE); // open key for reading
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) { // open operation successful
			Object[] info = RegQueryValueEx(handle, super.name);
			if (info != null) {
				int t = ((Integer) info[REG_VALUE_TYPE]).intValue();
				if (t != REG_SZ && t != REG_EXPAND_SZ) // not correct type
					throw new RegistryException(super.name + " is no longer of type REG_SZ or REG_EXPAND_SZ."); // throw exception
				super.typei = t;
				super.type = RegistryKey.getValueType(t);
				if (super.typei == REG_EXPAND_SZ) {
					if (RegistryKey.isAutoExpandEnvironmentVariables())
						super.value = RegistryKey.expandEnvironmentVariables((String) info[REG_VALUE_DATA]);
					else
						super.value = (String) info[REG_VALUE_DATA];
				} else {
					super.value = (String) info[REG_VALUE_DATA];
				}
				info = null;
			}
			super.key.closeKey0(open[0]);
		}
		open = null;
		return super.key.getLastError();
	}
	
	/**
	 * Retrieve a {@link String} representation of <code>this</code> value.
	 *
	 * @return A {@link String} representation for the value.
	 */
	public String toString() {
		try {
			return super.toString() + " Value: " + this.getValue();
		} catch (RegistryException e) {
			StringBuffer buf = new StringBuffer("[");
			byte[] v = super.getByteData();
			for (int i = 0; i < v.length; i++) {
				buf.append(Integer.toHexString(v[i]));
				if (i < v.length - 1)
					buf.append(", ");
			}
			buf.append("]");
			return super.toString() + " Value: " + buf.toString();
		}
	}
}