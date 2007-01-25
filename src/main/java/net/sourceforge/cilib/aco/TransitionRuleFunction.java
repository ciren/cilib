/*
 * TransitionRuleFunction.java
 * 
 * Created on Apr 24, 2004
 * 
 * Copyright (C) 2003 - 2006 
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
package net.sourceforge.cilib.aco;

import net.sourceforge.cilib.container.graph.Vertex;

/**
 * The point of this interface is to enable a generic method for the
 * transition rule used in ACO algorithms to be fully pluggable.
 * 
 * This is very useful when extending the standard rule to cater for other
 * eventuallities.
 * 
 * @author Gary Pampara
 */
public interface TransitionRuleFunction {
	/**
	 * Get the probability for moving from the current vertex to the connecting vertex
	 * @param ant The current <code>Ant</code> object
	 * @param currentVertex The <code>Vertex</code> the <code>Ant</code> is currently located at
	 * @param destinationVertex The desired <code>Vertex</code> to move to
	 * @return The probability for the ant to move from the current <code>Vertex</code> to the connecting <code>Vertex</code>
	 */
	public double getTransitionalProbability(Ant ant, Vertex currentVertex, Vertex destinationVertex);

	
	/**
	 * Clone the current <tt>TransitionRuleFunction</tt>.
	 * @return A cloned <tt>TransitionRuleFunction</tt>
	 */
	public TransitionRuleFunction clone();
}
