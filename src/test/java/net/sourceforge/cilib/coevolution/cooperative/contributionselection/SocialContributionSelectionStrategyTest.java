/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.coevolution.cooperative.contributionselection;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;

public class SocialContributionSelectionStrategyTest {
    @Test
    public void socialContributionSelectionTest(){

        PopulationBasedAlgorithm algorithm = new PSO();

        Particle e1 = new StandardParticle();
        Particle e2 = new StandardParticle();
        Particle e3 = new StandardParticle();

        e1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        e2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.5));
        e3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));

        e1.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(1.0));
        e2.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.0));
        e3.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.5));

        Vector v1 = new Vector();
        v1.add(new Real(1.0));
        e1.getProperties().put(EntityType.Particle.BEST_POSITION, v1);

        Vector v2 = new Vector();
        v2.add(new Real(2.0));
        e2.getProperties().put(EntityType.Particle.BEST_POSITION, v2);

        Vector v3 = new Vector();
        v3.add(new Real(3.0));
        e3.getProperties().put(EntityType.Particle.BEST_POSITION, v3);


        ((Topology<Particle>)algorithm.getTopology()).add(e1);
        ((Topology<Particle>)algorithm.getTopology()).add(e2);
        ((Topology<Particle>)algorithm.getTopology()).add(e3);

        SocialFitnessContributionSelectionStrategy test = new SocialFitnessContributionSelectionStrategy();

        Vector selected = test.getContribution(algorithm);

        assertEquals(2.0, selected.get(0).doubleValue(), 0.0);
    }
}
