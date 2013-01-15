/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiameterTest {

    @Test
    public void simpleDiameter() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();

        p1.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.0, 0.0));
        p2.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(2.0, 2.0));

        final Topology<Particle> topology = new GBestTopology<Particle>();
        topology.add(p1);
        topology.add(p2);

        final PopulationBasedAlgorithm algorithm = mock(PopulationBasedAlgorithm.class);

        when(algorithm.getTopology()).thenReturn((Topology) topology);

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

        final Topology<Particle> topology = new GBestTopology<Particle>();
        topology.add(p1);
        topology.add(p2);
        topology.add(p3);
        topology.add(p4);

        final PopulationBasedAlgorithm algorithm = mock(PopulationBasedAlgorithm.class);

        when(algorithm.getTopology()).thenReturn((Topology) topology);

        Measurement m = new Diameter();
        Assert.assertEquals(Real.valueOf(Math.sqrt(8)), m.getValue(algorithm));
    }
}
