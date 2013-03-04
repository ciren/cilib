/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.multiple.moo;

import java.util.Collection;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * <p>
 * Measures the set of non-dominated objective vectors within an archive.
 * Requires the set of non-dominated decision vectors to be evaluated.
 * </p>
 *
 */
public class ParetoOptimalFront implements Measurement<TypeList> {

    private static final long serialVersionUID = 6695894359780745776L;

    @Override
    public ParetoOptimalFront getClone() {
        return this;
    }

    @Override
    public TypeList getValue(Algorithm algorithm) {
        TypeList allFitnessValues = new TypeList();
        Collection<OptimisationSolution> solutions = Archive.Provider.get();
        for (OptimisationSolution solution : solutions) {
            MOFitness fitnesses = (MOFitness) solution.getFitness();
            TypeList fitnessValues = new TypeList();
            for (int i = 0; i < fitnesses.getDimension(); ++i) {
                Fitness fitness = fitnesses.getFitness(i);
                fitnessValues.add(fitness);
            }
            allFitnessValues.add(fitnessValues);
        }
        return allFitnessValues;
    }
}
