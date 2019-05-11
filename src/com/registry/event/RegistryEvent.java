/*
 * RegistryEvent.java    2011/08/07
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

package com.registry.event;

import com.registry.RegistryKey;

/**
 * <p>
 * This class represents a registry event for
 * when a registry key changes.
 * These changes can be anywhere from
 * a key or value being added to a key
 * or value being renamed.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 */
public class RegistryEvent {
	private int id;
	private RegistryKey key;
	
	/**
	 * Constructs a <code>new</code> RegistryEvent.
	 *
	 * @param id the id for the watched registry key.
	 * @param key the watched registry key.
	 */
	public RegistryEvent(int id, RegistryKey key) {
		this.id = id;
		this.key = key;
	}
	
	/**
	 * Returns the watch id number for the registry
	 * key. This can be used to tell different
	 * events apart from each other by looking for
	 * matching ids.
	 *
	 * @return the id for watched key.
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Used to retrieve the key that is being watched.
	 *
	 * @return the registry key that is being watched.
	 */
	public RegistryKey getKey() {
		return this.key;
	}
}