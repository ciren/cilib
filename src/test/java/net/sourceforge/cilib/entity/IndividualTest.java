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
        Vector genes = new Vector(5);
        genes.add(new Real(1.0));
        genes.add(new Real(2.0));
        genes.add(new Real(3.0));
        genes.add(new Real(4.0));
        genes.add(new Real(5.0));

        Individual i = new Individual();
        i.setCandidateSolution(genes);

        Individual clone = i.getClone();

        assertEquals(5, i.getDimension());
        assertEquals(5, clone.getDimension());
        assertEquals(i.getDimension(), clone.getDimension());

        Vector cloneVector = (Vector) clone.getCandidateSolution();

        for (int k = 0; k < cloneVector.getDimension(); k++) {
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
