/*
 * VectorMath.java
 * 
 * Created on Apr 30, 2006
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
package net.sourceforge.cilib.math;

import net.sourceforge.cilib.type.types.Vector;

/**
 * Interface to define operations associated with Vector mathematics.
 * 
 * @author Gary Pampara
 */
public interface VectorMath {
	
	/**
	 * Adding this {@see net.sourceforge.cilib.type.types.Vector} to another 
	 * will result in a resultant {@see net.sourceforge.cilib.type.types.Vector}.
	 * 
	 * @param vector The {@see net.sourceforge.cilib.type.types.Vector} to add to the current one
	 * @return The resultant {@see net.sourceforge.cilib.type.types.Vector}
	 */
	public Vector plus(Vector vector);

	/**
	 * 
	 * @param vector
	 * @return
	 */
	public Vector subtract(Vector vector);
	
	/**
	 * 
	 * @param vector
	 * @return
	 */
	public Vector divide(Vector vector);
	public Vector divide(double scalar);
	
	/**
	 * 
	 * @param vector
	 * @return
	 */
	public Vector multiply(Vector vector);
	public Vector multiply(double scalar);
	
	/**
	 * Calculate the norm of this <tt>Vector</tt> object. All the elements must
	 * be of type {@see net.sourceforge.cilib.type.types.Numeric}.
	 * 
	 * @return The value of the vector norm 
	 */	
	public double norm();
	
	
	/**
	 * Calculate the vector dot product of the current <tt>Vector</tt> and the 
	 * given <tt>Vector</tt>.
	 * 
	 * @param vector The given <tt>Vector</tt> object with which the vector dot 
	 *                product is to be calculated.
	 * @return The dot product value.
	 */
	public double dot(Vector vector);
	
	
	/**
	 * Get the cross-product vector based on the current <tt>Vector</tt> and the
	 * given <tt>Vector</tt>.
	 * 
	 * @param vector The specified <tt>Vector</tt> with with the cross product operation is to be performed.
	 * @return The orthoganol vector to the current and the specified <tt>Vector</tt>.
	 */
	public Vector cross(Vector vector);
}
