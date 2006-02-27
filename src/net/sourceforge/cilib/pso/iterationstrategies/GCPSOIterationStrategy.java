/*
 * GCPSOIterationStrategy.java
 * 
 * Created on Oct 17, 2005
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
 *
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.parameterupdatestrategies.RhoUpdateStrategy;
import net.sourceforge.cilib.pso.parameterupdatestrategies.StandardRhoUpdateStrategy;


/**
 * Iteration strategy for the GCPSO. This class is a decorator for another <code>IterationStrategy</code>
 * as the <code>rho</code> update is an extra step that is required.
 *  
 * @author Gary Pampara
 *
 */
public class GCPSOIterationStrategy implements IterationStrategy {
	
	private IterationStrategy strategy;
	private RhoUpdateStrategy rhoUpdateStrategy; 
	
	/**
	 * Create the <code>IterationStrategy</code> for the GCPSO 
	 *
	 */
	public GCPSOIterationStrategy() {
		strategy = new SynchronousIterationStrategy();
		rhoUpdateStrategy = new StandardRhoUpdateStrategy();
	}
	

	/**
	 * Perform the iteration of the GCPSO and then perform the required rho update
	 * @param pso The PSO for which the iteration must be done
	 */
	public void performIteration(PSO pso) {
		strategy.performIteration(pso);
		rhoUpdateStrategy.updateRho(pso);
	}
	
	
	/**
	 * Get the decorated <code>IterationStrategy</code>
	 * @return Returns the strategy.
	 */
	public IterationStrategy getStrategy() {
		return strategy;
	}

	
	/**
	 * Set the decorated <code>IterationStrategy</code>
	 * @param strategy The strategy to set.
	 */
	public void setStrategy(IterationStrategy strategy) {
		this.strategy = strategy;
	}


	/**
	 * Get the <code>rho</code> update strategy
	 * @return Returns the rhoUpdateStrategy.
	 */
	public RhoUpdateStrategy getRhoUpdateStrategy() {
		return rhoUpdateStrategy;
	}


	/**
	 * Set the <code>rho</code> update strategy
	 * @param rhoUpdateStrategy The rhoUpdateStrategy to set.
	 */
	public void setRhoUpdateStrategy(RhoUpdateStrategy rhoUpdateStrategy) {
		this.rhoUpdateStrategy = rhoUpdateStrategy;
	}

}
