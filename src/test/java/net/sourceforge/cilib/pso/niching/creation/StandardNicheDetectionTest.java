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
package net.sourceforge.cilib.pso.niching.creation;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.niching.NicheTest;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;

/**
 *
 * @author filipe
 */
public class StandardNicheDetectionTest {
    
    @Test
    public void testTrueDetection() {
        Particle p1 = NicheTest.createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 1.0));
    }

}
