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
package net.sourceforge.cilib.entity;

import java.util.Arrays;
import java.util.Set;
import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.comparator.DescendingFitnessComparator;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.entity.topologies.LBestTopology;
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class TopologiesTest {
    
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

        Topology<Particle> topology = new LBestTopology<Particle>();
        topology.add(i1);
        topology.add(i2);
        topology.add(i3);

        Particle socialBest = Topologies.getBestEntity(topology, new SocialBestFitnessComparator<Particle>());
        Particle mostFit = Topologies.getBestEntity(topology, new AscendingFitnessComparator<Particle>());
        Particle leastFit = Topologies.getBestEntity(topology, new DescendingFitnessComparator<Particle>());
        Particle other = Topologies.getBestEntity(topology);

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

        Particle best = Topologies.getBestEntity(topology);

        Assert.assertThat(best, is(p1));
    }
    
    @Test
    public void comparatorNeighbourhoodBest() {
        Particle i1 = new StandardParticle();
        Particle i2 = new StandardParticle();
        Particle i3 = new StandardParticle();
        Particle i4 = new StandardParticle();

        i1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.7));
        i2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));
        i3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.5));
        i4.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.2));

        i1.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.7));
        i2.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(1.0));
        i3.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.0));
        i4.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.1));

        Topology<Particle> topology = new LBestTopology<Particle>();
        topology.add(i1);
        topology.add(i2);
        topology.add(i3);
        topology.add(i4);

        Particle socialBest = Topologies.getNeighbourhoodBest(topology, i1, new SocialBestFitnessComparator<Particle>());
        Particle mostFit = Topologies.getNeighbourhoodBest(topology, i1, new AscendingFitnessComparator<Particle>());
        Particle leastFit = Topologies.getNeighbourhoodBest(topology, i1, new DescendingFitnessComparator<Particle>());
        Particle other = Topologies.getNeighbourhoodBest(topology, i2);

        Assert.assertThat(socialBest, is(i4));
        Assert.assertThat(mostFit, is(i4));
        Assert.assertThat(leastFit, is(i2));
        Assert.assertThat(other, is(i3));
    }
    
    @Test
    public void comparatorNeighbourhoodBestEntities() {
        Particle i1 = new StandardParticle();
        Particle i2 = new StandardParticle();
        Particle i3 = new StandardParticle();
        Particle i4 = new StandardParticle();

        i1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.7));
        i2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));
        i3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.5));
        i4.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.2));

        i1.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.7));
        i2.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(1.0));
        i3.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.0));
        i4.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.1));

        Topology<Particle> topology = new LBestTopology<Particle>();
        topology.add(i1);
        topology.add(i2);
        topology.add(i3);
        topology.add(i4);

        Set<Particle> socialBest = Topologies.getNeighbourhoodBestEntities(topology, new SocialBestFitnessComparator<Particle>());
        Set<Particle> mostFit = Topologies.getNeighbourhoodBestEntities(topology, new AscendingFitnessComparator<Particle>());
        Set<Particle> leastFit = Topologies.getNeighbourhoodBestEntities(topology, new DescendingFitnessComparator<Particle>());
        Set<Particle> other = Topologies.getNeighbourhoodBestEntities(topology);

        Assert.assertTrue(socialBest.containsAll(Arrays.asList(i3, i4)));
        Assert.assertTrue(mostFit.containsAll(Arrays.asList(i3, i4)));
        Assert.assertTrue(leastFit.containsAll(Arrays.asList(i1, i2)));
        Assert.assertTrue(other.containsAll(Arrays.asList(i3, i4)));
    }
}
