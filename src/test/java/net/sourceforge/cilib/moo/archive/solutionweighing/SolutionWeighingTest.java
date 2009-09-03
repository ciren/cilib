/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.moo.archive.solutionweighing;

import java.util.List;
import net.sourceforge.cilib.util.selection.Selection.Entry;
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
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

import net.sourceforge.cilib.util.selection.Selection;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Wiehann Matthysen
 */
public class SolutionWeighingTest {

    private static List<OptimisationSolution> solutions;
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
            return new MinimisationFitness(position.getReal(index));
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

    @BeforeClass
    public static void setUp() {
        solutions = new ArrayList<OptimisationSolution>();
        MOOptimisationProblem moProblem = new MOOptimisationProblem();
        for (int i = 0; i < 2; i++) {
            moProblem.add(new DummyOptimisationProblem(i));
        }

        Vector position = new Vector();
        position.add(new Real(1));
        position.add(new Real(1));
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = new Vector();
        position.add(new Real(2));
        position.add(new Real(4));
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = new Vector();
        position.add(new Real(3));
        position.add(new Real(2));
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = new Vector();
        position.add(new Real(4));
        position.add(new Real(6));
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = new Vector();
        position.add(new Real(5));
        position.add(new Real(1));
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));
    }

    @Test
    public void testAntiClusteringWeighingStrategy() {
        List<Entry<OptimisationSolution>> weighedSolutions = Selection.from(solutions).weigh(new AntiClusterWeighing()).entries();

        Iterator<Entry<OptimisationSolution>> weighedSolutionIterator = weighedSolutions.iterator();
        Selection.Entry<OptimisationSolution> weighedSolution = weighedSolutionIterator.next();
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
