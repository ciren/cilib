/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
