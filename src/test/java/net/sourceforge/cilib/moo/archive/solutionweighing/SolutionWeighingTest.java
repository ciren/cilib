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
package net.sourceforge.cilib.moo.archive.solutionweighing;

import java.util.List;
import net.sourceforge.cilib.util.selection.WeightedObject;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.ProblemVisitor;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.Selection;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Wiehann Matthysen
 */
public class SolutionWeighingTest {

    private static final double EPSILON = 0.00000000001;

    private static class DummyOptimisationProblem implements OptimisationProblem {

        private static final long serialVersionUID = -2955989686805159033L;
        private int index;

        public DummyOptimisationProblem(int index) {
            this.index = index;
        }

        public DummyOptimisationProblem(DummyOptimisationProblem copy) {
        }

        @Override
        public DummyOptimisationProblem getClone() {
            return new DummyOptimisationProblem(this);
        }

        @Override
        public Fitness getFitness(Type solution) {
            Vector position = (Vector) solution;
            return new MinimisationFitness(position.doubleValueOf(index));
        }

        @Override
        public int getFitnessEvaluations() {
            return 0;
        }

        @Override
        public DomainRegistry getDomain() {
            return null;
        }

        @Override
        public DataSetBuilder getDataSetBuilder() {
            return null;
        }

        @Override
        public void setDataSetBuilder(DataSetBuilder dataSet) {
        }

        @Override
        public void accept(ProblemVisitor visitor) {
            throw new UnsupportedOperationException("This method is not implemented.");
        }

        @Override
        public void changeEnvironment() {
            throw new UnsupportedOperationException("This method is not implemented.");
        }
    }

    @Test
    public void testAntiClusteringWeighingStrategy() {
        MOOptimisationProblem moProblem = new MOOptimisationProblem();
        for (int i = 0; i < 2; i++) {
            moProblem.add(new DummyOptimisationProblem(i));
        }

        List<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();

        Vector position = Vector.of(1.0, 1.0);
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = Vector.of(2.0, 4.0);
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = Vector.of(3.0, 2.0);
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = Vector.of(4.0, 6.0);
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = Vector.of(5.0, 1.0);
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        List<WeightedObject> weighedSolutions = Selection.copyOf(solutions).weigh(new AntiClusterWeighing()).weightedElements();

        Iterator<WeightedObject> weighedSolutionIterator = weighedSolutions.iterator();
        WeightedObject weighedSolution = weighedSolutionIterator.next();
        Assert.assertEquals(0.290823093508, weighedSolution.getWeight(), EPSILON);

        weighedSolution = weighedSolutionIterator.next();
        Assert.assertEquals(0.367312140917, weighedSolution.getWeight(), EPSILON);

        weighedSolution = weighedSolutionIterator.next();
        Assert.assertEquals(0.418956131219, weighedSolution.getWeight(), EPSILON);

        weighedSolution = weighedSolutionIterator.next();
        Assert.assertEquals(0.266011104583, weighedSolution.getWeight(), EPSILON);

        weighedSolution = weighedSolutionIterator.next();
        Assert.assertEquals(0.283305258318, weighedSolution.getWeight(), EPSILON);

        assertThat(weighedSolutionIterator.hasNext(), is(false));
    }
}
