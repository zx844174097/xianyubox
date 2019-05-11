/*
 * SystemTime.java    2011/08/07
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
 * A class which represents the last write time for a registry key.
 * This class is capable of tracking the year, month, day of the week, day of the month,
 * hour, minute, second, and milliseconds for when the key was last written to.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 */
public final class SystemTime implements Comparable<SystemTime>, java.io.Serializable {
	static final long serialVersionUID = -5472959631374046825L;
	
	// The year. The valid values for this member are 1601 through 30827.
	private static final byte WYEAR         = 0;
	
	/*
	 * The month. This member can be one of the following values.
	 * Value	Meaning
	 * ----------------------
	 * 1		January
	 * 2		February
	 * 3		March
	 * 4		April
	 * 5		May
	 * 6		June
	 * 7		July
	 * 8		August
	 * 9		September
	 * 10		October
	 * 11		November
	 * 12		December
	 */
	private static final byte WMONTH        = 1;
	
	/*
	 * The day of the week. This member can be one of the following values.
	 * Value	Meaning
	 * ----------------------
	 * 0		Sunday
	 * 1		Monday
	 * 2		Tuesday
	 * 3		Wednesday
	 * 4		Thursday
	 * 5		Friday
	 * 6		Saturday
	 */
	private static final byte WDAY_OF_WEEK  = 2;

	// The day of the month. The valid values for this member are 1 through 31.
	private static final byte WDAY          = 3;
	
	// The hour. The valid values for this member are 0 through 23.
	private static final byte WHOUR         = 4;
	
	// The minute. The valid values for this member are 0 through 59.
	private static final byte WMINUTE       = 5;
	
	// The second. The valid values for this member are 0 through 59.
	private static final byte WSECOND       = 6;
	
	// The millisecond. The valid values for this member are 0 through 999.
	private static final byte WMILLISECONDS = 7;
	
	private final int[] timeInfo; // An array which stores the time information
	
	private SystemTime(int[] timeInfo) {
		// Don't let outside classes instantiate this
		this.timeInfo = timeInfo;
	}
	
	/**
	 * Retrieves the year for when the key was last written to.
	 * The valid values are 1601 through 30827.
	 *
	 * @return The year when the key was last written to.
	 */
	public int getYear() {
		return this.timeInfo[WYEAR];
	}
	
	/**
	 * Retrieves the month for when the key was last written to.
	 * This can be one of the following values:
	 * <br><br>
	 * <pre>
	 * Value   Meaning<br>
	 * ------------------<br>
	 * 1       January<br>
	 * 2       February<br>
	 * 3       March<br>
	 * 4       April<br>
	 * 5       May<br>
	 * 6       June<br>
	 * 7       July<br>
	 * 8       August<br>
	 * 9       September<br>
	 * 10      October<br>
	 * 11      November<br>
	 * 12      December<br>
	 * </pre>
	 *
	 * @return The month when the key was last written to.
	 */
	public int getMonth() {
		return this.timeInfo[WMONTH];
	}
	
	/**
	 * Retrieves the day of the week for when the key was last written to.
	 * This can be one of the following values:
	 * <br><br>
	 * <pre>
	 * Value   Meaning<br>
	 * ------------------<br>
	 * 0       Sunday<br>
	 * 1       Monday<br>
	 * 2       Tuesday<br>
	 * 3       Wednesday<br>
	 * 4       Thursday<br>
	 * 5       Friday<br>
	 * 6       Saturday<br>
	 * </pre>
	 *
	 * @return The day of the week when the key was last written to.
	 */
	public int getDayOfWeek() {
		return this.timeInfo[WDAY_OF_WEEK];
	}
	
	/**
	 * Retrieves the day of the month for when the key was last written to.
	 *
	 * @return The day of the month when the key was last written to.
	 */
	public int getDay() {
		return this.timeInfo[WDAY];
	}
	
	/**
	 * Retrieves the hour for when the key was last written to.
	 * The value returned can be from 0 to 23.
	 *
	 * @return The hour when the key was last written to.
	 */
	public int getHour() {
		return this.timeInfo[WHOUR];
	}
	
	/**
	 * Retrieves the minute for when the key was last written to.
	 *
	 * @return The minute when the key was last written to.
	 */
	public int getMinute() {
		return this.timeInfo[WMINUTE];
	}
	
	/**
	 * Retrieves the seconds for when the key was last written to.
	 *
	 * @return The seconds when the key was last written to.
	 */
	public int getSeconds() {
		return this.timeInfo[WSECOND];
	}
	
	/**
	 * Retrieves the milliseconds for when the key was last written to.
	 *
	 * @return The milliseconds when the key was last written to.
	 */
	public int getMilliseconds() {
		return this.timeInfo[WMILLISECONDS];
	}
	
	/**
	 * Tests for equality with another {@link Object}.
	 * Two {@link Object}s are equal if they are
	 * both of type <code>SystemTime</code>, and all
	 * of the time attributes are the same.
	 *
	 * @param o An {@link Object} to test equality with.
	 * @return <code>true</code> if the two are equal,
	 *         <code>false</code> otherwise.
	 */
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SystemTime))
			return false;
		SystemTime st = (SystemTime) o;
		for (int i = 0; i < 8; i++) {
			if (this.timeInfo[i] != st.timeInfo[i])
				return false;
		}
		return true;
	}
	
	/**
	 * Generates a hash code for <code>this</code> structure.
	 *
	 * @return The hash code for <code>this</code>.
	 */
	public int hashCode() {
		int hash = 31;
		for (int i = 0; i < 8; i++) {
			hash += 31 * this.timeInfo[i];
		}
		return hash;
	}
	
	/**
	 * Retrieve a {@link String} representation of <code>this</code> time.
	 * The format is Month/Day/Year - Hour:Minute (AM or PM).
	 *
	 * @return A {@link String} representation for the value.
	 */
	public String toString() {
		return this.getMonth() + "/" + this.getDay() + "/" + this.getYear() +
			" - " + (this.getHour() == 0 ? 12 :
				(this.getHour() > 12 ? this.getHour() - 12 :
				this.getHour())) + ":" + (this.getMinute() > 10 ?
				this.getMinute() : "0" + this.getMinute()) +
			(this.getHour() < 12 ? " AM" : " PM");
	}
	
	/**
	 * Compares one {@code SystemTime} to another.
	 *
	 * @param other The {@code SystemTime} to compare with {@code this}.
	 * @return -1, 0, or 1 if {@code this} is smaller than, equal to
	 *         or greater than {@code other} respectively.
	 * @since 1.6.3
	 */
	public int compareTo(SystemTime other) {
		for (int i = 0; i < 8; i++) {
			if (this.timeInfo[i] < other.timeInfo[i]) {
				return -1;
			} else {
				if (this.timeInfo[i] > other.timeInfo[i]) {
					return 1;
				}
			}
		}
		return 0;
	}
}