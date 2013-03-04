/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.problem;

import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.tuning.parameters.TuningBounds;

public class MultiDimensionProblemGenerator extends ProblemGenerator {

    private ProblemGenerator problemsProvider;
    private TuningBounds dimensionBounds;

    public MultiDimensionProblemGenerator() {
        this.dimensionBounds = new TuningBounds(1, 100);
    }

    @Override
    public Problem _1() {
        final Problem p = problemsProvider._1();
        final UniformDistribution uniform = new UniformDistribution();
        String d = p.getDomain().getDomainString();
        p.setDomain(d.replaceAll("[\\^][\\d]*",
            "^" + Integer.toString((int) uniform.getRandomNumber(dimensionBounds.getLowerBound(),
            dimensionBounds.getUpperBound()))));
        return p;
    }

    public void setProblemsProvider(ProblemGenerator problemsProvider) {
        this.problemsProvider = problemsProvider;
    }

    public ProblemGenerator getProblemsProvider() {
        return problemsProvider;
    }

    public void setDimensionBounds(TuningBounds dimensionBounds) {
        this.dimensionBounds = dimensionBounds;
    }

    public TuningBounds getDimensionBounds() {
        return dimensionBounds;
    }

}
