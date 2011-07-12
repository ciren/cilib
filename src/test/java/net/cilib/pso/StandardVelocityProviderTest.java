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
import fj.P;
import fj.P1;
import fj.data.Array;
import fj.data.List;
import fj.data.Option;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.FitnessComparator;
import net.cilib.entity.Particle;
import org.junit.Assert;
import org.junit.Test;

import static net.cilib.predef.Predef.solution;
import static net.cilib.predef.Predef.velocity;

/**
 * @author gpampara
 */
public class StandardVelocityProviderTest {

    @Test
    public void velocityCalculation() {
        final P1<Double> constant = P.p(5.0);
        final StandardVelocityProvider provider = new StandardVelocityProvider(
                P.p(1.0),
                constant, constant, constant, constant,
                new PersonalBest(), new NeighborhoodBest(FitnessComparator.MIN));
        final Particle particle1 = new Particle(solution(1.0),
                solution(1.0),
                velocity(1.0),
                Option.some(1.0));
        final Particle particle2 = new Particle(solution(1.0),
                solution(1.0),
                velocity(1.0),
                Option.some(1.0));

        List<Double> newVelocity = provider.f(particle1, ImmutableGBestTopology.topologyOf(particle1, particle2));

        Assert.assertTrue(Iterables.elementsEqual(Array.<Double>array(1.0), newVelocity));
    }
}
