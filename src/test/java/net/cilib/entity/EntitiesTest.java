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

import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import org.junit.Assert;
import fj.data.List;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class EntitiesTest {

    @Test
    public void particleGen() {
        RandomProvider random = new MersenneTwister();
        Supplier<Particle> generator = Entities.particleGen(30, random);

        Particle p1 = generator.get();
        Particle p2 = generator.get();

        Assert.assertNotSame(p1, p2);

        Assert.assertEquals(30, p1.size());
        Assert.assertEquals(p1.solution(), p1.memory());
        Assert.assertTrue(p1.fitness().isNone());
        Assert.assertTrue(Iterables.elementsEqual(p1.velocity(), List.<Double>replicate(30, 0.0)));
    }
}
