/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.TypeConversionOperator;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the functionality of the StandardPatternDataTable class.
 */
public class StandardPatternDataTableTest {

    private static StandardDataTable<Type> typedData;
    private StandardPatternDataTable vectorTargetPatterns;
    private StandardPatternDataTable stringTargetPatterns;

    /**
     * Constructs some test data we can use to test the class.
     * @throws CIlibIOException 
     */
    @BeforeClass
    public static void setupTestData() throws CIlibIOException {
        StandardDataTable<StringType> rawData = new StandardDataTable<StringType>();
        rawData.addRow(Arrays.asList(new StringType("0.1"), new StringType("0.2"),
                new StringType("0.3"), new StringType("0.4"), new StringType("0.5"), new StringType("class1")));
        rawData.addRow(Arrays.asList(new StringType("1.1"), new StringType("1.2"),
                new StringType("1.3"), new StringType("1.4"), new StringType("1.5"), new StringType("class2")));
        rawData.addRow(Arrays.asList(new StringType("2.1"), new StringType("2.2"),
                new StringType("2.3"), new StringType("2.4"), new StringType("2.5"),
                new StringType("1.0"), new StringType("1.0"), new StringType("0.0")));
        rawData.addRow(Arrays.asList(new StringType("3.1"), new StringType("3.2"),
                new StringType("3.3"), new StringType("3.4"), new StringType("3.5"),
                new StringType("0.0"), new StringType("0.0"), new StringType("1.0")));
        TypeConversionOperator operator = new TypeConversionOperator();
        typedData = (StandardDataTable<Type>) operator.operate(rawData);
    }

    /**
     * Before each test, make a new StandardPatternDataTable object and add in the test data.
     */
    @Before
    public void setRawData() {
        stringTargetPatterns = new StandardPatternDataTable();
        vectorTargetPatterns = new StandardPatternDataTable();
        Vector.Builder feature;
        Type classification;
        for (int i = 0; i < typedData.size() / 2; i++) {
            feature = Vector.newBuilder();
            List<Type> row = typedData.getRow(i);
            for (int j = 0; j < row.size() - 1; j++) {
                feature.add((Numeric)row.get(j));
            }
            classification = row.get(row.size() - 1).getClone();
            stringTargetPatterns.addRow(new StandardPattern(feature.build(), classification));
        }
        for (int i = typedData.size() / 2; i < typedData.size(); i++) {
            feature = Vector.newBuilder();
            Vector.Builder classificationB = Vector.newBuilder();
            List<Type> row = typedData.getRow(i);
            for (int j = 0; j < row.size() - 3; j++) {
                feature.add((Numeric)row.get(j));
            }
            for (int j = row.size() - 3; j < row.size(); j++) {
                classificationB.add((Numeric)row.get(j));
            }
            vectorTargetPatterns.addRow(new StandardPattern(feature.build(), classificationB.build()));
        }
    }

    @Test
    public void testClone() {
        StandardPatternDataTable copy = (StandardPatternDataTable) vectorTargetPatterns.getClone();
        Assert.assertEquals(vectorTargetPatterns.size(), copy.size());
        vectorTargetPatterns.clear();
        Assert.assertNotSame(vectorTargetPatterns.size(), copy.size());
    }

    @Test
    public void testAddRow() {
        Assert.assertEquals(typedData.size() / 2, stringTargetPatterns.size());
        Assert.assertEquals(typedData.size() / 2, vectorTargetPatterns.size());
    }

    @Test
    public void testRemoveRow() {
        StandardPattern removed = stringTargetPatterns.removeRow(0);
        List<Type> row = typedData.getRow(0);
        for (int j = 0; j < row.size() - 1; j++) {
            Assert.assertEquals(row.get(j), removed.getVector().get(j));
        }
        Assert.assertEquals(row.get(row.size() - 1), removed.getTarget());


        removed = vectorTargetPatterns.removeRow(1);
        row = typedData.getRow(3);
        for (int j = 0; j < row.size() - 3; j++) {
            Assert.assertEquals(row.get(j), removed.getVector().get(j));
        }
        for (int j = row.size() - 3; j < row.size(); j++) {
            Assert.assertEquals(row.get(j), ((Vector) removed.getTarget()).get(j - (row.size() - 3)));
        }

        Assert.assertEquals(1, stringTargetPatterns.size());
        Assert.assertEquals(1, vectorTargetPatterns.size());
    }

    /**
     * Test the get row method.
     */
    @Test
    public void testGetRow() {
        StandardPattern pattern = stringTargetPatterns.getRow(0);
        List<Type> row = typedData.getRow(0);
        for (int j = 0; j < row.size() - 1; j++) {
            Assert.assertEquals(row.get(j), pattern.getVector().get(j));
        }
        Assert.assertEquals(row.get(row.size() - 1), pattern.getTarget());


        pattern = vectorTargetPatterns.getRow(0);
        row = typedData.getRow(2);
        for (int j = 0; j < row.size() - 3; j++) {
            Assert.assertEquals(row.get(j), pattern.getVector().get(j));
        }
        for (int j = row.size() - 3; j < row.size(); j++) {
            Assert.assertEquals(row.get(j), ((Vector) pattern.getTarget()).get(j - (row.size() - 3)));
        }
    }

    /**
     * Test the set row method.
     */
    @Test
    public void testSetRow() {
        stringTargetPatterns.setRow(0, vectorTargetPatterns.getRow(0));
        Assert.assertEquals(stringTargetPatterns.getRow(0), vectorTargetPatterns.getRow(0));
    }

    /**
     * Test the get column method.
     */
    @Test
    public void testGetColumn() {
        TypeList expected = new TypeList();
        expected.add(Real.valueOf(0.2));
        expected.add(Real.valueOf(1.2));
        TypeList column = stringTargetPatterns.getColumn(1);
        Assert.assertEquals(expected, column);

        expected = new TypeList();
        Vector target = Vector.of(1,1,0);
        expected.add(target);
        target = Vector.of(0,0,1);
        expected.add(target);
        column = vectorTargetPatterns.getColumn(5);
        Assert.assertEquals(expected, column);
    }

    @Test
    public void testSetColumn() {
        TypeList newColumn = new TypeList();
        List<Real> list = Arrays.asList(Real.valueOf(5.2), Real.valueOf(6.2));
        for (Real real : list) {
            newColumn.add(real);
        }
        stringTargetPatterns.setColumn(0, newColumn);
        Assert.assertEquals(newColumn, stringTargetPatterns.getColumn(0));

        newColumn = new TypeList();
        Vector target = Vector.of(1,1,0);
        newColumn.add(target);
        target = Vector.of(0,0,1);
        newColumn.add(target);
        newColumn = vectorTargetPatterns.getColumn(5);
        Assert.assertEquals(newColumn, vectorTargetPatterns.getColumn(5));
    }

    @Test
    public void testNaming() {
        stringTargetPatterns.setColumnName(0, "age");
        Assert.assertEquals("age", stringTargetPatterns.getColumnName(0));
        Assert.assertEquals("", stringTargetPatterns.getColumnName(1));
    }
}
