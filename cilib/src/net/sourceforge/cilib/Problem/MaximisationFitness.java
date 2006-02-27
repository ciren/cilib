/*
 * MaximisationFitness.java
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
package net.sourceforge.cilib.Problem;

/**
 * @author espeer
 *
 * This class implements the <code>Comparable</code> interface for a maximisation problem. 
 * That is, larger fitness values have superior fitness.
 */
public class MaximisationFitness implements Fitness {

   /**
	* Constructs a new <code>MaximisationFitness</code> with the given fitness value.
	* 
	* @param value The actual vitness value for the problem.
    */
	public MaximisationFitness(Comparable value) {
		this.value = value;
	}

	public Comparable getValue() {
		return value;
	}

	/**
	 * Returns superior fitness for larger fitness values.
	 * 
	 * @return 1 for superior fitness, -1 for inferior fitness and 0 for equivalent fitness.
	 */
	public int compareTo(Object other) {
		if (other == InferiorFitness.instance()) {
			return 1;
		}
		return value.compareTo(((Fitness) other).getValue());
	}
	
	private Comparable value;
	
}
