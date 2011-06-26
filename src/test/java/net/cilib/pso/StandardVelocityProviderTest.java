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
import net.cilib.collection.immutable.CandidateSolution;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.collection.immutable.Velocity;
import net.cilib.entity.FitnessComparator;
import net.cilib.entity.Particle;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author gpampara
 */
public class StandardVelocityProviderTest {

    @Test
    public void velocityCalculation() {
        final Supplier<Double> constant = Suppliers.ofInstance(1.0);
        final StandardVelocityProvider provider = new StandardVelocityProvider(constant, constant, constant, constant,
                new PersonalBest(), new NeighborhoodBest(FitnessComparator.MIN));
        final Particle particle1 = new Particle(CandidateSolution.solution(1.0),
                CandidateSolution.solution(1.0),
                Velocity.copyOf(1.0),
                Option.some(1.0));
        final Particle particle2 = new Particle(CandidateSolution.solution(1.0),
                CandidateSolution.solution(1.0),
                Velocity.copyOf(1.0),
                Option.some(1.0));

        Velocity newVelocity = provider.f(particle1, ImmutableGBestTopology.topologyOf(particle1, particle2));
        Assert.assertArrayEquals(new double[]{1.0}, newVelocity.toArray(), 0.0001);
    }
}