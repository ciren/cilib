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
package net.sourceforge.cilib.container;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gpampara
 */
public class MultiDimensionalArrayTest {

    @Test
    public void creation() {
        MultiDimensionalArray<Double> mArray = new MultiDimensionalArray<Double>();
        assertEquals(0, mArray.size());
    }

    /**
     * This test is not that great... Creating an Array with 1 dimension - that
     * aditional index is not the best... Need to look at this.
     */
    @Test
    public void creationSingleDimension() {
        MultiDimensionalArray<Double> mArray = new MultiDimensionalArray<Double>(3);
        mArray.setElement(1.0, 0, 0);
        mArray.setElement(2.0, 0, 1);
        mArray.setElement(3.0, 0, 2);
    }

    @Test
    public void getAndSet() {
        MultiDimensionalArray<Double> mArray = new MultiDimensionalArray<Double>(2, 3, 4);
        Array a = new StandardArray();
        mArray.set(a, 1);

        assertSame(a, mArray.get(1));
    }

    @Test
    public void getAndSetElement() {
        MultiDimensionalArray<Double> mArray = new MultiDimensionalArray<Double>(2, 3, 4);
        mArray.setElement(3.0, 1, 2, 1);

        assertEquals(3.0, mArray.getElement(1, 2, 1), 0.0);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void invalidIndex() {
        MultiDimensionalArray<Double> mArray = new MultiDimensionalArray<Double>(2, 3, 4);
        mArray.get(2);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void invalidElementIndex() {
        MultiDimensionalArray<Double> mArray = new MultiDimensionalArray<Double>(2, 3, 4);
        mArray.getElement(0, 2, 4);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void invalidSet() {
        MultiDimensionalArray<Double> mArray = new MultiDimensionalArray<Double>(2, 3, 4);
        mArray.set(new StandardArray(), 2);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void invalidSetElement() {
        MultiDimensionalArray<Double> mArray = new MultiDimensionalArray<Double>(2, 3, 4);
        mArray.setElement(3.0, 1, 2, 4);
    }

}
