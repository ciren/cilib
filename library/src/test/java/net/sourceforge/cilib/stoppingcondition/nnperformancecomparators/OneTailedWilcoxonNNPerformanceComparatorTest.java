/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.stoppingcondition.nnperformancecomparators;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 These tests are mostly arbitrary. The expected output values were determined
 from the output values produced by R.
 */
public class OneTailedWilcoxonNNPerformanceComparatorTest {

    @Test
    public void testIsSame() {
        OneTailedWilcoxonNNPerformanceComparator test = new OneTailedWilcoxonNNPerformanceComparator();
        test.setConfLevel(0.7);

        StandardPatternDataTable table = new StandardPatternDataTable();
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(2.0, 1.5)));
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(4.0, 7.0)));
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(6.0, 1.0)));
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(8.0, 1.9)));
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(10.0, 8.4)));
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(12.0, 10.0)));

        ArrayList<Vector> x = new ArrayList<Vector>();  //0.125,2.5,2,24.505,47.645,58.12
        x.add(Vector.of(2.0, 1.0));	//zero
        x.add(Vector.of(2.0, 6.0));	//tie
        x.add(Vector.of(8.0, 1.0));	//tie
        x.add(Vector.of(3.0, 6.8));
        x.add(Vector.of(16.0, 0.7));
        x.add(Vector.of(5.0, 1.8));

        ArrayList<Vector> y = new ArrayList<Vector>();  //0.125,2,2.5,37.645,32.045,183.38
        y.add(Vector.of(2.0, 1.0));	//zero
        y.add(Vector.of(6.0, 7.0));	//tie
        y.add(Vector.of(4.0, 2.0));	//tie
        y.add(Vector.of(4.0, 9.6));
        y.add(Vector.of(2.0, 8.7));
        y.add(Vector.of(-7.0, 7.6));

        assertFalse(test.isSame(table, y, x));
        assertTrue(test.isSame(table, x, y));

        StandardPatternDataTable tableZ = new StandardPatternDataTable();
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));

        ArrayList<Vector> x1 = new ArrayList<Vector>(); //1,4,9,16,25,36
        x1.add(Vector.of(1.0));
        x1.add(Vector.of(2.0));
        x1.add(Vector.of(3.0));
        x1.add(Vector.of(4.0));
        x1.add(Vector.of(5.0));
        x1.add(Vector.of(6.0));

        ArrayList<Vector> y1 = new ArrayList<Vector>(); //36,25,16,9,4,1
        y1.add(Vector.of(6.0));
        y1.add(Vector.of(5.0));
        y1.add(Vector.of(4.0));
        y1.add(Vector.of(3.0));
        y1.add(Vector.of(2.0));
        y1.add(Vector.of(1.0));

        assertTrue(test.isSame(tableZ, y1, x1));
        assertTrue(test.isSame(tableZ, x1, y1));
        //case where all differences are zero
        assertTrue(test.isSame(tableZ, x1, x1));

        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));

        ArrayList<Vector> x2 = new ArrayList<Vector>(); //1,1,1,1,1,1,1,1,1
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));

        ArrayList<Vector> y2 = new ArrayList<Vector>(); //4,0,0,0,0,0,0,0,0
        y2.add(Vector.of(2.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));

        assertTrue(test.isSame(tableZ, y2, x2));
        assertFalse(test.isSame(tableZ, x2, y2));
    }

    @Test
    public void testPValue() {
        StandardPatternDataTable table = new StandardPatternDataTable();
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(2.0, 1.5)));
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(4.0, 7.0)));
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(6.0, 1.0)));
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(8.0, 1.9)));
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(10.0, 8.4)));
        table.addRow(new StandardPattern(Vector.of(0.0), Vector.of(12.0, 10.0)));

        ArrayList<Vector> x = new ArrayList<Vector>();  //0.125,2.5,2,24.505,47.645,58.12
        x.add(Vector.of(2.0, 1.0));	//zero
        x.add(Vector.of(2.0, 6.0));	//tie
        x.add(Vector.of(8.0, 1.0));	//tie
        x.add(Vector.of(3.0, 6.8));
        x.add(Vector.of(16.0, 0.7));
        x.add(Vector.of(5.0, 1.8));

        ArrayList<Vector> y = new ArrayList<Vector>();  //0.125,2,2.5,37.645,32.045,183.38
        y.add(Vector.of(2.0, 1.0));	//zero
        y.add(Vector.of(6.0, 7.0));	//tie
        y.add(Vector.of(4.0, 2.0));	//tie
        y.add(Vector.of(4.0, 9.6));
        y.add(Vector.of(2.0, 8.7));
        y.add(Vector.of(-7.0, 7.6));

        assertEquals(0.2940, OneTailedWilcoxonNNPerformanceComparator.pvalue(table, y, x), 0.0001);
        assertEquals(0.706, OneTailedWilcoxonNNPerformanceComparator.pvalue(table, x, y), 0.001);

        StandardPatternDataTable tableZ = new StandardPatternDataTable();
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));

        ArrayList<Vector> x1 = new ArrayList<Vector>(); //1,4,9,16,25,36
        x1.add(Vector.of(1.0));
        x1.add(Vector.of(2.0));
        x1.add(Vector.of(3.0));
        x1.add(Vector.of(4.0));
        x1.add(Vector.of(5.0));
        x1.add(Vector.of(6.0));

        ArrayList<Vector> y1 = new ArrayList<Vector>(); //36,25,16,9,4,1
        y1.add(Vector.of(6.0));
        y1.add(Vector.of(5.0));
        y1.add(Vector.of(4.0));
        y1.add(Vector.of(3.0));
        y1.add(Vector.of(2.0));
        y1.add(Vector.of(1.0));

        assertEquals(0.5, OneTailedWilcoxonNNPerformanceComparator.pvalue(tableZ, y1, x1), 0.0001);
        assertEquals(0.5, OneTailedWilcoxonNNPerformanceComparator.pvalue(tableZ, x1, y1), 0.0001);
        //case where all differences are zero
        assertEquals(0.5, OneTailedWilcoxonNNPerformanceComparator.pvalue(tableZ, x1, x1), 0.0001);

        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));
        tableZ.addRow(new StandardPattern(Vector.of(0.0), Vector.of(0.0)));

        ArrayList<Vector> x2 = new ArrayList<Vector>(); //1,1,1,1,1,1,1,1,1
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));
        x2.add(Vector.of(1.0));

        ArrayList<Vector> y2 = new ArrayList<Vector>(); //4,0,0,0,0,0,0,0,0
        y2.add(Vector.of(2.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));
        y2.add(Vector.of(0.0));

        assertEquals(0.9584, OneTailedWilcoxonNNPerformanceComparator.pvalue(tableZ, y2, x2), 0.0001);
        assertEquals(0.04163, OneTailedWilcoxonNNPerformanceComparator.pvalue(tableZ, x2, y2), 0.00001);
    }

    @Test
    public void testPNorm() {
        assertEquals(0.0, OneTailedWilcoxonNNPerformanceComparator.pnorm(-38), Maths.EPSILON);
        assertEquals(5.725571e-300, OneTailedWilcoxonNNPerformanceComparator.pnorm(-37), 1.0e-306);
        assertEquals(9.865876e-10, OneTailedWilcoxonNNPerformanceComparator.pnorm(-6), 1.0e-16);
        assertEquals(2.866516e-07, OneTailedWilcoxonNNPerformanceComparator.pnorm(-5), 1.0e-13);
        assertEquals(3.167124e-05, OneTailedWilcoxonNNPerformanceComparator.pnorm(-4), 1.0e-11);
        assertEquals(0.001349898, OneTailedWilcoxonNNPerformanceComparator.pnorm(-3), 0.00000001);
        assertEquals(0.02275013, OneTailedWilcoxonNNPerformanceComparator.pnorm(-2), 0.0000001);
        assertEquals(0.1586553, OneTailedWilcoxonNNPerformanceComparator.pnorm(-1), 0.000001);
        assertEquals(0.3085375, OneTailedWilcoxonNNPerformanceComparator.pnorm(-0.5), 0.000001);
        assertEquals(0.4601722, OneTailedWilcoxonNNPerformanceComparator.pnorm(-0.1), 0.000001);
        assertEquals(0.5, OneTailedWilcoxonNNPerformanceComparator.pnorm(0), 0.000001);
        assertEquals(0.5398278, OneTailedWilcoxonNNPerformanceComparator.pnorm(0.1), 0.000001);
        assertEquals(0.6914625, OneTailedWilcoxonNNPerformanceComparator.pnorm(0.5), 0.000001);
        assertEquals(0.8413447, OneTailedWilcoxonNNPerformanceComparator.pnorm(1), 0.000001);
        assertEquals(0.9772499, OneTailedWilcoxonNNPerformanceComparator.pnorm(2), 0.000001);
        assertEquals(0.9986501, OneTailedWilcoxonNNPerformanceComparator.pnorm(3), 0.000001);
        assertEquals(0.9999683, OneTailedWilcoxonNNPerformanceComparator.pnorm(4), 0.000001);
        assertEquals(0.9999997, OneTailedWilcoxonNNPerformanceComparator.pnorm(5), 0.000001);
        assertEquals(1, OneTailedWilcoxonNNPerformanceComparator.pnorm(6), 0.000001);
        assertEquals(1, OneTailedWilcoxonNNPerformanceComparator.pnorm(9), 0.000001);
    }
}
