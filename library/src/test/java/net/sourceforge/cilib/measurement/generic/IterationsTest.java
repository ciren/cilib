/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.generic;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Int;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IterationsTest {

    @Test
    public void iterationNumber() {
        final int expected = 1000;
        final PopulationBasedAlgorithm algorithm = mock(PopulationBasedAlgorithm.class);

        when(algorithm.getIterations()).thenReturn(expected);

        Measurement m = new Iterations();
        Assert.assertEquals(expected, ((Int) m.getValue(algorithm)).intValue());
    }

    @Test
    public void resultType() {
        final PopulationBasedAlgorithm algorithm = mock(PopulationBasedAlgorithm.class);

        Measurement m = new Iterations();
        Assert.assertThat(m.getValue(algorithm), is(Int.class));
    }
}
