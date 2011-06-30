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
package net.cilib.matchers;

import fj.data.List;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.FitnessComparator;
import net.cilib.entity.Individual;
import org.junit.Assert;
import org.junit.Test;
import static fj.data.Option.some;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 */
public class EntityMatchersTest {

    @Test
    public void mostFitEntity() {
        Individual best = new Individual(List.<Double>nil(), some(4.0));
        Individual worst = new Individual(List.<Double>nil(), some(1.0));
        Topology<Individual> topology = ImmutableGBestTopology.<Individual>topologyOf(best, worst);

        Individual result = EntityMatchers.mostFit(topology, FitnessComparator.MAX);
        Assert.assertThat(result, is(best));
    }
}
