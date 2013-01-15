/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.comparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.positionprovider.IterationNeighbourhoodBestUpdateStrategy;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 *
 */
public class SocialBestFitnessComparatorTest {

    /**
     * Create a particle and by default use the memory based fitness strategy.
     * The Ordering will be from least fit to most fit. In other words, this test
     * ensure that the most fit entity is the last element in a sorted list.
     *
     * The most fit entity will be the entity with the <b>smallest</b> fitness value for
     * it's BEST_FITNESS property.
     */
    @Test
    public void minimisationFitnessesMemory() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();
        Particle p3 = new StandardParticle();

        p1.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(0.0));
        p2.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(1.0));
        p3.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(3.0));

        List<Particle> entities = Arrays.asList(p1, p2, p3);
        Collections.sort(entities, new SocialBestFitnessComparator());

        Assert.assertThat(p1, is(entities.get(2)));
    }

    /**
     * Create a particle and by default use the memory based fitness strategy.
     * The Ordering will be from least fit to most fit. In other words, this test
     * ensure that the most fit entity is the last element in a sorted list.
     *
     * The most fit entity will be the entity with the <b>largest</b> fitness value for
     * it's BEST_FITNESS property.
     */
    @Test
    public void maximisationFitnessesMemory() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();
        Particle p3 = new StandardParticle();

        p1.getProperties().put(EntityType.Particle.BEST_FITNESS, new MaximisationFitness(0.0));
        p2.getProperties().put(EntityType.Particle.BEST_FITNESS, new MaximisationFitness(1.0));
        p3.getProperties().put(EntityType.Particle.BEST_FITNESS, new MaximisationFitness(3.0));

        List<Particle> entities = Arrays.asList(p1, p2, p3);
        Collections.sort(entities, new SocialBestFitnessComparator());

        Assert.assertThat(p3, is(entities.get(2)));
    }

    /**
     * Create a particle and set the default fitness strategy to be iteration based.
     * The ordering will be from least fit to most fit, with the most fit particle being
     * the particle with the <b>smallest</b> fitness value.
     *
     * The current iteration strategy operates on the current fitness of the entity.
     */
    @Test
    public void minimisationFitnessesIteration() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();
        Particle p3 = new StandardParticle();

        p1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.0));
        p2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));
        p3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(3.0));

        p1.setNeighbourhoodBestUpdateStrategy(new IterationNeighbourhoodBestUpdateStrategy());
        p2.setNeighbourhoodBestUpdateStrategy(new IterationNeighbourhoodBestUpdateStrategy());
        p3.setNeighbourhoodBestUpdateStrategy(new IterationNeighbourhoodBestUpdateStrategy());

        List<Particle> entities = Arrays.asList(p1, p2, p3);
        Collections.sort(entities, new SocialBestFitnessComparator());

        Assert.assertThat(p1, is(entities.get(2)));
    }

    /**
     * Create a particle and set the default fitness strategy to be iteration based.
     * The ordering will be from least fit to most fit, with the most fit particle being
     * the particle with the <b>largest</b> fitness value.
     *
     * The current iteration strategy operates on the current fitness of the entity.
     */
    @Test
    public void maximisationFitnessesIteration() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();
        Particle p3 = new StandardParticle();

        p1.getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.0));
        p2.getProperties().put(EntityType.FITNESS, new MaximisationFitness(1.0));
        p3.getProperties().put(EntityType.FITNESS, new MaximisationFitness(3.0));

        p1.setNeighbourhoodBestUpdateStrategy(new IterationNeighbourhoodBestUpdateStrategy());
        p2.setNeighbourhoodBestUpdateStrategy(new IterationNeighbourhoodBestUpdateStrategy());
        p3.setNeighbourhoodBestUpdateStrategy(new IterationNeighbourhoodBestUpdateStrategy());

        List<Particle> entities = Arrays.asList(p1, p2, p3);
        Collections.sort(entities, new SocialBestFitnessComparator());

        Assert.assertThat(p3, is(entities.get(2)));
    }

}
