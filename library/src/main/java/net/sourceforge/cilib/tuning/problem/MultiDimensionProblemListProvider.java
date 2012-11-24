/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.problem;

import fj.F;
import fj.data.List;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.tuning.parameters.TuningBounds;

public class MultiDimensionProblemListProvider extends ProblemListProvider {

    private ProblemListProvider problemsProvider;
    private TuningBounds dimensionBounds;

    public MultiDimensionProblemListProvider() {
        this.dimensionBounds = new TuningBounds(1, 100);
    }

    @Override
    public List<Problem> _1() {
        final List<Problem> p = problemsProvider._1();
        final UniformDistribution uniform = new UniformDistribution();

        return p.map(new F<Problem, Problem>() {
            @Override
            public Problem f(Problem a) {
                String d = a.getDomain().getDomainString();
                a.setDomain(d.replaceAll("[\\^][\\d]*",
                    "^" + Integer.toString((int) uniform.getRandomNumber(dimensionBounds.getLowerBound(),
                    dimensionBounds.getUpperBound()))));
                return a;
            }
        });
    }

    public void setProblemsProvider(ProblemListProvider problemsProvider) {
        this.problemsProvider = problemsProvider;
    }

    public ProblemListProvider getProblemsProvider() {
        return problemsProvider;
    }

    public void setDimensionBounds(TuningBounds dimensionBounds) {
        this.dimensionBounds = dimensionBounds;
    }

    public TuningBounds getDimensionBounds() {
        return dimensionBounds;
    }

}
