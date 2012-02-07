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
package net.sourceforge.cilib.entity.topologies;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.comparator.DescendingFitnessComparator;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Test;

public class AbstractTopologyTest {

    @Test
    public void comparatorBestEntity() {
        Particle i1 = new StandardParticle();
        Particle i2 = new StandardParticle();
        Particle i3 = new StandardParticle();

        i1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.0));
        i2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));
        i3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.5));

        i1.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.0));
        i2.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(1.0));
        i3.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.5));

        Topology<Particle> topology = new GBestTopology<Particle>();
        topology.add(i1);
        topology.add(i2);
        topology.add(i3);

        Particle socialBest = topology.getBestEntity(new SocialBestFitnessComparator<Particle>());
        Particle mostFit = topology.getBestEntity(new AscendingFitnessComparator<Particle>());
        Particle leastFit = topology.getBestEntity(new DescendingFitnessComparator<Particle>());
        Particle other = topology.getBestEntity();

        Assert.assertThat(socialBest, is(other));
        Assert.assertThat(mostFit, is(i1));
        Assert.assertThat(leastFit, is(i2));
    }

    @Test
    public void compareBestFitnessEntity() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();

        p1.getProperties().put(EntityType.FITNESS, new MaximisationFitness(400.0));
        p2.getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.0));

        Topology<Particle> topology = new GBestTopology<Particle>();
        topology.add(p1);
        topology.add(p2);

        Particle best = topology.getBestEntity();

        Assert.assertThat(best, is(p1));
    }

}
