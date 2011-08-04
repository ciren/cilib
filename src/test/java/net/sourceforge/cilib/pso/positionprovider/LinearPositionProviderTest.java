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
package net.sourceforge.cilib.pso.positionprovider;

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.entity.Particle;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author filipe
 */
public class LinearPositionProviderTest {
    @Test
    public void testGet() {
        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION,
                    Vector.of(Real.valueOf(1.0, new Bounds(-5.0, 5.0)),
                              Real.valueOf(2.0, new Bounds(-5.0, 5.0))));
        
        particle.getProperties().put(EntityType.Particle.VELOCITY,
                    Vector.of(Real.valueOf(0.0, new Bounds(-10.0, 10.0)),
                              Real.valueOf(0.0, new Bounds(-10.0, 10.0))));
        
        Vector updatedVector = new LinearPositionProvider().get(particle);
        
        //must have velocity's vector components and position's bounds
        assertEquals(updatedVector, Vector.of(Real.valueOf(0.0, new Bounds(-5.0, 5.0)),
                                              Real.valueOf(0.0, new Bounds(-5.0, 5.0))));
    }
}
