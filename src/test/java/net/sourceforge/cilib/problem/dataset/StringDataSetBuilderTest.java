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
package net.sourceforge.cilib.problem.dataset;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringDataSetBuilderTest {

    private static StringDataSetBuilder dataSetBuilder;

    @BeforeClass
    public static void intialise() {
        dataSetBuilder = new StringDataSetBuilder();
        dataSetBuilder.addDataSet(new MockStringDataSet());

        dataSetBuilder.initialise();
    }


    @AfterClass
    public static void destroy() {
        dataSetBuilder = null;
    }


    /**
     * Test the length of the longest and shortest strings. Using the given
     * dataSetBuilder, the lengths and the returned strings should be the same.
     */
    @Test
    public void testStringLength() {
        assertEquals(12, dataSetBuilder.getShortestString().length());
        assertEquals(12, dataSetBuilder.getLongestString().length());
        assertEquals(dataSetBuilder.getShortestString(), dataSetBuilder.getLongestString());
    }

}
