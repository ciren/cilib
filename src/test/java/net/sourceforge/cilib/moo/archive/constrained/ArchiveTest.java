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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MOFitness;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Wiehann Matthysen
 */
public class ArchiveTest {

    private static Archive archive;
    private static final double EPSILON = 0.00000000001;

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
            return null;
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
            return null;
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
            return null;
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
            return null;
        }

        @Override
        public DomainRegistry getDomain() {
            return null;
        }
    }

    private class DummyOptimisationProblem1 extends MOOptimisationProblem {

        private static final long serialVersionUID = -4356017239778725107L;

        public DummyOptimisationProblem1() {
            super();
            add(new SubOptimisationProblem1());
            add(new SubOptimisationProblem2());
        }
    }

    private class DummyOptimisationProblem2 extends MOOptimisationProblem {

        private static final long serialVersionUID = -4364285430142583816L;

        public DummyOptimisationProblem2() {
            super();
            add(new SubOptimisationProblem3());
            add(new SubOptimisationProblem4());
        }
    }

    @BeforeClass
    public static void setUp() {
        archive = new SetBasedConstrainedArchive();
    }

    @Before
    public void clearArchive() {
        archive.clear();
    }

    private static Collection<Vector> createDummyVectors() {
        // Create 3 test vectors with values (1,2,3,4,5); (2,3,4,5,6) and (3,4,5,6,7).
        List<Vector> testVectors = new ArrayList<Vector>();
        for (int i = 1; i <= 3; ++i) {
            Vector vector = new Vector();
            for (int j = i; j < i + 5; ++j) {
                vector.add(Real.valueOf(j));
            }
            testVectors.add(vector);
        }
        return testVectors;
    }

    /**
     * This method creates three solutions, inserts them into the archive and
     * check if they exist in the archive (check if hashing works correctly due to
     * use of hash set). It then inserts these solutions into the archive again
     * and checks if the archive stays the same size (should not grow).
     */
    @Test
    public void testDuplicateSolutions() {
        Collection<Vector> testVectors = createDummyVectors();
        DummyOptimisationProblem1 problem = new DummyOptimisationProblem1();
        // For each of the tree vectors, calculate the fitness value (2-dimensional)
        // and test if the fitness-value is correct.
        Iterator<Vector> vectorIterator = testVectors.iterator();
        Vector testVector = vectorIterator.next();
        MOFitness fitness = problem.getFitness(testVector);
        assertThat(fitness.getDimension(), is(2));
        assertEquals(1.46361111111, fitness.getFitness(0).getValue(), EPSILON);
        assertEquals(55, fitness.getFitness(1).getValue(), EPSILON);
        archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem.getFitness(testVector))));
        assertThat(archive.contains(new OptimisationSolution(testVector, problem.getFitness(testVector))), is(true));

        testVector = vectorIterator.next();
        fitness = problem.getFitness(testVector);
        assertThat(fitness.getDimension(), is(2));
        assertEquals(0.491388888889, fitness.getFitness(0).getValue(), EPSILON);
        assertEquals(90, fitness.getFitness(1).getValue(), EPSILON);
        archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem.getFitness(testVector))));
        assertThat(archive.contains(new OptimisationSolution(testVector, problem.getFitness(testVector))), is(true));
        assertThat(archive.size(), is(2));

        testVector = vectorIterator.next();
        fitness = problem.getFitness(testVector);
        assertThat(fitness.getDimension(), is(2));
        assertEquals(0.261797052154, fitness.getFitness(0).getValue(), EPSILON);
        assertEquals(135, fitness.getFitness(1).getValue(), EPSILON);
        archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem.getFitness(testVector))));
        assertThat(archive.contains(new OptimisationSolution(testVector, problem.getFitness(testVector))), is(true));
        assertThat(archive.size(), is(3));

        // After the archive size has been monitored continuously, insert the exact same solutions into the archive again.
        testVectors = createDummyVectors();
        vectorIterator = testVectors.iterator();
        while (vectorIterator.hasNext()) {
            testVector = vectorIterator.next();
            fitness = problem.getFitness(testVector);
            assertThat(fitness.getDimension(), is(2));
            archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem.getFitness(testVector))));
        }
        // Archive size must stay the same due to set-based behavior of archive.
        assertThat(archive.size(), is(3));
    }

    /**
     * This method creates an archive with 3-solutions and presents 3 other (external) solutions to the
     * archive. The archive then returns which of the solutions (it contains) dominates these external
     * solution. The dominant solutions are then checked for validity. A check is also done to see
     * that the archive does not accept these (external) dominated solutions.
     */
    @Test
    public void testArchiveDominatesCandidateSolution() {
        // Create archive and add solutions corresponding to optimisation problem 1. <---> These are the internal solutions.
        DummyOptimisationProblem1 problem1 = new DummyOptimisationProblem1();
        Collection<Vector> testVectors = createDummyVectors();
        for (Vector testVector : testVectors) {
            archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem1.getFitness(testVector))));
        }
        // Arhive size is now 3.
        assertThat(archive.size(), is(3));



        // <---> Now test the external solutions.
        // For the first test vector (1,2,3,4,5) calculate the fitness in terms of optimisation problem 2 and check it.
        DummyOptimisationProblem2 problem2 = new DummyOptimisationProblem2();
        Iterator<Vector> vectorIterator = testVectors.iterator();
        Vector testVector = vectorIterator.next();
        MOFitness fitness = problem2.getFitness(testVector);
        assertThat(fitness.getDimension(), is(2));
        assertEquals(2.92722222222, fitness.getFitness(0).getValue(), EPSILON);
        assertEquals(110, fitness.getFitness(1).getValue(), EPSILON);

        // Then, check which solutions (corresponding to problem 1) dominates the first external solution corresponding to problem 2.
        Collection<OptimisationSolution> dominantSolutions = archive.getDominant(new OptimisationSolution(testVector, problem2.getFitness(testVector)));
        assertThat(dominantSolutions.size(), is(2));
        Iterator<OptimisationSolution> dominantIterator = dominantSolutions.iterator();

        // Solution corresponding to (1,2,3,4,5) with fitness (1.46361111111, 55) dominates external problem 2 solution
        // with fitness (2.92722222222, 110).
        OptimisationSolution dominantSolution = dominantIterator.next();
        assertEquals(1.46361111111, ((MOFitness) dominantSolution.getFitness()).getFitness(0).getValue(), EPSILON);
        assertEquals(55, ((MOFitness) dominantSolution.getFitness()).getFitness(1).getValue(), EPSILON);

        // Solution corresponding to (2,3,4,5,6) with fitness (0.491388888889, 90) also dominates external problem 2 solution
        // with fitness (2.92722222222, 110).
        dominantSolution = dominantIterator.next();
        assertEquals(0.491388888889, ((MOFitness) dominantSolution.getFitness()).getFitness(0).getValue(), EPSILON);
        assertEquals(90, ((MOFitness) dominantSolution.getFitness()).getFitness(1).getValue(), EPSILON);

        assertThat(dominantIterator.hasNext(), is(false));
        // Thus, solution corresponding to (3,4,5,6,7) with fitness (0.261797052154, 135) did not dominate external problem 2
        // solution with fitness (2.92722222222, 110) which is correct.

        // Finally, check that archive does not accept this solution.
        archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem2.getFitness(testVector))));
        assertThat(archive.size(), is(3));
        assertThat(archive.contains(new OptimisationSolution(testVector, problem2.getFitness(testVector))), is(false));




        // For the second vector (2,3,4,5,6) calculate the fitness in terms of optimisation problem 2 and check it.
        testVector = vectorIterator.next();
        fitness = problem2.getFitness(testVector);
        assertThat(fitness.getDimension(), is(2));
        assertEquals(0.982777777778, fitness.getFitness(0).getValue(), EPSILON);
        assertEquals(180, fitness.getFitness(1).getValue(), EPSILON);

        // Now, check which solutions (corresponding to problem 1) dominates the second external solution corresponding to problem 2.
        dominantSolutions = archive.getDominant(new OptimisationSolution(testVector, problem2.getFitness(testVector)));
        assertThat(dominantSolutions.size(), is(2));
        dominantIterator = dominantSolutions.iterator();

        // Solution corresponding to (2,3,4,5,6) with fitness (0.491388888889, 90) also dominates external problem 2 solution
        // with fitness (0.982777777778, 180).
        dominantSolution = dominantIterator.next();
        assertEquals(0.491388888889, ((MOFitness) dominantSolution.getFitness()).getFitness(0).getValue(), EPSILON);
        assertEquals(90, ((MOFitness) dominantSolution.getFitness()).getFitness(1).getValue(), EPSILON);

        // Solution corresponding to (3,4,5,6,7) with fitness (0.261797052154, 135) also dominates external problem 2 solution
        // with fitness (0.982777777778, 180).
        dominantSolution = dominantIterator.next();
        assertEquals(0.261797052154, ((MOFitness) dominantSolution.getFitness()).getFitness(0).getValue(), EPSILON);
        assertEquals(135, ((MOFitness) dominantSolution.getFitness()).getFitness(1).getValue(), EPSILON);

        assertThat(dominantIterator.hasNext(), is(false));
        // Thus, solution corresponding to (1,2,3,4,5) with fitness (1.46361111111, 55) did not dominate external problem 2
        // solution with fitness (0.982777777778, 180) which is correct.

        // Finally, check that archive does not accept this solution.
        archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem2.getFitness(testVector))));
        assertThat(archive.size(), is(3));
        assertThat(archive.contains(new OptimisationSolution(testVector, problem2.getFitness(testVector))), is(false));




        // For the third vector (3,4,5,6,7) calculate the fitness in terms of optimisation problem 2 and check it.
        testVector = vectorIterator.next();
        fitness = problem2.getFitness(testVector);
        assertThat(fitness.getDimension(), is(2));
        assertEquals(0.523594104308, fitness.getFitness(0).getValue(), EPSILON);
        assertEquals(270, fitness.getFitness(1).getValue(), EPSILON);

        // Now, check which solutions (corresponding to problem 1) dominates the second solution corresponding to problem 2.
        dominantSolutions = archive.getDominant(new OptimisationSolution(testVector, problem2.getFitness(testVector)));
        assertThat(dominantSolutions.size(), is(2));
        dominantIterator = dominantSolutions.iterator();

        // Solution corresponding to (2,3,4,5,6) with fitness (0.491388888889, 90) also dominates external problem 2 solution
        // with fitness (0.523594104308, 270).
        dominantSolution = dominantIterator.next();
        assertEquals(0.491388888889, ((MOFitness) dominantSolution.getFitness()).getFitness(0).getValue(), EPSILON);
        assertEquals(90, ((MOFitness) dominantSolution.getFitness()).getFitness(1).getValue(), EPSILON);

        // Solution corresponding to (3,4,5,6,7) with fitness (0.261797052154, 135) also dominates external problem 2 solution
        // with fitness (0.523594104308, 270).
        dominantSolution = dominantIterator.next();
        assertEquals(0.261797052154, ((MOFitness) dominantSolution.getFitness()).getFitness(0).getValue(), EPSILON);
        assertEquals(135, ((MOFitness) dominantSolution.getFitness()).getFitness(1).getValue(), EPSILON);

        assertThat(dominantIterator.hasNext(), is(false));
        // Thus, solution corresponding to (1,2,3,4,5) with fitness (1.46361111111, 55) did not dominate external problem 2
        // solution with fitness (0.523594104308, 270) which is correct.

        // Finally, check that archive does not accept this solution.
        archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem2.getFitness(testVector))));
        assertThat(archive.size(), is(3));
        assertThat(archive.contains(new OptimisationSolution(testVector, problem2.getFitness(testVector))), is(false));
    }

    /**
     * This method creates an archive with 3-solutions and presents 3 other (external) solutions to the
     * archive. The archive then returns which of its solutions is dominated by these external
     * solution. The dominated solutions are then checked for validity. A check is also done to see
     * that the archive accepts the external dominant solutions, and that these solutions replace the
     * dominated solutions.
     */
    @Test
    public void testCandidateDominatesArchiveSolutions() {
        // Create archive and add solutions corresponding to optimisation problem 2.
        DummyOptimisationProblem2 problem2 = new DummyOptimisationProblem2();
        Collection<Vector> testVectors = createDummyVectors();
        for (Vector testVector : testVectors) {
            archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem2.getFitness(testVector))));
        }
        // Arhive size is now 3.
        assertThat(archive.size(), is(3));




        // For the first test vector (1,2,3,4,5) calculate the fitness in terms of optimisation problem 1 and check it.
        DummyOptimisationProblem1 problem1 = new DummyOptimisationProblem1();
        Iterator<Vector> vectorIterator = testVectors.iterator();
        Vector testVector = vectorIterator.next();
        MOFitness fitness = problem1.getFitness(testVector);
        assertThat(fitness.getDimension(), is(2));
        assertEquals(1.46361111111, fitness.getFitness(0).getValue(), EPSILON);
        assertEquals(55, fitness.getFitness(1).getValue(), EPSILON);

        // Then, check which solutions (corresponding to problem 2) is dominated by the first external solution corresponding to problem 1.
        Collection<OptimisationSolution> dominatedSolutions = archive.getDominated(new OptimisationSolution(testVector, problem1.getFitness(testVector)));
        assertThat(dominatedSolutions.size(), is(1));
        Iterator<OptimisationSolution> dominatedIterator = dominatedSolutions.iterator();

        // Solution corresponding to (1,2,3,4,5) with fitness (2.92722222222, 110) is dominated by external problem 1 solution
        // with fitness (1.46361111111, 55).
        OptimisationSolution dominatedSolution = dominatedIterator.next();
        assertEquals(2.92722222222, ((MOFitness) dominatedSolution.getFitness()).getFitness(0).getValue(), EPSILON);
        assertEquals(110, ((MOFitness) dominatedSolution.getFitness()).getFitness(1).getValue(), EPSILON);

        assertThat(dominatedIterator.hasNext(), is(false));
        // Thus, no more solutions in archive is dominated by this external solution.

        // Now, insert this external solution and it should replace abovementioned dominated solution.
        archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem1.getFitness(testVector))));
        assertThat(archive.size(), is(3));
        assertThat(archive.contains(dominatedSolution), is(false));
        assertThat(archive.contains(new OptimisationSolution(testVector, problem1.getFitness(testVector))), is(true));

        // -- Arhive contains (0.982777777778, 180); (0.523594104308, 270); new --> (1.46361111111, 55) <-- new;




        // For the second vector (2,3,4,5,6) calculate the fitness in terms of optimisation problem 1 and check it.
        testVector = vectorIterator.next();
        fitness = problem1.getFitness(testVector);
        assertThat(fitness.getDimension(), is(2));
        assertEquals(0.491388888889, fitness.getFitness(0).getValue(), EPSILON);
        assertEquals(90, fitness.getFitness(1).getValue(), EPSILON);

        // Then, check which solutions (corresponding to problem 2) is dominated by the second external solution corresponding to problem 1.
        dominatedSolutions = archive.getDominated(new OptimisationSolution(testVector, problem1.getFitness(testVector)));
        assertThat(dominatedSolutions.size(), is(2));
        dominatedIterator = dominatedSolutions.iterator();

        // Solution corresponding to (2,3,4,5,6) with fitness (0.982777777778, 180) is dominated by external problem 1 solution
        // with fitness (0.491388888889, 90).
        OptimisationSolution dominatedSolution1 = dominatedIterator.next();
        assertEquals(0.982777777778, ((MOFitness) dominatedSolution1.getFitness()).getFitness(0).getValue(), EPSILON);
        assertEquals(180, ((MOFitness) dominatedSolution1.getFitness()).getFitness(1).getValue(), EPSILON);

        // Solution corresponding to (3,4,5,6,7) with fitness (0.523594104308, 270) is dominated by external problem 1 solution
        // with fitness (0.491388888889, 90).
        OptimisationSolution dominatedSolution2 = dominatedIterator.next();
        assertEquals(0.523594104308, ((MOFitness) dominatedSolution2.getFitness()).getFitness(0).getValue(), EPSILON);
        assertEquals(270, ((MOFitness) dominatedSolution2.getFitness()).getFitness(1).getValue(), EPSILON);

        // Now, insert this external solution and it should replace both of the abovementioned dominated solutions.
        archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem1.getFitness(testVector))));
        assertThat(archive.size(), is(2));
        assertThat(archive.contains(dominatedSolution1), is(false));
        assertThat(archive.contains(dominatedSolution2), is(false));
        assertThat(archive.contains(new OptimisationSolution(testVector, problem1.getFitness(testVector))), is(true));

        // -- Arhive contains (1.46361111111, 55); new --> (0.491388888889, 90) <-- new;




        // For the third vector (3,4,5,6,7) calculate the fitness in terms of optimisation problem 1 and check it.
        testVector = vectorIterator.next();
        fitness = problem1.getFitness(testVector);
        assertThat(fitness.getDimension(), is(2));
        assertEquals(0.261797052154, fitness.getFitness(0).getValue(), EPSILON);
        assertEquals(135, fitness.getFitness(1).getValue(), EPSILON);

        // Then, check which solutions (corresponding to problem 2) is dominated by the second external solution corresponding to problem 1.
        dominatedSolutions = archive.getDominated(new OptimisationSolution(testVector, problem1.getFitness(testVector)));
        assertThat(dominatedSolutions.size(), is(0));    // No solutions dominate external solution.

        // Now, insert this external solution and it should be accepted in archive and not replace any other solution.
        archive.addAll(Arrays.asList(new OptimisationSolution(testVector, problem1.getFitness(testVector))));
        assertThat(archive.size(), is(3));
        assertThat(archive.contains(new OptimisationSolution(testVector, problem1.getFitness(testVector))), is(true));

        // -- Archive contains (1.46361111111, 55); (0.491388888889, 90); (0.261797052154, 135);
    }
}
