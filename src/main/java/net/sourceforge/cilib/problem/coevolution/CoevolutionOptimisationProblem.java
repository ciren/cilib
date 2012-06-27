/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.problem.coevolution;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;

/**
 * This interface should be extended by any problem that should be optimised 
 * using a {@linkplain CoevolutionAlgorithm}.
 */
public interface CoevolutionOptimisationProblem {

    /**
     * This method evaluates an entity with regards to a number of groups of 
     * entities depending on the instance of the problem.
     * 
     * @param the population of the solution to be evaluated
     * @param entityData the blackboard of the {@linkplain Entity} being evaluated. 
     * This contains the solution vector, scoreboard and competitor list
     * @return the entity's fitness
     */
    Fitness evaluateEntity(int populationID, int evaluationRound, Blackboard<Enum<?>, Type> entityData);

    /**
     * This method is used by the coevolution algorithm to assign an optimization 
     * problem instance to each sub population this optimization problem needs to 
     * define the domain for that specific sub population.
     * 
     * @param populationNo the population number for the sub population for which this problem is generated
     * @return the optimization problem for the sub population
     */
    DomainRegistry getSubPopulationDomain(int populationNo);

    /**
     * This method adds any problem specific entries to the all the populations 
     * entity's blackboards.
     * 
     * @param pba the sub population to initialize
     * @param populationID the populationID of the sub population
     */
    void initializeEntities(PopulationBasedAlgorithm pba, int populationID);

    /**
     * This will correlate with the amount of groups of entity's an individual 
     * required for evaluation.
     * 
     * @return the amount of sub population for this problem
     */
    int getAmountSubPopulations();
}
