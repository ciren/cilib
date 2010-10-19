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

import net.cilib.entity.CandidateSolution;
import net.cilib.entity.Fitnesses;
import net.cilib.entity.PartialEntity;
import net.cilib.entity.Particle;
import net.cilib.entity.Velocity;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class StandardVelocityProviderTest {
    private Mockery context = new JUnit4Mockery();

    @Test
    public void velocityCalculation() {
        final Guide localGuide = context.mock(Guide.class, "localGuide");
        final Guide globalGuide = context.mock(Guide.class, "globalGuide");
        
        StandardVelocityProvider provider = new StandardVelocityProvider(localGuide, globalGuide);
        final Particle particle = new Particle(CandidateSolution.copyOf(1.0),
                CandidateSolution.copyOf(1.0),
                Velocity.copyOf(1.0),
                Fitnesses.newMaximizationFitness(1.0));

        final PartialEntity tempEntity = new PartialEntity(CandidateSolution.copyOf(0.0));
        context.checking(new Expectations() {{
            oneOf(localGuide).of(with(particle)); will(returnValue(tempEntity));
            oneOf(globalGuide).of(with(particle)); will(returnValue(tempEntity));
        }});

        Velocity newVelocity = provider.create(particle);
        Assert.assertArrayEquals(new double[]{-4.0}, newVelocity.toArray(), 0.0001);
    }
}