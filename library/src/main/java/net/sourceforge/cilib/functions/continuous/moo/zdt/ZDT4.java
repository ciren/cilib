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
 * <p>Zitzler-Thiele-Deb Test Function 4</p>
 *
 * Characteristics:
 * <ul>
 * <li>Convex Pareto-optimal front.</li>
 * <li>Multimodal - Several local pareto-optimal fronts.</li>
 * </ul>
 *
 * <p>
 * This function contains 21^9 local Pareto-optimal fronts and, therefore,
 * tests for an EA's ability to deal with multimodality.
 * </p>
 *
 * <p>
 * The global Pareto-optimal front is formed with g(x) = 1, the best local
 * Pareto-optimal front with g(x) = 1.25. Note that not all local Pareto-optimal
 * sets are distinguishable in the objective space.
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
public final class ZDT4 extends MOOptimisationProblem {

    private static final long serialVersionUID = 6807423144357771198L;
    private static final String DOMAIN = "R(0:1)^1, R(-5:5)^9";

    private static class ZDT4_g implements ContinuousFunction {

        private static final long serialVersionUID = -4693394582794280778L;

        @Override
        public Double apply(Vector input) {
            double sum = 0.0;
            for (int i = 1; i < input.size(); ++i) {
                sum += input.doubleValueOf(i) * input.doubleValueOf(i) - 10.0 * Math.cos(4.0 * Math.PI * input.doubleValueOf(i));
            }
            return 1 + 10.0 * (input.size() - 1.0) + sum;
        }
    }

    private static class ZDT4_h implements ContinuousFunction {

        private static final long serialVersionUID = 3672916606445089134L;
        private final ZDT_f1 f1;
        private final ZDT4_g g;

        public ZDT4_h() {
            this.f1 = new ZDT_f1();
            this.g = new ZDT4_g();
        }

        @Override
        public Double apply(Vector input) {
            return 1.0 - Math.sqrt(this.f1.apply(input) / this.g.apply(input));
        }
    }

    private static class ZDT4_f2 implements ContinuousFunction {

        private static final long serialVersionUID = -4303326355255421549L;
        private final ZDT4_g g;
        private final ZDT4_h h;

        public ZDT4_f2() {
            this.g = new ZDT4_g();
            this.h = new ZDT4_h();
        }

        @Override
        public Double apply(Vector input) {
            return this.g.apply(input) * this.h.apply(input);
        }
    }

    public ZDT4() {
        FunctionOptimisationProblem zdt4_f1 = new FunctionOptimisationProblem();
        zdt4_f1.setFunction(new ZDT_f1());
        zdt4_f1.setDomain(DOMAIN);
        add(zdt4_f1);

        FunctionOptimisationProblem zdt4_f2 = new FunctionOptimisationProblem();
        zdt4_f2.setFunction(new ZDT4_f2());
        zdt4_f2.setDomain(DOMAIN);
        add(zdt4_f2);
    }

    public ZDT4(ZDT4 copy) {
        super(copy);
    }

    @Override
    public ZDT4 getClone() {
        return new ZDT4(this);
    }
}
