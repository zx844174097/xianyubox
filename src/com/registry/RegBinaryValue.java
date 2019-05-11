/*
 * RegBinaryValue.java    2011/08/07
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
import static com.registry.RegistryKey.REG_BINARY;
import static com.registry.RegistryKey.REG_FULL_RESOURCE_DESCRIPTOR;
import static com.registry.RegistryKey.REG_LINK;
import static com.registry.RegistryKey.REG_NONE;
import static com.registry.RegistryKey.REG_RESOURCE_LIST;
import static com.registry.RegistryKey.REG_RESOURCE_REQUIREMENTS_LIST;
import static com.registry.RegistryKey.REG_VALUE_DATA;
import static com.registry.RegistryKey.REG_VALUE_TYPE;
import static com.registry.WinRegistryAPI.RegQueryValueEx;
import static com.registry.WinRegistryAPI.RegSetValueEx;

/**
 * <p>
 * This class represents a named value of type REG_BINARY, REG_NONE, REG_LINK,
 * REG_RESOURCE_LIST, REG_FULL_RESOURCE_DESCRIPTOR, or REG_RESOURCE_REQUIREMENTS_LIST.
 * The type specific data for the value will be an array of {@code byte}s.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 * @see RegistryValue
 */
public class RegBinaryValue extends RegistryValue {
	static final long serialVersionUID = 5098519913987257547L;
	
	/**
	 * Constructor used by {@link RegistryKey} to create a new representation of a registry value
	 * of type REG_BINARY, REG_NONE, REG_LINK, REG_RESOURCE_LIST,
	 * REG_FULL_RESOURCE_DESCRIPTOR, or REG_RESOURCE_REQUIREMENTS_LIST.
	 *
	 * @param key The {@link RegistryKey} that contains the value.
	 * @param name The name of the value.
	 * @param type The type of the value.
	 * @param value An array of {@code byte}s representing the data for the value.
	 */
	RegBinaryValue(RegistryKey key, String name, ValueType type, byte[] value) {
		super(key, name, type, value);
	}
	
	/**
	 * Constructor used by {@link RegistryKey} to create a new representation of a registry value
	 * of type REG_BINARY, REG_NONE, REG_LINK, REG_RESOURCE_LIST,
	 * REG_FULL_RESOURCE_DESCRIPTOR, or REG_RESOURCE_REQUIREMENTS_LIST.
	 *
	 * @param key The {@link RegistryKey} that contains the value.
	 * @param name The name of the value.
	 * @param type The type of the value.
	 * @param value An array of {@code byte}s representing the data for the value.
	 * @since 1.4
	 */
	RegBinaryValue(RegistryKey key, String name, int type, byte[] value) {
		super.key = key;
		super.name = name;
		super.type = null;
		super.value = value;
		super.typei = type;
	}
	
	/**
	 * Retrieves the type specific form of the data for <code>this</code> value
	 * which is an array of {@code byte}s.
	 * <br><br>
	 * If the type was changed through a call to {@link #setValueType(ValueType)}
	 * and is no longer of type REG_BINARY, REG_NONE, REG_LINK, REG_RESOURCE_LIST,
	 * REG_FULL_RESOURCE_DESCRIPTOR, or REG_RESOURCE_REQUIREMENTS_LIST,
	 * then the byte form of the data is returned.
	 *
	 * @return An array of bytes representing the data.
	 */
	public byte[] getValue() {
		if (super.typei != REG_BINARY && super.typei != REG_NONE && super.typei != REG_LINK
		&& super.typei != REG_RESOURCE_LIST && super.typei != REG_FULL_RESOURCE_DESCRIPTOR
		&& super.typei != REG_RESOURCE_REQUIREMENTS_LIST)
			return super.getByteData();
		return (byte[]) super.value;
	}
	
	/**
	 * Used to set the data of <code>this</code> value.
	 * If the type is no longer of type REG_BINARY, REG_NONE, REG_LINK,
	 * REG_RESOURCE_LIST, REG_FULL_RESOURCE_DESCRIPTOR, or REG_RESOURCE_REQUIREMENTS_LIST,
	 * then a call to {@link #setByteData(byte[])} is made.
	 *
	 * @param value A {@code byte} array representing the new data for the value.
	 *              <strong>NOTE</strong>: As of version 1.7.6, this can now be a variable list of {@code byte}s.
	 * @return An integer error code which tells if the operation was successful or not.
	 *         To obtain a description of the error, use {@link RegistryKey#formatErrorMessage(int)}.
	 */
	public int setValue(byte... value) {
		if (super.typei != REG_BINARY && super.typei != REG_NONE && super.typei != REG_LINK
		&& super.typei != REG_RESOURCE_LIST && super.typei != REG_FULL_RESOURCE_DESCRIPTOR
		&& super.typei != REG_RESOURCE_REQUIREMENTS_LIST) // not correct type
			return super.setByteData(value); // call setByteData
		long[] open = super.key.openKey0(KEY_SET_VALUE); // open key for writing
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) { // open operation successful
			int error = RegSetValueEx(
				handle, super.name, super.typei, value); // try to change data
			if (error == ERROR_SUCCESS) {
				super.value = value; // set to new data if successful
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
	 * @throws RegistryException if the type is not REG_BINARY, REG_NONE, REG_LINK,
	 *                           REG_RESOURCE_LIST, REG_FULL_RESOURCE_DESCRIPTOR,
	 *                           or REG_RESOURCE_REQUIREMENTS_LIST; or an unknown value type.
	 * @since 1.5
	 */
	public int refreshData() {
		long[] open =  super.key.openKey0(KEY_QUERY_VALUE); // open key for reading
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) { // open operation successful
			Object[] info = RegQueryValueEx(handle, super.name);
			if (info != null) {
				int t = ((Integer) info[REG_VALUE_TYPE]).intValue();
				if ((t != REG_BINARY && t != REG_NONE && t != REG_LINK
				&& t != REG_RESOURCE_LIST && t != REG_FULL_RESOURCE_DESCRIPTOR
				&& t != REG_RESOURCE_REQUIREMENTS_LIST) || (t > 11)) // not correct type
					throw new RegistryException(
						super.name + " is no longer of type REG_BINARY, REG_NONE, REG_LINK, " +
						"REG_RESOURCE_LIST, REG_FULL_RESOURCE_DESCRIPTOR, " +
						"or REG_RESOURCE_REQUIREMENTS_LIST."); // throw exception
				super.typei = t;
				super.type = RegistryKey.getValueType(t);
				super.value = (byte[]) info[REG_VALUE_DATA];
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
		StringBuffer buf = new StringBuffer("[");
		byte[] v = this.getValue();
		for (int i = 0; i < v.length; i++) {
			buf.append(Integer.toHexString(v[i]));
			if (i < v.length - 1)
				buf.append(", ");
		}
		buf.append("]");
		return super.toString() + " Value: " + buf.toString();
	}
}