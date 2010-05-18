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
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;
import static org.junit.Assert.*;
public class ContextEntityTest {
    @Test
    public void CopyFromTest(){
        ContextEntity testEntity = new ContextEntity();
        Vector context = new Vector();
        context.add(Real.valueOf(0));
        context.add(Real.valueOf(0));
        context.add(Real.valueOf(0));
        context.add(Real.valueOf(0));
        testEntity.setCandidateSolution(context);
        Vector data = new Vector();
        data.add(Real.valueOf(1));
        data.add(Real.valueOf(2));

        SequencialDimensionAllocation allocation = new SequencialDimensionAllocation(1, 2);

        testEntity.copyFrom(data, allocation);
        context = testEntity.getCandidateSolution();

        assertEquals(0.0, context.get(0).doubleValue(),0);
        assertEquals(1.0, context.get(1).doubleValue(),0);
        assertEquals(2.0, context.get(2).doubleValue(),0);
        assertEquals(0.0, context.get(3).doubleValue(),0);
    }
}
