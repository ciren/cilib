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
package net.sourceforge.cilib.pso.niching.merging;

import fj.P2;
import fj.data.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.niching.Niche;
import net.sourceforge.cilib.pso.niching.NicheTest;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author filipe
 */
public class StandardMergeStrategyTest {

    @Test
    public void testStandardMerge() {
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        pso1.setTopology(new GBestTopology<Particle>());
        pso2.setTopology(new GBestTopology<Particle>());

        Particle p1 = NicheTest.createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle p2 = NicheTest.createParticle(new MinimisationFitness(0.0), Vector.of(Math.sqrt(0.6), Math.sqrt(0.6)));
        Particle p3 = NicheTest.createParticle(new MinimisationFitness(2.0), Vector.of(Math.sqrt(0.3), Math.sqrt(0.3)));
        Particle p4 = NicheTest.createParticle(new MinimisationFitness(3.0), Vector.of(1.0, 1.0));

        pso1.getTopology().add(p1); pso2.getTopology().add(p2);
        pso2.getTopology().add(p3); pso2.getTopology().add(p4);

        StandardMergeStrategy merge = new StandardMergeStrategy();

        Assert.assertEquals(4, merge.f(pso1, pso2).getTopology().size());
        Assert.assertEquals(merge.f(pso1, pso2).getBestSolution().getPosition(), pso2.getBestSolution().getPosition());
    }
}