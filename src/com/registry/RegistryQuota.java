/*
 * RegistryQuota.java    2012/09/14
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
 * A class which contains the size limits of the system registry.
 * The class holds information for the maximum size and the current size
 * of the system registry.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 * @since 1.8
 */
public final class RegistryQuota implements Comparable<RegistryQuota>, java.io.Serializable {
	static final long serialVersionUID = -6768409861399644553L;
	
	private final int dwQuotaAllowed;
	private final int dwQuotaUsed;
	
	private RegistryQuota(int dwQuotaAllowed, int dwQuotaUsed) {
		// Don't let outside classes instantiate this
		this.dwQuotaAllowed = dwQuotaAllowed;
		this.dwQuotaUsed = dwQuotaUsed;
	}
	
	/**
	 * Retrieves the current size of the registry in bytes.
	 *
	 * @return The current size of the whole system registry.
	 */
	public int getCurrentSize() {
		return this.dwQuotaUsed;
	}
	
	/**
	 * Retrieves the maximum size allowed for the registry in bytes.
	 *
	 * @return The maximum size allowed for the whole system registry.
	 */
	public int getMaximumSize() {
		return this.dwQuotaAllowed;
	}
	
	/**
	 * Tests for equality with another {@link Object}.
	 * Two {@link Object}s are equal if they are
	 * both of type <code>RegistryQouta</code>, and both
	 * size attributes are the same.
	 *
	 * @param o An {@link Object} to test equality with.
	 * @return <code>true</code> if the two are equal,
	 *         <code>false</code> otherwise.
	 */
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof RegistryQuota))
			return false;
		RegistryQuota rq = (RegistryQuota) o;
		if (this.dwQuotaAllowed != rq.dwQuotaAllowed)
			return false;
		if (this.dwQuotaUsed != rq.dwQuotaUsed)
			return false;
		return true;
	}
	
	/**
	 * Generates a hash code for <code>this</code> structure.
	 *
	 * @return The hash code for <code>this</code>.
	 */
	public int hashCode() {
		return this.dwQuotaAllowed ^ this.dwQuotaUsed;
	}
	
	/**
	 * Retrieve a {@link String} representation of <code>this</code> qouta object.
	 *
	 * @return A {@link String} representation for the value.
	 */
	public String toString() {
		return "Maximum size: " + this.dwQuotaAllowed + " bytes, Current size: " + this.dwQuotaUsed + " bytes";
	}
	
	/**
	 * Compares one {@code RegistryQuota} to another.
	 *
	 * @param other The {@code RegistryQuota} to compare with {@code this}.
	 * @return -1, 0, or 1 if {@code this} is smaller than, equal to
	 *         or greater than {@code other} respectively.
	 */
	public int compareTo(RegistryQuota other) {
		int cmp = this.dwQuotaAllowed - other.dwQuotaAllowed;
		if (cmp == 0)
			cmp = this.dwQuotaUsed - other.dwQuotaUsed;
		return cmp;
	}
}