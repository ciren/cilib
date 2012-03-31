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
package net.sourceforge.cilib.pso.niching.creation;

import fj.P2;
import java.util.Arrays;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.niching.NichingTest;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author filipe
 */
public class ClosestNeighbourNicheCreationStrategyTest {
    
    @Test
    public void testCreation() {
        Particle p1 = NichingTest.createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 1.0));
        Particle p2 = NichingTest.createParticle(new MinimisationFitness(2.0), Vector.of(1.0, 1.0));
        Particle p3 = NichingTest.createParticle(new MinimisationFitness(1.0), Vector.of(2.0, 2.0));
        
        PSO pso = new PSO();        
        pso.getTopology().addAll(Arrays.asList(p1, p2, p3));
        
        ClosestNeighbourNicheCreationStrategy creator = new ClosestNeighbourNicheCreationStrategy();
        P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> swarms = creator.f(pso, p1);
        
        Assert.assertEquals(1, swarms._1().getTopology().size());
        Assert.assertEquals(Vector.of(2.0, 2.0), swarms._1().getTopology().get(0).getCandidateSolution());
        Assert.assertEquals(2, swarms._2().getTopology().size());
        Assert.assertEquals(Vector.of(0.0, 1.0), swarms._2().getTopology().get(0).getCandidateSolution());
        Assert.assertEquals(Vector.of(1.0, 1.0), swarms._2().getTopology().get(1).getCandidateSolution());
    }
}