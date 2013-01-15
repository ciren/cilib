/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class EntitiesTest {
    @Test
    public void testGetCandidateSolutions() {
        Vector v1 = Vector.of(1.0, 2.0, 3.0);
        Vector v2 = Vector.of(4.0, 5.0, 6.0);

        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();

        p1.setCandidateSolution(v1);
        p2.setCandidateSolution(v2);

        List<Vector> list = Entities.<Vector>getCandidateSolutions(Arrays.asList(p1, p2));

        assertEquals(v1, list.get(0));
        assertEquals(v2, list.get(1));
    }
}
