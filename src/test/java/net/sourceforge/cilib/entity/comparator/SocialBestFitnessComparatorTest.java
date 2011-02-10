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
package net.sourceforge.cilib.entity.comparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.positionprovider.IterationNeighbourhoodBestUpdateStrategy;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 *
 * @author gpampara
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
    public void minimizationFitnessesMemory() {
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
    public void maximizationFitnessesMemory() {
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
    public void minimizationFitnessesIteration() {
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
    public void maximizationFitnessesIteration() {
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
