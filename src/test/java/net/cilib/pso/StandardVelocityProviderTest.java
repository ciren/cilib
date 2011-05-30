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

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import fj.data.Option;
import net.cilib.entity.CandidateSolution;
import net.cilib.entity.PartialEntity;
import net.cilib.entity.Particle;
import net.cilib.entity.Velocity;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author gpampara
 */
public class StandardVelocityProviderTest {

    @Test
    public void velocityCalculation() {
        final PartialEntity tempEntity = new PartialEntity(CandidateSolution.of(0.0));
        final Supplier<Double> constant = Suppliers.ofInstance(1.0);
        final StandardVelocityProvider provider = new StandardVelocityProvider(constant, constant, constant, constant);
        final Particle particle = new Particle(CandidateSolution.of(1.0),
                CandidateSolution.of(1.0),
                Velocity.copyOf(1.0),
                Option.some(1.0));

        Velocity newVelocity = provider.create(particle, tempEntity, tempEntity);
        Assert.assertArrayEquals(new double[]{-1.0}, newVelocity.toArray(), 0.0001);
    }
}