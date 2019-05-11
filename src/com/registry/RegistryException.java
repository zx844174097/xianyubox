/*
 * RegistryException.java    2011/08/07
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
 * This exception class is designed to notify
 * the user for when an error has occurred while
 * reading or writing registry values.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 */
public class RegistryException extends RuntimeException {
	static final long serialVersionUID = -2578862491734133457L;
	
	/**
	 * Constructs a new exception.
	 *
	 * @param msg The error message to display to the user.
	 */
	public RegistryException(String msg) {
		super(msg);
	}
}