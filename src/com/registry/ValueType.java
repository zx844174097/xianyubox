/*
 * ValueType.java    2011/08/07
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

/**
 * <p>
 * An enumeration representing all of the different value types
 * available in the Windows Registry.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 */
public enum ValueType {
	/**
	 * No defined value type.
	 */
	REG_NONE               (com.registry.RegistryKey.REG_NONE,                "REG_NONE"),
	
	/**
	 * A string value.
	 */
	REG_SZ                 (com.registry.RegistryKey.REG_SZ,                  "REG_SZ"),
	
	/**
	 * A string value that contains unexpanded references to environment variables.
	 */
	REG_EXPAND_SZ          (com.registry.RegistryKey.REG_EXPAND_SZ,           "REG_EXPAND_SZ"),
	
	/**
	 * Binary data in any form.
	 */
	REG_BINARY             (com.registry.RegistryKey.REG_BINARY,              "REG_BINARY"),
	
	/**
	 * A 32-bit number.
	 */
	REG_DWORD              (com.registry.RegistryKey.REG_DWORD,               "REG_DWORD"),
	
	/**
	 * A 32-bit number in little-endian format.
	 *
	 * Windows is designed to run on little-endian computer architectures.
	 * Therefore, this value is defined as REG_DWORD in the Windows header files.
	 */
	REG_DWORD_LITTLE_ENDIAN(com.registry.RegistryKey.REG_DWORD_LITTLE_ENDIAN, "REG_DWORD_LITTLE_ENDIAN"),
	
	/**
	 * A 32-bit number in big-endian format.
	 *
	 * Some UNIX systems support big-endian architectures.
	 */
	REG_DWORD_BIG_ENDIAN   (com.registry.RegistryKey.REG_DWORD_BIG_ENDIAN,    "REG_DWORD_BIG_ENDIAN"),
	
	/**
	 * A string that contains the target path of a symbolic
	 * link that was created by calling the RegCreateKeyEx
	 * function with REG_OPTION_CREATE_LINK.
	 */
	REG_LINK               (com.registry.RegistryKey.REG_LINK,                "REG_LINK"),
	
	/**
	 * An array of string values.
	 */
	REG_MULTI_SZ           (com.registry.RegistryKey.REG_MULTI_SZ,            "REG_MULTI_SZ"),
	
	/**
	 * Resource list in the resource map.
	 */
	REG_RESOURCE_LIST      (com.registry.RegistryKey.REG_RESOURCE_LIST,       "REG_RESOURCE_LIST"),
	
	/**
	 * Resource list in the hardware description.
	 */
	REG_FULL_RESOURCE_DESCRIPTOR (com.registry.RegistryKey.REG_FULL_RESOURCE_DESCRIPTOR, "REG_FULL_RESOURCE_DESCRIPTOR"),
	
	REG_RESOURCE_REQUIREMENTS_LIST (com.registry.RegistryKey.REG_RESOURCE_REQUIREMENTS_LIST, "REG_RESOURCE_REQUIREMENTS_LIST"),
	
	/**
	 * A 64-bit number.
	 */
	REG_QWORD              (com.registry.RegistryKey.REG_QWORD,               "REG_QWORD"),
	
	/**
	 * A 64-bit number in little-endian format.
	 *
	 * Windows is designed to run on little-endian computer architectures.
	 * Therefore, this value is defined as REG_QWORD in the Windows header files.
	 */
	REG_QWORD_LITTLE_ENDIAN(com.registry.RegistryKey.REG_QWORD_LITTLE_ENDIAN, "REG_QWORD_LITTLE_ENDIAN");
	
	private int value;
	private String name;
	
	/**
	 * Internal Constructor for the different enumeration entries.
	 *
	 * @param value The int value describing the value type.
	 * @param name The name of the type.
	 */
	ValueType(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	/**
	 * Retrieves the <code>int</code> representing the value
	 * as specified in the Windows API.
	 *
	 * @return The <code>int</code> which describes the value type.
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Used to retrieve the name of the value type.
	 *
	 * @return The name of the value type.
	 */
	public String getName() {
		return this.name;
	}
}