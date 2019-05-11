/*
 * RegistryValue.java    2012/09/14
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

import static com.registry.RegistryKey.ERROR_INVALID_PARAMETER;
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
import static com.registry.WinRegistryAPI.RegQueryValueEx;

/**
 * <p>
 * This class represents a named value that is found in the registry.
 * Each value has a specific name which refers to the value
 * as well as a type. The different types can be found in {@link ValueType}.
 * </p><p>
 * The data that is associated with the value depends on the type.
 * For instance if a value was of type REG_SZ, then the data is a {@link String} type.
 * If the type was REG_MULTI_SZ, then the data is an array of {@link String}s.
 * </p><p>
 * This class can only retrieve the data in binary form. In order to obtain the data
 * in its expected form, this class must be type cast into the appropriate class
 * representing the value type for the named value.
 * <br>
 * For example: REG_SZ values must be type cast into {@link RegStringValue}.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 */
public abstract class RegistryValue implements Cloneable, Comparable<RegistryValue>, java.io.Serializable {
	static final long serialVersionUID = -5424474989836803217L;
	
	/**
	 * The name of the value.
	 */
	protected String name;
	
	/**
	 * The type of the value.
	 */
	protected ValueType type;
	
	/**
	 * An object which holds the data. Used in subclasses.
	 */
	protected Object value;
	
	/**
	 * The key that contains {@code this} value.
	 */
	protected RegistryKey key;
	
	/**
	 * The integer representing the type of the value.
	 */
	protected int typei;
	
	/**
	 * Used to construct a new {@link RegistryValue}. This is provided for use by
	 * subclasses.
	 *
	 * @since 1.4
	 */
	RegistryValue() {
		this.key = null;
		this.name = null;
		this.type = null;
		this.value = null;
		this.typei = 0;
	}
	
	/**
	 * Used to construct a new {@link RegistryValue}. This is provided for use by
	 * subclasses.
	 *
	 * @param key The {@link RegistryKey} that contains the value.
	 * @param name The name of the value.
	 * @param type The type of the value.
	 * @param value An {@link Object} representing the data for the value.
	 */
	RegistryValue(RegistryKey key, String name, ValueType type, Object value) {
		this.key = key;
		this.name = name;
		this.type = type;
		this.value = value;
		this.typei = type.getValue();
	}
	
	/**
	 * Creates a shallow copy of <code>this</code>.
	 *
	 * @return A copy of <code>this</code> value.
	 */
	public Object clone() {
		try {
			RegistryValue clone = (RegistryValue) super.clone();
			clone.name = this.name;
			clone.type = this.type;
			clone.value = this.value;
			clone.typei = this.typei;
			clone.key = this.key;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}
	
	/**
	 * Implements the compareTo method in the interface {@link Comparable}.
	 * Allows a list of <code>RegistryValue</code>s to be sorted in ascending order.
	 * {@code Null} values always come before non-{@code null} values.
	 *
	 * @param o A <code>RegistryValue</code> to compare against <code>this</code>.
	 * @return An integer describing which one should be sorted first:
	 *         if < 0, <code>this</code> should go first
	 *         if > 0, <code>o</code> should go first
	 */
	public int compareTo(RegistryValue o) {
		if (o == null)
			return 1;
		int cmp = this.key.compareTo(o.key);
		if (cmp == 0) {
			cmp = this.name.compareToIgnoreCase(o.name);
			if (cmp == 0) {
				if (this.type == null) {
					if (o.type != null && this.typei != o.typei)
						cmp = -1;
				} else if (o.type == null) {
					if (this.typei != o.typei)
						cmp = 1;
				} else {
					cmp = this.type.compareTo(o.type);
				}
				if (cmp == 0) {
					int len1 = this.getByteLength();
					int len2 = o.getByteLength();
					cmp = len1 - len2;
					if (cmp == 0) {
						byte[] b1 = this.getByteData();
						byte[] b2 = o.getByteData();
						for (int i = 0; i < b1.length; i++) {
							cmp = b1[i] - b2[i];
							if (cmp != 0)
								break;
						}
					}
				}
			}
		}
		return cmp;
	}
	
	/**
	 * Tests for equality with another {@link Object}.
	 * Two {@link Object}s are equal if they are
	 * both of type <code>RegistryValue</code>, they are both
	 * found in the same key, and their names, type, and
	 * data are the same.
	 *
	 * @param o An {@link Object} to test equality with.
	 * @return <code>true</code> if the two are equal,
	 *         <code>false</code> otherwise.
	 */
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof RegistryValue))
			return false;
		RegistryValue v = (RegistryValue) o;
		return this.compareTo(v) == 0;
	}
	
	/**
	 * Used to retrieve the registry key that contains <code>this</code> value.
	 *
	 * @return The registry key containing <code>this</code>.
	 * @since 1.8
	 */
	public RegistryKey getKey() {
		return this.key;
	}
	
	/**
	 * Used to retrieve the name of <code>this</code> value.
	 *
	 * @return The name of the value.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Calculates the hash code value for <code>this</code> value.
	 *
	 * @return The hash code for <code>this</code>.
	 */
	public int hashCode() {
		return (this.key.hashCode() ^ this.name.hashCode() ^ this.typei ^ this.getByteLength());
	}
	
	/**
	 * Renames <code>this</code> value to <code>name</code>.
	 */
	int setName(String name) {
		if (!this.name.equalsIgnoreCase(name)) {
			byte[] old = this.getByteData();
			this.name = name;
			return this.setByteData(old); // makes the change in the registry.
		}
		return ERROR_INVALID_PARAMETER;
	}
	
	/**
	 * Used to retrieve the type of <code>this</code> value.
	 *
	 * @return The type of the value.
	 */
	public ValueType getValueType() {
		return this.type;
	}
	
	/**
	 * Used to retrieve the type of <code>this</code> value.
	 *
	 * @return The type of the value as an integer.
	 * @since 1.4
	 */
	public int getValueTypeInt() {
		return this.typei;
	}
	
	/**
	 * Used to change the type of <code>this</code> value.
	 *
	 * @param type A {@link ValueType} describing what the new type will be.
	 */
	public void setValueType(ValueType type) {
		if (!this.type.equals(type)) {
			this.typei = type.getValue();
			this.type = type;
			byte[] old = this.getByteData();
			this.setByteData(old); // makes the change in the registry.
		}
	}
	
	/**
	 * Used to change the type of <code>this</code> value.
	 *
	 * @param type An int describing what the new type will be.
	 * @since 1.4
	 */
	public void setValueTypeInt(int type) {
		if (this.typei != type) {
			this.typei = type;
			this.type = RegistryKey.getValueType(this.typei);
			byte[] old = this.getByteData();
			this.setByteData(old); // makes the change in the registry.
		}
	}
	
	/**
	 * This method is used to update the data for <code>this</code> value
	 * because the data does not update automatically if it was changed.
	 *
	 * @return An integer error code which tells if the operation was successful or not.
	 *         To obtain a description of the error, use {@link RegistryKey#formatErrorMessage(int)}.
	 * @since 1.5
	 */
	public abstract int refreshData();
	
	/**
	 * Used to retrieve the byte information of the data from
	 * the registry. If the specific type is wanted,
	 * then <code>this</code> must be type cast to the corresponding
	 * subclass based on the type of the value.
	 * <br><br>
	 * If the data cannot be read, then {@code null} is returned.
	 *
	 * @return An array of bytes which represent the data of the value
	 *         or null if the data cannot be read.
	 */
	public byte[] getByteData() {
		long[] open = this.key.openKey0(KEY_QUERY_VALUE); // open the key for reading
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) { // open operation was successful
			byte[] data = this.getByteData(handle, this.name); // read the data
			this.key.closeKey0(open[0]); // close the key
			open = null;
			return data;
		}
		this.key.closeKey0(open[0]); // close the key if unsuccessful
		open = null;
		return null;
	}
	
	// native method to retrieve the bytes
	private native byte[] getByteData(long hKey, String name);
	
	/**
	 * Used to change the bytes of the data for <code>this</code> value.
	 *
	 * @param value An array of bytes which will be the new data for the value.
	 *              NOTE: As of version 1.7.6, this can now be a variable list of bytes.
	 * @return An integer error code which tells if the operation was successful or not.
	 *         To obtain a description of the error, use {@link RegistryKey#formatErrorMessage(int)}.
	 */
	public int setByteData(byte... value) {
		long[] open = this.key.openKey0(KEY_SET_VALUE | KEY_QUERY_VALUE); // open key for writing
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) { // open operation successful
			int error = this.setByteData(
				handle, this.name, this.typei, value); // try to change data
			if (error == ERROR_SUCCESS) {
				if (this.typei == REG_NONE || this.typei == REG_BINARY
				|| this.typei == REG_LINK || this.typei == REG_RESOURCE_LIST
				|| this.typei == REG_FULL_RESOURCE_DESCRIPTOR
				|| this.typei == REG_RESOURCE_REQUIREMENTS_LIST) {
					this.value = value; // change the value in the class
				} else {
					Object[] info = RegQueryValueEx(handle, this.name);
					if (info != null) {
						this.value = info[REG_VALUE_DATA]; // Need type specific data
					} else {
						this.value = null;
					}
				}
			}
			this.key.closeKey0(open[0]); // close the key
			open = null;
			return error;
		}
		this.key.closeKey0(open[0]); // close the key if unsuccessful
		open = null;
		return this.key.getLastError();
	}
	
	// native method to set the bytes
	private native int setByteData(long hKey, String name, int type, byte[] data);
	
	/**
	 * Used to retrieve the number of bytes the data takes up
	 * in the registry.
	 *
	 * @return The number of bytes for the value's data or -1 if the value doesn't exist.
	 */
	public int getByteLength() {
		long[] open = this.key.openKey0(KEY_QUERY_VALUE); // open the key for reading
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) { // open operation was successful
			int length = this.getByteLength(handle, this.name); // read the data
			this.key.closeKey0(open[0]); // close the key
			open = null;
			return length;
		}
		this.key.closeKey0(open[0]); // close the key if unsuccessful
		open = null;
		return -1;
	}
	
	// native method to get number of bytes this value's data takes up
	private native int getByteLength(long hKey, String name);
	
	/**
	 * Retrieve a {@link String} representation of <code>this</code> value.
	 *
	 * @return A {@link String} representation for the value.
	 */
	public String toString() {
		String s="Name: " + this.name + " Type: " + this.type;
		return s;
	}

	public Object getValue() {
		return value;
	}
	
}