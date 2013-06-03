/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity;

import fj.data.List;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Real;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiversityTest {

    @Test
    public void getValue() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();
        Particle p3 = new StandardParticle();

        p1.setPosition(Vector.of(1, 1, 1));
        p2.setPosition(Vector.of(2, 2, 2));
        p3.setPosition(Vector.of(3, 3, 3));

        final List<Particle> topology = List.list(p1, p2, p3);

        final SinglePopulationBasedAlgorithm algorithm = mock(SinglePopulationBasedAlgorithm.class);

        when(algorithm.getTopology()).thenReturn(topology);

        Diversity diversity = new Diversity();
        double expected = (Math.sqrt(3) + Math.sqrt(3)) / 3.0;
        Assert.assertEquals(Real.valueOf(expected), diversity.getValue(algorithm));
    }
}
