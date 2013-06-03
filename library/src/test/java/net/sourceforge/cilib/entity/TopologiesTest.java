/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.Set;

import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.comparator.DescendingFitnessComparator;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.topologies.LBestNeighbourhood;
import net.sourceforge.cilib.entity.topologies.Neighbourhood;
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;

import org.junit.Assert;
import org.junit.Test;

public class TopologiesTest {

    @Test
    public void comparatorBestEntity() {
        Particle i1 = new StandardParticle();
        Particle i2 = new StandardParticle();
        Particle i3 = new StandardParticle();

        i1.put(Property.FITNESS, new MinimisationFitness(0.0));
        i2.put(Property.FITNESS, new MinimisationFitness(1.0));
        i3.put(Property.FITNESS, new MinimisationFitness(0.5));

        i1.put(Property.BEST_FITNESS, new MinimisationFitness(0.0));
        i2.put(Property.BEST_FITNESS, new MinimisationFitness(1.0));
        i3.put(Property.BEST_FITNESS, new MinimisationFitness(0.5));

        fj.data.List<Particle> topology = fj.data.List.list(i1, i2, i3);

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

        p1.put(Property.FITNESS, new MaximisationFitness(400.0));
        p2.put(Property.FITNESS, new MaximisationFitness(0.0));

        fj.data.List<Particle> topology = fj.data.List.list(p1, p2);

        Particle best = Topologies.getBestEntity(topology);

        Assert.assertThat(best, is(p1));
    }

    @Test
    public void comparatorNeighbourhoodBest() {
        Particle i1 = new StandardParticle();
        Particle i2 = new StandardParticle();
        Particle i3 = new StandardParticle();
        Particle i4 = new StandardParticle();

        i1.put(Property.FITNESS, new MinimisationFitness(0.7));
        i2.put(Property.FITNESS, new MinimisationFitness(1.0));
        i3.put(Property.FITNESS, new MinimisationFitness(0.5));
        i4.put(Property.FITNESS, new MinimisationFitness(0.2));

        i1.put(Property.BEST_FITNESS, new MinimisationFitness(0.7));
        i2.put(Property.BEST_FITNESS, new MinimisationFitness(1.0));
        i3.put(Property.BEST_FITNESS, new MinimisationFitness(0.0));
        i4.put(Property.BEST_FITNESS, new MinimisationFitness(0.1));

        fj.data.List<Particle> topology = fj.data.List.list(i1, i2, i3, i4);
        Neighbourhood<Particle> neighbourhood = new LBestNeighbourhood<>();

        Particle socialBest = Topologies.getNeighbourhoodBest(topology, i1, neighbourhood, new SocialBestFitnessComparator<Particle>());
        Particle mostFit = Topologies.getNeighbourhoodBest(topology, i1, neighbourhood, new AscendingFitnessComparator<Particle>());
        Particle leastFit = Topologies.getNeighbourhoodBest(topology, i1, neighbourhood, new DescendingFitnessComparator<Particle>());
        Particle other = Topologies.getNeighbourhoodBest(topology, i2, neighbourhood);

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

        i1.put(Property.FITNESS, new MinimisationFitness(0.7));
        i2.put(Property.FITNESS, new MinimisationFitness(1.0));
        i3.put(Property.FITNESS, new MinimisationFitness(0.5));
        i4.put(Property.FITNESS, new MinimisationFitness(0.2));

        i1.put(Property.BEST_FITNESS, new MinimisationFitness(0.7));
        i2.put(Property.BEST_FITNESS, new MinimisationFitness(1.0));
        i3.put(Property.BEST_FITNESS, new MinimisationFitness(0.0));
        i4.put(Property.BEST_FITNESS, new MinimisationFitness(0.1));

        fj.data.List<Particle> topology = fj.data.List.list(i1, i2, i3, i4);
        Neighbourhood<Particle> neighbourhood = new LBestNeighbourhood<>();

        Set<Particle> socialBest = Topologies.getNeighbourhoodBestEntities(topology, neighbourhood, new SocialBestFitnessComparator<Particle>());
        Set<Particle> mostFit = Topologies.getNeighbourhoodBestEntities(topology, neighbourhood, new AscendingFitnessComparator<Particle>());
        Set<Particle> leastFit = Topologies.getNeighbourhoodBestEntities(topology, neighbourhood, new DescendingFitnessComparator<Particle>());
        Set<Particle> other = Topologies.getNeighbourhoodBestEntities(topology, neighbourhood);

        Assert.assertTrue(socialBest.containsAll(Arrays.asList(i3, i4)));
        Assert.assertTrue(mostFit.containsAll(Arrays.asList(i3, i4)));
        Assert.assertTrue(leastFit.containsAll(Arrays.asList(i1, i2)));
        Assert.assertTrue(other.containsAll(Arrays.asList(i3, i4)));
    }
}
