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
package net.cilib.pso;

import fj.F;
import fj.data.Option;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.CandidateSolution;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.collection.immutable.Velocity;
import net.cilib.entity.Entity;
import net.cilib.entity.Individual;
import net.cilib.entity.Particle;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 */
public class PersonalBestTest {

    @Test
    public void memoryReturnedFromMemoryEntity() {
        CandidateSolution memory = CandidateSolution.solution(1.0);
        PersonalBest pbest = new PersonalBest();

        F<Topology, Option<Entity>> partial = pbest.f(new Particle(memory, memory, Velocity.copyOf(1.0), Option.<Double>none()));
        Option<Entity> partialEntity = partial.f(ImmutableGBestTopology.of());

        Assert.assertThat(partialEntity.some().solution(), is(memory));
    }

    @Test
    public void memoryReturnedFromNonMemoryEntity() {
        CandidateSolution position = CandidateSolution.solution(1.0);
        PersonalBest pbest = new PersonalBest();
        F<Topology, Option<Entity>> partial = pbest.f(new Individual(position, Option.<Double>none()));
        Option<Entity> partialEntity = partial.f(ImmutableGBestTopology.of());

        Assert.assertThat(partialEntity.some().solution(), equalTo(CandidateSolution.solution(0.0)));
    }
}
