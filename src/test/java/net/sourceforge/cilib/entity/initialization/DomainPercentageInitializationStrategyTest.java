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
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

/**
 *
 * @author gpampara
 */
public class DomainPercentageInitializationStrategyTest {

    @Test
    public void initialize() {
        final Bounds bounds = new Bounds(-8.0, 8.0);
        Vector vector = Vector.newBuilder()
                .addWithin(0.0, bounds)
                .addWithin(0.0, bounds)
                .addWithin(0.0, bounds)
                .build();

        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.Particle.VELOCITY, vector);

        DomainPercentageInitializationStrategy<Particle> strategy = new DomainPercentageInitializationStrategy<Particle>();
        strategy.setVelocityInitialisationStrategy(new ConstantInitializationStrategy(1.0));
        strategy.initialize(EntityType.Particle.VELOCITY, particle);

        Vector velocity = (Vector) particle.getVelocity();
        for (int i = 0; i < velocity.size(); i++) {
            Assert.assertThat(velocity.doubleValueOf(i), is(lessThanOrEqualTo(0.1)));
        }
    }

    @Test
    public void defaultPercentage() {
        DomainPercentageInitializationStrategy strategy = new DomainPercentageInitializationStrategy();

        Assert.assertThat(strategy.getPercentage(), is(equalTo(0.1)));
    }

}