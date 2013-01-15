/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.moo;

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
 * Measures the distance (in objective space) between the two non-dominated
 * solutions located furthest from one another in an archive.
 * </p>
 *
 */
public class ParetoFrontExtent implements Measurement {

    private static final long serialVersionUID = 1264405714078826569L;

    public ParetoFrontExtent() {
    }

    public ParetoFrontExtent(ParetoFrontExtent copy) {
    }

    @Override
    public ParetoFrontExtent getClone() {
        return new ParetoFrontExtent(this);
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        Archive archive = Archive.Provider.get();

        double maximumDistance = 0.0;

        for (OptimisationSolution solution : archive) {
            Vector solutionFitness = Fitnesses.vectorOf((MOFitness) solution.getFitness());
            for (OptimisationSolution otherSolution : archive) {
                Vector otherFitness = Fitnesses.vectorOf((MOFitness) otherSolution.getFitness());
                double distance = otherFitness.subtract(solutionFitness).norm();
                maximumDistance = Math.max(maximumDistance, distance);
            }
        }

        return Real.valueOf(maximumDistance);
    }
}
