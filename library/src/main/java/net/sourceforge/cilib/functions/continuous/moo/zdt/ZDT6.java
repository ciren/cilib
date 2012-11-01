/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.moo.zdt;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>Zitzler-Thiele-Deb Test Function 6</p>
 *
 * Characteristics:
 * <ul>
 * <li>Nonconvex Pareto-optimal front.</li>
 * <li>Nonuniform Pareto-optimal front.</li>
 * </ul>
 *
 * <p>
 * This test function includes two difficulties caused by the nonuniformity of
 * the search space: first, the Pareto-optimal solutions are nonuniformly
 * distributed along the global Pareto front (the front is biased for solutions
 * for which f1(x) is near one); second, the density of the solutions is lowest
 * near the Pareto-optimal front and highest away from the front.
 * </p>
 *
 * <p>
 * The Pareto-optimal front is formed with g(x) = 1.
 * </p>
 *
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li>
 * E. Zitzler, K. Deb and L. Thiele, "Comparison of multiobjective
 * evolutionary algorithms: Empirical results", in Evolutionary Computation,
 * vol 8, no 2, pp. 173-195, 2000.
 * </li>
 * </ul>
 * </p>
 *
 */
public final class ZDT6 extends MOOptimisationProblem {

    private static final long serialVersionUID = -8294718517983975376L;
    private static final String DOMAIN = "R(0:1)^10";

    private static class ZDT6_f1 implements ContinuousFunction {

        private static final long serialVersionUID = -7611726748395645976L;

        @Override
        public Double apply(Vector input) {
            return 1.0 - Math.exp(-4.0 * input.doubleValueOf(0)) * Math.pow(Math.sin(6.0 * Math.PI * input.doubleValueOf(0)), 6.0);
        }
    }

    private static class ZDT6_g implements ContinuousFunction {

        private static final long serialVersionUID = -7961935910114582096L;

        @Override
        public Double apply(Vector input) {
            double sum = 0.0;
            for (int i = 1; i < input.size(); ++i) {
                sum += input.doubleValueOf(i) / (input.size() - 1.0);
            }
            return 1.0 + 9.0 * Math.pow(sum, 0.25);
        }
    }

    private static class ZDT6_h implements ContinuousFunction {

        private static final long serialVersionUID = -6636124986465822446L;
        private final ZDT6_f1 f1;
        private final ZDT6_g g;

        public ZDT6_h() {
            this.f1 = new ZDT6_f1();
            this.g = new ZDT6_g();
        }

        @Override
        public Double apply(Vector input) {
            return 1.0 - Math.pow(this.f1.apply(input) / this.g.apply(input), 2.0);
        }
    }

    private static class ZDT6_f2 implements ContinuousFunction {

        private static final long serialVersionUID = 6790101111642461359L;
        private final ZDT6_g g;
        private final ZDT6_h h;

        public ZDT6_f2() {
            this.g = new ZDT6_g();
            this.h = new ZDT6_h();
        }

        @Override
        public Double apply(Vector input) {
            return this.g.apply(input) * this.h.apply(input);
        }
    }

    public ZDT6() {
        FunctionOptimisationProblem zdt6_f1 = new FunctionOptimisationProblem();
        zdt6_f1.setFunction(new ZDT6_f1());
        zdt6_f1.setDomain(DOMAIN);
        add(zdt6_f1);

        FunctionOptimisationProblem zdt6_f2 = new FunctionOptimisationProblem();
        zdt6_f2.setFunction(new ZDT6_f2());
        zdt6_f2.setDomain(DOMAIN);
        add(zdt6_f2);
    }

    public ZDT6(ZDT6 copy) {
        super(copy);
    }

    @Override
    public ZDT6 getClone() {
        return new ZDT6(this);
    }
}
