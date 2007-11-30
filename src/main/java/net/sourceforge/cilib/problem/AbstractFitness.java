/*
 * AbstractFitness.java
 * 
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.problem;

/**
 * Abstract class to provide the adapter class fo the <tt>Fitness</tt>
 * implementations. Even though they inherit from <tt>Type</tt> the
 * methods provided do not make sense for a <tt>Fitness</tt> object.
 * 
 * As a result, the methods are implemented and throw a
 * <tt>UnsupportedOperationException</tt>.
 */
public abstract class AbstractFitness implements Fitness {
	
	public abstract Fitness getClone();

	public abstract int compareTo(Fitness o);
	
	public abstract boolean equals(Object obj);

	public abstract Double getValue();

	/**
	 * @throws UnsupportedOperationException
	 */
	public int getDimension() {
		throw new UnsupportedOperationException("getDimension() does not make sense on a Fitness object.");
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public String getRepresentation() {
		throw new UnsupportedOperationException("getRepresentation() does not make sense on a Fitness object.");
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean isInsideBounds() {
		throw new UnsupportedOperationException("isInsideBounds() does not make sense on a Fitness object.");
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public void randomise() {
		throw new UnsupportedOperationException("randomise() does not make sense on a Fitness object.");
	}

	/**
	 * @throws UnsupportedOperationException 
	 */
	public void reset() {
		throw new UnsupportedOperationException("reset() does not make sense on a Fitness object.");
	}

}
