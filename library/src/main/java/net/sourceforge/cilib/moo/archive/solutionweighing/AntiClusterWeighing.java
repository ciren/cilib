/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.moo.archive.solutionweighing;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.util.selection.WeightedObject;

/**
 * An implementation of {@link SolutionWeighing} that weighs a collection of
 * {@link OptimisationSolution}s based on how closely clustered these solutions
 * are to one another.
 * <p>
 * This class can be used to select the most closely clustered optimisation
 * solutions to be removed from the
 * {@link net.sourceforge.cilib.moo.archive.Archive} if it reaches its maximum
 * capacity. However, by selecting the least closely clustered solutions, you
 * have a collection of solutions that are good candidates to be used as guides
 * in a Multi-objective optimisation algorithm.
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
    public <T> Iterable<WeightedObject> weigh(Iterable<T> iterable) {
        // Get first fitness as dummy fitness to set size of initial min and max fitness
        // arrays as well as populating these arrays.
        List<OptimisationSolution> solutions = Lists.newArrayList((Iterable<OptimisationSolution>) iterable);
        Iterator<OptimisationSolution> solutionIterator = solutions.iterator();
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
        List<WeightedObject> weighedOptimisationSolutions = Lists.newArrayList();
        for (OptimisationSolution fromSolution : solutions) {
            double totalDistance = 0.0;
            MOFitness fromFitnesses = (MOFitness) fromSolution.getFitness();
            for (OptimisationSolution toSolution : solutions) {
                if (fromSolution != toSolution) {
                    double distance = 0.0;
                    MOFitness toFitnesses = (MOFitness) toSolution.getFitness();
                    for (int i = 0; i < fromFitnesses.getDimension(); ++i) {
                        distance += Math.pow((fromFitnesses.getFitness(i).getValue() - toFitnesses.getFitness(i).getValue())
                                / (maxFitnesses[i].getValue() - minFitnesses[i].getValue()), 2.0);
                    }
                    totalDistance += Math.sqrt(distance);
                }
            }
            weighedOptimisationSolutions.add(new WeightedObject(fromSolution, (totalDistance != 0.0) ? 1.0 / totalDistance : Double.MAX_VALUE));
        }

        return weighedOptimisationSolutions;
    }
}
