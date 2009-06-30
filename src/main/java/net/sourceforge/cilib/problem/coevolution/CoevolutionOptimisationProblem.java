/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.problem.coevolution;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.CoevolutionAlgorithm;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;

/**
 * This interface should be extended by any problem that should be optimised using a {@linkplain CoevolutionAlgorithm}
 * @author leo
 */
public interface CoevolutionOptimisationProblem {
	/**
	 * This method evaluates an entity with regards to a number of groups of entities depending on the instance of the problem
	 * @param the population of the solution to be evaluated
	 * @param entityData the blackboard of the {@linkplain Entity} being evaluated. This contains the solution vector, scoreboard and competitor list
	 * @return the entitie's fitness
	 */
	public Fitness evaluateEntity(int populationID, int evaluationRound, Blackboard<Enum<?>, Type> entityData);
	/**
	 * This method is used by the coevolution algorithm to assign an optimization problem instance to each sub population
	 * this optimization problem needs to define the domain for that specific sub population
	 * @param populationNo the population number for the sub population for which this problem is generated
	 * @return the optimization problem for the sub population
	 */
	public DomainRegistry getSubPopulationDomain(int populationNo);
	/**
	 * This method adds any problem specific entries to the all the populations entites's blackboards.
	 * @param pba the sub population to initialize
	 * @param populationID the populationID of the sub population
	 */
	public void initializeEntities(PopulationBasedAlgorithm pba, int populationID);
	/**
	 * this will correlate with the amount of groups of entites an individual required for evaluation
	 * @return the amount of sub population for this problem
	 */
	public int getAmountSubPopulations();
}
