/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.particle.Particle;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class LinearPositionProviderTest {
    @Test
    public void testGet() {
        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION,
                    Vector.of(Real.valueOf(1.0, new Bounds(-5.0, 5.0)),
                              Real.valueOf(2.0, new Bounds(-5.0, 5.0))));

        particle.getProperties().put(EntityType.Particle.VELOCITY,
                    Vector.of(Real.valueOf(0.0, new Bounds(-10.0, 10.0)),
                              Real.valueOf(0.0, new Bounds(-10.0, 10.0))));

        Vector updatedVector = new LinearPositionProvider().get(particle);

        //must have velocity's vector components and position's bounds
        assertEquals(updatedVector, Vector.of(Real.valueOf(0.0, new Bounds(-5.0, 5.0)),
                                              Real.valueOf(0.0, new Bounds(-5.0, 5.0))));
    }
}
