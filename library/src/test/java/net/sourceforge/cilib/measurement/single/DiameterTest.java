/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.measurement.single;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.cilib.entity.EntityType;
import net.cilib.measurement.Measurement;
import net.cilib.pso.particle.Particle;
import net.cilib.pso.particle.StandardParticle;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.Vector;

import org.junit.Assert;
import org.junit.Test;

public class DiameterTest {

    @Test
    public void simpleDiameter() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();

        p1.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.0, 0.0));
        p2.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(2.0, 2.0));

        final fj.data.List<Particle> topology = fj.data.List.list(p1, p2);

        final SinglePopulationBasedAlgorithm algorithm = mock(SinglePopulationBasedAlgorithm.class);

        when(algorithm.getTopology()).thenReturn((fj.data.List) topology);

        Measurement m = new Diameter();
        Assert.assertEquals(Real.valueOf(Math.sqrt(8)), m.getValue(algorithm));
    }

    @Test
    public void complexDiameter() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();
        Particle p3 = new StandardParticle();
        Particle p4 = new StandardParticle();

        p1.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.0, 0.0));
        p2.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(1.0, 1.0));
        p3.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(1.5, 1.5));
        p4.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(2.0, 2.0));

        final fj.data.List<Particle> topology = fj.data.List.list(p1, p2, p3, p4);

        final SinglePopulationBasedAlgorithm algorithm = mock(SinglePopulationBasedAlgorithm.class);

        when(algorithm.getTopology()).thenReturn((fj.data.List) topology);

        Measurement m = new Diameter();
        Assert.assertEquals(Real.valueOf(Math.sqrt(8)), m.getValue(algorithm));
    }
}
