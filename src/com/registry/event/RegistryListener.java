/*
 * RegistryListener.java    2011/08/07
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

/**
 * <p>
 * This listener class is designed to listen for
 * changes within the registry. Specifically
 * it listenes for when keys or values are
 * added, removed, or modified.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 */
public interface RegistryListener {
	/**
	 * This method gets called when a change to the registry occurs.
	 *
	 * @param e the event that describes the change to
	 *          the registry.
	 */
	public void notifyChange(RegistryEvent e);
}