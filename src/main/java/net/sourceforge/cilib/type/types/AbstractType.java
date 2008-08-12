/*
 * Copyright (C) 2003 - 2008
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

/**
 * @author Gary Pampara
 */
public abstract class AbstractType implements Type {
	private static final long serialVersionUID = 8719572276888639657L;

	/**
	 * {@inheritDoc}
	 */
	public abstract boolean equals(Object obj);
	
	/**
	 * {@inheritDoc}
	 */
	public abstract int hashCode(); 
	
	/**
	 * Utility method. Change the values within the <code>Type</code>, randomly, based on the
	 * upper and lower bounds that are defined for that <code>Type</code>
	 */
	public abstract void randomise();

	/**
	 * Utility method. Change the values within the <code>Type</code> to the default value as
	 * defined by Java primitives. All variables will be set to 0 or false.
	 */
	public abstract void reset();

	/**
	 * This method is used to print out the data values of the <code>Type</code>. This is probably
	 * not the best way to get the data out, but it is a valid workaround, based on the current
	 * implementation of the Simulator class. Also, seeing as the results are returned as a
	 * <code>String</code> at the end of the day when it is written to the output destination,
	 * using the <code>toString()</code> to perform such output is valid. <b>TODO: Find a better
	 * solution than toString() ?</b>
	 * @return The data value of the <code>Type</code>
	 */
	public abstract String toString();

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
	public abstract String getRepresentation();

	/**
	 * Get the dimension of the <code>Type</code>.
	 * @return The dimension of the <code>Type</code>
	 */
	public abstract int getDimension();

	/**
	 * All sub-classes should override this method to determine if this <tt>Type</tt> object's value lies within the upper and lower bounds of the domain.
	 * @return <tt>true</tt> if the value of this <tt>Type</tt> object is within the upper and lower bounds of the domain; <tt>false</tt> otherwise.
	 */
	public abstract boolean isInsideBounds();
}
