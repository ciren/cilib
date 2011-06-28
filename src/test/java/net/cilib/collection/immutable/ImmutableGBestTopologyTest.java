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

import fj.data.List;
import fj.data.Option;
import net.cilib.collection.TopologyBuffer;
import com.google.common.collect.Iterables;
import fj.F;
import net.cilib.collection.Topology;
import net.cilib.entity.Particle;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

/**
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
        Topology<Double> t = ImmutableGBestTopology.topologyOf(3.0, 4.0);

        Assert.assertThat(Iterables.size(t), equalTo(2));
    }

    @Test
    public void gBestNeighbourhood() {
        Topology<Double> t = ImmutableGBestTopology.topologyOf(3.0, 4.0);

        Assert.assertThat(Iterables.size(t), equalTo(2));
        Assert.assertArrayEquals(new Double[]{3.0, 4.0}, Iterables.toArray(t, Double.class));
    }

    @Test
    public void foldLeft() {
        Topology<Integer> t = ImmutableGBestTopology.topologyOf(1, 2, 3, 4, 5);
        Integer result = t.foldLeft(new F<Integer, F<Integer, Integer>>() {

            @Override
            public F<Integer, Integer> f(final Integer a1) {
                return new F<Integer, Integer>() {

                    @Override
                    public Integer f(final Integer a2) {
                        return a1 + a2;
                    }
                };
            }
        }, 0);

        Assert.assertThat(result, equalTo(15));
    }

    @Test
    public void bufferCreationContainsCurrentTopologyElements() {
        List<Double> nil = List.nil();
        Particle dummy = new Particle(nil, nil, nil, Option.<Double>none());
        ImmutableGBestTopology lbest = ImmutableGBestTopology.topologyOf(dummy, dummy, dummy, dummy);

        TopologyBuffer<Particle> buffer = lbest.newBuffer();

        Assert.assertThat(Iterables.size(buffer), equalTo(0));
    }
}
