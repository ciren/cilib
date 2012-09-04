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
package net.sourceforge.cilib.niching.creation;

import fj.P2;
import fj.data.List;
import java.util.Arrays;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.niching.NichingSwarms;
import net.sourceforge.cilib.niching.NichingFunctionsTest;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

public class ClosestNeighbourNicheCreationStrategyTest {
    
    @Test
    public void testCreation() {
        Particle p1 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 1.0));
        Particle p2 = NichingFunctionsTest.createParticle(new MinimisationFitness(2.0), Vector.of(1.0, 1.0));
        Particle p3 = NichingFunctionsTest.createParticle(new MinimisationFitness(1.0), Vector.of(2.0, 2.0));
        
        PSO pso = new PSO();        
        pso.getTopology().addAll(Arrays.asList(p1, p2, p3));
        
        ClosestNeighbourNicheCreationStrategy creator = new ClosestNeighbourNicheCreationStrategy();
        creator.setSwarmBehavior(new ParticleBehavior());
        NichingSwarms swarms = creator.f(NichingSwarms.of(pso, List.<PopulationBasedAlgorithm>nil()), p1);
        
        Assert.assertEquals(1, swarms._1().getTopology().size());
        Assert.assertEquals(Vector.of(2.0, 2.0), swarms._1().getTopology().get(0).getCandidateSolution());
        Assert.assertEquals(2, swarms._2().head().getTopology().size());
        Assert.assertEquals(Vector.of(0.0, 1.0), swarms._2().head().getTopology().get(0).getCandidateSolution());
        Assert.assertEquals(Vector.of(1.0, 1.0), swarms._2().head().getTopology().get(1).getCandidateSolution());
    }
}
