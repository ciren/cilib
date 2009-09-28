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
        Vector context = new Vector(4);
        context.add(new Real(0));
        context.add(new Real(0));
        context.add(new Real(0));
        context.add(new Real(0));
        testEntity.setCandidateSolution(context);
        Vector data = new Vector(2);
        data.add(new Real(1));
        data.add(new Real(2));

        SequencialDimensionAllocation allocation = new SequencialDimensionAllocation(1, 2);

        testEntity.copyFrom(data, allocation);
        context = testEntity.getCandidateSolution();

        assertEquals(0.0, context.get(0).getReal(),0);
        assertEquals(1.0, context.get(1).getReal(),0);
        assertEquals(2.0, context.get(2).getReal(),0);
        assertEquals(0.0, context.get(3).getReal(),0);
    }
}
