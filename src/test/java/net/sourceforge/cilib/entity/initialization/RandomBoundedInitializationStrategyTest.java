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

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

/**
 *
 */
public class RandomBoundedInitializationStrategyTest {

    @Test
    public void initialize() {
        Vector vector = new Vector();
        vector.add(new Real(5.0));
        vector.add(new Real(10.0));
        vector.add(new Int(7));

        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, vector);

        RandomBoundedInitializationStrategy<Particle> strategy = new RandomBoundedInitializationStrategy<Particle>();
        strategy.setLowerBound(new ConstantControlParameter(-5.0));
        strategy.setUpperBound(new ConstantControlParameter(5.0));
        strategy.initialize(EntityType.CANDIDATE_SOLUTION, particle);

        for (int i = 0; i < vector.size(); i++) {
            Numeric numeric = vector.get(i);
            Assert.assertThat(numeric.getReal(), is(greaterThanOrEqualTo(-5.0)));
            Assert.assertThat(numeric.getReal(), is(lessThanOrEqualTo(5.0)));
        }
    }

}