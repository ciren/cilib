/*
 * InferiorFitness.java
 * 
 * Created on Jul 24, 2004
 *
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
 * This class is used to represent a fitness value that is always inferior.
 * <p />
 * This class is a singleton.
 * 
 * @author Edwin Peer
 */
public class InferiorFitness extends AbstractFitness {

	private static final long serialVersionUID = -2129092436359289935L;

	private InferiorFitness() {
		
	}
	
	public InferiorFitness getClone() {
		return instance;
	}
	
	/**
	 * Always returns null. <code>InferiorFitness</code> does not have a value. The
	 * most sensible value to return is Double.NaN as it is still a value, however,
	 * it represents something that is not a number (effectively null). Returning 
	 * Double.NaN will ensure that some of the Measurements do get an value, even if
	 * the value is Double.NaN
	 * 
	 * @return null
	 */
	public Double getValue() {
		return Double.NaN;
	}

	/**
	 * Returns -1, unless other is also the <code>InferiorFitness</code> instance. 
	 * 
	 * @return 0 if other is <code>InferiorFitness</code> instance, -1 otherwise. 
	 */
	public int compareTo(Fitness other) {
		if (this == other) {
			return 0;
		}
		else {
			return -1;
		}
	}
	
	public boolean equals(Object object) {
		Fitness otherFitness = (Fitness) object;
		return getValue().equals(otherFitness.getValue());
	}
	
	/**
	 * Obtain a reference to the <code>InferiorFitness</code> instance.
	 * 
	 * @return the <code>InferiorFitness</code> instance.
	 */
	public static Fitness instance() {
		return instance;
	}
	
	private static InferiorFitness instance = new InferiorFitness();
  
}
