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
import com.google.common.collect.Iterables;
import net.cilib.entity.Particle;
import net.cilib.collection.TopologyBuffer;
import net.cilib.predef.Predicate;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author gpampara
 */
public class ImmutableLBestTopologyTest {

    private Predicate<Double> lookupF(final double value) {
        return new Predicate<Double>() {
            @Override
            public Boolean f(Double a) {
                return Double.compare(a, value) == 0;
            }
        };
    }

    @Test
    public void neighborhoodOf() {
        ImmutableLBestTopology<Double> lbest = new ImmutableLBestTopology.ImmutableLBestTopologyBuffer<Double>().add(4.0).add(3.0).add(2.0).add(1.0).add(0.0).build();

        Iterable<Double> neighborhood = lbest.neighborhoodOf(2.0); // Should contain 3.0, 2.0, and 1.0

        List<Double> neighbors = List.iterableList(neighborhood);
        Assert.assertThat(neighbors.length(), equalTo(3));
        Assert.assertTrue(neighbors.find(lookupF(3.0)).isSome());
        Assert.assertTrue(neighbors.find(lookupF(2.0)).isSome());
        Assert.assertTrue(neighbors.find(lookupF(1.0)).isSome());
    }

    @Test
    public void neighborhoodOfWrappingLower() {
        ImmutableLBestTopology<Double> lbest = new ImmutableLBestTopology.ImmutableLBestTopologyBuffer<Double>().add(4.0).add(3.0).add(2.0).add(1.0).add(0.0).build();

        Iterable<Double> neighborhood = lbest.neighborhoodOf(4.0); // Should contain 3.0, 2.0, and 1.0

        List<Double> neighbors = List.iterableList(neighborhood);

        Assert.assertThat(neighbors.length(), equalTo(3));
        Assert.assertTrue(neighbors.find(lookupF(3.0)).isSome());
        Assert.assertTrue(neighbors.find(lookupF(4.0)).isSome());
        Assert.assertTrue(neighbors.find(lookupF(0.0)).isSome());
    }

    @Test
    public void neighborhoodOfWrappingUpper() {
        ImmutableLBestTopology<Double> lbest = new ImmutableLBestTopology.ImmutableLBestTopologyBuffer<Double>().add(4.0).add(3.0).add(2.0).add(1.0).add(0.0).build();

        Iterable<Double> neighborhood = lbest.neighborhoodOf(0.0); // Should contain 3.0, 2.0, and 1.0

        List<Double> neighbors = List.iterableList(neighborhood);
        Assert.assertThat(neighbors.length(), equalTo(3));
        Assert.assertTrue(neighbors.find(lookupF(1.0)).isSome());
        Assert.assertTrue(neighbors.find(lookupF(0.0)).isSome());
        Assert.assertTrue(neighbors.find(lookupF(4.0)).isSome());
    }

    @Test
    public void bufferCreationContainsCurrentTopologyElements() {
        List<Double> nil = List.nil();
        Particle dummy = new Particle(nil, nil, nil, Option.<Double>none());
        ImmutableLBestTopology lbest = ImmutableLBestTopology.topologyOf(3, dummy, dummy, dummy, dummy);

        TopologyBuffer<Particle> buffer = lbest.newBuffer();

        Assert.assertThat(Iterables.size(buffer), equalTo(0));
    }
}
