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

import com.google.common.collect.Iterables;
import fj.F;
import fj.data.List;
import fj.data.Option;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.Entity;
import net.cilib.entity.Individual;
import net.cilib.entity.Particle;
import org.junit.Assert;
import org.junit.Test;
import static net.cilib.predef.Predef.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 */
public class PersonalBestTest {

    @Test
    public void memoryReturnedFromMemoryEntity() {
        List<Double> memory = List.list(1.0);
        PersonalBest pbest = new PersonalBest();

        F<Topology, Entity> partial = pbest.f(new Particle(memory, memory, List.<Double>list(1.0), Option.<Double>none()));
        Entity partialEntity = partial.f(ImmutableGBestTopology.of());

        Assert.assertThat(partialEntity.solution(), is(memory));
    }

    @Test
    public void memoryReturnedFromNonMemoryEntity() {
        List<Double> position = List.list(1.0);
        PersonalBest pbest = new PersonalBest();
        F<Topology, Entity> partial = pbest.f(new Individual(position, Option.<Double>none()));
        Entity partialEntity = partial.f(ImmutableGBestTopology.of());

        Assert.assertTrue(Iterables.elementsEqual(partialEntity.solution(), solution(0.0)));
    }
}
