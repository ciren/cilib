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
package net.sourceforge.cilib.functions.continuous.moo.zdt;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>Zitzler-Thiele-Deb Test Function 2</p>
 *
 * Characteristics:
 * <ul>
 * <li>Nonconvex Pareto-optimal front.</li>
 * </ul>
 *
 * <p>
 * This function is representative of the nonconvex counterpart to T1.
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
public final class ZDT2 extends MOOptimisationProblem {

    private static final long serialVersionUID = -2949170760033824427L;
    private static final String DOMAIN = "R(0, 1)^30";

    private static class ZDT2_h implements ContinuousFunction {

        private static final long serialVersionUID = 6575398958907399233L;
        private final ZDT_f1 f1;
        private final ZDT_g g;

        public ZDT2_h() {
            this.f1 = new ZDT_f1();
            this.g = new ZDT_g();
        }

        @Override
        public Double apply(Vector input) {
            return 1.0 - (this.f1.apply(input) / this.g.apply(input)) * (this.f1.apply(input) / this.g.apply(input));
        }
    }

    private static class ZDT2_f2 implements ContinuousFunction {

        private static final long serialVersionUID = 1983853514735870004L;
        private final ZDT_g g;
        private final ZDT2_h h;

        public ZDT2_f2() {
            this.g = new ZDT_g();
            this.h = new ZDT2_h();
        }

        @Override
        public Double apply(Vector input) {
            return this.g.apply(input) * this.h.apply(input);
        }
    }

    public ZDT2() {
        FunctionMinimisationProblem zdt2_f1 = new FunctionMinimisationProblem();
        zdt2_f1.setFunction(new ZDT_f1());
        zdt2_f1.setDomain(DOMAIN);
        add(zdt2_f1);

        FunctionMinimisationProblem zdt2_f2 = new FunctionMinimisationProblem();
        zdt2_f2.setFunction(new ZDT2_f2());
        zdt2_f2.setDomain(DOMAIN);
        add(zdt2_f2);
    }

    public ZDT2(ZDT2 copy) {
        super(copy);
    }

    @Override
    public ZDT2 getClone() {
        return new ZDT2(this);
    }
}
