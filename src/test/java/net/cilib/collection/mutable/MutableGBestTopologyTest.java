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
package net.cilib.collection.mutable;

import com.google.common.collect.Iterables;
import net.cilib.entity.Individual;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class MutableGBestTopologyTest {

    @Test
    public void addingToTopology() {
        MutableGBestTopology<Individual> t = new MutableGBestTopology<Individual>();
        t.add(new Individual(null, null));

        Assert.assertEquals(1, Iterables.size(t));
    }
}
