/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity.normalisation;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.Numeric;

/**
 * Normalization based on the longest diagonal of the {@link Problem} search
 * space.
 */
public class DiagonalSpaceNormalisation implements DiversityNormalisation {

    /**
     * {@inheritDoc}
     */
    @Override
    public double getNormalisationParameter(SinglePopulationBasedAlgorithm algorithm) {
        Problem problem = algorithm.getOptimisationProblem();
        StructuredType<Numeric> domain = problem.getDomain().getBuiltRepresentation();

        double longestDiagonal = Double.MIN_VALUE;

        for (Numeric n : domain) {
            double range = n.getBounds().getRange();
            longestDiagonal = Math.max(longestDiagonal, range);
        }

        return longestDiagonal;
    }
}
