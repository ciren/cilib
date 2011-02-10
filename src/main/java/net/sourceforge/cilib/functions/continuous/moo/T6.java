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
 * @author Wiehann Matthysen
 */
public final class T6 extends MOOptimisationProblem {

    private static final long serialVersionUID = -8294718517983975376L;
    private static final String DOMAIN = "R(0, 1)^10";

    private static class T6_f1 implements ContinuousFunction {

        private static final long serialVersionUID = -7611726748395645976L;

        @Override
        public Double apply(Vector input) {
            return 1.0 - Math.exp(-4.0 * input.doubleValueOf(0)) * Math.pow(Math.sin(6.0 * Math.PI * input.doubleValueOf(0)), 6.0);
        }
    }

    private static class T6_g implements ContinuousFunction {

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

    private static class T6_h implements ContinuousFunction {

        private static final long serialVersionUID = -6636124986465822446L;
        private final T6_f1 f1;
        private final T6_g g;

        public T6_h() {
            this.f1 = new T6_f1();
            this.g = new T6_g();
        }

        @Override
        public Double apply(Vector input) {
            return 1.0 - Math.pow(this.f1.apply(input) / this.g.apply(input), 2.0);
        }
    }

    private static class T6_f2 implements ContinuousFunction {

        private static final long serialVersionUID = 6790101111642461359L;
        private final T6_g g;
        private final T6_h h;

        public T6_f2() {
            this.g = new T6_g();
            this.h = new T6_h();
        }

        @Override
        public Double apply(Vector input) {
            return this.g.apply(input) * this.h.apply(input);
        }
    }

    public T6() {
        FunctionMinimisationProblem t6_f1 = new FunctionMinimisationProblem();
        t6_f1.setFunction(new T6_f1());
        t6_f1.setDomain(DOMAIN);
        add(t6_f1);

        FunctionMinimisationProblem t6_f2 = new FunctionMinimisationProblem();
        t6_f2.setFunction(new T6_f2());
        t6_f2.setDomain(DOMAIN);
        add(t6_f2);
    }

    public T6(T6 copy) {
        super(copy);
    }

    @Override
    public T6 getClone() {
        return new T6(this);
    }
}
