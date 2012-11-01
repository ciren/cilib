/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.type.types.StringType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the functionality of the StandardDataTable class.
 */
public class StandardDataTableTest {
    private static List<List<StringType>> testData;
    private StandardDataTable<StringType> rawData;

    /**
     * Constructs some test data we can use to test the class.
     */
    @BeforeClass
    public static void setupTestData() {
        testData = new ArrayList<List<StringType>>();
        List<StringType> testDataRow = new ArrayList<StringType>();
        testDataRow.addAll(Arrays.asList(new StringType("0.1"),new StringType("0.2"),
                new StringType("0.3"),new StringType("0.4"),new StringType("0.5"),new StringType("class1")));
        testData.add(testDataRow);
        testDataRow = new ArrayList<StringType>();
        testDataRow.addAll(Arrays.asList(new StringType("1.1"),new StringType("1.2"),
                new StringType("1.3"),new StringType("1.4"),new StringType("1.5"),new StringType("class2")));
        testData.add(testDataRow);
        testDataRow = new ArrayList<StringType>();
        testDataRow.addAll(Arrays.asList(new StringType("2.1"),new StringType("2.2"),
                new StringType("2.3"),new StringType("2.4"),new StringType("2.5"),new StringType("class3")));
        testData.add(testDataRow);
    }

    /**
     * Before each test, make a new StandardDataTable object and add in the test data.
     */
    @Before
    public void setRawData() {
        rawData = new StandardDataTable<StringType>();
        rawData.addRow(testData.get(0));
        rawData.addRow(testData.get(1));
        rawData.addRow(testData.get(2));
    }

    @Test
    public void testClone() {
        StandardDataTable<StringType> copy = (StandardDataTable<StringType>) rawData.getClone();
        Assert.assertEquals(3, copy.size());
        copy.getRow(0).set(0, new StringType("99"));
        Assert.assertNotSame(copy.getRow(0).get(0), rawData.getRow(0).get(0));
        Assert.assertEquals(rawData.getColumnNames(), copy.getColumnNames());
        rawData.clear();
        Assert.assertEquals(3, copy.size());
    }

    /**
     * Implicitely tests the addRow method (which was called before the test).
     */
    @Test
    public void testAddRow() {
        Assert.assertEquals(3, rawData.size());
    }

    @Test
    public void testRemoveRow() {
        List<StringType> removed = rawData.removeRow(0);
        Assert.assertEquals(testData.get(0), removed);

        removed = rawData.removeRow(1);
        Assert.assertEquals(testData.get(2), removed);

        Assert.assertEquals(1, rawData.size());
    }

    /**
     * Test the get row method.
     */
    @Test
    public void testGetRow() {
        Assert.assertEquals(testData.get(1), rawData.getRow(1));
    }

    /**
     * Test the set row method.
     */
    @Test
    public void testSetRow() {
        rawData.setRow(2, testData.get(0));
        Assert.assertEquals(testData.get(0), rawData.getRow(2));
        //make sure only rawData was changed and not the references in testData.
        Assert.assertNotSame(testData.get(0), testData.get(2));
    }

    /**
     * Test the get column method.
     */
    @Test
    public void testGetColumn() {
        List<StringType> expected = new ArrayList<StringType>();
        expected.addAll(Arrays.asList(new StringType("0.2"),new StringType("1.2"),new StringType("2.2")));
        Assert.assertEquals(expected, rawData.getColumn(1));
    }

    @Test
    public void addColumn() {
        rawData = new StandardDataTable<StringType>();
        List<StringType> expected = new ArrayList<StringType>();
        expected.addAll(Arrays.asList(new StringType("0.1"),new StringType("1.1"),new StringType("2.1")));
        rawData.addColumn(expected);
        Assert.assertEquals(expected, rawData.getColumn(0));

        expected.clear();
        expected.addAll(Arrays.asList(new StringType("0.2"),new StringType("1.2"),new StringType("2.2")));
        rawData.addColumn(expected);
        Assert.assertEquals(expected, rawData.getColumn(1));
    }

    @Test
    public void testNaming() {
        rawData.setColumnName(0, "age");
        Assert.assertEquals("age", rawData.getColumnName(0));
        Assert.assertEquals("", rawData.getColumnName(1));
    }

}
