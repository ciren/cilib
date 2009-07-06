/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Real;
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
        Real r = new Real(-8.0, 8.0);
        Vector vector = new Vector();
        vector.add(r.getClone());
        vector.add(r.getClone());
        vector.add(r.getClone());

        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.Particle.VELOCITY, vector);

        DomainPercentageInitializationStrategy<Particle> strategy = new DomainPercentageInitializationStrategy<Particle>();
        strategy.setVelocityInitialisationStrategy(new ConstantInitializationStrategy(1.0));
        strategy.initialize(EntityType.Particle.VELOCITY, particle);

        Vector velocity = (Vector) particle.getVelocity();
        for (int i = 0; i < velocity.size(); i++) {
            Assert.assertThat(velocity.getReal(i), is(lessThanOrEqualTo(0.1)));
        }
    }

    @Test
    public void defaultPercentage() {
        DomainPercentageInitializationStrategy strategy = new DomainPercentageInitializationStrategy();

        Assert.assertThat(strategy.getPercentage(), is(equalTo(0.1)));
    }

}