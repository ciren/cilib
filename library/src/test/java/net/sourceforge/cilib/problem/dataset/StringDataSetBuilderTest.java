/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
