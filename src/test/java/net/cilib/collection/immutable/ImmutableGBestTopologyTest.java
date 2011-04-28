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
package net.cilib.collection.immutable;

import com.google.common.collect.Iterables;
import net.cilib.collection.Topology;
import net.cilib.entity.Particle;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author gpampara
 */
public class ImmutableGBestTopologyTest {

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorIsUnmodifiable() {
        ImmutableGBestTopology<Particle> topology = ImmutableGBestTopology.of();

        Iterator<Particle> iterator = topology.iterator();
        iterator.remove();
    }

    @Test
    public void iteratorOfTopology() {
        Topology<Double> t = new ImmutableGBestTopology.ImmutableGBestTopologyBuilder<Double>()
                .add(3.0).add(4.0).build();

        Assert.assertThat(Iterables.size(t), equalTo(2));
    }

    @Test
    public void gBestNeighbourhood() {
        Topology<Double> t = new ImmutableGBestTopology.ImmutableGBestTopologyBuilder<Double>()
                .add(3.0).add(4.0).build();

        Assert.assertThat(Iterables.size(t), equalTo(2));
        Assert.assertArrayEquals(new Double[]{3.0, 4.0}, Iterables.toArray(t, Double.class));
    }
}
