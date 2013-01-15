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
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ParticleBoundViolationsTest {

    @Test
    public void testParticleBoundViolations() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();
        Particle p3 = new StandardParticle();
        Particle p4 = new StandardParticle();

        Bounds bounds = new Bounds(0.0, 2.0);
        p1.getProperties().put(EntityType.CANDIDATE_SOLUTION, vectorOf(bounds, 0.0, -1.0, 0.0));
        p2.getProperties().put(EntityType.CANDIDATE_SOLUTION, vectorOf(bounds, 1.0, 2.0, 2.0));
        p3.getProperties().put(EntityType.CANDIDATE_SOLUTION, vectorOf(bounds, -1.0,0.0,1.0));
        p4.getProperties().put(EntityType.CANDIDATE_SOLUTION, vectorOf(bounds, 1.0,2.0,-1.0));

        final Topology<Particle> topology = new GBestTopology<Particle>();
        topology.add(p1);
        topology.add(p2);
        topology.add(p3);
        topology.add(p4);

        final PopulationBasedAlgorithm pba = mock(PopulationBasedAlgorithm.class);

        when(pba.getTopology()).thenReturn((Topology) topology);

        Measurement m = new ParticleBoundViolations();
        Assert.assertEquals(Real.valueOf(3.0/topology.size()), m.getValue(pba));

        verify(pba, atLeast(1)).getTopology();
    }

    private Vector vectorOf(Bounds bounds, double... values) {
        Vector.Builder vector = Vector.newBuilder();
        for (int i = 0; i < values.length; i++) {
            vector.add(Real.valueOf(values[i], bounds));
        }
        return vector.build();
    }

}
