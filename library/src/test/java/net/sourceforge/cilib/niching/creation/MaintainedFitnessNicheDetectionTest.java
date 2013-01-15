/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.creation;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.niching.NichingFunctionsTest;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

public class MaintainedFitnessNicheDetectionTest {

    @Test
    public void testDetection() {
        Particle p1 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 1.0));
        Particle p2 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 1.0));

        MaintainedFitnessNicheDetection detection = new MaintainedFitnessNicheDetection();
        Assert.assertFalse(detection.f(null, p1));
        p1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.999999));
        p1 = p1.getClone();
        Assert.assertFalse(detection.f(null, p1));
        p1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.999998));
        p1 = p1.getClone();
        Assert.assertTrue(detection.f(null, p1));

        Assert.assertFalse(detection.f(null, p2));
        p2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.999999));
        p2 = p2.getClone();
        Assert.assertFalse(detection.f(null, p2));
        p2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.999998));
        p2 = p2.getClone();
        Assert.assertTrue(detection.f(null, p2));
        p2 = p2.getClone();
        detection.f(null, p2);
        Assert.assertEquals(3, ((TypeList) p2.getProperties().get(MaintainedFitnessNicheDetection.NicheEnum.NICHE_DETECTION_FITNESSES)).size());
    }

}
