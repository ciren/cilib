/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.solution;

import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class MOFitnessTest {

    private static MOOptimisationProblem moproblem;

    public MOFitnessTest() {

    }

    @BeforeClass
    public static void setUp() {
        moproblem = new MOOptimisationProblem();
        for (int i = 0; i < 3; i++) {
            moproblem.add(new DummyOptimisationProblem(i));
        }
    }

    @Test
    public void testAllInferior() {
        Fitness inferior[] = new Fitness[]{InferiorFitness.instance(), InferiorFitness.instance(), InferiorFitness.instance()};
        Fitness oneFitness[] = new Fitness[]{
            new MinimisationFitness(new Integer(1).doubleValue()),
            new MinimisationFitness(new Integer(1).doubleValue()),
            new MinimisationFitness(new Integer(1).doubleValue())};

        Fitness f1 = moproblem.getFitness(inferior);
        Fitness f2 = moproblem.getFitness(oneFitness);

        assertTrue(f1.compareTo(f2) < 0);
        assertTrue(f2.compareTo(f1) > 0);
    }

    private static class DummyOptimisationProblem implements Problem {
        private static final long serialVersionUID = -2955989686805159033L;

        private int i;

        public DummyOptimisationProblem(int i) {
            this.i = i;
        }

        public DummyOptimisationProblem(DummyOptimisationProblem copy) {
        }

        @Override
        public DummyOptimisationProblem getClone() {
            return new DummyOptimisationProblem(this);
        }

        @Override
        public Fitness getFitness(Type solution) {
            return (Fitness) solution;
        }

        @Override
        public int getFitnessEvaluations() {
            throw new UnsupportedOperationException("This method is not implemented");
        }

        @Override
        public DomainRegistry getDomain() {
            throw new UnsupportedOperationException("This method is not implemented");
        }

        @Override
        public void setDomain(String domain) {
            throw new UnsupportedOperationException("This method is not implemented");
        }
    }
}
