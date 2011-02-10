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
package net.sourceforge.cilib.moo.archive.constrained;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import org.junit.Test;

/**
 * @author Wiehann Matthysen
 */
public class ConstrainedArchiveTest {

    private class SubOptimisationProblem1 extends OptimisationProblemAdapter {

        private static final long serialVersionUID = -6450436273476937541L;

        @Override
        protected Fitness calculateFitness(Type solution) {
            Vector x = (Vector) solution;
            double y = 0.0;
            for (int i = 0; i < x.size(); ++i) {
                y += 1.0 / (x.doubleValueOf(i) * x.doubleValueOf(i));
            }
            return new MinimisationFitness(y);
        }

        @Override
        public OptimisationProblemAdapter getClone() {
            return this;
        }

        @Override
        public DomainRegistry getDomain() {
            return null;
        }
    }

    private class SubOptimisationProblem2 extends OptimisationProblemAdapter {

        private static final long serialVersionUID = -1284888804119511449L;

        @Override
        protected Fitness calculateFitness(Type solution) {
            Vector x = (Vector) solution;
            double y = 0.0;
            for (int i = 0; i < x.size(); ++i) {
                y += x.doubleValueOf(i) * x.doubleValueOf(i);
            }
            return new MinimisationFitness(y);
        }

        @Override
        public OptimisationProblemAdapter getClone() {
            return this;
        }

        @Override
        public DomainRegistry getDomain() {
            return null;
        }
    }

    private class SubOptimisationProblem3 extends OptimisationProblemAdapter {

        private static final long serialVersionUID = 2255106605755142990L;

        @Override
        protected Fitness calculateFitness(Type solution) {
            Vector x = (Vector) solution;
            double y = 0.0;
            for (int i = 0; i < x.size(); ++i) {
                y += 2.0 / (x.doubleValueOf(i) * x.doubleValueOf(i));
            }
            return new MinimisationFitness(y);
        }

        @Override
        public OptimisationProblemAdapter getClone() {
            return this;
        }

        @Override
        public DomainRegistry getDomain() {
            return null;
        }
    }

    private class SubOptimisationProblem4 extends OptimisationProblemAdapter {

        private static final long serialVersionUID = -1284888804119511449L;

        @Override
        protected Fitness calculateFitness(Type solution) {
            Vector x = (Vector) solution;
            double y = 0.0;
            for (int i = 0; i < x.size(); ++i) {
                y += 2.0 * x.doubleValueOf(i) * x.doubleValueOf(i);
            }
            return new MinimisationFitness(y);
        }

        @Override
        public OptimisationProblemAdapter getClone() {
            return this;
        }

        @Override
        public DomainRegistry getDomain() {
            return null;
        }
    }

    private class DummyOptimisationProblem1 extends MOOptimisationProblem {

        private static final long serialVersionUID = -8866300863883970611L;

        public DummyOptimisationProblem1() {
            super();
            add(new SubOptimisationProblem1());
            add(new SubOptimisationProblem2());
            add(new SubOptimisationProblem3());
            add(new SubOptimisationProblem4());
        }
    }

    @Test
    public void testSetBasedConstrainedArchive() {
        SetBasedConstrainedArchive archive = new SetBasedConstrainedArchive();
        archive.setPruningSelection(new RandomSelector<OptimisationSolution>());
        archive.setCapacity(100);
        DummyOptimisationProblem1 problem = new DummyOptimisationProblem1();
        for (int i = 1; i <= 500; ++i) {
            Vector.Builder builder = Vector.newBuilder();
            for (int j = i; j < i + 5; ++j) {
                builder.add(Real.valueOf(j));
            }
            Vector vector = builder.build();
            archive.addAll(Arrays.asList(new OptimisationSolution(vector, problem.getFitness(vector))));
        }
        assertThat(archive.size(), is(100));
        archive.clear();

        List<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();
        for (int i = 1; i <= 500; ++i) {
            Vector.Builder builder = Vector.newBuilder();
            for (int j = i; j < i + 5; ++j) {
                builder.add(Real.valueOf(j));
            }
            Vector vector = builder.build();
            solutions.add(new OptimisationSolution(vector, problem.getFitness(vector)));
        }
        archive.addAll(solutions);
        assertThat(archive.size(), is(100));
    }
}
