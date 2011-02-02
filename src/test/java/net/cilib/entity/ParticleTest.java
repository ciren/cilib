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
package net.cilib.entity;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author gpampara
 */
public class ParticleTest {
    @Test
    public void isMoreFit() {
        Particle p1 = new Particle(CandidateSolution.empty(), CandidateSolution.empty(), Velocity.copyOf(), Fitnesses.newMaximizationFitness(0.0));
        Particle p2 = new Particle(CandidateSolution.empty(), CandidateSolution.empty(), Velocity.copyOf(), Fitnesses.newMaximizationFitness(1.0));

        Assert.assertTrue(p2.isMoreFit(p1));
    }

    @Test
    public void staticCreation() {
        Particle p = Particle.create(CandidateSolution.empty());

        Assert.assertThat(CandidateSolution.empty(), sameInstance(p.solution()));
        Assert.assertThat(CandidateSolution.empty(), sameInstance(p.memory()));
        Assert.assertThat(Fitnesses.inferior(), sameInstance(p.fitness()));
//        Assert.assertThat(Velocity.copyOf(), p.velocity()));
    }

    @Test
    public void equivalence() {
        CandidateSolution empty = CandidateSolution.empty();
        Particle p1 = new Particle(empty, empty, Velocity.copyOf(), Fitnesses.newMinimizationFitness(1.0));
        Particle p2 = new Particle(empty, empty, Velocity.copyOf(), Fitnesses.newMinimizationFitness(1.0));

        Assert.assertTrue(p1.equiv(p2));
    }

    @Test
    public void falseEquivalence() {
        CandidateSolution empty = CandidateSolution.empty();
        Particle p1 = new Particle(empty, empty, Velocity.copyOf(), Fitnesses.newMinimizationFitness(1.0));
        Particle p2 = new Particle(empty, empty, Velocity.copyOf(), Fitnesses.newMinimizationFitness(8.0));

        Assert.assertFalse(p1.equiv(p2));
    }
}