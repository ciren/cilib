/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.particle;

import static org.junit.Assert.*;

import org.junit.Test;

public class StandardParticleTest {

    @Test
    public void equals() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();

        p1.setNeighbourhoodBest(p1);
        p2.setNeighbourhoodBest(p1);

        assertFalse(p1.equals(p2));
        assertFalse(p1.hashCode() == p2.hashCode());
        assertFalse(p1.equals(null));
    }

    @Test
    public void hashCodes() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();

        assertTrue(p1.hashCode() != p2.hashCode());
    }

}
