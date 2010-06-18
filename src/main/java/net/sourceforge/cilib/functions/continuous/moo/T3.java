/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.functions.continuous.moo;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>Zitzler-Thiele-Deb Test Function 3</p>
 *
 * Characteristics:
 * <ul>
 * <li>Convex Pareto-optimal front.</li>
 * <li>Discontiguous Pareto-optimal front.</li>
 * </ul>
 *
 * <p>
 * This function represents the discreteness feature; its Pareto-optimal front
 * consists of several noncontiguous convex parts. The introduction of the sine
 * function in h causes discontinuity in the Pareto-optimal front. However,
 * there is no discontinuity in the parameter space.
 * </p>
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
 * @author Wiehann Matthysen
 */
public final class T3 extends MOOptimisationProblem {

    private static final long serialVersionUID = 5783167168187614882L;
    private static final String DOMAIN = "R(0, 1)^30";

    private static class T3_h implements ContinuousFunction {

        private static final long serialVersionUID = -3438306908263146396L;
        private final T_f1 f1;
        private final T_g g;

        public T3_h() {
            this.f1 = new T_f1();
            this.g = new T_g();
        }

        @Override
        public Double apply(Vector input) {
            double f1_val = this.f1.apply(input);
            double g_val = this.g.apply(input);
            return 1.0 - Math.sqrt(f1_val / g_val) - (f1_val / g_val) * Math.sin(10.0 * Math.PI * f1_val);
        }
    }

    private static class T3_f2 implements ContinuousFunction {

        private static final long serialVersionUID = 1052615620850285975L;
        private final T_g g;
        private final T3_h h;

        public T3_f2() {
            this.g = new T_g();
            this.h = new T3_h();
        }

        @Override
        public Double apply(Vector input) {
            return this.g.apply(input) * this.h.apply(input);
        }
    }

    public T3() {
        FunctionMinimisationProblem t3_f1 = new FunctionMinimisationProblem();
        t3_f1.setFunction(new T_f1());
        t3_f1.setDomain(DOMAIN);
        add(t3_f1);

        FunctionMinimisationProblem t3_f2 = new FunctionMinimisationProblem();
        t3_f2.setFunction(new T3_f2());
        t3_f2.setDomain(DOMAIN);
        add(t3_f2);
    }

    public T3(T3 copy) {
        super(copy);
    }

    @Override
    public T3 getClone() {
        return new T3(this);
    }
}
