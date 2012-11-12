/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.moo.archive.constrained;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.problem.AbstractProblem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import org.junit.Test;

/**
 */
public class ConstrainedArchiveTest {

    private class SubOptimisationProblem1 extends AbstractProblem {

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
        public AbstractProblem getClone() {
            return this;
        }

        @Override
        public DomainRegistry getDomain() {
            return null;
        }
    }

    private class SubOptimisationProblem2 extends AbstractProblem {

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
        public AbstractProblem getClone() {
            return this;
        }

        @Override
        public DomainRegistry getDomain() {
            return null;
        }
    }

    private class SubOptimisationProblem3 extends AbstractProblem {

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
        public AbstractProblem getClone() {
            return this;
        }

        @Override
        public DomainRegistry getDomain() {
            return null;
        }
    }

    private class SubOptimisationProblem4 extends AbstractProblem {

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
        public AbstractProblem getClone() {
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
