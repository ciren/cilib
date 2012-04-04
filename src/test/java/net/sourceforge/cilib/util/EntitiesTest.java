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
package net.sourceforge.cilib.util;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class EntitiesTest {
    @Test
    public void testGetCandidateSolutions() {
        Vector v1 = Vector.of(1.0, 2.0, 3.0);
        Vector v2 = Vector.of(4.0, 5.0, 6.0);
        
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();
        
        p1.setCandidateSolution(v1);
        p2.setCandidateSolution(v2);
        
        List<Vector> list = Entities.<Vector>getCandidateSolutions(Arrays.asList(p1, p2));
        
        assertEquals(v1, list.get(0));
        assertEquals(v2, list.get(1));
    }
}
