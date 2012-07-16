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
package net.sourceforge.cilib.coevolution.cooperative;

import net.sourceforge.cilib.coevolution.cooperative.problem.SequencialDimensionAllocation;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ContextEntityTest {
    @Test
    public void CopyFromTest(){
        ContextEntity testEntity = new ContextEntity();
        Vector context = Vector.of(0.0, 0.0, 0.0, 0.0);
        testEntity.setCandidateSolution(context);
        Vector data = Vector.of(1, 2);

        SequencialDimensionAllocation allocation = new SequencialDimensionAllocation(1, 2);

        testEntity.copyFrom(data, allocation);
        context = testEntity.getCandidateSolution();

        assertEquals(0.0, context.get(0).doubleValue(),0);
        assertEquals(1.0, context.get(1).doubleValue(),0);
        assertEquals(2.0, context.get(2).doubleValue(),0);
        assertEquals(0.0, context.get(3).doubleValue(),0);
    }
}
