/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.niching.merging.detection;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.niching.NichingFunctionsTest;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author filipe
 */
public class RadiusOverlapMergeDetectionTest {
    @Test
    public void testFalseMergeDetection() {
        RadiusOverlapMergeDetection detector = new RadiusOverlapMergeDetection();
        
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();
        
        pso1.setTopology(new GBestTopology());
        pso2.setTopology(new GBestTopology());
        
        Particle p1 = NichingFunctionsTest.createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 1.0));
        Particle p2 = NichingFunctionsTest.createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle p3 = NichingFunctionsTest.createParticle(new MinimisationFitness(2.0), Vector.of(10.0, 10.0));
        Particle p4 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(10.0, 11.0));
        
        pso1.getTopology().add(p1); pso1.getTopology().add(p2);
        pso2.getTopology().add(p3); pso2.getTopology().add(p4);

        assertFalse(detector.f(pso1, pso2));
    }
    
    @Test
    public void testTrueMergeDetection() {
        RadiusOverlapMergeDetection detector = new RadiusOverlapMergeDetection();
        
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();
        
        pso1.setTopology(new GBestTopology());
        pso2.setTopology(new GBestTopology());
        
        Particle p1 = NichingFunctionsTest.createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 1.0));
        Particle p2 = NichingFunctionsTest.createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle p3 = NichingFunctionsTest.createParticle(new MinimisationFitness(2.0), Vector.of(1.0, 1.0));
        Particle p4 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(1.0, 0.0));
        
        pso1.getTopology().add(p1); pso1.getTopology().add(p2);
        pso2.getTopology().add(p3); pso2.getTopology().add(p4);

        assertTrue(detector.f(pso1, pso2));
    }
    
    @Test
    public void testApproximateMergeDetection() {
        RadiusOverlapMergeDetection detector = new RadiusOverlapMergeDetection();
        
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();
        
        pso1.setTopology(new GBestTopology());
        pso2.setTopology(new GBestTopology());
        
        Particle p1 = NichingFunctionsTest.createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 1.0 + Maths.EPSILON));
        Particle p2 = NichingFunctionsTest.createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 1.0));
        Particle p3 = NichingFunctionsTest.createParticle(new MinimisationFitness(2.0), Vector.of(0.0 + Maths.EPSILON, 1.0));
        Particle p4 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 1.0));
        
        pso1.getTopology().add(p1); pso1.getTopology().add(p2);
        pso2.getTopology().add(p3); pso2.getTopology().add(p4);

        assertTrue(detector.f(pso1, pso2));
    }
}
