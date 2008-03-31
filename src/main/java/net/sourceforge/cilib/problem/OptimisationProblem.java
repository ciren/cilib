/*
 * OptimisationProblem.java
 *
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
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;

/**
 * Optimisation problems are characterised by a domain that specifies the search space and
 * a fitness given a potential solution. This interface ensures that an 
 * {@link net.sourceforge.cilib.algorithm.OptimisationAlgorithm} has 
 * all the information it needs to find a solution to a given optimisation problem. In addition, it is the
 * responsibility of an optimisation problem to keep track of the number of times the fitness has
 * been evaluated.
 * <p />
 * All optimisation problems must implement this interface.
 *
 * @author  Edwin Peer
 */
public interface OptimisationProblem extends Problem {

	/**
	 * {@inheritDoc}
	 */
	public OptimisationProblem getClone();
    
    /**
     * Returns the fitness of a potential solution to this problem. The solution object is described 
     * by the domain of this problem, see {@link #getDomain()}. An instance of {@link InferiorFitness} 
     * should be returned if the solution falls outside the search space of this problem.
     * 
     * @param solution The potential solution found by the optimisation algorithm.
     * @param count True if this call should contribute to the fitness evaluation count, see {@link #getFitnessEvaluations()}.
     * @return The fitness of the solution. 
     */
    public Fitness getFitness(Object solution, boolean count);
    
    /**
     * Returns the number of times the underlying fitness function has been evaluated. 
     * 
     * @return The number fitness evaluations.
     */
    public int getFitnessEvaluations();
    
    /**
     * Returns the domain component that describes the search space for this problem.
     * 
     * @return A {@link net.sourceforge.cilib.Domain.Component} object representing the search space.
     */
    public DomainRegistry getDomain();
    
    
    /**
     * Returns the domain component that describes the search space of the needed
     * behavioral characteristics of the problem. This is mainly used for ECs, however,
     * the use of behavioral parameters with PSO will be investigated.
     * 
     * @return The domain of the behavioral component
     */
    public DomainRegistry getBehaviouralDomain();
    
    
    /**
     * Get the associated {@linkplain DataSetBuilder}.
     * @return The currently associated {@linkplain DataSetBuilder}.
     */
    public DataSetBuilder getDataSetBuilder();
    
    
    /**
     * Set the {@linkplain DataSetBuilder} for this {@linkplain OptimisationProblem}.
     * @param dataSetBuilder The {@linkplain DataSetBuilder} to be set on the current
     *        {@linkplain OptimisationProblem}.
     */
    public void setDataSetBuilder(DataSetBuilder dataSetBuilder);
    
}
