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
package net.sourceforge.cilib.moo.archive.solutionweighing;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MOFitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.moo.guideselectionstrategies.GuideSelectionStrategy;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.Selection.Entry;

/**
 * <p>
 * An implementation of {@link SolutionWeighing} that weighs a collection of
 * {@link OptimisationSolution}s based on how closely clustered these solutions are to
 * one another.
 * </p>
 *
 * <p>
 * This class can be used to select the most closely clustered optimisation
 * solutions to be removed from the {@link Archive} if it reaches its maximum capacity.
 * However, by selecting the least closely clustered solutions, you have a collection of
 * solutions that are good candidates to be used as guides in a Multi-objective optimisation
 * algorithm (see {@link GuideSelectionStrategy}).
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class AntiClusterWeighing implements SolutionWeighing {
    private static final long serialVersionUID = -4395783169143386500L;

    public AntiClusterWeighing() {
    }

    public AntiClusterWeighing(AntiClusterWeighing copy) {
    }

    @Override
    public AntiClusterWeighing getClone() {
        return new AntiClusterWeighing(this);
    }

    @Override
    public boolean weigh(List<Selection.Entry<OptimisationSolution>> solutions) {
        // Get first fitness as dummy fitness to set size of initial min and max fitness
        // arrays as well as populating these arrays.
        Iterator<? extends Entry<OptimisationSolution>> solutionIterator = solutions.iterator();
        MOFitness tempFitness = (MOFitness) solutionIterator.next().getElement().getFitness();
        Fitness[] minFitnesses = new Fitness[tempFitness.getDimension()];
        Fitness[] maxFitnesses = new Fitness[tempFitness.getDimension()];
        for (int i = 0; i < tempFitness.getDimension(); ++i) {
            minFitnesses[i] = tempFitness.getFitness(i);
            maxFitnesses[i] = tempFitness.getFitness(i);
        }

        // Iterate over all remaining optimisation solutions and find the min and max fitness values.
        while (solutionIterator.hasNext()) {
            Entry<OptimisationSolution> optimisationSolution = solutionIterator.next();
            MOFitness fitnesses = (MOFitness) optimisationSolution.getElement().getFitness();
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
//        List<Pair<Double, OptimisationSolution>> weighedOptimisationSolutions = new ArrayList<Pair<Double, OptimisationSolution>>();
        for (Selection.Entry<OptimisationSolution> fromSolution : solutions) {
            double totalDistance = 0.0;
            MOFitness fromFitnesses = (MOFitness) fromSolution.getElement().getFitness();
            for (Selection.Entry<OptimisationSolution> toSolution : solutions) {
                if (fromSolution != toSolution) {
                    double distance = 0.0;
                    MOFitness toFitnesses = (MOFitness) toSolution.getElement().getFitness();
                    for (int i = 0; i < fromFitnesses.getDimension(); ++i) {
                        distance += Math.pow((fromFitnesses.getFitness(i).getValue() - toFitnesses.getFitness(i).getValue()) /
                                (maxFitnesses[i].getValue() - minFitnesses[i].getValue()), 2.0);
                    }
                    totalDistance += Math.sqrt(distance);
                }
            }
            fromSolution.setWeight((totalDistance != 0.0) ? 1.0 / totalDistance : Double.MAX_VALUE);
//            weighedOptimisationSolutions.add(new Pair<Double, OptimisationSolution>((totalDistance != 0.0) ? 1.0 / totalDistance : Double.MAX_VALUE, fromSolution));
        }

//        return weighedOptimisationSolutions;
        return true;
    }
}
