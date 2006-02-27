/*
 * Fitness.java
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
 * This interface is an abstraction for the fitness of a solution to an optimisation problem.
 * The actual fitness value (as determined by the {@link OptimisationProblem} in question) 
 * can be obtained by calling {@link #getValue()} while fitnesses can be compared using the
 * standard Java <code>Comparable</code> interface.
 * <p />
 * <b>Example:</b> <br /> 
 * <code>
 * Fitness a = ...; <br />
 * Fitness b = ...; <br />
 * <br />
 * int result = a.compareTo(b); <br />
 * if (result > 0) { <br />
 *   // a is a superior fitness to b <br />
 * } <br />
 * else if (result < 0) { <br />
 *   // b is a superior fitness to a <br />
 * } <br />
 * else { <br />
 *  // a and b are equally fit <br />
 * } <br />
 * </code> <br />
 * 
 * @author espeer
 */
public interface Fitness extends Comparable {
	
	/**
	 * Returns the underlying fitness value.
	 * 
	 * @return the actual fitness value.
	 */
	public Comparable getValue();
}
