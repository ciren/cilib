/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering;

import junit.framework.Assert;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.DelimitedTextFileReader;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.PatternConversionOperator;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;

public class SlidingWindowTest {
    
    /**
     * Test of slideWindow method, of class SlidingWindow.
     */
    @Test
    public void testSlideWindow() {
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(1);
        window.setSlideFrequency(1);
        window.initialiseWindow();
        
        Vector beforeSlide =  ((StandardPattern) window.getCurrentDataset().getRow(0)).getVector();
        Vector expectedBeforeSlide = Vector.of(1.0,1.0,1.0,2.0);
        
        Assert.assertTrue(beforeSlide.containsAll(expectedBeforeSlide));
        
        window.slideWindow();
        
        Vector afterSlide =  ((StandardPattern) window.getCurrentDataset().getRow(0)).getVector();
        Vector expectedAfterSlide = Vector.of(2.0,3.0,4.0,2.0);
        
        Assert.assertTrue(afterSlide.containsAll(expectedAfterSlide));
    }

    /**
     * Test of initialiseWindow method, of class SlidingWindow.
     */
    @Test
    public void testInitialiseWindow() {
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(2);
        window.initialiseWindow();
        
        int totalPatternsInWindow = window.getCurrentDataset().size();
        Vector firstValue =  ((StandardPattern) window.getCurrentDataset().getRow(0)).getVector();
        Vector secondValue =  ((StandardPattern) window.getCurrentDataset().getRow(1)).getVector();
        
        Vector expectedValue1 = Vector.of(1.0,1.0,1.0,2.0);
        Vector expectedValue2 = Vector.of(2.0,3.0,4.0,2.0);
        
        Assert.assertEquals(2, totalPatternsInWindow);
        Assert.assertTrue(firstValue.containsAll(expectedValue1));
        Assert.assertTrue(secondValue.containsAll(expectedValue2));
    }

    /**
     * Test of setWindowSize method, of class SlidingWindow.
     */
    @Test
    public void testSetWindowSize() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(2);
        Assert.assertEquals(2, window.getWindowSize());
    }

    /**
     * Test of getWindowSize method, of class SlidingWindow.
     */
    @Test
    public void testGetWindowSize() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(2);
        Assert.assertEquals(2, window.getWindowSize());
    }

    /**
     * Test of getDataTableBuilder method, of class SlidingWindow.
     */
    @Test
    public void testGetDataTableBuilder() {
        SlidingWindow window = new SlidingWindow();
        window.setDataTableBuilder(new DataTableBuilder(new DelimitedTextFileReader()));
        Assert.assertTrue(window.getDataTableBuilder().getDataReader() instanceof DelimitedTextFileReader);
    }

    /**
     * Test of setDataTableBuilder method, of class SlidingWindow.
     */
    @Test
    public void testSetDataTableBuilder() {
        SlidingWindow window = new SlidingWindow();
        window.setDataTableBuilder(new DataTableBuilder(new DelimitedTextFileReader()));
        Assert.assertTrue(window.getDataTableBuilder().getDataReader() instanceof DelimitedTextFileReader);
    }

    /**
     * Test of getSourceURL method, of class SlidingWindow.
     */
    @Test
    public void testGetSourceURL() {
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets");
        Assert.assertTrue(window.getSourceURL().contains("library/src/test/resources/datasets") || 
                window.getSourceURL().contains("library\\src\\test\\resources\\datasets"));
    }

    /**
     * Test of setSourceURL method, of class SlidingWindow.
     */
    @Test
    public void testSetSourceURL() {
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets");
        Assert.assertTrue(window.getSourceURL().contains("library/src/test/resources/datasets") || 
                window.getSourceURL().contains("library\\src\\test\\resources\\datasets"));
    }

    /**
     * Test of getPatternConversionOperator method, of class SlidingWindow.
     */
    @Test
    public void testGetPatternConversionOperator() {
        SlidingWindow window = new SlidingWindow();
        PatternConversionOperator operator = new PatternConversionOperator();
        window.setPatternConversionOperator(operator);
        Assert.assertSame(operator, window.getPatternConversionOperator());
    }

    /**
     * Test of setPatternConversionOperator method, of class SlidingWindow.
     */
    @Test
    public void testSetPatternConversionOperator() {
        SlidingWindow window = new SlidingWindow();
        PatternConversionOperator operator = new PatternConversionOperator();
        window.setPatternConversionOperator(operator);
        Assert.assertSame(operator, window.getPatternConversionOperator());
    }

    /**
     * Test of getCurrentDataset method, of class SlidingWindow.
     */
    @Test
    public void testGetCurrentDataset() {
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(1);
        window.initialiseWindow();
        
        int totalPatterns = window.getCurrentDataset().size();
        Vector result =  ((StandardPattern) window.getCurrentDataset().getRow(0)).getVector();
        Vector expected = Vector.of(1.0,1.0,1.0,2.0);
        
        Assert.assertTrue(result.containsAll(expected));
        Assert.assertEquals(1, totalPatterns);
    }

    /**
     * Test of getCompleteDataset method, of class SlidingWindow.
     */
    @Test
    public void testGetCompleteDataset() {
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(1);
        window.initialiseWindow();
        
        int totalPatterns = window.getCurrentDataset().size();
        Vector result =  ((StandardPattern) window.getCurrentDataset().getRow(0)).getVector();
        Vector expected = Vector.of(1.0,1.0,1.0,2.0);
        
        Assert.assertTrue(result.containsAll(expected));
        Assert.assertEquals(1, totalPatterns);
    }
}
