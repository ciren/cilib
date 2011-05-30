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

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author gpampara
 */
public class ImmutableLBestTopologyTest {

    @Test
    public void neighborhoodOf() {
        ImmutableLBestTopology<Double> lbest = new ImmutableLBestTopology.ImmutableLBestTopologyBuilder<Double>()
                .add(4.0).add(3.0).add(2.0).add(1.0).add(0.0).build();

        Iterable<Double> neighborhood = lbest.neighborhoodOf(2.0); // Should contain 3.0, 2.0, and 1.0

        List<Double> neighbors = Lists.newArrayList(neighborhood);
        Assert.assertThat(neighbors.size(), equalTo(3));
        Assert.assertTrue(neighbors.contains(3.0));
        Assert.assertTrue(neighbors.contains(2.0));
        Assert.assertTrue(neighbors.contains(1.0));
    }

    @Test
    public void neighborhoodOfWrappingLower() {
        ImmutableLBestTopology<Double> lbest = new ImmutableLBestTopology.ImmutableLBestTopologyBuilder<Double>()
                .add(4.0).add(3.0).add(2.0).add(1.0).add(0.0).build();

        Iterable<Double> neighborhood = lbest.neighborhoodOf(4.0); // Should contain 3.0, 2.0, and 1.0

        List<Double> neighbors = Lists.newArrayList(neighborhood);
        Assert.assertThat(neighbors.size(), equalTo(3));
        Assert.assertTrue(neighbors.contains(3.0));
        Assert.assertTrue(neighbors.contains(4.0));
        Assert.assertTrue(neighbors.contains(0.0));
    }

    @Test
    public void neighborhoodOfWrappingUpper() {
        ImmutableLBestTopology<Double> lbest = new ImmutableLBestTopology.ImmutableLBestTopologyBuilder<Double>()
                .add(4.0).add(3.0).add(2.0).add(1.0).add(0.0).build();

        Iterable<Double> neighborhood = lbest.neighborhoodOf(0.0); // Should contain 3.0, 2.0, and 1.0

        List<Double> neighbors = Lists.newArrayList(neighborhood);
        Assert.assertThat(neighbors.size(), equalTo(3));
        Assert.assertTrue(neighbors.contains(1.0));
        Assert.assertTrue(neighbors.contains(0.0));
        Assert.assertTrue(neighbors.contains(4.0));
    }
}
