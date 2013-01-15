/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.Int;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class FitnessEvaluationsTest {

    @Test
    public void result() {
        final PopulationBasedAlgorithm pba = mock(PopulationBasedAlgorithm.class);
        final Problem problem = mock(Problem.class);

        when(pba.getOptimisationProblem()).thenReturn(problem);
        when(problem.getFitnessEvaluations()).thenReturn(10, 20);

        Measurement m = new FitnessEvaluations();
        Int i1 = (Int) m.getValue(pba);
        Int i2 = (Int) m.getValue(pba);

        Assert.assertThat(i1.intValue(), is(10));
        Assert.assertThat(i2.intValue(), is(20));
    }
}
