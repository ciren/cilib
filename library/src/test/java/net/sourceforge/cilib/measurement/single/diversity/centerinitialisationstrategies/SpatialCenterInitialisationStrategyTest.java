/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies;

import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SpatialCenterInitialisationStrategyTest {
    @Test
    public void testGetCenter() {
        Topology<StandardParticle> topology = new GBestTopology();

        StandardParticle p1 = new StandardParticle();
        StandardParticle p2 = new StandardParticle();
        StandardParticle p3 = new StandardParticle();

        p1.setCandidateSolution(Vector.of(1, 1, 1));
        p2.setCandidateSolution(Vector.of(2, 2, 2));
        p3.setCandidateSolution(Vector.of(3, 3, 3));

        topology.add(p1);
        topology.add(p2);
        topology.add(p3);

        SpatialCenterInitialisationStrategy sc =
            new SpatialCenterInitialisationStrategy();

        Vector center = sc.getCenter(topology);
        assertEquals(Vector.of(2, 2, 2), center);
    }

}
