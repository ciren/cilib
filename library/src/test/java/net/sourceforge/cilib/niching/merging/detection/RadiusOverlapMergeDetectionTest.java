/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.niching.merging.detection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.cilib.math.Maths;
import net.cilib.niching.NichingFunctionsTest;
import net.cilib.problem.solution.MinimisationFitness;
import net.cilib.pso.PSO;
import net.cilib.pso.particle.Particle;
import net.cilib.type.types.container.Vector;

import org.junit.Test;

public class RadiusOverlapMergeDetectionTest {
    @Test
    public void testFalseMergeDetection() {
        RadiusOverlapMergeDetection detector = new RadiusOverlapMergeDetection();

        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        Particle p1 = NichingFunctionsTest.createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 1.0));
        Particle p2 = NichingFunctionsTest.createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle p3 = NichingFunctionsTest.createParticle(new MinimisationFitness(2.0), Vector.of(10.0, 10.0));
        Particle p4 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(10.0, 11.0));

        pso1.setTopology(fj.data.List.list(p1, p2));
        pso2.setTopology(fj.data.List.list(p3, p4));

        assertFalse(detector.f(pso1, pso2));
    }

    @Test
    public void testTrueMergeDetection() {
        RadiusOverlapMergeDetection detector = new RadiusOverlapMergeDetection();

        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        Particle p1 = NichingFunctionsTest.createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 1.0));
        Particle p2 = NichingFunctionsTest.createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle p3 = NichingFunctionsTest.createParticle(new MinimisationFitness(2.0), Vector.of(1.0, 1.0));
        Particle p4 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(1.0, 0.0));

        pso1.setTopology(fj.data.List.list(p1, p2));
        pso2.setTopology(fj.data.List.list(p3, p4));

        assertTrue(detector.f(pso1, pso2));
    }

    @Test
    public void testApproximateMergeDetection() {
        RadiusOverlapMergeDetection detector = new RadiusOverlapMergeDetection();

        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        Particle p1 = NichingFunctionsTest.createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 1.0 + Maths.EPSILON));
        Particle p2 = NichingFunctionsTest.createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 1.0));
        Particle p3 = NichingFunctionsTest.createParticle(new MinimisationFitness(2.0), Vector.of(0.0 + Maths.EPSILON, 1.0));
        Particle p4 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 1.0));

        pso1.setTopology(fj.data.List.list(p1, p2));
        pso2.setTopology(fj.data.List.list(p3, p4));

        assertTrue(detector.f(pso1, pso2));
    }
}
