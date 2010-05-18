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
package net.sourceforge.cilib.io;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringDataTableTest {
    
    private static StringDataTable stringDataTable;
    
    @BeforeClass
    public static void intialise() {
        stringDataTable = new StringDataTable();
        stringDataTable.addRow("CGTGGTAATCAC");
        stringDataTable.addRow("AGCAGTGTATCC");
        stringDataTable.addRow("CGGGCTATGCCG");
    }
    
    
    @AfterClass
    public static void destroy() {
        stringDataTable = null;
    }
    
    
    /**
     * Test the length of the longest and shortest strings. Using the given
     * dataSetBuilder, the lengths and the returned strings should be the same.
     */
    @Test
    public void testStringLength() {
        assertEquals(12, stringDataTable.getShortestString().length());
        assertEquals(12, stringDataTable.getLongestString().length());
        assertEquals(stringDataTable.getShortestString(), stringDataTable.getLongestString());
    }

}
