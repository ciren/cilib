/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import org.junit.Test;

public class AbstractTopologyTest {

    @Test
    public void comparatorBestEntity() {
        /*Particle i1 = new StandardParticle();
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
        Assert.assertThat(leastFit, is(i2));*/
    }

    @Test
    public void compareBestFitnessEntity() {
        /*Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();

        p1.getProperties().put(EntityType.FITNESS, new MaximisationFitness(400.0));
        p2.getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.0));

        Topology<Particle> topology = new GBestTopology<Particle>();
        topology.add(p1);
        topology.add(p2);

        Particle best = topology.getBestEntity();

        Assert.assertThat(best, is(p1));*/
    }

}
