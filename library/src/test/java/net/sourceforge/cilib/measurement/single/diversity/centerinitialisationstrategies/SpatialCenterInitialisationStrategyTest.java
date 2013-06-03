/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies;

import fj.data.List;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SpatialCenterInitialisationStrategyTest {
    @Test
    public void testGetCenter() {
        StandardParticle p1 = new StandardParticle();
        StandardParticle p2 = new StandardParticle();
        StandardParticle p3 = new StandardParticle();

        p1.setPosition(Vector.of(1, 1, 1));
        p2.setPosition(Vector.of(2, 2, 2));
        p3.setPosition(Vector.of(3, 3, 3));

        List<StandardParticle> topology = List.list(p1, p2, p3);

        SpatialCenterInitialisationStrategy sc =
            new SpatialCenterInitialisationStrategy();

        Vector center = sc.getCenter(topology);
        assertEquals(Vector.of(2, 2, 2), center);
    }

}
