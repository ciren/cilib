/**
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
package net.sourceforge.cilib.moo.archive.solutionweighingstrategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.entity.operators.selection.SelectionStrategy;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MOFitness;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * <p>
 * An implementation of {@link SolutionWeighingStrategy} that weighs a collection of
 * {@link OptimisationSolution}s based on how closely clustered these solutions are to
 * one another. Solutions that are clustered closely together will get a large weight
 * value associated with them meaning that these solutions will have a higher probability
 * of being selected by a {@link SelectionStrategy}.
 * </p>
 *
 * <p>
 * This class is usually used to select a collection of closely clustered optimisation
 * solutions to be removed from the {@link Archive} if it reaches its maximum capacity.
 * However, in combination with {@link InverseWeighingStrategyDecorator} the solutions
 * that are the least clustered will be selected with a higher probability. These solutions
 * are usually good guides for a Multi-objective optimisation algorithm (see
 * {@link GuideSelectionStrategy}).
 * </p>
 * 
 * @author Wiehann Matthysen
 */
public class AntiClusterWeighingStrategy implements SolutionWeighingStrategy {

    private static final long serialVersionUID = -4395783169143386500L;

    public AntiClusterWeighingStrategy() {
    }

    public AntiClusterWeighingStrategy(AntiClusterWeighingStrategy copy) {
    }

    @Override
    public AntiClusterWeighingStrategy getClone() {
        return new AntiClusterWeighingStrategy(this);
    }

    @Override
    public List<Pair<Double, OptimisationSolution>> weigh(Collection<? extends OptimisationSolution> solutions) {
        // Get first fitness as dummy fitness to set size of initial min and max fitness
        // arrays as well as populating these arrays.
        Iterator<? extends OptimisationSolution> solutionIterator = solutions.iterator();
        MOFitness tempFitness = (MOFitness) solutionIterator.next().getFitness();
        Fitness[] minFitnesses = new Fitness[tempFitness.getDimension()];
        Fitness[] maxFitnesses = new Fitness[tempFitness.getDimension()];
        for (int i = 0; i < tempFitness.getDimension(); ++i) {
            minFitnesses[i] = tempFitness.getFitness(i);
            maxFitnesses[i] = tempFitness.getFitness(i);
        }

        // Iterate over all remaining optimisation solutions and find the min and max fitness values.
        while (solutionIterator.hasNext()) {
            OptimisationSolution optimisationSolution = solutionIterator.next();
            MOFitness fitnesses = (MOFitness) optimisationSolution.getFitness();
            for (int i = 0; i < fitnesses.getDimension(); ++i) {
                Double fitnessValue = fitnesses.getFitness(i).getValue();
                if (fitnessValue < minFitnesses[i].getValue()) {
                    minFitnesses[i] = fitnesses.getFitness(i);
                } else if (fitnessValue > maxFitnesses[i].getValue()) {
                    maxFitnesses[i] = fitnesses.getFitness(i);
                }
            }
        }

        // Now, iterate over all solutions again, but calculate the distance from each solution to every other
        // solution and store the results in a list. Each solution in the list contains the distance as weight value.
        List<Pair<Double, OptimisationSolution>> weighedOptimisationSolutions = new ArrayList<Pair<Double, OptimisationSolution>>();
        for (OptimisationSolution fromSolution : solutions) {
            double totalDistance = 0.0;
            MOFitness fromFitnesses = (MOFitness) fromSolution.getFitness();
            for (OptimisationSolution toSolution : solutions) {
                if (fromSolution != toSolution) {
                    double distance = 0.0;
                    MOFitness toFitnesses = (MOFitness) toSolution.getFitness();
                    for (int i = 0; i < fromFitnesses.getDimension(); ++i) {
                        distance += Math.pow((fromFitnesses.getFitness(i).getValue() - toFitnesses.getFitness(i).getValue()) /
                                (maxFitnesses[i].getValue() - minFitnesses[i].getValue()), 2.0);
                    }
                    totalDistance += Math.sqrt(distance);
                }
            }
            weighedOptimisationSolutions.add(new Pair<Double, OptimisationSolution>((totalDistance != 0.0) ? 1.0 / totalDistance : Double.MAX_VALUE, fromSolution));
        }

        return weighedOptimisationSolutions;
    }
}
