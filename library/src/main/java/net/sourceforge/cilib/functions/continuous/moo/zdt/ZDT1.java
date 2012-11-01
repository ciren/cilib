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
 * <p>Zitzler-Thiele-Deb Test Function 1</p>
 *
 * Characteristics:
 * <ul>
 * <li>Convex Pareto-optimal front.</li>
 * </ul>
 *
 * <p>
 * The Pareto-optimal front is formed with g(x) = 1
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
public final class ZDT1 extends MOOptimisationProblem {

    private static final long serialVersionUID = 3345180577731621477L;
    private static final String DOMAIN = "R(0:1)^30";

    private static class ZDT1_h implements ContinuousFunction {

        private static final long serialVersionUID = 3672916606445089134L;
        private final ZDT_f1 f1;
        private final ZDT_g g;

        public ZDT1_h() {
            this.f1 = new ZDT_f1();
            this.g = new ZDT_g();
        }

        @Override
        public Double apply(Vector input) {
            return 1.0 - Math.sqrt(this.f1.apply(input) / this.g.apply(input));
        }
    }

    private static class ZDT1_f2 implements ContinuousFunction {

        private static final long serialVersionUID = 5864890886162485183L;
        private final ZDT_g g;
        private final ZDT1_h h;

        private ZDT1_f2() {
            this.g = new ZDT_g();
            this.h = new ZDT1_h();
        }

        @Override
        public Double apply(Vector input) {
            return this.g.apply(input) * this.h.apply(input);
        }
    }

    public ZDT1() {
        FunctionOptimisationProblem zdt1_f1 = new FunctionOptimisationProblem();
        zdt1_f1.setFunction(new ZDT_f1());
        zdt1_f1.setDomain(DOMAIN);
        add(zdt1_f1);

        FunctionOptimisationProblem zdt1_f2 = new FunctionOptimisationProblem();
        zdt1_f2.setFunction(new ZDT1_f2());
        zdt1_f2.setDomain(DOMAIN);
        add(zdt1_f2);
    }

    public ZDT1(ZDT1 copy) {
        super(copy);
    }

    @Override
    public ZDT1 getClone() {
        return new ZDT1(this);
    }
}
