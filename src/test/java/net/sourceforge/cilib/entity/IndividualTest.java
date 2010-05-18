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
package net.sourceforge.cilib.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;

public class IndividualTest {

    @Test
    public void testClone() {
        Vector genes = new Vector();
        genes.add(Real.valueOf(1.0));
        genes.add(Real.valueOf(2.0));
        genes.add(Real.valueOf(3.0));
        genes.add(Real.valueOf(4.0));
        genes.add(Real.valueOf(5.0));

        Individual i = new Individual();
        i.setCandidateSolution(genes);

        Individual clone = i.getClone();

        assertEquals(5, i.getDimension());
        assertEquals(5, clone.getDimension());
        assertEquals(i.getDimension(), clone.getDimension());

        Vector cloneVector = (Vector) clone.getCandidateSolution();

        for (int k = 0; k < cloneVector.size(); k++) {
            assertEquals(genes.get(k), cloneVector.get(k));
        }
    }

    @Test
    public void equals() {
        Individual i1 = new Individual();
        Individual i2 = new Individual();

        assertFalse(i1.equals(i2));
        assertFalse(i2.equals(i1));
        assertTrue(i1.equals(i1));

        assertFalse(i1.equals(null));
    }

    @Test
    public void hashCodes() {
        Individual i1 = new Individual();
        Individual i2 = new Individual();

        assertTrue(i1.hashCode() != i2.hashCode());
    }

}
