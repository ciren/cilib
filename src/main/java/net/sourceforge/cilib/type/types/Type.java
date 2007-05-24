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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

/**
 * @author Gary Pampara
 */
public abstract class Type implements Serializable {// Externalizable {
	public abstract Type clone();

	public abstract boolean equals(Object other);

	public abstract int hashCode();

	// public Type get(int index);

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
	 * Serailise the <code>Type</code> onto the <code>ObjectOutput</code>. The serialisation is
	 * a minimal type of serialisation, with only the needed information being serialised.
	 * @param oos The <code>ObjectOutput</code> interface onto with the serialised data should be
	 *        written.
	 * @throws IOException if I/O errors occur while writing to the underlying stream
	 */
	@Deprecated
	public abstract void writeExternal(ObjectOutput oos) throws IOException;

	/**
	 * Get the data back from the serialised stream.
	 * <p>
	 * <b><code>readExternal()</code> must read the data values in the same order in which they
	 * where written.</b>
	 * @param ois
	 * @throws IOException if I/O errors occur while writing to the underlying stream
	 * @throws ClassNotFoundException If the class for an object being restored cannot be found.
	 */
	@Deprecated
	public abstract void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException;

	/**
	 * Helper method to perform the serialisation of the <code>Type</code> object. This method
	 * defers invocation to <code>writeExternal()</code>
	 * @param oo The <code>ObjectOutput</code> to use to serialise the data.
	 * @throws IOException if I/O errors occur while writing to the underlying stream
	 */
	@Deprecated
	public void serialise(ObjectOutput oo) throws IOException {
		this.writeExternal(oo);
	}

	/**
	 * Helper method to perform the de-serialisation of the <code>Type</code> object. This method
	 * defers invocation to <code>readExternal()</code>
	 * @param oi The <code>ObjectInput</code> to use to de-serialise the data.
	 * @throws IOException if I/O errors occur while writing to the underlying stream.
	 * @throws ClassNotFoundException If the class for an object being restored cannot be found.
	 */
	@Deprecated
	public void deserialise(ObjectInput oi) throws IOException, ClassNotFoundException {
		this.readExternal(oi);
	}
}
