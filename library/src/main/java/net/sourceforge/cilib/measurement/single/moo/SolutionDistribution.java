/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.moo;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.solution.Fitnesses;
import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * Measures the distribution (in objective space) of the non-dominated solutions
 * in an archive.
 * </p>
 *
 */
public class SolutionDistribution implements Measurement {

    private static final long serialVersionUID = -3434812635896475717L;

    public SolutionDistribution() {
    }

    public SolutionDistribution(SolutionDistribution copy) {
    }

    @Override
    public SolutionDistribution getClone() {
        return new SolutionDistribution(this);
    }

    @Override
    public Real getValue(Algorithm algorithm) {

        List<Double> distances = Lists.newArrayList();

        // Fist, calculate nearest distance between solution i and another solution in Pareto front.
        // Add every nearest distance in distances list.
        Archive archive = Archive.Provider.get();
        for (OptimisationSolution solution : archive) {
            Vector solutionFitness = Fitnesses.vectorOf((MOFitness) solution.getFitness());
            double nearestDistance = Double.MAX_VALUE;
            for (OptimisationSolution otherSolution : archive) {
                if (solution != otherSolution) {
                    Vector otherFitness = Fitnesses.vectorOf((MOFitness) otherSolution.getFitness());
                    double distance = otherFitness.subtract(solutionFitness).norm();
                    nearestDistance = Math.min(nearestDistance, distance);
                }
            }
            distances.add(nearestDistance);
        }

        // Then, calculate average nearest distance.
        double averageNearest = 0.0;
        for (Double distance : distances) {
            averageNearest += distance;
        }
        averageNearest /= archive.size();

        // Now calculate the standard deviation in nearest distances.
        double sumSqr = 0.0;
        for (Double distance : distances) {
            sumSqr += (distance - averageNearest) * (distance - averageNearest);
        }

        return Real.valueOf((1.0 / archive.size()) * Math.sqrt(sumSqr / archive.size()));
    }
}
