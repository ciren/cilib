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
package net.sourceforge.cilib.coevolution.cooperative.contributionselection;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TopologyBestContributionSelectionStrategyTest {

    @Test
    public void TopologyBestContributionSelectionTest(){

        PopulationBasedAlgorithm algorithm = new PSO();

        Particle e1 = new StandardParticle();
        Particle e2 = new StandardParticle();
        Particle e3 = new StandardParticle();

        e1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        e2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.5));
        e3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));

        e1.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.0));
        e2.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(1.0));
        e3.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.5));

        Vector v1 = Vector.of(1);
        e1.getProperties().put(EntityType.CANDIDATE_SOLUTION, v1);

        Vector v2 = Vector.of(2);
        e2.getProperties().put(EntityType.CANDIDATE_SOLUTION, v2);

        Vector v3 = Vector.of(3);
        e3.getProperties().put(EntityType.CANDIDATE_SOLUTION, v3);


        ((Topology<Particle>)algorithm.getTopology()).add(e1);
        ((Topology<Particle>)algorithm.getTopology()).add(e2);
        ((Topology<Particle>)algorithm.getTopology()).add(e3);

        TopologyBestContributionSelectionStrategy test = new TopologyBestContributionSelectionStrategy();

        Vector selected = test.getContribution(algorithm);

        assertEquals(3.0, selected.get(0).doubleValue(), 0.0);
    }

}
