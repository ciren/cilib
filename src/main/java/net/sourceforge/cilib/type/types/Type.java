/*
 * Type.java
 * 
 * Created on Oct 16, 2004
 *
 * Copyright (C)  2004 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 */
package net.sourceforge.cilib.type.types;

import java.io.Serializable;

/**
 * @author Gary Pampara
 */
public interface Type extends Serializable {
	public Type clone();

	public boolean equals(Object other);

	public int hashCode();

	/**
	 * Utility method. Change the values within the <code>Type</code>, randomly, based on the
	 * upper and lower bounds that are defined for that <code>Type</code>
	 */
	public void randomise();

	/**
	 * Utility method. Change the values within the <code>Type</code> to the default value as
	 * defined by Java primitives. All variables will be set to 0 or false.
	 */
	public void reset();

	/**
	 * This method is used to print out the data values of the <code>Type</code>. This is probably
	 * not the best way to get the data out, but it is a valid workaround, based on the current
	 * implementation of the Simulator class. Also, seeing as the results are returned as a
	 * <code>String</code> at the end of the day when it is written to the output destination,
	 * using the <code>toString()</code> to perform such output is valid. <b>TODO: Find a better
	 * solution than toString() ?</b>
	 * @return The data value of the <code>Type</code>
	 */
	public String toString();

	/**
	 * Get the representation of the type in the form expressed by the domain notation.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li>R(-30,30)</li>
	 * <li>Z(-7,4)</li>
	 * <li>B</li>
	 * </ul>
	 * @return A <code>String</code> representing the <code>Type</code> in domain notation.
	 */
	public String getRepresentation();

	/**
	 * Get the dimension of the <code>Type</code>.
	 * @return The dimension of the <code>Type</code>
	 */
	public int getDimension();

	public boolean isInsideBounds();

}
